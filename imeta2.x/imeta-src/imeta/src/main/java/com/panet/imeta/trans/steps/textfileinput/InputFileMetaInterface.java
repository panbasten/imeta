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
package com.panet.imeta.trans.steps.textfileinput;

import com.panet.imeta.core.variables.VariableSpace;
import com.panet.imeta.trans.step.StepMetaInterface;

public interface InputFileMetaInterface extends StepMetaInterface {

	public TextFileInputField[] getInputFields();

	public int getFileFormatTypeNr();
	
	public boolean hasHeader();
	
	public int getNrHeaderLines();
	
	public String[] getFilePaths(VariableSpace space);

	public boolean isErrorIgnored();
	public String getErrorCountField();
	public String getErrorFieldsField();
	public String getErrorTextField();
	public String getFileType();
	public String getEnclosure();
 	public String getEscapeCharacter();
	public String getSeparator();
	public boolean isErrorLineSkipped();
	public boolean includeFilename();
	public boolean includeRowNumber();
}
