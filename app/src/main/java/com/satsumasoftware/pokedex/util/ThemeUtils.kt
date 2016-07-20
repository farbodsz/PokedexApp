package com.satsumasoftware.pokedex.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.StyleRes
import android.util.TypedValue
import com.satsumasoftware.pokedex.R


// TODO generate programmatically
fun colourDetailByType(activity: Activity, typeId: Int) {
    when (typeId) {
        1 -> useTheme(activity, R.style.AppTheme_Detail_Type_Normal)
        10 -> useTheme(activity, R.style.AppTheme_Detail_Type_Fire)
        2 -> useTheme(activity, R.style.AppTheme_Detail_Type_Fighting)
        11 -> useTheme(activity, R.style.AppTheme_Detail_Type_Water)
        3 -> useTheme(activity, R.style.AppTheme_Detail_Type_Flying)
        12 -> useTheme(activity, R.style.AppTheme_Detail_Type_Grass)
        4 -> useTheme(activity, R.style.AppTheme_Detail_Type_Poison)
        13 -> useTheme(activity, R.style.AppTheme_Detail_Type_Electric)
        5 -> useTheme(activity, R.style.AppTheme_Detail_Type_Ground)
        14 -> useTheme(activity, R.style.AppTheme_Detail_Type_Psychic)
        6 -> useTheme(activity, R.style.AppTheme_Detail_Type_Rock)
        15 -> useTheme(activity, R.style.AppTheme_Detail_Type_Ice)
        7 -> useTheme(activity, R.style.AppTheme_Detail_Type_Bug)
        16 -> useTheme(activity, R.style.AppTheme_Detail_Type_Dragon)
        8 -> useTheme(activity, R.style.AppTheme_Detail_Type_Ghost)
        17 -> useTheme(activity, R.style.AppTheme_Detail_Type_Dark)
        9 -> useTheme(activity, R.style.AppTheme_Detail_Type_Steel)
        18 -> useTheme(activity, R.style.AppTheme_Detail_Type_Fairy)
        else -> throw IllegalArgumentException("invalid type id '$typeId'")
    }
}

// TODO generate programmatically
fun colourDetailByColour(activity: Activity, colorId: Int) {
    when (colorId) {
        1 -> useTheme(activity, R.style.AppTheme_Detail_Colour_Black)
        2 -> useTheme(activity, R.style.AppTheme_Detail_Colour_Blue)
        3 -> useTheme(activity, R.style.AppTheme_Detail_Colour_Brown)
        4 -> useTheme(activity, R.style.AppTheme_Detail_Colour_Grey)
        5 -> useTheme(activity, R.style.AppTheme_Detail_Colour_Green)
        6 -> useTheme(activity, R.style.AppTheme_Detail_Colour_Pink)
        7 -> useTheme(activity, R.style.AppTheme_Detail_Colour_Purple)
        8 -> useTheme(activity, R.style.AppTheme_Detail_Colour_Red)
        9 -> useTheme(activity, R.style.AppTheme_Detail_Colour_White)
        10 -> useTheme(activity, R.style.AppTheme_Detail_Colour_Yellow)
        else -> throw IllegalArgumentException("invalid color id '$colorId'")
    }
}

fun useTheme(activity: Activity, @StyleRes theme: Int) {
    // http://stackoverflow.com/a/31137826/4230345
    activity.setTheme(theme)
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.window.statusBarColor = getAttributeColor(activity, R.attr.colorPrimaryDark)
    }
}

private fun getAttributeColor(activity: Activity, resId: Int): Int {
    // http://stackoverflow.com/a/31137826/4230345
    val typedValue = TypedValue()
    activity.theme.resolveAttribute(resId, typedValue, true)
    var color = 0x000000
    if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT &&
            typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {  // if resId is a color
        color = typedValue.data
    }
    return color
}

fun getLightColourByType(context: Context, type: String): Int {
    return try {
        val name = "type_" + type.toLowerCase() + "_primary_light"
        context.resources.getIdentifier(name, "color", context.packageName)
    } catch (e: Resources.NotFoundException) {
        e.printStackTrace()
        0
    }

}


// TODO: Generate programmatically
@ColorRes
fun getTypeBkgdColorRes(typeId: Int): Int {
    return when (typeId) {
        1 -> R.color.type_normal
        2 -> R.color.type_fighting
        3 -> R.color.type_flying
        4 -> R.color.type_poison
        5 -> R.color.type_ground
        6 -> R.color.type_rock
        7 -> R.color.type_bug
        8 -> R.color.type_ghost
        9 -> R.color.type_steel
        10 -> R.color.type_fire
        11 -> R.color.type_water
        12 -> R.color.type_grass
        13 -> R.color.type_electric
        14 -> R.color.type_psychic
        15 -> R.color.type_ice
        16 -> R.color.type_dragon
        17 -> R.color.type_dark
        18 -> R.color.type_fairy
        else -> R.color.mdu_text_black
    }
}

// TODO generate programmatically
@Deprecated("")
fun getTypeBkgdColorRes(type: String): Int {
    if (type.equals("normal", ignoreCase = true)) {
        return R.color.type_normal
    } else if (type.equals("fire", ignoreCase = true)) {
        return R.color.type_fire
    } else if (type.equals("fighting", ignoreCase = true)) {
        return R.color.type_fighting
    } else if (type.equals("water", ignoreCase = true)) {
        return R.color.type_water
    } else if (type.equals("flying", ignoreCase = true)) {
        return R.color.type_flying
    } else if (type.equals("grass", ignoreCase = true)) {
        return R.color.type_grass
    } else if (type.equals("poison", ignoreCase = true)) {
        return R.color.type_poison
    } else if (type.equals("electric", ignoreCase = true)) {
        return R.color.type_electric
    } else if (type.equals("ground", ignoreCase = true)) {
        return R.color.type_ground
    } else if (type.equals("psychic", ignoreCase = true)) {
        return R.color.type_psychic
    } else if (type.equals("rock", ignoreCase = true)) {
        return R.color.type_rock
    } else if (type.equals("ice", ignoreCase = true)) {
        return R.color.type_ice
    } else if (type.equals("bug", ignoreCase = true)) {
        return R.color.type_bug
    } else if (type.equals("dragon", ignoreCase = true)) {
        return R.color.type_dragon
    } else if (type.equals("ghost", ignoreCase = true)) {
        return R.color.type_ghost
    } else if (type.equals("dark", ignoreCase = true)) {
        return R.color.type_dark
    } else if (type.equals("steel", ignoreCase = true)) {
        return R.color.type_steel
    } else if (type.equals("fairy", ignoreCase = true)) {
        return R.color.type_fairy
    } else if (type.equals("none", ignoreCase = true)) {
        return R.color.mdu_text_black
    } else {
        return R.color.mdu_text_black
    }
}
