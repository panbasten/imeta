package com.plywet.imeta.core.vfs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Comparator;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.cache.WeakRefFilesCache;
import org.apache.commons.vfs.impl.DefaultFileSystemManager;
import org.apache.commons.vfs.impl.StandardFileSystemManager;
import org.apache.commons.vfs.provider.local.LocalFile;

import com.plywet.imeta.core.exception.ImetaFileException;
import com.plywet.imeta.core.variables.VariableSpace;
import com.plywet.imeta.core.variables.Variables;
import com.plywet.imeta.core.vfs.configuration.FileSystemConfigBuilderFactory;
import com.plywet.imeta.core.vfs.configuration.IFileSystemConfigBuilder;
import com.plywet.imeta.i18n.BaseMessages;
import com.plywet.imeta.utils.Const;
import com.plywet.imeta.utils.UUIDUtil;

/**
 * 
 * @author 潘巍（Peter Pan）
 * @since 2010-9-19 下午02:58:24
 */
public class VFS {

	private static final VFS vfs = new VFS();

	private static Class<?> PKG = VFS.class;

	private final DefaultFileSystemManager fsm;

	private static VariableSpace defaultVariableSpace;

	static {
		// 创建新的空参数空间
		defaultVariableSpace = new Variables();
		defaultVariableSpace.initializeVariablesFrom(null);
	}

	private VFS() {
		fsm = new StandardFileSystemManager();
		try {
			fsm.setFilesCache(new WeakRefFilesCache());
			fsm.init();
		} catch (FileSystemException e) {
			e.printStackTrace();
		}

		// 安装shutdown hook，确保文件系统管理器被关闭
		// 清除vfs_cache中的临时文件
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				if (fsm != null) {
					fsm.close();
				}
			}
		}));
	}

	public FileSystemManager getFileSystemManager() {
		return fsm;
	}

	public static VFS getInstance() {
		return vfs;
	}

	/**
	 * 获得文件对象
	 * 
	 * @param vfsFilename
	 *            文件名称
	 * @return
	 * @throws ImetaFileException
	 */
	public static FileObject getFileObject(String vfsFilename)
			throws ImetaFileException {
		return getFileObject(vfsFilename, defaultVariableSpace);
	}

	/**
	 * 获得文件对象
	 * 
	 * @param vfsFilename
	 *            文件名称
	 * @param space
	 *            变量空间
	 * @return
	 * @throws ImetaFileException
	 */
	public static FileObject getFileObject(String vfsFilename,
			VariableSpace space) throws ImetaFileException {
		return getFileObject(vfsFilename, space, null);
	}

	/**
	 * 获得文件对象
	 * 
	 * @param vfsFilename
	 *            文件名称
	 * @param space
	 *            变量空间
	 * @param fsOptions
	 *            fs选项
	 * @return
	 * @throws ImetaFileException
	 */
	public static FileObject getFileObject(String vfsFilename,
			VariableSpace space, FileSystemOptions fsOptions)
			throws ImetaFileException {
		try {
			FileSystemManager fsManager = getInstance().getFileSystemManager();

			// We have one problem with VFS: if the file is in a subdirectory of
			// the current one: somedir/somefile
			// In that case, VFS doesn't parse the file correctly.
			// We need to put file: in front of it to make it work.
			// However, how are we going to verify this?
			// 
			// We are going to see if the filename starts with one of the known
			// protocols like file: zip: ram: smb: jar: etc.
			// If not, we are going to assume it's a file.
			// 关于VFS有个问题
			boolean relativeFilename = true;
			String[] schemes = fsManager.getSchemes();
			for (int i = 0; i < schemes.length && relativeFilename; i++) {
				if (vfsFilename.startsWith(schemes[i] + ":")) {
					relativeFilename = false;
					// We have a VFS URL, load any options for the file system
					// driver
					fsOptions = buildFsOptions(space, fsOptions, vfsFilename,
							schemes[i]);
				}
			}

			String filename;
			if (vfsFilename.startsWith("\\\\")) {
				File file = new File(vfsFilename);
				filename = file.toURI().toString();
			} else {
				if (relativeFilename) {
					File file = new File(vfsFilename);
					filename = file.getAbsolutePath();
				} else {
					filename = vfsFilename;
				}
			}

			FileObject fileObject = null;

			if (fsOptions != null) {
				fileObject = fsManager.resolveFile(filename, fsOptions);
			} else {
				fileObject = fsManager.resolveFile(filename);
			}

			return fileObject;
		} catch (IOException e) {
			throw new ImetaFileException("无法通过文件名获得VFS文件对象'" + vfsFilename
					+ "' : " + e.getMessage());
		}
	}

	private static FileSystemOptions buildFsOptions(VariableSpace varSpace,
			FileSystemOptions sourceOptions, String vfsFilename, String scheme)
			throws IOException {
		if (varSpace == null || vfsFilename == null) {
			// We cannot extract settings from a non-existant variable space
			return null;
		}

		IFileSystemConfigBuilder configBuilder = FileSystemConfigBuilderFactory
				.getConfigBuilder(varSpace, scheme);

		FileSystemOptions fsOptions = (sourceOptions == null) ? new FileSystemOptions()
				: sourceOptions;

		String[] varList = varSpace.listVariables();

		for (String var : varList) {
			if (var.startsWith("vfs.")) { //$NON-NLS-1$
				String param = configBuilder.parseParameterName(var, scheme);
				if (param != null) {
					configBuilder.setParameter(fsOptions, param, varSpace
							.getVariable(var), var, vfsFilename);
				} else {
					throw new IOException(
							"FileSystemConfigBuilder could not parse parameter: " + var); //$NON-NLS-1$
				}
			}
		}
		return fsOptions;
	}

	/**
	 * Read a text file (like an XML document). WARNING DO NOT USE FOR DATA
	 * FILES.
	 * 
	 * @param vfsFilename
	 *            the filename or URL to read from
	 * @param charSetName
	 *            the character set of the string (UTF-8, ISO8859-1, etc)
	 * @return The content of the file as a String
	 * @throws IOException
	 */
	public static String getTextFileContent(String vfsFilename,
			String charSetName) throws ImetaFileException {
		return getTextFileContent(vfsFilename, null, charSetName);
	}

	public static String getTextFileContent(String vfsFilename,
			VariableSpace space, String charSetName) throws ImetaFileException {
		try {
			InputStream inputStream = null;

			if (space == null) {
				inputStream = getInputStream(vfsFilename);
			} else {
				inputStream = getInputStream(vfsFilename, space);
			}
			InputStreamReader reader = new InputStreamReader(inputStream,
					charSetName);
			int c;
			StringBuffer stringBuffer = new StringBuffer();
			while ((c = reader.read()) != -1)
				stringBuffer.append((char) c);
			reader.close();
			inputStream.close();

			return stringBuffer.toString();
		} catch (IOException e) {
			throw new ImetaFileException(e);
		}
	}

	public static boolean fileExists(String vfsFilename)
			throws ImetaFileException {
		return fileExists(vfsFilename, null);
	}

	public static boolean fileExists(String vfsFilename, VariableSpace space)
			throws ImetaFileException {
		FileObject fileObject = null;
		try {
			fileObject = getFileObject(vfsFilename, space);
			return fileObject.exists();
		} catch (IOException e) {
			throw new ImetaFileException(e);
		} finally {
			if (fileObject != null) {
				try {
					fileObject.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public static InputStream getInputStream(FileObject fileObject)
			throws FileSystemException {
		FileContent content = fileObject.getContent();
		return content.getInputStream();
	}

	public static InputStream getInputStream(String vfsFilename)
			throws ImetaFileException {
		return getInputStream(vfsFilename, null);
	}

	public static InputStream getInputStream(String vfsFilename,
			VariableSpace space) throws ImetaFileException {
		try {
			FileObject fileObject = getFileObject(vfsFilename, space);
			return getInputStream(fileObject);
		} catch (IOException e) {
			throw new ImetaFileException(e);
		}
	}

	public static OutputStream getOutputStream(FileObject fileObject,
			boolean append) throws IOException {
		FileObject parent = fileObject.getParent();
		if (parent != null) {
			if (!parent.exists()) {
				throw new IOException(BaseMessages.getString(PKG,
						"VFS.Exception.ParentDirectoryDoesNotExist",
						getFilename(parent)));
			}
		}
		try {
			fileObject.createFile();
			FileContent content = fileObject.getContent();
			return content.getOutputStream(append);
		} catch (FileSystemException e) {
			// Perhaps if it's a local file, we can retry using the standard
			// File object. This is because on Windows there is a bug in VFS.
			//
			if (fileObject instanceof LocalFile) {
				try {
					String filename = getFilename(fileObject);
					return new FileOutputStream(new File(filename), append);
				} catch (Exception e2) {
					throw e; // throw the original exception: hide the retry.
				}
			} else {
				throw e;
			}
		}
	}

	public static OutputStream getOutputStream(String vfsFilename,
			boolean append) throws ImetaFileException {
		return getOutputStream(vfsFilename, null, append);
	}

	public static OutputStream getOutputStream(String vfsFilename,
			VariableSpace space, boolean append) throws ImetaFileException {
		try {
			FileObject fileObject = getFileObject(vfsFilename, space);
			return getOutputStream(fileObject, append);
		} catch (IOException e) {
			throw new ImetaFileException(e);
		}
	}

	public static String getFilename(FileObject fileObject) {
		FileName fileName = fileObject.getName();
		String root = fileName.getRootURI();
		if (!root.startsWith("file:"))return fileName.getURI(); // nothing we can do about non-normal files. //$NON-NLS-1$
		if (root.startsWith("file:////"))
			return fileName.getURI(); // we'll see 4 forward slashes for a
		// windows/smb network share
		if (root.endsWith(":/")) // Windows //$NON-NLS-1$
		{
			root = root.substring(8, 10);
		} else // *nix & OSX
		{
			root = ""; //$NON-NLS-1$
		}
		String fileString = root + fileName.getPath();
		if (!"/".equals(Const.FILE_SEPARATOR)) //$NON-NLS-1$
		{
			fileString = Const.replace(fileString, "/", Const.FILE_SEPARATOR); //$NON-NLS-1$
		}
		return fileString;
	}

	public static FileObject createTempFile(String prefix, String suffix,
			String directory) throws ImetaFileException {
		return createTempFile(prefix, suffix, directory, null);
	}

	public static FileObject createTempFile(String prefix, String suffix,
			String directory, VariableSpace space) throws ImetaFileException {
		try {
			FileObject fileObject;
			do {
				// Build temporary file name using UUID to ensure uniqueness.
				// Old mechanism would fail using Sort Rows (for example)
				// when there multiple nodes with multiple JVMs on each node. In
				// this case, the temp file names would end up being
				// duplicated which would cause the sort to fail.
				String filename = new StringBuffer(50).append(directory)
						.append('/').append(prefix).append('_').append(
								UUIDUtil.getUUIDAsString()).append(suffix)
						.toString();
				fileObject = getFileObject(filename, space);
			} while (fileObject.exists());
			return fileObject;
		} catch (IOException e) {
			throw new ImetaFileException(e);
		}
	}

	public static Comparator<FileObject> getComparator() {
		return new Comparator<FileObject>() {
			public int compare(FileObject o1, FileObject o2) {
				String filename1 = getFilename(o1);
				String filename2 = getFilename(o2);
				return filename1.compareTo(filename2);
			}
		};
	}

}
