package com.bitbill.www.model.transaction.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by isanwenyu on 2018/1/10.
 */
@Entity
public class Input extends com.bitbill.www.common.base.model.entity.Entity {
    @Id(autoincrement = true)
    private Long id;
    private Long txId;
    private String address;
    private long value;//unit satoshi

    @Generated(hash = 1713187622)
    public Input(Long id, Long txId, String address, long value) {
        this.id = id;
        this.txId = txId;
        this.address = address;
        this.value = value;
    }

    @Generated(hash = 289903166)
    public Input() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTxId() {
        return this.txId;
    }

    public void setTxId(Long txId) {
        this.txId = txId;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getValue() {
        return this.value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
