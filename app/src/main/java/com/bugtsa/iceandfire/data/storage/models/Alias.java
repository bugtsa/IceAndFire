package com.bugtsa.iceandfire.data.storage.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(active = true, nameInDb = "ALIASES")
public class Alias {

    @Id
    private Long id;

    private String alias;

    private String characterRemoteId;

    /** Used for active entity operations. */
    @Generated(hash = 1571512818)
    private transient AliasDao myDao;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public Alias(String characterRemoteId, String alias) {
        this.alias = alias;
        this.characterRemoteId = characterRemoteId;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 360304207)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAliasDao() : null;
    }

    public String getCharacterRemoteId() {
        return this.characterRemoteId;
    }

    public void setCharacterRemoteId(String characterRemoteId) {
        this.characterRemoteId = characterRemoteId;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 1514350672)
    public Alias(Long id, String alias, String characterRemoteId) {
        this.id = id;
        this.alias = alias;
        this.characterRemoteId = characterRemoteId;
    }

    @Generated(hash = 1265971347)
    public Alias() {
    }

}
