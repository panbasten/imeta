package com.plywet.imeta.core.vfs.configuration;

import java.io.IOException;

import org.apache.commons.vfs.FileSystemOptions;

/**
 * 
 * @author 潘巍（Peter Pan）
 * @since 2010-9-19 下午02:58:57
 */
public interface IFileSystemConfigBuilder {

	/**
	 * Extract the FileSystemOptions parameter name from a Kettle variable
	 * 
	 * @param parameter
	 * @return
	 */
	public String parseParameterName(String parameter, String scheme);

	/**
	 * Publicly expose a generic way to set parameters
	 */
	public void setParameter(FileSystemOptions opts, String name, String value,
			String fullParameterName, String vfsUrl) throws IOException;
}
