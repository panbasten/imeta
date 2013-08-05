package com.panet.imeta.lineage;

import java.util.ArrayList;
import java.util.List;

import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.StepMeta;

/**
 * This class contains value lineage information.<br>
 * That means that we will have information on where and how a certain value is originating, being manipulated etc.<br>
 * 
 * @author matt
 *
 */
public class ValueLineage {
	private TransMeta transMeta;
	private ValueMeta valueMeta;
	
	private List<StepMeta> sourceSteps;
	
	/**
	 * Create a new ValueLineage object with an empty set of source steps.
	 * @param valueMeta
	 */
	public ValueLineage(TransMeta transMeta, ValueMeta valueMeta) {
		this.transMeta = transMeta;
		this.valueMeta = valueMeta;
		this.sourceSteps = new ArrayList<StepMeta>();
	}

	/**
	 * @return the transMeta
	 */
	public TransMeta getTransMeta() {
		return transMeta;
	}

	/**
	 * @param transMeta the transMeta to set
	 */
	public void setTransMeta(TransMeta transMeta) {
		this.transMeta = transMeta;
	}

	/**
	 * @return the valueMeta
	 */
	public ValueMeta getValueMeta() {
		return valueMeta;
	}

	/**
	 * @param valueMeta the valueMeta to set
	 */
	public void setValueMeta(ValueMeta valueMeta) {
		this.valueMeta = valueMeta;
	}

	/**
	 * @return the sourceSteps
	 */
	public List<StepMeta> getSourceSteps() {
		return sourceSteps;
	}

	/**
	 * @param sourceSteps the sourceSteps to set
	 */
	public void setSourceSteps(List<StepMeta> sourceSteps) {
		this.sourceSteps = sourceSteps;
	}
	
	
}
