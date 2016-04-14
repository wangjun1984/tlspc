package org.jzkangta.tlspc.framework.util.file;

/**
 * 多媒体文件类型枚举
 * @author hongyuhao
 *
 */
public enum MediaTypeEnum {
	 
	MEDIA_IMAGE(1, "image"),   //多媒体文件：图片
	MEDIA_VOICE(2, "voice"),   //多媒体文件：音频
	MEDIA_VIDEO(3, "video"),   //多媒体文件：视频
	MEDIA_FILE(4, "file");	   //多媒体文件：普通文件
    
    int id;
    String name;
    MediaTypeEnum(int id,String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static boolean isHave(int id){
         for(MediaTypeEnum goodsEnum:MediaTypeEnum.values()){
             if(id == goodsEnum.getId()){
                 return true;
             }
         }
         return false;
    }
}
