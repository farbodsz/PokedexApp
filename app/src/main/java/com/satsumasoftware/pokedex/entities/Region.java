package com.satsumasoftware.pokedex.entities;

import android.content.Context;
import android.support.v4.util.ArrayMap;

public class Region {

    private int mId;
    private ArrayMap<String, String> mNames;

    public Region(Context context, int id) {
        // TODO search from SQLite or CSV database (using Univocity CSV might actually be faster than SQLite)
        // the following is temporary

        String name;
        switch (id) {
            case 1:
                name = "Kanto";
                break;
            case 2:
                name = "Johto";
                break;
            case 3:
                name = "Hoenn";
                break;
            case 4:
                name = "Sinnoh";
                break;
            case 5:
                name = "Unova";
                break;
            case 6:
                name = "Kalos";
                break;
            default:
                throw new IllegalArgumentException("parameter id " + id + " is invalid");
        }
        ArrayMap<String, String> langauges = new ArrayMap<>(1);
        langauges.put(Langs.KEY_EN, name);

        mId = id;
        mNames = langauges;
    }

    public Region(int id, ArrayMap<String, String> names) {
        mId = id;
        mNames = names;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mNames.get(Langs.KEY_EN);
    }

    public ArrayMap<String, String> getAllNames() {
        return mNames;
    }

}
