/*************************************************************************************** 
 * Copyright (C) 2007 Samatar.  All rights reserved. 
 * This software was developed by Samatar and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. A copy of the license, 
 * is included with the binaries and source code. The Original Code is Samatar.  
 * The Initial Developer is Samatar.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an 
 * "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. 
 * Please refer to the license for the specific language governing your rights 
 * and limitations.
 ***************************************************************************************/
package com.panet.imeta.trans.steps.creditcardvalidator;

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
 * Created on 03-Juin-2008
 * 
 */

public class CreditCardValidatorMeta extends BaseStepMeta implements StepMetaInterface
{
	public static final String STEP_ATTRIBUTE_FIELDNAME = "fieldname";
	public static final String STEP_ATTRIBUTE_RESULTFIELDNAME = "resultfieldname";
	public static final String STEP_ATTRIBUTE_CARDTYPE = "cardtype";
	public static final String STEP_ATTRIBUTE_NOTVALIDMSG = "notvalidmsg";
	public static final String STEP_ATTRIBUTE_ONLYDIGITS = "onlydigits";
//	fieldname = rep.getStepAttributeString(id_step, "fieldname"); //$NON-NLS-1$
   /** dynamic field */
    private String    fieldname;
    
    private String cardtype;
    
    private String notvalidmsg;
    
    /** function result: new value name */
    private String    resultfieldname;
    
    private boolean onlydigits;
    
    public CreditCardValidatorMeta()
    {
        super(); // allocate BaseStepMeta
    }

    /**
     * @return Returns the fieldname.
     */
    public String getDynamicField()
    {
        return this.fieldname;
    }

    /**
     * @param fieldname The fieldname to set.
     */
    public void setDynamicField(String fieldname)
    {
        this.fieldname = fieldname;
    }

    /**
     * @return Returns the resultName.
     */
    public String getResultFieldName()
    {
        return resultfieldname;
    }
    
    public void setOnlyDigits(boolean onlydigits)
    {
    	this.onlydigits=onlydigits;
    }
    
    public boolean isOnlyDigits()
    {
    	return this.onlydigits;
    }


    /**
     * @param resultfieldname The resultfieldname to set.
     */
    public void setResultFieldName(String resultfieldname)
    {
        this.resultfieldname = resultfieldname;
    }
    
    /**
     * @param cardtype The cardtype to set.
     */
    public void setCardType(String cardtype)
    {
        this.cardtype = cardtype;
    }

    /**
     * @return Returns the cardtype.
     */
    public String getCardType()
    {
        return cardtype;
    }
    /**
     * @param notvalidmsg The notvalidmsg to set.
     */
    public void setNotValidMsg(String notvalidmsg)
    {
        this.notvalidmsg = notvalidmsg;
    }
    /**
     * @return Returns the notvalidmsg.
     */
    public String getNotValidMsg()
    {
        return notvalidmsg;
    }
    
    
    public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
	throws KettleXMLException
	{
		readData(stepnode);
	}
 

    public Object clone()
    {
        CreditCardValidatorMeta retval = (CreditCardValidatorMeta) super.clone();
       
        return retval;
    }

    public void setDefault()
    {
        resultfieldname = "result"; //$NON-NLS-1$
        onlydigits=false;
        cardtype="card type";
        notvalidmsg="not valid message";
    }

	public void getFields(RowMetaInterface inputRowMeta, String name, RowMetaInterface info[], StepMeta nextStep, VariableSpace space) throws KettleStepException
	{ 
        String realresultfieldname=space.environmentSubstitute(resultfieldname);
        if (!Const.isEmpty(realresultfieldname))
        {
            ValueMetaInterface v = new ValueMeta(realresultfieldname, ValueMeta.TYPE_BOOLEAN);
			v.setOrigin(name);
			inputRowMeta.addValueMeta(v);
        }
        String realcardtype=space.environmentSubstitute(cardtype);
        if (!Const.isEmpty(realcardtype))
	     {  
	        ValueMetaInterface v = new ValueMeta(realcardtype, ValueMeta.TYPE_STRING);
			v.setOrigin(name);
			inputRowMeta.addValueMeta(v);
	     }
        String realnotvalidmsg=space.environmentSubstitute(notvalidmsg);
        if (!Const.isEmpty(notvalidmsg))
	     {  
		     ValueMetaInterface v = new ValueMeta(realnotvalidmsg, ValueMeta.TYPE_STRING);
		     v.setOrigin(name);
			 inputRowMeta.addValueMeta(v);
	     }
    }
	

    public String getXML()
    {
        StringBuffer retval = new StringBuffer();

        retval.append("    " + XMLHandler.addTagValue("fieldname", fieldname)); //$NON-NLS-1$ //$NON-NLS-2$
        retval.append("    " + XMLHandler.addTagValue("resultfieldname", resultfieldname)); //$NON-NLS-1$ //$NON-NLS-2$
        retval.append("    " + XMLHandler.addTagValue("cardtype", cardtype));
        retval.append("    ").append(XMLHandler.addTagValue("onlydigits", onlydigits));
        retval.append("    " + XMLHandler.addTagValue("notvalidmsg", notvalidmsg));
        
        return retval.toString();
    }

	private void readData(Node stepnode) throws KettleXMLException
	{
	try
	{
			fieldname = XMLHandler.getTagValue(stepnode, "fieldname"); //$NON-NLS-1$
            resultfieldname = XMLHandler.getTagValue(stepnode, "resultfieldname");
            cardtype = XMLHandler.getTagValue(stepnode, "cardtype");  
            notvalidmsg = XMLHandler.getTagValue(stepnode, "notvalidmsg");
            onlydigits  = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "onlydigits"));
            
        }
        catch (Exception e)
        {
            throw new KettleXMLException(Messages.getString("CreditCardValidatorMeta.Exception.UnableToReadStepInfo"), e); //$NON-NLS-1$
        }
    }

	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters)
	throws KettleException
	{
        try
        {
        	fieldname = rep.getStepAttributeString(id_step, "fieldname"); //$NON-NLS-1$
            resultfieldname = rep.getStepAttributeString(id_step, "resultfieldname"); //$NON-NLS-1$
            cardtype = rep.getStepAttributeString(id_step, "cardtype"); //$NON-NLS-1$
            notvalidmsg = rep.getStepAttributeString(id_step, "notvalidmsg"); //$NON-NLS-1$
            onlydigits  = rep.getStepAttributeBoolean(id_step, "onlydigits");
            
        }
        catch (Exception e)
        {
            throw new KettleException(Messages.getString("CreditCardValidatorMeta.Exception.UnexpectedErrorReadingStepInfo"), e); //$NON-NLS-1$
        }
    }

    public void saveRep(Repository rep, long id_transformation, long id_step) throws KettleException
    {
        try
        {
            rep.saveStepAttribute(id_transformation, id_step, "fieldname", fieldname); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, "resultfieldname", resultfieldname); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "cardtype", cardtype);
			rep.saveStepAttribute(id_transformation, id_step, "notvalidmsg", notvalidmsg);
			rep.saveStepAttribute(id_transformation, id_step, "onlydigits",onlydigits);
			
        }
        catch (Exception e)
        {
            throw new KettleException(Messages.getString("CreditCardValidatorMeta.Exception.UnableToSaveStepInfo") + id_step, e); //$NON-NLS-1$
        }
    }

    public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
        CheckResult cr;
        String error_message = ""; //$NON-NLS-1$

        String realresultfieldname=transMeta.environmentSubstitute(resultfieldname);
        if (Const.isEmpty(realresultfieldname))
        {
            error_message = Messages.getString("CreditCardValidatorMeta.CheckResult.ResultFieldMissing"); //$NON-NLS-1$
            cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, error_message, stepMeta);
            remarks.add(cr);
        }
        else
        {
            error_message = Messages.getString("CreditCardValidatorMeta.CheckResult.ResultFieldOK"); //$NON-NLS-1$
            cr = new CheckResult(CheckResult.TYPE_RESULT_OK, error_message, stepMeta);
            remarks.add(cr);
        }
        if (Const.isEmpty(fieldname))
        {
            error_message = Messages.getString("CreditCardValidatorMeta.CheckResult.CardFieldMissing"); //$NON-NLS-1$
            cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, error_message, stepMeta);
            remarks.add(cr);
        }
        else
        {
            error_message = Messages.getString("CreditCardValidatorMeta.CheckResult.CardFieldOK"); //$NON-NLS-1$
            cr = new CheckResult(CheckResult.TYPE_RESULT_OK, error_message, stepMeta);
            remarks.add(cr);
        }
        // See if we have input streams leading to this step!
        if (input.length > 0)
        {
            cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("CreditCardValidatorMeta.CheckResult.ReceivingInfoFromOtherSteps"), stepMeta); //$NON-NLS-1$
            remarks.add(cr);
        }
        else
        {
            cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("CreditCardValidatorMeta.CheckResult.NoInpuReceived"), stepMeta); //$NON-NLS-1$
            remarks.add(cr);
        }

    }

    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans trans)
    {
        return new CreditCardValidator(stepMeta, stepDataInterface, cnr, transMeta, trans);
    }

    public StepDataInterface getStepData()
    {
        return new CreditCardValidatorData();
    }

    public boolean supportsErrorHandling()
    {
        return true;
    }

	@Override
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		// TODO Auto-generated method stub
		fieldname = BaseStepMeta.parameterToString(p.get(id + ".fieldname"));
		cardtype = BaseStepMeta.parameterToString(p.get(id + ".cardtype"));
		notvalidmsg = BaseStepMeta.parameterToString(p.get(id + ".notvalidmsg"));
		resultfieldname= BaseStepMeta.parameterToString(p.get(id + ".resultfieldname"));
		onlydigits= BaseStepMeta.parameterToBoolean(p.get(id + ".onlydigits"));
	}

}
