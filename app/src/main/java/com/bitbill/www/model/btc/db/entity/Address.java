package com.bitbill.www.model.btc.db.entity;


import com.bitbill.model.db.dao.AddressDao;
import com.bitbill.model.db.dao.DaoSession;
import com.bitbill.model.db.dao.WalletDao;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;

/**
 * Created by isanwenyu on 2018/1/5.
 */

@Entity(nameInDb = "address")
public class Address extends com.bitbill.www.common.base.model.entity.Entity {

    @ToOne(joinProperty = "walletId")
    Wallet wallet;
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "name")
    @Unique
    private String name;
    @NotNull
    @Property(nameInDb = "wallet_id")
    private Long walletId;
    @Property(nameInDb = "index")
    private Long index;

    @Property(nameInDb = "coin_type")
    private String coinType;//默认"BTC"

    @Property(nameInDb = "created_at")
    private Date createdAt;

    @Property(nameInDb = "balance")
    private Long balance;

    @Property(nameInDb = "is_internal")
    private Boolean isInternal;

    @Property(nameInDb = "is_used")
    private Boolean isUsed;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1580986028)
    private transient AddressDao myDao;

    @Generated(hash = 1885063144)
    private transient Long wallet__resolvedKey;

    @Generated(hash = 40652152)
    public Address(Long id, String name, @NotNull Long walletId, Long index, String coinType,
                   Date createdAt, Long balance, Boolean isInternal, Boolean isUsed) {
        this.id = id;
        this.name = name;
        this.walletId = walletId;
        this.index = index;
        this.coinType = coinType;
        this.createdAt = createdAt;
        this.balance = balance;
        this.isInternal = isInternal;
        this.isUsed = isUsed;
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

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1903052073)
    public Wallet getWallet() {
        Long __key = this.walletId;
        if (wallet__resolvedKey == null || !wallet__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WalletDao targetDao = daoSession.getWalletDao();
            Wallet walletNew = targetDao.load(__key);
            synchronized (this) {
                wallet = walletNew;
                wallet__resolvedKey = __key;
            }
        }
        return wallet;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 276091648)
    public void setWallet(@NotNull Wallet wallet) {
        if (wallet == null) {
            throw new DaoException(
                    "To-one property 'walletId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.wallet = wallet;
            walletId = wallet.getId();
            wallet__resolvedKey = walletId;
        }
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
    @Generated(hash = 543375780)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAddressDao() : null;
    }

    public Boolean getIsInternal() {
        if (isInternal == null) {
            return false;
        }
        return this.isInternal;
    }

    public void setIsInternal(Boolean isInternal) {
        this.isInternal = isInternal;
    }

    public Boolean getIsUsed() {
        if (isUsed == null) {
            return false;
        }
        return this.isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }
}
