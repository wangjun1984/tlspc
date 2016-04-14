package org.jzkangta.tlspc.framework.util;

import java.io.Serializable;

/**
 * 翻页信息
 * 
 * @version 1.0
 */
public class PageTurn implements Serializable {

	private static final long serialVersionUID = -3078109584161867959L;

	private Integer firstPage = new Integer(0); // 首页数

	private Integer pageCount = new Integer(0); // 页数

	private Integer page = new Integer(0);// 当前页

	private Integer nextPage = new Integer(0);// 下一页

	private Integer prevPage = new Integer(0);// 上一页

	private Integer start = new Integer(0);// 当前页开始条

	private Integer end = new Integer(0);// 当前页结束条

	private Integer pageSize = new Integer(0);// 每页显示的条数

	private Integer rowCount = new Integer(0);// 记录数
	
	public static final int DEFAULT_PAGE_SIZE=1000;

	/**
	 * 空的构造函数
	 */
	public PageTurn() {
		this.page=1;
		this.pageSize=DEFAULT_PAGE_SIZE;
	}

	/**
	 * 构造函数，初始化
	 * 
	 * @param page
	 *            当前页编号，从1开始编号
	 * @param pagesize
	 *            每页显示的条数
	 */
	public PageTurn(int page, int pagesize) {
		initWithPage(page, pagesize, (page > 0 ? page : 1) * pagesize);
	}

	/**
	 * 构造函数，初始化
	 * 
	 * @param page
	 *            当前页编号，从1开始编号
	 * @param pagesize
	 *            每页显示的条数
	 * @param rowscount
	 *            总的记录条数
	 */
	public PageTurn(int page, int pagesize, int rowscount) {
		initWithPage(page, pagesize, rowscount);
	}

	/**
	 * 初始化
	 * 
	 * @param page
	 *            当前页编号，从1开始编号
	 * @param pagesize
	 *            每页显示的条数
	 * @param rowscount
	 *            总的记录条数
	 */
	public void initWithPage(int page, int pagesize, int rowscount) {
		initWithStart((page - 1) * pagesize, pagesize, rowscount);
	}

	/**
	 * 初始化
	 * 
	 * @param startRow
	 *            开始记录数
	 * @param pagesize
	 *            每页显示的条数
	 * @param rowscount
	 *            总的记录条数
	 */
	public void initWithStart(int startRow, int pagesize, int rowscount) {
		// System.out.println("startRow:"+startRow+",pagesize:"+pagesize+",rowscount:"+rowscount);

		if (rowscount <= 0 || pagesize <= 0)
			return;

		this.firstPage = new Integer(1);
		this.rowCount = new Integer(rowscount);
		// this.pageSize = new Integer( pagesize > rowscount ? rowscount :
		// pagesize );
		this.pageSize = new Integer(pagesize);

		int startResult = startRow < 0 ? 0 : startRow;
		if (startResult > rowscount) {
			// startResult = rowscount - pagesize;
			startResult = rowscount - rowscount % pagesize;
		}
		if (startResult < 0) {
			startResult = 0;
		}
		this.start = new Integer(startResult);

		this.pageCount = new Integer((int) Math.ceil((double) rowscount
				/ pagesize));

		int pageResult = startResult / pagesize + 1;
		if (pageResult > pageCount.intValue())
			pageResult = pageCount.intValue();
		this.page = new Integer(pageResult);

		int nextpageResult = pageResult + 1;
		nextpageResult = nextpageResult >= pageCount.intValue() ? pageCount
				.intValue() : nextpageResult;
		this.nextPage = new Integer(nextpageResult);

		int endResult = startResult + pagesize;
		endResult = endResult > rowscount ? rowscount : endResult;
		this.end = new Integer(endResult);

		this.prevPage = new Integer(pageResult > 1 ? (pageResult - 1) : 1);
	}

	/**
	 * @return 第一页编号
	 */
	public Integer getFirstPage() {
		return firstPage;
	}

	/**
	 * @return 本页结束记录编号
	 */
	public Integer getEnd() {
		return end;
	}

	/**
	 * @return 当前页编号
	 */
	public Integer getPage() {
		return page;
	}

	/**
	 * @return 当前页编号
	 */
	public Integer getCurrentPage() {
		return page;
	}

	/**
	 * @return 下页编号
	 */
	public Integer getNextPage() {
		return nextPage;
	}

	/**
	 * @return 总页数
	 */
	public Integer getPageCount() {
		return pageCount;
	}

	/**
	 * @return 每页显示的记录条数
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @return 上页编号
	 */
	public Integer getPrevPage() {
		return prevPage;
	}

	/**
	 * @return 总记录数
	 */
	public Integer getRowCount() {
		return rowCount;
	}

	/**
	 * @return 本页开始记录编号
	 */
	public Integer getStart() {
		return start;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("{rowstart:").append(start)
				.append(",rowend:").append(end).append(",prevpage:")
				.append(prevPage).append(",currentpage:").append(page)
				.append(",nextpage:").append(nextPage).append(",pagesize:")
				.append(pageSize).append(",pagecount:").append(pageCount)
				.append(",rowcount:").append(rowCount).append("}");
		return sb.toString();
	}

	private String goPage(String pageUrl, int pageNo) {
		return pageUrl.replace("{pageNo}", String.valueOf(pageNo));
	}

	/**
	 * 输出显示分页导航信息
	 * 
	 * @param pageUrl
	 *            翻页的url，可包含参数占位符{pageNo}
	 * @param delta
	 *            翻多页的具体数量，如下5页，上5页
	 * @param arrowString
	 *            上页下页的显示箭头，额可以是文字或图片的
	 * @param selectPager
	 *            是否可以直接选取某个页面范围
	 * @return html代码片段
	 */
	public String toPageString(String pageUrl, int delta, String[] arrowString,
			boolean selectPager) {
		StringBuffer result = new StringBuffer();

		if (delta < 1 || delta > 10)
			delta = 10;
		if (StringUtil.isEmpty(pageUrl))
			pageUrl = "javascript:gopage({pageNo});";

		// String[] img = { "《","<",">","》" };
		String[] img = { "上" + delta + "页", "上页", "下页", "下" + delta + "页", "转到" };
		if (arrowString != null && arrowString.length == 5) {
			img = arrowString;
		}

		int num = page.intValue() % delta;
		int nextDelta = page.intValue() + 1 + (num == 0 ? 0 : (delta - num));

		if (page.intValue() > delta && !StringUtil.isEmpty(img[0]))
			result.append("<a href='")
					.append(goPage(pageUrl, page.intValue() - delta))
					.append("' title=\"前").append(delta)
					.append("页\" class='deltapage'>").append(img[0])
					.append("</a> ");
		if (page.intValue() > 1 && !StringUtil.isEmpty(img[1]))
			result.append("<a href='")
					.append(goPage(pageUrl, page.intValue() - 1))
					.append("' title=\"上一页\" class='nextpage'>").append(img[1])
					.append("</a> ");

		for (int i = Math.max(nextDelta - delta, 1); i <= pageCount.intValue()
				&& i < nextDelta; i++) {
			if (i == page.intValue())
				result.append("<a href='").append(goPage(pageUrl, i))
						.append("' title=\"第").append(i)
						.append("页\" class='cpage'><b>").append(i)
						.append("</b></a>");
			else
				result.append("<a href='").append(goPage(pageUrl, i))
						.append("' title=\"第").append(i)
						.append("页\" class='page'>").append(i).append("</a>");
		}

		if (page.intValue() < pageCount.intValue()
				&& !StringUtil.isEmpty(img[2]))
			result.append("<a href='")
					.append(goPage(pageUrl, page.intValue() + 1))
					.append("' title=\"下一页\" class='nextpage'>").append(img[2])
					.append("</a> ");
		if (nextDelta < pageCount.intValue() && !StringUtil.isEmpty(img[3]))
			result.append("<a href='").append(goPage(pageUrl, nextDelta))
					.append("' title=\"后").append(delta)
					.append("页\" class='deltapage'>").append(img[3])
					.append("</a> ");

		if (selectPager && !StringUtil.isEmpty(img[4])) {
			result.append(
					"<span onclick='javascript:gopage(this.nextSibling.value)' class=gopage>")
					.append(img[4])
					.append("</span><select name=pgs size=1 onChange='gopage(this.options[this.selectedIndex].value)' class=select>");
			for (int i = 1; i <= pageCount.intValue(); i += delta) {
				result.append("<option value=")
						.append(i)
						.append(((page.intValue() >= i && page.intValue() < (i + delta)) ? "selected"
								: "")).append(">").append(i).append("-")
						.append(Math.min(i + delta, pageCount.intValue()))
						.append("</option>");
			}
			result.append("</select>页");
		} else if (!StringUtil.isEmpty(img[4])) {
			result.append(
					"<span onclick='javascript:gopage(this.nextSibling.value)' class=gopage>")
					.append(img[4])
					.append("</span><input name=pgi type=text size=3 value=\"")
					.append(page.intValue()).append("\" class=textinput>页");
		}

		return result.toString();
	}

	/**
	 * 输出显示分页导航信息
	 * 
	 * @param delta
	 *            翻多页的具体数量，如下5页，上5页
	 * @param arrowString
	 *            上页下页的显示箭头，额可以是文字或图片的
	 * @param selectPager
	 *            是否可以直接选取某个页面范围
	 * @return html代码片段
	 */
	public String toPageString(int delta, String[] arrowString,
			boolean selectPager) {
		return toPageString(null, delta, arrowString, selectPager);
	}

	public void setFirstPage(int firstPage) {
		this.firstPage = firstPage;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public void setNextPage(Integer nextPage) {
		this.nextPage = nextPage;
	}

	public void setPrevPage(Integer prevPage) {
		this.prevPage = prevPage;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	/*
	 * public static void main( String args[] ){ PageTurn turn =new PageTurn();
	 * turn.initWithPage( 0, 11, 120 ); System.out.println( turn ); }
	 */

	
	
	
}
