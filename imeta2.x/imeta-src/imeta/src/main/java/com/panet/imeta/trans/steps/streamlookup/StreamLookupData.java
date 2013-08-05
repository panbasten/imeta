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
 

package com.panet.imeta.trans.steps.streamlookup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.panet.imeta.core.RowMetaAndData;
import com.panet.imeta.core.exception.KettleValueException;
import com.panet.imeta.core.hash.ByteArrayHashIndex;
import com.panet.imeta.core.hash.LongHashIndex;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.trans.step.BaseStepData;
import com.panet.imeta.trans.step.StepDataInterface;




/**
 * @author Matt
 * @since 24-jan-2005
 */
public class StreamLookupData extends BaseStepData implements StepDataInterface
{
    /** used to store values in used to look up things */
	public Map<RowMetaAndData, Object[]> look;
    
    public List<KeyValue> list;
	
	/** nrs of keys-values in row. */
	public int    keynrs[];
	
    /** The metadata we send out */
    public RowMetaInterface outputRowMeta;
    
	/**default string converted to values...*/
	public Object nullIf[];
	
	/** Flag to indicate that we have to read lookup values from the info step */
	public boolean readLookupValues;

    /** Stores the first row of the lookup-values to later determine if the types are the same as the input row lookup values.*/
    public RowMetaInterface keyTypes;

    public RowMetaInterface keyMeta;

    public RowMetaInterface valueMeta;

    public Comparator<KeyValue> comparator;

    public ByteArrayHashIndex hashIndex;
    public LongHashIndex longIndex;

    public RowMetaInterface lookupMeta;

    public RowMetaInterface infoMeta;
   
	public int[] lookupColumnIndex;

	public boolean metadataVerifiedIntegerPair;
	
	/** See if we need to convert the keys to a native data type */
	public boolean[] convertKeysToNative;
	
	// Did we read rows from the lookup hop.
	public boolean hasLookupRows;
	
	public StreamLookupData()
	{
        super();
        look = new HashMap<RowMetaAndData, Object[]>();
        hashIndex = null;
        longIndex = new LongHashIndex();
        list = new ArrayList<KeyValue>();
        metadataVerifiedIntegerPair=false;
        hasLookupRows=false;
        
        comparator = new Comparator<KeyValue>()
        {
            public int compare(KeyValue k1, KeyValue k2)
            {
                try
                {
                    return keyMeta.compare(k1.getKey(), k2.getKey());
                }
                catch(KettleValueException e)
                {
                    LogWriter.getInstance().logError("Stream Lookup comparator", e.getMessage());
                    return 0;
                }
            }
        };
	}

}
