package com.satsumasoftware.pokedex.framework.nature;

import com.satsumasoftware.pokedex.db.NaturesDBHelper;

public class BaseNature {

    public static final String[] DB_COLUMNS =
            {NaturesDBHelper.COL_ID, NaturesDBHelper.COL_NAME};

    protected int mId;
    protected String mName;

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }
}
