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
package com.panet.imeta.resource;

public interface ResourceHolderInterface {
  
  /**
   * @return The name of the holder of the resource
   */
  public String getName();
  /**
   * @return The description of the holder of the resource
   */
  public String getDescription();
  /**
   * @return The ID of the holder of the resource
   */
  public long getID();
  /**
   * @return The Type ID of the resource holder. The Type ID
   * is the system-defined type identifier (like TRANS or SORT).
   */
  public String getTypeId();
  
  /**
   * Gets the high-level type of resource holder. 
   * @return JOBENTRY, STEP, etc.
   */
  public String getHolderType();
  
}
