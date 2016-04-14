package org.jzkangta.tlspc.dao.ibatis;

import org.jzkangta.tlspc.framework.base.dao.mybatis.BaseDaoMybatis;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.jzkangta.tlspc.dao.WechatUserBaseInfoDao;
import org.jzkangta.tlspc.entity.WechatUserBaseInfo;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Repository;

@Repository("wechatUserBaseInfoDao")
public class WechatUserBaseInfoDaoImpl extends BaseDaoMybatis<WechatUserBaseInfo> implements WechatUserBaseInfoDao {

    public WechatUserBaseInfoDaoImpl() {
        super();
        setNameSpace( "tlspc.wechat_user_base_info" );
    }

    @Required
    @Resource
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory( sqlSessionFactory );
    }
}