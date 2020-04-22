package com.calcprojects.constructorbuddy.ui

data class StateUIActivity(
    val systemUiVisibility: Int? = null,
    val bottomNavViewVisibility: Boolean = true,
    val requestedOrientation: Int? = null,
    val delayTime: Long = 0
)