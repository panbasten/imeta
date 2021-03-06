package com.panet.imeta.trans.steps.formula;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.jfree.formula.ContextEvaluationException;
import org.jfree.formula.DefaultErrorValue;
import org.jfree.formula.DefaultFormulaContext;
import org.jfree.formula.ErrorValue;
import org.jfree.formula.FormulaContext;
import org.jfree.formula.LocalizationContext;
import org.jfree.formula.function.FunctionRegistry;
import org.jfree.formula.operators.OperatorFactory;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.coretypes.AnyType;
import org.jfree.util.Configuration;

import com.panet.imeta.core.exception.KettleValueException;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.row.ValueMetaInterface;

public class RowForumulaContext implements FormulaContext
{
    private RowMetaInterface rowMeta;
    private FormulaContext formulaContext;
    private Map<String, Integer> valueIndexMap;
	private Object[] rowData;
    
    public RowForumulaContext(RowMetaInterface row)
    {
        this.formulaContext = new DefaultFormulaContext();
        this.rowMeta = row;
        this.rowData = null;
        this.valueIndexMap = new Hashtable<String, Integer>();
    }
    
    public Type resolveReferenceType(Object name) throws ContextEvaluationException
    {
        return AnyType.TYPE;
    }

    /**
     * We return the content of a Value with the given name.  We cache the position of the field indexes. 
     * 
     * @see org.jfree.formula.FormulaContext#resolveReference(java.lang.Object)
     */
    public Object resolveReference(Object name) throws ContextEvaluationException
    {
        if(name instanceof String)
        {
            ValueMetaInterface valueMeta;
            Integer idx = (Integer) valueIndexMap.get(name);
            if (idx!=null)
            {
                valueMeta = rowMeta.getValueMeta(idx.intValue());
            }
            else
            {
                int index = rowMeta.indexOfValue((String) name);
                if (index<0)
                {
                	ErrorValue errorValue = new DefaultErrorValue((String)name, -1, "Value with name '"+name+"' could not be found in the row.");
    				throw new ContextEvaluationException(errorValue);
                }
                valueMeta = rowMeta.getValueMeta(index);
                idx = new Integer(index);
                valueIndexMap.put((String)name, idx);
            }
            Object valueData = rowData[idx];
            try {
				return getPrimitive(valueMeta, valueData);
			} catch (KettleValueException e) {
				ErrorValue errorValue = new DefaultErrorValue(valueMeta.getName(), -1, e.toString());
				throw new ContextEvaluationException(errorValue);
			}
        }
        return null;
    }

    public Configuration getConfiguration()
    {
      return formulaContext.getConfiguration();
    }

    public FunctionRegistry getFunctionRegistry()
    {
      return formulaContext.getFunctionRegistry();
    }

    public LocalizationContext getLocalizationContext()
    {
      return formulaContext.getLocalizationContext();
    }

    public OperatorFactory getOperatorFactory()
    {
      return formulaContext.getOperatorFactory();
    }

    public TypeRegistry getTypeRegistry()
    {
      return formulaContext.getTypeRegistry();
    }

    public boolean isReferenceDirty(Object name) throws ContextEvaluationException
    {
      return formulaContext.isReferenceDirty(name);
    }

    /**
     * @return the row
     */
    public RowMetaInterface getRowMeta()
    {
        return rowMeta;
    }

    /**
     * @param rowMeta the row to set
     */
    public void setRowMeta(RowMetaInterface rowMeta)
    {
        this.rowMeta = rowMeta;
    }
    
    /**
     * @param rowData the new row of data to inject
     */
    public void setRowData(Object[] rowData)
    {
    	this.rowData = rowData;
    }
    
    /**
     * @return the current row of data
     */
    public Object[] getRowData() 
    {
		return rowData;
	}
    

    public static Object getPrimitive(ValueMetaInterface valueMeta, Object valueData) throws KettleValueException
    {
        switch(valueMeta.getType())
        {
        case ValueMetaInterface.TYPE_BIGNUMBER: return valueMeta.getBigNumber(valueData);
        case ValueMetaInterface.TYPE_BINARY: return valueMeta.getBinary(valueData);
        case ValueMetaInterface.TYPE_BOOLEAN: return valueMeta.getBoolean(valueData);
        case ValueMetaInterface.TYPE_DATE: return valueMeta.getDate(valueData);
        case ValueMetaInterface.TYPE_INTEGER: valueMeta.getInteger(valueData);
        case ValueMetaInterface.TYPE_NUMBER: return valueMeta.getNumber(valueData);
        // case ValueMetaInterface.TYPE_SERIALIZABLE: return valueMeta.(valueData);
        case ValueMetaInterface.TYPE_STRING: return valueMeta.getString(valueData);
        default: return null;
        }
    }
    
    public static Class<?> getPrimitiveClass(int valueType)
    {
        switch(valueType)
        {
        case ValueMetaInterface.TYPE_BIGNUMBER: return BigDecimal.class;
        case ValueMetaInterface.TYPE_BINARY: return (new byte[] {}).getClass();
        case ValueMetaInterface.TYPE_BOOLEAN: return Boolean.class;
        case ValueMetaInterface.TYPE_DATE: return Date.class;
        case ValueMetaInterface.TYPE_INTEGER: return Long.class;
        case ValueMetaInterface.TYPE_NUMBER: return Double.class;
        // case Value.VALUE_TYPE_SERIALIZABLE: return Serializable.class;
        case ValueMetaInterface.TYPE_STRING: return String.class;
        default: return null;
        }
    }
}
