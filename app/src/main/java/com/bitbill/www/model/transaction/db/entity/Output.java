package com.bitbill.www.model.transaction.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by isanwenyu on 2018/1/10.
 */
@Entity(

        indexes = {
                @Index(value = "walletId,txHash,txIndex", unique = true)
        }
)
public class Output extends com.bitbill.www.common.base.model.entity.Entity {
    @Id(autoincrement = true)
    private Long id;
    private Long txId;
    private Long walletId;
    private String address;
    private long value;//unit satoshi
    private int txIndex;
    private String txHash;
    @Transient
    private boolean isMine;
    @Transient
    private boolean isInternal;

    @Generated(hash = 1192166948)
    public Output(Long id, Long txId, Long walletId, String address, long value, int txIndex,
                  String txHash) {
        this.id = id;
        this.txId = txId;
        this.walletId = walletId;
        this.address = address;
        this.value = value;
        this.txIndex = txIndex;
        this.txHash = txHash;
    }

    @Generated(hash = 1780391644)
    public Output() {
    }

    public Output(Long txId, String address, long value) {
        this.txId = txId;
        this.address = address;
        this.value = value;
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

    public int getTxIndex() {
        return this.txIndex;
    }

    public void setTxIndex(int txIndex) {
        this.txIndex = txIndex;
    }

    public String getTxHash() {
        return this.txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public Long getWalletId() {
        return this.walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    public boolean isInternal() {
        return isInternal;
    }

    public void setInternal(boolean internal) {
        isInternal = internal;
    }
}
