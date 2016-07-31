package com.satsumasoftware.pokedex.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.StyleRes
import android.util.TypedValue
import com.satsumasoftware.pokedex.R
import com.satsumasoftware.pokedex.framework.Type


fun colourDetailByType(activity: Activity, typeId: Int) = when (typeId) {
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

fun colourDetailByColour(activity: Activity, colorId: Int) = when (colorId) {
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

private fun useTheme(activity: Activity, @StyleRes theme: Int) {
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

@ColorRes
fun getTypeBkgdColorRes(context: Context, typeId: Int) = try {
    val type = Type(typeId)
    val name = if (type.isMainType()) {
        "type_" + type.name.toLowerCase()
    } else {
        "mdu_text_black"
    }
    context.resources.getIdentifier(name, "color", context.packageName)
} catch (e: Resources.NotFoundException) {
    e.printStackTrace()
    0
}

@Deprecated("Use the function taking type id instead")
fun getTypeBkgdColorRes(context: Context, type: String) = getTypeBkgdColorRes(context, Type(type).id)
