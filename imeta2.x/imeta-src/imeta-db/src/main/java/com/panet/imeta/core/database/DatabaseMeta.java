/* Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.*/

package com.panet.imeta.core.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Node;

import com.panet.imeta.core.Const;
import com.panet.imeta.core.RowMetaAndData;
import com.panet.imeta.core.encryption.Encr;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.core.row.ValueMetaInterface;
import com.panet.imeta.core.variables.VariableSpace;
import com.panet.imeta.core.variables.Variables;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.core.xml.XMLInterface;
import com.panet.imeta.shared.SharedObjectBase;
import com.panet.imeta.shared.SharedObjectInterface;

/**
 * This class defines the database specific parameters for a certain database
 * type. It also provides static information regarding a number of well known
 * databases.
 * 
 * @author Matt
 * @since 18-05-2003
 * 
 */
public class DatabaseMeta extends SharedObjectBase implements Cloneable,
		XMLInterface, SharedObjectInterface, VariableSpace {
	public static final String XML_TAG = "connection";

	// Comparator for sorting databases alphabetically by name
	public static final Comparator<DatabaseMeta> comparator = new Comparator<DatabaseMeta>() {
		public int compare(DatabaseMeta dbm1, DatabaseMeta dbm2) {
			return dbm1.getName().compareToIgnoreCase(dbm2.getName());
		}
	};

	private DatabaseInterface databaseInterface;
	private static DatabaseInterface[] allDatabaseInterfaces;

	private VariableSpace variables = new Variables();

	/**
	 * Indicates that the connections doesn't point to a type of database yet.
	 */
	public static final int TYPE_DATABASE_NONE = 0;

	/**
	 * Connection to a MySQL database
	 */
	public static final int TYPE_DATABASE_MYSQL = 1;

	/**
	 * Connection to an Oracle database
	 */
	public static final int TYPE_DATABASE_ORACLE = 2;

	/**
	 * Connection to an AS/400 (IBM iSeries) DB400 database
	 */
	public static final int TYPE_DATABASE_AS400 = 3;

	/**
	 * Connection to an Microsoft Access database
	 */
	public static final int TYPE_DATABASE_ACCESS = 4;

	/**
	 * Connection to a Microsoft SQL Server database
	 */
	public static final int TYPE_DATABASE_MSSQL = 5;

	/**
	 * Connection to an IBM DB2 database
	 */
	public static final int TYPE_DATABASE_DB2 = 6;

	/**
	 * Connection to a PostgreSQL database
	 */
	public static final int TYPE_DATABASE_POSTGRES = 7;

	/**
	 * Connection to an Intersystems Cache database
	 */
	public static final int TYPE_DATABASE_CACHE = 8;

	/**
	 * Connection to an IBM Informix database
	 */
	public static final int TYPE_DATABASE_INFORMIX = 9;

	/**
	 * Connection to a Sybase ASE database
	 */
	public static final int TYPE_DATABASE_SYBASE = 10;

	/**
	 * Connection to a Gupta SQLBase database
	 */
	public static final int TYPE_DATABASE_GUPTA = 11;

	/**
	 * Connection to a DBase III/IV/V database through JDBC
	 */
	public static final int TYPE_DATABASE_DBASE = 12;

	/**
	 * Connection to a FireBird database
	 */
	public static final int TYPE_DATABASE_FIREBIRD = 13;

	/**
	 * Connection to a SAP DB database
	 */
	public static final int TYPE_DATABASE_SAPDB = 14;

	/**
	 * Connection to a Hypersonic java database
	 */
	public static final int TYPE_DATABASE_HYPERSONIC = 15;

	/**
	 * Connection to a generic database
	 */
	public static final int TYPE_DATABASE_GENERIC = 16;

	/**
	 * Connection to an SAP R/3 system
	 */
	public static final int TYPE_DATABASE_SAPR3 = 17;

	/**
	 * Connection to an Ingress database
	 */
	public static final int TYPE_DATABASE_INGRES = 18;

	/**
	 * Connection to a Borland Interbase database
	 */
	public static final int TYPE_DATABASE_INTERBASE = 19;

	/**
	 * Connection to an ExtenDB database
	 */
	public static final int TYPE_DATABASE_EXTENDB = 20;

	/**
	 * Connection to a Teradata database
	 */
	public static final int TYPE_DATABASE_TERADATA = 21;

	/**
	 * Connection to an Oracle RDB database
	 */
	public static final int TYPE_DATABASE_ORACLE_RDB = 22;

	/**
	 * Connection to an H2 database
	 */
	public static final int TYPE_DATABASE_H2 = 23;

	/**
	 * Connection to a Netezza database
	 */
	public static final int TYPE_DATABASE_NETEZZA = 24;

	/**
	 * Connection to an IBM UniVerse database
	 */
	public static final int TYPE_DATABASE_UNIVERSE = 25;

	/**
	 * Connection to a SQLite database
	 */
	public static final int TYPE_DATABASE_SQLITE = 26;

	/**
	 * Connection to an Apache Derby database
	 */
	public static final int TYPE_DATABASE_DERBY = 27;

	/**
	 * Connection to a BMC Remedy Action Request System
	 */
	public static final int TYPE_DATABASE_REMEDY_AR_SYSTEM = 28;

	/**
	 * Connection to a Palo MOLAP Server
	 */
	public static final int TYPE_DATABASE_PALO = 29;

	/**
	 * Connection to a SybaseIQ ASE database
	 */
	public static final int TYPE_DATABASE_SYBASEIQ = 30;

	/**
	 * Connection to a Greenplum database
	 */
	public static final int TYPE_DATABASE_GREENPLUM = 31;

	/**
	 * Connection to a MonetDB database
	 */
	public static final int TYPE_DATABASE_MONETDB = 32;

	/**
	 * Connection to a KingbaseES database
	 */
	public static final int TYPE_DATABASE_KINGBASEES = 33;

	/**
	 * Connection to a Vertica database
	 */
	public static final int TYPE_DATABASE_VERTICA = 34;

	/**
	 * Connection to a Neoview database
	 */
	public static final int TYPE_DATABASE_NEOVIEW = 35;

	/**
	 * Connection to a LucidDB database
	 */
	public static final int TYPE_DATABASE_LUCIDDB = 36;

	/**
	 * Connect natively through JDBC thin driver to the database.
	 */
	public static final int TYPE_ACCESS_NATIVE = 0;

	/**
	 * Connect to the database using ODBC.
	 */
	public static final int TYPE_ACCESS_ODBC = 1;

	/**
	 * Connect to the database using OCI. (Oracle only)
	 */
	public static final int TYPE_ACCESS_OCI = 2;

	/**
	 * Connect to the database using plugin specific method. (SAP R/3)
	 */
	public static final int TYPE_ACCESS_PLUGIN = 3;

	/**
	 * Connect to the database using JNDI.
	 */
	public static final int TYPE_ACCESS_JNDI = 4;

	/**
	 * Short description of the access type, used in XML and the repository.
	 */
	public static final String dbAccessTypeCode[] = { "Native", "ODBC", "OCI",
			"Plugin", "JNDI", };

	/**
	 * Longer description for user interactions.
	 */
	public static final String dbAccessTypeDesc[] = {
			Messages.getString("Database.AccessType.Native.Desc"),
			Messages.getString("Database.AccessType.ODBC.Desc"),
			Messages.getString("Database.AccessType.OCI.Desc"),
			Messages.getString("Database.AccessType.Plugin.Desc"),
			Messages.getString("Database.AccessType.JNDI.Desc"),
			Messages.getString("Database.AccessType.Custom.Desc"), };

	/**
	 * Use this length in a String value to indicate that you want to use a CLOB
	 * in stead of a normal text field.
	 */
	public static final int CLOB_LENGTH = 9999999;

	/**
	 * The value to store in the attributes so that an empty value doesn't get
	 * lost...
	 */
	public static final String EMPTY_OPTIONS_STRING = "><EMPTY><";

	/**
	 * Construct a new database connections. Note that not all these parameters
	 * are not always mandatory. 不是所有参数都是强制性的
	 * 
	 * @param name
	 *            The database name  数据库名称
	 * @param type
	 *            The type of database  数据库类型
	 * @param access
	 *            The type of database access 数据库访问类型
	 * @param host
	 *            The hostname or IP address 数据库ip地址
	 * @param db
	 *            The database name 数据库名称
	 * @param port
	 *            The port on which the database listens. 数据库端口
	 * @param user
	 *            The username 用户名
	 * @param pass
	 *            The password 密码
	 */
	public DatabaseMeta(String name, String type, String access, String host,
			String db, String port, String user, String pass) {
		setValues(name, type, access, host, db, port, user, pass);
		addOptions();
	}

	/**
	 * Create an empty database connection 创建一个空的数据库连接
	 * 
	 */
	public DatabaseMeta() {
		setDefault();
		addOptions();
	}

	/**
	 * Set default values for an Oracle database. 为数据库设置一些默认的基本配置值
	 * 
	 */
	public void setDefault() {
		setValues("", "Oracle", "Native", "", "", "1521", "", "");
	}

	/**
	 * Add a list of common options for some databases. 增加一些数据库的操作列表
	 * 
	 */
	public void addOptions() {
		String mySQL = new MySQLDatabaseMeta().getDatabaseTypeDesc();

		addExtraOption(mySQL, "defaultFetchSize", "500");
		addExtraOption(mySQL, "useCursorFetch", "true");
	}

	/**
	 * @return the system dependend database interface for this database
	 *         metadata definition   返回定义数据库元的接口的对象
	 */
	public DatabaseInterface getDatabaseInterface() {
		return databaseInterface;
	}

	/**
	 * Set the system dependend database interface for this database metadata
	 * definition 设置数据源的接口的对象
	 * 
	 * @param databaseInterface
	 *            the system dependend database interface
	 */
	public void setDatabaseInterface(DatabaseInterface databaseInterface) {
		this.databaseInterface = databaseInterface;
	}

	/**
	 * Search for the right type of DatabaseInterface object and clone it.找到合适的类型DatabaseInterface对象和克隆它
	 * 
	 * @param databaseType
	 *            the type of DatabaseInterface to look for (description)
	 * @return The requested DatabaseInterface
	 * 
	 * @throws KettleDatabaseException
	 *             when the type could not be found or referenced.
	 */
	public static final DatabaseInterface getDatabaseInterface(
			String databaseType) throws KettleDatabaseException {
		return (DatabaseInterface) findDatabaseInterface(databaseType).clone();
	}

	/**
	 * Search for the right type of DatabaseInterface object and return it.找到合适的类型DatabaseInterface对象
	 * 
	 * @param databaseType
	 *            the type of DatabaseInterface to look for (description)
	 * @return The requested DatabaseInterface
	 * 
	 * @throws KettleDatabaseException
	 *             when the type could not be found or referenced.
	 */
	private static final DatabaseInterface findDatabaseInterface(
			String databaseTypeDesc) throws KettleDatabaseException {
		DatabaseInterface di[] = getDatabaseInterfaces();
		for (int i = 0; i < di.length; i++) {
			if (di[i].getDatabaseTypeDesc().equalsIgnoreCase(databaseTypeDesc)
					|| di[i].getDatabaseTypeDescLong().equalsIgnoreCase(
							databaseTypeDesc))
				return di[i];
		}

		throw new KettleDatabaseException("database type [" + databaseTypeDesc
				+ "] couldn't be found!");
	}

	/**
	 * Returns the database ID of this database connection if a repository was
	 * used before.返回数据库ID的数据库连接
	 * 
	 * @return the ID of the db connection.
	 */
	public long getID() {
		return databaseInterface.getId();
	}

	public void setID(long id) {
		databaseInterface.setId(id);
	}

	public Object clone() {
		DatabaseMeta databaseMeta = new DatabaseMeta();
		databaseMeta.replaceMeta(this);
		databaseMeta.setID(-1L);
		return databaseMeta;
	}

	public void replaceMeta(DatabaseMeta databaseMeta) {
		this.setValues(databaseMeta.getName(), databaseMeta
				.getDatabaseTypeDesc(), databaseMeta.getAccessTypeDesc(),
				databaseMeta.getHostname(), databaseMeta.getDatabaseName(),
				databaseMeta.getDatabasePortNumberString(), databaseMeta
						.getUsername(), databaseMeta.getPassword());
		this.setServername(databaseMeta.getServername());
		this.setDataTablespace(databaseMeta.getDataTablespace());
		this.setIndexTablespace(databaseMeta.getIndexTablespace());

		this.databaseInterface = (DatabaseInterface) databaseMeta.databaseInterface
				.clone();

		this.setID(databaseMeta.getID());
		this.setChanged();
	}

	public void setValues(String name, String type, String access, String host,
			String db, String port, String user, String pass) {
		try {
			databaseInterface = getDatabaseInterface(type);
		} catch (KettleDatabaseException kde) {
			throw new RuntimeException("Database type not found!", kde);
		}

		setName(name);
		setAccessType(getAccessType(access));
		setHostname(host);
		setDBName(db);
		setDBPort(port);
		setUsername(user);
		setPassword(pass);
		setServername(null);
		setChanged(false);
	}

	public void setDatabaseType(String type) {
		DatabaseInterface oldInterface = databaseInterface;

		try {
			databaseInterface = getDatabaseInterface(type);
		} catch (KettleDatabaseException kde) {
			throw new RuntimeException("Database type [" + type
					+ "] not found!", kde);
		}

		setName(oldInterface.getName());
		setAccessType(oldInterface.getAccessType());
		setHostname(oldInterface.getHostname());
		setDBName(oldInterface.getDatabaseName());
		setDBPort(oldInterface.getDatabasePortNumberString());
		setUsername(oldInterface.getUsername());
		setPassword(oldInterface.getPassword());
		setServername(oldInterface.getServername());
		setDataTablespace(oldInterface.getDataTablespace());
		setIndexTablespace(oldInterface.getIndexTablespace());
		setChanged(oldInterface.isChanged());
	}

	public void setValues(DatabaseMeta info) {
		databaseInterface = (DatabaseInterface) info.databaseInterface.clone();
	}

	/**
	 * Sets the name of the database connection. This name should be unique in a
	 * transformation and in general in a single repository.设置数据库连接的名字
	 * 
	 * @param name
	 *            The name of the database connection
	 */
	public void setName(String name) {
		databaseInterface.setName(name);
	}

	/**
	 * Returns the name of the database connection返回数据库连接的名字
	 * 
	 * @return The name of the database connection
	 */
	public String getName() {
		return databaseInterface.getName();
	}

	/**
	 * Returns the type of database, one of返回数据库的类型
	 * <p>
	 * TYPE_DATABASE_MYSQL
	 * <p>
	 * TYPE_DATABASE_ORACLE
	 * <p>
	 * TYPE_DATABASE_...
	 * <p>
	 * 
	 * @return the database type
	 */
	public int getDatabaseType() {
		return databaseInterface.getDatabaseType();
	}

	/*
	 * Sets the type of database. @param db_type The database type public void
	 * setDatabaseType(int db_type) { databaseInterface this.databaseType =
	 * db_type; }
	 */

	/**
	 * Return the type of database access. One of返回数据库访问类型
	 * <p>
	 * TYPE_ACCESS_NATIVE
	 * <p>
	 * TYPE_ACCESS_ODBC
	 * <p>
	 * TYPE_ACCESS_OCI
	 * <p>
	 * 
	 * @return The type of database access.
	 */
	public int getAccessType() {
		return databaseInterface.getAccessType();
	}

	/**
	 * Set the type of database access.
	 * 
	 * @param access_type
	 *            The access type.
	 */
	public void setAccessType(int access_type) {
		databaseInterface.setAccessType(access_type);
	}

	/**
	 * Returns a short description of the type of database.
	 * 
	 * @return A short description of the type of database.
	 */
	public String getDatabaseTypeDesc() {
		return databaseInterface.getDatabaseTypeDesc();
	}

	/**
	 * Gets you a short description of the type of database access.
	 * 
	 * @return A short description of the type of database access.
	 */
	public String getAccessTypeDesc() {
		return dbAccessTypeCode[getAccessType()];
	}

	/**
	 * Return the hostname of the machine on which the database runs.
	 * 
	 * @return The hostname of the database.
	 */
	public String getHostname() {
		return databaseInterface.getHostname();
	}

	/**
	 * Sets the hostname of the machine on which the database runs.
	 * 
	 * @param hostname
	 *            The hostname of the machine on which the database runs.
	 */
	public void setHostname(String hostname) {
		databaseInterface.setHostname(hostname);
	}

	/**
	 * Return the port on which the database listens as a String. Allows for
	 * parameterisation.
	 * 
	 * @return The database port.
	 */
	public String getDatabasePortNumberString() {
		return databaseInterface.getDatabasePortNumberString();
	}

	/**
	 * Sets the port on which the database listens.
	 * 
	 * @param db_port
	 *            The port number on which the database listens
	 */
	public void setDBPort(String db_port) {
		databaseInterface.setDatabasePortNumberString(db_port);
	}

	/**
	 * Return the name of the database.
	 * 
	 * @return The database name.
	 */
	public String getDatabaseName() {
		return databaseInterface.getDatabaseName();
	}

	/**
	 * Set the name of the database.
	 * 
	 * @param databaseName
	 *            The new name of the database
	 */
	public void setDBName(String databaseName) {
		databaseInterface.setDatabaseName(databaseName);
	}

	/**
	 * Get the username to log into the database on this connection.
	 * 
	 * @return The username to log into the database on this connection.
	 */
	public String getUsername() {
		return databaseInterface.getUsername();
	}

	/**
	 * Sets the username to log into the database on this connection.
	 * 
	 * @param username
	 *            The username
	 */
	public void setUsername(String username) {
		databaseInterface.setUsername(username);
	}

	/**
	 * Get the password to log into the database on this connection.
	 * 
	 * @return the password to log into the database on this connection.
	 */
	public String getPassword() {
		return databaseInterface.getPassword();
	}

	/**
	 * Sets the password to log into the database on this connection.
	 * 
	 * @param password
	 *            the password to log into the database on this connection.
	 */
	public void setPassword(String password) {
		databaseInterface.setPassword(password);
	}

	/**
	 * @param servername
	 *            the Informix servername
	 */
	public void setServername(String servername) {
		databaseInterface.setServername(servername);
	}

	/**
	 * @return the Informix servername
	 */
	public String getServername() {
		return databaseInterface.getServername();
	}

	public String getDataTablespace() {
		return databaseInterface.getDataTablespace();
	}

	public void setDataTablespace(String data_tablespace) {
		databaseInterface.setDataTablespace(data_tablespace);
	}

	public String getIndexTablespace() {
		return databaseInterface.getIndexTablespace();
	}

	public void setIndexTablespace(String index_tablespace) {
		databaseInterface.setIndexTablespace(index_tablespace);
	}

	public void setChanged() {
		setChanged(true);
	}

	public void setChanged(boolean ch) {
		databaseInterface.setChanged(ch);
	}

	public boolean hasChanged() {
		return databaseInterface.isChanged();
	}

	public String toString() {
		return getName();
	}

	/**
	 * @return The extra attributes for this database connection返回数据库连接的额外的特征
	 */
	public Properties getAttributes() {
		return databaseInterface.getAttributes();
	}

	/**
	 * Set extra attributes on this database connection
	 * 
	 * @param attributes
	 *            The extra attributes to set on this database connection.
	 */
	public void setAttributes(Properties attributes) {
		databaseInterface.setAttributes(attributes);
	}

	/**
	 * Constructs a new database using an XML string snippet. It expects the
	 * snippet to be enclosed in <code>connection</code> tags.通过读xml配置文件 构造一个新的数据库
	 * 
	 * @param xml
	 *            The XML string to parse
	 * @throws KettleXMLException
	 *             in case there is an XML parsing error
	 */
	public DatabaseMeta(String xml) throws KettleXMLException {
		this(XMLHandler.getSubNode(XMLHandler.loadXMLString(xml), "connection"));
	}

	/**
	 * Reads the information from an XML Node into this new database connection.
	 * 读取的信息从一个XML节点进入这一新的数据库连接
	 * @param con
	 *            The Node to read the data from
	 * @throws KettleXMLException
	 */
	public DatabaseMeta(Node con) throws KettleXMLException {
		this();

		try {
			String type = XMLHandler.getTagValue(con, "type");
			try {
				databaseInterface = getDatabaseInterface(type);

			} catch (KettleDatabaseException kde) {
				throw new KettleXMLException(
						"Unable to create new database interface", kde);
			}

			setName(XMLHandler.getTagValue(con, "name"));
			setHostname(XMLHandler.getTagValue(con, "server"));
			String acc = XMLHandler.getTagValue(con, "access");
			setAccessType(getAccessType(acc));

			setDBName(XMLHandler.getTagValue(con, "database"));
			setDBPort(XMLHandler.getTagValue(con, "port"));
			setUsername(XMLHandler.getTagValue(con, "username"));
			setPassword(Encr.decryptPasswordOptionallyEncrypted(XMLHandler
					.getTagValue(con, "password")));
			setServername(XMLHandler.getTagValue(con, "servername"));
			setDataTablespace(XMLHandler.getTagValue(con, "data_tablespace"));
			setIndexTablespace(XMLHandler.getTagValue(con, "index_tablespace"));

			// Also, read the database attributes...
			Node attrsnode = XMLHandler.getSubNode(con, "attributes");
			if (attrsnode != null) {
				int nr = XMLHandler.countNodes(attrsnode, "attribute");
				for (int i = 0; i < nr; i++) {
					Node attrnode = XMLHandler.getSubNodeByNr(attrsnode,
							"attribute", i);
					String code = XMLHandler.getTagValue(attrnode, "code");
					String attribute = XMLHandler.getTagValue(attrnode,
							"attribute");
					if (code != null && attribute != null)
						getAttributes().put(code, attribute);
				}
			}
		} catch (Exception e) {
			throw new KettleXMLException(
					"Unable to load database connection info from XML node", e);
		}
	}

	@SuppressWarnings("unchecked")
	public String getXML() {
		StringBuffer retval = new StringBuffer(250);

		retval.append("  <").append(XML_TAG).append('>').append(Const.CR);
		retval.append("    ").append(XMLHandler.addTagValue("name", getName()));
		retval.append("    ").append(
				XMLHandler.addTagValue("server", getHostname()));
		retval.append("    ").append(
				XMLHandler.addTagValue("type", getDatabaseTypeDesc()));
		retval.append("    ").append(
				XMLHandler.addTagValue("access", getAccessTypeDesc()));
		retval.append("    ").append(
				XMLHandler.addTagValue("database", getDatabaseName()));
		retval.append("    ").append(
				XMLHandler.addTagValue("port", getDatabasePortNumberString()));
		retval.append("    ").append(
				XMLHandler.addTagValue("username", getUsername()));
		retval.append("    ").append(
				XMLHandler.addTagValue("password", Encr
						.encryptPasswordIfNotUsingVariables(getPassword())));
		retval.append("    ").append(
				XMLHandler.addTagValue("servername", getServername()));
		retval.append("    ").append(
				XMLHandler.addTagValue("data_tablespace", getDataTablespace()));
		retval.append("    ").append(
				XMLHandler
						.addTagValue("index_tablespace", getIndexTablespace()));

		retval.append("    <attributes>").append(Const.CR);

		List list = new ArrayList(getAttributes().keySet());
		Collections.sort(list); // Sort the entry-sets to make sure we can
		// compare XML strings: if the order is
		// different, the XML is different.

		for (Iterator iter = list.iterator(); iter.hasNext();) {
			String code = (String) iter.next();
			String attribute = getAttributes().getProperty(code);
			if (!Const.isEmpty(attribute)) {
				retval.append("      <attribute>"
						+ XMLHandler.addTagValue("code", code, false)
						+ XMLHandler.addTagValue("attribute", attribute, false)
						+ "</attribute>" + Const.CR);
			}
		}
		retval.append("    </attributes>").append(Const.CR);

		retval.append("  </" + XML_TAG + ">").append(Const.CR);
		return retval.toString();
	}

	public int hashCode() {
		return getName().hashCode(); // name of connection is unique!
	}

	public boolean equals(Object obj) {
		return getName().equals(((DatabaseMeta) obj).getName());
	}

	public String getURL() throws KettleDatabaseException {
		return getURL(null);
	}

	public String getURL(String partitionId) throws KettleDatabaseException {
		// First see if we're not doing any JNDI...
		// 
		if (getAccessType() == TYPE_ACCESS_JNDI) {
			// We can't really determine the URL here.
			//
			//
		}

		String baseUrl;
		if (isPartitioned() && !Const.isEmpty(partitionId)) {
			// Get the cluster information...
			PartitionDatabaseMeta partition = getPartitionMeta(partitionId);
			String hostname = partition.getHostname();
			String port = partition.getPort();
			String databaseName = partition.getDatabaseName();

			baseUrl = databaseInterface.getURL(hostname, port, databaseName);
		} else {
			baseUrl = databaseInterface.getURL(getHostname(),
					getDatabasePortNumberString(), getDatabaseName());
		}
		StringBuffer url = new StringBuffer(environmentSubstitute(baseUrl));

		if (databaseInterface.supportsOptionsInURL()) {
			// OK, now add all the options...
			String optionIndicator = getExtraOptionIndicator();
			String optionSeparator = getExtraOptionSeparator();
			String valueSeparator = getExtraOptionValueSeparator();

			Map<String, String> map = getExtraOptions();
			if (map.size() > 0) {
				Iterator<String> iterator = map.keySet().iterator();
				boolean first = true;
				while (iterator.hasNext()) {
					String typedParameter = (String) iterator.next();
					int dotIndex = typedParameter.indexOf('.');
					if (dotIndex >= 0) {
						String typeCode = typedParameter.substring(0, dotIndex);
						String parameter = typedParameter
								.substring(dotIndex + 1);
						String value = map.get(typedParameter);

						// Only add to the URL if it's the same database type
						// code...
						//
						if (databaseInterface.getDatabaseTypeDesc().equals(
								typeCode)) {
							if (first && url.indexOf(valueSeparator) == -1) {
								url.append(optionIndicator);
							} else {
								url.append(optionSeparator);
							}

							url.append(parameter);
							if (!Const.isEmpty(value)
									&& !value.equals(EMPTY_OPTIONS_STRING)) {
								url.append(valueSeparator).append(value);
							}
							first = false;
						}
					}
				}
			}
		} else {
			// We need to put all these options in a Properties file later
			// (Oracle & Co.)
			// This happens at connect time...
		}

		return url.toString();
	}

	public Properties getConnectionProperties() {
		Properties properties = new Properties();

		Map<String, String> map = getExtraOptions();
		if (map.size() > 0) {
			Iterator<String> iterator = map.keySet().iterator();
			while (iterator.hasNext()) {
				String typedParameter = (String) iterator.next();
				int dotIndex = typedParameter.indexOf('.');
				if (dotIndex >= 0) {
					String typeCode = typedParameter.substring(0, dotIndex);
					String parameter = typedParameter.substring(dotIndex + 1);
					String value = (String) map.get(typedParameter);

					// Only add to the URL if it's the same database type
					// code...
					//
					if (databaseInterface.getDatabaseTypeDesc()
							.equals(typeCode)) {
						if (value != null && value.equals(EMPTY_OPTIONS_STRING))
							value = "";
						properties.put(parameter, environmentSubstitute(Const
								.NVL(value, "")));
					}
				}
			}
		}

		return properties;
	}

	public String getExtraOptionIndicator() {
		return databaseInterface.getExtraOptionIndicator();
	}

	/**
	 * @return The extra option separator in database URL for this platform
	 *         (usually this is semicolon ; )额外的选择分离数据库网址这个平台
	 */
	public String getExtraOptionSeparator() {
		return databaseInterface.getExtraOptionSeparator();
	}

	/**
	 * @return The extra option value separator in database URL for this
	 *         platform (usually this is the equal sign = )
	 */
	public String getExtraOptionValueSeparator() {
		return databaseInterface.getExtraOptionValueSeparator();
	}

	/**
	 * Add an extra option to the attributes list增加额外的属性选择名单
	 * 
	 * @param databaseTypeCode
	 *            The database type code for which the option applies
	 * @param option
	 *            The option to set
	 * @param value
	 *            The value of the option
	 */
	public void addExtraOption(String databaseTypeCode, String option,
			String value) {
		databaseInterface.addExtraOption(databaseTypeCode, option, value);
	}

	/**
	 * @deprecated because the same database can support transactions or not. It
	 *             all depends on the database setup. Therefor, we look at the
	 *             database metadata DatabaseMetaData.supportsTransactions() in
	 *             stead of this.
	 * @return true if the database supports transactions
	 */
	public boolean supportsTransactions() {
		return databaseInterface.supportsTransactions();
	}

	public boolean supportsAutoinc() {
		return databaseInterface.supportsAutoInc();
	}

	public boolean supportsSequences() {
		return databaseInterface.supportsSequences();
	}

	public String getSQLSequenceExists(String sequenceName) {
		return databaseInterface.getSQLSequenceExists(sequenceName);
	}

	public boolean supportsBitmapIndex() {
		return databaseInterface.supportsBitmapIndex();
	}

	public boolean supportsSetLong() {
		return databaseInterface.supportsSetLong();
	}

	/**
	 * @return true if the database supports schemas返回真正如果数据库支持模式
	 */
	public boolean supportsSchemas() {
		return databaseInterface.supportsSchemas();
	}

	/**
	 * @return true if the database supports catalogs返回真正如果数据库支持目录
	 */
	public boolean supportsCatalogs() {
		return databaseInterface.supportsCatalogs();
	}

	/**
	 * 
	 * @return true when the database engine supports empty transaction. (for
	 *         example Informix does not on a non-ANSI database type!)返回数据库引擎支持空转换的布尔值
	 */
	public boolean supportsEmptyTransactions() {
		return databaseInterface.supportsEmptyTransactions();
	}

	/**
	 * See if this database supports the setCharacterStream() method on a
	 * PreparedStatement.
	 * 
	 * @return true if we can set a Stream on a field in a PreparedStatement.
	 *         False if not.
	 */
	public boolean supportsSetCharacterStream() {
		return databaseInterface.supportsSetCharacterStream();
	}

	/**
	 * Get the maximum length of a text field for this database connection. This
	 * includes optional CLOB, Memo and Text fields. (the maximum!)得到的最大长度的文本字段这个数据库连接。这包括可选的CLOB ，备忘和文本字段。 （最高！ ）
	 * 
	 * @return The maximum text field length for this database type. (mostly
	 *         CLOB_LENGTH)
	 */
	public int getMaxTextFieldLength() {
		return databaseInterface.getMaxTextFieldLength();
	}

	public final static int getDatabaseType(String dbTypeDesc) {
		// Backward compatibility...
		if (dbTypeDesc.equalsIgnoreCase("ODBC-ACCESS"))
			return TYPE_DATABASE_ACCESS;

		try {
			DatabaseInterface di = getDatabaseInterface(dbTypeDesc);
			return di.getDatabaseType();
		} catch (KettleDatabaseException kde) {
			return TYPE_DATABASE_NONE;
		}
	}

	/**
	 * Get a string representing the unqiue database type code
	 * 
	 * @param dbtype
	 *            the database type to get the code of
	 * @return The database type code
	 * @deprecated please use getDatabaseTypeCode()
	 */
	public final static String getDBTypeDesc(int dbtype) {
		return getDatabaseTypeCode(dbtype);
	}

	/**
	 * Get a string representing the unqiue database type code取得一个字符串代表unqiue数据库类型codefields 。 （最高！ ）
	 * 
	 * @param dbtype
	 *            the database type to get the code of
	 * @return The database type code
	 */
	public final static String getDatabaseTypeCode(int dbtype) {
		// Find the DatabaseInterface for this type...
		DatabaseInterface[] di = getDatabaseInterfaces();

		for (int i = 0; i < di.length; i++) {
			if (di[i].getDatabaseType() == dbtype) {
				return di[i].getDatabaseTypeDesc();
			}
		}

		return null;
	}

	/**
	 * Get a description of the database type获取数据库类型的描述
	 * 
	 * @param dbtype
	 *            the database type to get the description for
	 * @return The database type description
	 */
	public final static String getDatabaseTypeDesc(int dbtype) {
		// Find the DatabaseInterface for this type...
		DatabaseInterface[] di = getDatabaseInterfaces();

		for (int i = 0; i < di.length; i++) {
			if (di[i].getDatabaseType() == dbtype)
				return di[i].getDatabaseTypeDescLong();
		}

		return null;
	}

	public final static int getAccessType(String dbaccess) {
		int i;

		if (dbaccess == null)
			return TYPE_ACCESS_NATIVE;

		for (i = 0; i < dbAccessTypeCode.length; i++) {
			if (dbAccessTypeCode[i].equalsIgnoreCase(dbaccess)) {
				return i;
			}
		}
		for (i = 0; i < dbAccessTypeDesc.length; i++) {
			if (dbAccessTypeDesc[i].equalsIgnoreCase(dbaccess)) {
				return i;
			}
		}

		return TYPE_ACCESS_NATIVE;
	}

	public final static String getAccessTypeDesc(int dbaccess) {
		if (dbaccess < 0)
			return null;
		if (dbaccess > dbAccessTypeCode.length)
			return null;

		return dbAccessTypeCode[dbaccess];
	}

	public final static String getAccessTypeDescLong(int dbaccess) {
		if (dbaccess < 0)
			return null;
		if (dbaccess > dbAccessTypeDesc.length)
			return null;

		return dbAccessTypeDesc[dbaccess];
	}

	public final static String[] getDBTypeDescLongList() {
		DatabaseInterface[] di = getDatabaseInterfaces();

		String[] retval = new String[di.length];
		for (int i = 0; i < di.length; i++) {
			retval[i] = di[i].getDatabaseTypeDescLong();
		}

		return retval;
	}

	public final static String[] getDBTypeDescList() {
		DatabaseInterface[] di = getDatabaseInterfaces();

		String[] retval = new String[di.length];
		for (int i = 0; i < di.length; i++) {
			retval[i] = di[i].getDatabaseTypeDesc();
		}

		return retval;
	}

	public static final DatabaseInterface[] getDatabaseInterfaces() {
		if (allDatabaseInterfaces != null)
			return allDatabaseInterfaces;

		Class<?> ic[] = DatabaseInterface.implementingClasses;
		allDatabaseInterfaces = new DatabaseInterface[ic.length];
		for (int i = 0; i < ic.length; i++) {
			try {
				Class.forName(ic[i].getName());
				allDatabaseInterfaces[i] = (DatabaseInterface) ic[i]
						.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Error creating class for : "
						+ ic[i].getName(), e);
			}
		}
		return allDatabaseInterfaces;
	}

	public final static int[] getAccessTypeList(String dbTypeDesc) {
		try {
			DatabaseInterface di = findDatabaseInterface(dbTypeDesc);
			return di.getAccessTypeList();
		} catch (KettleDatabaseException kde) {
			return null;
		}
	}

	public static final int getPortForDBType(String strtype, String straccess) {
		try {
			DatabaseInterface di = getDatabaseInterface(strtype);
			di.setAccessType(getAccessType(straccess));
			return di.getDefaultDatabasePort();
		} catch (KettleDatabaseException kde) {
			return -1;
		}
	}

	public int getDefaultDatabasePort() {
		return databaseInterface.getDefaultDatabasePort();
	}

	public int getNotFoundTK(boolean use_autoinc) {
		return databaseInterface.getNotFoundTK(use_autoinc);
	}

	public String getDriverClass() {
		return environmentSubstitute(databaseInterface.getDriverClass());
	}

	public String stripCR(String sbsql) {
		return stripCR(new StringBuffer(sbsql));
	}

	public String stripCR(StringBuffer sbsql) {
		// DB2 Can't handle \n in SQL Statements...
		if (getDatabaseType() == DatabaseMeta.TYPE_DATABASE_DB2
				|| getDatabaseType() == DatabaseMeta.TYPE_DATABASE_CACHE
				|| getDatabaseType() == DatabaseMeta.TYPE_DATABASE_UNIVERSE) {
			// Remove CR's
			for (int i = sbsql.length() - 1; i >= 0; i--) {
				if (sbsql.charAt(i) == '\n' || sbsql.charAt(i) == '\r')
					sbsql.setCharAt(i, ' ');
			}
		}

		return sbsql.toString();
	}

	public String getSeqNextvalSQL(String sequenceName) {
		return databaseInterface.getSQLNextSequenceValue(sequenceName);
	}

	public String getSQLCurrentSequenceValue(String sequenceName) {
		return databaseInterface.getSQLCurrentSequenceValue(sequenceName);
	}

	public boolean isFetchSizeSupported() {
		return databaseInterface.isFetchSizeSupported();
	}

	/**
	 * Indicates the need to insert a placeholder (0) for auto increment fields.表明需要插入的占位符（ 0 ）自动增量空间。
	 * 
	 * @return true if we need a placeholder for auto increment fields in insert
	 *         statements.
	 */
	public boolean needsPlaceHolder() {
		return databaseInterface.needsPlaceHolder();
	}

	public String getFunctionSum() {
		return databaseInterface.getFunctionSum();
	}

	public String getFunctionAverage() {
		return databaseInterface.getFunctionAverage();
	}

	public String getFunctionMaximum() {
		return databaseInterface.getFunctionMaximum();
	}

	public String getFunctionMinimum() {
		return databaseInterface.getFunctionMinimum();
	}

	public String getFunctionCount() {
		return databaseInterface.getFunctionCount();
	}

	/**
	 * Check the database connection parameters and give back an array of
	 * remarks检查数据库连接参数并返回一个数组的备注
	 * 
	 * @return an array of remarks Strings
	 */
	public String[] checkParameters() {
		ArrayList<String> remarks = new ArrayList<String>();

		if (getDatabaseType() == TYPE_DATABASE_NONE) {
			remarks.add("没有选择数据库类型");
		}

		if (getName() == null || getName().length() == 0) {
			remarks.add("请指定一个数据库连接的名称");
		}

		if (!isPartitioned() && getDatabaseType() != TYPE_DATABASE_SAPR3
				&& getDatabaseType() != TYPE_DATABASE_GENERIC) {
			if (getDatabaseName() == null || getDatabaseName().length() == 0) {
				remarks.add("请确定数据库的名字");
			}
		}

		return remarks.toArray(new String[remarks.size()]);
	}

	/**
	 * Calculate the schema-table combination, usually this is the schema and
	 * table separated with a dot. (schema.table)计算架构表的组合，这是通常的架构和表分离的点。
	 * 
	 * @param schemaName
	 *            the schema-name or null if no schema is used.
	 * @param tableName
	 *            the table name
	 * @return the schemaname-tablename combination
	 */
	public String getSchemaTableCombination(String schemaName, String tableName) {
		if (Const.isEmpty(schemaName)) {
			if (Const.isEmpty(getPreferredSchemaName())) {
				return tableName; // no need to look further
			} else {
				return databaseInterface.getSchemaTableCombination(
						getPreferredSchemaName(), tableName);
			}
		} else {
			return databaseInterface.getSchemaTableCombination(schemaName,
					tableName);
		}
	}

	public boolean isClob(ValueMetaInterface v) {
		boolean retval = true;

		if (v == null || v.getLength() < DatabaseMeta.CLOB_LENGTH) {
			retval = false;
		} else {
			return true;
		}
		return retval;
	}

	public String getFieldDefinition(ValueMetaInterface v, String tk,
			String pk, boolean use_autoinc) {
		return getFieldDefinition(v, tk, pk, use_autoinc, true, true);
	}

	public String getFieldDefinition(ValueMetaInterface v, String tk,
			String pk, boolean use_autoinc, boolean add_fieldname,
			boolean add_cr) {
		return databaseInterface.getFieldDefinition(v, tk, pk, use_autoinc,
				add_fieldname, add_cr);
	}

	public String getLimitClause(int nrRows) {
		return databaseInterface.getLimitClause(nrRows);
	}

	/**
	 * @param tableName
	 *            The table or schema-table combination. We expect this to be
	 *            quoted properly already!返回表或视图表的组合
	 * @return the SQL for to get the fields of this table.
	 */
	public String getSQLQueryFields(String tableName) {
		return databaseInterface.getSQLQueryFields(tableName);
	}

	public String getAddColumnStatement(String tablename, ValueMetaInterface v,
			String tk, boolean use_autoinc, String pk, boolean semicolon) {
		String retval = databaseInterface.getAddColumnStatement(tablename, v,
				tk, use_autoinc, pk, semicolon);
		retval += Const.CR;
		if (semicolon)
			retval += ";" + Const.CR;
		return retval;
	}

	public String getDropColumnStatement(String tablename,
			ValueMetaInterface v, String tk, boolean use_autoinc, String pk,
			boolean semicolon) {
		String retval = databaseInterface.getDropColumnStatement(tablename, v,
				tk, use_autoinc, pk, semicolon);
		retval += Const.CR;
		if (semicolon)
			retval += ";" + Const.CR;
		return retval;
	}

	public String getModifyColumnStatement(String tablename,
			ValueMetaInterface v, String tk, boolean use_autoinc, String pk,
			boolean semicolon) {
		String retval = databaseInterface.getModifyColumnStatement(tablename,
				v, tk, use_autoinc, pk, semicolon);
		retval += Const.CR;
		if (semicolon)
			retval += ";" + Const.CR;

		return retval;
	}

	/**
	 * @return an array of reserved words for the database type...返回一个数组的保留字的数据库类型
	 */
	public String[] getReservedWords() {
		return databaseInterface.getReservedWords();
	}

	/**
	 * @return true if reserved words need to be double quoted ("password",
	 *         "select", ...)
	 */
	public boolean quoteReservedWords() {
		return databaseInterface.quoteReservedWords();
	}

	/**
	 * @return The start quote sequence, mostly just double quote, but sometimes [,
	 *         ...
	 */
	public String getStartQuote() {
		return databaseInterface.getStartQuote();
	}

	/**
	 * @return The end quote sequence, mostly just double quote, but sometimes ],
	 *         ...
	 */
	public String getEndQuote() {
		return databaseInterface.getEndQuote();
	}

	/**
	 * Returns a quoted field if this is needed: contains spaces, is a reserved
	 * word, ...
	 * 
	 * @param field
	 *            The fieldname to check for quoting
	 * @return The quoted field (if this is needed.
	 */
	public String quoteField(String field) {
		if (Const.isEmpty(field))
			return null;

		if (isForcingIdentifiersToLowerCase()) {
			field = field.toLowerCase();
		} else if (isForcingIdentifiersToUpperCase()) {
			field = field.toUpperCase();
		}

		// If the field already contains quotes, we don't touch it anymore, just
		// return the same string...
		if (field.indexOf(getStartQuote()) >= 0
				|| field.indexOf(getEndQuote()) >= 0) {
			return field;
		}

		if (isReservedWord(field) && quoteReservedWords()) {
			return handleCase(getStartQuote() + field + getEndQuote());
		} else {
			if (databaseInterface.isQuoteAllFields() || hasSpacesInField(field)
					|| hasSpecialCharInField(field) || hasDotInField(field)) {
				return getStartQuote() + field + getEndQuote();
			} else {
				return field;
			}
		}
	}

	private String handleCase(String field) {
		if (databaseInterface.isDefaultingToUppercase()) {
			return field.toUpperCase();
		} else {
			return field.toLowerCase();
		}
	}

	/**
	 * Determines whether or not this field is in need of quoting:<br> - When
	 * the fieldname contains spaces<br> - When the fieldname is a reserved
	 * word<br>
	 * 
	 * @param fieldname
	 *            the fieldname to check if there is a need for quoting
	 * @return true if the fieldname needs to be quoted.
	 */
	public boolean isInNeedOfQuoting(String fieldname) {
		return isReservedWord(fieldname) || hasSpacesInField(fieldname);
	}

	/**
	 * Returns true if the string specified is a reserved word on this database
	 * type.返回true如果字符串指定的保留字在此数据库类型
	 * 
	 * @param word
	 *            The word to check
	 * @return true if word is a reserved word on this database.
	 */
	public boolean isReservedWord(String word) {
		String reserved[] = getReservedWords();
		if (Const.indexOfString(word, reserved) >= 0)
			return true;
		return false;
	}

	/**
	 * Detects if a field has spaces in the name. We need to quote the field in
	 * that case.
	 * 
	 * @param fieldname
	 *            The fieldname to check for spaces
	 * @return true if the fieldname contains spaces
	 */
	public boolean hasSpacesInField(String fieldname) {
		if (fieldname == null)
			return false;
		if (fieldname.indexOf(' ') >= 0)
			return true;
		return false;
	}

	/**
	 * Detects if a field has spaces in the name. We need to quote the field in
	 * that case.
	 * 
	 * @param fieldname
	 *            The fieldname to check for spaces
	 * @return true if the fieldname contains spaces
	 */
	public boolean hasSpecialCharInField(String fieldname) {
		if (fieldname == null)
			return false;
		if (fieldname.indexOf('/') >= 0)
			return true;
		if (fieldname.indexOf('-') >= 0)
			return true;
		if (fieldname.indexOf('+') >= 0)
			return true;
		if (fieldname.indexOf(',') >= 0)
			return true;
		if (fieldname.indexOf('*') >= 0)
			return true;
		if (fieldname.indexOf('(') >= 0)
			return true;
		if (fieldname.indexOf(')') >= 0)
			return true;
		if (fieldname.indexOf('{') >= 0)
			return true;
		if (fieldname.indexOf('}') >= 0)
			return true;
		if (fieldname.indexOf('[') >= 0)
			return true;
		if (fieldname.indexOf(']') >= 0)
			return true;
		if (fieldname.indexOf('%') >= 0)
			return true;
		if (fieldname.indexOf('@') >= 0)
			return true;
		return false;
	}

	public boolean hasDotInField(String fieldname) {
		if (fieldname == null)
			return false;
		if (fieldname.indexOf('.') >= 0)
			return true;
		return false;
	}

	/**
	 * Checks the fields specified for reserved words and quotes them.检查指定的领域保留字和引用他们
	 * 
	 * @param fields
	 *            the list of fields to check
	 * @return true if one or more values have a name that is a reserved word on
	 *         this database type.
	 */
	public boolean replaceReservedWords(RowMetaInterface fields) {
		boolean hasReservedWords = false;
		for (int i = 0; i < fields.size(); i++) {
			ValueMetaInterface v = fields.getValueMeta(i);
			if (isReservedWord(v.getName())) {
				hasReservedWords = true;
				v.setName(quoteField(v.getName()));
			}
		}
		return hasReservedWords;
	}

	/**
	 * Checks the fields specified for reserved words 检查指定的领域保留字
	 * 
	 * @param fields
	 *            the list of fields to check
	 * @return The nr of reserved words for this database.
	 */
	public int getNrReservedWords(RowMetaInterface fields) {
		int nrReservedWords = 0;
		for (int i = 0; i < fields.size(); i++) {
			ValueMetaInterface v = fields.getValueMeta(i);
			if (isReservedWord(v.getName()))
				nrReservedWords++;
		}
		return nrReservedWords;
	}

	/**
	 * @return a list of types to get the available tables 返回类型的列表获得可用表
	 */
	public String[] getTableTypes() {
		return databaseInterface.getTableTypes();
	}

	/**
	 * @return a list of types to get the available views 返回可使用视图的列表
	 */
	public String[] getViewTypes() {
		return databaseInterface.getViewTypes();
	}

	/**
	 * @return a list of types to get the available synonyms 返回可使用的别名的列表
	 */
	public String[] getSynonymTypes() {
		return databaseInterface.getSynonymTypes();
	}

	/**
	 * @return true if we need to supply the schema-name to getTables in order
	 *         to get a correct list of items.如果我们需要供应模式名称getTables为了得到正确的项目清单
	 */
	public boolean useSchemaNameForTableList() {
		return databaseInterface.useSchemaNameForTableList();
	}

	/**
	 * @return true if the database supports views如果数据库支持视图返回真
	 */
	public boolean supportsViews() {
		return databaseInterface.supportsViews();
	}

	/**
	 * @return true if the database supports synonyms如果数据库支持列表返回真
	 */
	public boolean supportsSynonyms() {
		return databaseInterface.supportsSynonyms();
	}

	/**
	 * 
	 * @return The SQL on this database to get a list of stored procedures.在SQL数据库上获得这个列表的存储过程
	 */
	public String getSQLListOfProcedures() {
		return databaseInterface.getSQLListOfProcedures();
	}

	/**
	 * @param tableName
	 *            The tablename to be truncated
	 * @return The SQL statement to remove all rows from the specified
	 *         statement, if possible without using transactionsSQL语句删除所有行从指定
 声明，如果可能的话，而不使用交换
	 */
	public String getTruncateTableStatement(String schema, String tableName) {
		return databaseInterface
				.getTruncateTableStatement(getQuotedSchemaTableCombination(
						schema, tableName));
	}

	/**
	 * @return true if the database rounds floating point numbers to the right
	 *         precision. For example if the target field is number(7,2) the
	 *         value 12.399999999 is converted into 12.40
	 */
	public boolean supportsFloatRoundingOnUpdate() {
		return databaseInterface.supportsFloatRoundingOnUpdate();
	}

	/**
	 * @param tableNames
	 *            The names of the tables to lock
	 * @return The SQL commands to lock database tables for write purposes. null
	 *         is returned in case locking is not supported on the target
	 *         database.
	 */
	public String getSQLLockTables(String tableNames[]) {
		return databaseInterface.getSQLLockTables(tableNames);
	}

	/**
	 * @param tableNames
	 *            The names of the tables to unlock
	 * @return The SQL commands to unlock databases tables. null is returned in
	 *         case locking is not supported on the target database.
	 */
	public String getSQLUnlockTables(String tableNames[]) {
		return databaseInterface.getSQLUnlockTables(tableNames);
	}

	/**
	 * @return a feature list for the chosen database type.返回功能清单选择数据库类型
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<RowMetaAndData> getFeatureSummary() {
		List<RowMetaAndData> list = new ArrayList<RowMetaAndData>();
		RowMetaAndData r = null;
		final String par = "Parameter";
		final String val = "Value";

		ValueMetaInterface testValue = new ValueMeta("FIELD",
				ValueMetaInterface.TYPE_STRING);
		testValue.setLength(30);

		if (databaseInterface != null) {
			// Type of database
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING, "Database type");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					getDatabaseTypeDesc());
			list.add(r);
			// Type of access
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING, "Access type");
			r
					.addValue(val, ValueMetaInterface.TYPE_STRING,
							getAccessTypeDesc());
			list.add(r);
			// Name of database
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING, "Database name");
			r.addValue(val, ValueMetaInterface.TYPE_STRING, getDatabaseName());
			list.add(r);
			// server host name
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING, "Server hostname");
			r.addValue(val, ValueMetaInterface.TYPE_STRING, getHostname());
			list.add(r);
			// Port number
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING, "Service port");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					getDatabasePortNumberString());
			list.add(r);
			// Username
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING, "Username");
			r.addValue(val, ValueMetaInterface.TYPE_STRING, getUsername());
			list.add(r);
			// Informix server
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"Informix server name");
			r.addValue(val, ValueMetaInterface.TYPE_STRING, getServername());
			list.add(r);
			// Other properties...
			Enumeration keys = getAttributes().keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				String value = getAttributes().getProperty(key);
				r = new RowMetaAndData();
				r.addValue(par, ValueMetaInterface.TYPE_STRING,
						"Extra attribute [" + key + "]");
				r.addValue(val, ValueMetaInterface.TYPE_STRING, value);
				list.add(r);
			}

			// driver class
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING, "Driver class");
			r.addValue(val, ValueMetaInterface.TYPE_STRING, getDriverClass());
			list.add(r);
			// URL
			String pwd = getPassword();
			setPassword("password"); // Don't give away the password in the
			// URL!
			String url = "";
			try {
				url = getURL();
			} catch (KettleDatabaseException e) {
			}
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING, "URL");
			r.addValue(val, ValueMetaInterface.TYPE_STRING, url);
			list.add(r);
			setPassword(pwd);
			// SQL: Next sequence value
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"SQL: next sequence value");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					getSeqNextvalSQL("SEQUENCE"));
			list.add(r);
			// is set fetch size supported
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"supported: set fetch size");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					isFetchSizeSupported() ? "Y" : "N");
			list.add(r);
			// needs place holder for auto increment
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"auto increment field needs placeholder");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					needsPlaceHolder() ? "Y" : "N");
			list.add(r);
			// Sum function
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"SUM aggregate function");
			r.addValue(val, ValueMetaInterface.TYPE_STRING, getFunctionSum());
			list.add(r);
			// Avg function
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"AVG aggregate function");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					getFunctionAverage());
			list.add(r);
			// Minimum function
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"MIN aggregate function");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					getFunctionMinimum());
			list.add(r);
			// Maximum function
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"MAX aggregate function");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					getFunctionMaximum());
			list.add(r);
			// Count function
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"COUNT aggregate function");
			r.addValue(val, ValueMetaInterface.TYPE_STRING, getFunctionCount());
			list.add(r);
			// Schema-table combination
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"Schema / Table combination");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					getSchemaTableCombination("SCHEMA", "TABLE"));
			list.add(r);
			// Limit clause
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"LIMIT clause for 100 rows");
			r
					.addValue(val, ValueMetaInterface.TYPE_STRING,
							getLimitClause(100));
			list.add(r);
			// add column statement
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"Add column statement");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					getAddColumnStatement("TABLE", testValue, null, false,
							null, false));
			list.add(r);
			// drop column statement
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"Drop column statement");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					getDropColumnStatement("TABLE", testValue, null, false,
							null, false));
			list.add(r);
			// Modify column statement
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"Modify column statement");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					getModifyColumnStatement("TABLE", testValue, null, false,
							null, false));
			list.add(r);

			// List of reserved words
			String reserved = "";
			if (getReservedWords() != null)
				for (int i = 0; i < getReservedWords().length; i++)
					reserved += (i > 0 ? ", " : "") + getReservedWords()[i];
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"List of reserved words");
			r.addValue(val, ValueMetaInterface.TYPE_STRING, reserved);
			list.add(r);

			// Quote reserved words?
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"Quote reserved words?");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					quoteReservedWords() ? "Y" : "N");
			list.add(r);
			// Start Quote
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"Start quote for reserved words");
			r.addValue(val, ValueMetaInterface.TYPE_STRING, getStartQuote());
			list.add(r);
			// End Quote
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"End quote for reserved words");
			r.addValue(val, ValueMetaInterface.TYPE_STRING, getEndQuote());
			list.add(r);

			// List of table types
			String types = "";
			String slist[] = getTableTypes();
			if (slist != null)
				for (int i = 0; i < slist.length; i++)
					types += (i > 0 ? ", " : "") + slist[i];
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"List of JDBC table types");
			r.addValue(val, ValueMetaInterface.TYPE_STRING, types);
			list.add(r);

			// List of view types
			types = "";
			slist = getViewTypes();
			if (slist != null)
				for (int i = 0; i < slist.length; i++)
					types += (i > 0 ? ", " : "") + slist[i];
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"List of JDBC view types");
			r.addValue(val, ValueMetaInterface.TYPE_STRING, types);
			list.add(r);

			// List of synonym types
			types = "";
			slist = getSynonymTypes();
			if (slist != null)
				for (int i = 0; i < slist.length; i++)
					types += (i > 0 ? ", " : "") + slist[i];
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"List of JDBC synonym types");
			r.addValue(val, ValueMetaInterface.TYPE_STRING, types);
			list.add(r);

			// Use schema-name to get list of tables?
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"use schema name to get table list?");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					useSchemaNameForTableList() ? "Y" : "N");
			list.add(r);
			// supports view?
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING, "supports views?");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					supportsViews() ? "Y" : "N");
			list.add(r);
			// supports synonyms?
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"supports synonyms?");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					supportsSynonyms() ? "Y" : "N");
			list.add(r);
			// SQL: get list of procedures?
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"SQL: list of procedures");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					getSQLListOfProcedures());
			list.add(r);
			// SQL: get truncate table statement?
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"SQL: truncate table");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					getTruncateTableStatement(null, "TABLE"));
			list.add(r);
			// supports float rounding on update?
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"supports floating point rounding on update/insert");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					supportsFloatRoundingOnUpdate() ? "Y" : "N");
			list.add(r);
			// supports time stamp to date conversion
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"supports timestamp-date conversion");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					supportsTimeStampToDateConversion() ? "Y" : "N");
			list.add(r);
			// supports batch updates
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"supports batch updates");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					supportsBatchUpdates() ? "Y" : "N");
			list.add(r);
			// supports boolean values
			r = new RowMetaAndData();
			r.addValue(par, ValueMetaInterface.TYPE_STRING,
					"supports boolean data type");
			r.addValue(val, ValueMetaInterface.TYPE_STRING,
					supportsBooleanDataType() ? "Y" : "N");
			list.add(r);
		}

		return list;
	}

	/**
	 * @return true if the database result sets support getTimeStamp() to
	 *         retrieve date-time. (Date)
	 */
	public boolean supportsTimeStampToDateConversion() {
		return databaseInterface.supportsTimeStampToDateConversion();
	}

	/**
	 * @return true if the database JDBC driver supports batch updates For
	 *         example Interbase doesn't support this!
	 */
	public boolean supportsBatchUpdates() {
		return databaseInterface.supportsBatchUpdates();
	}

	/**
	 * @return true if the database supports a boolean, bit, logical, ...
	 *         datatype
	 */
	public boolean supportsBooleanDataType() {
		return databaseInterface.supportsBooleanDataType();
	}

	/**
	 * 
	 * @param b
	 *            Set to true if the database supports a boolean, bit, logical,
	 *            ... datatype
	 */
	public void setSupportsBooleanDataType(boolean b) {
		databaseInterface.setSupportsBooleanDataType(b);
	}

	/**
	 * Changes the names of the fields to their quoted equivalent if this is
	 * needed
	 * 
	 * @param fields
	 *            The row of fields to change
	 */
	public void quoteReservedWords(RowMetaInterface fields) {
		for (int i = 0; i < fields.size(); i++) {
			ValueMetaInterface v = fields.getValueMeta(i);
			v.setName(quoteField(v.getName()));
		}
	}

	/**
	 * @return a map of all the extra URL options you want to set.
	 */
	public Map<String, String> getExtraOptions() {
		return databaseInterface.getExtraOptions();
	}

	/**
	 * @return true if the database supports connection options in the URL,
	 *         false if they are put in a Properties object.
	 */
	public boolean supportsOptionsInURL() {
		return databaseInterface.supportsOptionsInURL();
	}

	/**
	 * @return extra help text on the supported options on the selected database
	 *         platform.
	 */
	public String getExtraOptionsHelpText() {
		return databaseInterface.getExtraOptionsHelpText();
	}

	/**
	 * @return true if the database JDBC driver supports getBlob on the
	 *         resultset. If not we must use getBytes() to get the data.
	 */
	public boolean supportsGetBlob() {
		return databaseInterface.supportsGetBlob();
	}

	/**
	 * @return The SQL to execute right after connecting
	 */
	public String getConnectSQL() {
		return databaseInterface.getConnectSQL();
	}

	/**
	 * @param sql
	 *            The SQL to execute right after connecting
	 */
	public void setConnectSQL(String sql) {
		databaseInterface.setConnectSQL(sql);
	}

	/**
	 * @return true if the database supports setting the maximum number of
	 *         return rows in a resultset.
	 */
	public boolean supportsSetMaxRows() {
		return databaseInterface.supportsSetMaxRows();
	}

	/**
	 * Verify the name of the database and if required, change it if it already
	 * exists in the list of databases.
	 * 
	 * @param databases
	 *            the databases to check against.
	 * @param oldname
	 *            the old name of the database
	 * @return the new name of the database connection
	 */
	public String verifyAndModifyDatabaseName(List<DatabaseMeta> databases,
			String oldname) {
		String name = getName();
		if (name.equalsIgnoreCase(oldname))
			return name; // nothing to see here: move along!

		int nr = 2;
		while (DatabaseMeta.findDatabase(databases, getName()) != null) {
			setName(name + " " + nr);
			nr++;
		}
		return getName();
	}

	/**
	 * @return true if we want to use a database connection pool
	 */
	public boolean isUsingConnectionPool() {
		return databaseInterface.isUsingConnectionPool();
	}

	/**
	 * @param usePool
	 *            true if we want to use a database connection pool
	 */
	public void setUsingConnectionPool(boolean usePool) {
		databaseInterface.setUsingConnectionPool(usePool);
	}

	/**
	 * @return the maximum pool size
	 */
	public int getMaximumPoolSize() {
		return databaseInterface.getMaximumPoolSize();
	}

	/**
	 * @param maximumPoolSize
	 *            the maximum pool size
	 */
	public void setMaximumPoolSize(int maximumPoolSize) {
		databaseInterface.setMaximumPoolSize(maximumPoolSize);
	}

	/**
	 * @return the initial pool size
	 */
	public int getInitialPoolSize() {
		return databaseInterface.getInitialPoolSize();
	}

	/**
	 * @param initalPoolSize
	 *            the initial pool size
	 */
	public void setInitialPoolSize(int initalPoolSize) {
		databaseInterface.setInitialPoolSize(initalPoolSize);
	}

	/**
	 * @return true if the connection contains partitioning information
	 */
	public boolean isPartitioned() {
		return databaseInterface.isPartitioned();
	}

	/**
	 * @param partitioned
	 *            true if the connection is set to contain partitioning
	 *            information
	 */
	public void setPartitioned(boolean partitioned) {
		databaseInterface.setPartitioned(partitioned);
	}

	/**
	 * @return the available partition/host/databases/port combinations in the
	 *         cluster
	 */
	public PartitionDatabaseMeta[] getPartitioningInformation() {
		if (!isPartitioned())
			return new PartitionDatabaseMeta[] {};
		return databaseInterface.getPartitioningInformation();
	}

	/**
	 * @param partitionInfo
	 *            the available partition/host/databases/port combinations in
	 *            the cluster
	 */
	public void setPartitioningInformation(PartitionDatabaseMeta[] partitionInfo) {
		databaseInterface.setPartitioningInformation(partitionInfo);
	}

	/**
	 * Finds the partition metadata for the given partition iD
	 * 
	 * @param partitionId
	 *            The partition ID to look for
	 * @return the partition database metadata or null if nothing was found.
	 */
	public PartitionDatabaseMeta getPartitionMeta(String partitionId) {
		PartitionDatabaseMeta[] partitionInfo = getPartitioningInformation();
		for (int i = 0; i < partitionInfo.length; i++) {
			if (partitionInfo[i].getPartitionId().equals(partitionId))
				return partitionInfo[i];
		}
		return null;
	}

	public Properties getConnectionPoolingProperties() {
		return databaseInterface.getConnectionPoolingProperties();
	}

	public void setConnectionPoolingProperties(Properties properties) {
		databaseInterface.setConnectionPoolingProperties(properties);
	}

	public String getSQLTableExists(String tablename) {
		return databaseInterface.getSQLTableExists(tablename);
	}

	public String getSQLColumnExists(String columnname, String tablename) {
		return databaseInterface.getSQLColumnExists(columnname, tablename);
	}

	public boolean needsToLockAllTables() {
		return databaseInterface.needsToLockAllTables();
	}

	public String getQuotedSchemaTableCombination(String schemaName,
			String tableName) {
		if (Const.isEmpty(schemaName)) {
			return getSchemaTableCombination(
					quoteField(getPreferredSchemaName()), quoteField(tableName));
		} else {
			return getSchemaTableCombination(quoteField(schemaName),
					quoteField(tableName));
		}
	}

	/**
	 * @return true if the database is streaming results (normally this is an
	 *         option just for MySQL).
	 */
	public boolean isStreamingResults() {
		return databaseInterface.isStreamingResults();
	}

	/**
	 * @param useStreaming
	 *            true if we want the database to stream results (normally this
	 *            is an option just for MySQL).
	 */
	public void setStreamingResults(boolean useStreaming) {
		databaseInterface.setStreamingResults(useStreaming);
	}

	/**
	 * @return true if all fields should always be quoted in db
	 */
	public boolean isQuoteAllFields() {
		return databaseInterface.isQuoteAllFields();
	}

	/**
	 * @param quoteAllFields
	 *            true if all fields in DB should be quoted.
	 */
	public void setQuoteAllFields(boolean quoteAllFields) {
		databaseInterface.setQuoteAllFields(quoteAllFields);
	}

	/**
	 * @return true if all identifiers should be forced to lower case
	 */
	public boolean isForcingIdentifiersToLowerCase() {
		return databaseInterface.isForcingIdentifiersToLowerCase();
	}

	/**
	 * @param forceLowerCase
	 *            true if all identifiers should be forced to lower case
	 */
	public void setForcingIdentifiersToLowerCase(boolean forceLowerCase) {
		databaseInterface.setForcingIdentifiersToLowerCase(forceLowerCase);
	}

	/**
	 * @return true if all identifiers should be forced to upper case
	 */
	public boolean isForcingIdentifiersToUpperCase() {
		return databaseInterface.isForcingIdentifiersToUpperCase();
	}

	/**
	 * @param forceLowerCase
	 *            true if all identifiers should be forced to upper case
	 */
	public void setForcingIdentifiersToUpperCase(boolean forceUpperCase) {
		databaseInterface.setForcingIdentifiersToUpperCase(forceUpperCase);
	}

	/**
	 * Find a database with a certain name in an arraylist of databases.
	 * 
	 * @param databases
	 *            The ArrayList of databases
	 * @param dbname
	 *            The name of the database connection
	 * @return The database object if one was found, null otherwise.
	 */
	public static final DatabaseMeta findDatabase(
			List<? extends SharedObjectInterface> databases, String dbname) {
		if (databases == null)
			return null;

		for (int i = 0; i < databases.size(); i++) {
			DatabaseMeta ci = (DatabaseMeta) databases.get(i);
			if (ci.getName().equalsIgnoreCase(dbname))
				return ci;
		}
		return null;
	}

	/**
	 * Find a database with a certain name in an ArrayList of databases.
	 * 
	 * @param databases
	 *            The ArrayList of databases
	 * @param dbname
	 *            The name of the database connection
	 * @param exclude
	 *            the name of the database connection to exclude from the search
	 * @return The database object if one was found, null otherwise.
	 */
	public static final DatabaseMeta findDatabase(List<DatabaseMeta> databases,
			String dbname, String exclude) {
		if (databases == null)
			return null;

		for (int i = 0; i < databases.size(); i++) {
			DatabaseMeta ci = (DatabaseMeta) databases.get(i);
			if (ci.getName().equalsIgnoreCase(dbname))
				return ci;
		}
		return null;
	}

	/**
	 * Find a database with a certain ID in an arraylist of databases.
	 * 
	 * @param databases
	 *            The ArrayList of databases
	 * @param id
	 *            The id of the database connection
	 * @return The database object if one was found, null otherwise.
	 */
	public static final DatabaseMeta findDatabase(List<DatabaseMeta> databases,
			long id) {
		if (databases == null)
			return null;

		for (DatabaseMeta ci : databases) {
			if (ci.getID() == id) {
				return ci;
			}
		}
		return null;
	}

	public void copyVariablesFrom(VariableSpace space) {
		variables.copyVariablesFrom(space);
	}

	public String environmentSubstitute(String aString) {
		return variables.environmentSubstitute(aString);
	}

	public String[] environmentSubstitute(String aString[]) {
		return variables.environmentSubstitute(aString);
	}

	public VariableSpace getParentVariableSpace() {
		return variables.getParentVariableSpace();
	}

	public void setParentVariableSpace(VariableSpace parent) {
		variables.setParentVariableSpace(parent);
	}

	public String getVariable(String variableName, String defaultValue) {
		return variables.getVariable(variableName, defaultValue);
	}

	public String getVariable(String variableName) {
		return variables.getVariable(variableName);
	}

	public boolean getBooleanValueOfVariable(String variableName,
			boolean defaultValue) {
		if (!Const.isEmpty(variableName)) {
			String value = environmentSubstitute(variableName);
			if (!Const.isEmpty(value)) {
				return ValueMeta.convertStringToBoolean(value);
			}
		}
		return defaultValue;
	}

	public void initializeVariablesFrom(VariableSpace parent) {
		variables.initializeVariablesFrom(parent);
	}

	public String[] listVariables() {
		return variables.listVariables();
	}

	public void setVariable(String variableName, String variableValue) {
		variables.setVariable(variableName, variableValue);
	}

	public void shareVariablesWith(VariableSpace space) {
		variables = space;
	}

	public void injectVariables(Map<String, String> prop) {
		variables.injectVariables(prop);
	}

	/**
	 * @return the SQL Server instance
	 */
	public String getSQLServerInstance() {
		// This is also covered/persisted by JDBC option MS SQL Server /
		// instancename / <somevalue>
		// We want to return <somevalue>
		// --> MSSQL.instancename
		return (String) getExtraOptions().get("MSSQL.instance");
	}

	/**
	 * @param instanceName
	 *            the SQL Server instance
	 */
	public void setSQLServerInstance(String instanceName) {
		// This is also covered/persisted by JDBC option MS SQL Server /
		// instancename / <somevalue>
		// We want to return set <somevalue>
		// --> MSSQL.instancename
		addExtraOption("MSSQL", "instance", instanceName);
	}

	/**
	 * @return true if the Microsoft SQL server uses two decimals (..) to
	 *         separate schema and table (default==false).
	 */
	public boolean isUsingDoubleDecimalAsSchemaTableSeparator() {
		return databaseInterface.isUsingDoubleDecimalAsSchemaTableSeparator();
	}

	/**
	 * @param useStreaming
	 *            true if we want the database to stream results (normally this
	 *            is an option just for MySQL).
	 */
	public void setUsingDoubleDecimalAsSchemaTableSeparator(
			boolean useDoubleDecimalSeparator) {
		databaseInterface
				.setUsingDoubleDecimalAsSchemaTableSeparator(useDoubleDecimalSeparator);
	}

	/**
	 * @return true if this database needs a transaction to perform a query
	 *         (auto-commit turned off).
	 */
	public boolean isRequiringTransactionsOnQueries() {
		return databaseInterface.isRequiringTransactionsOnQueries();
	}

	public String testConnection() {

		StringBuffer report = new StringBuffer();

		// If the plug-in needs to provide connection information, we ask the
		// DatabaseInterface...
		//
		try {
			DatabaseFactoryInterface factory = getDatabaseFactory();
			return factory.getConnectionTestReport(this);
		} catch (ClassNotFoundException e) {
			report
					.append(
							Messages
									.getString("BaseDatabaseMeta.TestConnectionReportNotImplemented.Message"))
					.append(Const.CR); // $NON-NLS-1
			report
					.append(Messages.getString(
							"DatabaseMeta.report.ConnectionError", getName()) + e.toString() + Const.CR); //$NON-NLS-1$
			report.append(Const.getStackTracker(e) + Const.CR);
		} catch (Exception e) {
			report
					.append(Messages.getString(
							"DatabaseMeta.report.ConnectionError", getName()) + e.toString() + Const.CR); //$NON-NLS-1$
			report.append(Const.getStackTracker(e) + Const.CR);
		}
		return report.toString();
	}

	public DatabaseFactoryInterface getDatabaseFactory() throws Exception {
		Class<?> clazz = Class.forName(databaseInterface
				.getDatabaseFactoryName());
		return (DatabaseFactoryInterface) clazz.newInstance();
	}

	public String getPreferredSchemaName() {
		return databaseInterface.getPreferredSchemaName();
	}

	public void setPreferredSchemaName(String preferredSchemaName) {
		databaseInterface.setPreferredSchemaName(preferredSchemaName);
	}
}
