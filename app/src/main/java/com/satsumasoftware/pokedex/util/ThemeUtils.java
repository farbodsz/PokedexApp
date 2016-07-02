package com.satsumasoftware.pokedex.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.StyleRes;
import android.util.TypedValue;

import com.satsumasoftware.pokedex.R;

public final class ThemeUtils {

    public static void colourDetailByType(Activity activity, int typeId) {
        switch (typeId) {
            case 1:
                useTheme(activity, R.style.AppTheme_Detail_Type_Normal);
                break;
            case 10:
                useTheme(activity, R.style.AppTheme_Detail_Type_Fire);
                break;
            case 2:
                useTheme(activity, R.style.AppTheme_Detail_Type_Fighting);
                break;
            case 11:
                useTheme(activity, R.style.AppTheme_Detail_Type_Water);
                break;
            case 3:
                useTheme(activity, R.style.AppTheme_Detail_Type_Flying);
                break;
            case 12:
                useTheme(activity, R.style.AppTheme_Detail_Type_Grass);
                break;
            case 4:
                useTheme(activity, R.style.AppTheme_Detail_Type_Poison);
                break;
            case 13:
                useTheme(activity, R.style.AppTheme_Detail_Type_Electric);
                break;
            case 5:
                useTheme(activity, R.style.AppTheme_Detail_Type_Ground);
                break;
            case 14:
                useTheme(activity, R.style.AppTheme_Detail_Type_Psychic);
                break;
            case 6:
                useTheme(activity, R.style.AppTheme_Detail_Type_Rock);
                break;
            case 15:
                useTheme(activity, R.style.AppTheme_Detail_Type_Ice);
                break;
            case 7:
                useTheme(activity, R.style.AppTheme_Detail_Type_Bug);
                break;
            case 16:
                useTheme(activity, R.style.AppTheme_Detail_Type_Dragon);
                break;
            case 8:
                useTheme(activity, R.style.AppTheme_Detail_Type_Ghost);
                break;
            case 17:
                useTheme(activity, R.style.AppTheme_Detail_Type_Dark);
                break;
            case 9:
                useTheme(activity, R.style.AppTheme_Detail_Type_Steel);
                break;
            case 18:
                useTheme(activity, R.style.AppTheme_Detail_Type_Fairy);
                break;
            default:
                throw new IllegalArgumentException("type id \"" +
                        typeId + "\" is not a valid type");
        }
    }

    public static void colourDetailByColour(Activity activity, int colorId) {
        switch (colorId) {
            case 1:
                useTheme(activity, R.style.AppTheme_Detail_Colour_Black);
                break;
            case 2:
                useTheme(activity, R.style.AppTheme_Detail_Colour_Blue);
                break;
            case 3:
                useTheme(activity, R.style.AppTheme_Detail_Colour_Brown);
                break;
            case 4:
                useTheme(activity, R.style.AppTheme_Detail_Colour_Grey);
                break;
            case 5:
                useTheme(activity, R.style.AppTheme_Detail_Colour_Green);
                break;
            case 6:
                useTheme(activity, R.style.AppTheme_Detail_Colour_Pink);
                break;
            case 7:
                useTheme(activity, R.style.AppTheme_Detail_Colour_Purple);
                break;
            case 8:
                useTheme(activity, R.style.AppTheme_Detail_Colour_Red);
                break;
            case 9:
                useTheme(activity, R.style.AppTheme_Detail_Colour_White);
                break;
            case 10:
                useTheme(activity, R.style.AppTheme_Detail_Colour_Yellow);
                break;
            default:
                throw new IllegalArgumentException("color id \"" +
                        colorId + "\" is not a valid color");
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

    public static int getLightColourByType(Context context, String type) {
        try {
            String name = "type_" + type.toLowerCase() + "_primary_light";
            return context.getResources().getIdentifier(name, "color", context.getPackageName());
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
