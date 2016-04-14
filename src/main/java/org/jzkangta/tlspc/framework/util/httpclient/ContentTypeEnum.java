package org.jzkangta.tlspc.framework.util.httpclient;

public enum ContentTypeEnum {
	JSON("application/json"),
	XML("text/xml"),
	WWW_FORM("application/x-www-form-urlencoded"),
	MULTIPART_FORM("multipart/form-data"),
	IMAGE("image/jpeg");
	
	String type;
	
	ContentTypeEnum(String type){
        this.type = type;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}
}
