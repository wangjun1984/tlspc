package org.jzkangta.tlspc.wechat.entity;

public class Department {
	
	private int id ;//修改时用到，新增时不用
	
	private String name;
	
	private int parentid;//修改时不用，新增时用到

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParentid() {
		return parentid;
	}

	public void setParentid(int parentid) {
		this.parentid = parentid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
