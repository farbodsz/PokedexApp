package com.satsumasoftware.pokedex.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.satsumasoftware.pokedex.BuildConfig;

import java.util.Locale;

public final class PrefUtils {


    /* Welcome page */

    public static final String PREF_WELCOME_DONE = "pref_welcome_done_" + BuildConfig.VERSION_CODE;

    public static boolean isWelcomeDone(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_WELCOME_DONE, false);
    }

    public static void markWelcomeDone(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_WELCOME_DONE, true).apply();
    }


    /* App usage tip text */

    public static final String PREF_TIPS_CONTINUE_FROM_INDEX = "pref_tips_continue_from_index";

    public static int getTipsContinueFromIndex(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(PREF_TIPS_CONTINUE_FROM_INDEX, 0);
    }

    public static void setTipsContinueFromIndex(Context context, int continueFromIndex) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt(PREF_TIPS_CONTINUE_FROM_INDEX, continueFromIndex).apply();
    }


    /* Filter prompt */

    public static final String PREF_PROMPT_FILTER_DONE = "pref_prompt_filter_done";

    public static boolean isFilterPromptDone(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_PROMPT_FILTER_DONE, false);
    }

    public static void markFilterPromptDone(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_PROMPT_FILTER_DONE, true).apply();
    }


    /* Combined name in Pokedex */

    public static final String PREF_POKEDEX_COMBINED_NAME = "pref_pokedex_combined_name";

    public static boolean combinePokemonNameInPokedex(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_POKEDEX_COMBINED_NAME, false);
    }


    /* Alphabetical sorting */

    public static final String PREF_ABILITIES_SORT_AZ = "pref_abilities_sort_default_az";

    public static boolean sortAbilitiesAlphabetically(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_ABILITIES_SORT_AZ, false);
    }

    public static final String PREF_MOVES_SORT_AZ = "pref_moves_sort_default_az";

    public static boolean sortMovesAlphabetically(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_MOVES_SORT_AZ, false);
    }


    /* Detail page coloring */

    public static final String PREF_DETAIL_COLORING = "pref_detail_colouring_type";

    public static final String PREF_DETAIL_COLORING_VALUE_DEFAULT = "default";
    public static final String PREF_DETAIL_COLORING_VALUE_TYPE = "type";
    public static final String PREF_DETAIL_COLORING_VALUE_COLOUR = "colour";

    public static String detailColorType(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_DETAIL_COLORING, "default");
        // "default" = default/none, "colour" = by colour, "type" = by type
    }


    /* Show Pokemon images */

    public static final String PREF_ENABLE_IMAGES = "pref_enable_images";

    public static boolean showPokemonImages(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_ENABLE_IMAGES, false);
    }


    /* Play Pokemon cry at start */

    public static final String PREF_PLAY_CRY_AT_START = "pref_play_cry_at_start";

    public static boolean playPokemonCryAtStart(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_PLAY_CRY_AT_START, false);
    }


    /* Use imperial units */

    public static final String PREF_USE_IMPERIAL_UNITS = "pref_use_imperial_units";

    public static boolean useImperialUnits(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_USE_IMPERIAL_UNITS, false);
    }


    /* Language */

    public static final String PREF_APP_LOCALE = "pref_app_language";

    public static Locale getAppLocale(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String language = sp.getString(PREF_APP_LOCALE, "en");
        return new Locale(language);
    }

}
