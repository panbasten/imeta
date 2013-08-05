package com.plywet.imeta.builder.utils;

public enum EntityAttributeType {
	// 空
	TYPE_NONE("None", "Non", true, true),
	// 整型
	TYPE_INTEGER("Integer", "Int", true, true),
	// 数字型
	TYPE_NUMBER("Number", "Num", true, true),
	// 大数字型
	TYPE_BIGNUMBER("BigNumber", "Bnum", true, true),
	// 字符串型
	TYPE_STRING("String", "Str", true, true),
	// 字符型
	TYPE_CHAR("Char", "Char", true, true),
	// 日期型
	TYPE_DATE("Date", "Date", true, true),
	// 时间型
	TYPE_TIME("Time", "Time", true, true),
	// 日期时间型
	TYPE_DATETIME("DateTime", "DT", true, true),
	// 时间范围型
	TYPE_DURATION("Duration", "Dura", true, false),
	// 二进制型
	TYPE_BLOB("Blob", "Blob", false, true),
	// 大字符型
	TYPE_CLOB("Clob", "Clob", false, true),
	// 大文本型
	TYPE_TEXT("Text", "Text", false, false),
	// 图片类型
	TYPE_IMAGE("Image", "Img", false, false),
	// 范围型
	TYPE_RANGE("Range", "Ran", true, false),
	// 数量
	TYPE_QUANTITY("Quantity", "Quan", true, false),
	// 单位
	TYPE_UNIT("Unit", "Unit", true, false),
	// 定位
	TYPE_LOCATION("Location", "Loca", true, false),
	// 方位
	TYPE_DIRECTION("Direction", "Dire", true, false),
	// 复合类型
	COMPLEX_TYPE("ComplexType", "Cmp", false, false);

	// 代码
	private String code;

	// 简码
	private String shortCode;

	// 是否是可索引属性
	private boolean indexAttribute;

	// 是否是简单属性
	private boolean simpleAttribute;

	private EntityAttributeType(String code, String shortCode,
			boolean indexAttribute, boolean simpleAttribute) {
		this.code = code;
		this.shortCode = shortCode;
		this.indexAttribute = indexAttribute;
		this.simpleAttribute = simpleAttribute;
	}

	public String getCode() {
		return code;
	}

	public String getShortCode() {
		return shortCode;
	}

	public boolean isIndexAttribute() {
		return indexAttribute;
	}

	public boolean isSimpleAttribute() {
		return simpleAttribute;
	}

	/**
	 * 通过code获得实体属性类型
	 * 
	 * @param code
	 * @return
	 */
	public static final EntityAttributeType getType(String code) {
		for (EntityAttributeType type : EntityAttributeType.values()) {
			if (type.getCode().equalsIgnoreCase(code))
				return type;
		}
		return EntityAttributeType.TYPE_NONE;
	}
}
