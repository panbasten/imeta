package com.panet.imeta.trans.step;

import com.panet.imeta.trans.Trans;

public interface StepListener {
	public void stepFinished(Trans trans, StepMeta stepMeta, StepInterface step);
}
