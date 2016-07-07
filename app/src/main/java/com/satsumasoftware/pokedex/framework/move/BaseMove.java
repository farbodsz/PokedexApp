package com.satsumasoftware.pokedex.framework.move;

import com.satsumasoftware.pokedex.db.MovesDBHelper;

public class BaseMove {

    public static final String[] DB_COLUMNS =
            {MovesDBHelper.COL_ID, MovesDBHelper.COL_NAME};

    protected int mId;
    protected String mName;

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }
}
