package org.jzkangta.tlspc.framework.util.emulehash;

import java.io.File;

/**
 * IMD4 对文件内容或url进行hash
 */
public interface IMD4 {
	/**
	 * 文件内容进行md4的hash处理
	 * @param fileForMD4 文件全路径
	 * @return 32字节的16进制字符串
	 * @throws Exception
	 */
	public String add( String fileForMD4) throws Exception;

	/**
	 * 文件内容进行md4的hash处理
	 * @param fileForMD4 文件全路径
	 * @return 32字节的16进制字符串
	 * @throws Exception
	 */
	public String addFile( String fileForMD4) throws Exception;
	
	/**
	 * 文件内容进行md4的hash处理
	 * @param fileForMD4 文件
	 * @return 32字节的16进制字符串
	 * @throws Exception
	 */
	public String addFile( File fileForMD4) throws Exception;

	/**
	 * 对url进行md4的hash处理
	 * @param urlForMD4 url全路径
	 * @return 32字节的16进制字符串
	 * @throws Exception
	 */
	public String addURL( String urlForMD4) throws Exception;
}
