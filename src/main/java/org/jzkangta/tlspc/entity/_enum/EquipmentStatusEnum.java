package org.jzkangta.tlspc.entity._enum;

import java.util.HashMap;
import java.util.Map;


/**
 * @author xiaohei
 *	设备状态
 *{@link RecordStatusEnum}
 */
@Deprecated
public enum EquipmentStatusEnum {
	ALL(0,"全部"),
	YES(1,"可用"),
	NO(2,"禁用");
	
	int id;
    String name;
    
    EquipmentStatusEnum(int id,String name){
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
	
	public static Map<Integer,String> listAllTypes() {
		Map<Integer, String> typesMap = new HashMap<Integer, String>();
		EquipmentStatusEnum[] enums = values();
		for(EquipmentStatusEnum object : enums) {
			typesMap.put(object.getId(), object.getName());
		}
		return typesMap;
	}
}
