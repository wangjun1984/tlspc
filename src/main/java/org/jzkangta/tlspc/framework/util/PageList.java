package org.jzkangta.tlspc.framework.util;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 翻页列表
 * 
 * @version 1.0
 */
public class PageList<E> extends ArrayList<E> implements Serializable {

	private static final long serialVersionUID = -7317702765506896103L;

	private PageTurn pageTurn;

	public PageList() {

	}

	public PageList(int page, int pagesize, int rowscount) {
		pageTurn = new PageTurn(page, pagesize, rowscount);
	}

	/**
	 * @return 翻页信息
	 */
	public PageTurn getPageTurn() {
		return pageTurn;
	}

	/**
	 * @param pageTurn
	 *            要设置的翻页信息
	 */
	public void setPageTurn(PageTurn pageTurn) {
		this.pageTurn = pageTurn;
	}

	public String toString() {
		return "pageList:" + super.toString() + ",pageTurn:" + pageTurn;
	}

}
