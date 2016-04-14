package org.jzkangta.tlspc.framework.web.view;

import java.util.Map;

import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * 重写MappingJacksonJsonView方法.
 * MappingJacksonJsonView会返回 {model类名:{内容}}
 * 重写后返回{内容} 
 * @author Administrator
 *
 */
public class CustomMappingJacksonJsonView extends MappingJackson2JsonView {
	
	@Override  
    protected Object filterModel(Map<String, Object> model) {  
        Map<?, ?> result = (Map<?, ?>) super.filterModel(model);  
        if (result.size() == 1) {  
            return result.values().iterator().next();  
        } else {  
            return result;  
        }  
    }  

}
