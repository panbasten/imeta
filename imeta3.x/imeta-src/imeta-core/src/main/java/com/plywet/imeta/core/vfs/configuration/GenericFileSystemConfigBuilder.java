package com.plywet.imeta.core.vfs.configuration;

import java.io.IOException;

import org.apache.commons.vfs.FileSystemConfigBuilder;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.util.DelegatingFileSystemOptionsBuilder;

import com.plywet.imeta.core.log.Log;
import com.plywet.imeta.core.vfs.VFS;

/**
 * A generic FileSystemConfigBuilder that inserts parameters and values as
 * literally specified.
 * 
 * Note: ALL parameters are case sensitive! Please see individual
 * FileSystemConfigBuilder for list of available parameters. Please also see
 * Sino FileSystemConfigBuilder overrides for additional parameters.
 * 
 * @see SinoSftpFileSystemConfigBuilder
 * 
 * @author cboyden
 */
public class GenericFileSystemConfigBuilder extends FileSystemConfigBuilder
		implements IFileSystemConfigBuilder {

	private static final Log log = Log
			.getLog(GenericFileSystemConfigBuilder.class.getName());

	private final static GenericFileSystemConfigBuilder builder = new GenericFileSystemConfigBuilder();

	public String parseParameterName(String parameter, String scheme) {
		String result = null;

		// Frame the parameter name
		int begin = parameter.indexOf(".", parameter.indexOf(".") + 1) + 1; // Get the index of the second "." (vfs.scheme.parameter)  //$NON-NLS-1$//$NON-NLS-2$
		int end = -1;

		end = parameter.indexOf('.', begin);

		if (end < 0) {
			end = parameter.length();
		}

		if (end > begin) {
			result = parameter.substring(begin, end);
		}

		return result;
	}

	public static GenericFileSystemConfigBuilder getInstance() {
		return builder;
	}

	/**
	 * Extract the scheme from a Sino VFS configuration paramter
	 * (vfs.scheme.parameter)
	 * 
	 * @param fullParameterName
	 *            A VFS configuration parameter in the form of
	 *            'vfs.scheme.parameter'
	 */
	public static String extractScheme(String fullParameterName)
			throws IllegalArgumentException {
		String result = null;

		// Verify that this is a Sino VFS configuration parameter
		if ((fullParameterName != null) && (fullParameterName.length() > 4)
				&& (fullParameterName.startsWith("vfs."))) { //$NON-NLS-1$
			int schemeEnd = fullParameterName.indexOf(".", 4); //$NON-NLS-1$
			if (schemeEnd > 4) {
				result = fullParameterName.substring(4, schemeEnd);
			} else {
				throw new IllegalArgumentException(
						"The configuration parameter does not match a valid scheme: " + fullParameterName); //$NON-NLS-1$
			}
		} else {
			throw new IllegalArgumentException(
					"The configuration parameter does not match a valid scheme: " + fullParameterName); //$NON-NLS-1$
		}

		return result;
	}

	protected GenericFileSystemConfigBuilder() {
		super();
	}

	@Override
	protected Class<?> getConfigClass() {
		return GenericFileSystemConfigBuilder.class;
	}

	public void setParameter(FileSystemOptions opts, String name, String value,
			String fullParameterName, String vfsUrl) throws IOException {
		// Use the DelgatingFileSystemOptionsBuilder to insert generic
		// parameters
		// This must be done to assure the correct VFS FileSystem drivers will
		// process the parameters
		String scheme = extractScheme(fullParameterName);
		try {
			DelegatingFileSystemOptionsBuilder delegateFSOptionsBuilder = new DelegatingFileSystemOptionsBuilder(
					VFS.getInstance().getFileSystemManager());
			if (scheme != null) {
				delegateFSOptionsBuilder.setConfigString(opts, scheme, name,
						value);
			} else {
				log
						.warn("Warning: Cannot process VFS parameters if no scheme is specified: " + vfsUrl); //$NON-NLS-1$
			}
		} catch (FileSystemException e) {
			if (e.getCode().equalsIgnoreCase(
					"vfs.provider/config-key-invalid.error")) { //$NON-NLS-1$
				// This key is not supported by the default scheme config
				// builder. This may be a custom key of another config builder
				log
						.warn("Warning: The configuration parameter [" + name + "] is not supported by the default configuration builder for scheme: " + scheme); //$NON-NLS-1$//$NON-NLS-2$
			} else {
				// An unexpected error has occurred loading in parameters
				throw new IOException(e.getLocalizedMessage());
			}
		}
	}
}
