

package com.bitbill.www.model.contact.db.entity;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;

/**
 * Created by isanwenyu@163.com on 2017/11/17.
 */

@Entity(nameInDb = "contact")
public class Contact extends BaseIndexPinyinBean implements Serializable {

    private static final long serialVersionUID = 266;

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "wallet_id")
    private String walletId;

    @Property(nameInDb = "address")
    private String address;

    @Property(nameInDb = "remark")
    private String remark;


    @Generated(hash = 1912314887)
    public Contact(Long id, String name, String walletId, String address, String remark) {
        this.id = id;
        this.name = name;
        this.walletId = walletId;
        this.address = address;
        this.remark = remark;
    }

    @Generated(hash = 672515148)
    public Contact() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWalletId() {
        return this.walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getTarget() {
        return name;
    }

    @Override
    public boolean isNeedToPinyin() {
        return true;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}