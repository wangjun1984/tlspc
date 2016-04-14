package org.jzkangta.tlspc.framework.util;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;


/**
 * 读取和保存Properties文件的工具类
 *
 */
public class PropertyUtil {

	private static Logger log = Logger.getLogger(PropertyUtil.class);

	/**
	 * 获取属性配置中的int型配置，如果没有则返回默认值
	 * @param p 属性对象集合
	 * @param key 属性名称
	 * @param defaultValue 默认值
	 * @return 获取属性配置中的int型配置，如果没有则返回默认值
	 */
	public static int getIntProperty(Properties p, String key, int defaultValue ) {
		if( p==null ) return defaultValue;
		String ret = p.getProperty( key );
		if( StringUtil.isEmpty( ret ) ) return defaultValue;
		return Integer.parseInt(ret);
		
	}

	/**
	 * 保存properties到指定文件中
	 * @param p 配置信息
	 * @param fileName 可以是properties或xml文件
	 */
	public static void saveProperties(Properties p, String fileName) {
		saveProperties( p,  fileName, null, "UTF-8");
	}

	/**
	 * 保存properties到指定文件中
	 * @param p 配置信息
	 * @param fileName 可以是properties或xml文件
	 * @param comment 备注信息
	 */
	public static void saveProperties(Properties p, String fileName, String comment) {
		saveProperties( p,  fileName, comment, "UTF-8");
	}

	/**
	 * 保存properties到指定文件中
	 * @param p 配置信息
	 * @param fileName 可以是properties或xml文件
	 * @param encoding 编码格式
	 */
	public static void savePropertiesXml(Properties p, String fileName, String encoding) {
		saveProperties( p,  fileName, null, encoding);
	}
	
	/**
	 * 保存properties到指定文件中
	 * @param p 配置信息
	 * @param fileName 可以是properties或xml文件
	 * @param comment 备注信息
	 * @param encoding 编码格式
	 */
	public static void saveProperties(Properties p, String fileName, String comment, String encoding) {
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream( fileName));
			if( fileName.toLowerCase().endsWith(".xml") )
				p.storeToXML(out, comment, encoding );
			else
				p.store(out, comment);
		} catch (Exception e) {
			log.error("Save properties to " + fileName + ",Exception: " + e.getMessage());
			e.printStackTrace();
		}
		finally{
			try {
				if ( out != null )
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取配置文件中的内容（通过PropertyResourceBundle，更改时需重启服务）
	 * @param fileName   文件名（含全路径，相对路径从WEB-INF/classes开始），不用.properties结尾
	 * @return Propertis 对象
	 */
	public static String getProperty(String fileName, String key) {
		String sRet = null;
		try {

			PropertyResourceBundle configBundle = (PropertyResourceBundle) ResourceBundle.getBundle(fileName);
			sRet = configBundle.getString(key);

		} catch (Exception e) {
			log.error("Read " + fileName + " " + key+ " getProperty Exception : " + e.getMessage());
			e.printStackTrace();
		}
		return sRet;
	}
	/**
	 * 获取配置文件中的内容（通过PropertyResourceBundle，更改时需重启服务）
	 * @param fileName   文件名（含全路径，相对路径从WEB-INF/classes开始），不用.properties结尾
	 * @return Propertis 对象
	 */
	public static String getProperty(String fileName, String key, String defaultValue) {
		String sRet = null;
		try {
			
			PropertyResourceBundle configBundle = (PropertyResourceBundle) ResourceBundle.getBundle(fileName);
			sRet = configBundle.getString(key);
			if( StringUtil.isEmpty( sRet ) )
				return defaultValue;
		} catch (Exception e) {
			log.error("Read " + fileName + " " + key+ " getProperty Exception : " + e.getMessage());
			e.printStackTrace();
		}
		return sRet;
	}
	
	/**
	 * 获取配置文件中的整数配置内容（通过PropertyResourceBundle，更改时需重启服务）
	 * @param fileName 文件名（含全路径，相对路径从WEB-INF/classes开始），不用.properties结尾
	 * @param key 配置项
	 * @param defaultValue 默认值
	 * @return 整数配置内容
	 */
	public static int getIntProperty(String fileName, String key, int defaultValue) {
		try {
			PropertyResourceBundle configBundle = (PropertyResourceBundle) ResourceBundle.getBundle(fileName);
			String sRet = configBundle.getString(key);
			if( StringUtil.isEmpty( sRet ) )
				return defaultValue;
			return Integer.parseInt( sRet );

		} catch (Exception e) {
			log.error("Read " + fileName + " " + key+ " getProperty Exception : " + e.getMessage());
			e.printStackTrace();
		}
		return defaultValue;
	}
	
	/**
	 * 判断配置文件中的配置项是否存在
	 * @param fileName 文件名（含全路径，相对路径从WEB-INF/classes开始），不用.properties结尾
	 * @param key 配置项
	 * @return 是否包含这个配置项
	 */
	public static boolean containsKey(String fileName, String key) {
		try {
			PropertyResourceBundle configBundle = (PropertyResourceBundle) ResourceBundle.getBundle(fileName);
			boolean flag = configBundle.containsKey(key);
			return flag;
		} catch (Exception e) {
			log.error("Read " + fileName + " " + key+ " getProperty Exception : " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	
	public static void main(String[] args ) throws Exception{
		System.out.print( PropertyUtil.class.getResource( "../../../../" ).getFile().replace( PropertyUtil.class.getPackage().getName().replace('.','/'), "" ) );
	}
*/
}
