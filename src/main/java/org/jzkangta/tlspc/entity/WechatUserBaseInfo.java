package org.jzkangta.tlspc.entity;

import java.io.Serializable;
import java.util.Date;

public class WechatUserBaseInfo implements Serializable {
    private static final long serialVersionUID = 6120874851511182505L;

    /**
     * This field corresponds to the database column wechat_user_base_info.id
     */
    private Integer id = 0;

    /**
     * This field corresponds to the database column wechat_user_base_info.sub_scribe
     */
    private Integer subScribe = 0;

    /**
     * This field corresponds to the database column wechat_user_base_info.open_id
     */
    private String openId;

    /**
     * This field corresponds to the database column wechat_user_base_info.nick_name
     */
    private String nickName;

    /**
     * This field corresponds to the database column wechat_user_base_info.city
     */
    private String city;

    /**
     * This field corresponds to the database column wechat_user_base_info.country
     */
    private String country;

    /**
     * This field corresponds to the database column wechat_user_base_info.province
     */
    private String province;

    /**
     * This field corresponds to the database column wechat_user_base_info.language
     */
    private String language;

    /**
     * This field corresponds to the database column wechat_user_base_info.head_img_url
     */
    private String headImgUrl;

    /**
     * This field corresponds to the database column wechat_user_base_info.subscribe_time
     */
    private String subscribeTime;

    /**
     * This field corresponds to the database column wechat_user_base_info.unsubscribe_time
     */
    private String unsubscribeTime;

    /**
     * This field corresponds to the database column wechat_user_base_info.create_time
     */
    private Date createTime = new Date();

    /**
     * This field corresponds to the database column wechat_user_base_info.update_time
     */
    private String updateTime;

    /**
     * This field corresponds to the database column wechat_user_base_info.receiver
     */
    private String receiver;

    /**
     * This field corresponds to the database column wechat_user_base_info.receive_province
     */
    private String receiveProvince;

    /**
     * This field corresponds to the database column wechat_user_base_info.receive_city
     */
    private String receiveCity;

    /**
     * This field corresponds to the database column wechat_user_base_info.receive_area
     */
    private String receiveArea;

    /**
     * This field corresponds to the database column wechat_user_base_info.receive_detail_info
     */
    private String receiveDetailInfo;

    /**
     * This field corresponds to the database column wechat_user_base_info.receive_tel
     */
    private String receiveTel;

    /**
     * This field corresponds to the database column wechat_user_base_info.receive_zipcode
     */
    private String receiveZipcode;

    /**
     * This field corresponds to the database column wechat_user_base_info.x
     */
    private String x;

    /**
     * This field corresponds to the database column wechat_user_base_info.y
     */
    private String y;

    /**
     * This field corresponds to the database column wechat_user_base_info.addrearea
     */
    private String addrearea;

    /**
     * This field corresponds to the database column wechat_user_base_info.user_id
     */
    private Integer userId = 0;

    /**
     * This field corresponds to the database column wechat_user_base_info.wechat_application_id
     */
    private Integer wechatApplicationId = 0;

    /**
     * This field corresponds to the database column wechat_user_base_info.wxuser_id
     */
    private Integer wxuserId = 0;

    /**
     * This field corresponds to the database column wechat_user_base_info.union_id
     */
    private String unionId;

    /**
     * This method returns the value of the database column wechat_user_base_info.id
     *
     * @return the value of wechat_user_base_info.id
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.id
     *
     * @param id the value for wechat_user_base_info.id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.sub_scribe
     *
     * @return the value of wechat_user_base_info.sub_scribe
     */
    public Integer getSubScribe() {
        return subScribe;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.sub_scribe
     *
     * @param subScribe the value for wechat_user_base_info.sub_scribe
     */
    public void setSubScribe(Integer subScribe) {
        this.subScribe = subScribe;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.open_id
     *
     * @return the value of wechat_user_base_info.open_id
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.open_id
     *
     * @param openId the value for wechat_user_base_info.open_id
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.nick_name
     *
     * @return the value of wechat_user_base_info.nick_name
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.nick_name
     *
     * @param nickName the value for wechat_user_base_info.nick_name
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.city
     *
     * @return the value of wechat_user_base_info.city
     */
    public String getCity() {
        return city;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.city
     *
     * @param city the value for wechat_user_base_info.city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.country
     *
     * @return the value of wechat_user_base_info.country
     */
    public String getCountry() {
        return country;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.country
     *
     * @param country the value for wechat_user_base_info.country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.province
     *
     * @return the value of wechat_user_base_info.province
     */
    public String getProvince() {
        return province;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.province
     *
     * @param province the value for wechat_user_base_info.province
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.language
     *
     * @return the value of wechat_user_base_info.language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.language
     *
     * @param language the value for wechat_user_base_info.language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.head_img_url
     *
     * @return the value of wechat_user_base_info.head_img_url
     */
    public String getHeadImgUrl() {
        return headImgUrl;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.head_img_url
     *
     * @param headImgUrl the value for wechat_user_base_info.head_img_url
     */
    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.subscribe_time
     *
     * @return the value of wechat_user_base_info.subscribe_time
     */
    public String getSubscribeTime() {
        return subscribeTime;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.subscribe_time
     *
     * @param subscribeTime the value for wechat_user_base_info.subscribe_time
     */
    public void setSubscribeTime(String subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.unsubscribe_time
     *
     * @return the value of wechat_user_base_info.unsubscribe_time
     */
    public String getUnsubscribeTime() {
        return unsubscribeTime;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.unsubscribe_time
     *
     * @param unsubscribeTime the value for wechat_user_base_info.unsubscribe_time
     */
    public void setUnsubscribeTime(String unsubscribeTime) {
        this.unsubscribeTime = unsubscribeTime;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.create_time
     *
     * @return the value of wechat_user_base_info.create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.create_time
     *
     * @param createTime the value for wechat_user_base_info.create_time
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.update_time
     *
     * @return the value of wechat_user_base_info.update_time
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.update_time
     *
     * @param updateTime the value for wechat_user_base_info.update_time
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.receiver
     *
     * @return the value of wechat_user_base_info.receiver
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.receiver
     *
     * @param receiver the value for wechat_user_base_info.receiver
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.receive_province
     *
     * @return the value of wechat_user_base_info.receive_province
     */
    public String getReceiveProvince() {
        return receiveProvince;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.receive_province
     *
     * @param receiveProvince the value for wechat_user_base_info.receive_province
     */
    public void setReceiveProvince(String receiveProvince) {
        this.receiveProvince = receiveProvince;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.receive_city
     *
     * @return the value of wechat_user_base_info.receive_city
     */
    public String getReceiveCity() {
        return receiveCity;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.receive_city
     *
     * @param receiveCity the value for wechat_user_base_info.receive_city
     */
    public void setReceiveCity(String receiveCity) {
        this.receiveCity = receiveCity;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.receive_area
     *
     * @return the value of wechat_user_base_info.receive_area
     */
    public String getReceiveArea() {
        return receiveArea;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.receive_area
     *
     * @param receiveArea the value for wechat_user_base_info.receive_area
     */
    public void setReceiveArea(String receiveArea) {
        this.receiveArea = receiveArea;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.receive_detail_info
     *
     * @return the value of wechat_user_base_info.receive_detail_info
     */
    public String getReceiveDetailInfo() {
        return receiveDetailInfo;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.receive_detail_info
     *
     * @param receiveDetailInfo the value for wechat_user_base_info.receive_detail_info
     */
    public void setReceiveDetailInfo(String receiveDetailInfo) {
        this.receiveDetailInfo = receiveDetailInfo;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.receive_tel
     *
     * @return the value of wechat_user_base_info.receive_tel
     */
    public String getReceiveTel() {
        return receiveTel;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.receive_tel
     *
     * @param receiveTel the value for wechat_user_base_info.receive_tel
     */
    public void setReceiveTel(String receiveTel) {
        this.receiveTel = receiveTel;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.receive_zipcode
     *
     * @return the value of wechat_user_base_info.receive_zipcode
     */
    public String getReceiveZipcode() {
        return receiveZipcode;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.receive_zipcode
     *
     * @param receiveZipcode the value for wechat_user_base_info.receive_zipcode
     */
    public void setReceiveZipcode(String receiveZipcode) {
        this.receiveZipcode = receiveZipcode;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.x
     *
     * @return the value of wechat_user_base_info.x
     */
    public String getX() {
        return x;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.x
     *
     * @param x the value for wechat_user_base_info.x
     */
    public void setX(String x) {
        this.x = x;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.y
     *
     * @return the value of wechat_user_base_info.y
     */
    public String getY() {
        return y;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.y
     *
     * @param y the value for wechat_user_base_info.y
     */
    public void setY(String y) {
        this.y = y;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.addrearea
     *
     * @return the value of wechat_user_base_info.addrearea
     */
    public String getAddrearea() {
        return addrearea;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.addrearea
     *
     * @param addrearea the value for wechat_user_base_info.addrearea
     */
    public void setAddrearea(String addrearea) {
        this.addrearea = addrearea;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.user_id
     *
     * @return the value of wechat_user_base_info.user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.user_id
     *
     * @param userId the value for wechat_user_base_info.user_id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.wechat_application_id
     *
     * @return the value of wechat_user_base_info.wechat_application_id
     */
    public Integer getWechatApplicationId() {
        return wechatApplicationId;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.wechat_application_id
     *
     * @param wechatApplicationId the value for wechat_user_base_info.wechat_application_id
     */
    public void setWechatApplicationId(Integer wechatApplicationId) {
        this.wechatApplicationId = wechatApplicationId;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.wxuser_id
     *
     * @return the value of wechat_user_base_info.wxuser_id
     */
    public Integer getWxuserId() {
        return wxuserId;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.wxuser_id
     *
     * @param wxuserId the value for wechat_user_base_info.wxuser_id
     */
    public void setWxuserId(Integer wxuserId) {
        this.wxuserId = wxuserId;
    }

    /**
     * This method returns the value of the database column wechat_user_base_info.union_id
     *
     * @return the value of wechat_user_base_info.union_id
     */
    public String getUnionId() {
        return unionId;
    }

    /**
     * This method sets the value of the database column wechat_user_base_info.union_id
     *
     * @param unionId the value for wechat_user_base_info.union_id
     */
    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
}