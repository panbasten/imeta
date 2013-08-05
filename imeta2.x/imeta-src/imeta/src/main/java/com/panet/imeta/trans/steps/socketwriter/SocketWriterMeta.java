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
 
package com.panet.imeta.trans.steps.socketwriter;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.core.CheckResult;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Counter;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleStepException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.variables.VariableSpace;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.shared.SharedObjectInterface;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepMeta;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;


/*
 * Created on 02-jun-2003
 *
 */

public class SocketWriterMeta extends BaseStepMeta implements StepMetaInterface
{
	public static final String STEP_ATTRIBUTE_PORT = "port";
	public static final String STEP_ATTRIBUTE_BUFFER_SIZE = "buffer_size";
	public static final String STEP_ATTRIBUTE_FLUSH_INTERVAL = "flush_interval";
	public static final String STEP_ATTRIBUTE_COMPRESSED = "compressed";
//	port          = rep.getStepAttributeString (id_step, "port");
//    bufferSize    = rep.getStepAttributeString (id_step, "buffer_size");
//    flushInterval = rep.getStepAttributeString (id_step, "flush_interval");
//    compressed    = rep.getStepAttributeBoolean(id_step, "compressed");
    
    private String port;
    private String bufferSize;
    private String flushInterval;
    private boolean compressed;
    
	public SocketWriterMeta()
	{
		super(); // allocate BaseStepMeta
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
		throws KettleXMLException
	{
		readData(stepnode);
	}

	public Object clone()
	{
		Object retval = super.clone();
		return retval;
	}
	
    public String getXML()
    {
        StringBuffer xml = new StringBuffer();
        
        xml.append("     "+XMLHandler.addTagValue("port", port));
        xml.append("     "+XMLHandler.addTagValue("buffer_size", bufferSize));
        xml.append("     "+XMLHandler.addTagValue("flush_interval", flushInterval));
        xml.append("     "+XMLHandler.addTagValue("compressed", compressed));

        return xml.toString();
    }
    
	private void readData(Node stepnode)
	{
        port     = XMLHandler.getTagValue(stepnode, "port");
        bufferSize    = XMLHandler.getTagValue(stepnode, "buffer_size");
        flushInterval = XMLHandler.getTagValue(stepnode, "flush_interval");
        compressed = "Y".equalsIgnoreCase( XMLHandler.getTagValue(stepnode, "compressed") );
	}

	public void setDefault()
	{
        bufferSize = "2000";
        flushInterval = "5000";
        compressed = true;
	}

	@Override
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {

		port = BaseStepMeta.parameterToString(p.get(id + ".port"));
		bufferSize = BaseStepMeta.parameterToString(p.get(id + ".bufferSize"));
		flushInterval = BaseStepMeta.parameterToString(p.get(id + ".flushInterval"));
		compressed = BaseStepMeta.parameterToBoolean(p.get(id + ".compressed"));
		
	}
	
	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException
	{
        port          = rep.getStepAttributeString (id_step, STEP_ATTRIBUTE_PORT );
        bufferSize    = rep.getStepAttributeString (id_step, STEP_ATTRIBUTE_BUFFER_SIZE );
        flushInterval = rep.getStepAttributeString (id_step, STEP_ATTRIBUTE_FLUSH_INTERVAL );
        compressed    = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_COMPRESSED );
	}
	
	public void saveRep(Repository rep, long id_transformation, long id_step) throws KettleException
	{
        rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_PORT, port);
        rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_BUFFER_SIZE, bufferSize);
        rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_FLUSH_INTERVAL, flushInterval);
        rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_COMPRESSED, compressed);
	}
	
	public void getFields(RowMetaInterface rowMeta, String origin, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) throws KettleStepException
	{
		// Default: nothing changes to rowMeta
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepinfo, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
		CheckResult cr;
		if (prev==null || prev.size()==0)
		{
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_WARNING, Messages.getString("SocketWriterMeta.CheckResult.NotReceivingFields"), stepinfo); //$NON-NLS-1$
			remarks.add(cr);
		}
		else
		{
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("SocketWriterMeta.CheckResult.StepRecevingData",prev.size()+""), stepinfo); //$NON-NLS-1$ //$NON-NLS-2$
			remarks.add(cr);
		}
		
		// See if we have input streams leading to this step!
		if (input.length>0)
		{
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("SocketWriterMeta.CheckResult.StepRecevingData2"), stepinfo); //$NON-NLS-1$
			remarks.add(cr);
		}
		else
		{
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("SocketWriterMeta.CheckResult.NoInputReceivedFromOtherSteps"), stepinfo); //$NON-NLS-1$
			remarks.add(cr);
		}
	}
	
	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta tr, Trans trans)
	{
		return new SocketWriter(stepMeta, stepDataInterface, cnr, tr, trans);
	}
	
	public StepDataInterface getStepData()
	{
		return new SocketWriterData();
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

    public String getBufferSize()
    {
        return bufferSize;
    }
    
    public void setBufferSize(String bufferSize)
    {
        this.bufferSize = bufferSize;
    }

    public String getFlushInterval()
    {
        return flushInterval;
    }
    
    public void setFlushInterval(String flushInterval)
    {
        this.flushInterval = flushInterval;
    }

    /**
     * @return the compressed
     */
    public boolean isCompressed()
    {
        return compressed;
    }

    /**
     * @param compressed the compressed to set
     */
    public void setCompressed(boolean compressed)
    {
        this.compressed = compressed;
    }
 
}
