package org.jzkangta.tlspc.framework.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import org.jzkangta.tlspc.framework.util.emulehash.IMD4;
import org.jzkangta.tlspc.framework.util.emulehash.MD4Factory;

/**
 * 文件类工具
 * 
 */
public final class FileUtil {
	private static Logger log = Logger.getLogger(FileUtil.class);
	
	private static final String IMAGE_TYPE = "bmp,dib,gif,jfif, jpe,jpeg,jpg, png,tif,tiff,ico";
	
	private static final String VOICE_TYPE = "amr,mp3,wav,wma,ogg,ape,acc";
	
	private static final String VIDEO_TYPE = "mov,avi,wma,rmvb,rm,flash,mp4,mid,3gp";

	/**
	 * 文件的元信息，如名称，类型，路径，内容hash值等
	 * 
	 */
	public static class FileInfo {
		private File obj;
		private String fileName = "";
		private String name = "";
		private String ext = "";
		private String path = "";
		private String hashCode = null;

		public FileInfo(File f) {
			this.obj = f;
			this.fileName = f.getName();
			this.path = f.getPath();
			int endIndex = fileName.lastIndexOf(".");
			if (endIndex > 0) {
				this.name = fileName.substring(0, endIndex);
				this.ext = fileName.substring(endIndex + 1).toLowerCase();
			}
		}

		public FileInfo(String filePath) {
			this(new File(filePath));
		}

		/**
		 * @return 返回 扩展名ext。
		 */
		public String getExt() {
			return ext;
		}

		/**
		 * @return 返回 文件内容的md4的hash值。
		 */
		public synchronized String getHashCode() {
			if (hashCode == null && obj != null && obj.isFile()) {
				hashCode = FileUtil.md4HashCode(obj);
			}
			return hashCode;
		}

		/**
		 * @return 返回 name。
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return 返回 文件本身File对象。
		 */
		public File getObj() {
			return obj;
		}

		/**
		 * @return 返回 完整路径。
		 */
		public String getPath() {
			return path;
		}

		/**
		 * @return 返回 fileName，含扩展名。
		 */
		public String getFileName() {
			return fileName;
		}

	}

	/**
	 * 目录信息，如名称，子目录列表，目录中的文件列表
	 * 
	 */
	public static class Folder {
		private String name;
		private File obj;
		private List<File> children;
		private List<File> files;

		public Folder(File obj, FileFilter filter) {
			this.obj = obj;
			this.name = obj.getName();
			children = FileUtil.listDirectorys(obj, false);
			files = FileUtil.listAll(obj, filter, false);
		}

		/**
		 * @return 返回 子目录列表。
		 */
		public List<File> getChildren() {
			return children;
		}

		/**
		 * @param children
		 *            要设置的 子目录列表。
		 */
		public void setChildren(List<File> children) {
			this.children = children;
		}

		/**
		 * @return 返回 目录中的文件列表。
		 */
		public List<File> getFiles() {
			return files;
		}

		/**
		 * @param files
		 *            要设置的 目录中的文件列表。
		 */
		public void setFiles(List<File> files) {
			this.files = files;
		}

		/**
		 * @return 返回 目录名。
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            要设置的 目录名。
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return 返回 目录本身对应的File对象。
		 */
		public File getObj() {
			return obj;
		}

		/**
		 * @param obj
		 *            要设置的 目录本身对应的File对象。
		 */
		public void setObj(File obj) {
			this.obj = obj;
		}

	}

	/**
	 * 获取文件的类型，即扩展名
	 * 
	 * @param fileName
	 *            文件名
	 * @return 文件的类型，即扩展名
	 */
	public static String getFileType(String fileName) {
		String ext = "";
		if (fileName != null && fileName.trim().length() > 0) {
			int endIndex = fileName.lastIndexOf(".");
			if (endIndex > 0) {
				ext = fileName.substring(endIndex + 1).toLowerCase();
			}
		}
		return ext;
	}

	/**
	 * 执行一个可执行文件，含参数（命令行）
	 * 
	 * @param command
	 *            命令行
	 * @return 命令的标准输出流的输出信息
	 * @throws RuntimeException
	 */
	public static String execCommand(String command) throws RuntimeException {
		Process process = null;
		InputStreamReader ir = null;
		InputStream in = null;
		LineNumberReader lnr = null;
		try {
			StringBuffer msg = new StringBuffer();
			process = Runtime.getRuntime().exec(command);

			in = process.getInputStream();
			ir = new InputStreamReader(in);
			lnr = new LineNumberReader(ir);
			String line = null;
			while ((line = lnr.readLine()) != null) {
				// msg = new String(msg.getBytes("iso8859-1"), "GB18030");
				msg.append(line).append("\n");
			}
			// in.close();
			// ir.close();
			// lnr.close();
			StringBuffer error = new StringBuffer();
			in = process.getErrorStream();
			ir = new InputStreamReader(in);
			lnr = new LineNumberReader(ir);
			while ((line = lnr.readLine()) != null) {
				// error = new String(error.getBytes("iso8859-1"), "GBK");
				error.append(line).append("\n");
				;
			}
			// in.close();
			// ir.close();
			// lnr.close();
			if (error.length() > 0) {
				throw new RuntimeException(error.toString());
			}
			return msg.toString();

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				process.getInputStream().close();
				process.getErrorStream().close();
				process.getOutputStream().close();
				// if( process.waitFor() != 0 );
				// process.destroy();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	/**
	 * 对文件内容做md4的hash运算
	 * 
	 * @param filename
	 *            文件名
	 * @return md4的hash值
	 */
	public static String md4HashCode(String filename) {
		try {
			IMD4 md4 = MD4Factory.getInstance();
			return md4.addFile(filename);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("md4HashCode error: " + filename + "," + e);
			return null;
		}
	}

	/**
	 * 对文件内容做md4的hash运算
	 * 
	 * @param file
	 *            文件
	 * @return md4的hash值
	 */
	public static String md4HashCode(File file) {
		try {
			IMD4 md4 = MD4Factory.getInstance();
			return md4.addFile(file);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("md4HashCode error: " + file.getName() + "," + e);
			return null;
		}
	}

	/**
	 * 修改文件的最后访问时间。 如果文件不存在则创建该文件。
	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * 
	 * @param file
	 *            需要修改最后访问时间的文件。
	 * @since 1.0
	 */
	public static boolean touch(File file) {
		if (!file.exists()) {
			try {
				return file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file.setLastModified(System.currentTimeMillis());
	}

	/**
	 * 修改文件的最后访问时间。 如果文件不存在则创建该文件。
	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * 
	 * @param fileName
	 *            需要修改最后访问时间的文件的文件名。
	 * @since 1.0
	 */
	public static boolean touch(String fileName) {
		File file = new File(fileName);
		return touch(file);
	}

	/**
	 * 修改文件的最后访问时间。 如果文件不存在则创建该文件。
	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * 
	 * @param files
	 *            需要修改最后访问时间的文件数组。
	 * @since 1.0
	 */
	public static void touch(File[] files) {
		for (int i = 0; i < files.length; i++) {
			touch(files[i]);
		}
	}

	/**
	 * 修改文件的最后访问时间。 如果文件不存在则创建该文件。
	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * 
	 * @param fileNames
	 *            需要修改最后访问时间的文件名数组。
	 * @since 1.0
	 */
	public static void touch(String[] fileNames) {
		File[] files = new File[fileNames.length];
		for (int i = 0; i < fileNames.length; i++) {
			files[i] = new File(fileNames[i]);
		}
		touch(files);
	}

	/**
	 * 将文件名中的特殊字符替换为“_”
	 * 
	 * @param fileName
	 * @return 转换后的文件名
	 */
	public static String unixRegFileName(String fileName) {
		return fileName.replaceAll("[\\x20\\x21\\x23\\x24\\x25\\x26\\x27\\x28\\x29\\x2B\\x3B\\x3d\\x40\\x5b\\x5c\\x5d\\x5e\\x60\\x7b\\x7d\\x7e]", "_");
	}

	/**
	 * 判断指定的文件是否存在。
	 * 
	 * @param fileName
	 *            要判断的文件的文件名
	 * @return 存在时返回true，否则返回false。
	 * @since 1.0
	 */
	public static boolean isExist(String fileName) {
		return new File(fileName).exists();
	}

	/**
	 * 判断指定的路径是否是一个目录
	 * 
	 * @param fileName
	 *            要判断的文件的文件名
	 * @return 如果不存在文件或者不是一个目录 都会返回 false，否则返回true
	 */
	public static boolean isDirectory(String fileName) {
		boolean flag = true;
		if (!isExist(fileName) || !(new File(fileName)).isDirectory()) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 创建指定的目录。 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。 <b>注意：可能会在返回false的时候创建部分父目录。</b>
	 * 
	 * @param file
	 *            要创建的目录
	 * @return 完全创建成功时返回true，否则返回false。
	 * @since 1.0
	 */
	public static boolean makeParentFolder(File file) {
		File parent = file.getParentFile();
		if (parent != null && !parent.exists()) {
			return parent.mkdirs();
		}
		return false;
	}

	/**
	 * 创建当前路径目录，成功返回true，创建失败返回false
	 * 
	 * @param file
	 * @return
	 */
	public static boolean makeFolder(String path) {
		if (!isDirectory(path)) {
			File file = new File(path);
			file.mkdirs();
			return true;
		}
		return false;
	}

	/**
	 * 创建指定的目录。 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。 <b>注意：可能会在返回false的时候创建部分父目录。</b>
	 * 
	 * @param fileName
	 *            要创建的目录的目录名
	 * @return 完全创建成功时返回true，否则返回false。
	 * @since 1.0
	 */
	public static boolean makeParentFolder(String fileName) {
		return makeParentFolder(new File(fileName));
	}

	/**
	 * 清空指定目录中的文件。 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。
	 * 另外这个方法不会迭代删除，即不会删除子目录及其内容。
	 * 
	 * @param directory
	 *            要清空的目录
	 * @return 目录下的所有文件都被成功删除时返回true，否则返回false.
	 * @since 1.0
	 */
	public static boolean emptyFolder(File directory) {
		boolean result = false;
		File[] entries = directory.listFiles();
		for (int i = 0; i < entries.length; i++) {
			if (!entries[i].delete()) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * 是否为空目录
	 * 
	 * @return
	 */
	public boolean isEmptyFolder(String dir) {
		File file = new File(dir);
		String[] files = file.list();
		return (files.length == 0);
	}

	/**
	 * 清空指定目录中的文件。 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。
	 * 另外这个方法不会迭代删除，即不会删除子目录及其内容。
	 * 
	 * @param directoryName
	 *            要清空的目录的目录名
	 * @return 目录下的所有文件都被成功删除时返回true，否则返回false。
	 * @since 1.0
	 */
	public static boolean emptyFolder(String directoryName) {
		File dir = new File(directoryName);
		return emptyFolder(dir);
	}

	/**
	 * 迭代删除指定目录及其中的所有文件＆子目录。
	 * 
	 * @param dirName
	 *            要删除的目录的目录名
	 * @param deleteAll
	 *            是否删除全部， 为true时迭代删除子目录的所有内容,
	 *            false时只删除当前目录中可以删除的内容,如果子目录的内容不会空时不能删除.
	 * @return 删除成功时返回true，否则返回false。
	 * @since 1.0
	 */
	public static boolean deleteDirectory(String dirName, boolean deleteAll) throws IllegalArgumentException {
		return deleteDirectory(new File(dirName), deleteAll);
	}

	/**
	 * 迭代删除指定目录及其中的所有文件＆子目录。
	 * 
	 * @param dir
	 *            要删除的目录
	 * @param deleteAll
	 *            是否删除全部，为true时迭代删除子目录的所有内容,
	 *            false时只删除当前目录中可以删除的内容,如果子目录的内容不会空时不能删除.
	 * @return 删除成功时返回true，否则返回false。
	 * @since 1.0
	 */
	public static boolean deleteDirectory(File dir, boolean deleteAll) {
		if ((dir == null) || !dir.isDirectory()) {
			log.error(dir + " is not a directory. ");
			return false;
		}

		File[] entries = dir.listFiles();
		int sz = entries.length;
		boolean result = true;

		for (int i = 0; i < sz; i++) {
			if (entries[i].isDirectory() && deleteAll) {
				if (!deleteDirectory(entries[i], deleteAll))
					result = false;
			}
			if (!entries[i].delete()) {
				result = false;
			}
		}

		if (!dir.delete()) {
			result = false;
		}
		return result;
	}

	/**
	 * 列出目录中的文件，可包括子目录中的内容。
	 * 
	 * @param dirName
	 *            要列出的目录
	 * @param listAll
	 *            是否递归查询
	 * @return 所有文件的列表
	 */
	public static List<File> listFiles(String dirName, boolean listAll) {
		return listFiles(new File(dirName), listAll);
	}

	/**
	 * 列出目录中的文件，可包括子目录中的内容。
	 * 
	 * @param dir
	 *            要列出的目录
	 * @param listAll
	 *            是否递归查询
	 * @return 所有文件列表
	 */
	public static List<File> listFiles(File dir, boolean listAll) {
		return listAll(dir, new FileFilter() {
			public boolean accept(File f) {
				return f.isFile();
			}
		}, listAll);
	}

	/**
	 * 列出目录中的子目录，可包括子目录中的所有子目录。
	 * 
	 * @param dirName
	 *            要列出的目录
	 * @param listAll
	 *            是否递归查询
	 * @return 所有子目录列表
	 */
	public static List<File> listDirectorys(String dirName, boolean listAll) {
		return listDirectorys(new File(dirName), listAll);
	}

	/**
	 * 列出目录中的子目录，可包括子目录中的所有子目录。
	 * 
	 * @param dir
	 *            要列出的目录
	 * @param listAll
	 *            是否递归查询
	 * @return File对象的列表
	 */
	public static List<File> listDirectorys(File dir, boolean listAll) {
		return listAll(dir, new FileFilter() {
			public boolean accept(File f) {
				return f.isDirectory();
			}
		}, listAll);
	}

	/**
	 * 列出目录中的子目录，可包括子目录中的所有子目录。
	 * 
	 * @param dirName
	 *            要列出的目录名称，全路径
	 * @param filter
	 *            文件过滤规则
	 * @return FileUtil.Folder对象的列表
	 */
	public static Map<String, Folder> getFolderMap(String dirName, FileFilter filter) {
		return getFolderMap(new File(dirName), filter);
	}

	/**
	 * 列出目录中的子目录，可包括子目录中的所有子目录。
	 * 
	 * @param dir
	 *            要列出的目录
	 * @param filter
	 *            文件过滤规则
	 * @return FileUtil.Folder对象的列表
	 */
	public static Map<String, Folder> getFolderMap(File dir, FileFilter filter) {

		HashMap<String, Folder> map = new HashMap<String, Folder>();
		if (!dir.exists()) {
			return map;
		}
		getFolderMap(map, dir, filter);
		return map;
	}

	private static void getFolderMap(Map<String, Folder> map, File dir, FileFilter filter) {
		Folder folder = new Folder(dir, filter);
		log.debug("file:" + dir.getAbsolutePath());
		map.put(dir.getAbsolutePath(), folder);
		Iterator<File> iter = folder.children.iterator();
		while (iter.hasNext()) {
			File child = (File) iter.next();
			log.debug("child:" + child.getAbsolutePath());
			getFolderMap(map, child, filter);
		}
	}

	/**
	 * 列出目录中的所有文件＆目录列表，可包括其子目录中的内容。
	 * 
	 * @param file
	 *            要列出的目录
	 * @param filter
	 *            过滤器
	 * @param listAll
	 *            是否递归查询
	 * @return 目录内容的文件列表。
	 * @since 1.0
	 */
	public static List<File> listAll(File file, FileFilter filter, boolean listAll) {
		ArrayList<File> list = new ArrayList<File>();
		if (!file.exists()) {
			return list;
		}
		listAll(list, file, filter, listAll);
		if (list.contains(file)) {
			list.remove(file);
		}
		log.debug(list);
		return list;
	}

	/**
	 * 列出目录中的所有文件＆目录列表，可包括其子目录中的内容。
	 * 
	 * @param list
	 *            文件列表
	 * @param filter
	 *            过滤器
	 * @param file
	 *            目录
	 * @param listAll
	 *            是否递归查询
	 */
	private static void listAll(ArrayList<File> list, File file, FileFilter filter, boolean listAll) {
		if (file.isDirectory()) {
			File files[] = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (filter.accept(files[i])) {
					list.add(files[i]);
				}
				log.debug("files:" + files[i].getAbsolutePath());
				if (listAll) {
					listAll(list, files[i], filter, listAll);
				}
			}
		}

	}

	/**
	 * 将DOS/Windows格式的路径转换为UNIX/Linux格式的路径。
	 * 其实就是将路径中的"\"全部换为"/"，因为在某些情况下我们转换为这种方式比较方便，
	 * 某中程度上说"/"比"\"更适合作为路径分隔符，而且DOS/Windows也将它当作路径分隔符。
	 * 
	 * @param filePath
	 *            转换前的路径
	 * @return 转换后的路径
	 * @since 1.0
	 */
	public static String toUNIXpath(String filePath) {
		return filePath.replace('\\', '/');
	}

	/**
	 * 拷贝文件。
	 * 
	 * @param fromFileName
	 *            源文件名
	 * @param toFileName
	 *            目标文件名
	 * @return 成功生成文件时返回true，否则返回false
	 * @since 1.0
	 */
	public static boolean copy(String fromFileName, String toFileName) {
		return copy(fromFileName, toFileName, false);
	}

	/**
	 * 移动文件。
	 * 
	 * @param fromFileName
	 *            源文件名
	 * @param toFileName
	 *            目标文件名
	 * @return 成功生成文件时返回true，否则返回false
	 * @since 1.0
	 */
	public static boolean move(String fromFileName, String toFileName) {
		File fromFile = new File(fromFileName);
		File toFile = new File(toFileName);
		return move(fromFile, toFile, false);
	}

	/**
	 * 移动文件。
	 * 
	 * @param fromFileName
	 *            源文件名
	 * @param toFileName
	 *            目标文件名
	 * @param override
	 *            目标文件存在时是否覆盖
	 * @return 成功生成文件时返回true，否则返回false
	 * @since 1.0
	 */
	public static boolean move(String fromFileName, String toFileName, boolean override) {
		File fromFile = new File(fromFileName);
		File toFile = new File(toFileName);
		return move(fromFile, toFile, override);
	}

	/**
	 * 移动文件。
	 * 
	 * @param fromFile
	 *            源文件
	 * @param toFile
	 *            目标文件
	 * @param override
	 *            目标文件存在时是否覆盖
	 * @return 成功生成文件时返回true，否则返回false
	 * @since 1.0
	 */
	public static boolean move(File fromFile, File toFile, boolean override) {
		boolean ret = copy(fromFile, toFile, override);
		if (ret)
			ret = fromFile.delete();
		return ret;
	}

	/**
	 * 拷贝文件。
	 * 
	 * @param fromFileName
	 *            源文件名
	 * @param toFileName
	 *            目标文件名
	 * @param override
	 *            目标文件存在时是否覆盖
	 * @return 成功生成文件时返回true，否则返回false
	 * @since 1.0
	 */
	public static boolean copy(String fromFileName, String toFileName, boolean override) {
		File fromFile = new File(fromFileName);
		File toFile = new File(toFileName);
		return copy(fromFile, toFile, override);
	}

	/**
	 * 拷贝文件。
	 * 
	 * @param fromFile
	 *            源文件
	 * @param toFile
	 *            目标文件
	 * @param override
	 *            目标文件存在时是否覆盖
	 * @return 成功生成文件时返回true，否则返回false
	 * @since 1.0
	 */
	public static boolean copy(File fromFile, File toFile, boolean override) {
		if (!fromFile.exists() || !fromFile.isFile() || !fromFile.canRead()) {
			return false;
		}

		if (toFile.isDirectory()) {
			toFile = new File(toFile, fromFile.getName());

		}
		if (toFile.exists()) {
			if (!toFile.canWrite() || override == false) {
				return false;
			}
		} else {
			String parent = toFile.getParent();
			if (parent == null) {
				parent = System.getProperty("user.dir");
			}
			File dir = new File(parent);
			if (!dir.exists() || dir.isFile() || !dir.canWrite()) {
				return false;
			}
		}

		BufferedInputStream from = null;
		try {
			from = new BufferedInputStream(new FileInputStream(fromFile));
			writeToFile(from, toFile);
			return true;
		} catch (IOException e) {
			log.error("copy " + fromFile + " to " + toFile + " :" + e);
			return false;
		} finally {
			close(from, null);
		}
	}

	/**
	 * 将指定文件的内容输出到指定的输出流中
	 * 
	 * @param fromFile
	 *            指定文件
	 * @param out
	 *            指定的输出流
	 * @return 如果成功放回true，否则返回false
	 * @throws IOException
	 */
	public static boolean putFileToStream(File fromFile, OutputStream out) throws IOException {
		if (!fromFile.exists() || !fromFile.isFile() || !fromFile.canRead()) {
			log.debug(" fromFile is not valid");
			return false;
		}

		BufferedInputStream from = null;
		try {
			from = new BufferedInputStream(new FileInputStream(fromFile));
			pipeTransform(from, out);
			return true;
		} catch (IOException e) {
			throw e;
		} finally {
			close(from, null);
		}
	}

	/**
	 * 将指定文本文件按指定的编码读出
	 * 
	 * @param srcFile
	 *            指定文本文件
	 * @param encoding
	 *            指定的编码
	 * @return 文本文件的内容
	 * @throws IOException
	 */
	public static String readTextFile(File srcFile, String encoding) throws IOException {
		if (!srcFile.exists() || !srcFile.isFile() || !srcFile.canRead()) {
			log.debug(" fromFile is not valid");
		}

		InputStreamReader from = null;
		try {
			char[] buffer = new char[4 * 1024];
			int read = 0;
			from = new InputStreamReader(new FileInputStream(srcFile), encoding);
			StringBuffer result = new StringBuffer();
			while ((read = from.read(buffer, 0, buffer.length)) != -1) {
				result.append(buffer, 0, read);
			}
			return result.toString();
		} catch (IOException e) {
			throw e;
		} finally {
			if (from != null)
				from.close();
		}
	}

	/**
	 * 从输入流中读取内容并写入到指定文件中
	 * 
	 * @param iStream
	 *            输入流
	 * @param fileName
	 *            指定输出文件名
	 * @throws IOException
	 */
	public static void writeToFile(InputStream iStream, String fileName) throws IOException {
		String me = "FileUtil.WriteToFile";
		if (fileName == null) {
			throw new IOException(me + ": filename is null");
		}
		File theFile = new File(fileName);
		// Check if a file exists.
		if (theFile.exists()) {
			String msg = theFile.isDirectory() ? "directory" : (!theFile.canWrite() ? "not writable" : null);
			if (msg != null) {
				throw new IOException(me + ": file '" + fileName + "' is " + msg);
			}
		}
		writeToFile(iStream, theFile);
	}

	/**
	 * 从输入流中读取内容并写入到指定文件中
	 * 
	 * @param iStream
	 *            输入流
	 * @param theFile
	 *            指定输出文件
	 * @throws IOException
	 */
	public static void writeToFile(InputStream iStream, File theFile) throws IOException {
		String me = "FileUtil.WriteToFile";
		if (theFile == null) {
			throw new IOException(me + ": theFile is null");
		}
		if (iStream == null) {
			throw new IOException(me + ": InputStream is null");
		}
		// Save InputStream to the file.
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(theFile);
			pipeTransform(iStream, fOut);
		} catch (Exception e) {
			throw new IOException(me + " failed, got: " + e.toString());
		} finally {
			close(iStream, fOut);
		}
	}

	public static boolean writeToTextFile(String fileName, String content, String encoding) {
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(fileName), encoding);
			writer.write(content);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * 字节流管道传输，从输入流中读取字节，并输出到输出流中
	 * 
	 * @param iStream
	 * @param oStream
	 * @throws IOException
	 */
	public static void pipeTransform(InputStream iStream, OutputStream oStream) throws IOException {
		byte[] buffer = new byte[4 * 1024];
		int bytesRead = 0;
		while ((bytesRead = iStream.read(buffer, 0, buffer.length)) != -1) {
			oStream.write(buffer, 0, bytesRead);
		}
	}

	public static void pipeTransform(Reader reader, Writer writer) throws IOException {
		char[] buffer = new char[4 * 1024];
		int read = 0;
		while ((read = reader.read(buffer, 0, buffer.length)) != -1) {
			writer.write(buffer, 0, read);
		}
	}

	/**
	 * Closes InputStream and/or OutputStream. It makes sure that both streams
	 * tried to be closed, even first throws an exception.
	 */
	private static void close(InputStream iStream, OutputStream oStream) {
		try {
			if (iStream != null)
				iStream.close();
		} catch (IOException e) {
		}

		try {
			if (oStream != null)
				oStream.close();
		} catch (IOException e) {
		}
	}

	private static void close(Reader reader, Writer writer) {
		try {
			if (reader != null)
				reader.close();
		} catch (IOException e) {
		}

		try {
			if (writer != null)
				writer.close();
		} catch (IOException e) {
		}
	}

	public static void convertEncoding(String srcFileName, String dstFileName, String srcEncoding, String dstEncoding) throws IOException {
		convertEncoding(new File(srcFileName), new File(dstFileName), srcEncoding, dstEncoding);
	}

	public static void convertEncoding(File srcFile, File dstFile, String srcEncoding, String dstEncoding) throws IOException {
		if (!srcFile.exists() || !srcFile.isFile() || !srcFile.canRead()) {
			log.debug(" fromFile is not valid");
		}
		FileUtil.makeParentFolder(dstFile);

		InputStreamReader from = null;
		Writer out = null;
		try {
			out = new OutputStreamWriter(new FileOutputStream(dstFile), dstEncoding);
			from = new InputStreamReader(new FileInputStream(srcFile), srcEncoding);
			pipeTransform(from, out);
		} finally {
			close(from, out);
		}
	}

	/**
	 * 以class为出发点，获取资源路径（一般在编译目录下）
	 * 
	 * @param clazz
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String getUrlPath(Class clazz, String fileName) throws FileNotFoundException {

		if (fileName.startsWith("classpath:"))
			fileName = fileName.substring("classpath:".length());

		URL url = clazz.getClassLoader().getResource(fileName);

		if (url == null) {
			throw new FileNotFoundException("file \"" + fileName + "\" not found in classpath!");
		}

		String urlPath = null;
		try {
			urlPath = url.toURI().getPath();
		} catch (URISyntaxException e) {
		}

		if (urlPath == null) {
			urlPath = url.getFile();
		}
		return urlPath;
	}

	/**
	 * 读文件中每一行的字符串。
	 * 
	 * @return
	 * @throws IOException
	 */
	public static List<String> readEachLineStr(String filePath) {
		File file = new File(filePath);
		BufferedReader br = null;
		try {
			FileReader fileReader = new FileReader(file);
			br = new BufferedReader(fileReader);
			String line = null;
			List<String> contentList = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				contentList.add(line);
			}
			return contentList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static List<String> readLines(String filePath) {
		File file = new File(filePath);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String line = null;
			List<String> lines = new ArrayList<String>();
			while ((line = reader.readLine()) != null) {
				lines.add(line.trim());
			}
			return lines;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("读取文件出错：", e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("读取文件出错：", e);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 处理文件名
	 * 
	 * @param fileName
	 *            文件名
	 * @return result 新生成的文件名
	 * 
	 */
	public static String dealName(String fileName) {
		String resultName = "";
		String time = ""; // 当前时间
		String extName = ""; // 文件后缀名

		extName = fileName.split("\\\\")[fileName.split("\\\\").length - 1];
		extName = extName.split("\\.")[extName.split("\\.").length - 1];

		time = String.valueOf(System.currentTimeMillis());
		// 以当前时间作为文件的名字
		resultName = time + "." + extName;

		return resultName;
	}

	public static boolean deleteFile(String filepath) {
		try {
			File file = new File(filepath);
			if (file.exists()) {
				file.delete();
			}
			return true;
		} catch (Exception e) {
			log.error("删除文件出错：", e);
		}

		return false;
	}

	/**
	 * 获取文件绝对目录，若文件所述文件夹不存在，则生成对应的文件夹
	 * 
	 * @param realPath
	 * @param relateDir
	 * @param fileName
	 * @return
	 */
	public static String getSaveFilePath(String realPath, String relateDir, String fileName) {
		// 绝对目录路径
		StringBuffer rootDir = new StringBuffer(realPath).append("/").append(relateDir);

		File logoSaveFile = new File(rootDir.toString());
		if (!logoSaveFile.exists())
			logoSaveFile.mkdirs();

		return rootDir.append(fileName).toString().replace("//", "/").replace("/./", "/");
	}

	/**
	 * 根据request获取项目根目录
	 * 
	 * @param request
	 * @return
	 */
	public static String getRealPath(HttpServletRequest request) {
		return request.getSession().getServletContext().getRealPath("");
	}
	
	/**
	 * 将文件的base64码转换成转换成文件
	 * @param baseCode
	 * @param filePath
	 */
	public static void writeBase64ToFile(String baseCode, String filePath) throws Exception {
		FileOutputStream fos = null;
		try {
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder(); 
			byte[] b = decoder.decodeBuffer(baseCode);
			File file = new File(filePath);
			fos = new FileOutputStream(file);
			fos.write(b);
		} catch(Exception e) {
			throw e;
		} finally {
			if(fos != null) {
				fos.close();
				fos = null;
			}
		}
	}
	
	public static void rename(String oldNamePath, String newNamePath) {
		File file = new File(oldNamePath);
		file.renameTo(new File(newNamePath));
	}
	
	/**
	 * 判断文件的文件类型(音频、视频、图片等)
	 * @param fileName
	 * @return
	 */
	public static String sortFileType(String fileName) {
		String filePreFix = getFileType(fileName);
		if(IMAGE_TYPE.indexOf(filePreFix.toLowerCase()) != -1) {
			return MediaTypeEnum.MEDIA_IMAGE.getName();
		} else if(VIDEO_TYPE.indexOf(filePreFix.toLowerCase()) != -1) {
			return MediaTypeEnum.MEDIA_VIDEO.getName();
		} else if(VOICE_TYPE.indexOf(filePreFix.toLowerCase()) != -1) {
			return MediaTypeEnum.MEDIA_VOICE.getName();
		} else {
			return MediaTypeEnum.MEDIA_FILE.getName();
		}
	}
	

}
