/*
 * Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
*/
package com.panet.imeta.cluster;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.panet.imeta.core.Const;
import com.panet.imeta.core.RowMetaAndData;
import com.panet.imeta.core.changed.ChangedFlag;
import com.panet.imeta.core.encryption.Encr;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.core.variables.VariableSpace;
import com.panet.imeta.core.variables.Variables;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.shared.SharedObjectInterface;
import com.panet.imeta.www.CleanupTransServlet;
import com.panet.imeta.www.GetJobStatusServlet;
import com.panet.imeta.www.GetSlavesServlet;
import com.panet.imeta.www.GetStatusServlet;
import com.panet.imeta.www.GetTransStatusServlet;
import com.panet.imeta.www.SlaveServerDetection;
import com.panet.imeta.www.SlaveServerJobStatus;
import com.panet.imeta.www.SlaveServerStatus;
import com.panet.imeta.www.SlaveServerTransStatus;
import com.panet.imeta.www.StartJobServlet;
import com.panet.imeta.www.StartTransServlet;
import com.panet.imeta.www.StopJobServlet;
import com.panet.imeta.www.StopTransServlet;
import com.panet.imeta.www.WebResult;

public class SlaveServer extends ChangedFlag implements Cloneable, SharedObjectInterface, VariableSpace
{
    public static final String XML_TAG = "slaveserver"; //$NON-NLS-1$

    private static LogWriter log = LogWriter.getInstance();    
    
    private String name;
    private String hostname;
    private String port;
    private String username;
    private String password;

    private String proxyHostname;
    private String proxyPort;
    private String nonProxyHosts;
    
    private boolean master;
    
    private boolean shared;
    private long id;
    
    private VariableSpace variables = new Variables();
    
    public SlaveServer()
    {
    	initializeVariablesFrom(null);
        id=-1L;
    }
    
    public SlaveServer(String name, String hostname, String port, String username, String password)
    {
        this(name, hostname, port, username, password, null, null, null, false);
    }
    
    public SlaveServer(String name, String hostname, String port, String username, String password, String proxyHostname, String proxyPort, String nonProxyHosts, boolean master)
    {
        this();
        this.name     = name;
        this.hostname = hostname;
        this.port     = port;
        this.username = username;
        this.password = password;

        this.proxyHostname = proxyHostname;
        this.proxyPort = proxyPort;
        this.nonProxyHosts = nonProxyHosts;
        
        this.master = master;
        initializeVariablesFrom(null);
    }
    
    public SlaveServer(Node slaveNode)
    {
        this();
        this.name       = XMLHandler.getTagValue(slaveNode, "name"); //$NON-NLS-1$
        this.hostname   = XMLHandler.getTagValue(slaveNode, "hostname"); //$NON-NLS-1$
        this.port       = XMLHandler.getTagValue(slaveNode, "port"); //$NON-NLS-1$
        this.username   = XMLHandler.getTagValue(slaveNode, "username"); //$NON-NLS-1$
        this.password   = Encr.decryptPasswordOptionallyEncrypted( XMLHandler.getTagValue(slaveNode, "password") ); //$NON-NLS-1$
        this.proxyHostname = XMLHandler.getTagValue(slaveNode, "proxy_hostname"); //$NON-NLS-1$
        this.proxyPort     = XMLHandler.getTagValue(slaveNode, "proxy_port"); //$NON-NLS-1$
        this.nonProxyHosts = XMLHandler.getTagValue(slaveNode, "non_proxy_hosts"); //$NON-NLS-1$
        this.master = "Y".equalsIgnoreCase( XMLHandler.getTagValue(slaveNode, "master") ); //$NON-NLS-1$ //$NON-NLS-2$
        initializeVariablesFrom(null);
    }

    public String getXML()
    {
        StringBuffer xml = new StringBuffer();
        
        xml.append("<").append(XML_TAG).append(">"); //$NON-NLS-1$  //$NON-NLS-2$
        
        xml.append(XMLHandler.addTagValue("name", name, false)); //$NON-NLS-1$
        xml.append(XMLHandler.addTagValue("hostname", hostname, false)); //$NON-NLS-1$
        xml.append(XMLHandler.addTagValue("port",     port, false)); //$NON-NLS-1$
        xml.append(XMLHandler.addTagValue("username", username, false)); //$NON-NLS-1$
        xml.append(XMLHandler.addTagValue("password", Encr.encryptPasswordIfNotUsingVariables(password), false)); //$NON-NLS-1$
        xml.append(XMLHandler.addTagValue("proxy_hostname", proxyHostname, false)); //$NON-NLS-1$
        xml.append(XMLHandler.addTagValue("proxy_port", proxyPort, false)); //$NON-NLS-1$
        xml.append(XMLHandler.addTagValue("non_proxy_hosts", nonProxyHosts, false)); //$NON-NLS-1$
        xml.append(XMLHandler.addTagValue("master", master, false)); //$NON-NLS-1$

        xml.append("</").append(XML_TAG).append(">"); //$NON-NLS-1$  //$NON-NLS-2$
        
        return xml.toString();
    }

    public void saveRep(Repository rep) throws KettleException
    {
        saveRep(rep, -1L, false);
    }
    
    public void saveRep(Repository rep, long id_transformation, boolean isUsedByTransformation) throws KettleException
    {
        setId(rep.getSlaveID(name));
        
        if (getId()<0)
        {
            setId(rep.insertSlave(this));
        }
        else
        {
            rep.updateSlave(this);
        }
        
        // Save the trans-slave relationship too.
        if (id_transformation>=0 && isUsedByTransformation) rep.insertTransformationSlave(id_transformation, getId());
    }
    
    public SlaveServer(Repository rep, long id_slave_server) throws KettleException
    {
        this();
        
        setId(id_slave_server);
        
        RowMetaAndData row = rep.getSlaveServer(id_slave_server);
        if (row==null)
        {
            throw new KettleDatabaseException(Messages.getString("SlaveServer.SlaveCouldNotBeFound", Long.toString(id_slave_server))); //$NON-NLS-1$
        }
        
        name          = row.getString("NAME", null); //$NON-NLS-1$
        hostname      = row.getString("HOST_NAME", null); //$NON-NLS-1$
        port          = row.getString("PORT", null); //$NON-NLS-1$
        username      = row.getString("USERNAME", null); //$NON-NLS-1$
        password      = row.getString("PASSWORD", null); //$NON-NLS-1$
        proxyHostname = row.getString("PROXY_HOST_NAME", null); //$NON-NLS-1$
        proxyPort     = row.getString("PROXY_PORT", null); //$NON-NLS-1$
        nonProxyHosts = row.getString("NON_PROXY_HOSTS", null); //$NON-NLS-1$
        master        = row.getBoolean("MASTER", false); //$NON-NLS-1$
    }

    
    public Object clone()
    {
        SlaveServer slaveServer = new SlaveServer();
        slaveServer.replaceMeta(this);
        return slaveServer;
    }

    public void replaceMeta(SlaveServer slaveServer)
    {
        this.name = slaveServer.name;
        this.hostname = slaveServer.hostname;
        this.port = slaveServer.port;
        this.username = slaveServer.username;
        this.password = slaveServer.password;
        this.proxyHostname = slaveServer.proxyHostname;
        this.proxyPort = slaveServer.proxyPort;
        this.nonProxyHosts = slaveServer.nonProxyHosts;
        this.master = slaveServer.master;
        
        this.id = slaveServer.id;
        this.shared = slaveServer.shared;
        this.setChanged(true);
    }
    
    public String toString()
    {
        return name;
    }
    
    
    public String getServerAndPort()
    {
        String realHostname = environmentSubstitute(hostname);
        if (!Const.isEmpty(realHostname)) return realHostname+getPortSpecification();
        return "Slave Server"; //$NON-NLS-1$
    }
    
    public boolean equals(Object obj)
    {
        SlaveServer slave = (SlaveServer) obj;
        return name.equalsIgnoreCase(slave.getName());
    }
    
    public int hashCode()
    {
        return name.hashCode();
    }
    
    public String getHostname()
    {
        return hostname;
    }
    
    public void setHostname(String urlString)
    {
        this.hostname = urlString;
    }
    
    /**
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * @return the username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username)
    {
        this.username = username;
    }
    

    /**
     * @return the nonProxyHosts
     */
    public String getNonProxyHosts()
    {
        return nonProxyHosts;
    }

    /**
     * @param nonProxyHosts the nonProxyHosts to set
     */
    public void setNonProxyHosts(String nonProxyHosts)
    {
        this.nonProxyHosts = nonProxyHosts;
    }

    /**
     * @return the proxyHostname
     */
    public String getProxyHostname()
    {
        return proxyHostname;
    }

    /**
     * @param proxyHostname the proxyHostname to set
     */
    public void setProxyHostname(String proxyHostname)
    {
        this.proxyHostname = proxyHostname;
    }

    /**
     * @return the proxyPort
     */
    public String getProxyPort()
    {
        return proxyPort;
    }

    /**
     * @param proxyPort the proxyPort to set
     */
    public void setProxyPort(String proxyPort)
    {
        this.proxyPort = proxyPort;
    }
    
    public String getPortSpecification()
    {
        String realPort = environmentSubstitute(port);
        String portSpec = ":"+realPort; //$NON-NLS-1$
        if (Const.isEmpty(realPort) || port.equals("80")) //$NON-NLS-1$
        {
            portSpec=""; //$NON-NLS-1$
        }
        return portSpec;
    }
    
    public String constructUrl(String serviceAndArguments) throws UnsupportedEncodingException
    {
        String realHostname = environmentSubstitute(hostname);
        String retval =  "http://"+realHostname+getPortSpecification()+serviceAndArguments; //$NON-NLS-1$ $NON-NLS-2$
        retval = Const.replace(retval, " ", "%20"); //$NON-NLS-1$  //$NON-NLS-2$
        return retval;
    }
    
    /**
     * @return the port
     */
    public String getPort()
    {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port)
    {
        this.port = port;
    }

    public String sendXML(String xml, String service) throws Exception
    {
        // The content
        // 
        byte[] content = xml.getBytes(Const.XML_ENCODING);
        
        // Prepare HTTP put
        // 
        String urlString = constructUrl(service);
        log.logDebug(toString(), Messages.getString("SlaveServer.DEBUG_ConnectingTo", urlString)); //$NON-NLS-1$
        PutMethod put = new PutMethod(urlString);
        
        // Request content will be retrieved directly from the input stream
        // 
        RequestEntity entity = new ByteArrayRequestEntity(content);
        
        put.setRequestEntity(entity);
        put.setDoAuthentication(true);
        put.addRequestHeader(new Header("Content-Type", "text/xml;charset=" + Const.XML_ENCODING));

        // post.setContentChunked(true);
        
        // Get HTTP client
        // 
        HttpClient client = new HttpClient();
        addCredentials(client);
        
        // Execute request
        // 
        try
        {
            int result = client.executeMethod(put);
            
            // The status code
            log.logDebug(toString(), Messages.getString("SlaveServer.DEBUG_ResponseStatus", Integer.toString(result))); //$NON-NLS-1$
            
            // the response
            InputStream inputStream = new BufferedInputStream(put.getResponseBodyAsStream(), 1000);
            
            StringBuffer bodyBuffer = new StringBuffer();
            int c;
            while ( (c=inputStream.read())!=-1) bodyBuffer.append((char)c);
            inputStream.close();
            String bodyTmp = bodyBuffer.toString();
            
            switch(result)
            {
            case 401: // Security problem: authentication required
              // Non-internationalized message
                String message = "Authentication failed"+Const.DOSCR+Const.DOSCR+bodyTmp; //$NON-NLS-1$
                WebResult webResult = new WebResult(WebResult.STRING_ERROR, message);
                bodyBuffer.setLength(0);
                bodyBuffer.append(webResult.getXML());
                break;
            }

            String body = bodyBuffer.toString();
            

            // String body = post.getResponseBodyAsString(); 
            log.logDebug(toString(), Messages.getString("SlaveServer.DEBUG_ResponseBody",body)); //$NON-NLS-1$
            
            return body;
        }
        finally
        {
            // Release current connection to the connection pool once you are done
            put.releaseConnection();
            log.logDetailed(toString(), Messages.getString("SlaveServer.DETAILED_SentXmlToService", service, hostname)); //$NON-NLS-1$
        }
    }


    private void addCredentials(HttpClient client)
    {
        client.getState().setCredentials
              (
                new AuthScope(environmentSubstitute(hostname), Const.toInt(environmentSubstitute(port), 80), "Kettle"), //$NON-NLS-1$
                new UsernamePasswordCredentials(environmentSubstitute(username), environmentSubstitute(password))
              );
    }

    /**
     * @return the master
     */
    public boolean isMaster()
    {
        return master;
    }

    /**
     * @param master the master to set
     */
    public void setMaster(boolean master)
    {
        this.master = master;
    }

    public String execService(String service) throws Exception
    {
        // Prepare HTTP get
        // 
        HttpClient client = new HttpClient();
        addCredentials(client);
        HttpMethod method = new GetMethod(constructUrl(service));
        
        // Execute request
        // 
        try
        {
            int result = client.executeMethod(method);
            
            // The status code
            log.logDebug(toString(), Messages.getString("SlaveServer.DEBUG_ResponseStatus", Integer.toString(result))); //$NON-NLS-1$
            
            // the response
            InputStream inputStream = new BufferedInputStream(method.getResponseBodyAsStream());
            
            StringBuffer bodyBuffer = new StringBuffer();
            int c;
            while ( (c=inputStream.read())!=-1) 
            {
                bodyBuffer.append((char)c);
            }
            inputStream.close();
            
            String body = bodyBuffer.toString();

            log.logDetailed(toString(), Messages.getString("SlaveServer.DETAILED_FinishedReading", Integer.toString(bodyBuffer.length()))); //$NON-NLS-1$
            log.logDebug(toString(), Messages.getString("SlaveServer.DEBUG_ResponseBody",body)); //$NON-NLS-1$
            
            return body;
        }
        finally
        {
            // Release current connection to the connection pool once you are done
            method.releaseConnection();
            log.logDetailed(toString(), Messages.getString("SlaveServer.DETAILED_ExecutedService", service, hostname) ); //$NON-NLS-1$
        }

    }

    /**
     * Contact the server and get back the reply as a string
     * @return the requested information
     * @throws Exception in case something goes awry 
     */
    public String getContentFromServer(String service) throws Exception
    {
        // Following variable hides class variable. MB 7/10/07
        // LogWriter log = LogWriter.getInstance();
        
        String urlToUse = constructUrl(service);
        URL server;
        StringBuffer result = new StringBuffer();
        
        try
        {
            String beforeProxyHost     = System.getProperty("http.proxyHost");  //$NON-NLS-1$
            String beforeProxyPort     = System.getProperty("http.proxyPort");  //$NON-NLS-1$
            String beforeNonProxyHosts = System.getProperty("http.nonProxyHosts");  //$NON-NLS-1$

            BufferedReader      input        = null;
            
            try
            {
                if(log.isBasic())
                	log.logBasic(toString(), Messages.getString("SlaveServer.DEBUG_ConnectingTo", urlToUse)); //$NON-NLS-1$

                if (proxyHostname!=null) 
                {
                    System.setProperty("http.proxyHost", environmentSubstitute(proxyHostname)); //$NON-NLS-1$
                    System.setProperty("http.proxyPort", environmentSubstitute(proxyPort)); //$NON-NLS-1$
                    if (nonProxyHosts!=null) System.setProperty("http.nonProxyHosts", environmentSubstitute(nonProxyHosts)); //$NON-NLS-1$
                }
                
                if (username!=null && username.length()>0)
                {
                    Authenticator.setDefault(new Authenticator()
                        {
                            protected PasswordAuthentication getPasswordAuthentication()
                            {
                                return new PasswordAuthentication(environmentSubstitute(username), password!=null ? environmentSubstitute(password).toCharArray() : new char[] {} );
                            }
                        }
                    );
                }

                // Get a stream for the specified URL
                server = new URL(urlToUse);
                URLConnection connection = server.openConnection();
                
                log.logDetailed(toString(), Messages.getString("SlaveServer.StartReadingReply")); //$NON-NLS-1$
    
                // Read the result from the server...
                InputStream inputStream = new BufferedInputStream(connection.getInputStream(), 1000);
                
                input = new BufferedReader(new InputStreamReader( inputStream ));
                
                long bytesRead = 0L;
                String line;
                while ( (line=input.readLine())!=null )
                {
                    result.append(line).append(Const.CR);
                    bytesRead+=line.length();
                }
                if(log.isBasic())
                	log.logBasic(toString(), Messages.getString("SlaveServer.FinishedReadingResponse"), bytesRead); //$NON-NLS-1$
                if(log.isDebug())
                	log.logDebug(toString(), "response from the webserver: {0}", result);
            }
            catch(MalformedURLException e)
            {
                log.logError(toString(), Messages.getString("SlaveServer.UrlIsInvalid", urlToUse, e.getMessage())); //$NON-NLS-1$
                log.logError(toString(), Const.getStackTracker(e));
            }
            catch(IOException e)
            {
                log.logError(toString(), Messages.getString("SlaveServer.CannotSaveDueToIOError", e.getMessage())); //$NON-NLS-1$
                log.logError(toString(), Const.getStackTracker(e));
            }
            catch(Exception e)
            {
                log.logError(toString(), Messages.getString("SlaveServer.ErrorReceivingFile", e.getMessage())); //$NON-NLS-1$
                log.logError(toString(), Const.getStackTracker(e));
            }
            finally
            {
                // Close it all
                try
                {
                    if (input!=null) input.close();
                }
                catch(Exception e)
                {
                    log.logError(toString(), Messages.getString("SlaveServer.CannotCloseStream", e.getMessage())); //$NON-NLS-1$
                    log.logError(toString(), Const.getStackTracker(e));
                }

            }

            // Set the proxy settings back as they were on the system!
            System.setProperty("http.proxyHost", Const.NVL(beforeProxyHost, "")); //$NON-NLS-1$  //$NON-NLS-2$
            System.setProperty("http.proxyPort", Const.NVL(beforeProxyPort, "")); //$NON-NLS-1$  //$NON-NLS-2$
            System.setProperty("http.nonProxyHosts", Const.NVL(beforeNonProxyHosts, "")); //$NON-NLS-1$  //$NON-NLS-2$
            
            // Get the result back...
            return result.toString();
        }
        catch(Exception e)
        {
            throw new Exception(Messages.getString("SlaveServer.CannotContactURLForSecurityInformation", urlToUse), e); //$NON-NLS-1$
        }
    }
    
    public SlaveServerStatus getStatus() throws Exception
    {
        String xml = execService(GetStatusServlet.CONTEXT_PATH+"/?xml=Y"); //$NON-NLS-1$
        return SlaveServerStatus.fromXML(xml);
    }
    
    public List<SlaveServerDetection> getSlaveServerDetections() throws Exception
    {
        String xml = execService(GetSlavesServlet.CONTEXT_PATH+"/"); //$NON-NLS-1$
        Document document = XMLHandler.loadXMLString(xml);
        Node detectionsNode = XMLHandler.getSubNode(document, GetSlavesServlet.XML_TAG_SLAVESERVER_DETECTIONS);
        int nrDetections = XMLHandler.countNodes(detectionsNode, SlaveServerDetection.XML_TAG);
        
        List<SlaveServerDetection> detections = new ArrayList<SlaveServerDetection>();
        for (int i=0;i<nrDetections;i++) {
        	Node detectionNode = XMLHandler.getSubNodeByNr(detectionsNode, SlaveServerDetection.XML_TAG, i);
        	SlaveServerDetection detection = new SlaveServerDetection(detectionNode);
        	detections.add(detection);
        }
        return detections;
    }

    public SlaveServerTransStatus getTransStatus(String transName) throws Exception
    {
        String xml = execService(GetTransStatusServlet.CONTEXT_PATH+"/?name="+URLEncoder.encode(transName, "UTF-8")+"&xml=Y"); //$NON-NLS-1$  //$NON-NLS-2$
        return SlaveServerTransStatus.fromXML(xml);
    }
    
    public SlaveServerJobStatus getJobStatus(String jobName) throws Exception
    {
        String xml = execService(GetJobStatusServlet.CONTEXT_PATH+"/?name="+URLEncoder.encode(jobName, "UTF-8")+"&xml=Y"); //$NON-NLS-1$  //$NON-NLS-2$
        return SlaveServerJobStatus.fromXML(xml);
    }
    
    public WebResult stopTransformation(String transName) throws Exception
    {
        String xml = execService(StopTransServlet.CONTEXT_PATH+"/?name="+URLEncoder.encode(transName, "UTF-8")+"&xml=Y"); //$NON-NLS-1$  //$NON-NLS-2$
        return WebResult.fromXMLString(xml);
    }
    
    public WebResult stopJob(String transName) throws Exception
    {
        String xml = execService(StopJobServlet.CONTEXT_PATH+"/?name="+URLEncoder.encode(transName, "UTF-8")+"&xml=Y"); //$NON-NLS-1$  //$NON-NLS-2$
        return WebResult.fromXMLString(xml);
    }
    
    public WebResult startTransformation(String transName) throws Exception
    {
        String xml = execService(StartTransServlet.CONTEXT_PATH+"/?name="+URLEncoder.encode(transName, "UTF-8")+"&xml=Y");  //$NON-NLS-1$ //$NON-NLS-2$
        return WebResult.fromXMLString(xml);
    }
    
    public WebResult startJob(String transName) throws Exception
    {
        String xml = execService(StartJobServlet.CONTEXT_PATH+"/?name="+URLEncoder.encode(transName, "UTF-8")+"&xml=Y");  //$NON-NLS-1$ //$NON-NLS-2$
        return WebResult.fromXMLString(xml);
    }

    public WebResult cleanupTransformation(String transName) throws Exception
    {
        String xml = execService(CleanupTransServlet.CONTEXT_PATH+"/?name="+URLEncoder.encode(transName, "UTF-8")+"&xml=Y"); //$NON-NLS-1$  //$NON-NLS-2$
        return WebResult.fromXMLString(xml);
    }
    
    public static SlaveServer findSlaveServer(List<SlaveServer> slaveServers, String name)
    {
        for (SlaveServer slaveServer : slaveServers)
        {
            if (slaveServer.getName()!=null && slaveServer.getName().equalsIgnoreCase(name)) return slaveServer;
        }
        return null;
    }
    
    public static String[] getSlaveServerNames(List<SlaveServer> slaveServers)
    {
        String[] names = new String[slaveServers.size()];
        for (int i=0;i<slaveServers.size();i++)
        {
            SlaveServer slaveServer = slaveServers.get(i);
            names[i] = slaveServer.getName();
        }
        return names;
    }

    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isShared()
    {
        return shared;
    }

    public void setShared(boolean shared)
    {
        this.shared = shared;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

	public void copyVariablesFrom(VariableSpace space) 
	{
		variables.copyVariablesFrom(space);		
	}

	public String environmentSubstitute(String aString) 
	{
		return variables.environmentSubstitute(aString);
	}

	public String[] environmentSubstitute(String aString[]) 
	{
		return variables.environmentSubstitute(aString);
	}		

	public VariableSpace getParentVariableSpace() 
	{
		return variables.getParentVariableSpace();
	}

	public void setParentVariableSpace(VariableSpace parent) 
	{
		variables.setParentVariableSpace(parent);
	}

	public String getVariable(String variableName, String defaultValue) 
	{
		return variables.getVariable(variableName, defaultValue);
	}

	public String getVariable(String variableName) 
	{
		return variables.getVariable(variableName);
	}
	
	public boolean getBooleanValueOfVariable(String variableName, boolean defaultValue) {
		if (!Const.isEmpty(variableName))
		{
			String value = environmentSubstitute(variableName);
			if (!Const.isEmpty(value))
			{
				return ValueMeta.convertStringToBoolean(value);
			}
		}
		return defaultValue;
	}

	public void initializeVariablesFrom(VariableSpace parent) 
	{
		variables.initializeVariablesFrom(parent);	
	}

	public String[] listVariables() 
	{
		return variables.listVariables();
	}

	public void setVariable(String variableName, String variableValue) 
	{
		variables.setVariable(variableName, variableValue);		
	}

	public void shareVariablesWith(VariableSpace space) 
	{
		variables = space;		
	}

	public void injectVariables(Map<String,String> prop) 
	{
		variables.injectVariables(prop);		
	}	       
}
