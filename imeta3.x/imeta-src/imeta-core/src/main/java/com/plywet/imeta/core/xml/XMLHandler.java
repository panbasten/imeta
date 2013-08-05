package com.plywet.imeta.core.xml;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import com.plywet.imeta.core.exception.ImetaException;
import com.plywet.imeta.core.exception.ImetaXMLException;
import com.plywet.imeta.core.vfs.VFS;
import com.plywet.imeta.utils.Const;
import com.plywet.imeta.utils.DateUtils;

/**
 * 该类包含一些(static final)方法，为处理来自XML Node的信息提供便利。
 * 
 * @author 潘巍（Peter Pan）
 * @since 2010-9-8 下午11:20:34
 */
public class XMLHandler {
	private static XMLHandlerCache cache = XMLHandlerCache.getInstance();

	/**
	 * 头部字符串指定用UTF-8编码
	 * 
	 * @return XML头部
	 */
	public static final String getXMLHeader() {
		return getXMLHeader(Const.XML_ENCODING);
	}

	/**
	 * 用指定的编码生成头部字符串
	 * 
	 * @param encoding
	 * @return XML头部
	 */
	public static final String getXMLHeader(String encoding) {
		return "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>"
				+ Const.CR;
	}

	/**
	 * 获得节点标签的值
	 * 
	 * @param n
	 *            从中查找的node
	 * @param tag
	 *            打算查找的tag
	 * @return 标签值，或者如果没有找到返回null
	 */
	public static final String getTagValue(Node n, String tag) {
		NodeList children;
		Node childnode;

		if (n == null)
			return null;

		children = n.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			childnode = children.item(i);
			if (childnode.getNodeName().equalsIgnoreCase(tag)) {
				if (childnode.getFirstChild() != null)
					return childnode.getFirstChild().getNodeValue();
			}
		}
		return null;
	}

	/**
	 * 获得节点标签的属性值
	 * 
	 * @param n
	 *            从中查找的node
	 * @param tag
	 *            打算查找的tag
	 * @param attribute
	 *            属性名
	 * @return 标签指定属性值，或者如果没有找到返回null
	 */
	public static final String getTagValueWithAttribute(Node n, String tag,
			String attribute) {
		NodeList children;
		Node childnode;

		if (n == null)
			return null;

		children = n.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			childnode = children.item(i);
			if (childnode.getNodeName().equalsIgnoreCase(tag)
					&& childnode.getAttributes().getNamedItem(attribute) != null) {
				if (childnode.getFirstChild() != null)
					return childnode.getFirstChild().getNodeValue();
			}
		}
		return null;
	}

	/**
	 * 查找特定的标签节点，查找特定subtag的子节点。返回subtag的值。
	 * 
	 * @param n
	 *            从中查找的node
	 * @param tag
	 *            打算查找的tag
	 * @param subtag
	 *            打算查找的subtag
	 * @return 子标签值，或者如果没有找到返回null
	 */
	public static final String getTagValue(Node n, String tag, String subtag) {
		NodeList children, tags;
		Node childnode, tagnode;

		if (n == null)
			return null;

		children = n.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			childnode = children.item(i);
			if (childnode.getNodeName().equalsIgnoreCase(tag)) // <file>
			{
				tags = childnode.getChildNodes();
				for (int j = 0; j < tags.getLength(); j++) {
					tagnode = tags.item(j);
					if (tagnode.getNodeName().equalsIgnoreCase(subtag)) {
						if (tagnode.getFirstChild() != null)
							return tagnode.getFirstChild().getNodeValue();
					}
				}
			}
		}
		return null;
	}

	/**
	 * 特定tag的节点数
	 * 
	 * @param n
	 *            从中查找的node
	 * @param tag
	 *            打算查找的tag
	 * @return 找到的特定tag的节点数
	 */
	public static final int countNodes(Node n, String tag) {
		NodeList children;
		Node childnode;

		int count = 0;

		if (n == null)
			return 0;

		children = n.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			childnode = children.item(i);
			if (childnode.getNodeName().equalsIgnoreCase(tag)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 获得下一级的特定tag的所有node
	 * 
	 * @param n
	 *            从中查找的node
	 * @param tag
	 *            打算查找的tag
	 * @return 特定tag的node列表
	 */
	public static final List<Node> getNodes(Node n, String tag) {
		NodeList children;
		Node childnode;

		List<Node> nodes = new ArrayList<Node>();

		if (n == null)
			return nodes;

		children = n.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			childnode = children.item(i);
			if (childnode.getNodeName().equalsIgnoreCase(tag)) // <file>
			{
				nodes.add(childnode);
			}
		}
		return nodes;
	}

	/**
	 * 获得特定subtag的子节点，并且等于特定值，第nr次出现
	 * 
	 * @param n
	 *            从中查找的node
	 * @param tag
	 *            打算查找的tag
	 * @param subtag
	 *            打算查找的subtag
	 * @param subtagvalue
	 *            subtag应有的值
	 * @param nr
	 *            该值出现的次数
	 * @return 子节点，或者如果没有找到返回null
	 */
	public static final Node getNodeWithTagValue(Node n, String tag,
			String subtag, String subtagvalue, int nr) {
		NodeList children;
		Node childnode, tagnode;
		String value;

		int count = 0;

		children = n.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			childnode = children.item(i);
			if (childnode.getNodeName().equalsIgnoreCase(tag)) // <hop>
			{
				tagnode = getSubNode(childnode, subtag);
				value = getNodeValue(tagnode);
				if (value.equalsIgnoreCase(subtagvalue)) {
					if (count == nr)
						return childnode;
					count++;
				}
			}
		}
		return null;
	}

	/**
	 * 获得特定subtag的子节点，并且等于特定值，首次出现
	 * 
	 * @param n
	 *            从中查找的node
	 * @param tag
	 *            打算查找的tag
	 * @param subtag
	 *            打算查找的subtag
	 * @param subtagvalue
	 *            subtag应有的值
	 * @return 子节点，或者如果没有找到返回null
	 */
	public static final Node getNodeWithAttributeValue(Node n, String tag,
			String attributeName, String attributeValue) {
		NodeList children;
		Node childnode;

		children = n.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			childnode = children.item(i);
			if (childnode.getNodeName().equalsIgnoreCase(tag)) {
				Node attribute = childnode.getAttributes().getNamedItem(
						attributeName);

				if (attribute != null
						&& attributeValue.equals(attribute.getTextContent()))
					return childnode;
			}
		}
		return null;
	}

	public static final NodeList getSubNodeList(Node n, String path) {
		XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			return (NodeList) xPath.evaluate(path, n, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			return null;
		}
	}

	/**
	 * 查找带有特定tag的子节点
	 * 
	 * @param n
	 *            从中查找的node
	 * @param tag
	 *            打算查找的tag
	 * @return 子节点，或者如果没有找到返回null
	 */
	public static final Node getSubNode(Node n, String tag) {
		int i;
		NodeList children;
		Node childnode;

		if (n == null)
			return null;

		children = n.getChildNodes();
		for (i = 0; i < children.getLength(); i++) {
			childnode = children.item(i);
			if (childnode.getNodeName().equalsIgnoreCase(tag)) {
				return childnode;
			}
		}
		return null;
	}

	/**
	 * 操作子子节点
	 * 
	 * @param n
	 *            从中查找的node
	 * @param tag
	 *            打算查找的tag
	 * @param subtag
	 *            打算查找的subtag
	 * @return 子节点，或者如果没有找到返回null
	 */
	public static final Node getSubNode(Node n, String tag, String subtag) {
		Node t = getSubNode(n, tag);
		if (t != null)
			return getSubNode(t, subtag);
		return null;
	}

	/**
	 * 获得出现第nr次的子节点<br>
	 * 该方法使用缓存，假定你的查询循环都是顺序（nr在每次调用后都增1）
	 * 
	 * @param n
	 *            从中查找的node
	 * @param tag
	 *            打算查找的tag
	 * @param nr
	 *            节点位置
	 * @return 子节点，或者如果没有找到返回null
	 */
	public static final Node getSubNodeByNr(Node n, String tag, int nr) {
		return getSubNodeByNr(n, tag, nr, true);
	}

	/**
	 * 获得出现第nr次的子节点<br>
	 * 选项允许你指定是否使用缓存<br>
	 * 缓存假定你的查询循环都是顺序（nr在每次调用后都增1）
	 * 
	 * @param n
	 *            从中查找的node
	 * @param tag
	 *            打算查找的tag
	 * @param nr
	 *            节点位置
	 * @param useCache
	 *            是否使用缓存
	 * @return 子节点，或者如果没有找到返回null
	 */
	public static final Node getSubNodeByNr(Node n, String tag, int nr,
			boolean useCache) {
		NodeList children;
		Node childnode;

		if (n == null)
			return null;

		int count = 0;
		children = n.getChildNodes();

		int lastChildNr = -1;
		XMLHandlerCacheEntry entry = null;

		if (useCache) {
			entry = new XMLHandlerCacheEntry(n, tag);
			lastChildNr = cache.getLastChildNr(entry);
		}
		if (lastChildNr < 0) {
			lastChildNr = 0;
		} else {
			count = nr;
			lastChildNr++;
		}

		for (int i = lastChildNr; i < children.getLength(); i++) {
			childnode = children.item(i);
			if (childnode.getNodeName().equalsIgnoreCase(tag)) {
				if (count == nr) {
					if (useCache)
						cache.storeCache(entry, i);
					return childnode;
				}
				count++;
			}
		}
		return null;
	}

	/**
	 * 获得节点值
	 * 
	 * @param n
	 *            节点
	 * @return 节点值（String）
	 */
	public static final String getNodeValue(Node n) {
		if (n == null)
			return null;

		NodeList children = n.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node childnode = children.item(i);
			String retval = childnode.getNodeValue();
			if (retval != null) {
				return retval;
			}
		}
		return null;
	}

	/**
	 * 获得节点的指定属性
	 * 
	 * @param node
	 *            节点
	 * @param attribute
	 *            属性名
	 * @return 属性值
	 */
	public static final String getTagAttribute(Node node, String attribute) {
		if (node == null)
			return null;

		String retval = null;

		NamedNodeMap nnm = node.getAttributes();
		if (nnm != null) {
			Node attr = nnm.getNamedItem(attribute);
			if (attr != null) {
				retval = attr.getNodeValue();
			}
		}
		return retval;
	}

	/**
	 * 加载文件到一个XML文档
	 * 
	 * @param filename
	 *            打算加载到document的文件名
	 * @return 如果一切正常返回Document，发生错误返回null
	 */
	public static final Document loadXMLFile(String filename)
			throws ImetaXMLException {
		try {
			return loadXMLFile(VFS.getFileObject(filename));
		} catch (Exception e) {
			throw new ImetaXMLException(e);
		}
	}

	/**
	 * 加载文件到一个XML文档
	 * 
	 * @param filename
	 *            打算加载到document的文件名
	 * @return 如果一切正常返回Document，发生错误返回null
	 */
	public static final Document loadXMLFile(FileObject fileObject)
			throws ImetaXMLException {
		return loadXMLFile(fileObject, null, false, false);
	}

	/**
	 * 加载文件到一个XML文档
	 * 
	 * @param filename
	 *            打算加载到document的文件名
	 * @param systemId
	 *            提供一个基础路径，用过解决相对URI的问题。
	 * @param ignoreEntities
	 *            是否忽略外部实体返回空对象
	 * @param namespaceAware
	 *            支持XML命名空间
	 * @return 如果一切正常返回Document，发生错误返回null
	 */
	public static final Document loadXMLFile(FileObject fileObject,
			String systemID, boolean ignoreEntities, boolean namespaceAware)
			throws ImetaXMLException {
		try {
			return loadXMLFile(VFS.getInputStream(fileObject), systemID,
					ignoreEntities, namespaceAware);
		} catch (FileSystemException e) {
			throw new ImetaXMLException("无法读取文件 [" + fileObject.toString()
					+ "]", e);
		}
	}

	/**
	 * 从一个输入流返回一个XML文档
	 * 
	 * @param inputStream
	 *            输入流
	 * @return 如果一切正常返回Document，发生错误返回null
	 */
	public static final Document loadXMLFile(InputStream inputStream)
			throws ImetaXMLException {
		return loadXMLFile(inputStream, null, false, false);
	}

	/**
	 * 加载文件到一个XML文档
	 * 
	 * @param inputStream
	 *            输入流
	 * @param systemId
	 *            提供一个基础路径，用过解决相对URI的问题。
	 * @param ignoreEntities
	 *            是否忽略外部实体返回空对象
	 * @param namespaceAware
	 *            支持XML命名空间
	 * @return 如果一切正常返回Document，发生错误返回null
	 */
	public static final Document loadXMLFile(InputStream inputStream,
			String systemID, boolean ignoreEntities, boolean namespaceAware)
			throws ImetaXMLException {
		DocumentBuilderFactory dbf;
		DocumentBuilder db;
		Document doc;

		try {
			// 检查并打开XML文档
			dbf = DocumentBuilderFactory.newInstance();
			dbf.setIgnoringComments(true);
			dbf.setNamespaceAware(namespaceAware);
			db = dbf.newDocumentBuilder();

			if (ignoreEntities) {
				db.setEntityResolver(new DTDIgnoringEntityResolver());
			}

			try {
				if (Const.isEmpty(systemID)) {
					// 正常解析
					doc = db.parse(inputStream);
				} else {
					String systemIDwithEndingSlash = systemID.trim();

					if (!systemIDwithEndingSlash.endsWith("/")
							&& !systemIDwithEndingSlash.endsWith("\\")) {
						systemIDwithEndingSlash = systemIDwithEndingSlash
								.concat("/");
					}
					doc = db.parse(inputStream, systemIDwithEndingSlash);
				}
			} catch (FileNotFoundException ef) {
				throw new ImetaXMLException(ef);
			} finally {
				if (inputStream != null)
					inputStream.close();
			}

			return doc;
		} catch (Exception e) {
			throw new ImetaXMLException("从输入流读取信息发生错误", e);
		}
	}

	public static final Document loadXMLFile(File resource)
			throws ImetaXMLException {
		try {
			return loadXMLFile(resource.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new ImetaXMLException(e);
		}
	}

	/**
	 * 从XML文档加载文件
	 * 
	 * @param resource
	 *            URL
	 * @return 如果一切正常返回Document，发生错误返回null
	 */
	public static final Document loadXMLFile(URL resource)
			throws ImetaXMLException {
		DocumentBuilderFactory dbf;
		DocumentBuilder db;
		Document doc;

		try {
			// 检查并打开XML文档
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			InputStream inputStream = resource.openStream();
			try {
				doc = db.parse(inputStream);
			} catch (IOException ef) {
				throw new ImetaXMLException(ef);
			} finally {
				inputStream.close();
			}

			return doc;
		} catch (Exception e) {
			throw new ImetaXMLException("从资源中读取信息出现错误", e);
		}
	}

	/**
	 * 调用loadXMLString，带有参数deferNodeExpansion = TRUE
	 * 
	 * @param string
	 * @return
	 * @throws ImetaXMLException
	 */
	public static final Document loadXMLString(String string)
			throws ImetaXMLException {

		return loadXMLString(string, Boolean.FALSE, Boolean.TRUE);

	}

	/**
	 * 加载String到XML文档，返回一个tag实体
	 * 
	 * @param xml
	 * @param tag
	 * @return
	 * @throws ImetaXMLException
	 */
	public static final Node loadXMLString(String xml, String tag)
			throws ImetaXMLException {
		Document doc = loadXMLString(xml);
		return getSubNode(doc, tag);
	}

	/**
	 * 加载String到XML文档
	 * 
	 * @param string
	 *            XML文本
	 * @param deferNodeExpansion
	 *            true to defer node expansion, false to not defer.
	 * @return 如果一切正常返回Document，发生错误返回null
	 */
	public static final Document loadXMLString(String string,
			Boolean namespaceAware, Boolean deferNodeExpansion)
			throws ImetaXMLException {
		DocumentBuilderFactory dbf;
		DocumentBuilder db;
		Document doc;

		try {
			// Check and open XML document
			dbf = DocumentBuilderFactory.newInstance();
			dbf.setFeature(
					"http://apache.org/xml/features/dom/defer-node-expansion",
					deferNodeExpansion);
			dbf.setNamespaceAware(namespaceAware); // parameterize this as well
			db = dbf.newDocumentBuilder();
			StringReader stringReader = new java.io.StringReader(string);
			InputSource inputSource = new InputSource(stringReader);
			try {
				doc = db.parse(inputSource);
			} catch (IOException ef) {
				throw new ImetaXMLException("解析XML出现错误", ef);
			} finally {
				stringReader.close();
			}

			return doc;
		} catch (Exception e) {
			throw new ImetaXMLException(
					"从XML字符串中读取信息出现错误：" + Const.CR + string, e);
		}
	}

	public static final String getString() {
		return XMLHandler.class.getName();
	}

	/**
	 * 为特定的tag构建XML字符串
	 * 
	 * @param tag
	 *            XML tag
	 * @param val
	 *            tag值
	 * @param cr
	 * @return XML String
	 */
	public static final String addTagValue(String tag, String val, boolean cr,
			String... attributes) {
		StringBuffer value;

		if (val != null && val.length() > 0) {
			value = new StringBuffer("<");
			value.append(tag);

			for (int i = 0; i < attributes.length; i += 2)
				value.append(" ").append(attributes[i]).append("=\"").append(
						attributes[i + 1]).append("\" ");

			value.append('>');

			appendReplacedChars(value, val);

			value.append("</");
			value.append(tag);
			value.append('>');
		} else {
			value = new StringBuffer("<");
			value.append(tag);

			for (int i = 0; i < attributes.length; i += 2)
				value.append(" ").append(attributes[i]).append("=\"").append(
						attributes[i + 1]).append("\" ");

			value.append("/>");
		}

		if (cr) {
			value.append(Const.CR);
		}

		return value.toString();
	}

	/**
	 * 将string的值和附加字符放入value中。如果字符不是XML允许的，将会被转成XML代码。
	 * 
	 * @param value
	 *            stringbuffer
	 * @param string
	 *            encode字符串
	 */
	public static void appendReplacedChars(StringBuffer value, String string) {
		// 如果是CDATA内容块，那么单独剩下那部分
		boolean isCDATA = string.startsWith("<![CDATA[")
				&& string.endsWith("]]>");

		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			switch (c) {
			case '&':
				value.append("&amp;");
				break;
			case '\'':
				value.append("&apos;");
				break;
			case '<':
				if (i != 0 || !isCDATA) {
					value.append("&lt;");
				} else {
					value.append(c);
				}
				break;
			case '>':
				if (i != string.length() - 1 || !isCDATA) {
					value.append("&gt;");
				} else {
					value.append(c);
				}
				break;
			case '"':
				value.append("&quot;");
				break;
			case '/':
				if (isCDATA) // 不替换CDATA块中斜杠
				{
					value.append(c);
				} else {
					value.append("&#47;");
				}
				break;
			case 0x1A:
				value.append("{ILLEGAL XML CHARACTER 0x1A}");
				break;
			default:
				value.append(c);
			}
		}
	}

	/**
	 * 构建一个XML字符串（包含一个回车换行），为特定的tag字符串值
	 * 
	 * @param tag
	 *            XML tag
	 * @param val
	 *            tag值
	 * @return XML字符串
	 */
	public static final String addTagValue(String tag, String val) {
		return addTagValue(tag, val, true);
	}

	/**
	 * 构建一个XML字符串（包含一个回车换行），为特定的boolean值
	 * 
	 * @param tag
	 *            XML tag
	 * @param bool
	 *            tag布尔值
	 * @return XML字符串
	 */
	public static final String addTagValue(String tag, boolean bool) {
		return addTagValue(tag, bool, true);
	}

	/**
	 * 构建一个XML字符串（包含一个回车换行），为特定的boolean值
	 * 
	 * @param tag
	 *            XML tag
	 * @param bool
	 *            tag布尔值
	 * @param cr
	 *            true 表示在结尾添加一个回车换行
	 * @return XML字符串
	 */
	public static final String addTagValue(String tag, boolean bool, boolean cr) {
		return addTagValue(tag, bool ? "Y" : "N", cr);
	}

	/**
	 * 构建一个XML字符串（包含一个回车换行），为特定的长整型值
	 * 
	 * @param tag
	 *            XML tag
	 * @param l
	 *            tag长整型值
	 * @return XML字符串
	 */
	public static final String addTagValue(String tag, long l) {
		return addTagValue(tag, l, true);
	}

	/**
	 * 构建一个XML字符串（包含一个回车换行），为特定的长整型值
	 * 
	 * @param tag
	 *            XML tag
	 * @param l
	 *            tag长整型值
	 * @param cr
	 *            true 表示在结尾添加一个回车换行
	 * @return XML字符串
	 */
	public static final String addTagValue(String tag, long l, boolean cr) {
		return addTagValue(tag, String.valueOf(l), cr);
	}

	/**
	 * 构建一个XML字符串（包含一个回车换行），为特定的整型值
	 * 
	 * @param tag
	 *            XML tag
	 * @param i
	 *            tag整型值
	 * @return XML字符串
	 */
	public static final String addTagValue(String tag, int i) {
		return addTagValue(tag, i, true);
	}

	/**
	 * 构建一个XML字符串（包含一个回车换行），为特定的整型值
	 * 
	 * @param tag
	 *            XML tag
	 * @param i
	 *            tag整型值
	 * @param cr
	 *            true 表示在结尾添加一个回车换行
	 * @return XML字符串
	 */
	public static final String addTagValue(String tag, int i, boolean cr) {
		return addTagValue(tag, "" + i, cr);
	}

	/**
	 * 构建一个XML字符串（包含一个回车换行），为特定的双精度值
	 * 
	 * @param tag
	 *            XML tag
	 * @param d
	 *            tag整型值
	 * @return XML字符串
	 */
	public static final String addTagValue(String tag, double d) {
		return addTagValue(tag, d, true);
	}

	/**
	 * 构建一个XML字符串（包含一个回车换行），为特定的双精度值
	 * 
	 * @param tag
	 *            XML tag
	 * @param d
	 *            tag整型值
	 * @param cr
	 *            true 表示在结尾添加一个回车换行
	 * @return XML字符串
	 */
	public static final String addTagValue(String tag, double d, boolean cr) {
		return addTagValue(tag, "" + d, cr);
	}

	/**
	 * 构建一个XML字符串（包含一个回车换行），为特定的日期型值
	 * 
	 * @param tag
	 *            XML tag
	 * @param date
	 *            tag日期型值
	 * @return XML字符串
	 */
	public static final String addTagValue(String tag, Date date) {
		return addTagValue(tag, date, true);
	}

	/**
	 * 构建一个XML字符串（包含一个回车换行），为特定的日期型值
	 * 
	 * @param tag
	 *            XML tag
	 * @param date
	 *            tag日期型值
	 * @param cr
	 *            true 表示在结尾添加一个回车换行
	 * @return XML字符串
	 */
	public static final String addTagValue(String tag, Date date, boolean cr) {
		return addTagValue(tag, DateUtils.dat2str(date), cr);
	}

	/**
	 * 构建一个XML字符串（包含一个回车换行），为特定的BigDecimal型值
	 * 
	 * @param tag
	 *            XML tag
	 * @param val
	 *            tag BigDecimal型值
	 * @return XML字符串
	 */
	public static final String addTagValue(String tag, BigDecimal val) {
		return addTagValue(tag, val, true);
	}

	/**
	 * 构建一个XML字符串（包含一个回车换行），为特定的BigDecimal型值
	 * 
	 * @param tag
	 *            XML tag
	 * @param val
	 *            tag BigDecimal型值
	 * @param cr
	 *            true 表示在结尾添加一个回车换行
	 * @return XML字符串
	 */
	public static final String addTagValue(String tag, BigDecimal val,
			boolean cr) {
		return addTagValue(tag, val != null ? val.toString() : (String) null,
				true);
	}

	/**
	 * 构建一个XML字符串（包含一个回车换行），为特定的byte[]型值
	 * 
	 * @param tag
	 *            XML tag
	 * @param val
	 *            tag byte[]型值
	 * @return XML字符串
	 * @throws IOException
	 *             如果发生Base64或者GZip编码错误
	 */
	public static final String addTagValue(String tag, byte[] val)
			throws IOException {
		return addTagValue(tag, val, true);
	}

	/**
	 * 构建一个XML字符串（包含一个回车换行），为特定的byte[]型值
	 * 
	 * @param tag
	 *            XML tag
	 * @param val
	 *            tag byte[]型值
	 * @param cr
	 *            true 表示在结尾添加一个回车换行
	 * @return XML字符串
	 * @throws IOException
	 *             如果发生Base64或者GZip编码错误
	 */
	public static final String addTagValue(String tag, byte[] val, boolean cr)
			throws IOException {
		String string;
		if (val == null) {
			string = null;
		} else {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream gzos = new GZIPOutputStream(baos);
			BufferedOutputStream bos = new BufferedOutputStream(gzos);
			bos.write(val);
			bos.flush();
			bos.close();

			string = new String(Base64.encodeBase64(baos.toByteArray()));
		}

		return addTagValue(tag, string, true);
	}

	/**
	 * 获得特定节点的所有属性名称（在根级别上）
	 * 
	 * @param node
	 * @return 属性名列表
	 */
	public static String[] getNodeAttributes(Node node) {
		NamedNodeMap nnm = node.getAttributes();
		if (nnm != null) {
			String attributes[] = new String[nnm.getLength()];
			for (int i = 0; i < nnm.getLength(); i++) {
				Node attr = nnm.item(i);
				attributes[i] = attr.getNodeName();
			}
			return attributes;
		}
		return null;

	}

	/**
	 * 获得特点节点的所有元素名称
	 * 
	 * @param node
	 * @return
	 */
	public static String[] getNodeElements(Node node) {
		ArrayList<String> elements = new ArrayList<String>();

		NodeList nodeList = node.getChildNodes();
		if (nodeList == null)
			return null;

		for (int i = 0; i < nodeList.getLength(); i++) {
			String nodeName = nodeList.item(i).getNodeName();
			if (elements.indexOf(nodeName) < 0)
				elements.add(nodeName);
		}

		if (elements.isEmpty())
			return null;

		return elements.toArray(new String[elements.size()]);
	}

	/**
	 * 转换一个XML编码的二进制字符串为一个二进制格式
	 * 
	 * @param string
	 *            (Byte64/GZip)编码的字符串
	 * @return the decoded binary (byte[]) object编码的二进制（byte[]）对象
	 * @throws ImetaException
	 *             如果发生编码错误
	 */
	public static byte[] stringToBinary(String string) throws ImetaException {
		try {
			byte[] bytes;
			if (string == null) {
				bytes = new byte[] {};
			} else {
				bytes = Base64.decodeBase64(string.getBytes());
			}
			if (bytes.length > 0) {
				ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				GZIPInputStream gzip = new GZIPInputStream(bais);
				BufferedInputStream bi = new BufferedInputStream(gzip);
				byte[] result = new byte[] {};

				byte[] extra = new byte[1000000];
				int nrExtra = bi.read(extra);
				while (nrExtra >= 0) {
					// add it to bytes...
					//
					int newSize = result.length + nrExtra;
					byte[] tmp = new byte[newSize];
					for (int i = 0; i < result.length; i++)
						tmp[i] = result[i];
					for (int i = 0; i < nrExtra; i++)
						tmp[result.length + i] = extra[i];

					// change the result
					result = tmp;
					nrExtra = bi.read(extra);
				}
				bytes = result;
				gzip.close();
			}

			return bytes;
		} catch (Exception e) {
			throw new ImetaException("转换字符串到二进制时发生错误", e);
		}
	}

	public static String buildCDATA(String string) {
		StringBuffer cdata = new StringBuffer("<![CDATA[");
		cdata.append(Const.NVL(string, "")).append("]]>");
		return cdata.toString();
	}

	public static final String openTag(String tag) {
		return "<" + tag + ">";
	}

	public static final String closeTag(String tag) {
		return "</" + tag + ">";
	}

	public static final String openTag(String tag, boolean cr,
			String... attributes) {
		StringBuffer value = new StringBuffer("<");
		value.append(tag);

		for (int i = 0; i < attributes.length; i += 2)
			value.append(" ").append(attributes[i]).append("=\"").append(
					attributes[i + 1]).append("\" ");

		value.append('>');
		if (cr)
			value.append(Const.CR);
		return value.toString();
	}

	/**
	 * 通过字符串获得输入流
	 * 
	 * @param value
	 * @return
	 */
	public static final InputSource createInputSource(String value) {
		value = Const.NVL(value, "");
		return createInputSource(null, null, value);
	}

	/**
	 * 通过字符串获得输入流
	 * 
	 * @param publicID
	 * @param systemID
	 * @param value
	 * @return
	 */
	public static final InputSource createInputSource(
			java.lang.String publicID, java.lang.String systemID, String value) {
		if (Const.isEmpty(value)) {
			value = "";
		}
		InputSource is = new InputSource(new ByteArrayInputStream(value
				.getBytes()));
		is.setPublicId(publicID);
		is.setSystemId(systemID);
		is.setEncoding(Const.XML_ENCODING);
		return is;
	}

}

/**
 * 处理外部的引用，并且返回空对象文档
 * 
 * @author 潘巍（Peter Pan）
 * @since 2010-9-9 上午12:50:33
 */
class DTDIgnoringEntityResolver implements EntityResolver {
	public DTDIgnoringEntityResolver() {
		// nothing
	}

	public InputSource resolveEntity(java.lang.String publicID,
			java.lang.String systemID) throws IOException {
		System.out.println("Public-ID: " + publicID.toString());
		System.out.println("System-ID: " + systemID.toString());

		return XMLHandler.createInputSource(publicID, systemID, "");
	}

}
