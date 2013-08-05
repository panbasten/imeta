package com.panet.imeta.trans.steps.encrypt;

import com.panet.imeta.core.Const;
import com.panet.imeta.core.encryption.Blowfish;
import com.panet.imeta.core.encryption.Des;
import com.panet.imeta.core.encryption.Encr;
import com.panet.imeta.core.encryption.ThreeDes;
import com.panet.imeta.core.encryption.Xor;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.row.RowDataUtil;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStep;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;

/**
 * 创建加密字段
 * 
 * @author Peter
 */
public class Encrypt extends BaseStep implements StepInterface {
	private EncryptMeta meta;
	private EncryptData data;

	public Encrypt(StepMeta stepMeta, StepDataInterface stepDataInterface,
			int copyNr, TransMeta transMeta, Trans trans) {
		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);

		meta = (EncryptMeta) getStepMeta().getStepMetaInterface();
		data = (EncryptData) stepDataInterface;
	}

	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi)
			throws KettleException {
		// 输入行记录
		Object[] r = getRow();

		if (r == null) // no more rows to be expected from the previous step(s)
		{
			setOutputDone();
			return false;
		}

		if (first) {
			// The output meta is the original input meta + the
			// additional constant fields.

			first = false;
			data.outputMeta = getInputRowMeta().clone();
			// 补充outputMeta
			meta.getFields(data.outputMeta, getStepname(), null, null, this);

		}

		// 得到加密解密字段的数据
		Object[] rowData = getRowData(getInputRowMeta(), r);
		if (rowData == null) {
			setOutputDone();
			return false;
		}
		r = RowDataUtil.addRowData(r, getInputRowMeta().size(), rowData);

		putRow(data.outputMeta, r);

		if (log.isRowLevel()) {
			log.logRowlevel(toString(), Messages.getString(
					"Encrypt.Log.Wrote.Row", Long.toString(getLinesWritten()),
					getInputRowMeta().getString(r)));
		}

		if (checkFeedback(getLinesWritten())) {
			if (log.isBasic())
				logBasic(Messages.getString("Encrypt.Log.LineNr", Long
						.toString(getLinesWritten())));
		}

		return true;
	}

	private Object[] getRowData(RowMetaInterface rowMeta, Object[] r) {
		Object[] rowData = new Object[meta.getFieldName().length];
		String realFieldName = null;
		int fieldnr;

		for (int i = 0; i < meta.getFieldName().length; i++) {
			if (meta.getFieldName()[i] != null
					&& meta.getFieldName()[i].length() > 0) {
				realFieldName = environmentSubstitute(meta.getFieldName()[i]);
				fieldnr = rowMeta.indexOfValue(realFieldName);

				int numErrors = 0;
				if (Const.isEmpty(meta.getNewFieldName()[i])) {
					logError(Messages
							.getString("Encrypt.Log.NewFieldNameIsNull")); //$NON-NLS-1$
					numErrors++;
				}

				if (fieldnr < 0) {
					logError(Messages
							.getString(
									"Encrypt.Log.CouldNotFindFieldToEncrypt", realFieldName)); //$NON-NLS-1$
					numErrors++;
				}

				if (!rowMeta.getValueMeta(fieldnr).isString()) {
					logError(Messages.getString(
							"Encrypt.Log.SplitFieldNotValid", realFieldName)); //$NON-NLS-1$
					numErrors++;
				}

				if (numErrors > 0) {
					setErrors(numErrors);
					stopAll();
					return null;
				}

				rowData[i] = encryptOption((String) r[fieldnr], meta
						.getOptionType()[i], meta.getEncryptType(), meta
						.getEncoding(), meta.getKey());
			}
		}

		return rowData;
	}

	public String encryptOption(String src, long optionType, long encryptType,
			String encoding, String key) {
		String rtnSrc = src;
		switch ((int) encryptType) {
		case Encr.ENCRYPT_TYPES_DES:
			if (optionType == Encr.ENCRYPT_OPTION_TYPE_ENCRYPT) {
				rtnSrc = Des.encrypt(key, src, encoding);
			} else if (optionType == Encr.ENCRYPT_OPTION_TYPE_DECRYPT) {
				rtnSrc = Des.decrypt(key, src, encoding);
			}
			break;
		case Encr.ENCRYPT_TYPES_3DES:
			if (optionType == Encr.ENCRYPT_OPTION_TYPE_ENCRYPT) {
				rtnSrc = ThreeDes.encrypt(key, src, encoding);
			} else if (optionType == Encr.ENCRYPT_OPTION_TYPE_DECRYPT) {
				rtnSrc = ThreeDes.decrypt(key, src, encoding);
			}
			break;
		case Encr.ENCRYPT_TYPES_BLOWFISH:
			if (optionType == Encr.ENCRYPT_OPTION_TYPE_ENCRYPT) {
				Blowfish crypt = new Blowfish(key);
				rtnSrc = crypt.encryptString(src);
			} else if (optionType == Encr.ENCRYPT_OPTION_TYPE_DECRYPT) {
				Blowfish crypt = new Blowfish(key);
				rtnSrc = crypt.decryptString(src);
			}
			break;
		case Encr.ENCRYPT_TYPES_EASY:
			if (optionType == Encr.ENCRYPT_OPTION_TYPE_ENCRYPT) {
				rtnSrc = Xor.encrypt(key, src, encoding);
			} else if (optionType == Encr.ENCRYPT_OPTION_TYPE_DECRYPT) {
				rtnSrc = Xor.decrypt(key, src, encoding);
			}
			break;
		}

		return rtnSrc;
	}

	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (EncryptMeta) smi;
		data = (EncryptData) sdi;

		if (super.init(smi, sdi)) {
			return true;
		}
		return false;
	}

	//
	// Run is were the action happens!
	public void run() {
		BaseStep.runStepThread(this, meta, data);
	}
}
