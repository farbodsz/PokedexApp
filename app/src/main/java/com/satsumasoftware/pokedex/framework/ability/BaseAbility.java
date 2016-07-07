package com.satsumasoftware.pokedex.framework.ability;

import com.satsumasoftware.pokedex.db.AbilitiesDBHelper;

public class BaseAbility {

    public static final String[] DB_COLUMNS =
            {AbilitiesDBHelper.COL_ID, AbilitiesDBHelper.COL_NAME};

    protected int mId;
    protected String mName;

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

}
