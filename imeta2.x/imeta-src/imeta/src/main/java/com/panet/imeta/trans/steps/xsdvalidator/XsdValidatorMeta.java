 /**********************************************************************
 **                                                                   **
 **               This code belongs to the KETTLE project.            **
 **                                                                   **
 ** Kettle, from version 2.2 on, is released into the public domain   **
 ** under the Lesser GNU Public License (LGPL).                       **
 **                                                                   **
 ** For more details, please read the document LICENSE.txt, included  **
 ** in this project                                                   **
 **                                                                   **
 ** http://www.kettle.be                                              **
 ** info@kettle.be                                                    **
 **                                                                   **
 **********************************************************************/

package com.panet.imeta.trans.steps.xsdvalidator;


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
 * Created on 14-08-2007
 *
 */

public class XsdValidatorMeta extends BaseStepMeta implements StepMetaInterface
{
	public static final String STEP_ATTRIBUTE_XDSFILENAME = "xdsfilename";
	public static final String STEP_ATTRIBUTE_XMLSTREAM = "xmlstream";
	public static final String STEP_ATTRIBUTE_RESULTFIELDNAME = "resultfieldname";
	public static final String STEP_ATTRIBUTE_XMLSOURECEFILE = "xmlsourcefile";
	public static final String STEP_ATTRIBUTE_ADDVALIDATIONMSG = "addvalidationmsg";
	public static final String STEP_ATTRIBUTE_VALIDATIONMSGFIELD = "validationmsgfield";
	public static final String STEP_ATTRIBUTE_IFXMLVALID = "ifxmlvalid";
	public static final String STEP_ATTRIBUTE_IFXMLUNVALID = "ifxmlunvalid";
	public static final String STEP_ATTRIBUTE_OUTPUTSTRINGFIELD = "outputstringfield";
	public static final String STEP_ATTRIBUTE_XSDDEFINEDFIELD = "xsddefinedfield";
	public static final String STEP_ATTRIBUTE_XSDSOURCE = "xsdsource";
	
//	xdsFilename     = rep.getStepAttributeString(id_step, "xdsfilename"); //$NON-NLS-1$
//	xmlStream     = rep.getStepAttributeString(id_step, "xmlstream"); //$NON-NLS-1$
//	resultFieldname     = rep.getStepAttributeString(id_step, "resultfieldname"); //$NON-NLS-1$
//	
//	xmlSourceFile    =      rep.getStepAttributeBoolean(id_step, "xmlsourcefile"); 
//	addValidationMessage    =      rep.getStepAttributeBoolean(id_step, "addvalidationmsg"); 
//	validationMessageField     = rep.getStepAttributeString(id_step, "validationmsgfield");
//	ifXmlValid     = rep.getStepAttributeString(id_step, "ifxmlvalid");
//	ifXmlInvalid     = rep.getStepAttributeString(id_step, "ifxmlunvalid");
//	
//	outputStringField    =      rep.getStepAttributeBoolean(id_step, "outputstringfield"); 
//	xsdDefinedField     = rep.getStepAttributeString(id_step, "xsddefinedfield");
//	xsdSource     = rep.getStepAttributeString(id_step, "xsdsource");
	
	private String  xsdFileName;
	private String  xmlStream;
	private String  resultFieldName;
	private boolean addValidationMessage;
	private String validationMessageField;
	private boolean outputStringField;
	private String ifXmlValid;
	private String ifXmlInvalid;
	private boolean xmlSourceFile;
	private String xsdDefinedField;
	
	private String xsdSource;
	
	public String SPECIFY_FILENAME="filename";
	public String SPECIFY_FIELDNAME="fieldname";
	public String NO_NEED="noneed";
	
	
	public void setXSDSource(String xsdsourcein)
	{
		this.xsdSource=xsdsourcein;
	}
	
	public String getXSDSource()
	{
		return xsdSource;
	}
	
	public void setXSDDefinedField(String xsddefinedfieldin)
	{
		this.xsdDefinedField=xsddefinedfieldin;
	}
	
	public String getXSDDefinedField()
	{
		return xsdDefinedField;
	}
	
	public boolean getXMLSourceFile()
	{
		return xmlSourceFile;
	}
	
	public void setXMLSourceFile(boolean xmlsourcefilein)
	{
		this.xmlSourceFile=xmlsourcefilein;
	}
	
	public String getIfXmlValid()
	{
		return ifXmlValid;
	}
	
	public String getIfXmlInvalid()
	{
		return ifXmlInvalid;
	}
	
	public void setIfXMLValid(String ifXmlValid)
	{
		this.ifXmlValid=ifXmlValid;
	}
	
	
	public void setIfXmlInvalid(String ifXmlInvalid)
	{
		this.ifXmlInvalid=ifXmlInvalid;
	}
	
	
	public boolean getOutputStringField()
	{
		return outputStringField;
	}
	
	
	public void setOutputStringField(boolean outputStringField)
	{
		this.outputStringField=outputStringField;
	}
	
	public String getValidationMessageField()
	{
		return validationMessageField;
	}
	
	public void setValidationMessageField(String validationMessageField)
	{
		this.validationMessageField=validationMessageField;
	}
	
	public boolean useAddValidationMessage()
	{
		return addValidationMessage;
	}
	
	
	public void setAddValidationMessage(boolean addValidationMessage)
	{
		this.addValidationMessage=addValidationMessage;
	}
	
	public XsdValidatorMeta()
	{
		super(); // allocate BaseStepMeta
	}
	

	    
    /**
     * @return Returns the XSD filename.
     */
    public String getXSDFilename()
    {
        return xsdFileName;
    }
    
    public String getResultfieldname()
    {
    	return resultFieldName;
    }
    

    public String getXMLStream()
    {
    	return xmlStream;
    }
    
    /**
     * @param xdsFilename The XSD filename to set.
     */
    public void setXSDfilename(String xdsFilename)
    {
        this.xsdFileName = xdsFilename;
    }
    
    public void setResultfieldname(String resultFieldname)
    {
        this.resultFieldName = resultFieldname;
    }
    

    
    public void setXMLStream(String xmlStream)
    {
        this.xmlStream =  xmlStream;
    }
    

	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
	throws KettleXMLException
	{
	readData(stepnode);
	}
	

	public Object clone()
	{
		XsdValidatorMeta retval = (XsdValidatorMeta)super.clone();
		

		return retval;
	}
	
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		resultFieldName = BaseStepMeta.parameterToString(p.get(id + ".resultFieldName"));
		xsdFileName = BaseStepMeta.parameterToString(p.get(id + ".xsdFileName"));
		xmlStream = BaseStepMeta.parameterToString(p.get(id + ".xmlStream"));
		addValidationMessage= BaseStepMeta.parameterToBoolean(p.get(id + ".addValidationMessage"));
		validationMessageField= BaseStepMeta.parameterToString(p.get(id + ".validationMessageField"));
		outputStringField= BaseStepMeta.parameterToBoolean(p.get(id + ".outputStringField"));
		ifXmlValid= BaseStepMeta.parameterToString(p.get(id + ".ifXmlValid"));
		ifXmlInvalid= BaseStepMeta.parameterToString(p.get(id + ".ifXmlInvalid"));
		xmlSourceFile= BaseStepMeta.parameterToBoolean(p.get(id + ".xmlSourceFile"));
		xsdDefinedField= BaseStepMeta.parameterToString(p.get(id + ".xsdDefinedField"));
		xsdSource= BaseStepMeta.parameterToString(p.get(id + ".xsdSource"));
	}
    
	private void readData(Node stepnode)
		throws KettleXMLException
	{
		try
		{

			
			xsdFileName     = XMLHandler.getTagValue(stepnode, "xdsfilename"); //$NON-NLS-1$
			xmlStream     = XMLHandler.getTagValue(stepnode, "xmlstream"); //$NON-NLS-1$
			resultFieldName     = XMLHandler.getTagValue(stepnode, "resultfieldname"); //$NON-NLS-1$
			xsdDefinedField     = XMLHandler.getTagValue(stepnode, "xsddefinedfield");
			xsdSource     = XMLHandler.getTagValue(stepnode, "xsdsource");
			
			
			addValidationMessage = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "addvalidationmsg"));
			
			validationMessageField     = XMLHandler.getTagValue(stepnode, "validationmsgfield"); //$NON-NLS-1$
			ifXmlValid     = XMLHandler.getTagValue(stepnode, "ifxmlvalid");
			ifXmlInvalid     = XMLHandler.getTagValue(stepnode, "ifxmlunvalid");
			outputStringField = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "outputstringfield"));
			xmlSourceFile = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "xmlsourcefile"));
			
	
		}
		catch(Exception e)
		{
			throw new KettleXMLException(Messages.getString("XsdValidatorMeta.Exception.UnableToLoadStepInfoFromXML"), e); //$NON-NLS-1$
		}
	}

	public void setDefault()
	{
		xsdFileName = ""; //$NON-NLS-1$
		xmlStream = "";
		resultFieldName="result";
		addValidationMessage=false;
		validationMessageField="ValidationMsgField";
		ifXmlValid="";
		ifXmlInvalid="";
		outputStringField=false;
		xmlSourceFile=false;
		xsdDefinedField="";
		xsdSource=SPECIFY_FILENAME;
		

	}
	public void getFields(RowMetaInterface inputRowMeta, String name, RowMetaInterface info[], StepMeta nextStep, VariableSpace space) throws KettleStepException
	{
	   if (!Const.isEmpty(resultFieldName))
        {
		   if (outputStringField)
	        {
	            // Output field (String)	
	            ValueMetaInterface v = new ValueMeta(space.environmentSubstitute(getResultfieldname()), ValueMeta.TYPE_STRING);
	            inputRowMeta.addValueMeta(v);
	        }
		   else
	        {
	         	
	            // Output field (boolean)	
	            ValueMetaInterface v = new ValueMeta(space.environmentSubstitute(getResultfieldname()), ValueMeta.TYPE_BOOLEAN);
	            inputRowMeta.addValueMeta(v);
	        }  

        }
	   // Add String Field that contain validation message (most the time, errors)
        if(addValidationMessage && !Const.isEmpty(validationMessageField))
        {
	          ValueMetaInterface v = new ValueMeta(space.environmentSubstitute(validationMessageField), ValueMeta.TYPE_STRING);
	          inputRowMeta.addValueMeta(v);
        }

    }

	
	public String getXML()
	{
        StringBuffer retval = new StringBuffer();
		
		retval.append("    "+XMLHandler.addTagValue("xdsfilename", xsdFileName)); //$NON-NLS-1$ //$NON-NLS-2$
		retval.append("    "+XMLHandler.addTagValue("xmlstream", xmlStream)); //$NON-NLS-1$ //$NON-NLS-2$
		retval.append("    "+XMLHandler.addTagValue("resultfieldname",resultFieldName));
		retval.append("    "+XMLHandler.addTagValue("addvalidationmsg",addValidationMessage));
		retval.append("    "+XMLHandler.addTagValue("validationmsgfield", validationMessageField));
		retval.append("    "+XMLHandler.addTagValue("ifxmlunvalid", ifXmlInvalid));
		retval.append("    "+XMLHandler.addTagValue("ifxmlvalid", ifXmlValid));
		
		retval.append("    "+XMLHandler.addTagValue("outputstringfield",outputStringField));
		retval.append("    "+XMLHandler.addTagValue("xmlsourcefile",xmlSourceFile));
		retval.append("    "+XMLHandler.addTagValue("xsddefinedfield", xsdDefinedField));
		retval.append("    "+XMLHandler.addTagValue("xsdsource", xsdSource));
		
		return retval.toString();
	}


	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException
	{
		try
		{
			xsdFileName     = rep.getStepAttributeString(id_step, "xdsfilename"); //$NON-NLS-1$
			xmlStream     = rep.getStepAttributeString(id_step, "xmlstream"); //$NON-NLS-1$
			resultFieldName     = rep.getStepAttributeString(id_step, "resultfieldname"); //$NON-NLS-1$
			
			xmlSourceFile    =      rep.getStepAttributeBoolean(id_step, "xmlsourcefile"); 
			addValidationMessage    =      rep.getStepAttributeBoolean(id_step, "addvalidationmsg"); 
			validationMessageField     = rep.getStepAttributeString(id_step, "validationmsgfield");
			ifXmlValid     = rep.getStepAttributeString(id_step, "ifxmlvalid");
			ifXmlInvalid     = rep.getStepAttributeString(id_step, "ifxmlunvalid");
			
			outputStringField    =      rep.getStepAttributeBoolean(id_step, "outputstringfield"); 
			xsdDefinedField     = rep.getStepAttributeString(id_step, "xsddefinedfield");
			xsdSource     = rep.getStepAttributeString(id_step, "xsdsource");
			
		}
		catch(Exception e)
		{
			throw new KettleException(Messages.getString("XsdValidatorMeta.Exception.UnexpectedErrorInReadingStepInfo"), e); //$NON-NLS-1$
		}
	}

	public void saveRep(Repository rep, long id_transformation, long id_step)
		throws KettleException
	{
		try
		{
			rep.saveStepAttribute(id_transformation, id_step, "xdsfilename", xsdFileName); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "xmlstream", xmlStream); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "resultfieldname",resultFieldName);
			rep.saveStepAttribute(id_transformation, id_step, "xmlsourcefile",  xmlSourceFile);
			rep.saveStepAttribute(id_transformation, id_step, "addvalidationmsg",  addValidationMessage);
			rep.saveStepAttribute(id_transformation, id_step, "validationmsgfield", validationMessageField); 
			rep.saveStepAttribute(id_transformation, id_step, "ifxmlvalid", ifXmlValid); 
			rep.saveStepAttribute(id_transformation, id_step, "ifxmlunvalid", ifXmlInvalid); 
			rep.saveStepAttribute(id_transformation, id_step, "outputstringfield",  outputStringField);
			rep.saveStepAttribute(id_transformation, id_step, "xsddefinedfield", xsdDefinedField);
			rep.saveStepAttribute(id_transformation, id_step, "xsdsource", xsdSource);
			
			
			
		
		}
		catch(Exception e)
		{
			throw new KettleException(Messages.getString("XsdValidatorMeta.Exception.UnableToSaveStepInfo")+id_step, e); //$NON-NLS-1$
		}
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepinfo,
			RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
		CheckResult cr;
		
		// Check XML stream field
		if(Const.isEmpty(xmlStream))
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("XsdValidatorMeta.CheckResult.XMLStreamFieldEmpty"), stepinfo); //$NON-NLS-1$ //$NON-NLS-2$
			remarks.add(cr);
		}else{
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("XsdValidatorMeta.CheckResult.XMLStreamFieldOK"), stepinfo); //$NON-NLS-1$ //$NON-NLS-2$
			remarks.add(cr);
		}
		
		// Check result fieldname
		if(Const.isEmpty(resultFieldName))
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("XsdValidatorMeta.CheckResult.ResultFieldEmpty"), stepinfo); //$NON-NLS-1$ //$NON-NLS-2$
			remarks.add(cr);
		}else{
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("XsdValidatorMeta.CheckResult.ResultFieldOK"), stepinfo); //$NON-NLS-1$ //$NON-NLS-2$
			remarks.add(cr);
		}
		
		if(xsdSource.equals(SPECIFY_FILENAME))
		{
			if(Const.isEmpty(xsdFileName))
			{
				cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("XsdValidatorMeta.CheckResult.XSDFieldEmpty"), stepinfo); //$NON-NLS-1$ //$NON-NLS-2$
				remarks.add(cr);				
			}
		}
		
		if (prev!=null && prev.size()>0)
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("XsdValidatorMeta.CheckResult.ConnectedStepOK",String.valueOf(prev.size())), stepinfo); //$NON-NLS-1$ //$NON-NLS-2$
			remarks.add(cr);
		}
        else
        {	
            cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("XsdValidatorMeta.CheckResult.NoInputReceived"), stepinfo); //$NON-NLS-1$
            remarks.add(cr);
        }
		
		
			
		  // See if we have input streams leading to this step!
        if (input.length > 0)
        {
            cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("XsdValidatorMeta.CheckResult.ExpectedInputOk"), stepinfo);
            remarks.add(cr);
        }
        else
        {
            cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("XsdValidatorMeta.CheckResult.ExpectedInputError"), stepinfo);
            remarks.add(cr);
        }
	}


	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans trans)
	{
		return new XsdValidator(stepMeta, stepDataInterface, cnr, transMeta, trans);
        
	}

	public StepDataInterface getStepData()
	{
		return new XsdValidatorData();
	}
    
 

    public boolean supportsErrorHandling()
    {
        return true;
    }
}
