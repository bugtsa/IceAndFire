package com.bugtsa.iceandfire.data.storage.models;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouse;
import com.bugtsa.iceandfire.data.storage.models.House;

import com.bugtsa.iceandfire.data.storage.models.CharacterOfHouseDao;
import com.bugtsa.iceandfire.data.storage.models.HouseDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig characterOfHouseDaoConfig;
    private final DaoConfig houseDaoConfig;

    private final CharacterOfHouseDao characterOfHouseDao;
    private final HouseDao houseDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        characterOfHouseDaoConfig = daoConfigMap.get(CharacterOfHouseDao.class).clone();
        characterOfHouseDaoConfig.initIdentityScope(type);

        houseDaoConfig = daoConfigMap.get(HouseDao.class).clone();
        houseDaoConfig.initIdentityScope(type);

        characterOfHouseDao = new CharacterOfHouseDao(characterOfHouseDaoConfig, this);
        houseDao = new HouseDao(houseDaoConfig, this);

        registerDao(CharacterOfHouse.class, characterOfHouseDao);
        registerDao(House.class, houseDao);
    }
    
    public void clear() {
        characterOfHouseDaoConfig.getIdentityScope().clear();
        houseDaoConfig.getIdentityScope().clear();
    }

    public CharacterOfHouseDao getCharacterOfHouseDao() {
        return characterOfHouseDao;
    }

    public HouseDao getHouseDao() {
        return houseDao;
    }

}
