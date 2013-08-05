/*
 * Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
*/
 package com.panet.imeta.trans.steps.sql;

import java.util.List;

import com.panet.imeta.core.Result;
import com.panet.imeta.core.database.Database;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.trans.step.BaseStepData;
import com.panet.imeta.trans.step.StepDataInterface;




/**
 * @author Matt
 * @since 20-jan-2005
 */
public class ExecSQLData extends BaseStepData implements StepDataInterface
{
	public Database db;
    public Result   result;
    public int[]    argumentIndexes;
    public List<Integer>     markerPositions;
    public RowMetaInterface outputRowMeta;
    public String   sql;
	public boolean isCanceled;
	
	public ExecSQLData()
	{
		super();
		
		db              = null;
        result          = null;
        argumentIndexes = null;
        markerPositions = null;
	}
}
