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

package com.panet.imeta.trans.steps.http;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.core.CheckResult;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Counter;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleStepException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.core.row.ValueMetaInterface;
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
 * Created on 10-dec-2006
 * 
 */
public class HTTPMeta extends BaseStepMeta implements StepMetaInterface
{
    /** URL / service to be called */
    private String  url;

    /** function arguments : fieldname*/
    private String  argumentField[];

    /** IN / OUT / INOUT */
    private String  argumentParameter[];

    /** function result: new value name */
    private String  fieldName;
    
    private boolean urlInField;
    
    private String urlField;
    

    public HTTPMeta()
    {
        super(); // allocate BaseStepMeta
    }

    /**
     * @return Returns the argument.
     */
    public String[] getArgumentField()
    {
        return argumentField;
    }

    /**
     * @param argument The argument to set.
     */
    public void setArgumentField(String[] argument)
    {
        this.argumentField = argument;
    }

    /**
     * @return Returns the argumentDirection.
     */
    public String[] getArgumentParameter()
    {
        return argumentParameter;
    }

    /**
     * @param argumentDirection The argumentDirection to set.
     */
    public void setArgumentParameter(String[] argumentDirection)
    {
        this.argumentParameter = argumentDirection;
    }

    /**
     * @return Returns the procedure.
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * @param procedure The procedure to set.
     */
    public void setUrl(String procedure)
    {
        this.url = procedure;
    }

    /**
     * @return Returns the resultName.
     */
    public String getFieldName()
    {
        return fieldName;
    }

    /**
     * @param resultName The resultName to set.
     */
    public void setFieldName(String resultName)
    {
        this.fieldName = resultName;
    }
    /**
     * @return Is the url coded in a field?
     */
	public boolean isUrlInField() {
		return urlInField;
	}
	
	/**
     * @param urlInField Is the url coded in a field?
     */
	public void setUrlInField(boolean urlInField) {
		this.urlInField = urlInField;
	}
	/**
     * @return The field name that contains the url.
     */
	public String getUrlField() {
		return urlField;
	}
	
	/**
     * @param urlField name of the field that contains the url
     */
	public void setUrlField(String urlField) {
		this.urlField = urlField;
	}
    public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleXMLException
    {
        readData(stepnode, databases);
    }

    public void allocate(int nrargs)
    {
        argumentField = new String[nrargs];
        argumentParameter = new String[nrargs];
    }

    public Object clone()
    {
        HTTPMeta retval = (HTTPMeta) super.clone();
        int nrargs = argumentField.length;

        retval.allocate(nrargs);

        for (int i = 0; i < nrargs; i++)
        {
            retval.argumentField[i] = argumentField[i];
            retval.argumentParameter[i] = argumentParameter[i];
        }

        return retval;
    }

    public void setDefault()
    {
        int i;
        int nrargs;

        nrargs = 0;

        allocate(nrargs);

        for (i = 0; i < nrargs; i++)
        {
            argumentField[i] = "arg" + i; //$NON-NLS-1$
            argumentParameter[i] = "arg"; //$NON-NLS-1$
        }

        fieldName = "result"; //$NON-NLS-1$
    }

    public void getFields(RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) throws KettleStepException    
    {
        if (!Const.isEmpty(fieldName))
        {
            ValueMetaInterface v = new ValueMeta(fieldName, ValueMeta.TYPE_STRING);
            inputRowMeta.addValueMeta(v);
        }
    }
    
    public String getXML()
    {
        StringBuffer retval = new StringBuffer(300);

        retval.append("    ").append(XMLHandler.addTagValue("url", url)); //$NON-NLS-1$ //$NON-NLS-2$
        retval.append("    "+XMLHandler.addTagValue("urlInField",  urlInField));
        retval.append("    "+XMLHandler.addTagValue("urlField",  urlField));
        retval.append("    <lookup>").append(Const.CR); //$NON-NLS-1$

        for (int i = 0; i < argumentField.length; i++)
        {
            retval.append("      <arg>").append(Const.CR); //$NON-NLS-1$
            retval.append("        ").append(XMLHandler.addTagValue("name", argumentField[i])); //$NON-NLS-1$ //$NON-NLS-2$
            retval.append("        ").append(XMLHandler.addTagValue("parameter", argumentParameter[i])); //$NON-NLS-1$ //$NON-NLS-2$
            retval.append("      </arg>").append(Const.CR); //$NON-NLS-1$
        }

        retval.append("    </lookup>").append(Const.CR); //$NON-NLS-1$

        retval.append("    <result>").append(Const.CR); //$NON-NLS-1$
        retval.append("      ").append(XMLHandler.addTagValue("name", fieldName)); //$NON-NLS-1$ //$NON-NLS-2$
        retval.append("    </result>").append(Const.CR); //$NON-NLS-1$

        return retval.toString();
    }

    private void readData(Node stepnode, List<? extends SharedObjectInterface> databases) throws KettleXMLException
    {
        try
        {
            int nrargs;

            url = XMLHandler.getTagValue(stepnode, "url"); //$NON-NLS-1$
            urlInField="Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "urlInField"));
            urlField       = XMLHandler.getTagValue(stepnode, "urlField");
			
            Node lookup = XMLHandler.getSubNode(stepnode, "lookup"); //$NON-NLS-1$
            nrargs = XMLHandler.countNodes(lookup, "arg"); //$NON-NLS-1$

            allocate(nrargs);

            for (int i = 0; i < nrargs; i++)
            {
                Node anode = XMLHandler.getSubNodeByNr(lookup, "arg", i); //$NON-NLS-1$

                argumentField[i] = XMLHandler.getTagValue(anode, "name"); //$NON-NLS-1$
                argumentParameter[i] = XMLHandler.getTagValue(anode, "parameter"); //$NON-NLS-1$
            }

            fieldName = XMLHandler.getTagValue(stepnode, "result", "name"); // Optional, can be null //$NON-NLS-1$
        }
        catch (Exception e)
        {
            throw new KettleXMLException(Messages.getString("HTTPMeta.Exception.UnableToReadStepInfo"), e); //$NON-NLS-1$
        }
    }

    @Override
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		// TODO Auto-generated method stub
    	url = BaseStepMeta.parameterToString(p.get(id + ".url"));
    	urlInField = BaseStepMeta.parameterToBoolean(p.get(id + ".urlInField"));
    	urlField = BaseStepMeta.parameterToString(p.get(id + ".urlField"));
    	fieldName = BaseStepMeta.parameterToString(p.get(id + ".fieldName"));
    	
    	String[] argumentField = p.get(id + "_fields.argumentField");
    	String[] argumentParameter = p.get(id + "_fields.argumentParameter");

		this.argumentField = argumentField;
		this.argumentParameter = argumentParameter;
	}
    
    public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException
    {
        try
        {
            url = rep.getStepAttributeString(id_step, "url"); //$NON-NLS-1$
            urlInField =      rep.getStepAttributeBoolean (id_step, "urlInField");
            urlField	=	   rep.getStepAttributeString (id_step, "urlField");
			
            int nrargs = rep.countNrStepAttributes(id_step, "arg_name"); //$NON-NLS-1$
            allocate(nrargs);

            for (int i = 0; i < nrargs; i++)
            {
                argumentField[i] = rep.getStepAttributeString(id_step, i, "arg_name"); //$NON-NLS-1$
                argumentParameter[i] = rep.getStepAttributeString(id_step, i, "arg_parameter"); //$NON-NLS-1$
            }

            fieldName = rep.getStepAttributeString(id_step, "result_name"); //$NON-NLS-1$
        }
        catch (Exception e)
        {
            throw new KettleException(Messages.getString("HTTPMeta.Exception.UnexpectedErrorReadingStepInfo"), e); //$NON-NLS-1$
        }
    }

    public void saveRep(Repository rep, long id_transformation, long id_step) throws KettleException
    {
        try
        {
            rep.saveStepAttribute(id_transformation, id_step, "url", url); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "urlInField",   urlInField);
			rep.saveStepAttribute(id_transformation, id_step, "urlField",   urlField);
			
            for (int i = 0; i < argumentField.length; i++)
            {
                rep.saveStepAttribute(id_transformation, id_step, i, "arg_name", argumentField[i]); //$NON-NLS-1$
                rep.saveStepAttribute(id_transformation, id_step, i, "arg_parameter", argumentParameter[i]); //$NON-NLS-1$
            }

            rep.saveStepAttribute(id_transformation, id_step, "result_name", fieldName); //$NON-NLS-1$
        }
        catch (Exception e)
        {
            throw new KettleException(Messages.getString("HTTPMeta.Exception.UnableToSaveStepInfo") + id_step, e); //$NON-NLS-1$
        }
    }

    public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
    {
        CheckResult cr;

        // See if we have input streams leading to this step!
        if (input.length > 0)
        {
            cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("HTTPMeta.CheckResult.ReceivingInfoFromOtherSteps"), stepMeta); //$NON-NLS-1$
            remarks.add(cr);
        }
        else
        {
            cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("HTTPMeta.CheckResult.NoInpuReceived"), stepMeta); //$NON-NLS-1$
            remarks.add(cr);
        }
        // check Url
        if(urlInField)
        {
        	if(Const.isEmpty(urlField))
        		cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("HTTPMeta.CheckResult.UrlfieldMissing"), stepMeta);	
        	else
        		cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("HTTPMeta.CheckResult.UrlfieldOk"), stepMeta);	
        	
        }else
        {
        	if(Const.isEmpty(url))
        		cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("HTTPMeta.CheckResult.UrlMissing"), stepMeta);
        	else
        		cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("HTTPMeta.CheckResult.UrlOk"), stepMeta);
        }
        remarks.add(cr);
    }

    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans trans)
    {
        return new HTTP(stepMeta, stepDataInterface, cnr, transMeta, trans);
    }

    public StepDataInterface getStepData()
    {
        return new HTTPData();
    }

    public boolean supportsErrorHandling()
    {
        return true;
    }
}
