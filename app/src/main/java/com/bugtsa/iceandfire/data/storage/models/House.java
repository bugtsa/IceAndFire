package com.bugtsa.iceandfire.data.storage.models;

import android.support.annotation.NonNull;

import com.bugtsa.iceandfire.data.network.res.HouseRes;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(active = true, nameInDb = "HOUSES")
public class House {

    @Id
    private Long id;

    @NonNull
    @Unique
    private String remoteUrl;

    private String name;
    private String region;
    private String coatOfArms;
    private String words;

    /** Used for active entity operations. */
    @Generated(hash = 1167916919)
    private transient HouseDao myDao;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;


//    @ToMany(joinProperties = {
//            @JoinProperty(name = "remoteId", referencedName = "userRemoteId")
//    })
//    private List<Repository> repositories;

//    @ToMany(joinProperties = {
//            @JoinProperty(name = "remoteId", referencedName = "userRemoteId")
//    })
//    private List<Like> likes;

    public House(HouseRes houseRes) {
        remoteUrl = houseRes.getUrl();
        name = houseRes.getName();
        region = houseRes.getRegion();
        coatOfArms = houseRes.getCoatOfArms();
        words = houseRes.getWords();
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
    @Generated(hash = 451323429)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getHouseDao() : null;
    }


    public String getWords() {
        return this.words;
    }


    public void setWords(String words) {
        this.words = words;
    }


    public String getCoatOfArms() {
        return this.coatOfArms;
    }


    public void setCoatOfArms(String coatOfArms) {
        this.coatOfArms = coatOfArms;
    }


    public String getRegion() {
        return this.region;
    }


    public void setRegion(String region) {
        this.region = region;
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getRemoteUrl() {
        return this.remoteUrl;
    }


    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    @Generated(hash = 102756475)
    public House(Long id, @NonNull String remoteUrl, String name, String region,
            String coatOfArms, String words) {
        this.id = id;
        this.remoteUrl = remoteUrl;
        this.name = name;
        this.region = region;
        this.coatOfArms = coatOfArms;
        this.words = words;
    }


    @Generated(hash = 389023854)
    public House() {
    }
}
