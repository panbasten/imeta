/*************************************************************************************** 
 * Copyright (C) 2007 Samatar.  All rights reserved. 
 * This software was developed by Samatar and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. A copy of the license, 
 * is included with the binaries and source code. The Original Code is Samatar.  
 * The Initial Developer is Samatar.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an 
 * "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. 
 * Please refer to the license for the specific language governing your rights 
 * and limitations.
 ***************************************************************************************/

package com.panet.imeta.trans.steps.rssoutput;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.core.CheckResult;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Counter;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.variables.VariableSpace;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.shared.SharedObjectInterface;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepMeta;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;

/**
 * Output rows to RSS feed and create a file.
 * 
 * @author Samatar
 * @since 6-nov-2007
 */
 
public class RssOutputMeta extends BaseStepMeta implements StepMetaInterface
{

	public static final String STEP_ATTRIBUTE_DISPLAYITEM  = "displayitem ";
	public static final String STEP_ATTRIBUTE_CUSTOMRSS  = "customrss ";
	public static final String STEP_ATTRIBUTE_CHANNEL_TITLE  = "channel_title ";
	public static final String STEP_ATTRIBUTE_CHANNEL_DESCRIPTION  = "channel_description ";
	public static final String STEP_ATTRIBUTE_CHANNEL_LINK = "channel_link";
	public static final String STEP_ATTRIBUTE_CHANNEL_PUBDATE  = "channel_pubdate ";
	public static final String STEP_ATTRIBUTE_CHANNEL_COPYRIGHT  = "channel_copyright ";
	public static final String STEP_ATTRIBUTE_CHANNEL_IMAGE_TITLE  = "channel_image_title ";
	public static final String STEP_ATTRIBUTE_CHANNEL_IMAGE_LINK = "channel_image_link";
	public static final String STEP_ATTRIBUTE_CHANNEL_IMAGE_URL  = "channel_image_url ";
	public static final String STEP_ATTRIBUTE_CHANNEL_IMAGE_DESCRIPTION  = "channel_image_description ";
	public static final String STEP_ATTRIBUTE_CHANNEL_LANGUAGE = "channel_language";
	public static final String STEP_ATTRIBUTE_CHANNEL_AUTHOR = "channel_author";
	public static final String STEP_ATTRIBUTE_VERSION  = "version ";
	public static final String STEP_ATTRIBUTE_ENCODING = "encoding";
	public static final String STEP_ATTRIBUTE_ADDIMAGE = "addimage";
	public static final String STEP_ATTRIBUTE_ITEM_TITLE = "item_title";
	public static final String STEP_ATTRIBUTE_ITEM_DESCRIPTION = "item_description";
	public static final String STEP_ATTRIBUTE_ITEM_LINK  = "item_link ";
	public static final String STEP_ATTRIBUTE_ITEM_PUBDATE = "item_pubdate";
	public static final String STEP_ATTRIBUTE_ITEM_AUTHOR  = "item_author ";
	public static final String STEP_ATTRIBUTE_ADDGEORSS  = "addgeorss ";
	public static final String STEP_ATTRIBUTE_USEGEORSSGML = "usegeorssgml";
	public static final String STEP_ATTRIBUTE_GEOPOINTLAT  = "geopointlat ";
	public static final String STEP_ATTRIBUTE_GEOPOINTLONG = "geopointlong";
	public static final String STEP_ATTRIBUTE_FILENAME_FIELD = "filename_field";
	public static final String STEP_ATTRIBUTE_FILE_NAME  = "file_name ";
	public static final String STEP_ATTRIBUTE_FILE_EXTENTION = "file_extention";
	public static final String STEP_ATTRIBUTE_FILE_ADD_STEPNR  = "file_add_stepnr ";
	public static final String STEP_ATTRIBUTE_FILE_ADD_PARTNR  = "file_add_partnr ";
	public static final String STEP_ATTRIBUTE_FILE_ADD_DATE  = "file_add_date ";
	public static final String STEP_ATTRIBUTE_FILE_ADD_TIME  = "file_add_time ";
	public static final String STEP_ATTRIBUTE_IS_FILENAME_IN_FIELD = "is_filename_in_field";
	public static final String STEP_ATTRIBUTE_CREATE_PARENT_FOLDER = "create_parent_folder";
	public static final String STEP_ATTRIBUTE_ADDTORESULT  = "addtoresult ";
	
	private String channeltitle;
	private String channeldescription;
	private String channellink;
	private String channelpubdate;
	private String channelcopyright;
	private String channelimagetitle;
	private String channelimagelink;
	private String channelimageurl;
	private String channelimagedescription;
	private String channellanguage;
	private String channelauthor;

	private String itemtitle;
	private String itemdescription;
	private String itemlink;
	private String itempubdate;
	private String itemauthor;
	private String geopointlat; 
	private String geopointlong; 
		
	private boolean	AddToResult;
		
	
    /** The base name of the output file */
	private  String fileName;
	
	/** The file extention in case of a generated filename */
	private  String  extension;
	
	/** Flag: add the stepnr in the filename */
    private  boolean stepNrInFilename;
	
	/** Flag: add the partition number in the filename */
    private  boolean partNrInFilename;
	
	/** Flag: add the date in the filename */
    private  boolean dateInFilename;
	
	/** Flag: add the time in the filename */
    private  boolean timeInFilename;
    
    /** Flag: create parent folder if needed */
    private boolean createparentfolder;
    
    /** Rss version	**/
    private String version;
    
    /** Rss encoding	**/
    private String encoding;
    
    /** Flag : add image to RSS feed **/
    private boolean addimage;
    
    private boolean addgeorss;
    
    private boolean usegeorssgml;
    
    /** The field that contain filename */
    private String filenamefield;
    
    /** Flag : is filename defined in a field **/
    private boolean isfilenameinfield;
	
	/** which fields do we use for Channel Custom ? */
	private String  ChannelCustomFields[];
	
	/** add namespaces? */
	private String NameSpaces[];
	
	private String NameSpacesTitle[];
	
	/** which fields do we use for getChannelCustomTags Custom ? */
	private String  ChannelCustomTags[];
	
	/** which fields do we use for Item Custom Field? */
	private String  ItemCustomFields[];
	
	/** which fields do we use for Item Custom tag ? */
	private String  ItemCustomTags[];
	
	/** create custom RSS ? */
	private boolean  customrss;
	
	/** display item tag in output ? */
	private boolean displayitem;
	

	 public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
	    throws KettleXMLException
	{
		readData(stepnode);
	}

	public Object clone()
	{
		
		RssOutputMeta retval = (RssOutputMeta)super.clone();
		int nrfields    = ChannelCustomFields.length;
		retval.allocate(nrfields);
		
		// Read custom channel fields
		for (int i=0;i<nrfields;i++)
		{
			retval.ChannelCustomFields[i] = ChannelCustomFields[i];
			retval.ChannelCustomTags[i] = ChannelCustomTags[i];
		}
		
		
		int nritemfields    = ItemCustomFields.length;
		retval.allocateitem(nritemfields);
		
		// Read custom channel fields
		for (int i=0;i<nritemfields;i++)
		{
			retval.ItemCustomFields[i] = ItemCustomFields[i];
			retval.ItemCustomTags[i] = ItemCustomTags[i];
		}
		
		// Namespaces
		int nramespaces=NameSpaces.length;
		retval.allocatenamespace(nramespaces);
		// Read custom channel fields
		for (int i=0;i<nramespaces;i++)
		{
			retval.NameSpacesTitle[i] = NameSpacesTitle[i];
			retval.NameSpaces[i] = NameSpaces[i];
		}
		
		return retval;

	}
	
	public void allocate(int nrfields)
	{
		ChannelCustomFields  = new String[nrfields];
		ChannelCustomTags  = new String[nrfields];
	}
	
	public void allocateitem(int nrfields)
	{
		ItemCustomFields  = new String[nrfields];
		ItemCustomTags  = new String[nrfields];
	}
	
	public void allocatenamespace(int nrnamespaces)
	{
		NameSpaces  = new String[nrnamespaces];
		NameSpacesTitle  = new String[nrnamespaces];
	}
	
    /**
     * @return Returns the version.
     */
    public String getVersion()
    {
        return version;
    }
    
    /**
     * @param version The version to set.
     */
    public void setVersion(String version)
    {
        this.version = version;
    }
    
    /**
     * @return Returns the encoding.
     */
    public String getEncoding()
    {
        return encoding;
    }
    
    /**
     * @param encoding The encoding to set.
     */
    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }
    
    /**
     * @return Returns the filenamefield.
     */
    public String getFileNameField()
    {
        return filenamefield;
    }
    
    /**
     * @param encoding The encoding to set.
     */
    public void setFileNameField(String filenamefield)
    {
        this.filenamefield = filenamefield;
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
     * @return Returns the fileName.
     */
    public String getFileName()
    {
        return fileName;
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
     * @return Returns the Add to result filesname flag.
     */
    public boolean AddToResult()
    {
        return AddToResult;
    }
    
    /**
     * @param AddToResult The Add file to result to set.
     */
    public void setAddToResult(boolean AddToResult)
    {
        this.AddToResult = AddToResult;
    }
    
    /**
     * @param customrss The custom RSS flag to set.
     */
    public void setCustomRss(boolean customrss)
    {
        this.customrss = customrss;
    }
    
    /**
     * @return Returns the custom RSS flag.
     */
    public boolean isCustomRss()
    {
        return customrss;
    }
    
    /**
     * @param displayitem The display itema ta flag.
     */
    public void setDisplayItem(boolean displayitem)
    {
        this.displayitem = displayitem;
    }
    
    /**
     * @return Returns the displayitem.
     */
    public boolean isDisplayItem()
    {
        return displayitem;
    }
    
    /**
     * @return Returns the addimage flag.
     */
    public boolean AddImage()
    {
        return addimage;
    }
    
    /**
     * @param addimage The addimage to set.
     */
    public void setAddImage(boolean addimage)
    {
        this.addimage = addimage;
    }
    
    /**
     * @return Returns the addgeorss flag.
     */
    public boolean AddGeoRSS()
    {
        return addgeorss;
    }
    
    /**
     * @param addgeorss The addgeorss to set.
     */
    public void setAddGeoRSS(boolean addgeorss)
    {
        this.addgeorss = addgeorss;
    }
    
    /**
     * @return Returns the addgeorss flag.
     */
    public boolean useGeoRSSGML()
    {
        return usegeorssgml;
    }
    
    /**
     * @param usegeorssgml The usegeorssgml to set.
     */
    public void setUseGeoRSSGML(boolean usegeorssgml)
    {
        this.usegeorssgml = usegeorssgml;
    }
  
    /**
     * @return Returns the isfilenameinfield flag.
     */
    public boolean isFilenameInField()
    {
        return isfilenameinfield;
    }
    
    /**
     * @param isfilenameinfield The isfilenameinfield to set.
     */
    public void setFilenameInField(boolean isfilenameinfield)
    {
        this.isfilenameinfield = isfilenameinfield;
    }
    
	/**
	 * @return Returns the ChannelCustomFields (names in the stream).
	 */
	public String[] getChannelCustomFields()
	{
		return ChannelCustomFields;
	}

	/**
	 * @param ChannelCustomFields The ChannelCustomFields to set.
	 */
	public void setChannelCustomFields(String[] ChannelCustomFields)
	{
		this.ChannelCustomFields = ChannelCustomFields;
	}
	
	/**
	 * @return Returns the NameSpaces.
	 */
	public String[] getNameSpaces()
	{
		return NameSpaces;
	}
	
	/**
	 * @param NameSpaces The NameSpaces to set.
	 */
	public void setNameSpaces(String[] NameSpaces)
	{
		this.NameSpaces = NameSpaces;
	}
	
	/**
	 * @return Returns the NameSpaces.
	 */
	public String[] getNameSpacesTitle()
	{
		return NameSpacesTitle;
	}
	
	/**
	 * @param NameSpacesTitle The NameSpacesTitle to set.
	 */
	public void setNameSpacesTitle(String[] NameSpacesTitle)
	{
		this.NameSpacesTitle = NameSpacesTitle;
	}
	
	/**
	 * @return Returns the getChannelCustomTags (names in the stream).
	 */
	public String[] getChannelCustomTags()
	{
		return ChannelCustomTags;
	}

	/**
	 * @param getChannelCustomTags The getChannelCustomTags to set.
	 */
	public void setChannelCustomTags(String[] ChannelCustomTags)
	{
		this.ChannelCustomTags = ChannelCustomTags;
	}
    
    /**
	 * @return Returns the getChannelCustomTags (names in the stream).
	 */
	public String[] getItemCustomTags()
	{
		return ItemCustomTags;
	}

	/**
	 * @param getChannelCustomTags The getChannelCustomTags to set.
	 */
	public void setItemCustomTags(String[] ItemCustomTags)
	{
		this.ItemCustomTags = ItemCustomTags;
	}

    /**
	 * @return Returns the ChannelItemFields (names in the stream).
	 */
	public String[] getItemCustomFields()
	{
		return ItemCustomFields;
	}

	/**
	 * @param getChannelCustomTags The getChannelCustomTags to set.
	 */
	public void setItemCustomFields(String[] ItemCustomTags)
	{
		this.ItemCustomTags = ItemCustomTags;
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
    
    public String[] getFiles(VariableSpace space)
	{
		int copies=1;
		int parts=1;

		if (stepNrInFilename)
		{
			copies=3;
		}
		
		if (partNrInFilename)
		{
			parts=3;
		}

		int nr=copies*parts;
		if (nr>1) nr++;
		
		String retval[]=new String[nr];
		
		int i=0;
		for (int copy=0;copy<copies;copy++)
		{
			for (int part=0;part<parts;part++)
			{
					retval[i]=buildFilename(space, copy);
					i++;
			}
		}
		if (i<nr)
		{
			retval[i]="...";
		}
		return retval;
	}
    
	public String buildFilename(VariableSpace space, int stepnr)
	{
		SimpleDateFormat daf     = new SimpleDateFormat();

		// Replace possible environment variables...
		String retval=space.environmentSubstitute(fileName) ;

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
		
		if (extension!=null && extension.length()!=0) 
		{
			retval+="."+extension;
		}
		
		return retval;
	}
    
	private void readData(Node stepnode) throws KettleXMLException
	{
		try
		{
			
			displayitem = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "displayitem"));
			customrss = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "customrss"));
			channeltitle    = XMLHandler.getTagValue(stepnode, "channel_title");
			channeldescription    = XMLHandler.getTagValue(stepnode, "channel_description");
			channellink    = XMLHandler.getTagValue(stepnode, "channel_link");
			channelpubdate    = XMLHandler.getTagValue(stepnode, "channel_pubdate");
			channelcopyright    = XMLHandler.getTagValue(stepnode, "channel_copyright");
			
			channelimagetitle   = XMLHandler.getTagValue(stepnode, "channel_image_title");
			channelimagelink   = XMLHandler.getTagValue(stepnode, "channel_image_link");
			channelimageurl   = XMLHandler.getTagValue(stepnode, "channel_image_url");
			channelimagedescription   = XMLHandler.getTagValue(stepnode, "channel_image_description");
			channellanguage    = XMLHandler.getTagValue(stepnode, "channel_language");
			channelauthor    = XMLHandler.getTagValue(stepnode, "channel_author");
			
			version    = XMLHandler.getTagValue(stepnode, "version");
			encoding    = XMLHandler.getTagValue(stepnode, "encoding");
			
			addimage = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "addimage"));
			
			// Items ...
			itemtitle    = XMLHandler.getTagValue(stepnode, "item_title");
			itemdescription    = XMLHandler.getTagValue(stepnode, "item_description");
			itemlink    = XMLHandler.getTagValue(stepnode, "item_link");
			itempubdate    = XMLHandler.getTagValue(stepnode, "item_pubdate");
			itemauthor    = XMLHandler.getTagValue(stepnode, "item_author");			
			
			addgeorss = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "addgeorss"));
			usegeorssgml = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "usegeorssgml"));
			geopointlat    = XMLHandler.getTagValue(stepnode, "geopointlat");
			geopointlong    = XMLHandler.getTagValue(stepnode, "geopointlong");
			
			filenamefield  = XMLHandler.getTagValue(stepnode, "file", "filename_field");
			fileName  = XMLHandler.getTagValue(stepnode, "file", "name");
			
			isfilenameinfield ="Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "is_filename_in_field"));
			createparentfolder ="Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "create_parent_folder"));
			extension = XMLHandler.getTagValue(stepnode, "file", "extention");
			stepNrInFilename     = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "split"));
			partNrInFilename     = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "haspartno"));
			dateInFilename  = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "add_date"));
			timeInFilename  = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "add_time"));
			AddToResult     = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "AddToResult"));
			
			Node keys = XMLHandler.getSubNode(stepnode, "fields"); //$NON-NLS-1$
			// Custom Channel fields
			int nrchannelfields    = XMLHandler.countNodes(keys, "channel_custom_fields"); //$NON-NLS-1$
			allocate(nrchannelfields);

			for (int i=0;i<nrchannelfields;i++)
			{
				Node knode = XMLHandler.getSubNodeByNr(keys, "channel_custom_fields", i);
				ChannelCustomTags[i]      = XMLHandler.getTagValue(knode, "tag"); 
				ChannelCustomFields[i]      = XMLHandler.getTagValue(knode, "field");	
			}
			// Custom Item fields
			int nritemfields    = XMLHandler.countNodes(keys, "item_custom_fields"); //$NON-NLS-1$
			allocateitem(nritemfields);

			for (int i=0;i<nritemfields;i++)
			{
				Node knode = XMLHandler.getSubNodeByNr(keys, "item_custom_fields", i);
				ItemCustomTags[i]      = XMLHandler.getTagValue(knode, "tag"); 
				ItemCustomFields[i]      = XMLHandler.getTagValue(knode, "field");	
			}
			// NameSpaces
			Node keysNameSpaces = XMLHandler.getSubNode(stepnode, "namespaces"); //$NON-NLS-1$
			int nrnamespaces    = XMLHandler.countNodes(keysNameSpaces, "namespace"); //$NON-NLS-1$
			allocatenamespace(nrnamespaces);
			for (int i=0;i<nrnamespaces;i++)
			{
				Node knode = XMLHandler.getSubNodeByNr(keysNameSpaces, "namespace", i);
				NameSpacesTitle[i]  = XMLHandler.getTagValue(knode, "namespace_tag"); 
				NameSpaces[i]      = XMLHandler.getTagValue(knode, "namespace_value"); 
			}
		
        }
		catch(Exception e)
		{
			throw new KettleXMLException("Unable to load step info from XML", e);
		}
	}

	public void setDefault()
	{
		displayitem=true;
		customrss=false;
		channeltitle=null;
		channeldescription=null;
		channellink=null;
		channelpubdate=null;
		channelcopyright=null;
		channelimagetitle=null;
		channelimagelink=null;
		channelimageurl=null;
		channelimagedescription=null;
		channellanguage=null;
		channelauthor=null;
		createparentfolder=false;
		isfilenameinfield=false;
		version="rss_2.0";
		encoding="iso-8859-1";
		filenamefield=null;
		isfilenameinfield=false;
		
		// Items ...
		itemtitle    = null;
		itemdescription    = null;
		itemlink    = null;
		itempubdate    = null;
		itemauthor    = null;
		geopointlat=null;
		geopointlong=null;
		int nrchannelfields  = 0;
		allocate(nrchannelfields);
		// channel custom fields
		for (int i=0;i<nrchannelfields;i++)
		{
			ChannelCustomFields[i] ="field"+i; //$NON-NLS-1$
			ChannelCustomTags[i]="tag"+i;
		}
		
		int nritemfields  = 0;
		allocateitem(nritemfields);
		// Custom Item Fields
		for (int i=0;i<nritemfields;i++)
		{
			ItemCustomFields[i] ="field"+i; //$NON-NLS-1$
			ItemCustomTags[i]="tag"+i;
		}
        //  Namespaces
		int nrnamespaces  = 0;
		allocatenamespace(nrnamespaces);
		// Namespaces
		for (int i=0;i<nrnamespaces;i++)
		{
			NameSpacesTitle[i]="namespace_title"+i;
			NameSpaces[i] ="namespace"+i; //$NON-NLS-1$
		}
	}

	public String getXML()
	{
		StringBuffer retval=new StringBuffer();
		
		retval.append("    "+XMLHandler.addTagValue("displayitem",      displayitem));
		retval.append("    "+XMLHandler.addTagValue("customrss",      customrss));
        retval.append("    "+XMLHandler.addTagValue("channel_title",        channeltitle));
        retval.append("    "+XMLHandler.addTagValue("channel_description",  channeldescription));
        retval.append("    "+XMLHandler.addTagValue("channel_link",  channellink));
        retval.append("    "+XMLHandler.addTagValue("channel_pubdate",  channelpubdate));
        retval.append("    "+XMLHandler.addTagValue("channel_copyright",  channelcopyright));
        
        retval.append("    "+XMLHandler.addTagValue("channel_image_title",  channelimagetitle));
        retval.append("    "+XMLHandler.addTagValue("channel_image_link",  channelimagelink));
        retval.append("    "+XMLHandler.addTagValue("channel_image_url",  channelimageurl));
        retval.append("    "+XMLHandler.addTagValue("channel_image_description",  channelimagedescription));
        retval.append("    "+XMLHandler.addTagValue("channel_language",  channellanguage));
        retval.append("    "+XMLHandler.addTagValue("channel_author",  channelauthor));
        
        retval.append("    "+XMLHandler.addTagValue("version",        version));
        retval.append("    "+XMLHandler.addTagValue("encoding",       encoding));
        
        retval.append("    "+XMLHandler.addTagValue("addimage",      addimage));
		
		// Items ...
		
		retval.append("    "+XMLHandler.addTagValue("item_title",        itemtitle));
		retval.append("    "+XMLHandler.addTagValue("item_description",   itemdescription));
		retval.append("    "+XMLHandler.addTagValue("item_link",        itemlink));
		retval.append("    "+XMLHandler.addTagValue("item_pubdate",        itempubdate));
		retval.append("    "+XMLHandler.addTagValue("item_author",        itemauthor));		
		retval.append("    "+XMLHandler.addTagValue("addgeorss",      addgeorss));
		retval.append("    "+XMLHandler.addTagValue("usegeorssgml",      usegeorssgml));
		retval.append("    "+XMLHandler.addTagValue("geopointlat",      geopointlat));
		retval.append("    "+XMLHandler.addTagValue("geopointlong",      geopointlong));
		
		retval.append("    <file>"+Const.CR);
		retval.append("      "+XMLHandler.addTagValue("filename_field",   filenamefield));
		retval.append("      "+XMLHandler.addTagValue("name",       fileName));
		retval.append("      "+XMLHandler.addTagValue("extention",  extension));
		retval.append("      "+XMLHandler.addTagValue("split",      stepNrInFilename));
		retval.append("      "+XMLHandler.addTagValue("haspartno",  partNrInFilename));
		retval.append("      "+XMLHandler.addTagValue("add_date",   dateInFilename));
		retval.append("      "+XMLHandler.addTagValue("add_time",   timeInFilename));
		retval.append("      "+XMLHandler.addTagValue("is_filename_in_field",   isfilenameinfield));
		retval.append("      "+XMLHandler.addTagValue("create_parent_folder",   createparentfolder));
		retval.append("    "+XMLHandler.addTagValue("addtoresult",      AddToResult));
		retval.append("      </file>"+Const.CR);
		
		retval.append("      <fields>").append(Const.CR); //$NON-NLS-1$
		
		for (int i=0;i<ChannelCustomFields.length;i++)
		{
			retval.append("        <channel_custom_fields>").append(Const.CR); //$NON-NLS-1$
			retval.append("          ").append(XMLHandler.addTagValue("tag",   ChannelCustomTags[i]));
			retval.append("          ").append(XMLHandler.addTagValue("field",   ChannelCustomFields[i])); //$NON-NLS-1$ //$NON-NLS-2$
			retval.append("        </channel_custom_fields>").append(Const.CR); //$NON-NLS-1$
		}
		for (int i=0;i<ItemCustomFields.length;i++)
		{
			retval.append("        <Item_custom_fields>").append(Const.CR); //$NON-NLS-1$
			retval.append("          ").append(XMLHandler.addTagValue("tag",   ItemCustomTags[i]));
			retval.append("          ").append(XMLHandler.addTagValue("field",   ItemCustomFields[i])); //$NON-NLS-1$ //$NON-NLS-2$
			retval.append("        </Item_custom_fields>").append(Const.CR); //$NON-NLS-1$
		}
		retval.append("      </fields>").append(Const.CR); //$NON-NLS-1$
		

		retval.append("      <namespaces>").append(Const.CR); 
		for (int i=0;i<NameSpaces.length;i++)
		{
			retval.append("        <namespace>").append(Const.CR);
			retval.append("          ").append(XMLHandler.addTagValue("namespace_tag",   NameSpacesTitle[i]));
			retval.append("          ").append(XMLHandler.addTagValue("namespace_value",   NameSpaces[i]));
			retval.append("        </namespace>").append(Const.CR); 
		}
		retval.append("      </namespaces>").append(Const.CR); 
		
		return retval.toString();
	}
	
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
	
		displayitem = BaseStepMeta.parameterToBoolean(p.get(id+ ".displayitem"));
		customrss = BaseStepMeta.parameterToBoolean(p.get(id+ ".customrss"));
		channeltitle = BaseStepMeta.parameterToString(p.get(id+ ".channeltitle"));
		channeldescription = BaseStepMeta.parameterToString(p.get(id+ ".channeldescription"));
		channellink = BaseStepMeta.parameterToString(p.get(id+ ".channellink"));
		channelpubdate = BaseStepMeta.parameterToString(p.get(id+ ".channelpubdate"));
		channelcopyright = BaseStepMeta.parameterToString(p.get(id+ ".channelcopyright"));
		channelimagetitle = BaseStepMeta.parameterToString(p.get(id+ ".channelimagetitle"));
		channelimagelink = BaseStepMeta.parameterToString(p.get(id+ ".channelimagelink"));
		channelimageurl = BaseStepMeta.parameterToString(p.get(id+ ".channelimageurl"));
		channelimagedescription = BaseStepMeta.parameterToString(p.get(id+ ".channelimagedescription"));
		channellanguage = BaseStepMeta.parameterToString(p.get(id+ ".channellanguage"));
		channelauthor = BaseStepMeta.parameterToString(p.get(id+ ".channelauthor"));
		version = BaseStepMeta.parameterToString(p.get(id+ ".version"));
		encoding = BaseStepMeta.parameterToString(p.get(id+ ".encoding"));
		addimage = BaseStepMeta.parameterToBoolean(p.get(id+ ".addimage"));
		itemtitle = BaseStepMeta.parameterToString(p.get(id+ ".itemtitle"));
		itemdescription = BaseStepMeta.parameterToString(p.get(id+ ".itemdescription"));
		itemlink = BaseStepMeta.parameterToString(p.get(id+ ".itemlink"));
		itempubdate = BaseStepMeta.parameterToString(p.get(id+ ".itempubdate"));
		itemauthor = BaseStepMeta.parameterToString(p.get(id+ ".itemauthor"));
		addgeorss = BaseStepMeta.parameterToBoolean(p.get(id+ ".addgeorss"));
		usegeorssgml = BaseStepMeta.parameterToBoolean(p.get(id+ ".usegeorssgml"));
		geopointlat = BaseStepMeta.parameterToString(p.get(id+ ".geopointlat"));
		geopointlong = BaseStepMeta.parameterToString(p.get(id+ ".geopointlong"));
		filenamefield = BaseStepMeta.parameterToString(p.get(id+ ".filenamefield"));
		fileName = BaseStepMeta.parameterToString(p.get(id+ ".fileName"));
		extension = BaseStepMeta.parameterToString(p.get(id+ ".extension"));
		stepNrInFilename = BaseStepMeta.parameterToBoolean(p.get(id+ ".stepNrInFilename"));
		partNrInFilename = BaseStepMeta.parameterToBoolean(p.get(id+ ".partNrInFilename"));
		dateInFilename = BaseStepMeta.parameterToBoolean(p.get(id+ ".dateInFilename"));
		timeInFilename = BaseStepMeta.parameterToBoolean(p.get(id+ ".timeInFilename"));
		isfilenameinfield = BaseStepMeta.parameterToBoolean(p.get(id+ ".isfilenameinfield"));
		createparentfolder = BaseStepMeta.parameterToBoolean(p.get(id+ ".createparentfolder"));
		AddToResult = BaseStepMeta.parameterToBoolean(p.get(id+ ".AddToResult"));
	
        String[] channelCustomTags = p.get(id+"_channelFields1.ChannelCustomTags");
		String[] channelCustomFields = p.get(id+"_channelFields1.ChannelCustomFields");
		
		String[] itemCustomTags = p.get(id+"_itemFields1.ItemCustomTags");	
		String[] itemCustomFields = p.get(id+"_itemFields1.ItemCustomFields");
		
		String[] nameSpacesTitle = p.get(id+"_namespaces.NameSpacesTitle");
		String[] nameSpaces = p.get(id+"_namespaces.NameSpaces");
		
	
		this.ChannelCustomTags = channelCustomTags;
		this.ChannelCustomFields = channelCustomFields;
		this.ItemCustomTags = itemCustomTags;		
		this.ItemCustomFields = itemCustomFields;
		this.NameSpacesTitle = nameSpacesTitle;
		this.NameSpaces = nameSpaces;

	}
	

	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters)
    throws KettleException
    {
		try
		{
			
			displayitem = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_DISPLAYITEM);
			customrss = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_CUSTOMRSS);
			channeltitle = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_CHANNEL_TITLE);
			channeldescription = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_CHANNEL_DESCRIPTION);
			channellink = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_CHANNEL_LINK);
			channelpubdate = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_CHANNEL_PUBDATE);
			channelcopyright = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_CHANNEL_COPYRIGHT);
			channelimagetitle = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_CHANNEL_IMAGE_TITLE);
			channelimagelink = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_CHANNEL_IMAGE_LINK);
			channelimageurl = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_CHANNEL_IMAGE_URL);
			channelimagedescription = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_CHANNEL_IMAGE_DESCRIPTION);
			channellanguage = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_CHANNEL_LANGUAGE);
			channelauthor = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_CHANNEL_AUTHOR);
			version = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_VERSION);
			encoding = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_ENCODING);
			addimage = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_ADDIMAGE);
			itemtitle = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_ITEM_TITLE);
			itemdescription = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_ITEM_DESCRIPTION);
			itemlink = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_ITEM_LINK);
			itempubdate = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_ITEM_PUBDATE);
			itemauthor = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_ITEM_AUTHOR);
			addgeorss = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_ADDGEORSS);
			usegeorssgml = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_USEGEORSSGML);
			geopointlat = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_GEOPOINTLAT);
			geopointlong = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_GEOPOINTLONG);
			filenamefield = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_FILENAME_FIELD);
			fileName = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_FILE_NAME);
			extension = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_FILE_EXTENTION);
			stepNrInFilename = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_FILE_ADD_STEPNR);
			partNrInFilename = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_FILE_ADD_PARTNR);
			dateInFilename = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_FILE_ADD_DATE);
			timeInFilename = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_FILE_ADD_TIME);
			isfilenameinfield = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_IS_FILENAME_IN_FIELD);
			createparentfolder = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_CREATE_PARENT_FOLDER);
			AddToResult = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_ADDTORESULT);
			
			// Channel Custom
			int nrchannel   = rep.countNrStepAttributes(id_step, "channel_custom_fields"); //$NON-NLS-1$
			
			allocate(nrchannel);
			for (int i=0;i<nrchannel;i++)
			{
				ChannelCustomTags[i]  = rep.getStepAttributeString(id_step, i, "channel_custom_tag"); //$NON-NLS-1$
				ChannelCustomFields[i]  = rep.getStepAttributeString(id_step, i, "channel_custom_field"); //$NON-NLS-1$	
			}
			// Item Custom
			int nritem   = rep.countNrStepAttributes(id_step, "item_custom_fields"); //$NON-NLS-1$
			allocateitem(nritem);
			for (int i=0;i<nritem;i++)
			{
				ItemCustomTags[i]  = rep.getStepAttributeString(id_step, i, "item_custom_tag"); //$NON-NLS-1$
				ItemCustomFields[i]  = rep.getStepAttributeString(id_step, i, "item_custom_field"); //$NON-NLS-1$	
			}
			
			// Namespaces
			int nrnamespaces   = rep.countNrStepAttributes(id_step, "namespace_tag"); //$NON-NLS-1$
			allocatenamespace(nrnamespaces);

			for (int i=0;i<nrnamespaces;i++)
			{
				NameSpacesTitle[i]  = rep.getStepAttributeString(id_step, i, "namespace_tag"); //$NON-NLS-1$	
				NameSpaces[i]  = rep.getStepAttributeString(id_step, i, "namespace_value"); //$NON-NLS-1$	
			}
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
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_DISPLAYITEM,displayitem);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_CUSTOMRSS,customrss);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_CHANNEL_TITLE,channeltitle);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_CHANNEL_DESCRIPTION,channeldescription);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_CHANNEL_LINK,channellink);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_CHANNEL_PUBDATE,channelpubdate);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_CHANNEL_COPYRIGHT,channelcopyright);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_CHANNEL_IMAGE_TITLE,channelimagetitle);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_CHANNEL_IMAGE_LINK,channelimagelink);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_CHANNEL_IMAGE_URL,channelimageurl);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_CHANNEL_IMAGE_DESCRIPTION ,channelimagedescription);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_CHANNEL_LANGUAGE,channelauthor);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_CHANNEL_AUTHOR,channellanguage);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_VERSION,version);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_ENCODING,encoding);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_ADDIMAGE,addimage);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_ITEM_TITLE,itemtitle);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_ITEM_DESCRIPTION,itemdescription);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_ITEM_LINK,itemlink);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_ITEM_PUBDATE,itempubdate);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_ITEM_AUTHOR,itemauthor);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_ADDGEORSS,addgeorss);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_USEGEORSSGML,usegeorssgml);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_GEOPOINTLAT,geopointlat);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_GEOPOINTLONG,geopointlong);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_FILENAME_FIELD,filenamefield);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_FILE_NAME,fileName);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_FILE_EXTENTION, extension);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_FILE_ADD_STEPNR,stepNrInFilename);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_FILE_ADD_PARTNR,partNrInFilename);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_FILE_ADD_DATE,dateInFilename);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_FILE_ADD_TIME,timeInFilename);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_IS_FILENAME_IN_FIELD,isfilenameinfield);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_CREATE_PARENT_FOLDER,createparentfolder);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_ADDTORESULT,AddToResult);
			
			for (int i=0;i<ChannelCustomFields.length;i++)
			{
				rep.saveStepAttribute(id_transformation, id_step, i, "channel_custom_field",   ChannelCustomFields[i]); //$NON-NLS-1$
				rep.saveStepAttribute(id_transformation, id_step, i, "channel_custom_tag",      ChannelCustomTags[i]); //$NON-NLS-1$	
			}
			for (int i=0;i<ItemCustomFields.length;i++)
			{
				rep.saveStepAttribute(id_transformation, id_step, i, "item_custom_field",   ItemCustomFields[i]); //$NON-NLS-1$
				rep.saveStepAttribute(id_transformation, id_step, i, "item_custom_tag",      ItemCustomTags[i]); //$NON-NLS-1$	
			}
			
			for (int i=0;i<NameSpaces.length;i++)
			{
				rep.saveStepAttribute(id_transformation, id_step, i, "namespace_tag",   NameSpacesTitle[i]); //$NON-NLS-1$
				rep.saveStepAttribute(id_transformation, id_step, i, "namespace_value",   NameSpaces[i]); //$NON-NLS-1$
			}
			
		}
		catch(Exception e)
		{
			throw new KettleException("Unable to save step information to the repository for id_step="+id_step, e);
		}
	}

	 public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	 {
		
		CheckResult cr;

		//String error_message = "";
		//boolean error_found = false;
		// OK, we have the table fields.
		// Now see what we can find as previous step...
		if (prev!=null && prev.size()>0)
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("RssOutputMeta.CheckResult.FieldsReceived", ""+prev.size()), stepMeta);
			remarks.add(cr);

			// Starting from prev...
			/*for (int i=0;i<prev.size();i++)
			{
				Value pv = prev.getValue(i);
				int idx = r.searchValueIndex(pv.getName());
				if (idx<0) 
				{
					error_message+="\t\t"+pv.getName()+" ("+pv.getTypeDesc()+")"+Const.CR;
					error_found=true;
				} 
			}
			if (error_found) 
			{
				error_message=Messages.getString("RssOutputMeta.CheckResult.FieldsNotFoundInOutput", error_message);

				cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, error_message, stepMeta);
				remarks.add(cr);
			}
			else
			{
				cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("RssOutputMeta.CheckResult.AllFieldsFoundInOutput"), stepMeta);
				remarks.add(cr);
			}*/

			// Starting from table fields in r...
			/*for (int i=0;i<r.size();i++)
			{
				Value rv = r.getValue(i);
				int idx = prev.searchValueIndex(rv.getName());
				if (idx<0) 
				{
					error_message+="\t\t"+rv.getName()+" ("+rv.getTypeDesc()+")"+Const.CR;
					error_found=true;
				} 
			}
			if (error_found) 
			{
				error_message=Messages.getString("RssOutputMeta.CheckResult.FieldsNotFound", error_message);

				cr = new CheckResult(CheckResult.TYPE_RESULT_WARNING, error_message, stepMeta);
				remarks.add(cr);
			}
			else
			{
				cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("RssOutputMeta.CheckResult.AllFieldsFound"), stepMeta);
				remarks.add(cr);
			}*/
		}
		else
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("RssOutputMeta.CheckResult.NoFields"), stepMeta);
			remarks.add(cr);
		}
				
			
		
		// See if we have input streams leading to this step!
		if (input.length>0)
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("RssOutputMeta.CheckResult.ExpectedInputOk"), stepMeta);
			remarks.add(cr);
		}
		else
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("RssOutputMeta.CheckResult.ExpectedInputError"), stepMeta);
			remarks.add(cr);
		}
	}

	
	public StepDataInterface getStepData()
	{
		return new RssOutputData();
	}

    /**
     * @return the channeltitle
     */
    public String getChannelTitle()
    {
        return channeltitle;
    }
    
    /**
     * @return the channeldescription
     */
    public String getChannelDescription()
    {
        return channeldescription;
    }
    
    /**
     * @return the channellink
     */
    public String getChannelLink()
    {
        return channellink;
    }
       
    /**
     * @return the channelpubdate
     */
    public String getChannelPubDate()
    {
        return channelpubdate;
    }
    
    /**
     * @return the channelimagelink
     */
    public String getChannelImageLink()
    {
        return channelimagelink;
    }
    
    /**
     * @return the channelimageurl
     */
    public String getChannelImageUrl()
    {
        return channelimageurl;
    }
    
    /**
     * @return the channelimagedescription
     */
    public String getChannelImageDescription()
    {
        return channelimagedescription;
    }
    
    /**
     * @return the channelimagetitle
     */
    public String getChannelImageTitle()
    {
        return channelimagetitle;
    }
    
    /**
     * @return the channellanguage
     */
    public String getChannelLanguage()
    {
        return channellanguage;
    }
    
    /**
     * @return the channelauthor
     */
    public String getChannelAuthor()
    {
        return channelauthor;
    }
    
    /**
     * @param channelauthor the channelauthor to set
     */
    public void setChannelAuthor(String channelauthor)
    {
        this.channelauthor = channelauthor;
    }
    
    /**
     * @param channeltitle the channeltitle to set
     */
    public void setChannelTitle(String channeltitle)
    {
        this.channeltitle = channeltitle;
    }
    
    /**
     * @param channellink the channellink to set
     */
    public void setChannelLink(String channellink)
    {
        this.channellink = channellink;
    }
    
    /**
     * @param channelpubdate the channelpubdate to set
     */
    public void setChannelPubDate(String channelpubdate)
    {
        this.channelpubdate = channelpubdate;
    }
    
    /**
     * @param channelimagetitle the channelimagetitle to set
     */
    public void setChannelImageTitle(String channelimagetitle)
    {
        this.channelimagetitle = channelimagetitle;
    }
    
    /**
     * @param channelimagelink the channelimagelink to set
     */
    public void setChannelImageLink(String channelimagelink)
    {
        this.channelimagelink = channelimagelink;
    }
    /**
     * @param channelimageurl the channelimageurl to set
     */
    public void setChannelImageUrl(String channelimageurl)
    {
        this.channelimageurl = channelimageurl;
    }
    
    /**
     * @param channelimagedescription the channelimagedescription to set
     */
    public void setChannelImageDescription(String channelimagedescription)
    {
        this.channelimagedescription = channelimagedescription;
    }

    /**
     * @param channellanguage the channellanguage to set
     */
    public void setChannelLanguage(String channellanguage)
    {
        this.channellanguage = channellanguage;
    }

    /**
     * @param channeldescription the channeldescription to set
     */
    public void setChannelDescription(String channeldescription)
    {
        this.channeldescription = channeldescription;
    }
    
    /**
     * @return the itemtitle
     */
    public String getItemTitle()
    {
        return itemtitle;
    }
    
    /**
     * @return the geopointlat
     */
    public String getGeoPointLat()
    {
        return geopointlat;
    }
    
    /**
     * @param geopointlat the geopointlat to set
     */
    public void setGeoPointLat(String geopointlat)
    {
        this.geopointlat = geopointlat;
    }
    
    /**
     * @return the geopointlong
     */
    public String getGeoPointLong()
    {
        return geopointlong;
    }
    
    /**
     * @param geopointlong the geopointlong to set
     */
    public void setGeoPointLong(String geopointlong)
    {
        this.geopointlong = geopointlong;
    }
    
    /**
     * @return the itemdescription
     */
    public String getItemDescription()
    {
        return itemdescription;
    }
    
    /**
     * @return the itemlink
     */
    public String getItemLink()
    {
        return itemlink;
    }
    
    /**
     * @return the itempubdate
     */
    public String getItemPubDate()
    {
        return itempubdate;
    }
    
    /**
     * @return the itemauthor
     */
    public String getItemAuthor()
    {
        return itemauthor;
    }

    /**
     * @param itemtitle the itemtitle to set
     */
    public void setItemTitle(String itemtitle)
    {
        this.itemtitle = itemtitle;
    }
    
    /**
     * @param itemdescription the itemdescription to set
     */
    public void setItemDescription(String itemdescription)
    {
        this.itemdescription = itemdescription;
    }
    
    /**
     * @param itemlink the itemlink to set
     */
    public void setItemLink(String itemlink)
    {
        this.itemlink = itemlink;
    }
    
    /**
     * @param itempubdate the itempubdate to set
     */
    public void setItemPubDate(String itempubdate)
    {
        this.itempubdate = itempubdate;
    }
    
    /**
     * @param itemauthor the itemauthor to set
     */
    public void setItemAuthor(String itemauthor)
    {
        this.itemauthor = itemauthor;
    }

    /**
     * @return channelcopyrightt
     */
    public String getChannelCopyright()
    {
        return  channelcopyright;
    }
    
    /**
     * @param channelcopyright the channelcopyright to set
     */
    public void setChannelCopyright(String channelcopyright)
    {
        this.channelcopyright = channelcopyright;
    }

    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta tr, Trans trans)
	{
		return new RssOutput(stepMeta, stepDataInterface, cnr, tr, trans);
	}

}
