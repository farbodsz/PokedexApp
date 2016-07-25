package com.satsumasoftware.pokedex.framework.item;

import com.satsumasoftware.pokedex.db.ItemsDBHelper;

public class BaseItem {

    public static final String[] DB_COLUMNS = {ItemsDBHelper.COL_ID, ItemsDBHelper.COL_NAME};

    protected int mId;
    protected String mName;

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

}
