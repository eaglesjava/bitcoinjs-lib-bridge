

package com.bitbill.www.model.wallet.db.entity;

import com.bitbill.model.db.dao.AddressDao;
import com.bitbill.model.db.dao.DaoSession;
import com.bitbill.model.db.dao.TxRecordDao;
import com.bitbill.model.db.dao.WalletDao;
import com.bitbill.www.model.address.db.entity.Address;
import com.bitbill.www.model.transaction.db.entity.TxRecord;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

/**
 * Created by isanwenyu@163.com on 2017/11/17.
 */

@Entity(nameInDb = "wallet")
public class Wallet extends com.bitbill.www.common.base.model.entity.Entity {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "name")
    @Unique
    private String name;

    @Property(nameInDb = "encrypt_mnemonic")
    private String encryptMnemonic;

    @Property(nameInDb = "mnemonic_hash")
    private String mnemonicHash;

    @Property(nameInDb = "last_address_index")
    private Long lastAddressIndex;

    @Property(nameInDb = "last_change_address_index")
    private Long lastChangeAddressIndex;

    @Property(nameInDb = "last_address")
    private String lastAddress;

    @Property(nameInDb = "last_change_address")
    private String lastChangeAddress;

    @Property(nameInDb = "encrypt_seed")
    private String encryptSeed;

    @Property(nameInDb = "seedhex_hash")
    private String seedHexHash;

    @Property(nameInDb = "is_backuped")
    private Boolean isBackuped;

    @Property(nameInDb = "created_at")
    private Long createdAt;

    @Property(nameInDb = "updated_at")
    private Long updatedAt;

    @Property(nameInDb = "is_default")
    private Boolean isDefault;

    @Property(nameInDb = "extented_public_key")
    private String extentedPublicKey;//十六进制字符串

    @Property(nameInDb = "internal_public_key")
    private String internalPublicKey;//十六进制字符串

    @Property(nameInDb = "balance")
    private Long balance;//unit Satoshi  1 BTC = 100000000 Satoshi
    @Property(nameInDb = "unconfirm")
    private Long unconfirm;//unit Satoshi  1 BTC = 100000000 Satoshi

    @Property(nameInDb = "coin_type")
    private String coinType;//默认"BTC"

    @Property(nameInDb = "version")
    private Long version;

    @ToMany(referencedJoinProperty = "walletId")
    @OrderBy("isInternal ASC,index ASC")
    private List<Address> addressList;

    @ToMany(referencedJoinProperty = "walletId")
    @OrderBy("createdTime DESC")
    private List<TxRecord> txRecordList;

    @Transient
    private String mnemonic;
    @Transient
    private String seedHex;
    @Transient
    private String tradePwd;
    @Transient
    private boolean selected;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 741381941)
    private transient WalletDao myDao;

    @Generated(hash = 1691628326)
    public Wallet(Long id, String name, String encryptMnemonic, String mnemonicHash, Long lastAddressIndex, Long lastChangeAddressIndex,
                  String lastAddress, String lastChangeAddress, String encryptSeed, String seedHexHash, Boolean isBackuped, Long createdAt,
                  Long updatedAt, Boolean isDefault, String extentedPublicKey, String internalPublicKey, Long balance, Long unconfirm,
                  String coinType, Long version) {
        this.id = id;
        this.name = name;
        this.encryptMnemonic = encryptMnemonic;
        this.mnemonicHash = mnemonicHash;
        this.lastAddressIndex = lastAddressIndex;
        this.lastChangeAddressIndex = lastChangeAddressIndex;
        this.lastAddress = lastAddress;
        this.lastChangeAddress = lastChangeAddress;
        this.encryptSeed = encryptSeed;
        this.seedHexHash = seedHexHash;
        this.isBackuped = isBackuped;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDefault = isDefault;
        this.extentedPublicKey = extentedPublicKey;
        this.internalPublicKey = internalPublicKey;
        this.balance = balance;
        this.unconfirm = unconfirm;
        this.coinType = coinType;
        this.version = version;
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

    public Long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
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

    public Long getBalance() {
        if (balance == null) {
            return 0l;
        }
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getLastAddressIndex() {
        if (lastAddressIndex == null) {
            return 0l;
        }
        return this.lastAddressIndex;
    }

    public void setLastAddressIndex(Long lastAddressIndex) {
        this.lastAddressIndex = lastAddressIndex;
    }

    public Long getLastChangeAddressIndex() {
        if (lastChangeAddressIndex == null) {
            return 0l;
        }
        return lastChangeAddressIndex;
    }

    public void setLastChangeAddressIndex(Long lastChangeAddressIndex) {
        this.lastChangeAddressIndex = lastChangeAddressIndex;
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

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getIsBackuped() {
        if (isBackuped == null) {
            return false;
        }
        return this.isBackuped;
    }

    public void setIsBackuped(Boolean isBackuped) {
        this.isBackuped = isBackuped;
    }

    public String getInternalPublicKey() {
        return internalPublicKey;
    }

    public void setInternalPublicKey(String internalPublicKey) {
        this.internalPublicKey = internalPublicKey;
    }

    public String getExtentedPublicKey() {
        return extentedPublicKey;
    }

    public Wallet setExtentedPublicKey(String extentedPublicKey) {
        this.extentedPublicKey = extentedPublicKey;
        return this;
    }

    public String getLastAddress() {
        return lastAddress;
    }

    public Wallet setLastAddress(String lastAddress) {
        this.lastAddress = lastAddress;
        return this;
    }

    public Long getUnconfirm() {
        if (unconfirm == null) {
            return 0l;
        }
        return this.unconfirm;
    }

    public void setUnconfirm(Long unconfirm) {
        this.unconfirm = unconfirm;
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
            return id.equals(wallet.getId()) && name.equals(((Wallet) obj).getName());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return id.hashCode() + name.hashCode();
    }

    public String getCoinType() {
        return this.coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1005262466)
    public List<Address> getAddressList() {
        if (addressList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AddressDao targetDao = daoSession.getAddressDao();
            List<Address> addressListNew = targetDao._queryWallet_AddressList(id);
            synchronized (this) {
                if (addressList == null) {
                    addressList = addressListNew;
                }
            }
        }
        return addressList;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1977742708)
    public synchronized void resetAddressList() {
        addressList = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 657468544)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getWalletDao() : null;
    }

    public Long getVersion() {
        if (version == null) {
            return 0l;
        }
        return this.version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1072630674)
    public List<TxRecord> getTxRecordList() {
        if (txRecordList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TxRecordDao targetDao = daoSession.getTxRecordDao();
            List<TxRecord> txRecordListNew = targetDao._queryWallet_TxRecordList(id);
            synchronized (this) {
                if (txRecordList == null) {
                    txRecordList = txRecordListNew;
                }
            }
        }
        return txRecordList;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 789112272)
    public synchronized void resetTxRecordList() {
        txRecordList = null;
    }

    public String getLastChangeAddress() {
        return this.lastChangeAddress;
    }

    public void setLastChangeAddress(String lastChangeAddress) {
        this.lastChangeAddress = lastChangeAddress;
    }
}