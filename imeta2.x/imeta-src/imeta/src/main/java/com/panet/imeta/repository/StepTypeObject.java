package com.panet.imeta.repository;

public class StepTypeObject {
	private long idStepType;
	private String code;

	public StepTypeObject() {
		super();
	}

	public StepTypeObject(long idStepType, String code, String stepGroup) {
		this();
		this.idStepType = idStepType;
		this.code = code;
	}

	public long getIdStepType() {
		return idStepType;
	}

	public void setIdStepType(long idStepType) {
		this.idStepType = idStepType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
