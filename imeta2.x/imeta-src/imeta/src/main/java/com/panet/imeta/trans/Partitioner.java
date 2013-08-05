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
package com.panet.imeta.trans;

import org.w3c.dom.Node;

import com.panet.imeta.core.exception.KettleDatabaseException;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.trans.step.StepPartitioningMeta;

public interface Partitioner {

	public abstract Partitioner getInstance();
	
	public int getPartition(RowMetaInterface rowMeta, Object[] r ) throws KettleException;
	
	public void setMeta(StepPartitioningMeta meta);

	public String getId( );
	
	public String getDescription( );

	public void setId( String id );
	
	public void setDescription( String description );
	
	public String getDialogClassName();
	
	public Partitioner clone();
	
    public String getXML();
    
	public void loadXML(Node partitioningMethodNode) throws KettleXMLException;

    /**
     * Saves partitioning properties in the repository for the given step.
     * @param rep the repository to save in
     * @param id_transformation the ID of the transformation
     * @param id_step the ID of the step
     * @throws KettleDatabaseException In case anything goes wrong
     */
    public void saveRep(Repository rep, long id_transformation, long id_step) throws KettleException;

    public void loadRep(Repository rep, long id_step) throws KettleException;

}
