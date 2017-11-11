/*
 * Copyright 2016-2017 Farbod Salamat-Zadeh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.satsumasoftware.pokedex.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.satsumasoftware.pokedex.db.AbilitiesDBHelper;
import com.satsumasoftware.pokedex.db.EncounterConditionsDBHelper;
import com.satsumasoftware.pokedex.db.LocationAreasDBHelper;
import com.satsumasoftware.pokedex.db.LocationsDBHelper;
import com.satsumasoftware.pokedex.db.MovesDBHelper;
import com.satsumasoftware.pokedex.db.NaturesDBHelper;
import com.satsumasoftware.pokedex.db.PokemonDBHelper;

public final class DatabaseUtils {

    public static final String DB_VERSIONS =
            AbilitiesDBHelper.DATABASE_VERSION + "_" +
                    EncounterConditionsDBHelper.DATABASE_VERSION + "_" +
                    LocationAreasDBHelper.DATABASE_VERSION + "_" +
                    LocationsDBHelper.DATABASE_VERSION + "_" +
                    MovesDBHelper.DATABASE_VERSION + "_" +
                    NaturesDBHelper.DATABASE_VERSION + "_" +
                    PokemonDBHelper.DATABASE_VERSION;

    public static final String PREF_SAVED_DB_VERSIONS = "pref_database_saved_versions";

    public static String[] getDbVersionArray() {
        return DB_VERSIONS.split("_");
    }

    public static String getSavedDbVersionString(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_SAVED_DB_VERSIONS, null);
    }

    public static boolean hasDatabaseUpgraded(Context context) {
        String savedVersions = getSavedDbVersionString(context);
        return savedVersions == null || !savedVersions.equals(DB_VERSIONS);
    }

    public static String[] getSavedDbVersionArray(Context context) {
        return getSavedDbVersionString(context).split("_");
    }

    public static void markDatabaseUpgraded(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_SAVED_DB_VERSIONS, DB_VERSIONS).apply();
    }
}
