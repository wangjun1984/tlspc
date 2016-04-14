package org.jzkangta.tlspc.framework.util.emulehash;


public class MD4Factory {
	private static IMD4 md4 = null;
	
	public synchronized static IMD4 getInstance() throws Exception{
		if(md4==null){
			md4 = new MD4SimpleImpl();
		}
		return md4;
	}
}
