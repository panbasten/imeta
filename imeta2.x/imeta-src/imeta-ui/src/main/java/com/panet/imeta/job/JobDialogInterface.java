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

package com.panet.imeta.job;

import com.panet.imeta.core.BaseDialogInterface;
import com.panet.imeta.repository.Repository;

/**
 * This interface is used to launch Step Dialogs. All dialogs that implement
 * this simple interface can be opened by Spoon.
 * 
 * @author Matt
 * @since 4-aug-2004
 * 
 */
public interface JobDialogInterface extends BaseDialogInterface {

	public void setRepository(Repository repository);
}