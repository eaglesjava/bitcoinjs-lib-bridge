package com.bitbill.www.model.address.db.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;

/**
 * Created by isanwenyu on 2018/1/5.
 */

@Entity(nameInDb = "address")
public class Address extends com.bitbill.www.common.base.model.entity.Entity {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "name")
    @Unique
    private String name;

    @Property(nameInDb = "wallet_id")
    private Long walletId;

    @Property(nameInDb = "index")
    private Long index;

    @Property(nameInDb = "coin_type")
    private String coinType;//默认"BTC"

    @Property(nameInDb = "created_at")
    private Date createdAt;

    @Generated(hash = 1524232744)
    public Address(Long id, String name, Long walletId, Long index, String coinType,
                   Date createdAt) {
        this.id = id;
        this.name = name;
        this.walletId = walletId;
        this.index = index;
        this.coinType = coinType;
        this.createdAt = createdAt;
    }

    @Generated(hash = 388317431)
    public Address() {
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

    public Long getWalletId() {
        return this.walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Long getIndex() {
        return this.index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getCoinType() {
        return this.coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
