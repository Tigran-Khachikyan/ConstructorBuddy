package com.calcprojects.constructorbuddy.ui

import android.content.Context
import java.text.DecimalFormat

const val NO_INPUT: Double = -1.0
const val SELECTED: Boolean = true
const val UNSELECTED: Boolean = false
const val SCREEN_DELAY_TIME = 800L
const val DEFAULT_RES_ARG = -1

const val KEY_UNITS = "key_units"
const val KEY_RATES = "key_rates"

fun Double.to2p(): String = DecimalFormat("#,###.##").format(this)
fun Double.to3p(): String = DecimalFormat("#,###.###").format(this)

fun Int.name(context: Context): String? {
    return try {
        context.resources.getString(this)
    } catch (ex: Exception) {
        null
    }
}