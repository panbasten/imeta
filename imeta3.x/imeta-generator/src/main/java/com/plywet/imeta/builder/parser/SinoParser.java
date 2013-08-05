package com.plywet.imeta.builder.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

/**
 * 系统XML解析类，泛型T-定义解析根对象的类型
 * 
 * @author 潘巍（Peter Pan）
 * @since 2010-8-12 下午04:50:01
 */
public class SinoParser<T> {

	private static final Logger logger = Logger.getLogger(SinoParser.class
			.getName());

	/**
	 * 通过XML枚举类获得模式工厂
	 * 
	 * @param t
	 * @return
	 * @throws JAXBException
	 * @throws SAXException
	 */
	public Schema getSchema(SinoParserType t) {
		try {
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			String[] sourceArr = t.getSchemaFileName().split(",");
			Source[] sources = new Source[sourceArr.length];
			for (int i = 0; i < sourceArr.length; i++) {
				sources[i] = new StreamSource(getResourceStream(sourceArr[i]));
			}
			return schemaFactory.newSchema(sources);
		} catch (Exception e) {
			logger.info("解析Schema文件出现错误(getSchema)：" + t.getSchemaFileName());
		}

		return null;
	}

	/**
	 * 通过XML枚举类获得解析器
	 * 
	 * @param t
	 * @return
	 * @throws JAXBException
	 */
	public Unmarshaller getUnmarshaller(SinoParserType t) {

		try {
			// 创建一个jaxb上下文
			JAXBContext jaxbContext = JAXBContext.newInstance(t
					.getJaxbFactoryPackage());

			// 创建一个解组器，并且设置其模式
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			unmarshaller.setSchema(getSchema(t));

			return unmarshaller;

		} catch (Exception e) {
			logger.info("获得解析器出现错误(getUnmarshaller)：" + t.getSchemaFileName());
		}

		return null;
	}

	/**
	 * 通过XML枚举类获得构造器
	 * 
	 * @param t
	 * @return
	 */
	public Marshaller getMarshaller(SinoParserType t) {
		try {
			// 创建一个jaxb上下文
			JAXBContext jaxbContext = JAXBContext.newInstance(t
					.getJaxbFactoryPackage());

			// 创建一个解组器，并且设置其模式
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setSchema(getSchema(t));

			return marshaller;

		} catch (Exception e) {
			logger.info("获得构造器出现错误(getMarshaller)：" + t.getSchemaFileName());
		}

		return null;
	}

	/**
	 * 通过XML枚举类反解析
	 * 
	 * @param t
	 * @param o
	 * @param pathAndName
	 *            相对路径和名称
	 * @throws IOException
	 * @throws JAXBException
	 */
	public void unparse(SinoParserType t, T o, String pathAndName)
			throws IOException, JAXBException {
		logger.info("Unparsing Schema : " + t.getSchemaFileName());
		logger.info("Unparsing XML dir : " + t.getXmlFileNameOrPath());
		File file = getXMLFile(t);
		File f = new File(file, pathAndName);
		f.delete();
		unparse(t, o, f);
	}

	/**
	 * 通过XML枚举类反解析
	 * 
	 * @param t
	 * @param o
	 * @param f
	 *            创建文件的File对象
	 * @throws JAXBException
	 */
	public void unparse(SinoParserType t, T o, File f) throws JAXBException {
		Marshaller marshaller = getMarshaller(t);
		marshaller.marshal(o, f);
	}

	/**
	 * 通过XML枚举类解析
	 * 
	 * @param t
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 */
	public List<T> parse(SinoParserType t, List<String> errs)
			throws IOException {
		logger.info("Parsing Schema : " + t.getSchemaFileName());
		logger.info("Parsing XML dir : " + t.getXmlFileNameOrPath());
		Unmarshaller unmarshaller = getUnmarshaller(t);
		// 创建返回对象
		List<T> rtn = new ArrayList<T>();

		// 获得xml对象
		T tt = null;
		File file = getXMLFile(t);
		if (file != null && file.exists()) {
			logger.info(file.getName());
			if (file.isDirectory()) {
				parse(rtn, unmarshaller, file, t, errs);
			} else {
				if (t.accept(file)) {
					tt = parse(unmarshaller, t.getXmlFileNameOrPath(), errs);
					if (tt != null)
						rtn.add(tt);
				}
			}
		}

		return rtn;

	}

	/**
	 * 通过Schema解析器和文件路径获得解析对象
	 * 
	 * @param unmarshaller
	 * @param fileName
	 * @return
	 * @throws JAXBException
	 */
	public T parse(Unmarshaller unmarshaller, String fileName, List<String> errs) {
		logger.info("Parsing XML : " + fileName);
		try {
			return parse(unmarshaller, getResourceStream(fileName));
		} catch (JAXBException e) {
			logger.severe("Error in parsing XML : " + fileName);
			logger.severe(e.toString());
			if (errs != null) {
				errs.add("解析XML出现错误：" + fileName + "\n" + e.toString());
			}
		}
		return null;
	}

	/**
	 * 通过Schema解析器和文件流获得解析对象
	 * 
	 * @param unmarshaller
	 * @param is
	 * @return
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public T parse(Unmarshaller unmarshaller, InputStream is)
			throws JAXBException {
		return (T) unmarshaller.unmarshal(new StreamSource(is));

	}

	/**
	 * 解析文件夹下面的所有文件
	 * 
	 * @param unmarshaller
	 * @param parent
	 * @throws JAXBException
	 */
	private void parse(List<T> rtn, Unmarshaller unmarshaller, File parent,
			SinoParserType t, List<String> errs) {
		File[] files = parent.listFiles();
		T tt = null;
		for (File file : files) {
			if (file.isDirectory()) {
				parse(rtn, unmarshaller, file, t, errs);
			} else {
				if (t.accept(file)) {
					tt = parse(unmarshaller, file.getPath(), errs);
					if (tt != null)
						rtn.add(tt);
				}
			}
		}
	}

	/**
	 * 获得文件形式的XML的File
	 * 
	 * @param t
	 * @return
	 * @throws IOException
	 */
	public static File getXMLFile(SinoParserType t) throws IOException {
		List<URL> urls = getURLs(t.getXmlFileNameOrPath());
		if (urls != null) {
			for (URL u : urls) {
				File f = new File(u.getFile());
				if (f.isFile() || f.isDirectory()) {
					return f;
				}
			}
		}
		return null;
	}

	public static List<URL> getURLs(String filename) throws IOException {
		ClassLoader classLoader = SinoParser.class.getClassLoader();
		List<URL> urlList = new ArrayList<URL>();
		Enumeration<URL> urls = classLoader.getResources(filename);
		while (urls.hasMoreElements()) {
			urlList.add(urls.nextElement());
		}
		return urlList;
	}

	public static URL getURL(String filename) throws MalformedURLException {
		URL url;
		File file = new File(filename);
		if (file.exists()) {
			url = file.toURI().toURL();
		} else {
			ClassLoader classLoader = SinoParser.class.getClassLoader();
			url = classLoader.getResource(filename);
		}
		return url;
	}

	/**
	 * 获得一个资源的输入流的实用方法。 这个方法尝试处理不同服务器之间的API差异实现。
	 * 
	 * @param path
	 *            资源路径
	 * @return 给定路径资源的输入流
	 */
	public static InputStream getResourceStream(String path) {

		InputStream sourceStream = null;

		try {
			// 1.直接通过绝对路径查找文件流
			sourceStream = new FileInputStream(new File(path));
		} catch (Exception e) {
		}
		if (sourceStream == null) {
			// 2.从类加载器中获得文件流
			sourceStream = SinoParser.class.getClassLoader()
					.getResourceAsStream(path);
		}

		return sourceStream;
	}
}
