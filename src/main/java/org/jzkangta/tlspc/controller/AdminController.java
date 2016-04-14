package org.jzkangta.tlspc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import org.jzkangta.tlspc.framework.base.web.BaseController3;
import org.jzkangta.tlspc.framework.exception.BusinessException;
import org.jzkangta.tlspc.framework.util.StringUtil;
import org.jzkangta.tlspc.framework.web.HttpRequestInfo;


/**
 * User Controller
 */
@Controller
public class AdminController extends BaseController3{
	private Logger log = Logger.getLogger( getClass() );
	
    private static final String LOGIN = "/login";
	
	private final static String LOGON = "/logon";

    private static final String INDEX = "/index";

	private static final String INDEX_VIEW = "index";

    private static final String LOGON_VIEW = "login";

    private static final String LOGOUT = "/admin/logout";


    private final static String REDIRECT_LOGON = "redirect:/admin/logon";

    private static final int TOP_MENU = 1040;

    private static final int ORG_TYPE_ID = 0;



    @RequestMapping(LOGON)
    public String logon(HttpServletRequest request){

        HttpRequestInfo httpRequest = new HttpRequestInfo(request);
        String retUrl = httpRequest.getParameter("retUrl");
        String url = httpRequest.getParameter("jsp");
        request.setAttribute("ReturnUrl", retUrl);

        if(!StringUtil.isEmpty(url)){
            return url ;
        }
        return LOGON_VIEW;
    }


    @RequestMapping(LOGIN)
    public String login(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
        HttpRequestInfo reqInfo = new HttpRequestInfo(request);
        String eqAccount = reqInfo.getParameter("eqAccount", "");
        String eqPassword = reqInfo.getParameter("pwd", "");
        String returnUrl = "";
        System.out.println("eqAccount:"+eqAccount+" eqPassword:"+eqPassword);
        
        if(eqAccount.equals("Wan97UnAdmin")&&eqPassword.equals("uVsjr8Oj53VdFUBxeOMD")){
        	returnUrl = "redirect:"+ reqInfo.getParameter("retUrl", INDEX);
        }
    
        return returnUrl;

    }


    @RequestMapping(LOGOUT)
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String view = REDIRECT_LOGON;
      
        return view;
    }

    private void verify(HttpServletRequest request, String validCode, String eqOrgAccount, String eqAccount, String eqPassword) throws BusinessException {
        if (StringUtil.isEmpty(eqAccount)) {
            throw new BusinessException("user.login.username.empty");
        }

        if (StringUtil.isEmpty(eqPassword)) {
            throw new BusinessException("user.login.userpassword.empty");
        }
    }
    
    @RequestMapping(INDEX)
	public String index(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws Exception {

		setRequestModelMap(request, model, true);
		
		HttpRequestInfo reqInfo = new HttpRequestInfo(request);
		String view = reqInfo.getParameter( "view", INDEX_VIEW );
		try {
			
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

}






