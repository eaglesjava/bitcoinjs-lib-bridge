

package com.bitbill.www.model.contact.db.entity;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;

/**
 * Created by isanwenyu@163.com on 2017/11/17.
 */

@Entity(nameInDb = "contact",
        indexes = {
                @Index(value = "walletId,address", unique = true)
        })
public class Contact extends BaseIndexPinyinBean implements Serializable {

    private static final long serialVersionUID = 266;

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "wallet_id")
    private String walletId;

    @Property(nameInDb = "address")
    private String address;

    @Property(nameInDb = "remark")
    private String remark;

    @Property(nameInDb = "contact_name")
    @NotNull
    private String contactName;

    @Property(nameInDb = "coin_type")
    private String coinType;//默认"BTC"


    @Generated(hash = 773915021)
    public Contact(Long id, String walletId, String address, String remark,
                   @NotNull String contactName, String coinType) {
        this.id = id;
        this.walletId = walletId;
        this.address = address;
        this.remark = remark;
        this.contactName = contactName;
        this.coinType = coinType;
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
        return contactName;
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

    public String getCoinType() {
        return this.coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public String getContactName() {
        return this.contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}