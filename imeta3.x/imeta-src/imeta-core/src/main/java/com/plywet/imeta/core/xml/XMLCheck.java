package com.plywet.imeta.core.xml;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.vfs.FileObject;
import org.xml.sax.helpers.DefaultHandler;

import com.plywet.imeta.core.exception.ImetaException;

public class XMLCheck {

	public static class XMLTreeHandler extends DefaultHandler {

	}

	/**
	 * 检查一个XML文件的格式
	 * 
	 * @param file
	 *            用于检查的文件
	 * @return
	 */
	public static final boolean isXMLFileWellFormed(FileObject file)
			throws ImetaException {
		boolean retval = false;
		try {
			retval = isXMLWellFormed(file.getContent().getInputStream());
		} catch (Exception e) {
			throw new ImetaException(e);
		}

		return retval;
	}

	/**
	 * 检查一个XML字符串的格式
	 * 
	 * @param is
	 *            输入流
	 * @return
	 */
	public static final boolean isXMLWellFormed(InputStream is)
			throws ImetaException {
		boolean retval = false;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			XMLTreeHandler handler = new XMLTreeHandler();

			// Parse the input.
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(is, handler);
			retval = true;
		} catch (Exception e) {
			throw new ImetaException(e);
		}
		return retval;
	}

}
