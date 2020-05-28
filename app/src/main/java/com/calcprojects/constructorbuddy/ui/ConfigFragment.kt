package com.calcprojects.constructorbuddy.ui

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import kotlinx.coroutines.*

abstract class ConfigFragment :
    Fragment(),
    ScreenConfigurations,
    SendModel {

    override val hostActivity: Activity?
        get() = activity
}