package com.plywet.imeta.repository;

import com.plywet.imeta.core.exception.ImetaException;

/**
 * This interface allows you to pass a simple interface to an object to allow it
 * to store or load itself from or to any type of repository in a generic
 * fashion.
 * 
 * @author matt
 * 
 */
public interface RepositoryAttributeInterface {

	/**
	 * Set a String attribute
	 * 
	 * @param code
	 * @param value
	 * @throws SinoException
	 */
	public void setAttribute(String code, String value) throws ImetaException;

	/**
	 * Get a string attribute. If the attribute is not found, return null
	 * 
	 * @param code
	 * @return
	 * @throws SinoException
	 */
	public String getAttributeString(String code) throws ImetaException;

	/**
	 * Set a boolean attribute
	 * 
	 * @param code
	 * @param value
	 * @throws SinoException
	 */
	public void setAttribute(String code, boolean value) throws ImetaException;

	/**
	 * Get a boolean attribute, if the attribute is not found, return false;
	 * 
	 * @param code
	 * @return
	 * @throws SinoException
	 */
	public boolean getAttributeBoolean(String code) throws ImetaException;

	/**
	 * Set an integer attribute
	 * 
	 * @param code
	 * @param value
	 * @throws SinoException
	 */
	public void setAttribute(String code, long value) throws ImetaException;

	/**
	 * Get an integer attribute. If the attribute is not found, return 0;
	 * 
	 * @param code
	 * @return
	 * @throws SinoException
	 */
	public long getAttributeInteger(String code) throws ImetaException;
}
