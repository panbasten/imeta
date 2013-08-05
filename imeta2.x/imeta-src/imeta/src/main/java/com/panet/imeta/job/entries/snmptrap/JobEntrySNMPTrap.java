/*
 * Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Samatar Hassan and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Samatar Hassan 
 * The Initial Developer is Samatar Hassan.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
*/


package com.panet.imeta.job.entries.snmptrap;


import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.w3c.dom.Node;

import com.panet.imeta.cluster.SlaveServer;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Result;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.job.Job;
import com.panet.imeta.job.JobEntryType;
import com.panet.imeta.job.entry.JobEntryBase;
import com.panet.imeta.job.entry.JobEntryInterface;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.shared.SharedObjectInterface;



/**
 * This defines an SNMPTrap job entry.
 *
 * @author Matt
 * @since 05-11-2003
 *
 */

public class JobEntrySNMPTrap extends JobEntryBase implements Cloneable, JobEntryInterface
{
	public static final String ENTRY_ATTRIBUTE_PORT = "port";
	public static final String ENTRY_ATTRIBUTE_SERVERNAME = "servername";
	public static final String ENTRY_ATTRIBUTE_OID = "oid";
	public static final String ENTRY_ATTRIBUTE_MESSAGE = "message";
	public static final String ENTRY_ATTRIBUTE_COMSTRING = "comstring";
	public static final String ENTRY_ATTRIBUTE_TIMEOUT = "timeout";
	public static final String ENTRY_ATTRIBUTE_NRRETRY = "nrretry";
	public static final String ENTRY_ATTRIBUTE_TARGETTYPE = "targettype";
	public static final String ENTRY_ATTRIBUTE_USER = "user";
	public static final String ENTRY_ATTRIBUTE_PASSPHRASE = "passphrase";
	public static final String ENTRY_ATTRIBUTE_ENGINEID = "engineid";
	
private static Logger log4j = Logger.getLogger(JobEntrySNMPTrap.class);
	
	private String serverName;
	private String port;
	private String timeout;
    private String nrretry;
    private String comString;
    private String message;
    private String oid;
    private String targettype;
    private String user;
    private String passphrase;
    private String engineid;
    
    /**
     * Default retries
     */
    static private int DEFAULT_RETRIES = 1;
    
    /**
     * Default timeout to 500 milliseconds
     */
    static private int DEFAULT_TIME_OUT = 5000;    
    
    /**
     * Default port
     */
    public static int DEFAULT_PORT = 162;    
    
	public static final String[] target_type_Desc = new String[] { Messages.getString("JobSNMPTrap.TargetType.Community"), Messages.getString("JobSNMPTrap.TargetType.User") };
	public static final String[] target_type_Code = new String[] {"community", "user"};
    
	public JobEntrySNMPTrap(String n)
	{
		super(n, "");
		port=""+DEFAULT_PORT;
		serverName=null;
		comString="public";
		nrretry=""+DEFAULT_RETRIES;
		timeout=""+DEFAULT_TIME_OUT;
		message=null;
		oid=null;
		targettype=target_type_Code[0];
		user=null;
		passphrase=null;
		engineid=null;
		
		setID(-1L);
		setJobEntryType(JobEntryType.SNMP_TRAP);
	}

	public JobEntrySNMPTrap()
	{
		this("");
	}

	public JobEntrySNMPTrap(JobEntryBase jeb)
	{
		super(jeb);
	}

    public Object clone()
    {
        JobEntrySNMPTrap je = (JobEntrySNMPTrap) super.clone();
        return je;
    }
    public String getTargetTypeDesc(String tt)
    {
   	 if(Const.isEmpty(tt)) return target_type_Desc[0]; 
		if(tt.equalsIgnoreCase(target_type_Code[1]))
			return target_type_Desc[1];
		else
			return target_type_Desc[0]; 
    }
    public String getTargetTypeCode(String tt)
    {
   	if(tt==null) return target_type_Code[0]; 
		if(tt.equals(target_type_Desc[1]))
			return target_type_Code[1];
		else
			return target_type_Code[0]; 
    }
	public String getXML()
	{
        StringBuffer retval = new StringBuffer(128);
		
		retval.append(super.getXML());
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_PORT,   port));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_SERVERNAME,   serverName));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_OID,   oid));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_COMSTRING,   comString));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_MESSAGE,   message));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_TIMEOUT,      timeout));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_NRRETRY,      nrretry));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_TARGETTYPE,      targettype));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_USER,      user));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_PASSPHRASE,      passphrase));
		retval.append("      ").append(XMLHandler.addTagValue(ENTRY_ATTRIBUTE_ENGINEID,      engineid));
		return retval.toString();
	}
	
	public void loadXML(Node entrynode, List<DatabaseMeta> databases, List<SlaveServer> slaveServers, Repository rep) throws KettleXMLException
	{
	try
	{
		  super.loadXML(entrynode, databases, slaveServers);
	      	port          = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_PORT);
			serverName   = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_SERVERNAME);
			oid          = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_OID);
			message      = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_MESSAGE);
			comString    = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_COMSTRING);
			timeout      = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_TIMEOUT);
			nrretry      = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_NRRETRY);
			targettype      = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_TARGETTYPE);
			user      = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_USER);
			passphrase      = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_PASSPHRASE);
			engineid      = XMLHandler.getTagValue(entrynode, ENTRY_ATTRIBUTE_ENGINEID);
		      
		}
		catch(KettleXMLException xe)
		{
			throw new KettleXMLException("Unable to load job entry of type 'SNMPTrap' from XML node", xe);
		}
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		port = JobEntryBase.parameterToString(p.get(id + ".port"));
		serverName = JobEntryBase.parameterToString(p.get(id + ".serverName"));
		oid = JobEntryBase.parameterToString(p.get(id + ".oid"));
		message = JobEntryBase.parameterToString(p.get(id + ".message"));
		comString = JobEntryBase.parameterToString(p.get(id + ".comString"));
		timeout = JobEntryBase.parameterToString(p.get(id + ".timeout"));
		nrretry = JobEntryBase.parameterToString(p.get(id + ".nrretry"));
		targettype = JobEntryBase.parameterToString(p.get(id + ".targettype"));
		user = JobEntryBase.parameterToString(p.get(id + ".user"));
		passphrase = JobEntryBase.parameterToString(p.get(id + ".passphrase"));
		engineid = JobEntryBase.parameterToString(p.get(id + ".engineid"));
	}
	
	public void loadRep(Repository rep, long id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
	throws KettleException
  { 
	try
	{
		super.loadRep(rep, id_jobentry, databases, slaveServers);

	      	port          = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_PORT);
			serverName    = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_SERVERNAME);
			oid           = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_OID);
			message       = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_MESSAGE);
			comString     = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_COMSTRING);
			timeout       = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_TIMEOUT);
			nrretry       = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_NRRETRY);
			targettype       = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_TARGETTYPE);
			user       = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_USER);
			passphrase       = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_PASSPHRASE);
			engineid       = rep.getJobEntryAttributeString(id_jobentry, ENTRY_ATTRIBUTE_ENGINEID);
			
		}
		catch(KettleException dbe)
		{
			throw new KettleException("Unable to load job entry of type 'SNMPTrap' from the repository for id_jobentry="+id_jobentry, dbe);
		}
	}
	
	public void saveRep(Repository rep, long id_job)
		throws KettleException
	{
		try
		{
			super.saveRep(rep, id_job);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_PORT,      port);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_SERVERNAME,      serverName);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_OID,      oid);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_MESSAGE,      message);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_COMSTRING,      comString);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_TIMEOUT,         timeout);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_NRRETRY,         nrretry);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_TARGETTYPE,         targettype);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_USER,         user);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_PASSPHRASE,         passphrase);
			rep.saveJobEntryAttribute(id_job, getID(), ENTRY_ATTRIBUTE_ENGINEID,         engineid);
           
		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleException("Unable to save job entry of type 'SNMPTrap' to the repository for id_job="+id_job, dbe);
		}
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
	 * @return Returns the OID.
	 */
	public String getOID()
	{
		return oid;
	}
	/**
	 * @param serverName The oid to set.
	 */
	public void setOID(String oid)
	{
		this.oid = oid;
	}
	/**
	 * @return Returns the comString.
	 */
	public String getComString()
	{
		return comString;
	}
	/**
	 * @param comString The comString to set.
	 */
	public void setComString(String comString)
	{
		this.comString = comString;
	}
	/**
	 * @param user The user to set.
	 */
	public void setUser(String user)
	{
		this.user = user;
	}
	/**
	 * @return Returns the user.
	 */
	public String getUser()
	{
		return user;
	}
	/**
	 * @param user The passphrase to set.
	 */
	public void setPassPhrase(String passphrase)
	{
		this.passphrase = passphrase;
	}
	/**
	 * @return Returns the passphrase.
	 */
	public String getPassPhrase()
	{
		return passphrase;
	}
	/**
	 * @param user The engineid to set.
	 */
	public void setEngineID(String engineid)
	{
		this.engineid = engineid;
	}
	/**
	 * @return Returns the engineid.
	 */
	public String getEngineID()
	{
		return engineid;
	}
	public String getTargetType() 
	{
		return targettype;
	}
	public void setTargetType(String targettypein) 
	{
		this.targettype = getTargetTypeCode(targettypein);
	}
	/**
	 * @param message The message to set.
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}
	/**
	 * @return Returns the comString.
	 */
	public String getMessage()
	{
		return message;
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
	 * @param timeout The timeout to set.
	 */
	public void setTimeout(String timeout)
	{
		this.timeout = timeout;
	}

	/**
	 * @return Returns the timeout.
	 */
	public String getTimeout()
	{
		return timeout;
	}
	/**
	 * @param nrretry The nrretry to set.
	 */
	public void setRetry(String nrretry)
	{
		this.nrretry = nrretry;
	}
	/**
	 * @return Returns the nrretry.
	 */
	public String getRetry()
	{
		return nrretry;
	}
    
	public Result execute(Result previousResult, int nr, Repository rep, Job parentJob)
	{		
		LogWriter log = LogWriter.getInstance();
		log4j.info(Messages.getString("JobEntrySNMPTrap.Started", serverName)); //$NON-NLS-1$
		
		Result result = previousResult;
		result.setNrErrors(1);
		result.setResult( false );
		
		String servername=environmentSubstitute(serverName);
		int nrPort=Const.toInt(environmentSubstitute(""+port), DEFAULT_PORT);
		String Oid=environmentSubstitute(oid);
		int timeOut=Const.toInt(environmentSubstitute(""+timeout), DEFAULT_TIME_OUT);
		int retry=Const.toInt(environmentSubstitute(""+nrretry), 1);
		String messageString=environmentSubstitute(message);

		
		Snmp snmp = null;
		
		try
		{
			TransportMapping transMap = new DefaultUdpTransportMapping();
			snmp = new Snmp(transMap);
			
			UdpAddress udpAddress=new UdpAddress(InetAddress.getByName(servername), nrPort);
			ResponseEvent response=null;
			if(targettype.equals(target_type_Code[0]))
			{
				// Community target
				String community=environmentSubstitute(comString);
				
				CommunityTarget target  = new CommunityTarget();
				PDUv1 pdu1 = new PDUv1();
				transMap.listen();
				
				target.setCommunity(new OctetString(community));
				target.setVersion(SnmpConstants.version1);
				target.setAddress(udpAddress);
				if (target.getAddress().isValid()) 
				{
					if(log.isDebug()) log.logDebug(toString(),"Valid IP address");
				} else 
				     throw new KettleException("Invalid IP address");
				target.setRetries(retry);
				target.setTimeout(timeOut);
				
				// create the PDU
				pdu1.setGenericTrap(6);
				pdu1.setSpecificTrap(PDUv1.ENTERPRISE_SPECIFIC);
				pdu1.setEnterprise(new OID(Oid));
				pdu1.add(new VariableBinding(new OID(Oid),new OctetString(messageString)));
				
				response =snmp.send(pdu1, target);

			}else
			{
				// User target
				String userName=environmentSubstitute(user);
				String passPhrase=environmentSubstitute(passphrase);
				String engineID=environmentSubstitute(engineid);
				
				UserTarget usertarget = new UserTarget();
				transMap.listen();
				usertarget.setAddress(udpAddress);
				if (usertarget.getAddress().isValid()) 
				{
					if(log.isDebug()) log.logDebug(toString(),"Valid IP address");
				} else 
				     throw new KettleException("Invalid IP address");
				

				usertarget.setRetries(retry);
				usertarget.setTimeout(timeOut);
				usertarget.setVersion(SnmpConstants.version3);
				usertarget.setSecurityLevel(SecurityLevel.AUTH_PRIV);
				usertarget.setSecurityName(new OctetString("MD5DES"));
				
				
				// Since we are using SNMPv3 we use authenticated users 
				// this is handled by the UsmUser and USM class

				UsmUser uu = new UsmUser(new OctetString(userName), 
						AuthMD5.ID, new OctetString(passPhrase), 
						PrivDES.ID, new OctetString(passPhrase));

				if (uu == null) {
					throw new KettleException("Null UsmUser");
				 } else {
					if(log.isDebug()) log.logDebug(toString(),"Valid UsmUser");
				}

				USM usm = snmp.getUSM();

				  if (usm == null) 
					  throw new KettleException("Null Usm");
				  else 
				  {
					  usm = new USM(SecurityProtocols.getInstance(), 
					  new  OctetString(MPv3.createLocalEngineID()), 0);
					          usm.addUser(new OctetString(userName), uu);
					  if(log.isDebug()) log.logDebug(toString(),"Valid Usm");
				   }

				
			     // create the PDU
				 ScopedPDU pdu = new ScopedPDU();
			     pdu.add(new VariableBinding(new OID(Oid),new OctetString(messageString)));		     
			     pdu.setType(PDU.TRAP);
			     if(!Const.isEmpty(engineID)) pdu.setContextEngineID(new OctetString(engineID));

			     // send the PDU
			     response =snmp.send(pdu, usertarget);
			}


		     if (response == null) 
		     {
		    	                 
		     } else 
		     {
		    	  if(log.isDebug()) log.logDebug(toString(),"Received response from: " +
		    	  response.getPeerAddress() + response.toString());                
		     }

			result.setNrErrors(0);
			result.setResult(true);
		}
		catch(Exception e){
			log.logError(toString(), Messages.getString("JobEntrySNMPTrap.ErrorGetting", e.getMessage())); //$NON-NLS-1$
		}
        finally{
        	try{
            if(snmp!=null) snmp.close();}catch(Exception e){};
        }


		return result;
	}

	
    public boolean evaluates()
	{
		return true;
	}

}
