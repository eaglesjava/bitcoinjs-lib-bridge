package com.bitbill.www.model.btc.db.entity;

import com.bitbill.model.db.dao.DaoSession;
import com.bitbill.model.db.dao.InputDao;
import com.bitbill.model.db.dao.OutputDao;
import com.bitbill.model.db.dao.TxRecordDao;
import com.bitbill.model.db.dao.WalletDao;
import com.bitbill.www.common.base.model.entity.Entity;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Date;
import java.util.List;

/**
 * Created by isanwenyu@163.com on 2017/12/23.
 */
@org.greenrobot.greendao.annotation.Entity(

        indexes = {
                @Index(value = "walletId,txHash", unique = true)
        }
)
public class TxRecord extends Entity {
    @ToOne(joinProperty = "walletId")
    Wallet wallet;
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private Long walletId;
    @NotNull
    private String txHash;
    @Convert(converter = InOutConverter.class, columnType = Integer.class)
    private InOut inOut = InOut.IN;//0: 转移,1：in（接收）,2：out(发送)
    private Long sumAmount;
    private Long height;
    private Date createdTime;
    @ToMany(referencedJoinProperty = "txId")
    @OrderBy("id ASC")
    private List<Input> inputs;
    @ToMany(referencedJoinProperty = "txId")
    @OrderBy("id ASC")
    private List<Output> outputs;
    private String mRemark;
    private Long elementId;
    private Long fee;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 253635275)
    private transient TxRecordDao myDao;
    @Generated(hash = 1885063144)
    private transient Long wallet__resolvedKey;

    @Generated(hash = 835338750)
    public TxRecord(Long id, @NotNull Long walletId, @NotNull String txHash, InOut inOut, Long sumAmount,
                    Long height, Date createdTime, String mRemark, Long elementId, Long fee) {
        this.id = id;
        this.walletId = walletId;
        this.txHash = txHash;
        this.inOut = inOut;
        this.sumAmount = sumAmount;
        this.height = height;
        this.createdTime = createdTime;
        this.mRemark = mRemark;
        this.elementId = elementId;
        this.fee = fee;
    }

    @Generated(hash = 1564994407)
    public TxRecord() {
    }

    public InOut getInOut() {
        return inOut;
    }

    public TxRecord setInOut(InOut inOut) {
        this.inOut = inOut;
        return this;
    }

    public Long getSumAmount() {
        return sumAmount;
    }

    public TxRecord setSumAmount(Long sumAmount) {
        this.sumAmount = sumAmount;
        return this;
    }

    public String getTxHash() {
        return txHash;
    }

    public TxRecord setTxHash(String txHash) {
        this.txHash = txHash;
        return this;
    }

    public Long getHeight() {
        if (height == null) {
            return -1l;
        }
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public TxRecord setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public String getRemark() {
        return StringUtils.isEmpty(mRemark) ? "无" : mRemark;
    }

    public void setRemark(String remark) {
        mRemark = remark;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWalletId() {
        return this.walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public String getMRemark() {
        return this.mRemark;
    }

    public void setMRemark(String mRemark) {
        this.mRemark = mRemark;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 203866864)
    public List<Input> getInputs() {
        if (inputs == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InputDao targetDao = daoSession.getInputDao();
            List<Input> inputsNew = targetDao._queryTxRecord_Inputs(id);
            synchronized (this) {
                if (inputs == null) {
                    inputs = inputsNew;
                }
            }
        }
        return inputs;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 754384309)
    public synchronized void resetInputs() {
        inputs = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 78242321)
    public List<Output> getOutputs() {
        if (outputs == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OutputDao targetDao = daoSession.getOutputDao();
            List<Output> outputsNew = targetDao._queryTxRecord_Outputs(id);
            synchronized (this) {
                if (outputs == null) {
                    outputs = outputsNew;
                }
            }
        }
        return outputs;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1925425011)
    public synchronized void resetOutputs() {
        outputs = null;
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
    @Generated(hash = 1357860971)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTxRecordDao() : null;
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

    public Long getElementId() {
        return this.elementId;
    }

    public void setElementId(Long elementId) {
        this.elementId = elementId;
    }

    public Long getFee() {
        return this.fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public enum InOut {
        TRANSFER,
        IN,
        OUT
    }

    public static class InOutConverter implements PropertyConverter<InOut, Integer> {

        @Override
        public InOut convertToEntityProperty(Integer databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            for (InOut inOut : InOut.values()) {
                if (inOut.ordinal() == databaseValue) {
                    return inOut;
                }
            }
            return InOut.TRANSFER;
        }

        @Override
        public Integer convertToDatabaseValue(InOut entityProperty) {
            return entityProperty == null ? null : entityProperty.ordinal();
        }
    }
}

