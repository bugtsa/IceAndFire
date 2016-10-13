package com.bugtsa.iceandfire.data.storage.models;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(active = true, nameInDb = "CHARACTERS")
public class CharacterOfHouse {

    @Id
    private Long id;

    @NonNull
    @Unique
    private String remoteId;

    private String houseRemoteId;

    private String name;
    private String gender;
    private String culture;
    private String born;
    private String died;
    private String father;
    private String mother;
    private String spouse;

    /** Used for active entity operations. */
    @Generated(hash = 1342124280)
    private transient CharacterOfHouseDao myDao;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public CharacterOfHouse(String remoteId, String houseRemoteId) {
        this.remoteId = remoteId;
        this.houseRemoteId = houseRemoteId;
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
    @Generated(hash = 1199340470)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCharacterOfHouseDao() : null;
    }

    public String getSpouse() {
        return this.spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    public String getMother() {
        return this.mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getFather() {
        return this.father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getDied() {
        return this.died;
    }

    public void setDied(String died) {
        this.died = died;
    }

    public String getBorn() {
        return this.born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public String getCulture() {
        return this.culture;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHouseRemoteId() {
        return this.houseRemoteId;
    }

    public void setHouseRemoteId(String houseRemoteId) {
        this.houseRemoteId = houseRemoteId;
    }

    public String getRemoteId() {
        return this.remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 1173420797)
    public CharacterOfHouse(Long id, @NonNull String remoteId,
            String houseRemoteId, String name, String gender, String culture,
            String born, String died, String father, String mother, String spouse) {
        this.id = id;
        this.remoteId = remoteId;
        this.houseRemoteId = houseRemoteId;
        this.name = name;
        this.gender = gender;
        this.culture = culture;
        this.born = born;
        this.died = died;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;
    }

    @Generated(hash = 1658210378)
    public CharacterOfHouse() {
    }
}
