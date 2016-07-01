package com.phoenixenterprise.pokedex.util;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.StyleRes;
import android.util.TypedValue;

import com.phoenixenterprise.pokedex.R;

public final class ThemeUtils {

    public static void colourDetailByType(Activity activity, String type) {
        switch (type.toLowerCase()) {
            case "normal":
                useTheme(activity, R.style.AppTheme_Detail_Type_Normal);
                break;
            case "fire":
                useTheme(activity, R.style.AppTheme_Detail_Type_Fire);
                break;
            case "fighting":
                useTheme(activity, R.style.AppTheme_Detail_Type_Fighting);
                break;
            case "water":
                useTheme(activity, R.style.AppTheme_Detail_Type_Water);
                break;
            case "flying":
                useTheme(activity, R.style.AppTheme_Detail_Type_Flying);
                break;
            case "grass":
                useTheme(activity, R.style.AppTheme_Detail_Type_Grass);
                break;
            case "poison":
                useTheme(activity, R.style.AppTheme_Detail_Type_Poison);
                break;
            case "electric":
                useTheme(activity, R.style.AppTheme_Detail_Type_Electric);
                break;
            case "ground":
                useTheme(activity, R.style.AppTheme_Detail_Type_Ground);
                break;
            case "psychic":
                useTheme(activity, R.style.AppTheme_Detail_Type_Psychic);
                break;
            case "rock":
                useTheme(activity, R.style.AppTheme_Detail_Type_Rock);
                break;
            case "ice":
                useTheme(activity, R.style.AppTheme_Detail_Type_Ice);
                break;
            case "bug":
                useTheme(activity, R.style.AppTheme_Detail_Type_Bug);
                break;
            case "dragon":
                useTheme(activity, R.style.AppTheme_Detail_Type_Dragon);
                break;
            case "ghost":
                useTheme(activity, R.style.AppTheme_Detail_Type_Ghost);
                break;
            case "dark":
                useTheme(activity, R.style.AppTheme_Detail_Type_Dark);
                break;
            case "steel":
                useTheme(activity, R.style.AppTheme_Detail_Type_Steel);
                break;
            case "fairy":
                useTheme(activity, R.style.AppTheme_Detail_Type_Fairy);
                break;
            default:
                throw new IllegalArgumentException("type \"" +
                        type + "\" is not a valid type");
        }
    }

    public static void colourDetailByColour(Activity activity, String colour) {
        switch (colour.toLowerCase()) {
            case "black":
                useTheme(activity, R.style.AppTheme_Detail_Colour_Black);
                break;
            case "blue":
                useTheme(activity, R.style.AppTheme_Detail_Colour_Blue);
                break;
            case "brown":
                useTheme(activity, R.style.AppTheme_Detail_Colour_Brown);
                break;
            case "grey":
                useTheme(activity, R.style.AppTheme_Detail_Colour_Grey);
                break;
            case "green":
                useTheme(activity, R.style.AppTheme_Detail_Colour_Green);
                break;
            case "pink":
                useTheme(activity, R.style.AppTheme_Detail_Colour_Pink);
                break;
            case "purple":
                useTheme(activity, R.style.AppTheme_Detail_Colour_Purple);
                break;
            case "red":
                useTheme(activity, R.style.AppTheme_Detail_Colour_Red);
                break;
            case "white":
                useTheme(activity, R.style.AppTheme_Detail_Colour_White);
                break;
            case "yellow":
                useTheme(activity, R.style.AppTheme_Detail_Colour_Yellow);
                break;
            default:
                throw new IllegalArgumentException("type \"" +
                        colour + "\" is not a valid colour");
        }
    }

    public static void useTheme(Activity activity, @StyleRes int theme){
        // http://stackoverflow.com/a/31137826/4230345
        activity.setTheme(theme);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(getAttributeColor(activity, R.attr.colorPrimaryDark));
        }
    }

    private static int getAttributeColor(Activity activity, int resId) {
        // http://stackoverflow.com/a/31137826/4230345
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(resId, typedValue, true);
        int color = 0x000000;
        if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            // if resId is a color
            color = typedValue.data;
        }
        return color;
    }

}
