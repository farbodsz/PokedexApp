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
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.db.PokemonDBHelper;
import com.satsumasoftware.pokedex.framework.pokemon.BasePokemon;
import com.satsumasoftware.pokedex.framework.pokemon.MiniPokemon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public final class FavoriteUtils {

    // String of words separated by commas to show favourite Pokémon
    public static final String PREF_FAVORITE_POKEMON = "pref_favourite_pokemon";
    public static final String PREF_FAVOURITE_POKEMON_OLD = "pref_favourite_pokemon_old";


    public static void addFavoritePkmn(Context context, MiniPokemon miniPokemon) {
        // Parameter 'pokemon' is entered as ABC where ABC is the pokemon id
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String currentValue = sp.getString(PREF_FAVORITE_POKEMON, "");
        String addToList = currentValue + "," + miniPokemon.getId();
        sp.edit().putString(PREF_FAVORITE_POKEMON, addToList).apply();
    }

    public static void removeFavoritePkmn(Context context, MiniPokemon miniPokemon) {
        String whatToDelete = String.valueOf(miniPokemon.getId());
        String line = getFavouritePkmnRawLine(context);

        if (line.contains(whatToDelete)) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            String editedList = line.replace(whatToDelete, "");
            sp.edit().putString(PREF_FAVORITE_POKEMON, editedList).apply();
        } else {
            Toast.makeText(context, R.string.favourites_error, Toast.LENGTH_SHORT).show();
        }
    }

    public static String getFavouritePkmnRawLine(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_FAVORITE_POKEMON, "");
    }

    public static void doFavouritesTempConversion(Context context) {
        String line = getFavouritePkmnRawLine(context);
        String[] segments = line.split(",");
        Collections.sort(Arrays.asList(segments));

        ArrayList<MiniPokemon> arrayList = new ArrayList<>();

        for (String aSegment : segments) {
            String[] details = aSegment.split("_");
            if (details.length == 3) {
                int id = Integer.parseInt(details[0]);
                // String name = details[1]; // redundant
                String form;
                if (details[2].equals("NONE")) {
                    form = "";
                } else {
                    form = details[2];
                }
                boolean isMega = (form.toLowerCase().contains("mega"));
                MiniPokemon aPokemon = MiniPokemon.createFromSpecies(context, id, isMega);
                arrayList.add(aPokemon);
            }
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < arrayList.size(); i++) {
            MiniPokemon pokemon = arrayList.get(i);
            builder.append(pokemon.getId());
            if (i != arrayList.size()-1) builder.append(",");  // add ',' if not last item
        }

        String newList = (arrayList.isEmpty() ? "" : builder.toString());

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_FAVORITE_POKEMON, newList).apply();
    }

    public static ArrayList<MiniPokemon> getFavouritePkmnArrayLists(Context context) {
        String line = getFavouritePkmnRawLine(context);
        ArrayList<MiniPokemon> pokemonList = new ArrayList<>();

        String[] pokemonIds = line.split(",");

        for (String anId : pokemonIds) {
            if (anId.equals("")) {
                continue;
            }

            PokemonDBHelper helper = PokemonDBHelper.getInstance(context);
            Cursor cursor = helper.getReadableDatabase().query(
                    PokemonDBHelper.TABLE_NAME,
                    BasePokemon.DB_COLUMNS,
                    PokemonDBHelper.COL_ID + "=? AND " + PokemonDBHelper.COL_FORM_IS_DEFAULT + "=?",
                    new String[] {anId, String.valueOf(1)},
                    null, null, null);
            cursor.moveToFirst();
            pokemonList.add(new MiniPokemon(cursor));
            cursor.close();
        }

        return pokemonList;
    }

    public static boolean isAFavouritePkmn(Context context, MiniPokemon miniPokemon) {
        String line = getFavouritePkmnRawLine(context);
        return line.contains(String.valueOf(miniPokemon.getId()));
    }

    public static void markAsFavouritePkmn(Context context, MiniPokemon miniPokemon, View rootLayout) {
        String markType;

        if (isAFavouritePkmn(context, miniPokemon)) {
            removeFavoritePkmn(context, miniPokemon);
            markType = "removed from";
        } else {
            addFavoritePkmn(context, miniPokemon);
            markType = "added to";
        }

        String message = context.getResources().getString(R.string.mark_favourites,
                miniPokemon.getFormAndPokemonName(), markType);

        Snackbar.make(rootLayout, message, Snackbar.LENGTH_SHORT)
                .show();
    }

    public static void clearFavouritePkmnList(final Context context) {
        backupOldFavouritePkmnList(context);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_FAVORITE_POKEMON, "").apply();
        // Snackbar message is shown in the activity calling to this method
    }

    public static void backupOldFavouritePkmnList(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_FAVOURITE_POKEMON_OLD, getFavouritePkmnRawLine(context)).apply();
    }

    public static void restoreBackupFavouritePkmnList(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String backup = sp.getString(PREF_FAVOURITE_POKEMON_OLD, "");
        sp.edit().putString(PREF_FAVORITE_POKEMON, backup).apply();
    }
}
