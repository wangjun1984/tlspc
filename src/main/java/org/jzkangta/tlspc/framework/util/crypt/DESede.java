package org.jzkangta.tlspc.framework.util.crypt;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
解密
 */
public class DESede {
    private static String Algorithm = "DESede";//加密算法的名称
    /**
     * 对 Byte 数组进行解密
     * @param buff 要解密的数据
     * @return 返回加密后的 String
     * @throws IOException 
     */
     public static String createDecryptor(String parm, String key) throws
      NoSuchPaddingException, NoSuchAlgorithmException,InvalidKeySpecException,
      IOException {
    	 sun.misc.BASE64Decoder dec=new sun.misc.BASE64Decoder();
         byte[] dKey = dec.decodeBuffer(key);//对base64编码的string解码成byte数组
         byte[] cipherByte = null;
        try {
        	SecretKey deskey=new javax.crypto.spec.SecretKeySpec(dKey,Algorithm);
        	Cipher c = Cipher.getInstance(Algorithm);
            byte[] dnParm = dec.decodeBuffer(parm);//对base64编码的string解码成byte数组
            c.init(Cipher.DECRYPT_MODE, deskey);//初始化密码器，用密钥deskey,进入解密模式
            cipherByte = c.doFinal(dnParm);
        }
        catch(java.security.InvalidKeyException ex){
        	
            ex.printStackTrace();
        }
        catch(javax.crypto.BadPaddingException ex){
            ex.printStackTrace();
        }
        catch(javax.crypto.IllegalBlockSizeException ex){
            ex.printStackTrace();
        }
        return (new String(cipherByte,"UTF-8"));
     }
     
//     public static void main(String args[]) throws IOException,
//      NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException,
//      InvalidKeyException, IOException {
//    	 DESede des = new DESede();
////    	 des.getKey("VOjqgrAi0Q6vxwYMHDtX6izGxjaxQfBi");
////    	 byte[] dBy = des.deBase64("i5YNJo+5gvB9ys4L8s77dPwy9isZgVUNvuIileDuCwo=");
//    	 String dStr = des.createDecryptor("i5YNJo+5gvB9ys4L8s77dPwy9isZgVUNvuIileDuCwo=","VOjqgrAi0Q6vxwYMHDtX6izGxjaxQfBi");
//	     System.out.println("解："+dStr);
//     }
}
