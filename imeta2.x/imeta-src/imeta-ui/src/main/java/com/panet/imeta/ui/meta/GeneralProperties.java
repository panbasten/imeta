package com.panet.imeta.ui.meta;

public class GeneralProperties {

	public static String[] generalPropertyNames = new String[] { "generalId",
			"generalLabel", "generalX", "generalY", "generalWidth",
			"generalHeight" };

	public static boolean[] generalPropertyDisabled = new boolean[] { true,
			false, true, true, true, true };

	private String generalId;

	private String generalLabel;

	private String generalX;

	private String generalY;

	private String generalWidth;

	private String generalHeight;

	public String getGeneralId() {
		return generalId;
	}

	public void setGeneralId(String generalId) {
		this.generalId = generalId;
	}

	public String getGeneralLabel() {
		return generalLabel;
	}

	public void setGeneralLabel(String generalLabel) {
		this.generalLabel = generalLabel;
	}

	public String getGeneralX() {
		return generalX;
	}

	public void setGeneralX(String generalX) {
		this.generalX = generalX;
	}

	public String getGeneralY() {
		return generalY;
	}

	public void setGeneralY(String generalY) {
		this.generalY = generalY;
	}

	public String getGeneralWidth() {
		return generalWidth;
	}

	public void setGeneralWidth(String generalWidth) {
		this.generalWidth = generalWidth;
	}

	public String getGeneralHeight() {
		return generalHeight;
	}

	public void setGeneralHeight(String generalHeight) {
		this.generalHeight = generalHeight;
	}

}
