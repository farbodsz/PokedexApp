package com.satsumasoftware.pokedex.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.ColorRes;
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

    @ColorRes
    public static int getTypeBkgdColorRes(int typeId) {
        // TODO: Generate programmatically
        switch (typeId) {
            case 1: return R.color.type_normal;
            case 2: return R.color.type_fighting;
            case 3: return R.color.type_flying;
            case 4: return R.color.type_poison;
            case 5: return R.color.type_ground;
            case 6: return R.color.type_rock;
            case 7: return R.color.type_bug;
            case 8: return R.color.type_ghost;
            case 9: return R.color.type_steel;
            case 10: return R.color.type_fire;
            case 11: return R.color.type_water;
            case 12: return R.color.type_grass;
            case 13: return R.color.type_electric;
            case 14: return R.color.type_psychic;
            case 15: return R.color.type_ice;
            case 16: return R.color.type_dragon;
            case 17: return R.color.type_dark;
            case 18: return R.color.type_fairy;
            default: return R.color.mdu_text_black;
        }
    }

    @Deprecated
    public static int getTypeBkgdColorRes(String type) {
        if (type.equalsIgnoreCase("normal")) {
            return R.color.type_normal;
        } else if (type.equalsIgnoreCase("fire")) {
            return R.color.type_fire;
        } else if (type.equalsIgnoreCase("fighting")) {
            return R.color.type_fighting;
        } else if (type.equalsIgnoreCase("water")) {
            return R.color.type_water;
        } else if (type.equalsIgnoreCase("flying")) {
            return R.color.type_flying;
        } else if (type.equalsIgnoreCase("grass")) {
            return R.color.type_grass;
        } else if (type.equalsIgnoreCase("poison")) {
            return R.color.type_poison;
        } else if (type.equalsIgnoreCase("electric")) {
            return R.color.type_electric;
        } else if (type.equalsIgnoreCase("ground")) {
            return R.color.type_ground;
        } else if (type.equalsIgnoreCase("psychic")) {
            return R.color.type_psychic;
        } else if (type.equalsIgnoreCase("rock")) {
            return R.color.type_rock;
        } else if (type.equalsIgnoreCase("ice")) {
            return R.color.type_ice;
        } else if (type.equalsIgnoreCase("bug")) {
            return R.color.type_bug;
        } else if (type.equalsIgnoreCase("dragon")) {
            return R.color.type_dragon;
        } else if (type.equalsIgnoreCase("ghost")) {
            return R.color.type_ghost;
        } else if (type.equalsIgnoreCase("dark")) {
            return R.color.type_dark;
        } else if (type.equalsIgnoreCase("steel")) {
            return R.color.type_steel;
        } else if (type.equalsIgnoreCase("fairy")) {
            return R.color.type_fairy;
        } else if (type.equalsIgnoreCase("none")) {
            return R.color.mdu_text_black;
        } else {
            return R.color.mdu_text_black;
        }
    }
}
