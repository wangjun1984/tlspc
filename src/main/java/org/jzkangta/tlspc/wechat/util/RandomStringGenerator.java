package org.jzkangta.tlspc.wechat.util;

import java.util.Random;


public class RandomStringGenerator {

    /**
     * 获取一定长度的随机字符串
     * @param length 指定字符串长度
     * @param type 0-字母数字 1-纯字母 2-纯数字
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length,int type) {
        String base = "";
        if(type == 0){
        	base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        }else if(type == 1){
        	base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }
        else if(type == 2){
        	base = "0123456789";
        }
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
