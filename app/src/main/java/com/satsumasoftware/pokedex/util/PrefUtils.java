package com.satsumasoftware.pokedex.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.satsumasoftware.pokedex.BuildConfig;
import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.db.AbilityDBHelper;
import com.satsumasoftware.pokedex.db.LearnsetDBHelper;
import com.satsumasoftware.pokedex.db.LocationDBHelper;
import com.satsumasoftware.pokedex.db.MoveDBHelper;
import com.satsumasoftware.pokedex.db.NatureDBHelper;
import com.satsumasoftware.pokedex.db.PokedexDBHelper;
import com.satsumasoftware.pokedex.db.internal.EncounterDBHelper;
import com.satsumasoftware.pokedex.db.internal.EncounterSlotsDBHelper;
import com.satsumasoftware.pokedex.db.internal.LocationAreaDBHelper;
import com.satsumasoftware.pokedex.db.internal.LocationAreaEncounterRatesDBHelper;
import com.satsumasoftware.pokedex.db.internal.LocationGameIndicesDBHelper;
import com.satsumasoftware.pokedex.object.MiniPokemon;
import com.satsumasoftware.pokedex.object.Pokemon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public final class PrefUtils {

    // Boolean indicating whether the one-time welcome has been done
    // (there will be a different preferences string value for different versions of the
    // database as preferences are not reset on app update)
    public static final String PREF_DATABASE_INITIALISED = "pref_database_initialised_" +
            PokedexDBHelper.DATABASE_VERSION +
            AbilityDBHelper.DATABASE_VERSION +
            MoveDBHelper.DATABASE_VERSION +
            NatureDBHelper.DATABASE_VERSION +
            LearnsetDBHelper.DATABASE_VERSION +
            LocationDBHelper.DATABASE_VERSION + // TODO: Remove before production
            EncounterDBHelper.DATABASE_VERSION +
            EncounterSlotsDBHelper.DATABASE_VERSION +
            LocationAreaDBHelper.DATABASE_VERSION +
            LocationAreaEncounterRatesDBHelper.DATABASE_VERSION +
            LocationGameIndicesDBHelper.DATABASE_VERSION;

    // Boolean indicating whether the one-time welcome has been done
    // (there will be a different preferences string value for different versions of the
    // app as preferences are not reset on app update)
    public static final String PREF_WELCOME_DONE = "pref_welcome_done_" + BuildConfig.VERSION_CODE;

    // Boolean indicating whether the prompt/helper card in the Advanced Filter activity
    // has been dismissed
    public static final String PREF_PROMPT_FILTER_DONE = "pref_prompt_filter_done";

    /*
     * Pref page
     */

    public static final String PREF_POKEDEX_COMBINED_NAME = "pref_pokedex_combined_name";
    public static final String PREF_ENABLE_IMAGES = "pref_enable_images";
    public static final String PREF_PLAY_CRY_AT_START = "pref_play_cry_at_start";

    // Boolean indicating if the detail page should be coloured by type
    public static final String PREF_DETAIL_COLOURING = "pref_detail_colouring_type";


    // String of words separated by commas to show favourite Pokémon
    public static final String PREF_FAVOURITE_POKEMON = "pref_favourite_pokemon";
    public static final String PREF_FAVOURITE_POKEMON_OLD = "pref_favourite_pokemon_old";



    /*
     * Favourite Pokemon
     */

    public static void addFavouritePkmn(Context context, String id, String name, String form) {
        // Parameter 'pokemon' is entered as example 123_Pokemon_Form
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String currentValue = sp.getString(PREF_FAVOURITE_POKEMON, "");
        String pokemonInfo = id + "_" + name + "_" + form;
        String addToList = currentValue + "," + pokemonInfo;
        sp.edit().putString(PREF_FAVOURITE_POKEMON, addToList).apply();
    }

    public static void removeFavouritePkmn(Context context, String id, String name, String form) {
        String whatToDelete = id + "_" + name + "_" + form;
        String line = getFavouritePkmnRawLine(context);

        if (line.contains(whatToDelete)) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            String editedList = line.replace(whatToDelete, "");
            sp.edit().putString(PREF_FAVOURITE_POKEMON, editedList).apply();
        } else {
            Toast.makeText(context, R.string.favourites_error, Toast.LENGTH_SHORT).show();
        }
    }

    public static String getFavouritePkmnRawLine(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_FAVOURITE_POKEMON, "");
    }

    public static void doTempBugFix(Context context) {
        // Replaces formatted forms with raw form names
        String line = getFavouritePkmnRawLine(context);
        PokedexDBHelper helper = new PokedexDBHelper(context);
        ArrayList<MiniPokemon> miniPokemons = helper.getAllPokemon();
        for (int i = 0; i < miniPokemons.size(); i++) {
            String formattedForm = miniPokemons.get(i).getFormFormatted();
            String plainForm = miniPokemons.get(i).getForm();
            line = line.replace(formattedForm, plainForm);
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_FAVOURITE_POKEMON, line).apply();
    }

    public static ArrayList<MiniPokemon> getFavouritePkmnArrayLists(Context context) {
        String line = getFavouritePkmnRawLine(context);

        ArrayList<MiniPokemon> pokemonList = new ArrayList<>();

        String[] segments = line.split(",");

        Collections.sort(Arrays.asList(segments));

        for (String aSegment : segments) {
            String[] details = aSegment.split("_");
            if (details.length == 3) {
                int id = Integer.parseInt(details[0]);
                String name = details[1];
                String form;
                if (details[2].equals("NONE")) {
                    form = "";
                } else {
                    form = details[2];
                }
                pokemonList.add(new MiniPokemon(id, name, form));
            }
        }

        return pokemonList;
    }

    public static boolean isAFavouritePkmn(Context context, String id, String name, String form) {
        String whatToFind = id + "_" + name + "_" + form;
        String line = getFavouritePkmnRawLine(context);

        return line.contains(whatToFind);
    }

    // TODO: Change favourites methods to use MiniPokemon objects instead of strings
    public static boolean isAFavouritePkmn(Context context, MiniPokemon miniPokemon) {
        return isAFavouritePkmn(
                context,
                miniPokemon.getNationalIdFormatted(),
                miniPokemon.getPokemon(),
                miniPokemon.getForm());
    }

    public static void markAsFavouritePkmn(Context context, String id, String name, String form, View rootLayout) {
        String markType, formText;

        if (form.equals("")) {
            form = "NONE";
        }
        if (isAFavouritePkmn(context, id, name, form)) {
            removeFavouritePkmn(context, id, name, form);
            markType = "removed from";
        } else {
            addFavouritePkmn(context, id, name, form);
            markType = "added to";
        }
        if (form.equals("NONE")) {
            formText = "";
        } else {
            formText = " (" + form + ")";
        }

        String pokemonTxt = name + formText;
        String message = context.getResources().getString(R.string.mark_favourites, pokemonTxt, markType);

        Snackbar.make(rootLayout, message, Snackbar.LENGTH_SHORT)
                .show();
    }

    public static void clearFavouritePkmnList(final Context context) {
        backupOldFavouritePkmnList(context);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_FAVOURITE_POKEMON, "").apply();

        // Snackbar message is shown in the activity calling to this method
    }

    public static void backupOldFavouritePkmnList(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_FAVOURITE_POKEMON_OLD, getFavouritePkmnRawLine(context)).apply();
    }

    public static void restoreBackupFavouritePkmnList(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String backup = sp.getString(PREF_FAVOURITE_POKEMON_OLD, "");
        sp.edit().putString(PREF_FAVOURITE_POKEMON, backup).apply();
    }

    /*
     * TEAM POKEMON
     */

    public static ArrayList<Pokemon> getTeamPkmnList(Context context) {
        ArrayList<Pokemon> list = new ArrayList<>();
        list.add(new Pokemon(context, "Pikachu", ""));
        list.add(new Pokemon(context, "Raichu", ""));
        return list;
    }

    /*
     * OTHERS
     */

    public static boolean hasDatabaseInitialised(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_DATABASE_INITIALISED, false);
    }

    public static void markDatabaseInitialised(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_DATABASE_INITIALISED, true).apply();
    }

    public static boolean isWelcomeDone(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_WELCOME_DONE, false);
    }

    public static void markWelcomeDone(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_WELCOME_DONE, true).apply();
    }


    public static boolean isFilterPromptDone(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_PROMPT_FILTER_DONE, false);
    }

    public static void markFilterPromptDone(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_PROMPT_FILTER_DONE, true).apply();
    }

    public static boolean combinePokemonNameInPokedex(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_POKEDEX_COMBINED_NAME, false);
    }

    public static final String PREF_DETAIL_COLOURING_VALUE_DEFAULT = "default";
    public static final String PREF_DETAIL_COLOURING_VALUE_TYPE = "type";
    public static final String PREF_DETAIL_COLOURING_VALUE_COLOUR = "colour";

    public static String detailColourType(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_DETAIL_COLOURING, "default");
        // "default" = default/none, "colour" = by colour, "type" = by type
    }

    public static boolean showPokemonImages(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_ENABLE_IMAGES, false);
    }

    public static boolean playPokemonCryAtStart(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_PLAY_CRY_AT_START, false);
    }

}
