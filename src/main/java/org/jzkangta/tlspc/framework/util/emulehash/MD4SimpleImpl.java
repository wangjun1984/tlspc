/**
 * MD4SimpleImpl.java   2007-2-12
 *
 */
package org.jzkangta.tlspc.framework.util.emulehash;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import org.jzkangta.tlspc.framework.util.crypt.MD4;


/**
 * MD4SimpleImpl
 */
public class MD4SimpleImpl implements IMD4 {

	private final int MD4_PARTSIZE = 9728000; //1024;//
	
	private final char[] HEX_DIGITS ={
	        '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
	};
	
	private String hexToString(byte[] ba)
    {
        int length = ba.length;
        char[] buf = new char[length * 2];
        for (int i = 0, j = 0, k; i < length; )
        {
            k = ba[i++];
            buf[j++] = HEX_DIGITS[(k >>> 4) & 0x0F];
            buf[j++] = HEX_DIGITS[ k        & 0x0F];
        }
        return new String(buf);
    }

	public MD4SimpleImpl() {
		super();
		System.out.println( "MD4SimpleImpl inited");
	}

	public String add(String fileForMD4) throws Exception {
		return addFile( fileForMD4 );
	}

	public String addFile(String fileForMD4) throws Exception {
		File file = new File(fileForMD4);
		return addFile( file );
	}

	public String addFile(File file) throws Exception {
		long fileLen = file.length();
		if(fileLen==0){
			throw new Exception( file.getName() + " can't be empty!");	
		}
		ArrayList<byte[]> al = new ArrayList<byte[]>();
        byte[] temp = new byte[this.MD4_PARTSIZE];       
        RandomAccessFile raf = new RandomAccessFile(file,"r");

        long left = fileLen;
        while(left>=this.MD4_PARTSIZE){
        	raf.readFully(temp,0,this.MD4_PARTSIZE);
        	left = left - this.MD4_PARTSIZE;
 			byte[] part = MD4.digest(temp);
 			al.add(part);       	
    	}
        if(left==0){
        	byte[] part = MD4.digest("".getBytes());
        	al.add(part);    
        }else if(left<this.MD4_PARTSIZE){       	
        	int k = Integer.parseInt(String.valueOf(left));
        	temp = new byte[k];
        	raf.readFully(temp,0,k);
        	byte[] part = MD4.digest(temp);
 			al.add(part);
        }
        raf.close();

        byte[] last = new byte[16*(al.size())];
        for(int i=0;i<al.size();i++){	
  			byte[] soruce = (byte[])al.get(i);
  			System.arraycopy(soruce,0,last,i*16,16);	
        }
        
        if(fileLen < this.MD4_PARTSIZE){
        	return this.hexToString(last);
        }else{
        	return this.hexToString(MD4.digest(last));	
        }
	}

	/* 
	 * @see net.health.vgo.md4.IMD4#addURL(java.lang.String)
	 */
	public String addURL(String url) throws Exception {
		if( url == null || url.length() == 0 )
			throw new Exception("url can't be empty!");
		byte[] bytes = url.getBytes();
		byte[] part = MD4.digest(bytes);
		return this.hexToString(part);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO �Զ���ɷ������

	}

}
