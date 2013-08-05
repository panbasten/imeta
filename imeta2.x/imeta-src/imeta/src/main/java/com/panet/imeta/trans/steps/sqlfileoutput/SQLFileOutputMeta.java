 /**********************************************************************
 **                                                                   **
 **               This code belongs to the KETTLE project.            **
 **                                                                   **
 ** Kettle, from version 2.2 on, is released into the public domain   **
 ** under the Lesser GNU Public License (LGPL).                       **
 **                                                                   **
 ** For more details, please read the document LICENSE.txt, included  **
 ** in this project                                                   **
 **                                                                   **
 ** http://www.kettle.be                                              **
 ** info@kettle.be                                                    **
 **                                                                   **
 **********************************************************************/

package com.panet.imeta.trans.steps.sqlfileoutput;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.core.CheckResult;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Counter;
import com.panet.imeta.core.SQLStatement;
import com.panet.imeta.core.database.Database;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.row.ValueMetaInterface;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.shared.SharedObjectInterface;
import com.panet.imeta.trans.DatabaseImpact;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepMeta;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;

/*
 * Created on 26-may-2007
 *
 */
 
public class SQLFileOutputMeta extends BaseStepMeta implements StepMetaInterface
{
	public static final String STEP_ATTRIBUTE_ID_CONNECTION = "id_connection";
	public static final String STEP_ATTRIBUTE_SCHEMA= "schema";
	public static final String STEP_ATTRIBUTE_TABLE = "table";
	public static final String STEP_ATTRIBUTE_TRUNCATE = "truncate"; 
	public static final String STEP_ATTRIBUTE_CREATE = "create";
	public static final String STEP_ATTRIBUTE_NCODING = "ncoding"; 
	public static final String STEP_ATTRIBUTE_DATEFORMAT = "dateformat";
	public static final String STEP_ATTRIBUTE_ADDTORESULT = "addtoresult";
	public static final String STEP_ATTRIBUTE_STARTNEWLINE = "startnewline";
	public static final String STEP_ATTRIBUTE_FILE_NAME = "file_name";
	public static final String STEP_ATTRIBUTE_FILE_EXTENTION = "file_extention";
	public static final String STEP_ATTRIBUTE_FILE_APPEND = "file_append";
	public static final String STEP_ATTRIBUTE_FILE_SPLIT = "file_split";
	public static final String STEP_ATTRIBUTE_FILE_ADD_STEPNR = "file_add_stepnr"; 
	public static final String STEP_ATTRIBUTE_FILE_ADD_PARTNR = "file_add_partnr";
	public static final String STEP_ATTRIBUTE_FILE_ADD_DATE = "file_add_date";
    public static final String STEP_ATTRIBUTE_FILE_ADD_TIME = "file_add_time";
    public static final String STEP_ATTRIBUTE_CREATE_PARENT_FOLDER = "create_parent_folder";
    public static final String STEP_ATTRIBUTE_DONOTOPENNEWFILEINIT = "DoNotOpenNewFileInit";
		
	private DatabaseMeta databaseMeta;
    private String       schemaName;
	private String       tablename;
	private boolean      truncateTable;
	
	private boolean		AddToResult;
	
	private boolean      createTable;
 
    /** The base name of the output file */
	private  String fileName;
	
	/** The file extention in case of a generated filename */
	private  String  extension;
	
	/** if this value is larger then 0, the text file is split up into parts of this number of lines */
    private  int    splitEvery;

	/** Flag to indicate the we want to append to the end of an existing file (if it exists) */
    private  boolean fileAppended;

	/** Flag: add the stepnr in the filename */
    private  boolean stepNrInFilename;
	
	/** Flag: add the partition number in the filename */
    private  boolean partNrInFilename;
	
	/** Flag: add the date in the filename */
    private  boolean dateInFilename;
	
	/** Flag: add the time in the filename */
    private  boolean timeInFilename;
    
    /** The encoding to use for reading: null or empty string means system default encoding */
    private String encoding;
    
    /** The date format */
    private String dateformat;
    
    /** Start New line for each statement */
    private boolean		StartNewLine;
    
    /** Flag: create parent folder if needed */
    private boolean createparentfolder;
    
    private boolean DoNotOpenNewFileInit;
    
	
	
	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
	throws KettleXMLException
{
		readData(stepnode, databases);
	}

	public Object clone()
	{
		
		SQLFileOutputMeta retval = (SQLFileOutputMeta)super.clone();
		
		return retval;
	}
	

	
	/**
     * @return Returns the database.
     */
    public DatabaseMeta getDatabaseMeta()
    {
        return databaseMeta;
    }
    
    /**
     * @param database The database to set.
     */
    public void setDatabaseMeta(DatabaseMeta database)
    {
        this.databaseMeta = database;
    }
    
    /**
     * @return Returns the extension.
     */
    public String getExtension()
    {
        return extension;
    }

    /**
     * @param extension The extension to set.
     */
    public void setExtension(String extension)
    {
        this.extension = extension;
    }
    
    /**
     * @return Returns the fileAppended.
     */
    public boolean isFileAppended()
    {
        return fileAppended;
    }
    
    /**
     * @param fileAppended The fileAppended to set.
     */
    public void setFileAppended(boolean fileAppended)
    {
        this.fileAppended = fileAppended;
    }
    
    /**
     * @return Returns the fileName.
     */
    public String getFileName()
    {
        return fileName;
    }

    
    /**
     * @return Returns the splitEvery.
     */
    public int getSplitEvery()
    {
        return splitEvery;
    }

    /**
     * @param splitEvery The splitEvery to set.
     */
    public void setSplitEvery(int splitEvery)
    {
        this.splitEvery = splitEvery;
    }

    /**
     * @return Returns the stepNrInFilename.
     */
    public boolean isStepNrInFilename()
    {
        return stepNrInFilename;
    }

    /**
     * @param stepNrInFilename The stepNrInFilename to set.
     */
    public void setStepNrInFilename(boolean stepNrInFilename)
    {
        this.stepNrInFilename = stepNrInFilename;
    }
    

    /**
     * @return Returns the timeInFilename.
     */
    public boolean isTimeInFilename()
    {
        return timeInFilename;
    }

    /**
     * @return Returns the dateInFilename.
     */
    public boolean isDateInFilename()
    {
        return dateInFilename;
    }

    /**
     * @param dateInFilename The dateInFilename to set.
     */
    public void setDateInFilename(boolean dateInFilename)
    {
        this.dateInFilename = dateInFilename;
    }
    
    /**
     * @param timeInFilename The timeInFilename to set.
     */
    public void setTimeInFilename(boolean timeInFilename)
    {
        this.timeInFilename = timeInFilename;
    }

    
    /**
     * @param fileName The fileName to set.
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }


    /**
     * @return The desired encoding of output file, null or empty if the default system encoding needs to be used.
     */
    public String getEncoding()
    {
        return encoding;
    }

    /**
     * @return The desired date format.
     */
    public String getDateFormat()
    {
        return dateformat;
    }
    
    /**
     * @param encoding The desired encoding of output file, null or empty if the default system encoding needs to be
     * used.
     */
    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }
    
    
    /**
     * @param dateFormat The desired date format of output field date used.
     */
    public void setDateFormat(String dateFormat)
    {
        this.dateformat = dateFormat;
    }
    
    
    
    /**
     * @return Returns the table name.
     */
    public String getTablename()
    {
        return tablename;
    }
    
    /**
     * @param tablename The table name to set.
     */
    public void setTablename(String tablename)
    {
        this.tablename = tablename;
    }
    
    /**
     * @return Returns the truncate table flag.
     */
    public boolean truncateTable()
    {
        return truncateTable;
    }
    
    
    /**
     * @return Returns the Add to result filesname flag.
     */
    public boolean AddToResult()
    {
        return AddToResult;
    }
    
    /**
     * @return Returns the Start new line flag.
     */
    public boolean StartNewLine()
    {
        return StartNewLine;
    }
    public boolean isDoNotOpenNewFileInit()
    {
        return DoNotOpenNewFileInit;
    }
    public void setDoNotOpenNewFileInit(boolean DoNotOpenNewFileInit)
    {
        this.DoNotOpenNewFileInit = DoNotOpenNewFileInit;
    }
    
    
 
    /**
     * @return Returns the create table flag.
     */
    public boolean createTable()
    {
        return createTable;
    }
    
    
    
    /**
     * @param truncateTable The truncate table flag to set.
     */
    public void setTruncateTable(boolean truncateTable)
    {
        this.truncateTable = truncateTable;
    }
    
    /**
     * @param AddToResult The Add file to result to set.
     */
    public void setAddToResult(boolean AddToResult)
    {
        this.AddToResult = AddToResult;
    }
    
    /**
     * @param StartNewLine The Start NEw Line to set.
     */
    public void setStartNewLine(boolean StartNewLine)
    {
        this.StartNewLine = StartNewLine;
    }
    
    /**
     * @param createTable The create table flag to set.
     */
    public void setCreateTable(boolean createTable)
    {
        this.createTable = createTable;
    }
    
    /**
     * @return Returns the create parent folder flag.
     */
    public boolean isCreateParentFolder()
    {
    	return createparentfolder;
    }
    
    /**
     * @param createparentfolder The create parent folder flag to set.
     */
    public void setCreateParentFolder(boolean createparentfolder)
    {
    	this.createparentfolder=createparentfolder;
    }
    
	public String[] getFiles(String fileName)
	{
		int copies=1;
		int splits=1;
		int parts=1;

		if (stepNrInFilename)
		{
			copies=3;
		}
		
		if (partNrInFilename)
		{
			parts=3;
		}
		
		if (splitEvery!=0)
		{
			splits=3;
		}
		
		int nr=copies*parts*splits;
		if (nr>1) nr++;
		
		String retval[]=new String[nr];
		
		int i=0;
		for (int copy=0;copy<copies;copy++)
		{
			for (int part=0;part<parts;part++)
			{
				for (int split=0;split<splits;split++)
				{
					retval[i]=buildFilename( fileName, copy, split);
					i++;
				}
			}
		}
		if (i<nr)
		{
			retval[i]="...";
		}
		
		return retval;
	}
	public String buildFilename(String fileName,int stepnr, int splitnr)
	{
		SimpleDateFormat daf     = new SimpleDateFormat();

		// Replace possible environment variables...
		String retval=fileName;
		

		Date now = new Date();
		
		if (dateInFilename)
		{
			daf.applyPattern("yyyMMdd");
			String d = daf.format(now);
			retval+="_"+d;
		}
		if (timeInFilename)
		{
			daf.applyPattern("HHmmss");
			String t = daf.format(now);
			retval+="_"+t;
		}
		if (stepNrInFilename)
		{
			retval+="_"+stepnr;
		}
		
		if (splitEvery>0)
		{
			retval+="_"+splitnr;
		}
		
		
		if (extension!=null && extension.length()!=0) 
		{
			retval+="."+extension;
		}
		
		return retval;
	}
    
    
	private void readData(Node stepnode, List<? extends SharedObjectInterface> databases) throws KettleXMLException
	{
		try
		{
		
			String con = XMLHandler.getTagValue(stepnode, "connection");
			databaseMeta      = DatabaseMeta.findDatabase(databases, con);
            schemaName    = XMLHandler.getTagValue(stepnode, "schema");
			tablename     = XMLHandler.getTagValue(stepnode, "table");
			truncateTable = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "truncate"));
			createTable = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "create"));
			encoding         = XMLHandler.getTagValue(stepnode, "encoding");
			dateformat         = XMLHandler.getTagValue(stepnode, "dateformat");
			AddToResult = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "AddToResult"));
			
			
			StartNewLine = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "StartNewLine"));
			
			fileName  = XMLHandler.getTagValue(stepnode, "file", "name");
			createparentfolder ="Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "create_parent_folder"));
			extension = XMLHandler.getTagValue(stepnode, "file", "extention");
			fileAppended    = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "append"));
			stepNrInFilename     = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "split"));
			partNrInFilename     = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "haspartno"));
			dateInFilename  = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "add_date"));
			timeInFilename  = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "add_time"));
			splitEvery=Const.toInt(XMLHandler.getTagValue(stepnode, "file", "splitevery"), 0);
			DoNotOpenNewFileInit    = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "DoNotOpenNewFileInit"));
	
        }
		catch(Exception e)
		{
			throw new KettleXMLException("Unable to load step info from XML", e);
		}
	}

	public void setDefault()
	{
		databaseMeta = null;
		tablename      = "";
		createparentfolder=false;
		DoNotOpenNewFileInit=false;
		extension="sql";
		
        
	}

	public String getXML()
	{
		StringBuffer retval=new StringBuffer();
		
		retval.append("    "+XMLHandler.addTagValue("connection",    databaseMeta==null?"":databaseMeta.getName()));
        retval.append("    "+XMLHandler.addTagValue("schema",        schemaName));
		retval.append("    "+XMLHandler.addTagValue("table",         tablename));
		retval.append("    "+XMLHandler.addTagValue("truncate",      truncateTable));
		retval.append("    "+XMLHandler.addTagValue("create",      createTable));	
		retval.append("    "+XMLHandler.addTagValue("encoding",  encoding));
		retval.append("    "+XMLHandler.addTagValue("dateformat",  dateformat));
		retval.append("    "+XMLHandler.addTagValue("addtoresult",      AddToResult));
		
		retval.append("    "+XMLHandler.addTagValue("startnewline",      StartNewLine));
		
		retval.append("    <file>"+Const.CR);
		retval.append("      "+XMLHandler.addTagValue("name",       fileName));
		retval.append("      "+XMLHandler.addTagValue("extention",  extension));
		retval.append("      "+XMLHandler.addTagValue("append",     fileAppended));
		retval.append("      "+XMLHandler.addTagValue("split",      stepNrInFilename));
		retval.append("      "+XMLHandler.addTagValue("haspartno",  partNrInFilename));
		retval.append("      "+XMLHandler.addTagValue("add_date",   dateInFilename));
		retval.append("      "+XMLHandler.addTagValue("add_time",   timeInFilename));
		retval.append("      "+XMLHandler.addTagValue("splitevery", splitEvery));
		retval.append("      "+XMLHandler.addTagValue("create_parent_folder",   createparentfolder));
		retval.append("      "+XMLHandler.addTagValue("DoNotOpenNewFileInit",     DoNotOpenNewFileInit));
		
		
		retval.append("      </file>"+Const.CR);
		
		
		return retval.toString();
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		databaseMeta  = DatabaseMeta.findDatabase(
				(List<DatabaseMeta>) databases, BaseStepMeta.parameterToLong(p
						.get(id + ".connection")));  
		schemaName = BaseStepMeta.parameterToString(p.get(id+ ".schemaName"));
		tablename = BaseStepMeta.parameterToString(p.get(id+ ".tablename"));
		truncateTable = BaseStepMeta.parameterToBoolean(p.get(id+ ".truncateTable"));
		createTable = BaseStepMeta.parameterToBoolean(p.get(id+ ".createTable"));
		encoding = BaseStepMeta.parameterToString(p.get(id+ ".encoding"));
		dateformat = BaseStepMeta.parameterToString(p.get(id+ ".dateformat"));
		AddToResult = BaseStepMeta.parameterToBoolean(p.get(id+ ".AddToResult"));
		StartNewLine = BaseStepMeta.parameterToBoolean(p.get(id+ ".StartNewLine"));
		fileName = BaseStepMeta.parameterToString(p.get(id+ ".fileName"));
		extension = BaseStepMeta.parameterToString(p.get(id+ ".extension"));
		fileAppended = BaseStepMeta.parameterToBoolean(p.get(id+ ".fileAppended"));
		splitEvery = BaseStepMeta.parameterToInt(p.get(id+ ".splitEvery"));
		stepNrInFilename = BaseStepMeta.parameterToBoolean(p.get(id+ ".stepNrInFilename"));
		partNrInFilename = BaseStepMeta.parameterToBoolean(p.get(id+ ".partNrInFilename"));
		dateInFilename = BaseStepMeta.parameterToBoolean(p.get(id+ ".dateInFilename"));
		timeInFilename = BaseStepMeta.parameterToBoolean(p.get(id+ ".timeInFilename"));
		createparentfolder = BaseStepMeta.parameterToBoolean(p.get(id+ ".createparentfolder"));
		DoNotOpenNewFileInit = BaseStepMeta.parameterToBoolean(p.get(id+ ".DoNotOpenNewFileInit"));

	}
	
	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters)
	throws KettleException
{
		try
		{
			long id_connection =   rep.getStepAttributeInteger(id_step, STEP_ATTRIBUTE_ID_CONNECTION); 
			databaseMeta = DatabaseMeta.findDatabase( databases, id_connection);
			
			schemaName = rep.getStepAttributeString(id_step , STEP_ATTRIBUTE_SCHEMA);                          
			tablename = rep.getStepAttributeString(id_step , STEP_ATTRIBUTE_TABLE);                            
			truncateTable = rep.getStepAttributeBoolean(id_step , STEP_ATTRIBUTE_TRUNCATE);                    
			createTable = rep.getStepAttributeBoolean(id_step , STEP_ATTRIBUTE_CREATE);                        
			encoding = rep.getStepAttributeString(id_step , STEP_ATTRIBUTE_NCODING);                           
			dateformat = rep.getStepAttributeString(id_step , STEP_ATTRIBUTE_DATEFORMAT);                      
			AddToResult = rep.getStepAttributeBoolean(id_step , STEP_ATTRIBUTE_ADDTORESULT);                   
			StartNewLine = rep.getStepAttributeBoolean(id_step , STEP_ATTRIBUTE_STARTNEWLINE);                 
			fileName = rep.getStepAttributeString(id_step , STEP_ATTRIBUTE_FILE_NAME);                         
			extension = rep.getStepAttributeString(id_step , STEP_ATTRIBUTE_FILE_EXTENTION);                   
			fileAppended = rep.getStepAttributeBoolean(id_step , STEP_ATTRIBUTE_FILE_APPEND);                  
			splitEvery = (int)rep.getStepAttributeInteger(id_step , STEP_ATTRIBUTE_FILE_SPLIT);                
			stepNrInFilename = rep.getStepAttributeBoolean(id_step , STEP_ATTRIBUTE_FILE_ADD_STEPNR);          
			partNrInFilename = rep.getStepAttributeBoolean(id_step , STEP_ATTRIBUTE_FILE_ADD_PARTNR);          
			dateInFilename = rep.getStepAttributeBoolean(id_step , STEP_ATTRIBUTE_FILE_ADD_DATE);              
			timeInFilename = rep.getStepAttributeBoolean(id_step , STEP_ATTRIBUTE_FILE_ADD_TIME);              
			createparentfolder = rep.getStepAttributeBoolean(id_step , STEP_ATTRIBUTE_CREATE_PARENT_FOLDER);   
			DoNotOpenNewFileInit = rep.getStepAttributeBoolean(id_step , STEP_ATTRIBUTE_DONOTOPENNEWFILEINIT); 
		}
		catch(Exception e)
		{
			throw new KettleException("Unexpected error reading step information from the repository", e);
		}
	}

	public void saveRep(Repository rep, long id_transformation, long id_step) throws KettleException
	{
		try
		{
			rep.saveStepAttribute( id_transformation, id_step, STEP_ATTRIBUTE_ID_CONNECTION, databaseMeta==null?-1:databaseMeta.getID());
			rep.saveStepAttribute( id_transformation, id_step, STEP_ATTRIBUTE_SCHEMA, schemaName);
			rep.saveStepAttribute( id_transformation, id_step, STEP_ATTRIBUTE_TABLE, tablename);
			rep.saveStepAttribute( id_transformation, id_step, STEP_ATTRIBUTE_TRUNCATE, truncateTable);
			rep.saveStepAttribute( id_transformation, id_step, STEP_ATTRIBUTE_CREATE, createTable);		
			rep.saveStepAttribute( id_transformation, id_step, STEP_ATTRIBUTE_NCODING, encoding);
			rep.saveStepAttribute( id_transformation, id_step, STEP_ATTRIBUTE_DATEFORMAT, dateformat);
			rep.saveStepAttribute( id_transformation, id_step, STEP_ATTRIBUTE_ADDTORESULT, AddToResult);
			rep.saveStepAttribute( id_transformation, id_step, STEP_ATTRIBUTE_FILE_NAME, fileName);
			rep.saveStepAttribute( id_transformation, id_step, STEP_ATTRIBUTE_FILE_EXTENTION, extension);
			rep.saveStepAttribute( id_transformation, id_step, STEP_ATTRIBUTE_FILE_APPEND, fileAppended);
			rep.saveStepAttribute( id_transformation, id_step, STEP_ATTRIBUTE_FILE_SPLIT, splitEvery);
			rep.saveStepAttribute( id_transformation, id_step, STEP_ATTRIBUTE_FILE_ADD_STEPNR, stepNrInFilename);
			rep.saveStepAttribute( id_transformation, id_step, STEP_ATTRIBUTE_FILE_ADD_PARTNR, partNrInFilename);
			rep.saveStepAttribute( id_transformation, id_step, STEP_ATTRIBUTE_FILE_ADD_DATE, dateInFilename);
			rep.saveStepAttribute( id_transformation, id_step, STEP_ATTRIBUTE_FILE_ADD_TIME, timeInFilename);
			rep.saveStepAttribute( id_transformation, id_step, STEP_ATTRIBUTE_CREATE_PARENT_FOLDER, createparentfolder);
			rep.saveStepAttribute( id_transformation, id_step, STEP_ATTRIBUTE_DONOTOPENNEWFILEINIT, DoNotOpenNewFileInit);

			// Also, save the step-database relationship!
			if (databaseMeta!=null) rep.insertStepDatabase(id_transformation, id_step, databaseMeta.getID());
			
		}
		catch(Exception e)
		{
			throw new KettleException("Unable to save step information to the repository for id_step="+id_step, e);
		}
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
		if (databaseMeta!=null)
		{
			CheckResult cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("SQLFileOutputMeta.CheckResult.ConnectionExists"), stepMeta);
			remarks.add(cr);

			Database db = new Database(databaseMeta);
			try
			{
				db.connect();
				
				cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("SQLFileOutputMeta.CheckResult.ConnectionOk"), stepMeta);
				remarks.add(cr);

				if (!Const.isEmpty(tablename))
				{
                    String schemaTable = databaseMeta.getQuotedSchemaTableCombination(schemaName, tablename);
					// Check if this table exists...
					if (db.checkTableExists(schemaTable))
					{
						cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("SQLFileOutputMeta.CheckResult.TableAccessible", schemaTable), stepMeta);
						remarks.add(cr);

						RowMetaInterface r = db.getTableFields(schemaTable);
						if (r!=null)
						{
							cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("SQLFileOutputMeta.CheckResult.TableOk", schemaTable), stepMeta);
							remarks.add(cr);

							String error_message = "";
							boolean error_found = false;
							// OK, we have the table fields.
							// Now see what we can find as previous step...
							if (prev!=null && prev.size()>0)
							{
								cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("SQLFileOutputMeta.CheckResult.FieldsReceived", ""+prev.size()), stepMeta);
								remarks.add(cr);
	
								// Starting from prev...
								for (int i=0;i<prev.size();i++)
								{
									ValueMetaInterface pv = prev.getValueMeta(i);
									int idx = r.indexOfValue(pv.getName());
									if (idx<0) 
									{
										error_message+="\t\t"+pv.getName()+" ("+pv.getTypeDesc()+")"+Const.CR;
										error_found=true;
									} 
								}
								if (error_found) 
								{
									error_message=Messages.getString("SQLFileOutputMeta.CheckResult.FieldsNotFoundInOutput", error_message);
	
									cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, error_message, stepMeta);
									remarks.add(cr);
								}
								else
								{
									cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("SQLFileOutputMeta.CheckResult.AllFieldsFoundInOutput"), stepMeta);
									remarks.add(cr);
								}
	
								// Starting from table fields in r...
								for (int i=0;i<r.size();i++)
								{
									ValueMetaInterface rv = r.getValueMeta(i);
									int idx = prev.indexOfValue(rv.getName());
									if (idx<0) 
									{
										error_message+="\t\t"+rv.getName()+" ("+rv.getTypeDesc()+")"+Const.CR;
										error_found=true;
									} 
								}
								if (error_found) 
								{
									error_message=Messages.getString("SQLFileOutputMeta.CheckResult.FieldsNotFound", error_message);
	
									cr = new CheckResult(CheckResult.TYPE_RESULT_WARNING, error_message, stepMeta);
									remarks.add(cr);
								}
								else
								{
									cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("SQLFileOutputMeta.CheckResult.AllFieldsFound"), stepMeta);
									remarks.add(cr);
								}
							}
							else
							{
								cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("SQLFileOutputMeta.CheckResult.NoFields"), stepMeta);
								remarks.add(cr);
							}
						}
						else
						{
							cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("SQLFileOutputMeta.CheckResult.TableNotAccessible"), stepMeta);
							remarks.add(cr);
						}
					}
					else
					{
						cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("SQLFileOutputMeta.CheckResult.TableError", schemaTable), stepMeta);
						remarks.add(cr);
					}
				}
				else
				{
					cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("SQLFileOutputMeta.CheckResult.NoTableName"), stepMeta);
					remarks.add(cr);
				}
			}
			catch(KettleException e)
			{
				cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("SQLFileOutputMeta.CheckResult.UndefinedError", e.getMessage()), stepMeta);
				remarks.add(cr);
			}
			finally
			{
				db.disconnect();
			}
		}
		else
		{
			CheckResult cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("SQLFileOutputMeta.CheckResult.NoConnection"), stepMeta);
			remarks.add(cr);
		}
		
		// See if we have input streams leading to this step!
		if (input.length>0)
		{
			CheckResult cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("SQLFileOutputMeta.CheckResult.ExpectedInputOk"), stepMeta);
			remarks.add(cr);
		}
		else
		{
			CheckResult cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("SQLFileOutputMeta.CheckResult.ExpectedInputError"), stepMeta);
			remarks.add(cr);
		}
	}

	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans trans)
	{
		return new SQLFileOutput(stepMeta, stepDataInterface, cnr, transMeta, trans);
	}
	
	
	public StepDataInterface getStepData()
	{
		return new SQLFileOutputData();
	}

	public void analyseImpact(List<DatabaseImpact> impact, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
		if (truncateTable)
		{
			DatabaseImpact ii = new DatabaseImpact( DatabaseImpact.TYPE_IMPACT_TRUNCATE, 
											transMeta.getName(),
											stepMeta.getName(),
											databaseMeta.getDatabaseName(),
											tablename,
											"",
											"",
											"",
											"",
											"Truncate of table"
											);
			impact.add(ii);

		}
		// The values that are entering this step are in "prev":
		if (prev!=null)
		{
			for (int i=0;i<prev.size();i++)
			{
				ValueMetaInterface v = prev.getValueMeta(i);
				DatabaseImpact ii = new DatabaseImpact( DatabaseImpact.TYPE_IMPACT_WRITE, 
												transMeta.getName(),
												stepMeta.getName(),
												databaseMeta.getDatabaseName(),
												tablename,
												v.getName(),
												v.getName(),
                                                v!=null?v.getOrigin():"?",
												"",
												"Type = "+v.toStringMeta()
												);
				impact.add(ii);
			}
		}
	}

	public SQLStatement getSQLStatements(TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev)
	{
		SQLStatement retval = new SQLStatement(stepMeta.getName(), databaseMeta, null); // default: nothing to do!
	
		if (databaseMeta!=null)
		{
			if (prev!=null && prev.size()>0)
			{
				if (!Const.isEmpty(tablename))
				{
					Database db = new Database(databaseMeta);
					db.shareVariablesWith(transMeta);
					try
					{
						db.connect();
						
                        String schemaTable = databaseMeta.getQuotedSchemaTableCombination(schemaName, tablename);
                        String cr_table = db.getDDL(schemaTable, prev);
						
						// Empty string means: nothing to do: set it to null...
						if (cr_table==null || cr_table.length()==0) cr_table=null;
						
						retval.setSQL(cr_table);
					}
					catch(KettleDatabaseException dbe)
					{
						retval.setError(Messages.getString("SQLFileOutputMeta.Error.ErrorConnecting", dbe.getMessage()));
					}
					finally
					{
						db.disconnect();
					}
				}
				else
				{
					retval.setError(Messages.getString("SQLFileOutputMeta.Exception.TableNotSpecified"));
				}
			}
			else
			{
				retval.setError(Messages.getString("SQLFileOutputMeta.Error.NoInput"));
			}
		}
		else
		{
			retval.setError(Messages.getString("SQLFileOutputMeta.Error.NoConnection"));
		}

		return retval;
	}

	 public RowMetaInterface getRequiredFields() throws KettleException
	    {
	        if (databaseMeta!=null)
	        {
	            Database db = new Database(databaseMeta);
	            try
	            {
	                db.connect();
	                
	                if (!Const.isEmpty(tablename))
	                {
	                    String schemaTable = databaseMeta.getQuotedSchemaTableCombination(schemaName, tablename);
	                    
	                    // Check if this table exists...
	                    if (db.checkTableExists(schemaTable))
	                    {
	                        return db.getTableFields(schemaTable);
	                    }
	                    else
	                    {
	                        throw new KettleException(Messages.getString("SQLFileOutputMeta.Exception.TableNotFound"));
	                    }
	                }
	                else
	                {
	                    throw new KettleException(Messages.getString("SQLFileOutputMeta.Exception.TableNotSpecified"));
	                }
	            }
	            catch(Exception e)
	            {
	                throw new KettleException(Messages.getString("SQLFileOutputMeta.Exception.ErrorGettingFields"), e);
	            }
	            finally
	            {
	                db.disconnect();
	            }
	        }
	        else
	        {
	            throw new KettleException(Messages.getString("SQLFileOutputMeta.Exception.ConnectionNotDefined"));
	        }

	    }

    
    public DatabaseMeta[] getUsedDatabaseConnections()
    {
        if (databaseMeta!=null) 
        {
            return new DatabaseMeta[] { databaseMeta };
        }
        else
        {
            return super.getUsedDatabaseConnections();
        }
    }

    /**
     * @return the schemaName
     */
    public String getSchemaName()
    {
        return schemaName;
    }

    /**
     * @param schemaName the schemaName to set
     */
    public void setSchemaName(String schemaName)
    {
        this.schemaName = schemaName;
    }
    
    public boolean supportsErrorHandling()
    {
        return true;
    }


}
