package com.calcprojects.constructorbuddy.ui

import java.text.DecimalFormat

const val NO_INPUT: Double = -1.0
const val SELECTED: Boolean = true
const val UNSELECTED: Boolean = false
const val SCREEN_DELAY_TIME = 800L
val decFormatter2p = DecimalFormat("#,###.##")

fun Double.to2p(): String = decFormatter2p.format(this)