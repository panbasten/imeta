/*
 * Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Samatar HASSAN.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
*/
package com.panet.imeta.trans.steps.stringcut;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.trans.step.BaseStepData;
import com.panet.imeta.trans.step.StepDataInterface;


/**
 * @author Samatar Hassan
 * @since 30 September 2008
 */
public class StringCutData extends BaseStepData implements StepDataInterface {
	
	public int inStreamNrs[]; 

	public String outStreamNrs[];
	
	public int cutFrom[];
	
	public int cutTo[];
	
	public RowMetaInterface outputRowMeta;
	
	public int inputFieldsNr;
	
	/**
	 * Default constructor.
	 */
	public StringCutData() {
		super();
		inputFieldsNr=0;
	}
}
