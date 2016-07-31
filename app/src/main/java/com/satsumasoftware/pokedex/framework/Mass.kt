package com.satsumasoftware.pokedex.framework

import android.content.Context
import com.satsumasoftware.pokedex.util.PrefUtils
import java.math.RoundingMode
import java.text.DecimalFormat

class Mass(val dbValue: Int) {

    val metricValue: Double

    init {
        metricValue = dbValue / 10.0
    }

    fun getDisplayedText(context: Context) = if (PrefUtils.useImperialUnits(context)) {
        val decimalFormat = DecimalFormat("#.#")  // one decimal place
        decimalFormat.roundingMode = RoundingMode.HALF_UP

        val kilogramsToPounds: Double = 2.2046226218
        val inPounds = (metricValue * kilogramsToPounds).toDouble()

        val formattedVal = decimalFormat.format(inPounds)
        "$formattedVal lbs"

    } else {
        "$metricValue kg"
    }

}
