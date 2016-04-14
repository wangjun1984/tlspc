package org.jzkangta.tlspc.framework.util;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 翻页HashMap
 * @version 1.0
 */
public class PageHashMap<K,V> extends HashMap<K,V> implements Serializable{

	private static final long serialVersionUID = 1963503083952338317L;
	private PageTurn pageTurn;

    /**
     * @return 翻页信息
     */
    public PageTurn getPageTurn() {
            return pageTurn;
    }

    /**
     * @param pageTurn 要设置的翻页信息
     */
    public void setPageTurn(PageTurn pageTurn) {
            this.pageTurn = pageTurn;
    }

    public String toString() {
            return "pageHashMap:" + super.toString() +",pageTurn:"+ pageTurn.toString();
    }

}
