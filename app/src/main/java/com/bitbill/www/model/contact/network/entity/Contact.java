package com.bitbill.www.model.contact.network.entity;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

import java.io.Serializable;

public class Contact extends BaseIndexPinyinBean implements Serializable {

    private String name;//名字
    private String address;//地址
    private boolean isTop;//是否是最上面的 不需要被转化成拼音的
    private String remark;


    public Contact() {
    }

    public Contact(String mName) {
        this.name = mName;
    }

    public String getName() {
        return name;
    }

    public Contact setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isTop() {
        return isTop;
    }

    public Contact setTop(boolean top) {
        isTop = top;
        return this;
    }

    @Override
    public String getTarget() {
        return name;
    }

    @Override
    public boolean isNeedToPinyin() {
        return !isTop;
    }


    @Override
    public boolean isShowSuspension() {
        return !isTop;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
