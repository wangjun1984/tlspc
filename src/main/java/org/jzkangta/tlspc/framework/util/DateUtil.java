/********************************************************************
 * 文件名：DateUtil.java
 * 功能描述：Java日期的相关操作类
 * 版本号： 1.0
 * 创建日期：
 * 修改记录：
 * 
 ********************************************************************/
package org.jzkangta.tlspc.framework.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.springframework.context.i18n.LocaleContextHolder;

import org.jzkangta.tlspc.framework.util.contants.Constants;

/**
 * <p><b>Java日期类型相关操作类</b></p>
 * <p>该类负责对日期格式化转换、日期比较、日期加减、润年判断、获取相关的日期信息等。</p>
 *
 */

public class DateUtil {
	private static String defaultDatePattern = null;
	
	public static final long ONE_MINUTE_MILLISECOND = 60*1000L;
	public static final long ONE_HOUR_MILLISECOND = 60 * ONE_MINUTE_MILLISECOND;
	/**
	 * 一天所对应的毫秒数
	 */
	public static final long ONE_DAY_MILLISECOND = 24 * ONE_HOUR_MILLISECOND;
	
	/**
	 * 一周所对应的毫秒数
	 */
	public static final long ONE_WEEK_MILLISECOND = 7* ONE_DAY_MILLISECOND;
	public static final long ONE_MONTH_MILLISECOND = 30* ONE_DAY_MILLISECOND;
	public static final long ONE_YEAR_MILLISECOND = 365* ONE_DAY_MILLISECOND;
	
	 /**
	    * 得到系统当前时间的Timestamp对象
	    * @return  系统当前时间的Timestamp对象
	    */
	   public static Timestamp getCurrTimestamp()
	   {
	      return new Timestamp(System.currentTimeMillis());
	   }
	      
	   /**
	    * 获取当前unix时间的秒数。
	    * @return
	    */
	   public static long getCurrentUnixTimeSecond(){
		   return getCurrTimestamp().getTime()/1000;
	   }
	/**
	 * 从配置文件中返回配置项"date.format",默认的日期格式符 (yyyy-MM-dd)，
	 * @return a string representing the date pattern on the UI
	 */
	public static synchronized String getDatePattern() {
		Locale locale = LocaleContextHolder.getLocale();
		try {
			defaultDatePattern = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale).getString("date.format");
		} catch (MissingResourceException mse) {
			defaultDatePattern = "yyyy-MM-dd";
		}
		
		return defaultDatePattern;
	}
	
	/**
	 * 获取日期的年份
	 * @param date
	 * @return 日期的年份
	 */
	public static int getYear( Date date) {
		return getCalendar(date).get( Calendar.YEAR );
	}

	/**
	 * 获取日期的月份（0-11）
	 * @param date
	 * @return 日期的月份（0-11）
	 */
	public static int getMonth( Date date) {
		return getCalendar(date).get( Calendar.MONTH );
	}
	
	/**
	 * 获取当前日期是第几周
	 * @param date
	 * @return
	 */
	public static int getWeekOfMonth( Date date) {
		return getCalendar(date).get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * 获取日期的一个月中的某天
	 * @param date
	 * @return 日期的一个月中的某天(1-31)
	 */
	public static int getDay( Date date) {
		return getCalendar(date).get( Calendar.DATE );
	}

	/**
	 * 获取日期的一个星期中的某天
	 * @param date
	 * @return 日期的星期中日期(1:sunday-7:SATURDAY)
	 */
	public static int getWeek( Date date) {
		return getCalendar(date).get( Calendar.DAY_OF_WEEK );
	}
	
	/**
	 * 获取日期的一天中的某个小时
	 * @param date
	 * @return 日期的一个天中的某个小时(1-24)
	 */
	public static int getHour( Date date) {
		return getCalendar(date).get( Calendar.HOUR_OF_DAY );
	}
	
	/**
	 * 获取日期的一个小时中的某个分钟
	 * @param date
	 * @return 日期的一个小时中的某个分钟(0-60)
	 */
	public static int getMinutes( Date date) {
		return getCalendar(date).get( Calendar.MINUTE );
	}
	
	/**
	 * 获取日期的当月1号所对应的星期中的某天
	 * @param date
	 * @return 日期的星期中日期(1:sunday-7:SATURDAY)
	 */
	public static int getWeekOfFirstDayOfMonth( Date date) {
		return getWeek( getFirstDayOfMonth( date ) );
	}
	
	/**
	 * 获取日期的当月最后1天所对应的星期中的某天
	 * @param date
	 * @return 日期的星期中日期(1:sunday-7:SATURDAY)
	 */
	public static int getWeekOfLastDayOfMonth( Date date) {
		return getWeek( getLastDayOfMonth( date ) );
	}
	
	/**
	 * 将日期字符串按指定的格式转为Date类型
	 * 
	 * @param strDate 待解析的日期字符串
	 * @param format 日期格式
	 * @return 字符串对应的日期对象
	 * @see {@link #parseDate(String, String, boolean)}
	 */
	public static final Date parseDate(String strDate , String format)  {
		return parseDate(strDate, format, true);
	}

	/**
	 * 将日期字符串按指定的格式转为Date类型
	 * 
	 * @param strDate
	 *            待解析的日期字符串
	 * @param format
	 *            日期格式
	 * @param isLenient
	 *            false时表示为严格的日期格式，如2013年1月33日将会抛出错误；true时表示为宽松的日期格式，
	 *            如2013年1月33日实际会转换成2013年1月1日之后的32天，即2013年2月2日
	 * @return 字符串对应的日期对象
	 * @see {@link DateFormat#setLenient(boolean)}
	 */
	public static final Date parseDate(String strDate , String format, boolean isLenient)  {
		SimpleDateFormat df = new SimpleDateFormat(format);
		df.setLenient(isLenient);
		try {
			return df.parse(strDate);
		} catch (ParseException pe) {
			return null;
		}
	}
	
	/**
	 * 当前日期自然周计算
	 * @param date
	 * @param week 小于0表示往前退的周数，大于零表示往后退的周数
	 * @return
	 */
	public static final Date afterWeek(Date date, int week) {
		Calendar calendar = getCalendar(date);
		calendar.setWeekDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR) + week, calendar.get(Calendar.DAY_OF_WEEK));
		return calendar.getTime();
	}
	
	/**
	 * 当前日期自然月计算
	 * @param date
	 * @param month 小于0表示往前退的月数，大于零表示往后退的月数
	 * @return
	 */
	public static final Date afterMonth(Date date, int month) {
		Calendar calendar = getCalendar(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + month, calendar.get(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	/**
	 * 将日期字符串按系统配置中指定默认格式(getDatePattern()返回的格式)转为Date类型
	 * 
	 * @param strDate 待解析的日期字符串
	 * @return 字符串对应的日期对象
	 */
	public static Date parseDate(String strDate) {
		return parseDate( strDate, getDatePattern() );
	}
	
	
	/**
	 * <p>检查所给的年份是否是闰年</p>
	 * 
	 * @param  year 年
	 * @return 检查结果: true - 是闰年; false - 是平年
	 */
	public static boolean isLeapYear(int year) {
		if (year / 4 * 4 != year) {
			return false; //不能被4整除
		}
		else if (year / 100 * 100 != year) {
			return true; //能被4整除，不能被100整除
		}
		else if (year / 400 * 400 != year) {
			return false; //能被100整除，不能被400整除
		}
		else {
			return true; //能被400整除
		}
	}
	
	
	/**
	 * 按照默认格式化样式格式化当前系统时间
	 * @return 日期字符串
	 */
	public static String getCurrentTime(){
		return formatDate( new Date() );
	}

	/**
	 * 按照默认格式化样式格式化当前系统时间
	 * @param format String 日期格式化标准
	 * @return String 日期字符串。
	 */
	public static String getCurrentTime(String format){
		return formatDate( new Date(), format );
	}
	
	
	/**
	 * 按照指定格式化样式格式化指定的日期
	 * @param date 待格式化的日期
	 * @param format 日期格式
	 * @return 日期字符串
	 */
	public static String formatDate(Date date, String format){
		if( date == null ) date = new Date();
		if( format == null ) format = getDatePattern();
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);
		return formatter.format( date );
	}
	
	/**
	 * 按照默认格式化样式格式化指定的日期
	 * @param date 待格式化的日期
	 * @return 日期字符串
	 */
	public static String formatDate( Date date ){
		long offset = System.currentTimeMillis() - date.getTime();
		String pos = "前";
		if( offset < 0 ){
			pos = "后";
			offset = -offset;
		}
		if( offset >= ONE_YEAR_MILLISECOND )
			return formatDate( date,getDatePattern() );
		
		StringBuilder sb = new StringBuilder();
		if( offset >= 2*ONE_MONTH_MILLISECOND ){
			return sb.append( (offset+ONE_MONTH_MILLISECOND/2)/ONE_MONTH_MILLISECOND ).append( "个月" ).append( pos ).toString();
		}
		if( offset > ONE_WEEK_MILLISECOND ){
			return sb.append( (offset+ONE_WEEK_MILLISECOND/2)/ONE_WEEK_MILLISECOND ).append( "周" ).append( pos ).toString();
		}
		if( offset > ONE_DAY_MILLISECOND ){
			return sb.append( (offset+ONE_DAY_MILLISECOND/2)/ONE_DAY_MILLISECOND ).append( "天" ).append( pos ).toString();
		}
		if( offset > ONE_HOUR_MILLISECOND ){
			return sb.append( (offset+ONE_HOUR_MILLISECOND/2)/ONE_HOUR_MILLISECOND ).append( "小时" ).append( pos ).toString();
		}
		if( offset > ONE_MINUTE_MILLISECOND ){
			return sb.append( (offset+ONE_MINUTE_MILLISECOND/2)/ONE_MINUTE_MILLISECOND ).append( "分钟" ).append( pos ).toString();
		}
		return sb.append( offset/1000 ).append( "秒" ).append( pos ).toString();
	}
	
	
	/**
	 * 将date的时间部分清零
	 * @param day
	 * @return 返回Day将时间部分清零后对应日期
	 */
	public static Date getCleanDay( Date day ){
		return getCleanDay( getCalendar(day));
	}

	/**
	 * 获取day对应的Calendar对象
	 * @param day
	 * @return 返回date对应的Calendar对象
	 */
	public static Calendar getCalendar( Date day ){
		Calendar c = Calendar.getInstance();
		if( day != null)
			c.setTime(day);
		return c;
	}

	private static Date getCleanDay( Calendar c ){
		c.set(Calendar.HOUR_OF_DAY, 0) ;
		c.clear( Calendar.MINUTE );
		c.clear( Calendar.SECOND );
		c.clear( Calendar.MILLISECOND );
		return c.getTime();
	}
	
	/**
	 * 根据year，month，day构造日期对象
	 * @param year 年份（4位长格式）
	 * @param month 月份（1-12)
	 * @param day 天（1-31）
	 * @return 日期对象
	 */
	public static Date makeDate( int year, int month, int day ) {
		Calendar c = Calendar.getInstance();
		getCleanDay( c );
		c.set( Calendar.YEAR, year );
		c.set( Calendar.MONTH, month-1 );
		c.set( Calendar.DAY_OF_MONTH, day );
		return c.getTime();
	}

	private static Date getFirstCleanDay( int datePart, Date date ) {
		Calendar c = Calendar.getInstance();
		if( date != null )
			c.setTime( date );
		c.set( datePart,1 );
		return getCleanDay(c);
	}
	
	private static Date add( int datePart, int detal, Date date ) {
		Calendar c = Calendar.getInstance();
		if( date != null )
			c.setTime( date );
		c.add( datePart,detal );
		return c.getTime();
	}

	/**
	 * 日期date所在星期的第一天00:00:00对应日期对象
	 * @param date
	 * @return 日期所在星期的第一天00:00:00对应日期对象
	 */
	public static Date getFirstDayOfWeek( Date date ) {
		return getFirstCleanDay( Calendar.DAY_OF_WEEK, date);
	}

	/**
	 * 当前日期所在星期的第一天00:00:00对应日期对象
	 * @return 当前日期所在星期的第一天00:00:00对应日期对象
	 */
	public static Date getFirstDayOfWeek() {
		return getFirstDayOfWeek( null );
	}

	/**
	 * 日期date所在月份的第一天00:00:00对应日期对象
	 * @param date
	 * @return 日期所在月份的第一天00:00:00对应日期对象
	 */
	public static Date getFirstDayOfMonth( Date date ) {
		return getFirstCleanDay( Calendar.DAY_OF_MONTH, date);
	}

	/**
	 * 当前日期所在月份的第一天00:00:00对应日期对象
	 * @return 当前日期所在月份的第一天00:00:00对应日期对象
	 */
	public static Date getFirstDayOfMonth() {
		return getFirstDayOfMonth( null );
	}

	/**
	 * 当前日期所在月份的最后一天00:00:00对应日期对象
	 * @return 当前日期所在月份的最后一天00:00:00对应日期对象
	 */
	public static Date getLastDayOfMonth() {
		return getLastDayOfMonth(null);
	}

	/**
	 * 日期date所在月份的最后一天00:00:00对应日期对象
	 * @param date
	 * @return 日期date所在月份的最后一天00:00:00对应日期对象
	 */
	public static Date getLastDayOfMonth( Date date ) {
		Calendar c = getCalendar( getFirstDayOfMonth(date ));
		c.add( Calendar.MONTH, 1 );
		c.add( Calendar.DATE, -1 );
		return getCleanDay(c);
	}

	/**
	 * 日期date所在季度的第一天00:00:00对应日期对象
	 * @param date
	 * @return 日期date所在季度的第一天00:00:00对应日期对象
	 */
	public static Date getFirstDayOfSeason( Date date ) {
		Date d = getFirstDayOfMonth( date);
		int delta = DateUtil.getMonth(d) % 3;
		if( delta > 0 )
			d = DateUtil.getDateAfterMonths(d, -delta );
		return d;
	}

	/**
	 * 当前日期所在季度的第一天00:00:00对应日期对象
	 * @return 当前日期所在季度的第一天00:00:00对应日期对象
	 */
	public static Date getFirstDayOfSeason() {
		return getFirstDayOfMonth( null );
	}

	/**
	 * 日期date所在年份的第一天00:00:00对应日期对象
	 * @param date
	 * @return 日期date所在年份的第一天00:00:00对应日期对象
	 */
	public static Date getFirstDayOfYear( Date date ) {
		return makeDate( getYear(date),1,1 ); 
	}

	/**
	 * 当前日期年度的第一天00:00:00对应日期对象
	 * @return 当前日期年度第一天00:00:00对应日期对象
	 */
	public static Date getFirstDayOfYear() {
		return getFirstDayOfYear( new Date() );
	}
	/**
	 * 当前日期年度的最后一天23:59:59对应日期对象
	 * @return 当前日期年度的最后一天23:59:59对应日期对象
	 */
	public static Date getLastDayOfYear() {
		return parseDate(getYear(new Date())+"-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 计算N周后的日期
	 * @param start 开始日期
	 * @param weeks 可以为负，表示前N周
	 * @return 新的日期
	 */
	public static Date getDateAfterWeeks( Date start, int weeks ){
		return getDateAfterMs( start, weeks * ONE_WEEK_MILLISECOND );
	}
	
	/**
	 * 计算N月后的日期
	 * @param start 开始日期
	 * @param months 可以为负，表示前N月
	 * @return 新的日期
	 */
	public static Date getDateAfterMonths( Date start, int months ){
		return add( Calendar.MONTH, months, start );
	}
	

	/**
	 * 计算N年后的日期
	 * @param start 开始日期
	 * @param years 可以为负，表示前N年
	 * @return 新的日期
	 */
	public static Date getDateAfterYears( Date start, int years ){
		return add( Calendar.YEAR, years, start );
	}

	/**
	 * 计算N天后的日期（0点）
	 * @param start
	 * @param days
	 * @return
	 */
	public static Date getDateAfterDaysNoTime(Date start ,int days){
		Calendar cal = Calendar.getInstance();
        cal.setTime(getDateAfterDays(start, days));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return  cal.getTime();
	}
	
	/**
	 * 计算N天后的日期
	 * @param start 开始日期
	 * @param days 可以为负，表示前N天
	 * @return 新的日期
	 */
	public static Date getDateAfterDays( Date start, int days ){
		return getDateAfterMs( start, days * ONE_DAY_MILLISECOND );
	}
	
	/**
	 * 计算N毫秒后的日期
	 * @param start 开始日期
	 * @param ms 可以为负，表示前N毫秒
	 * @return 新的日期
	 */
	public static Date getDateAfterMs( Date start, long ms ){
		return new Date( start.getTime() + ms );
	}

	/**
	 * 计算2个日期之间的间隔的周期数
	 * @param start 开始日期
	 * @param end 结束日期
	 * @param msPeriod 单位周期的毫秒数
	 * @return 周期数
	 */
	public static long getPeriodNum(Date start, Date end, long msPeriod ){
		return getIntervalMs(start, end)/msPeriod  ;
	}
	
	/**
	 * 计算2个日期之间的毫秒数
	 * @param start 开始日期
	 * @param end 结束日期
	 * @return 毫秒数
	 */
	public static long getIntervalMs(Date start, Date end ){
		return end.getTime()-start.getTime() ;
	}
	
	/**
	 * 计算2个日期之间的天数
	 * @param start 开始日期
	 * @param end 结束日期
	 * @return 天数
	 */
	public static int getIntervalDays(Date start, Date end ){
		return (int)getPeriodNum(start, end, ONE_DAY_MILLISECOND );
	}

	/**
	 * 计算2个日期之间的周数
	 * @param start 开始日期
	 * @param end 结束日期
	 * @return 周数
	 */
	public static int getIntervalWeeks(Date start, Date end ){
		return (int)getPeriodNum( start, end, ONE_WEEK_MILLISECOND );
	}
	
	/**
	 * 比较日期前后关系
	 * @param base 基准日期
	 * @param date 待比较的日期
	 * @return 如果date在base之前或相等返回true，否则返回false
	 */
	public static boolean before( Date base, Date date ){
		return date.before( base )|| date.equals( base );
	}
	
	/**
	 * 比较日期前后关系
	 * @param base 基准日期
	 * @param date 待比较的日期
	 * @return 如果date在base之后或相等返回true，否则返回false
	 */
	public static boolean after( Date base, Date date ){
		return date.after( base )|| date.equals( base );
	}
	
	/**
	 * 返回对应毫秒数大的日期
	 * @param date1
	 * @param date2
	 * @return 返回对应毫秒数大的日期
	 */
	public static Date max( Date date1, Date date2 ) {
		if( date1.getTime() > date2.getTime() )
			return date1;
		else
			return date2;
	}
	
	/**
	 * 返回对应毫秒数小的日期
	 * @param date1
	 * @param date2
	 * @return 返回对应毫秒数小的日期
	 */
	public static Date min( Date date1, Date date2 ) {
		if( date1.getTime() < date2.getTime() )
			return date1;
		else
			return date2;
	}
	
	/**
	 * 判断date是否在指定的时期范围（start~end）内
	 * @param start 时期开始日期
	 * @param end 时期结束日期
	 * @param date 待比较的日期
	 * @return 如果date在指定的时期范围内，返回true，否则返回false
	 */
	public static boolean inPeriod( Date start, Date end, Date date ){
		return (end.after( date ) || end.equals( date ) ) && (start.before( date )||start.equals( date ) );
	}
	
	 /**
     * 获取当前日期是星期几<br>
     * 
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
    
    /**
     * 获得当天指定时间的时间戳
     * @param hour
     * @param minute
     * @param second
     * @return
     */
	public static long getTodayUnixTime(int hour, int minute, int second) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.MILLISECOND, 0);
		return (long) (cal.getTimeInMillis() / 1000);
	}
    
	/**
	 * 获取某一天的前一天日期
	 * @param date
	 * @param format 如："yyyy-MM-dd"
	 * @return
	 */
	public static String getYesterdayDate(Date date, String format) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
		return new SimpleDateFormat(format).format(c.getTime());
	}
	
	/**
	 * 测试
	 * @param args
	 */
	/*public static void main( String[] args ){
		System.out.println(parseDate("2013-01-33", "yyyy-MM-dd"));
		System.out.println(parseDate("2013-01-33", "yyyy-MM-dd", false));
		
		System.out.println( Calendar.getInstance() == Calendar.getInstance() );
		System.out.println( getCleanDay( new Date()));
		System.out.println( new Date() );
		Calendar c = Calendar.getInstance();
		c.set( Calendar.DAY_OF_MONTH,1 );
		System.out.println( getFirstDayOfMonth() );
		System.out.println( getLastDayOfMonth( makeDate(1996,2,1) ) );
		
		System.out.println( formatDate( makeDate(2009,5,1) ) );
		System.out.println( formatDate( makeDate(2010,5,1) ) );
		System.out.println( formatDate( makeDate(2010,12,21) ) );
		System.out.println( before( makeDate(2009,5,1), new Date() ) );
		System.out.println( after( makeDate(2009,5,1), new Date() ) );
		System.out.println( inPeriod( makeDate(2009,11,24),makeDate(2009,11,30), makeDate(2009,11,25) ) );
	}*/
	
	public static class Milliscond {

		private final static String format = "dd HH:mm:ss";

		// 传入起始、结束时间，默认格式
		public static String format2Str(Date start, Date end) {
			return replace(start, end, format);
		}

		// 传入毫秒数，默认格式
		public static String format2Str(long ms) {
			return replace(ms, format);
		}

		// 传入起始、结束时间，指定格式
		public static String format2Str(Date start, Date end, String format) {
			return replace(start, end, format);
		}

		// 传入毫秒数，指定格式
		public static String format2Str(long ms, String format) {
			return replace(ms, format);
		}

		// 传入起始、结束时间，获取Map
		public static Map<String, String> format2Map(Date start, Date end) {
			return format2Map(end.getTime() - start.getTime());
		}

		// 传入毫秒数，获取Map
		public static Map<String, String> format2Map(long ms) {
			long ss = 1000;
			long mi = ss * 60;
			long hh = mi * 60;
			long dd = hh * 24;

			long day = ms / dd;
			long hour = (ms - day * dd) / hh;
			long minute = (ms - day * dd - hour * hh) / mi;
			long second = (ms - day * dd - hour * hh - minute * mi) / ss;
			long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

			String strDay = day < 10 ? "0" + day : "" + day;
			String strHour = hour < 10 ? "0" + hour : "" + hour;
			String strMinute = minute < 10 ? "0" + minute : "" + minute;
			String strSecond = second < 10 ? "0" + second : "" + second;
			String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;

			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("day", strDay);
			resultMap.put("hour", strHour);
			resultMap.put("minute", strMinute);
			resultMap.put("second", strSecond);
			resultMap.put("milliSecond", strMilliSecond);
			return resultMap;
		}

		// 传入起始、结束时间，替换目标字符串
		private static String replace(Date start, Date end, String format) {
			String result = null;
			if (format.contains("dd") && format.contains("HH")
					&& format.contains("mm") && format.contains("ss")) {
				Map<String, String> map = format2Map(start, end);
				format = format.replaceAll("dd", map.get("day"));
				format = format.replaceAll("HH", map.get("hour"));
				format = format.replaceAll("mm", map.get("minute"));
				format = format.replaceAll("ss", map.get("second")+"."+map.get("milliSecond"));
				result = format;
			}
			return result;
		}

		// 传入毫秒数，替换目标字符串
		private static String replace(long ms, String format) {
			String result = null;
			if (format.contains("dd") && format.contains("HH")
					&& format.contains("mm") && format.contains("ss")) {
				Map<String, String> map = format2Map(ms);
				format = format.replaceAll("dd", map.get("day"));
				format = format.replaceAll("HH", map.get("hour"));
				format = format.replaceAll("mm", map.get("minute"));
				format = format.replaceAll("ss", map.get("second")+"."+map.get("milliSecond"));
				result = format;
			}
			return result;
		}
	}
}