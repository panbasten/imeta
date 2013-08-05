package com.plywet.imeta.core.vfs.configuration;

import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.commons.vfs.FileSystemConfigBuilder;

import com.plywet.imeta.core.variables.VariableSpace;
import com.plywet.imeta.core.vfs.VFS;
import com.plywet.imeta.i18n.BaseMessages;

/**
 * This class supports overriding of config builders by supplying a
 * VariableSpace containing a variable in the format of
 * vfs.[scheme].config.parser where [scheme] is one of the VFS schemes (file,
 * http, sftp, etc...)
 * 
 * @author cboyden
 */
public class FileSystemConfigBuilderFactory {

	private static Class<?> PKG = VFS.class; // for i18n purposes, needed by

	// Translator2!! $NON-NLS-1$

	/**
	 * This factory returns a FileSystemConfigBuilder. Custom
	 * FileSystemConfigBuilders can be created by implementing the
	 * {@link IKettleFileSystemConfigBuilder} or overriding the
	 * {@link KettleGenericFileSystemConfigBuilder}
	 * 
	 * @see FileSystemConfigBuilder
	 * 
	 * @param varSpace
	 *            A Kettle variable space for resolving VFS config parameters
	 * @param scheme
	 *            The VFS scheme (FILE, HTTP, SFTP, etc...)
	 * @return A FileSystemConfigBuilder that can translate Kettle variables
	 *         into VFS config parameters
	 * @throws IOException
	 */
	public static IFileSystemConfigBuilder getConfigBuilder(
			VariableSpace varSpace, String scheme) throws IOException {
		IFileSystemConfigBuilder result = null;

		// Attempt to load the Config Builder from a variable: vfs.config.parser
		// = class
		String parserClass = varSpace
				.getVariable("vfs." + scheme + ".config.parser"); //$NON-NLS-1$ //$NON-NLS-2$

		if (parserClass != null) {
			try {
				Class<?> configBuilderClass = FileSystemConfigBuilderFactory.class
						.getClassLoader().loadClass(parserClass);
				Method mGetInstance = configBuilderClass
						.getMethod("getInstance"); //$NON-NLS-1$
				if ((mGetInstance != null)
						&& (IFileSystemConfigBuilder.class
								.isAssignableFrom(mGetInstance.getReturnType()))) {
					result = (IFileSystemConfigBuilder) mGetInstance
							.invoke(null);
				} else {
					result = (IFileSystemConfigBuilder) configBuilderClass
							.newInstance();
				}
			} catch (Exception e) {
				// Failed to load custom parser. Throw exception.
				throw new IOException(BaseMessages.getString(PKG,
						"CustomVfsSettingsParser.Log.FailedToLoad")); //$NON-NLS-1$
			}
		} else {
			// No custom parser requested, load default
			if (scheme.equalsIgnoreCase("sftp")) { //$NON-NLS-1$
				result = SftpFileSystemConfigBuilder.getInstance();
			} else {
				result = GenericFileSystemConfigBuilder.getInstance();
			}
		}

		return result;
	}

}
