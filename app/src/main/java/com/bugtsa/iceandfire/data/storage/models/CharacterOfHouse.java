package com.bugtsa.iceandfire.data.storage.models;

import com.bugtsa.iceandfire.data.network.res.CharacterRes;
import com.bugtsa.iceandfire.utils.StringUtils;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

@Entity(active = true, nameInDb = "CHARACTERS")
public class CharacterOfHouse {

    @Id
    private Long id;

    private String remoteId;

    private String houseRemoteId;

    private String url;
    private String name;
    private String gender;
    private String culture;
    private String born;
    private String died;
    private String father;
    private String mother;
    private String spouse;
    private String allegiances;
    private String aliasTitle;

    @ToMany(joinProperties = {
            @JoinProperty(name = "remoteId", referencedName = "characterRemoteId")
    })
    private List<Alias> aliases;

    @ToMany(joinProperties = {
            @JoinProperty(name = "remoteId", referencedName = "characterRemoteId")
    })
    private List<Title> titles;

    /** Used for active entity operations. */
    @Generated(hash = 1342124280)
    private transient CharacterOfHouseDao myDao;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public CharacterOfHouse(CharacterRes characterRes) {
        name = characterRes.getName();
        gender = characterRes.getGender();
        culture = characterRes.getCulture();
        born = characterRes.getBorn();
        died = characterRes.getDied();
        father = StringUtils.getIdFromUrlApi(characterRes.getFather());
        mother = StringUtils.getIdFromUrlApi(characterRes.getMother());
        spouse = StringUtils.getIdFromUrlApi(characterRes.getSpouse());

        if (characterRes.getUrl() != null) {
            if (!characterRes.getUrl().isEmpty()) {
                remoteId = StringUtils.getIdFromUrlApi(characterRes.getUrl());
            }
        }

        if (characterRes.getAllegiances() != null) {
            if (!characterRes.getAllegiances().isEmpty()) {
                houseRemoteId = StringUtils.getIdFromUrlApi(characterRes.getAllegiances().get(0));
            }
        }
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

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1506933621)
    public synchronized void resetTitles() {
        titles = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 680630419)
    public List<Title> getTitles() {
        if (titles == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TitleDao targetDao = daoSession.getTitleDao();
            List<Title> titlesNew = targetDao._queryCharacterOfHouse_Titles(remoteId);
            synchronized (this) {
                if(titles == null) {
                    titles = titlesNew;
                }
            }
        }
        return titles;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 731614754)
    public synchronized void resetAliases() {
        aliases = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 897749690)
    public List<Alias> getAliases() {
        if (aliases == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AliasDao targetDao = daoSession.getAliasDao();
            List<Alias> aliasesNew = targetDao._queryCharacterOfHouse_Aliases(remoteId);
            synchronized (this) {
                if(aliases == null) {
                    aliases = aliasesNew;
                }
            }
        }
        return aliases;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1199340470)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCharacterOfHouseDao() : null;
    }

    public String getAliasTitle() {
        return this.aliasTitle;
    }

    public void setAliasTitle(String aliasTitle) {
        this.aliasTitle = aliasTitle;
    }

    public String getAllegiances() {
        return this.allegiances;
    }

    public void setAllegiances(String allegiances) {
        this.allegiances = allegiances;
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

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    @Generated(hash = 1079922851)
    public CharacterOfHouse(Long id, String remoteId, String houseRemoteId, String url, String name,
            String gender, String culture, String born, String died, String father, String mother,
            String spouse, String allegiances, String aliasTitle) {
        this.id = id;
        this.remoteId = remoteId;
        this.houseRemoteId = houseRemoteId;
        this.url = url;
        this.name = name;
        this.gender = gender;
        this.culture = culture;
        this.born = born;
        this.died = died;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;
        this.allegiances = allegiances;
        this.aliasTitle = aliasTitle;
    }

    @Generated(hash = 1658210378)
    public CharacterOfHouse() {
    }
}
