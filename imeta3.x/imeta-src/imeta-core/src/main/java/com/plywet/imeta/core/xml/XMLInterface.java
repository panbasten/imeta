package com.plywet.imeta.core.xml;

import com.plywet.imeta.core.exception.ImetaException;

/**
 * XML接口，用来表示使用XML表达他们本身。也可以使用XML构建他们本身
 * 
 * @since 1.0 2010-1-27
 * @author 潘巍（Peter Pan）
 * 
 */
public interface XMLInterface {
	String getXML() throws ImetaException;
}
