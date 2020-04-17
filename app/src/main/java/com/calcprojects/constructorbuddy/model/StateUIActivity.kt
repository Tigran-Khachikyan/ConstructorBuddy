package com.calcprojects.constructorbuddy.model

data class StateUIActivity(
    val systemUiVisibility: Int? = null,
    val bottomNavViewVisibility: Boolean = true,
    val requestedOrientation: Int? = null,
    val delayTime: Long = 0
)