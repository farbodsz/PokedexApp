package com.satsumasoftware.pokedex.framework

import android.content.Context
import com.satsumasoftware.pokedex.util.PrefUtils
import java.math.RoundingMode
import java.text.DecimalFormat

class Height(val dbValue: Int) {

    val metricValue: Double

    init {
        metricValue = dbValue / 10.0
    }

    fun getDisplayedText(context: Context) = if (PrefUtils.useImperialUnits(context)) {
        val decimalFormat = DecimalFormat("#.#")  // one decimal place
        decimalFormat.roundingMode = RoundingMode.HALF_UP

        val metresToFeet: Double = 3.28084
        val inFeet = (metricValue * metresToFeet).toDouble()

        val formattedVal = decimalFormat.format(inFeet)
        "$formattedVal ft"

    } else {
        "$metricValue m"
    }

}
