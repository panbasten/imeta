/* Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.*/

package com.panet.imeta.job.entries.ftp;

import static com.panet.imeta.job.entry.validator.AndValidator.putValidators;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.andValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.fileExistsValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.notBlankValidator;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.notNullValidator;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.vfs.FileObject;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPFile;
import com.enterprisedt.net.ftp.FTPFileFactory;
import com.enterprisedt.net.ftp.FTPFileParser;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.ResultFile;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.encryption.Encr;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.core.util.StringUtil;
import com.panet.imeta.core.variables.VariableSpace;
import com.panet.imeta.core.vfs.KettleVFS;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.job.Job;
import com.panet.imeta.job.JobEntryType;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryBase;
import com.panet.imeta.job.entry.JobEntryInterface;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.resource.ResourceEntry;
import com.panet.imeta.resource.ResourceReference;
import com.panet.imeta.resource.ResourceEntry.ResourceType;
import com.panet.imeta.shared.SharedObjectInterface;
/**
 * This defines an FTP job entry.
 *
 * @author Matt
 * @since 05-11-2003
 *
 */

public class JobEntryFTP extends JobEntryBase implements Cloneable, JobEntryInterface
{
	private static Logger log4j = Logger.getLogger(JobEntryFTP.class);
	
	private String serverName;
	private String userName;
	private String password;
	private String ftpDirectory;
	private String targetDirectory;
	private String wildcard;
	private boolean binaryMode;
	private int     timeout;
	private boolean remove;
    private boolean onlyGettingNewFiles;  /* Don't overwrite files */
    private boolean activeConnection;
    private String  controlEncoding;      /* how to convert list of filenames e.g. */
    
    
    /**
     * Implicit encoding used before PDI v2.4.1
     */
    static private String LEGACY_CONTROL_ENCODING = "US-ASCII";
    
    /**
     * Default encoding when making a new ftp job entry instance.
     */
    static private String DEFAULT_CONTROL_ENCODING = "ISO-8859-1";    
    
    private boolean movefiles;
    private String movetodirectory;
    
	private boolean adddate;
	private boolean addtime;
	private boolean SpecifyFormat;
	private String date_time_format;
	private boolean AddDateBeforeExtension;
	private boolean isaddresult;
    private boolean createmovefolder;
    private String port;
    private String proxyHost;
     
    private String proxyPort;    /* string to allow variable substitution */
     
    private String proxyUsername;
     
    private String proxyPassword;
    
    private String socksProxyHost;
    private String socksProxyPort;
    private String socksProxyUsername;
    private String socksProxyPassword;
    
	public int ifFileExistsSkip=0;
	public String SifFileExistsSkip="ifFileExistsSkip";
	
	public int ifFileExistsCreateUniq=1;
	public String SifFileExistsCreateUniq="ifFileExistsCreateUniq";
	
	public int ifFileExistsFail=2;
	public String SifFileExistsFail="ifFileExistsFail";
	
	public int ifFileExists;
	public String SifFileExists;
	
	public  String SUCCESS_IF_AT_LEAST_X_FILES_DOWNLOADED="success_when_at_least";
	public  String SUCCESS_IF_ERRORS_LESS="success_if_errors_less";
	public  String SUCCESS_IF_NO_ERRORS="success_if_no_errors";
	
	private String nr_limit;
	private String success_condition;
	
	
	long NrErrors=0;
	long NrfilesRetrieved=0;
	boolean successConditionBroken=false;
	int limitFiles=0;
	
	String targetFilename =null;
	
	static String FILE_SEPARATOR="/";
	
	public JobEntryFTP(String n)
	{
		super(n, "");
		nr_limit="10";
		port="21";
		socksProxyPort="1080";
		success_condition=SUCCESS_IF_NO_ERRORS;
		ifFileExists=ifFileExistsSkip;
		SifFileExists=SifFileExistsSkip;
		
		serverName=null;
		movefiles=false;
		movetodirectory=null;
		adddate=false;
		addtime=false;
		SpecifyFormat=false;
		AddDateBeforeExtension=false;
		isaddresult=true;
		createmovefolder=false;
		
		setID(-1L);
	    setJobEntryType(JobEntryType.FTP);
		setControlEncoding(DEFAULT_CONTROL_ENCODING);
	}

	public JobEntryFTP()
	{
		this("");
	}

	public JobEntryFTP(JobEntryBase jeb)
	{
		super(jeb);
	}

    public Object clone()
    {
        JobEntryFTP je = (JobEntryFTP) super.clone();
        return je;
    }
    
	public String getXML()
	{
        StringBuffer retval = new StringBuffer(128);
		
		retval.append(super.getXML());
		retval.append("      ").append(XMLHandler.addTagValue("port",   port));
		retval.append("      ").append(XMLHandler.addTagValue("servername",   serverName));
		retval.append("      ").append(XMLHandler.addTagValue("username",     userName));
	    retval.append("      ").append(XMLHandler.addTagValue("password", Encr.encryptPasswordIfNotUsingVariables(password)));
		retval.append("      ").append(XMLHandler.addTagValue("ftpdirectory", ftpDirectory));
		retval.append("      ").append(XMLHandler.addTagValue("targetdirectory", targetDirectory));
		retval.append("      ").append(XMLHandler.addTagValue("wildcard",     wildcard));
		retval.append("      ").append(XMLHandler.addTagValue("binary",       binaryMode));
		retval.append("      ").append(XMLHandler.addTagValue("timeout",      timeout));
		retval.append("      ").append(XMLHandler.addTagValue("remove",       remove));
        retval.append("      ").append(XMLHandler.addTagValue("only_new",     onlyGettingNewFiles));
        retval.append("      ").append(XMLHandler.addTagValue("active",       activeConnection));
        retval.append("      ").append(XMLHandler.addTagValue("control_encoding",  controlEncoding));
        retval.append("      ").append(XMLHandler.addTagValue("movefiles",       movefiles));
        retval.append("      ").append(XMLHandler.addTagValue("movetodirectory",     movetodirectory));
        
		retval.append("      ").append(XMLHandler.addTagValue("adddate",  adddate));
		retval.append("      ").append(XMLHandler.addTagValue("addtime",  addtime));
		retval.append("      ").append(XMLHandler.addTagValue("SpecifyFormat",  SpecifyFormat));
		retval.append("      ").append(XMLHandler.addTagValue("date_time_format",  date_time_format));
		retval.append("      ").append(XMLHandler.addTagValue("AddDateBeforeExtension",  AddDateBeforeExtension));
		retval.append("      ").append(XMLHandler.addTagValue("isaddresult",  isaddresult));
		retval.append("      ").append(XMLHandler.addTagValue("createmovefolder",  createmovefolder));
		
	    retval.append("      ").append(XMLHandler.addTagValue("proxy_host", proxyHost)); //$NON-NLS-1$ //$NON-NLS-2$
	    retval.append("      ").append(XMLHandler.addTagValue("proxy_port", proxyPort)); //$NON-NLS-1$ //$NON-NLS-2$
	    retval.append("      ").append(XMLHandler.addTagValue("proxy_username", proxyUsername)); //$NON-NLS-1$ //$NON-NLS-2$
	    retval.append("      ").append(XMLHandler.addTagValue("proxy_password", Encr.encryptPasswordIfNotUsingVariables(proxyPassword))); //$NON-NLS-1$ //$NON-NLS-2$
        retval.append("      ").append(XMLHandler.addTagValue("socksproxy_host", socksProxyHost)); //$NON-NLS-1$ //$NON-NLS-2$
        retval.append("      ").append(XMLHandler.addTagValue("socksproxy_port", socksProxyPort)); //$NON-NLS-1$ //$NON-NLS-2$
        retval.append("      ").append(XMLHandler.addTagValue("socksproxy_username", socksProxyUsername)); //$NON-NLS-1$ //$NON-NLS-2$
        retval.append("      ").append(XMLHandler.addTagValue("socksproxy_password", Encr.encryptPasswordIfNotUsingVariables(socksProxyPassword))); //$NON-NLS-1$ //$NON-NLS-2$
	    
	    retval.append("      ").append(XMLHandler.addTagValue("ifFileExists", SifFileExists));
	    
		retval.append("      ").append(XMLHandler.addTagValue("nr_limit", nr_limit));
		retval.append("      ").append(XMLHandler.addTagValue("success_condition", success_condition));
	    
		return retval.toString();
	}
	
	  public void loadXML(Node entrynode, List<DatabaseMeta> databases, List<SlaveServer> slaveServers, Repository rep) throws KettleXMLException
	  {
	    try
	    {
	      super.loadXML(entrynode, databases, slaveServers);
	      	port          = XMLHandler.getTagValue(entrynode, "port");
			serverName          = XMLHandler.getTagValue(entrynode, "servername");
			userName            = XMLHandler.getTagValue(entrynode, "username");
		    password = Encr.decryptPasswordOptionallyEncrypted(XMLHandler.getTagValue(entrynode, "password")); 
			ftpDirectory        = XMLHandler.getTagValue(entrynode, "ftpdirectory");
			targetDirectory     = XMLHandler.getTagValue(entrynode, "targetdirectory");
			wildcard            = XMLHandler.getTagValue(entrynode, "wildcard");
			binaryMode          = "Y".equalsIgnoreCase( XMLHandler.getTagValue(entrynode, "binary") );
			timeout             = Const.toInt(XMLHandler.getTagValue(entrynode, "timeout"), 10000);
			remove              = "Y".equalsIgnoreCase( XMLHandler.getTagValue(entrynode, "remove") );
            onlyGettingNewFiles = "Y".equalsIgnoreCase( XMLHandler.getTagValue(entrynode, "only_new") );
            activeConnection    = "Y".equalsIgnoreCase( XMLHandler.getTagValue(entrynode, "active") );
            controlEncoding     = XMLHandler.getTagValue(entrynode, "control_encoding");
            if ( controlEncoding == null )
            {
            	// if we couldn't retrieve an encoding, assume it's an old instance and
            	// put in the the encoding used before v 2.4.0
            	controlEncoding = LEGACY_CONTROL_ENCODING;
            }     
            movefiles              = "Y".equalsIgnoreCase( XMLHandler.getTagValue(entrynode, "movefiles") );
            movetodirectory            = XMLHandler.getTagValue(entrynode, "movetodirectory");
            
			adddate = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode, "adddate"));	
			addtime = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode, "addtime"));	
			SpecifyFormat = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode, "SpecifyFormat"));	
			date_time_format = XMLHandler.getTagValue(entrynode, "date_time_format");
			AddDateBeforeExtension = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode, "AddDateBeforeExtension"));	
			
			String addresult = XMLHandler.getTagValue(entrynode, "isaddresult");	
			
			if(Const.isEmpty(addresult)) 
				isaddresult = true;
			else
				isaddresult = "Y".equalsIgnoreCase(addresult);
			
			createmovefolder = "Y".equalsIgnoreCase(XMLHandler.getTagValue(entrynode, "createmovefolder"));
			
		    proxyHost = XMLHandler.getTagValue(entrynode, "proxy_host"); //$NON-NLS-1$
		    proxyPort = XMLHandler.getTagValue(entrynode, "proxy_port"); //$NON-NLS-1$
		    proxyUsername = XMLHandler.getTagValue(entrynode, "proxy_username"); //$NON-NLS-1$
		    proxyPassword = Encr.decryptPasswordOptionallyEncrypted(XMLHandler.getTagValue(entrynode, "proxy_password")); //$NON-NLS-1$
		    socksProxyHost = XMLHandler.getTagValue(entrynode, "socksproxy_host"); //$NON-NLS-1$
		    socksProxyPort = XMLHandler.getTagValue(entrynode, "socksproxy_port"); //$NON-NLS-1$
            socksProxyUsername = XMLHandler.getTagValue(entrynode, "socksproxy_username"); //$NON-NLS-1$
            socksProxyPassword = Encr.decryptPasswordOptionallyEncrypted(XMLHandler.getTagValue(entrynode, "socksproxy_password")); //$NON-NLS-1$     
		    SifFileExists=XMLHandler.getTagValue(entrynode, "ifFileExists"); 
		    if(Const.isEmpty(SifFileExists))
		    {
		    	ifFileExists=ifFileExistsSkip;
		    }else
		    {
			    if(SifFileExists.equals(SifFileExistsCreateUniq))
			    	ifFileExists=ifFileExistsCreateUniq;
			    else if(SifFileExists.equals(SifFileExistsFail))
			    	ifFileExists=ifFileExistsFail;
			    else
			    	ifFileExists=ifFileExistsSkip;
			    
			    
		    }
		    nr_limit          = XMLHandler.getTagValue(entrynode, "nr_limit");
			success_condition = Const.NVL(XMLHandler.getTagValue(entrynode, "success_condition"),SUCCESS_IF_NO_ERRORS);
		      
		}
		catch(KettleXMLException xe)
		{
			throw new KettleXMLException("Unable to load job entry of type 'ftp' from XML node", xe);
		}
	}

	  public void setInfo(Map<String, String[]> p, String id,
			  List<? extends SharedObjectInterface> databases) {
		  port = JobEntryBase.parameterToString(p.get(id + ".port"));
		  serverName = JobEntryBase.parameterToString(p.get(id + ".serverName"));
		  userName = JobEntryBase.parameterToString(p.get(id + ".userName"));
		  password = Encr.decryptPasswordOptionallyEncrypted(JobEntryBase.parameterToString(p.get(id + ".password")));
		  ftpDirectory = JobEntryBase.parameterToString(p.get(id + ".ftpDirectory"));
		  targetDirectory = JobEntryBase.parameterToString(p.get(id + ".targetDirectory"));
		  wildcard = JobEntryBase.parameterToString(p.get(id + ".wildcard"));
		  binaryMode = JobEntryBase.parameterToBoolean(p.get(id + ".binaryMode"));	
		  timeout = JobEntryBase.parameterToInt(p.get(id + ".timeout"));
		  remove = JobEntryBase.parameterToBoolean(p.get(id + ".remove"));	
		  onlyGettingNewFiles = JobEntryBase.parameterToBoolean(p.get(id + ".onlyGettingNewFiles"));	
		  activeConnection = JobEntryBase.parameterToBoolean(p.get(id + ".activeConnection"));
		  controlEncoding = JobEntryBase.parameterToString(p.get(id + ".controlEncoding"));
		  movefiles = JobEntryBase.parameterToBoolean(p.get(id + ".movefiles"));
		  movetodirectory = JobEntryBase.parameterToString(p.get(id + ".movetodirectory"));
		  adddate = JobEntryBase.parameterToBoolean(p.get(id + ".adddate"));
		  addtime = JobEntryBase.parameterToBoolean(p.get(id + ".addtime"));	
		  SpecifyFormat = JobEntryBase.parameterToBoolean(p.get(id + ".SpecifyFormat"));
		  date_time_format = JobEntryBase.parameterToString(p.get(id + ".date_time_format"));
		  AddDateBeforeExtension = JobEntryBase.parameterToBoolean(p.get(id + ".AddDateBeforeExtension"));
		  createmovefolder = JobEntryBase.parameterToBoolean(p.get(id + ".createmovefolder"));
		  proxyHost = JobEntryBase.parameterToString(p.get(id + ".proxyHost"));
		  proxyPort = JobEntryBase.parameterToString(p.get(id + ".proxyPort"));
		  proxyUsername = JobEntryBase.parameterToString(p.get(id + ".proxyUsername"));
		  proxyPassword = Encr.decryptPasswordOptionallyEncrypted(JobEntryBase.parameterToString(p.get(id + ".proxyPassword")));
		  SifFileExists = JobEntryBase.parameterToString(p.get(id + ".SifFileExists"));
		  nr_limit = JobEntryBase.parameterToString(p.get(id + ".nr_limit"));
		  success_condition = JobEntryBase.parameterToString(p.get(id + ".success_condition"));
		  isaddresult = JobEntryBase.parameterToBoolean(p.get(id + ".isaddresult"));
		  
		  socksProxyHost = JobEntryBase.parameterToString(p.get(id + ".socksProxyHost"));
          socksProxyPort = JobEntryBase.parameterToString(p.get(id + ".socksProxyPort"));
          socksProxyUsername = JobEntryBase.parameterToString(p.get(id + ".socksProxyUsername"));
          socksProxyPassword = Encr.decryptPasswordOptionallyEncrypted(JobEntryBase.parameterToString(p.get(id + ".socksProxyPassword")));
		}
	  
	  public void loadRep(Repository rep, long id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers) throws KettleException
	  {
	    try
	    {
	      super.loadRep(rep, id_jobentry, databases, slaveServers);
	      	port          = rep.getJobEntryAttributeString(id_jobentry, "port");
			serverName          = rep.getJobEntryAttributeString(id_jobentry, "servername");
			userName            = rep.getJobEntryAttributeString(id_jobentry, "username");
			password = Encr.decryptPasswordOptionallyEncrypted( rep.getJobEntryAttributeString(id_jobentry, "password") );
			ftpDirectory        = rep.getJobEntryAttributeString(id_jobentry, "ftpdirectory");
			targetDirectory     = rep.getJobEntryAttributeString(id_jobentry, "targetdirectory");
			wildcard            = rep.getJobEntryAttributeString(id_jobentry, "wildcard");
			binaryMode          = rep.getJobEntryAttributeBoolean(id_jobentry, "binary");
			timeout             = (int)rep.getJobEntryAttributeInteger(id_jobentry, "timeout");
            remove              = rep.getJobEntryAttributeBoolean(id_jobentry, "remove");
			onlyGettingNewFiles = rep.getJobEntryAttributeBoolean(id_jobentry, "only_new");
            activeConnection    = rep.getJobEntryAttributeBoolean(id_jobentry, "active");
            controlEncoding     = rep.getJobEntryAttributeString(id_jobentry, "control_encoding");
            if ( controlEncoding == null )
            {
            	// if we couldn't retrieve an encoding, assume it's an old instance and
            	// put in the the encoding used before v 2.4.0
            	controlEncoding = LEGACY_CONTROL_ENCODING;
            }
            
            movefiles              = rep.getJobEntryAttributeBoolean(id_jobentry, "movefiles");
            movetodirectory            = rep.getJobEntryAttributeString(id_jobentry, "movetodirectory");
            
			adddate=rep.getJobEntryAttributeBoolean(id_jobentry, "adddate");
			addtime=rep.getJobEntryAttributeBoolean(id_jobentry, "adddate");
			SpecifyFormat=rep.getJobEntryAttributeBoolean(id_jobentry, "SpecifyFormat");
			date_time_format = rep.getJobEntryAttributeString(id_jobentry, "date_time_format");
			AddDateBeforeExtension=rep.getJobEntryAttributeBoolean(id_jobentry, "AddDateBeforeExtension");
			
		   String addToResult=rep.getStepAttributeString (id_jobentry, "add_to_result_filenames");
			if(Const.isEmpty(addToResult)) 
				isaddresult = true;
			else
				isaddresult =  rep.getStepAttributeBoolean(id_jobentry, "add_to_result_filenames");
			
			createmovefolder=rep.getJobEntryAttributeBoolean(id_jobentry, "createmovefolder");
			
		    proxyHost	= rep.getJobEntryAttributeString(id_jobentry, "proxy_host"); //$NON-NLS-1$
		    proxyPort	= rep.getJobEntryAttributeString(id_jobentry, "proxy_port"); //$NON-NLS-1$
		    proxyUsername	= rep.getJobEntryAttributeString(id_jobentry, "proxy_username"); //$NON-NLS-1$
		    proxyPassword = Encr.decryptPasswordOptionallyEncrypted(rep.getJobEntryAttributeString(id_jobentry, "proxy_password")); //$NON-NLS-1$
		    socksProxyHost  = rep.getJobEntryAttributeString(id_jobentry, "socksproxy_host"); //$NON-NLS-1$
            socksProxyPort   = rep.getJobEntryAttributeString(id_jobentry, "socksproxy_port"); //$NON-NLS-1$
            socksProxyUsername   = rep.getJobEntryAttributeString(id_jobentry, "socksproxy_username"); //$NON-NLS-1$
            socksProxyPassword = Encr.decryptPasswordOptionallyEncrypted(rep.getJobEntryAttributeString(id_jobentry, "socksproxy_password")); //$NON-NLS-1$
		    SifFileExists = rep.getJobEntryAttributeString(id_jobentry, "ifFileExists");
		    if(Const.isEmpty(SifFileExists))
		    {
		    	ifFileExists=ifFileExistsSkip;
		    }else
		    {
			    if(SifFileExists.equals(SifFileExistsCreateUniq))
			    	ifFileExists=ifFileExistsCreateUniq;
			    else if(SifFileExists.equals(SifFileExistsFail))
			    	ifFileExists=ifFileExistsFail;
			    else
			    	ifFileExists=ifFileExistsSkip;
		    }
		    nr_limit  = rep.getJobEntryAttributeString(id_jobentry, "nr_limit");
			success_condition  = Const.NVL(rep.getJobEntryAttributeString(id_jobentry, "success_condition"), SUCCESS_IF_NO_ERRORS);
			
		}
		catch(KettleException dbe)
		{
			throw new KettleException("Unable to load job entry of type 'ftp' from the repository for id_jobentry="+id_jobentry, dbe);
		}
	}
	
	public void saveRep(Repository rep, long id_job)
		throws KettleException
	{
		try
		{
			super.saveRep(rep, id_job);
			rep.saveJobEntryAttribute(id_job, getID(), "port",      port);
			rep.saveJobEntryAttribute(id_job, getID(), "servername",      serverName);
			rep.saveJobEntryAttribute(id_job, getID(), "username",        userName);
			rep.saveJobEntryAttribute(id_job, getID(), "password", Encr.encryptPasswordIfNotUsingVariables(password));
			rep.saveJobEntryAttribute(id_job, getID(), "ftpdirectory",    ftpDirectory);
			rep.saveJobEntryAttribute(id_job, getID(), "targetdirectory", targetDirectory);
			rep.saveJobEntryAttribute(id_job, getID(), "wildcard",        wildcard);
			rep.saveJobEntryAttribute(id_job, getID(), "binary",          binaryMode);
			rep.saveJobEntryAttribute(id_job, getID(), "timeout",         timeout);
            rep.saveJobEntryAttribute(id_job, getID(), "remove",          remove);
			rep.saveJobEntryAttribute(id_job, getID(), "only_new",        onlyGettingNewFiles);
            rep.saveJobEntryAttribute(id_job, getID(), "active",          activeConnection);
            rep.saveJobEntryAttribute(id_job, getID(), "control_encoding",controlEncoding);
            
            rep.saveJobEntryAttribute(id_job, getID(), "movefiles",          movefiles);
            rep.saveJobEntryAttribute(id_job, getID(), "movetodirectory",        movetodirectory);
            
			rep.saveJobEntryAttribute(id_job, getID(), "addtime", addtime);
			rep.saveJobEntryAttribute(id_job, getID(), "adddate", adddate);
			rep.saveJobEntryAttribute(id_job, getID(), "SpecifyFormat", SpecifyFormat);
			rep.saveJobEntryAttribute(id_job, getID(), "date_time_format", date_time_format);
			
			rep.saveJobEntryAttribute(id_job, getID(), "AddDateBeforeExtension", AddDateBeforeExtension);
			rep.saveJobEntryAttribute(id_job, getID(), "isaddresult", isaddresult);
			rep.saveJobEntryAttribute(id_job, getID(), "createmovefolder", createmovefolder);
			
		    rep.saveJobEntryAttribute(id_job, getID(), "proxy_host", proxyHost); //$NON-NLS-1$
		    rep.saveJobEntryAttribute(id_job, getID(), "proxy_port", proxyPort); //$NON-NLS-1$
		    rep.saveJobEntryAttribute(id_job, getID(), "proxy_username", proxyUsername); //$NON-NLS-1$
		    rep.saveJobEntryAttribute(id_job, getID(), "proxy_password", Encr.encryptPasswordIfNotUsingVariables(proxyPassword)); //$NON-NLS-1$
		    rep.saveJobEntryAttribute(id_job, getID(), "socksproxy_host", socksProxyHost); //$NON-NLS-1$
            rep.saveJobEntryAttribute(id_job, getID(), "socksproxy_port", socksProxyPort); //$NON-NLS-1$
            rep.saveJobEntryAttribute(id_job, getID(), "socksproxy_username", socksProxyUsername); //$NON-NLS-1$
            rep.saveJobEntryAttribute(id_job, getID(), "socksproxy_password", Encr.encryptPasswordIfNotUsingVariables(socksProxyPassword)); //$NON-NLS-1$
		    rep.saveJobEntryAttribute(id_job, getID(), "ifFileExists", SifFileExists);
		    
			rep.saveJobEntryAttribute(id_job, getID(), "nr_limit",  nr_limit);
			rep.saveJobEntryAttribute(id_job, getID(), "success_condition",    success_condition);
			
		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleException("Unable to save job entry of type 'ftp' to the repository for id_job="+id_job, dbe);
		}
	}

	
	public void setLimit(String nr_limitin)
	{
		this.nr_limit=nr_limitin;
	}
	
	public String getLimit()
	{
		return nr_limit;
	}
	
	
	public void setSuccessCondition(String success_condition)
	{
		this.success_condition=success_condition;
	}
	public String getSuccessCondition()
	{
		return success_condition;
	}
	
	
	public void setCreateMoveFolder(boolean createmovefolderin)
	{
		this.createmovefolder=createmovefolderin;
	}
	
	public boolean isCreateMoveFolder()
	{
		return createmovefolder;
	}
	
	public void setAddDateBeforeExtension(boolean AddDateBeforeExtension)
   {
   	this.AddDateBeforeExtension=AddDateBeforeExtension;
   }
 
	public boolean isAddDateBeforeExtension()
	{
		return AddDateBeforeExtension;
	}
	
	public void setAddToResult(boolean isaddresultin)
   {
		this.isaddresult=isaddresultin;
   }
	 
	public boolean isAddToResult()
	{
		return isaddresult;
	}

	public void setDateInFilename(boolean adddate) 
	{
		this.adddate= adddate;
	}
	
	public boolean isDateInFilename() 
	{
		return adddate;
	}
	
	public void setTimeInFilename(boolean addtime) 
	{
		this.addtime= addtime;
	}
	public boolean isTimeInFilename() 
	{
		return addtime;
	}
	 public boolean  isSpecifyFormat()
	 {
	   	return SpecifyFormat;
	 }
	 public void setSpecifyFormat(boolean SpecifyFormat)
	 {
	   	this.SpecifyFormat=SpecifyFormat;
	 }
	 public String getDateTimeFormat()
	 {
		return date_time_format;
	 }
	 public void setDateTimeFormat(String date_time_format)
	 {
		this.date_time_format=date_time_format;
	 }
	
	/**
	 * @return Returns the movefiles.
	 */
	public boolean isMoveFiles()
	{
		return movefiles;
	}
	/**
	 * @param movefilesin The movefiles to set.
	 */
	public void setMoveFiles(boolean movefilesin)
	{
		this.movefiles=movefilesin;
	}
	
	
	/**
	 * @return Returns the movetodirectory.
	 */
	public String getMoveToDirectory()
	{
		return movetodirectory;
	}
	
	/**
	 * @param movetoin The movetodirectory to set.
	 */
	public void setMoveToDirectory(String movetoin)
	{
		this.movetodirectory=movetoin;
	}
	
	/**
	 * @return Returns the binaryMode.
	 */
	public boolean isBinaryMode()
	{
		return binaryMode;
	}

	/**
	 * @param binaryMode The binaryMode to set.
	 */
	public void setBinaryMode(boolean binaryMode)
	{
		this.binaryMode = binaryMode;
	}

	/**
	 * @return Returns the directory.
	 */
	public String getFtpDirectory()
	{
		return ftpDirectory;
	}

	/**
	 * @param directory The directory to set.
	 */
	public void setFtpDirectory(String directory)
	{
		this.ftpDirectory = directory;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * @return Returns the serverName.
	 */
	public String getServerName()
	{
		return serverName;
	}

	/**
	 * @param serverName The serverName to set.
	 */
	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}

	/**
	 * @return Returns the port.
	 */
	public String getPort()
	{
		return port;
	}

	/**
	 * @param port The port to set.
	 */
	public void setPort(String port)
	{
		this.port = port;
	}

	
	
	/**
	 * @return Returns the userName.
	 */
	public String getUserName()
	{
		return userName;
	}

	/**
	 * @param userName The userName to set.
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	/**
	 * @return Returns the wildcard.
	 */
	public String getWildcard()
	{
		return wildcard;
	}

	/**
	 * @param wildcard The wildcard to set.
	 */
	public void setWildcard(String wildcard)
	{
		this.wildcard = wildcard;
	}

	/**
	 * @return Returns the targetDirectory.
	 */
	public String getTargetDirectory()
	{
		return targetDirectory;
	}

	/**
	 * @param targetDirectory The targetDirectory to set.
	 */
	public void setTargetDirectory(String targetDirectory)
	{
		this.targetDirectory = targetDirectory;
	}

	/**
	 * @param timeout The timeout to set.
	 */
	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	/**
	 * @return Returns the timeout.
	 */
	public int getTimeout()
	{
		return timeout;
	}

	/**
	 * @param remove The remove to set.
	 */
	public void setRemove(boolean remove)
	{
		this.remove = remove;
	}

	/**
	 * @return Returns the remove.
	 */
	public boolean getRemove()
	{
		return remove;
	}

    /**
     * @return Returns the onlyGettingNewFiles.
     */
    public boolean isOnlyGettingNewFiles()
    {
        return onlyGettingNewFiles;
    }

    /**
     * @param onlyGettingNewFiles The onlyGettingNewFiles to set.
     */
    public void setOnlyGettingNewFiles(boolean onlyGettingNewFilesin)
    {
        this.onlyGettingNewFiles = onlyGettingNewFilesin;
    }
    
    /**
     * Get the control encoding to be used for ftp'ing
     * 
     * @return the used encoding
     */
    public String getControlEncoding()
    {
        return controlEncoding;
    }
    
    /**
     *  Set the encoding to be used for ftp'ing. This determines how
     *  names are translated in dir e.g. It does impact the contents
     *  of the files being ftp'ed.
     *  
     *  @param encoding The encoding to be used.
     */
    public void setControlEncoding(String encoding)
    {
    	this.controlEncoding = encoding;
    }    
	

    /**
     * @return Returns the hostname of the ftp-proxy.
     */
    public String getProxyHost() 
    {
    	return proxyHost;
    }
      
    /**
     * @param proxyHost The hostname of the proxy.
     */
    public void setProxyHost(String proxyHost) 
    {
     	this.proxyHost = proxyHost;
    }
    
    /**
     * @param proxyPassword The password which is used to authenticate at the socks proxy.
     */
    public void setProxyPassword(String proxyPassword) 
    {
        this.proxyPassword = proxyPassword;
    }
    
    /**
     * @return Returns the password which is used to authenticate at the proxy.
     */
    public String getProxyPassword() 
    {
     	return proxyPassword;
    }
    
    /**
     * @param proxyPassword The password which is used to authenticate at the proxy.
     */
    public void setSocksProxyPassword(String socksProxyPassword) 
    {
     	this.socksProxyPassword = socksProxyPassword;
    }

    /**
     * @return Returns the password which is used to authenticate at the socks proxy.
     */
    public String getSocksProxyPassword() 
    {
        return socksProxyPassword;
    }

    /**
     * @param proxyPort The port of the ftp-proxy. 
     */
    public void setProxyPort(String proxyPort) 
    {
      this.proxyPort = proxyPort;
    }
    
    /**
     * @return Returns the port of the ftp-proxy.
     */
    public String getProxyPort() 
    {
      return proxyPort;
    }
      
    /**
     * @return Returns the username which is used to authenticate at the proxy.
     */
    public String getProxyUsername() {
      return proxyUsername;
    }
      
    /**
     * @param proxyUsername The username which is used to authenticate at the proxy.
     */
    public void setProxyUsername(String proxyUsername) {
    	this.proxyUsername = proxyUsername;
    }
    
    /**
     * @return Returns the username which is used to authenticate at the socks proxy.
     */
    public String getSocksProxyUsername() {
      return socksProxyUsername;
    }
      
    /**
     * @param proxyUsername The username which is used to authenticate at the socks proxy.
     */
    public void setSocksProxyUsername(String socksPoxyUsername) {
        this.socksProxyUsername = socksPoxyUsername;
    }

    /**
     * 
     * @param socksProxyHost The host name of the socks proxy host
     */
    public void setSocksProxyHost(String socksProxyHost) {
        this.socksProxyHost = socksProxyHost;
    }
    
    /**
     * @return The host name of the socks proxy host
     */
    public String getSocksProxyHost() {
        return this.socksProxyHost;
    }
    
    /**
     * @param socksProxyPort The port number the socks proxy host is using
     */
    public void setSocksProxyPort(String socksProxyPort) {
        this.socksProxyPort = socksProxyPort;
    }
    
    /**
     * @return The port number the socks proxy host is using
     */
    public String getSocksProxyPort() {
        return this.socksProxyPort;
    }
    
	public Result execute(Result previousResult, int nr, Repository rep, Job parentJob)
	{
		LogWriter log = LogWriter.getInstance();
		log4j.info(Messages.getString("JobEntryFTP.Started", serverName)); //$NON-NLS-1$
		
		Result result = previousResult;
		result.setNrErrors(1);
		result.setResult( false );
		NrErrors = 0;
		NrfilesRetrieved=0;
		successConditionBroken=false;
		boolean exitjobentry=false;
		limitFiles=Const.toInt(environmentSubstitute(getLimit()),10);

		
		// Here let's put some controls before stating the job
		if(movefiles)
		{
			if(Const.isEmpty(movetodirectory))
			{
				log.logError(toString(), Messages.getString("JobEntryFTP.MoveToFolderEmpty"));
				return result;
			}
				
		}
		
		if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryFTP.Start")); //$NON-NLS-1$

        FTPClient ftpclient=null;
        String realMoveToFolder=null;
        
		try
		{
			// Create ftp client to host:port ...
			ftpclient = new FTPClient();
            String realServername = environmentSubstitute(serverName);
            String realServerPort = environmentSubstitute(port);
            ftpclient.setRemoteAddr(InetAddress.getByName(realServername));
            if(!Const.isEmpty(realServerPort))
            {
            	 ftpclient.setRemotePort(Const.toInt(realServerPort, 21));
            }

            if (!Const.isEmpty(proxyHost)) 
            {
          	  String realProxy_host = environmentSubstitute(proxyHost);
          	  ftpclient.setRemoteAddr(InetAddress.getByName(realProxy_host));
          	  if ( log.isDetailed() )
          	      log.logDetailed(toString(), Messages.getString("JobEntryFTP.OpenedProxyConnectionOn",realProxy_host));

          	  // FIXME: Proper default port for proxy    	  
          	  int port = Const.toInt(environmentSubstitute(proxyPort), 21);
          	  if (port != 0) 
          	  {
          	     ftpclient.setRemotePort(port);
          	  }
            } 
            else 
            {
                ftpclient.setRemoteAddr(InetAddress.getByName(realServername));
                
                if ( log.isDetailed() )
          	      log.logDetailed(toString(), Messages.getString("JobEntryFTP.OpenedConnectionTo",realServername));                
            }
            
            
			// set activeConnection connectmode ...
            if (activeConnection){
                ftpclient.setConnectMode(FTPConnectMode.ACTIVE);
                if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryFTP.SetActive")); //$NON-NLS-1$
            }
            else{
                ftpclient.setConnectMode(FTPConnectMode.PASV);
                if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryFTP.SetPassive")); //$NON-NLS-1$
            }
			
			// Set the timeout
			ftpclient.setTimeout(timeout);
		      if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryFTP.SetTimeout", String.valueOf(timeout))); //$NON-NLS-1$
			
			ftpclient.setControlEncoding(controlEncoding);
		      if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryFTP.SetEncoding", controlEncoding)); //$NON-NLS-1$
	            
		    //  If socks proxy server was provided
		    if (!Const.isEmpty(socksProxyHost)) {  
		        if (!Const.isEmpty(socksProxyPort)) {
		           FTPClient.initSOCKS(environmentSubstitute(socksProxyPort), environmentSubstitute(socksProxyHost));
		       }
		       else {
		           throw new FTPException(Messages.getString("JobEntryFTP.SocksProxy.PortMissingException", environmentSubstitute(socksProxyHost), getName()));
		       }
		       //  then if we have authentication information
		       if (!Const.isEmpty(socksProxyUsername) && !Const.isEmpty(socksProxyPassword)) {
		           FTPClient.initSOCKSAuthentication(environmentSubstitute(socksProxyUsername), environmentSubstitute(socksProxyPassword));
		       }
		       else if (    !Const.isEmpty(socksProxyUsername) && Const.isEmpty(socksProxyPassword)
		                 || Const.isEmpty(socksProxyUsername) && !Const.isEmpty(socksProxyPassword)) {
		            //  we have a username without a password or vica versa
		           throw new FTPException(Messages.getString("JobEntryFTP.SocksProxy.IncompleteCredentials", environmentSubstitute(socksProxyHost), getName()));
		       }
		    }
		    
			// login to ftp host ...
            ftpclient.connect();
			
            String realUsername = environmentSubstitute(userName) +
            (!Const.isEmpty(proxyHost) ? "@" + realServername : "") + 
            (!Const.isEmpty(proxyUsername) ? " " + environmentSubstitute(proxyUsername) 
        		                           : ""); 
	            
            String realPassword = Encr.decryptPasswordOptionallyEncrypted(environmentSubstitute(password)) + 
            (!Const.isEmpty(proxyPassword) ? " " + Encr.decryptPasswordOptionallyEncrypted(environmentSubstitute(proxyPassword)) : "" );
            
            
            ftpclient.login(realUsername, realPassword);
			//  Remove password from logging, you don't know where it ends up.
			if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryFTP.LoggedIn", realUsername)); //$NON-NLS-1$

			// Fix for PDI-2534 - add auxilliary FTP File List parsers to the ftpclient object.
			this.hookInOtherParsers(log, ftpclient);

			// move to spool dir ...
			if (!Const.isEmpty(ftpDirectory)) {
                String realFtpDirectory = environmentSubstitute(ftpDirectory);
                realFtpDirectory=normalizePath(realFtpDirectory);
                ftpclient.chdir(realFtpDirectory);
                if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryFTP.ChangedDir", realFtpDirectory)); //$NON-NLS-1$
			}	

			//Create move to folder if necessary
			if(movefiles && !Const.isEmpty(movetodirectory)) {
				realMoveToFolder=environmentSubstitute(movetodirectory);
				realMoveToFolder=normalizePath(realMoveToFolder);
				// Folder exists?
				boolean folderExist=true;
				if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryFTP.CheckMoveToFolder",realMoveToFolder));
				String originalLocation = ftpclient.pwd();
				try{
					// does not work for folders, see PDI-2567: folderExist=ftpclient.exists(realMoveToFolder);
					// try switching to the 'move to' folder.
				    ftpclient.chdir(realMoveToFolder);
					// Switch back to the previous location.
				    if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryFTP.CheckMoveToFolderSwitchBack",originalLocation));
					ftpclient.chdir(originalLocation);				    
				}
				catch (Exception e){
					folderExist=false; 
					// Assume folder does not exist !!
				}
				
				if(!folderExist){
					if(createmovefolder){
						ftpclient.mkdir(realMoveToFolder);
						if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryFTP.MoveToFolderCreated",realMoveToFolder));
					}else{
						log.logError(toString(),Messages.getString("JobEntryFTP.MoveToFolderNotExist"));
						exitjobentry=true;
						NrErrors++;
					}
				}
			}
			
			if(!exitjobentry)
			{
				// Get all the files in the current directory...
				FTPFile[] ftpFiles = ftpclient.dirDetails(null);
				
			    //if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryFTP.FoundNFiles", String.valueOf(filelist.length))); //$NON-NLS-1$
				if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryFTP.FoundNFiles", String.valueOf(ftpFiles.length))); //$NON-NLS-1$
			    
				// set transfertype ...
				if (binaryMode) 
				{
					ftpclient.setType(FTPTransferType.BINARY);
			        if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryFTP.SetBinary")); //$NON-NLS-1$
				}
				else
				{
					ftpclient.setType(FTPTransferType.ASCII);
			        if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryFTP.SetAscii")); //$NON-NLS-1$
				}
	
				// Some FTP servers return a message saying no files found as a string in the filenlist
				// e.g. Solaris 8
				// CHECK THIS !!!
				
				if (ftpFiles.length == 1)
				{
					String translatedWildcard = environmentSubstitute(wildcard);
					if(!Const.isEmpty(translatedWildcard)){
					  if (ftpFiles[0].getName().startsWith(translatedWildcard))
					  {
					    throw new FTPException(ftpFiles[0].getName());
					  }
					}
				}
	
				Pattern pattern = null;
				if (!Const.isEmpty(wildcard)) {
	                String realWildcard = environmentSubstitute(wildcard);
	                pattern = Pattern.compile(realWildcard);
				}
				
				if(!getSuccessCondition().equals(SUCCESS_IF_NO_ERRORS))
					limitFiles=Const.toInt(environmentSubstitute(getLimit()),10);
				
				// Get the files in the list...
				for (FTPFile ftpFile : ftpFiles) 
				{
					
					if(parentJob.isStopped()){
						exitjobentry=true;
						throw new Exception(Messages.getString("JobEntryFTP.JobStopped"));
					}
					
					if(successConditionBroken){
						throw new Exception(Messages.getString("JobEntryFTP.SuccesConditionBroken",""+NrErrors));
					}
				
					boolean getIt = true;
					
					String filename=ftpFile.getName();
					if(log.isDebug()) log.logDebug(toString(), Messages.getString("JobEntryFTP.AnalysingFile",filename));
					
					// We get only files
					if(ftpFile.isDir())
					{
						// not a file..so let's skip it!
						getIt=false;
						if(log.isDebug()) log.logDebug(toString(), Messages.getString("JobEntryFTP.SkippingNotAFile",filename));
					}
					if(getIt)	{
						try{
							// See if the file matches the regular expression!
							if (pattern!=null){
								Matcher matcher = pattern.matcher(filename);
								getIt = matcher.matches();
							}
							if (getIt)	downloadFile(ftpclient,filename,realMoveToFolder,log,parentJob,result) ;
						}catch (Exception e){
							// Update errors number
							updateErrors();
							log.logError(toString(), Messages.getString("JobFTP.UnexpectedError",e.toString()));
						}
					}
				} // end for
			}
		}
		catch(Exception e){
			if(!successConditionBroken && !exitjobentry) updateErrors();
			log.logError(toString(), Messages.getString("JobEntryFTP.ErrorGetting", e.getMessage())); //$NON-NLS-1$
		}
        finally{
            if (ftpclient!=null) {
                try {
                    ftpclient.quit();
                }
                catch(Exception e) {
                	log.logError(toString(), Messages.getString("JobEntryFTP.ErrorQuitting", e.getMessage())); //$NON-NLS-1$
                }
            }
            FTPClient.clearSOCKS();
        }

		result.setNrErrors(NrErrors);
		result.setNrFilesRetrieved(NrfilesRetrieved);
		if(getSuccessStatus())	result.setResult(true);
		if(exitjobentry) result.setResult(false);
		displayResults(log);
		return result;
	}
	private void downloadFile(FTPClient ftpclient,String filename,String realMoveToFolder,
			LogWriter log, Job parentJob,Result result) throws Exception
	{
		String localFilename=filename;
		targetFilename = returnTargetFilename(localFilename);
		
        if ((!onlyGettingNewFiles) ||
        	(onlyGettingNewFiles && needsDownload(log, targetFilename)))
        {
        	if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryFTP.GettingFile",filename, environmentSubstitute(targetDirectory)));  //$NON-NLS-1$
			ftpclient.get(targetFilename, filename);
					
			// Update retrieved files
			updateRetrievedFiles();
            if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryFTP.GotFile", filename)); //$NON-NLS-1$
			
            // Add filename to result filenames
            addFilenameToResultFilenames(log, result, parentJob, targetFilename);

			// Delete the file if this is needed!
			if (remove) {
				ftpclient.delete(filename);
				if(log.isDetailed()) 
		            if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryFTP.DeletedFile", filename)); //$NON-NLS-1$
			}else
			{
				if(movefiles){
					// Try to move file to destination folder ...
					ftpclient.rename(filename, realMoveToFolder+FILE_SEPARATOR+filename);
					
					if(log.isDetailed()) 
						log.logDetailed(toString(), Messages.getString("JobEntryFTP.MovedFile",filename,realMoveToFolder));
				}
			}
        }
	}
	   /**
     * normalize / to \ and remove trailing slashes from a path
     * 
     * @param path
     * @return normalized path
     * @throws Exception
     */
    public String normalizePath(String path) throws Exception {
        
        String normalizedPath = path.replaceAll("\\\\", FILE_SEPARATOR);
        while (normalizedPath.endsWith("\\") || normalizedPath.endsWith(FILE_SEPARATOR)) {
            normalizedPath = normalizedPath.substring(0, normalizedPath.length()-1);
        }
        
        return normalizedPath;
    }
	private void addFilenameToResultFilenames(LogWriter log, Result result, Job parentJob, String filename ) throws  KettleException
	{
		if(isaddresult){
		FileObject targetFile = null;
		try{
			targetFile = KettleVFS.getFileObject(filename);
			
			// Add to the result files...
			ResultFile resultFile = new ResultFile(ResultFile.FILE_TYPE_GENERAL, targetFile, parentJob.getJobname(), toString());
            resultFile.setComment(Messages.getString("JobEntryFTP.Downloaded", serverName)); //$NON-NLS-1$
			result.getResultFiles().put(resultFile.getFile().toString(), resultFile);
			
            if(log.isDetailed()) log.logDetailed(toString(), Messages.getString("JobEntryFTP.FileAddedToResult", filename)); //$NON-NLS-1$
		}catch (Exception e){
			throw new KettleException(e);
		}
		finally{
			try{
				targetFile.close();
				targetFile=null;
			}catch(Exception e){}
		}
	 }
	}
	private void displayResults(LogWriter log)
	{
		if(log.isDetailed()){
			log.logDetailed(toString(), "=======================================");
			log.logDetailed(toString(), Messages.getString("JobEntryFTP.Log.Info.FilesInError","" + NrErrors));
			log.logDetailed(toString(), Messages.getString("JobEntryFTP.Log.Info.FilesRetrieved","" + NrfilesRetrieved));
			log.logDetailed(toString(), "=======================================");
		}
	}
	private boolean getSuccessStatus()
	{
		boolean retval=false;
		
		if ((NrErrors==0 && getSuccessCondition().equals(SUCCESS_IF_NO_ERRORS))
				|| (NrfilesRetrieved>=limitFiles && getSuccessCondition().equals(SUCCESS_IF_AT_LEAST_X_FILES_DOWNLOADED))
				|| (NrErrors<=limitFiles && getSuccessCondition().equals(SUCCESS_IF_ERRORS_LESS)))
			{
				retval=true;	
			}
		
		return retval;
	}
	private void updateErrors()
	{
		NrErrors++;
		if(checkIfSuccessConditionBroken())
		{
			// Success condition was broken
			successConditionBroken=true;
		}
	}
	private boolean checkIfSuccessConditionBroken()
	{
		boolean retval=false;
		if ((NrErrors>0 && getSuccessCondition().equals(SUCCESS_IF_NO_ERRORS))
				|| (NrErrors>=limitFiles && getSuccessCondition().equals(SUCCESS_IF_ERRORS_LESS)))
		{
			retval=true;	
		}
		return retval;
	}
	private void updateRetrievedFiles()
	{
		NrfilesRetrieved++;
	}


    /**
     * @param string the filename from the FTP server
     * 
     * @return the calculated target filename
     */
	private String returnTargetFilename(String filename)
    {
        String retval=null;
		// Replace possible environment variables...
		if(filename!=null) retval=filename;
		else return null;
		
		int lenstring=retval.length();
		int lastindexOfDot=retval.lastIndexOf(".");
		if(lastindexOfDot==-1) lastindexOfDot=lenstring;
		
		if(isAddDateBeforeExtension())	retval=retval.substring(0, lastindexOfDot);
		
		SimpleDateFormat daf   = new SimpleDateFormat();
		Date now = new Date();
		
		if(SpecifyFormat && !Const.isEmpty(date_time_format))
		{
			daf.applyPattern(date_time_format);
			String dt = daf.format(now);
			retval+=dt;
		}else
		{
			if (adddate)
			{
				daf.applyPattern("yyyyMMdd");
				String d = daf.format(now);
				retval+="_"+d;
			}
			if (addtime)
			{
				daf.applyPattern("HHmmssSSS");
				String t = daf.format(now);
				retval+="_"+t;
			}
		}
		
		if(isAddDateBeforeExtension())
			retval+=retval.substring(lastindexOfDot, lenstring);
		
		// Add foldername to filename		
		retval= environmentSubstitute(targetDirectory)+Const.FILE_SEPARATOR+retval;
		return retval;
    }

    public boolean evaluates()
	{
		return true;
	}
    
    /**
     * See if the filename on the FTP server needs downloading.
     * The default is to check the presence of the file in the target directory.
     * If you need other functionality, extend this class and build it into a plugin.
     * 
     * @param filename The local filename to check
     * @param remoteFileSize The size of the remote file
     * @return true if the file needs downloading
     */
    protected boolean needsDownload(LogWriter log, String filename)
    {
    	boolean retval=false;

        File file = new File(filename);
   
        if(!file.exists()){
        	// Local file not exists!
        	if(log.isDebug()) log.logDebug(Messages.getString("JobEntryFTP.LocalFileNotExists"), filename);
        	return true;
        }else{

        	// Local file exists!
        	if(ifFileExists==ifFileExistsCreateUniq){
        		if(log.isDebug()) log.logDebug(toString() , Messages.getString("JobEntryFTP.LocalFileExists"), filename);
        		// Create file with unique name
        		
        		int lenstring=targetFilename.length();
        		int lastindexOfDot=targetFilename.lastIndexOf('.');
        		if(lastindexOfDot==-1) lastindexOfDot=lenstring;
        		
        		targetFilename=targetFilename.substring(0, lastindexOfDot)
        		+ StringUtil.getFormattedDateTimeNow(true) 
        		+ targetFilename.substring(lastindexOfDot, lenstring);
        		
        		return true;
        	}
        	else if(ifFileExists==ifFileExistsFail){
        		log.logError(Messages.getString("JobEntryFTP.LocalFileExists"), filename);
        		updateErrors();
        	}else{
        		if(log.isDebug()) log.logDebug(toString() , Messages.getString("JobEntryFTP.LocalFileExists"), filename);
        	}
        }
        	
        return retval;
    }

    /**
     * @return the activeConnection
     */
    public boolean isActiveConnection()
    {
        return activeConnection;
    }

    /**
     * @param activeConnection the activeConnection to set
     */
    public void setActiveConnection(boolean passive)
    {
        this.activeConnection = passive;
    }


    public void check(List<CheckResultInterface> remarks, JobMeta jobMeta)
    {
      andValidator().validate(this, "serverName", remarks, putValidators(notBlankValidator())); //$NON-NLS-1$
      andValidator()
          .validate(this, "targetDirectory", remarks, putValidators(notBlankValidator(), fileExistsValidator())); //$NON-NLS-1$
      andValidator().validate(this, "userName", remarks, putValidators(notBlankValidator())); //$NON-NLS-1$
      andValidator().validate(this, "password", remarks, putValidators(notNullValidator())); //$NON-NLS-1$
    }

    public List<ResourceReference> getResourceDependencies(JobMeta jobMeta)
    {
      List<ResourceReference> references = super.getResourceDependencies(jobMeta);
      if (!Const.isEmpty(serverName)) 
      {
        String realServername = jobMeta.environmentSubstitute(serverName);
        ResourceReference reference = new ResourceReference(this);
        reference.getEntries().add(new ResourceEntry(realServername, ResourceType.SERVER));
        references.add(reference);
      }
      return references;
    }

    /**
     * Hook in known parsers, and then those that have been specified in the variable
     * ftp.file.parser.class.names
     * @param ftpClient
     * @throws FTPException
     * @throws IOException
     */
    protected void hookInOtherParsers(LogWriter log, FTPClient ftpClient) throws FTPException, IOException {
      if (log.isDebug()) {log.logDebug(toString(), Messages.getString("JobEntryFTP.DEBUG.Hooking.Parsers"));}
      String system = ftpClient.system();
      MVSFileParser parser = new MVSFileParser();
      if (log.isDebug()) {log.logDebug(toString(), Messages.getString("JobEntryFTP.DEBUG.Created.MVS.Parser"));}
      FTPFileFactory factory = new FTPFileFactory(system);
      if (log.isDebug()) {log.logDebug(toString(), Messages.getString("JobEntryFTP.DEBUG.Created.Factory"));}
      factory.addParser(parser);
      ftpClient.setFTPFileFactory(factory);
      if (log.isDebug()) {log.logDebug(toString(), Messages.getString("JobEntryFTP.DEBUG.Get.Variable.Space"));}
      VariableSpace vs = this.getVariables();
      if (vs != null) {
        if (log.isDebug()) {log.logDebug(toString(), Messages.getString("JobEntryFTP.DEBUG.Getting.Other.Parsers"));}
        String otherParserNames = vs.getVariable("ftp.file.parser.class.names");
        if (otherParserNames != null) {
          if (log.isDebug()) {log.logDebug(toString(), Messages.getString("JobEntryFTP.DEBUG.Creating.Parsers"));}
          String[] parserClasses = otherParserNames.split("|");
          String cName = null;
          Class<?> clazz = null;
          Object parserInstance = null;
          for (int i=0; i<parserClasses.length; i++) {
            cName = parserClasses[i].trim();
            if (cName.length() > 0) {
              try {
                clazz = Class.forName(cName);
                parserInstance = clazz.newInstance();
                if (parserInstance instanceof FTPFileParser) {
                  if (log.isDetailed()) {log.logDetailed(toString(), Messages.getString("JobEntryFTP.DEBUG.Created.Other.Parser", cName));}
                  factory.addParser((FTPFileParser)parserInstance);
                }
              } catch (Exception ignored) {
                if (log.isDebug()) {
                  ignored.printStackTrace();
                  log.logError(toString(), Messages.getString("JobEntryFTP.ERROR.Creating.Parser", cName));
                }
              }
            }
          }
        }
      }
    }   
}