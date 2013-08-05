package com.panet.imeta.ui.meta;

public class XMLFile {
	private String name;
	
	private String xml;
	
	public XMLFile(String name,String xml){
		this.name = name;
		this.xml = xml;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

}
