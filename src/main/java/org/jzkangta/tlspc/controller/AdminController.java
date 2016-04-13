package org.jzkangta.tlspc.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.frogs.admin.client.AdminSdk;
import com.frogs.admin.entity.User;
import com.frogs.admin.util.CookieConfig;
import com.frogs.activity.util.WebSiteEnum;
import com.frogs.framework.base.web.BaseController3;
import com.frogs.framework.component.cache.Cache;
import com.frogs.framework.exception.BusinessException;
import com.frogs.framework.util.StringUtil;
import com.frogs.framework.web.HttpRequestInfo;
import com.frogs.framework.web.HttpResponseUtil;


/**
 * User Controller
 */
@Controller
public class AdminController extends BaseController3{
	
	private static final Logger log = LoggerFactory.getLogger(AdminController.class);
	
    private static final String LOGIN = "/login";
	
	private final static String LOGON = "/logon";

    private static final String INDEX = "/index";

	private static final String INDEX_VIEW = "index";

    private static final String LOGON_VIEW = "login";

    private static final String LOGOUT = "/admin/logout";

    private final static String REDIRECT_LOGON = "redirect:/admin/logon";

    @Resource
    private Cache cache;
    
    private AdminSdk adminSdk = AdminSdk.getInstance(cache);

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
        HttpRequestInfo requestInfo = new HttpRequestInfo(request);
        String eqAccount = requestInfo.getParameter("eqAccount", "admin");
        String eqPasswd = requestInfo.getParameter("eqPasswd", "");
        String returnUrl = "redirect:"+ requestInfo.getParameter("returnUrl", INDEX);
		User user = adminSdk.login(eqAccount,eqPasswd);
		if (user == null) {
            throw new BusinessException("user.login.username.not.exist");
        }
		HttpResponseUtil.setCookie(response, CookieConfig.COOKIE_ADMIN_USER_ID, user.getId().toString(), WebSiteEnum.ROOT.getUrl(), "/", CookieConfig.expireOneDaySeconds);
		HttpResponseUtil.setCookie(response, CookieConfig.COOKIE_ADMIN_VERIFY_CODE, user.getVerify(), WebSiteEnum.ROOT.getUrl(), "/", CookieConfig.expireOneDaySeconds);
		return returnUrl;
    }

    @RequestMapping(LOGOUT)
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String view = REDIRECT_LOGON;
        HttpRequestInfo requestInfo = new HttpRequestInfo(request);
        int userId = requestInfo.getCookieIntValue(CookieConfig.COOKIE_ADMIN_USER_ID, 0);
		String verify = requestInfo.getCookieValue(CookieConfig.COOKIE_ADMIN_VERIFY_CODE,"");
		adminSdk.logout(userId, verify);
        return view;
    }
    
	@RequestMapping(INDEX)
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		setRequestModelMap(request, model, true);
		
		HttpRequestInfo reqInfo = new HttpRequestInfo(request);
		String view = reqInfo.getParameter("view", INDEX_VIEW);
		try {
			HttpRequestInfo requestInfo = new HttpRequestInfo(request);
			int userId = requestInfo.getCookieIntValue(CookieConfig.COOKIE_ADMIN_USER_ID, 0);
			String verify = requestInfo.getCookieValue(CookieConfig.COOKIE_ADMIN_VERIFY_CODE, "");
			User user = adminSdk.getUser(userId, verify);
			model.addAttribute("user", user);
			model.addAttribute("view", view);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}
}