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

package com.panet.imeta.trans.steps.xsdvalidator;

import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.trans.step.BaseStepData;
import com.panet.imeta.trans.step.StepDataInterface;

/**
 * @author Samatar
 * @since 14-08-2007
 *
 */
public class XsdValidatorData extends BaseStepData implements StepDataInterface
{
	public int xmlindex;
	public int xsdindex;
	public RowMetaInterface outputRowMeta;
	
	/**
	 * 
	 */
	public XsdValidatorData()
	{
		super();
		xmlindex=-1;
		xsdindex=-1;
		
	}

}
