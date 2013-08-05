package com.panet.imeta.trans.steps.blockuntilstepsfinish;

import java.util.concurrent.ConcurrentHashMap;

import com.panet.imeta.trans.step.BaseStepData;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;

public class BlockUntilStepsFinishData extends BaseStepData implements
		StepDataInterface {

	/**
	 * 
	 */
	boolean continueLoop;
	public ConcurrentHashMap<Integer, StepInterface> stepInterfaces;

	public BlockUntilStepsFinishData() {
		super();
		continueLoop = true;
	}

}
