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

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

import com.panet.imeta.core.database.Database;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.trans.step.BaseStepData;
import com.panet.imeta.trans.step.StepDataInterface;


/**
 * @author Matt
 * @since 24-jan-2005
 */
public class SQLFileOutputData extends BaseStepData implements StepDataInterface
{
	public int splitnr;
	
	public  Database db;

	public  String tableName;
	
    public OutputStreamWriter writer;
    
    public OutputStream fos;
    public RowMetaInterface outputRowMeta;
    
    /** Cache of the data formatter object */
    public SimpleDateFormat dateFormater;
    
    public boolean sendToErrorRow;
    
    public RowMetaInterface insertRowMeta;
    
	public SQLFileOutputData()
	{
		super();
		
		db=null;
		tableName=null;

	}

}
