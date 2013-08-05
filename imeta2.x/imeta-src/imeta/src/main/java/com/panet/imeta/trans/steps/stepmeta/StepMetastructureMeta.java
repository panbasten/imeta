package com.panet.imeta.trans.steps.stepmeta;

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

public class StepMetastructureMeta extends BaseStepMeta implements StepMetaInterface {

	public static final String STEP_ATTRIBUTE_OUTPUTROWCOUNT = "outputRowcount";
	public static final String STEP_ATTRIBUTE_ROWCOUNTFIELD = "rowcountField";
//	outputRowcount          =      rep.getStepAttributeBoolean(id_step, "outputRowcount");
//	rowcountField        =      rep.getStepAttributeString (id_step, "rowcountField");
	private String fieldName;
	private String comments;
	private String typeName;
	private String positionName;
	private String lengthName;
	private String precisionName;
	private String originName;
	
	private boolean outputRowcount;
	private String rowcountField;
	
	
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
        StringBuffer retval=new StringBuffer(500);
        
        retval.append("      ").append(XMLHandler.addTagValue("outputRowcount", outputRowcount));
        retval.append("    ").append(XMLHandler.addTagValue("rowcountField",  rowcountField));

        return retval.toString();
    }
	
	private void readData(Node stepnode) throws KettleXMLException
    {
        try
        {	
        	outputRowcount    = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "outputRowcount"));
        	rowcountField = XMLHandler.getTagValue(stepnode, "rowcountField");
        }
        catch(Exception e)
        {
            throw new KettleXMLException("Unable to load step info from XML", e);
        }
    }
	
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		outputRowcount = BaseStepMeta.parameterToBoolean(p.get(id + ".outputRowcount"));
		rowcountField = BaseStepMeta.parameterToString(p.get(id + ".rowcountField"));
	}
	
	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException {
        try
        {
        	outputRowcount          =      rep.getStepAttributeBoolean(id_step, "outputRowcount");
        	rowcountField        =      rep.getStepAttributeString (id_step, "rowcountField");
    
        }
        catch(Exception e)
        {
            throw new KettleException("Unexpected error reading step information from the repository", e);
        }
    }
	
	public void saveRep(Repository rep, long id_transformation, long id_step) throws KettleException
    {
        try
        {
            rep.saveStepAttribute(id_transformation, id_step, "outputRowcount",      outputRowcount);
            rep.saveStepAttribute(id_transformation, id_step, "rowcountField",       rowcountField);
            
        }
        catch(Exception e)
        {
            throw new KettleException("Unable to save step information to the repository for id_step="+id_step, e);
        }
    }
	
	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta tr, Trans trans)
	{
		return new StepMetastructure(stepMeta, stepDataInterface, cnr, tr, trans);
	}
	
	public StepDataInterface getStepData()
	{
		return new StepMetastructureData();
	}
	
	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepinfo, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
		CheckResult cr;
		
		
		cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, "Not implemented", stepinfo);
		remarks.add(cr);
		
	}
	
	public void getFields(RowMetaInterface r, String name, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) throws KettleStepException
    {
        //we create a new output row structure - clear r
		r.clear();
        
		this.setDefault();
		// create the new fields
		//Position
        ValueMetaInterface positionFieldValue = new ValueMeta(positionName, ValueMetaInterface.TYPE_INTEGER);
        positionFieldValue.setOrigin(name);
        r.addValueMeta(positionFieldValue);
        //field name
        ValueMetaInterface nameFieldValue = new ValueMeta(fieldName, ValueMetaInterface.TYPE_STRING);
        nameFieldValue.setOrigin(name);
        r.addValueMeta(nameFieldValue);
        //comments
        ValueMetaInterface commentsFieldValue = new ValueMeta(comments, ValueMetaInterface.TYPE_STRING);
        nameFieldValue.setOrigin(name);
        r.addValueMeta(commentsFieldValue);
        //Type
        ValueMetaInterface typeFieldValue = new ValueMeta(typeName, ValueMetaInterface.TYPE_STRING);
        typeFieldValue.setOrigin(name);
        r.addValueMeta(typeFieldValue);
        //Length
        ValueMetaInterface lengthFieldValue = new ValueMeta(lengthName, ValueMetaInterface.TYPE_INTEGER);
        lengthFieldValue.setOrigin(name);
        r.addValueMeta(lengthFieldValue);
        //Precision
        ValueMetaInterface precisionFieldValue = new ValueMeta(precisionName, ValueMetaInterface.TYPE_INTEGER);
        precisionFieldValue.setOrigin(name);
        r.addValueMeta(precisionFieldValue);
        //Origin
        ValueMetaInterface originFieldValue = new ValueMeta(originName, ValueMetaInterface.TYPE_STRING);
        originFieldValue.setOrigin(name);
        r.addValueMeta(originFieldValue);
        
        if (isOutputRowcount()) {
	        //RowCount
	        ValueMetaInterface v=new ValueMeta(this.getRowcountField(), ValueMetaInterface.TYPE_INTEGER);
	        v.setOrigin(name);
	        r.addValueMeta( v );
        }

    }
	
	public void setDefault()
	{
		positionName = Messages.getString("StepMetastructureMeta.PositionName");
    	fieldName = Messages.getString("StepMetastructureMeta.FieldName");
    	comments = Messages.getString("StepMetastructureMeta.Comments");
        typeName = Messages.getString("StepMetastructureMeta.TypeName");
    	lengthName = Messages.getString("StepMetastructureMeta.LengthName");
    	precisionName = Messages.getString("StepMetastructureMeta.PrecisionName");
    	originName = Messages.getString("StepMetastructureMeta.OriginName");
    	
	}
	
	public boolean isOutputRowcount() {
		return outputRowcount;
	}

	public void setOutputRowcount(boolean outputRowcount) {
		this.outputRowcount = outputRowcount;
	}

	public String getRowcountField() {
		return rowcountField;
	}

	public void setRowcountField(String rowcountField) {
		this.rowcountField = rowcountField;
	}

}
