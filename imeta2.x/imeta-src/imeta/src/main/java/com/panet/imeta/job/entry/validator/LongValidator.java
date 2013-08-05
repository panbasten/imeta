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
package com.panet.imeta.job.entry.validator;

import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.addFailureRemark;
import static com.panet.imeta.job.entry.validator.JobEntryValidatorUtils.getLevelOnFail;
import static org.apache.commons.validator.util.ValidatorUtils.getValueAsString;

import java.util.List;

import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;

import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.CheckResultSourceInterface;

public class LongValidator implements JobEntryValidator
{

  public static final LongValidator INSTANCE = new LongValidator();

  private String VALIDATOR_NAME = "long"; //$NON-NLS-1$

  public String getName()
  {
    return VALIDATOR_NAME;
  }

  public boolean validate(CheckResultSourceInterface source, String propertyName, List<CheckResultInterface> remarks,
      ValidatorContext context)
  {
    Object result = null;
    String value = null;

    value = getValueAsString(source, propertyName);

    if (GenericValidator.isBlankOrNull(value))
    {
      return Boolean.TRUE;
    }

    result = GenericTypeValidator.formatLong(value);

    if (result == null)
    {
      addFailureRemark(source, propertyName, VALIDATOR_NAME, remarks, getLevelOnFail(context, VALIDATOR_NAME));
      return false;
    }
    return true;
  }

}
