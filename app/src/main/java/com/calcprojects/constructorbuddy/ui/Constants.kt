package com.calcprojects.constructorbuddy.ui

import android.content.Context
import java.text.DecimalFormat

const val NO_INPUT: Double = -1.0
const val SELECTED: Boolean = true
const val UNSELECTED: Boolean = false
const val SCREEN_DELAY_TIME = 800L
const val SPlASH_DELAY_TIME = 1600L
const val PROGRESS_SHOW_DELAY_TIME = 800L
const val DEFAULT_RES_ARG = -1

const val KEY_UNITS = "key_units"
const val KEY_RATES = "key_rates"
const val KEY_PRICE_SWITCHER = "key_price_switcher"
const val KEY_MANUALLY_SWITCHER = "key_auto_manually_switcher"
const val LOG_EXCEPTION = "asbadbsd51"
const val LOG_VAL = "ljhbasp81w"

fun Double.to2p(): String = DecimalFormat("#,###.##").format(this)
fun Double.to3p(): String = DecimalFormat("#,###.###").format(this)

fun Int.name(context: Context): String? {
    return try {
        context.resources.getString(this)
    } catch (ex: Exception) {
        null
    }
}