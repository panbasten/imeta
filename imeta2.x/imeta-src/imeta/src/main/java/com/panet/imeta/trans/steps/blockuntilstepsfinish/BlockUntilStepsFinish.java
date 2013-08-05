package com.panet.imeta.trans.steps.blockuntilstepsfinish;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.panet.imeta.core.Const;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStep;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;

/**
 * Block all incoming rows until defined steps finish processing rows.
 * 
 * @author Samatar
 * @since 30-06-2008
 */

public class BlockUntilStepsFinish extends BaseStep implements StepInterface {
	private BlockUntilStepsFinishMeta meta;
	private BlockUntilStepsFinishData data;

	public BlockUntilStepsFinish(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int copyNr,
			TransMeta transMeta, Trans trans) {
		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
	}

	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi)
			throws KettleException {
		meta = (BlockUntilStepsFinishMeta) smi;
		data = (BlockUntilStepsFinishData) sdi;

		if (first) {
			first = false;
			String[] stepnames = null;
			int stepnrs = 0;
			if (meta.getStepName() != null && meta.getStepName().length > 0) {
				stepnames = meta.getStepName();
				stepnrs = stepnames.length;
			} else {
				throw new KettleException(Messages
						.getString("BlockUntilStepsFinish.Error.NotSteps"));
			}
			// Get target stepnames
			String[] targetSteps = getTransMeta().getNextStepNames(
					getStepMeta());

			data.stepInterfaces = new ConcurrentHashMap<Integer, StepInterface>();
			for (int i = 0; i < stepnrs; i++) {
				// We can not get metrics from current step
				if (stepnames[i].equals(getStepname()))
					throw new KettleException("You can not wait for step ["
							+ stepnames[i] + "] to finish!");
				if (targetSteps != null) {
					// We can not metrics from the target steps
					for (int j = 0; j < targetSteps.length; j++) {
						if (stepnames[i].equals(targetSteps[j]))
							throw new KettleException(
									"You can not get metrics for the target step ["
											+ targetSteps[i] + "]!");
					}
				}

				int CopyNr = Const.toInt(meta.getStepCopyNr()[i], 0);
				StepInterface step = getDispatcher()
						.findBaseSteps(stepnames[i]).get(CopyNr);
				if (step == null)
					throw new KettleException("Erreur finding step ["
							+ stepnames[i] + "] nr copy=" + CopyNr + "!");

				data.stepInterfaces.put(i, getDispatcher().findBaseSteps(
						stepnames[i]).get(CopyNr));
			}
		} // end if first

		// Wait until all specified steps have finished!
		while (data.continueLoop && !isStopped()) {
			data.continueLoop = false;
			Iterator<Entry<Integer, StepInterface>> it = data.stepInterfaces
					.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Integer, StepInterface> e = it.next();
				StepInterface step = e.getValue();
				if (step.getStatus() != StepDataInterface.STATUS_FINISHED) {
					// This step is still running...
					data.continueLoop = true;
				} else {
					// We have done with this step.
					// remove it from the map
					data.stepInterfaces.remove(e.getKey());
					if (log.isDetailed())
						logDetailed("Finished running step ["
								+ step.getStepname() + "(" + step.getCopy()
								+ ")].");
				}
			}

			if (data.continueLoop) {
				try {
					Thread.sleep(200);
				} catch (Exception e) {
				}
			}
		}

		// All steps we are waiting for are ended
		// let's now free all incoming rows
		Object[] r = getRow();

		if (r == null) {
			// no more input to be expected...
			setOutputDone();
			return false;
		}

		putRow(getInputRowMeta(), r);

		return true;
	}

	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (BlockUntilStepsFinishMeta) smi;
		data = (BlockUntilStepsFinishData) sdi;

		if (super.init(smi, sdi)) {
			return true;
		}
		return false;
	}

	public void run() {
		BaseStep.runStepThread(this, meta, data);
	}

}
