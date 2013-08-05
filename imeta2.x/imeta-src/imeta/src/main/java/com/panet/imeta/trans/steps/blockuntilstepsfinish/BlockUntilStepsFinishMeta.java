package com.panet.imeta.trans.steps.blockuntilstepsfinish;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.core.CheckResult;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Counter;
import com.panet.imeta.core.annotations.Step;
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
import com.panet.imeta.trans.step.StepCategory;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;

/*
 * Created on 30-06-2008
 *
 */
@Step(name = "BlockUntilStepsFinish", image = "WFS.png", tooltip = "BaseStep.TypeTooltipDesc.BlockUntilStepsFinish", description = "BaseStep.TypeLongDesc.BlockUntilStepsFinish", category = StepCategory.CATEGORY_FLOW)
public class BlockUntilStepsFinishMeta extends BaseStepMeta implements
		StepMetaInterface {

	/** by which steps to display? */
	private String stepName[];
	private String stepCopyNr[];

	public BlockUntilStepsFinishMeta() {
		super(); // allocate BaseStepMeta
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws KettleXMLException {
		readData(stepnode);
	}

	public Object clone() {
		BlockUntilStepsFinishMeta retval = (BlockUntilStepsFinishMeta) super
				.clone();

		int nrfields = stepName.length;

		retval.allocate(nrfields);

		for (int i = 0; i < nrfields; i++) {
			retval.stepName[i] = stepName[i];
			retval.stepCopyNr[i] = stepCopyNr[i];

		}
		return retval;
	}

	public void allocate(int nrfields) {
		stepName = new String[nrfields];
		stepCopyNr = new String[nrfields];
	}

	/**
	 * @return Returns the stepName.
	 */
	public String[] getStepName() {
		return stepName;
	}

	/**
	 * @return Returns the stepCopyNr.
	 */
	public String[] getStepCopyNr() {
		return stepCopyNr;
	}

	/**
	 * @param stepName
	 *            The stepName to set.
	 */
	public void setStepName(String[] stepName) {
		this.stepName = stepName;
	}

	/**
	 * @param stepCopyNr
	 *            The stepCopyNr to set.
	 */
	public void setStepCopyNr(String[] stepCopyNr) {
		this.stepCopyNr = stepCopyNr;
	}

	public void getFields(RowMetaInterface r, String name,
			RowMetaInterface info[], StepMeta nextStep, VariableSpace space)
			throws KettleStepException {

	}

	private void readData(Node stepnode) throws KettleXMLException {
		try {
			Node steps = XMLHandler.getSubNode(stepnode, "steps");
			int nrsteps = XMLHandler.countNodes(steps, "step");

			allocate(nrsteps);

			for (int i = 0; i < nrsteps; i++) {
				Node fnode = XMLHandler.getSubNodeByNr(steps, "step", i);
				stepName[i] = XMLHandler.getTagValue(fnode, "name");
				stepCopyNr[i] = XMLHandler.getTagValue(fnode, "CopyNr");
			}
		} catch (Exception e) {
			throw new KettleXMLException("Unable to load step info from XML", e);
		}
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer();

		retval.append("    <steps>" + Const.CR);
		for (int i = 0; i < stepName.length; i++) {
			retval.append("      <step>" + Const.CR);
			retval.append("        "
					+ XMLHandler.addTagValue("name", stepName[i]));
			retval.append("        "
					+ XMLHandler.addTagValue("CopyNr", stepCopyNr[i]));

			retval.append("        </step>" + Const.CR);
		}
		retval.append("      </steps>" + Const.CR);

		return retval.toString();
	}

	public void setDefault() {
		int nrsteps = 0;

		allocate(nrsteps);

		for (int i = 0; i < nrsteps; i++) {
			stepName[i] = "step" + i;
			stepCopyNr[i] = "CopyNr" + i;
		}
	}

	public void readRep(Repository rep, long id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		try {

			int nrsteps = rep.countNrStepAttributes(id_step, "step_name");

			allocate(nrsteps);

			for (int i = 0; i < nrsteps; i++) {
				stepName[i] = rep.getStepAttributeString(id_step, i,
						"step_name");
				stepCopyNr[i] = rep.getStepAttributeString(id_step, i,
						"step_CopyNr");
			}
		} catch (Exception e) {
			throw new KettleException(
					"Unexpected error reading step information from the repository",
					e);
		}
	}

	public void saveRep(Repository rep, long id_transformation, long id_step)
			throws KettleException {
		try {
			for (int i = 0; i < stepName.length; i++) {
				rep.saveStepAttribute(id_transformation, id_step, i,
						"step_name", stepName[i]);
				rep.saveStepAttribute(id_transformation, id_step, i,
						"step_CopyNr", stepCopyNr[i]);

			}
		} catch (Exception e) {
			throw new KettleException(
					"Unable to save step information to the repository for id_step="
							+ id_step, e);
		}
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta,
			StepMeta stepMeta, RowMetaInterface prev, String input[],
			String output[], RowMetaInterface info) {
		CheckResult cr;

		if (prev == null || prev.size() == 0) {
			cr = new CheckResult(
					CheckResult.TYPE_RESULT_WARNING,
					Messages
							.getString("BlockUntilStepsFinishMeta.CheckResult.NotReceivingFields"), stepMeta); //$NON-NLS-1$
		} else {
			if (stepName.length > 0)
				cr = new CheckResult(
						CheckResult.TYPE_RESULT_OK,
						Messages
								.getString("BlockUntilStepsFinishMeta.CheckResult.AllStepsFound"),
						stepMeta);
			else
				cr = new CheckResult(
						CheckResult.TYPE_RESULT_WARNING,
						Messages
								.getString("BlockUntilStepsFinishMeta.CheckResult.NoStepsEntered"),
						stepMeta);

		}
		remarks.add(cr);

		// See if we have input streams leading to this step!
		if (input.length > 0)
			cr = new CheckResult(
					CheckResult.TYPE_RESULT_OK,
					Messages
							.getString("BlockUntilStepsFinishMeta.CheckResult.StepRecevingData2"), stepMeta); //$NON-NLS-1$
		else
			cr = new CheckResult(
					CheckResult.TYPE_RESULT_ERROR,
					Messages
							.getString("BlockUntilStepsFinishMeta.CheckResult.NoInputReceivedFromOtherSteps"), stepMeta); //$NON-NLS-1$
		remarks.add(cr);

	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta tr,
			Trans trans) {
		return new BlockUntilStepsFinish(stepMeta, stepDataInterface, cnr, tr,
				trans);
	}

	public StepDataInterface getStepData() {
		return new BlockUntilStepsFinishData();
	}

	@Override
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		String[] fieldStepName = p.get(id + "_fields.stepName");
		String[] fieldStepCopyNr = p.get(id + "_fields.stepCopyNr");

		if (fieldStepName != null && fieldStepName.length > 0) {
			this.stepName = fieldStepName;
			this.stepCopyNr = fieldStepCopyNr;
		}
	}
}
