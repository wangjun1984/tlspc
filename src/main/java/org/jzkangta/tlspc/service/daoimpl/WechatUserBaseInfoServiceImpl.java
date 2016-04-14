package org.jzkangta.tlspc.service.daoimpl;

import org.jzkangta.tlspc.framework.base.dao.BaseDao;
import org.jzkangta.tlspc.framework.base.service.daoimpl.BaseServiceImpl;

import javax.annotation.Resource;

import org.jzkangta.tlspc.dao.WechatUserBaseInfoDao;
import org.jzkangta.tlspc.entity.WechatUserBaseInfo;
import org.jzkangta.tlspc.service.WechatUserBaseInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class) 
@Service("wechatUserBaseInfoService")
public class WechatUserBaseInfoServiceImpl extends BaseServiceImpl<WechatUserBaseInfo> implements WechatUserBaseInfoService {
    @Resource
    private WechatUserBaseInfoDao wechatUserBaseInfoDao;

    public WechatUserBaseInfoServiceImpl() {
        super();
    }

    public WechatUserBaseInfoDao getWechatUserBaseInfoDao() {
        return wechatUserBaseInfoDao;
    }

    /**
     * Don't remove or edit it.
     */
    @Override
    protected BaseDao<WechatUserBaseInfo> getBaseDao() {
        return wechatUserBaseInfoDao;
    }

    public void setWechatUserBaseInfoDao(WechatUserBaseInfoDao wechatUserBaseInfoDao) {
        this.wechatUserBaseInfoDao = wechatUserBaseInfoDao;
    }
}