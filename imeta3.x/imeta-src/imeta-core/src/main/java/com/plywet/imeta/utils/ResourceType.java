package com.plywet.imeta.utils;

/**
 * 资源类型枚举类
 * 
 * @since 1.0 2010-1-10
 * @author 潘巍（Peter Pan）
 * 
 */
public enum ResourceType {

	TYPE_XML("xml"), TYPE_TXT("txt");

	String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private ResourceType(String type) {
		this.type = type;
	}

	public static ResourceType getInstance(String type) {
		ResourceType[] all = values();
		for (int i = 0; i < all.length; i++) {
			if (type.equals(all[i].type)) {
				return all[i];
			}
		}
		return values()[Const.DEFAULT_FILE_TYPEVALUE];
	}
}