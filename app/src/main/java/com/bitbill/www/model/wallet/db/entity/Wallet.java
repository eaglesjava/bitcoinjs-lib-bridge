

package com.bitbill.www.model.wallet.db.entity;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by isanwenyu@163.com on 2017/11/17.
 */

@Entity(nameInDb = "wallet")
public class Wallet extends com.bitbill.www.common.base.model.entity.Entity implements Comparable<Wallet> {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "encrypt_mnemonic")
    private String encryptMnemonic;

    @Property(nameInDb = "mnemonic_hash")
    private String mnemonicHash;

    @Property(nameInDb = "last_address_index")
    private long lastAddressIndex;

    @Property(nameInDb = "encrypt_seed")
    private String encryptSeed;

    @Property(nameInDb = "seedhex_hash")
    private String seedHexHash;

    @Property(nameInDb = "is_backuped")
    private boolean isBackuped;

    @Property(nameInDb = "created_at")
    private long createdAt;

    @Property(nameInDb = "updated_at")
    private long updatedAt;

    @Property(nameInDb = "is_default")
    private boolean isDefault;

    @Property(nameInDb = "xpublic_key")
    private String XPublicKey;//十六进制字符串

    @Property(nameInDb = "btc_balance")
    private long btcBalance;//unit Satoshi  1 BTC = 100000000 Satoshi
    @Property(nameInDb = "btc_unconfirm")
    private long btcUnconfirm;//unit Satoshi  1 BTC = 100000000 Satoshi
    @Transient
    private String mnemonic;
    @Transient
    private String seedHex;
    @Transient
    private String tradePwd;
    @Transient
    private boolean selected;
    @Transient
    private String lastAddress;

    @Generated(hash = 1054506043)
    public Wallet(Long id, String name, String encryptMnemonic, String mnemonicHash, long lastAddressIndex,
                  String encryptSeed, String seedHexHash, boolean isBackuped, long createdAt, long updatedAt,
                  boolean isDefault, String XPublicKey, long btcBalance, long btcUnconfirm) {
        this.id = id;
        this.name = name;
        this.encryptMnemonic = encryptMnemonic;
        this.mnemonicHash = mnemonicHash;
        this.lastAddressIndex = lastAddressIndex;
        this.encryptSeed = encryptSeed;
        this.seedHexHash = seedHexHash;
        this.isBackuped = isBackuped;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDefault = isDefault;
        this.XPublicKey = XPublicKey;
        this.btcBalance = btcBalance;
        this.btcUnconfirm = btcUnconfirm;
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


    public String getEncryptSeed() {
        return this.encryptSeed;
    }

    public void setEncryptSeed(String encryptSeed) {
        this.encryptSeed = encryptSeed;
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

    public void setTradePwd(String tradePwd) {
        this.tradePwd = tradePwd;
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

    public long getBtcBalance() {
        return btcBalance;
    }

    public void setBtcBalance(long btcBalance) {
        this.btcBalance = btcBalance;
    }

    public long getLastAddressIndex() {
        return this.lastAddressIndex;
    }

    public void setLastAddressIndex(long lastAddressIndex) {
        this.lastAddressIndex = lastAddressIndex;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public Wallet setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
        return this;
    }

    public String getSeedHex() {
        return seedHex;
    }

    public Wallet setSeedHex(String seedHex) {
        this.seedHex = seedHex;
        return this;
    }

    public boolean isSelected() {
        return selected;
    }

    public Wallet setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    public boolean getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean getIsBackuped() {
        return this.isBackuped;
    }

    public void setIsBackuped(boolean isBackuped) {
        this.isBackuped = isBackuped;
    }

    public String getXPublicKey() {
        return XPublicKey;
    }

    public Wallet setXPublicKey(String XPublicKey) {
        this.XPublicKey = XPublicKey;
        return this;
    }

    public String getLastAddress() {
        return lastAddress;
    }

    public Wallet setLastAddress(String lastAddress) {
        this.lastAddress = lastAddress;
        return this;
    }

    public long getBtcUnconfirm() {
        return this.btcUnconfirm;
    }

    public void setBtcUnconfirm(long btcUnconfirm) {
        this.btcUnconfirm = btcUnconfirm;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof Wallet) {
            Wallet wallet = (Wallet) obj;
            return id.equals(wallet.getId());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int compareTo(@NonNull Wallet o) {

        return (int) (id - o.getId());
    }
}