package org.jzkangta.tlspc.wechat.entity;

import java.util.ArrayList;
import java.util.List;

public class WhiteList {
	
	private List<String> openid = new ArrayList<String>();
	private List<String> username = new ArrayList<String>();
	public List<String> getOpenid() {
		return openid;
	}
	public void setOpenid(List<String> openid) {
		this.openid = openid;
	}
	public List<String> getUsername() {
		return username;
	}
	public void setUsername(List<String> username) {
		this.username = username;
	}

}
