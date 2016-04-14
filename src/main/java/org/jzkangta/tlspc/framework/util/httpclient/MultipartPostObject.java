package org.jzkangta.tlspc.framework.util.httpclient;

import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

public class MultipartPostObject extends PostObject{
	
	private String[] fileBodyName ;
	
	private FileBody[] fileBody;
	
	private String[] fieldName;
	
	private StringBody[] fieldValue;

	public String[] getFileBodyName() {
		return fileBodyName;
	}

	public void setFileBodyName(String[] fileBodyName) {
		this.fileBodyName = fileBodyName;
	}

	public FileBody[] getFileBody() {
		return fileBody;
	}

	public void setFileBody(FileBody[] fileBody) {
		this.fileBody = fileBody;
	}

	public String[] getFieldName() {
		return fieldName;
	}

	public void setFieldName(String[] fieldName) {
		this.fieldName = fieldName;
	}

	public StringBody[] getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(StringBody[] fieldValue) {
		this.fieldValue = fieldValue;
	} 
	
	
	
	
}
