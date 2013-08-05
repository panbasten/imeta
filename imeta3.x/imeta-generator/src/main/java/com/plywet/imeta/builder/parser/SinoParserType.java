package com.plywet.imeta.builder.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * XML解析的枚举类
 * 
 * @since 1.0 2010-7-13
 * @author 潘巍（Peter Pan）
 * 
 */
public enum SinoParserType {
	// 应用模型
	APP("com.sinosoft.builder.templates.app", "apptemplate", "app", null);

	private static final Logger logger = Logger.getLogger(SinoParserType.class
			.getName());

	private final String jaxbFactoryPackage;

	private final String schemaFileName;

	private final String xmlFileNameOrPath;

	/**
	 * 文件过滤器，按照扩展名来过滤
	 */
	private final String[] exts;

	private final static String META_RESOURCE_PATH = "META-INF/schema/";

	private final static String DATA_RESOURCE_PATH = "META-INF/metadata/";

	SinoParserType(String jaxbFactoryPackage, String schemaFileName,
			String xmlDirectoryPath, String[] exts) {
		this.jaxbFactoryPackage = jaxbFactoryPackage;
		String[] schemaFileNameArr = schemaFileName.split(",");
		StringBuffer schemaFileNameSB = new StringBuffer();
		for (String s : schemaFileNameArr) {
			schemaFileNameSB.append(META_RESOURCE_PATH);
			schemaFileNameSB.append(s);
			schemaFileNameSB.append(".xsd,");
		}
		this.schemaFileName = schemaFileNameSB.toString().substring(0,
				schemaFileNameSB.length() - 1);

		if (xmlDirectoryPath != null) {
			this.xmlFileNameOrPath = DATA_RESOURCE_PATH + xmlDirectoryPath;
		} else {
			this.xmlFileNameOrPath = null;
		}

		this.exts = exts;
	}

	public String getJaxbFactoryPackage() {
		return jaxbFactoryPackage;
	}

	public String getSchemaFileName() {
		return schemaFileName;
	}

	public String getXmlFileNameOrPath() {
		return xmlFileNameOrPath;
	}

	public boolean accept(File f) {
		boolean rtn = false;
		if (this.exts != null && this.exts.length > 0) {
			for (String ext : this.exts) {
				rtn = f.getName().toLowerCase().endsWith("." + ext);
				if (rtn)
					return true;
			}
		}
		rtn = f.getName().toLowerCase().endsWith(".xml");
		return rtn;
	}

	public List<String> validation(List<?> os) {
		if (os != null) {
			List<String> rtn = new ArrayList<String>();
			for (Object o : os) {
				List<String> l = validationOne(o);
				if (l != null)
					rtn.addAll(l);
			}
			return rtn;
		}
		return null;
	}

	public List<String> validationOne(Object o) {
		return null;
	}

	public <T> List<T> parser() {
		return parser(null);
	}

	public <T> List<T> parser(List<String> errs) {
		try {
			return (new SinoParser<T>()).parse(this, errs);
		} catch (IOException e) {
			logger.severe("解析出现错误");
		}
		return null;
	}

}
