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
 
package com.panet.imeta.trans.steps.excelinput;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.vfs.FileObject;

import com.panet.imeta.core.fileinput.FileInputList;
import com.panet.imeta.core.playlist.FilePlayList;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.core.row.ValueMetaInterface;
import com.panet.imeta.trans.step.BaseStepData;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.errorhandling.FileErrorHandler;



/**
 * @author Matt
 * @since 24-jan-2005
 */
public class ExcelInputData extends BaseStepData implements StepDataInterface
{
    /**
     * The previous row in case we want to repeat values...
     */
    public Object[] previousRow;

	/**
	 * The maximum length of all filenames...
	 */
	public int maxfilelength;

	/**
	 * The maximum length of all sheets...
	 */
	public int maxsheetlength;

	/**
	 * The Excel files to read
	 */
	public FileInputList files;

	/**
	 * The file number that's being handled...
	 */
	public int filenr;

	public String filename;

	public FileObject file;

	/**
	 * The openFile that's being processed...
	 */
	public Workbook workbook;

	/**
	 * The sheet number that's being processed...
	 */
	public int sheetnr;

	/**
	 * The sheet that's being processed...
	 */
	public Sheet sheet;

	/**
	 * The row where we left off the previous time...
	 */
	public int rownr;

	/**
	 * The column where we left off previous time...
	 */
	public int colnr;

	/**
	 * The error handler when processing of a row fails.
	 */
	public FileErrorHandler errorHandler;

	public FilePlayList filePlayList;

    public RowMetaInterface outputRowMeta;
    
    ValueMetaInterface valueMetaString;
    ValueMetaInterface valueMetaNumber;
    ValueMetaInterface valueMetaDate;
    ValueMetaInterface valueMetaBoolean;

    public RowMetaInterface conversionRowMeta;

	public String[] sheetNames;
	public int[]    startColumn;
	public int[]    startRow;

	public int      defaultStartColumn;
	public int      defaultStartRow;
	
	/**
	 * 
	 */
	public ExcelInputData()
	{
		super();
		workbook=null;
		filenr=0;
		sheetnr=0;
		rownr=-1;
		colnr=-1;
        
        valueMetaString  = new ValueMeta("v", ValueMetaInterface.TYPE_STRING); 
        valueMetaNumber  = new ValueMeta("v", ValueMetaInterface.TYPE_NUMBER); 
        valueMetaDate    = new ValueMeta("v", ValueMetaInterface.TYPE_DATE); 
        valueMetaBoolean = new ValueMeta("v", ValueMetaInterface.TYPE_BOOLEAN); 
	}
}
