package com.plywet.imeta.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.CRC32;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;

/**
 * 操作文件系统的工具类
 * 
 * @author 潘巍（Peter Pan）
 * @since 2011-4-20 下午05:28:25
 */
public class FileUtils {

	private static final String JPEG = "jpeg";
	private static final String JPG = "jpg";
	private static final int BUFFER_SIZE = 4 * 1024;
	private static final boolean CLOCK = true;
	private static final boolean VERIFY = true;

	/**
	 * 工具方法：判断对象是否是目录
	 * 
	 * @param f
	 * @return
	 */
	public static boolean isDirectory(File f) {
		if (f != null && f.exists() && f.isDirectory()) {
			return true;
		}
		return false;
	}

	/**
	 * 工具方法：复制文件
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 */
	public static void copyFile(File srcFile, File destFile) throws IOException {
		if (!srcFile.getPath().toLowerCase().endsWith(JPG)
				&& !srcFile.getPath().toLowerCase().endsWith(JPEG)) {
			return;
		}
		final InputStream in = new FileInputStream(srcFile);
		final OutputStream out = new FileOutputStream(destFile);
		try {
			long millis = System.currentTimeMillis();
			CRC32 checksum;
			if (VERIFY) {
				checksum = new CRC32();
				checksum.reset();
			}
			final byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = in.read(buffer);
			while (bytesRead >= 0) {
				if (VERIFY) {
					checksum.update(buffer, 0, bytesRead);
				}
				out.write(buffer, 0, bytesRead);
				bytesRead = in.read(buffer);
			}
			if (CLOCK) {
				millis = System.currentTimeMillis() - millis;
			}
		} catch (IOException e) {
			throw e;
		} finally {
			out.close();
			in.close();
		}
	}

	/**
	 * 工具方法：复制目录
	 * 
	 * @param srcDir
	 *            源目录
	 * @param dstDir
	 *            目标目录
	 */
	public static void copyDirectory(File srcDir, File dstDir)
			throws IOException {

		if (".svn".equals(srcDir.getName())) {
			return;
		}

		if (srcDir.isDirectory()) {
			if (!dstDir.exists()) {
				dstDir.mkdir();
			}

			for (String aChildren : srcDir.list()) {
				copyDirectory(new File(srcDir, aChildren), new File(dstDir,
						aChildren));
			}
		} else {
			copyFile(srcDir, dstDir);
		}
	}

	/**
	 * 工具方法：删除目录
	 * 
	 * @param dir
	 *            待删除的目录
	 * @param isInitialDelete
	 *            如果出现错误是否继续删除其他的
	 * @return true 如果目录成果删除
	 */
	public static boolean deleteDirectory(File dir, boolean isInitialDelete) {
		if (dir.isDirectory()) {
			if (dir.exists()) {
				for (File child : dir.listFiles()) {
					try {
						deleteDirectory(child, isInitialDelete);
					} catch (Exception e) {
						if (isInitialDelete)
							continue;
						else
							return false;
					}
				}
			}
		}
		dir.delete();
		return true;
	}

	/**
	 * 工具方法：连接文件名，以文件分隔符分隔
	 * 
	 * @param files
	 *            待连接的文件名称集合
	 * @return
	 */
	public static String joinFiles(String... files) {
		final StringBuilder res = new StringBuilder();
		for (String file : files) {
			res.append(file).append(File.separatorChar);
		}

		return res.substring(0, res.length() - 1);
	}

	/**
	 * 工具方法：删除文件
	 * 
	 * @param file
	 */
	public static void deleteFile(File file) {
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 工具方法：从磁盘上读取图片文件，存储到一个BufferedImage对象中
	 * 
	 * @param data
	 *            图片的相对路径
	 * @param format
	 *            图片文件后缀
	 * @return
	 * 
	 */
	public static BufferedImage bitmapToImage(String data, String format)
			throws IOException {
		final InputStream inb = new FileInputStream(data);
		final ImageReader rdr = ImageIO.getImageReadersByFormatName(format)
				.next();
		final ImageInputStream imageInput = ImageIO.createImageInputStream(inb);
		rdr.setInput(imageInput);
		final BufferedImage image = rdr.read(0);
		inb.close();
		return image;
	}

	/**
	 * 工具方法：将一个BufferedImage对象保存到磁盘上
	 * 
	 * @param image
	 *            待保存的BufferedImage对象
	 * @param data
	 *            图片的相对路径
	 * @param format
	 *            图片后缀
	 * 
	 */
	public static void imageToBitmap(BufferedImage image, String data,
			String format) throws IOException {
		final OutputStream inb = new FileOutputStream(data);
		final ImageWriter wrt = ImageIO.getImageWritersByFormatName(format)
				.next();
		final ImageInputStream imageInput = ImageIO
				.createImageOutputStream(inb);
		wrt.setOutput(imageInput);
		wrt.write(image);
		inb.close();
	}

	/**
	 * 工具方法：缩放图片
	 * 
	 * @param img
	 *            用于缩放的原始图片
	 * @param targetWidth
	 *            目标宽度（像素）
	 * @param targetHeight
	 *            目标高度（像素）
	 * @param hint
	 *            渲染算法选择提示 {@code RenderingHints.KEY_INTERPOLATION} (e.g.
	 *            {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
	 *            {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR}, {@code
	 *            RenderingHints.VALUE_INTERPOLATION_BICUBIC})
	 * @param higherQuality
	 *            如果为true，该方法将使用多步缩放机制，进而提供高质量的图片 (仅用于缩小的情况下，及{@code
	 *            targetWidth} 或者{@code targetHeight}小于原始尺寸，并且通常用于{@code
	 *            BILINEAR}提示)
	 * @return 原始图的缩放版本 {@code BufferedImage}
	 */
	public static BufferedImage getScaledInstance(BufferedImage img,
			int targetWidth, int targetHeight, Object hint,
			boolean higherQuality) {
		final int type = img.getTransparency() == Transparency.OPAQUE ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage) img;
		int w;
		int h;
		if (higherQuality) {
			// 使用多步技术：开始时是原始大小
			w = img.getWidth();
			h = img.getHeight();
		} else {
			// 使用单步技术 :直接使用目标尺寸
			w = targetWidth;
			h = targetHeight;
		}

		do {
			if (higherQuality && w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}

			if (higherQuality && h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}

			final BufferedImage tmp = new BufferedImage(w, h, type);
			final Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();

			ret = tmp;
		} while (w != targetWidth || h != targetHeight);

		return ret;
	}

	/**
	 * 工具方法：创建目录
	 * 
	 * @param directory
	 *            待创建的目录
	 * @param isOverride
	 *            是否覆盖。如果true，将删除原有目录然后创建新的目录，否则如果目录存在将不会创建新的目录
	 * 
	 */
	public static void addDirectory(File directory, boolean isOverride) {
		if (directory.exists()) {
			if (isOverride)
				deleteDirectory(directory, false);
			else
				return;
		}
		directory.mkdirs();
	}

	/**
	 * 工具方法：通过文件路径名称获得URL对象<br>
	 * 首先，将filename当做全路径，判断是否存在该文件，<br>
	 * 如果存在转换URL；<br>
	 * 如果不存在，当做以类加载器为根的相对路径看待，判断是否存在该文件。
	 * 
	 * @param filename
	 * @return
	 * @throws MalformedURLException
	 */
	public static URL getURL(String filename) throws MalformedURLException {
		URL url;
		File file = new File(filename);
		if (file.exists()) {
			url = file.toURI().toURL();
		} else {
			ClassLoader classLoader = FileUtils.class.getClassLoader();
			url = classLoader.getResource(filename);
		}
		return url;
	}
}