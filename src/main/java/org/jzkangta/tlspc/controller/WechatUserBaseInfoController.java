package org.jzkangta.tlspc.controller;

import org.jzkangta.tlspc.framework.base.web.BaseController3;
import org.jzkangta.tlspc.framework.util.DWZResponse;
import org.jzkangta.tlspc.framework.util.PageList;
import org.jzkangta.tlspc.framework.util.RenderUtil;
import org.jzkangta.tlspc.framework.web.HttpRequestInfo;

import java.util.Arrays;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jzkangta.tlspc.entity.WechatUserBaseInfo;
import org.jzkangta.tlspc.service.WechatUserBaseInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/wechatUserBaseInfo")
public class WechatUserBaseInfoController extends BaseController3 {
    private static final String ROOT_VIEW = "admin/wechatUserBaseInfo";

    private static final String HOME_VIEW = "admin/wechatUserBaseInfo/home";

    private static final String EDIT_VIEW = "admin/wechatUserBaseInfo/edit";

    @Resource
    private WechatUserBaseInfoService wechatUserBaseInfoService;

    @RequestMapping()
    public String listWechatUserBaseInfo(HttpServletRequest request, ModelMap model) {
        HttpRequestInfo reqInfo = new HttpRequestInfo(request);
        setRequestModelMap( request, model, true );
        getPageList( model );
        return HOME_VIEW;
    }

    @RequestMapping(EDIT)
    public String editWechatUserBaseInfo(HttpServletRequest request, ModelMap model) {
        HttpRequestInfo reqInfo = new HttpRequestInfo(request);
        setRequestModelMap( request, model, false );
        Integer id = reqInfo.getIntParameter( "id", -1 );
        if( id > 0 ){
            model.put( "model",wechatUserBaseInfoService.getById( id ) ); 
        }
        return EDIT_VIEW;
    }

    @RequestMapping(ADD)
    public String addWechatUserBaseInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model, WechatUserBaseInfo wechatUserBaseInfo) {
        setRequestModelMap(request, model, false);
        DWZResponse.Builder builder;
        try {
            wechatUserBaseInfoService.insert( wechatUserBaseInfo ); 
            builder = DWZResponse.getSucessBuilder("success");
        } catch (Exception e) {
            builder = DWZResponse.getFailBuilder("fail" + Arrays.deepToString(e.getStackTrace()));
        }
        RenderUtil.renderHtml(builder.build().toString(), response);
        return null;
    }

    @RequestMapping(DETAIL)
    public String detailWechatUserBaseInfo(HttpServletRequest request, ModelMap model) {
        HttpRequestInfo reqInfo = new HttpRequestInfo(request);
        Integer id = reqInfo.getIntParameter( "id", -1 );
        if( id > 0 ){
            model.addAttribute( wechatUserBaseInfoService.getById( id ) ); 
        }
        setRequestModelMap(request, model);
        return null;
    }

    @RequestMapping(UPDATE)
    public String updateWechatUserBaseInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model, WechatUserBaseInfo wechatUserBaseInfoNew) throws Exception {
        HttpRequestInfo reqInfo = new HttpRequestInfo(request);
        Integer id = reqInfo.getIntParameter( "id", -1 );
        if( id > 0 ){
            DWZResponse.Builder builder;
            try {
                WechatUserBaseInfo wechatUserBaseInfo = wechatUserBaseInfoService.getById( id ); 
                super.copyProperties(wechatUserBaseInfo, wechatUserBaseInfoNew);
                wechatUserBaseInfoService.update( wechatUserBaseInfo ); 
                model.clear();
                builder = DWZResponse.getSucessBuilder("success");
            } catch (Exception e) {
                builder = DWZResponse.getFailBuilder("fail" + Arrays.deepToString(e.getStackTrace()));
            }
            RenderUtil.renderHtml(builder.build().toString(), response);
        }
        return null;
    }

    @RequestMapping(DELETE)
    public String deleteWechatUserBaseInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model, String inIdList) {
        HttpRequestInfo reqInfo = new HttpRequestInfo(request);
        setRequestModelMap(request, model);
        DWZResponse.Builder builder;
        try {
            String[] idArray = inIdList.split(",");
            model.put("inIdList", idArray);
            wechatUserBaseInfoService.delete( model ); 
            builder = DWZResponse.getSucessBuilder("success");
        } catch (Exception e) {
            builder = DWZResponse.getFailBuilder("fail" + Arrays.deepToString(e.getStackTrace()));
        }
        RenderUtil.renderHtml(builder.build().toString(), response);
        model.clear();
        return null;
    }

    private PageList<WechatUserBaseInfo> getPageList(ModelMap model) {
        PageList<WechatUserBaseInfo> wechatUserBaseInfoList = wechatUserBaseInfoService.getList( model, Integer.parseInt(model.get("pageNum").toString()), Integer.parseInt(model.get("numPerPage").toString()) ); 
        model.put("wechatUserBaseInfoList",wechatUserBaseInfoList);
        model.put("pageTurn",wechatUserBaseInfoList.getPageTurn());
        return wechatUserBaseInfoList;
    }
}