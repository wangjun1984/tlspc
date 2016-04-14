package org.jzkangta.tlspc.framework.util.httpclient;

/**
 * 平台ID
 */
public enum ResponseContentTypeEnum {
	
	CONTENT_TYPE_EZ("application/andrew-inset","application"),
	CONTENT_TYPE_HQX("application/mac-binhex40","application"),
	CONTENT_TYPE_CPT("application/mac-compactpro","application"),
	CONTENT_TYPE_DOC("application/msword","application"),
	CONTENT_TYPE_BIN("application/octet-stream","application"),
	CONTENT_TYPE_DMS("application/octet-stream","application"),
	CONTENT_TYPE_LHA("application/octet-stream","application"),
	CONTENT_TYPE_LZH("application/octet-stream","application"),
	CONTENT_TYPE_EXE("application/octet-stream","application"),
	CONTENT_TYPE_CLASS("application/octet-stream","application"),
	CONTENT_TYPE_SO("application/octet-stream","application"),
	CONTENT_TYPE_DLL("application/octet-stream","application"),
	CONTENT_TYPE_ODA("application/oda","application"),
	CONTENT_TYPE_PDF("application/pdf","application"),
	CONTENT_TYPE_AI("application/postscript","application"),
	CONTENT_TYPE_EPS("application/postscript","application"),
	CONTENT_TYPE_PS("application/postscript","application"),
	CONTENT_TYPE_SMI("application/smil","application"),
	CONTENT_TYPE_SMIL("application/smil","application"),
	CONTENT_TYPE_MIF("application/vnd.mif","application"),
	CONTENT_TYPE_XLS("application/vnd.ms-excel","application"),
	CONTENT_TYPE_PPT("application/vnd.ms-powerpoint","application"),
	CONTENT_TYPE_WBXML("application/vnd.wap.wbxml","application"),
	CONTENT_TYPE_WMLC("application/vnd.wap.wmlc","application"),
	CONTENT_TYPE_WMLSC("application/vnd.wap.wmlscriptc","application"),
	CONTENT_TYPE_BCPIO("application/x-bcpio","application"),
	CONTENT_TYPE_VCD("application/x-cdlink","application"),
	CONTENT_TYPE_PGN("application/x-chess-pgn","application"),
	CONTENT_TYPE_CPIO("application/x-cpio","application"),
	CONTENT_TYPE_CSH("application/x-csh","application"),
	CONTENT_TYPE_DCR("application/x-director","application"),
	CONTENT_TYPE_DIR("application/x-director","application"),
	CONTENT_TYPE_DXR("application/x-director","application"),
	CONTENT_TYPE_DVI("application/x-dvi","application"),
	CONTENT_TYPE_SPL("application/x-futuresplash","application"),
	CONTENT_TYPE_GTAR("application/x-gtar","application"),
	CONTENT_TYPE_HDF("application/x-hdf","application"),
	CONTENT_TYPE_JS("application/x-javascript","application"),
	CONTENT_TYPE_SKP("application/x-koan","application"),
	CONTENT_TYPE_SKD("application/x-koan","application"),
	CONTENT_TYPE_SKT("application/x-koan","application"),
	CONTENT_TYPE_SKM("application/x-koan","application"),
	CONTENT_TYPE_LATEX("application/x-latex","application"),
	CONTENT_TYPE_NC("application/x-netcdf","application"),
	CONTENT_TYPE_CDF("application/x-netcdf","application"),
	CONTENT_TYPE_SH("application/x-sh","application"),
	CONTENT_TYPE_SHAR("application/x-shar","application"),
	CONTENT_TYPE_SWF("application/x-shockwave-flash","application"),
	CONTENT_TYPE_SIT("application/x-stuffit","application"),
	CONTENT_TYPE_SV4CPIO("application/x-sv4cpio","application"),
	CONTENT_TYPE_SV4CRC("application/x-sv4crc","application"),
	CONTENT_TYPE_TAR("application/x-tar","application"),
	CONTENT_TYPE_TCL("application/x-tcl","application"),
	CONTENT_TYPE_TEX("application/x-tex","application"),
	CONTENT_TYPE_TEXINFO("application/x-texinfo","application"),
	CONTENT_TYPE_TEXI("application/x-texinfo","application"),
	CONTENT_TYPE_T("application/x-troff","application"),
	CONTENT_TYPE_TR("application/x-troff","application"),
	CONTENT_TYPE_ROFF("application/x-troff","application"),
	CONTENT_TYPE_MAN("application/x-troff-man","application"),
	CONTENT_TYPE_ME("application/x-troff-me","application"),
	CONTENT_TYPE_MS("application/x-troff-ms","application"),
	CONTENT_TYPE_USTAR("application/x-ustar","application"),
	CONTENT_TYPE_SRC("application/x-wais-source","application"),
	CONTENT_TYPE_XHTML("application/xhtml+xml","application"),
	CONTENT_TYPE_XHT("application/xhtml+xml","application"),
	CONTENT_TYPE_ZIP("application/zip","application"),
	CONTENT_TYPE_JSON("application/json","application"),
	//音频文件
	CONTENT_TYPE_AU("audio/basic","audio"),
	CONTENT_TYPE_SND("audio/basic","audio"),
	CONTENT_TYPE_MID("audio/midi","audio"),
	CONTENT_TYPE_MIDI("audio/midi","audio"),
	CONTENT_TYPE_KAR("audio/midi","audio"),
	CONTENT_TYPE_MPGA("audio/mpeg","audio"),
	CONTENT_TYPE_MP2("audio/mpeg","audio"),
	CONTENT_TYPE_MP3("audio/mpeg","audio"),
	CONTENT_TYPE_AIF("audio/x-aiff","audio"),
	CONTENT_TYPE_AIFF("audio/x-aiff","audio"),
	CONTENT_TYPE_AIFC("audio/x-aiff","audio"),
	CONTENT_TYPE_M3U("audio/x-mpegurl","audio"),
	CONTENT_TYPE_RAM("audio/x-pn-realaudio","audio"),
	CONTENT_TYPE_RM("audio/x-pn-realaudio","audio"),
	CONTENT_TYPE_RPM("audio/x-pn-realaudio-plugin","audio"),
	CONTENT_TYPE_RA("audio/x-realaudio","audio"),
	CONTENT_TYPE_WAV("audio/x-wav","audio"),
	
	CONTENT_TYPE_PDB("chemical/x-pdb","chemical"),
	CONTENT_TYPE_XYZ("chemical/x-xyz","chemical"),
	//图片
	CONTENT_TYPE_BMP("image/bmp","image"),
	CONTENT_TYPE_GIF("image/gif","image"),
	CONTENT_TYPE_IEF("image/ief","image"),
	CONTENT_TYPE_JPEG("image/jpeg","image"),
	CONTENT_TYPE_JPG("image/jpeg","image"),
	CONTENT_TYPE_JPE("image/jpeg","image"),
	CONTENT_TYPE_PNG("image/PNG","image"),
	CONTENT_TYPE_TIFF("image/tiff","image"),
	CONTENT_TYPE_TIF("image/tiff","image"),
	CONTENT_TYPE_DJVU("image/vnd.djvu","image"),
	CONTENT_TYPE_DJV("image/vnd.djvu","image"),
	CONTENT_TYPE_WBMP("image/vnd.wap.wbmp","image"),
	CONTENT_TYPE_RAS("image/x-cmu-raster","image"),
	CONTENT_TYPE_PNM("image/x-portable-anymap","image"),
	CONTENT_TYPE_PBM("image/x-portable-bitmap","image"),
	CONTENT_TYPE_PGM("image/x-portable-graymap","image"),
	CONTENT_TYPE_PPM("image/x-portable-pixmap","image"),
	CONTENT_TYPE_RGB("image/x-rgb","image"),
	CONTENT_TYPE_XBM("image/x-xbitmap","image"),
	CONTENT_TYPE_XPM("image/x-xpixmap","image"),
	CONTENT_TYPE_XWD("image/x-xwindowdump","image"),
	
	CONTENT_TYPE_IGS("model/iges","model"),
	CONTENT_TYPE_IGES("model/iges","model"),
	CONTENT_TYPE_MSH("model/mesh","model"),
	CONTENT_TYPE_MESH("model/mesh","model"),
	CONTENT_TYPE_SILO("model/mesh","model"),
	CONTENT_TYPE_WRL("model/vrml","model"),
	CONTENT_TYPE_VRML("model/vrml","model"),
	
	CONTENT_TYPE_MPEG("video/mpeg","video"),
	CONTENT_TYPE_MPG("video/mpeg","video"),
	CONTENT_TYPE_MPE("video/mpeg","video"),
	CONTENT_TYPE_QT("video/quicktime","video"),
	CONTENT_TYPE_MOV("video/quicktime","video"),
	CONTENT_TYPE_MXU("video/vnd.mpegurl","video"),
	CONTENT_TYPE_AVI("video/x-msvideo","video"),
	CONTENT_TYPE_MOVIE("video/x-sgi-movie","video"),
	CONTENT_TYPE_ICE("x-conference/x-cooltalk","video"),
	
	CONTENT_TYPE_CSS("text/css","text"),
	CONTENT_TYPE_HTML("text/html","text"),
	CONTENT_TYPE_HTM("text/html","text"),
	CONTENT_TYPE_ASC("text/plain","text"),
	CONTENT_TYPE_TXT("text/plain","text"),
	CONTENT_TYPE_RTX("text/richtext","text"),
	CONTENT_TYPE_RTF("text/rtf","text"),
	CONTENT_TYPE_SGML("text/sgml","text"),
	CONTENT_TYPE_SGM("text/sgml","text"),
	CONTENT_TYPE_TSV("text/tab-separated-values","text"),
	CONTENT_TYPE_WML("text/vnd.wap.wml","text"),
	CONTENT_TYPE_WMLS("text/vnd.wap.wmlscript","text"),
	CONTENT_TYPE_ETX("text/x-setext","text"),
	CONTENT_TYPE_XSL("text/xml","text"),
	CONTENT_TYPE_XML("text/xml","text"),
	CONTENT_TYPE_APPLICATION_XML("application/xml","text"),
	;
	
	
    String type;
    String name;
    ResponseContentTypeEnum(String name,String type){
        this.type = type;
        this.name = name;
    }
    
    public static ResponseContentTypeEnum getByName(String name) {
    	for(ResponseContentTypeEnum e: values()) {
    		if(name.equalsIgnoreCase(e.getName())) {
    			return e;
    		}
    	}
    	return null;
    }

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
