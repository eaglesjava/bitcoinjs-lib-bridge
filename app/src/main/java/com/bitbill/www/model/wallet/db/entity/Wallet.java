

package com.bitbill.www.model.wallet.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by isanwenyu@163.com on 2017/11/17.
 */

@Entity(nameInDb = "wallet")
public class Wallet extends com.bitbill.www.common.base.model.entity.Entity {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "encrypt_mnemonic")
    private String encryptMnemonic;

    @Property(nameInDb = "mnemonic_hash")
    private String mnemonicHash;

    @Property(nameInDb = "last_address_index")
    private int lastAddressIndex;

    @Property(nameInDb = "encrypt_seed")
    private String encryptSeed;

    @Property(nameInDb = "seedhex_hash")
    private String seedHexHash;

    @Property(nameInDb = "is_backup")
    private boolean isBackup;

    @Property(nameInDb = "created_at")
    private long createdAt;

    @Property(nameInDb = "updated_at")
    private long updatedAt;
    @Transient
    private String tradePwd;

    @Generated(hash = 387054013)
    public Wallet(Long id, String name, String encryptMnemonic, String mnemonicHash,
                  int lastAddressIndex, String encryptSeed, String seedHexHash, boolean isBackup,
                  long createdAt, long updatedAt) {
        this.id = id;
        this.name = name;
        this.encryptMnemonic = encryptMnemonic;
        this.mnemonicHash = mnemonicHash;
        this.lastAddressIndex = lastAddressIndex;
        this.encryptSeed = encryptSeed;
        this.seedHexHash = seedHexHash;
        this.isBackup = isBackup;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Generated(hash = 1197745249)
    public Wallet() {
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

    public String getEncryptMnemonic() {
        return this.encryptMnemonic;
    }

    public void setEncryptMnemonic(String encryptMnemonic) {
        this.encryptMnemonic = encryptMnemonic;
    }

    public int getLastAddressIndex() {
        return this.lastAddressIndex;
    }

    public void setLastAddressIndex(int lastAddressIndex) {
        this.lastAddressIndex = lastAddressIndex;
    }

    public String getEncryptSeed() {
        return this.encryptSeed;
    }

    public void setEncryptSeed(String encryptSeed) {
        this.encryptSeed = encryptSeed;
    }

    public boolean getIsBackup() {
        return this.isBackup;
    }

    public void setIsBackup(boolean isBackup) {
        this.isBackup = isBackup;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTradePwd() {
        return tradePwd;
    }

    public Wallet setTradePwd(String tradePwd) {
        this.tradePwd = tradePwd;
        return this;
    }

    public String getMnemonicHash() {
        return this.mnemonicHash;
    }

    public void setMnemonicHash(String mnemonicHash) {
        this.mnemonicHash = mnemonicHash;
    }

    public String getSeedHexHash() {
        return this.seedHexHash;
    }

    public void setSeedHexHash(String seedHexHash) {
        this.seedHexHash = seedHexHash;
    }
}