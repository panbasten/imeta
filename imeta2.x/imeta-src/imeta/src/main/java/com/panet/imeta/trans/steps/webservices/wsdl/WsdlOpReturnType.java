/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Header:$
 */
package com.panet.imeta.trans.steps.webservices.wsdl;

import javax.xml.namespace.QName;

/**
 * Represents the return value for a WSDL operation.
 */
public class WsdlOpReturnType implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	protected QName _xmlType;

	protected boolean _isArray;

	// The xmlType of an array's items
	protected QName _itemXmlType;

	protected ComplexType _itemComplexType;

	/**
	 * Constructor.
	 */
	protected WsdlOpReturnType() {
	}

	/**
	 * Get the Xml type.
	 * 
	 * @return QName for the XML type.
	 */
	public QName getXmlType() {
		return _xmlType;
	}

	/**
	 * If the return type is an array, get the xml type of the items in the
	 * array.
	 * 
	 * @return QName for the item XML type, null if not an array.
	 */
	public QName getItemXmlType() {
		return _itemXmlType;
	}

	/**
	 * Is this an array type?
	 * 
	 * @return true if this is an array type.
	 */
	public boolean isArray() {
		return _isArray;
	}

	public ComplexType getItemComplexType() {
		return _itemComplexType;
	}
}
