package com.calcprojects.constructorbuddy.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
import android.view.View

interface ScreenConfigurations {

    val hostActivity: Activity?

    fun setScreenConfigurations(
        orientationVertical: Boolean = true,
        fullScreenMode: Boolean = true,
        bottomNavViewVisible: Boolean = true,
        bottomNavViewAnim: Boolean = false
    ) {
        setScreenOrientationVertical(orientationVertical)
        setSystemVisibilityFullScreen(fullScreenMode)
        setBottomNavViewVisible(bottomNavViewVisible, bottomNavViewAnim)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun setScreenOrientationVertical(vertical: Boolean = true) {
        hostActivity?.let {
            (it as MainActivity).requestedOrientation =
                if (vertical) ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                else ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    private fun setSystemVisibilityFullScreen(fullScreen: Boolean = true) {

        hostActivity?.let {
            (it as MainActivity).window.decorView.systemUiVisibility =

                if (fullScreen) (View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)
                else (View.SYSTEM_UI_FLAG_VISIBLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION )
        }
    }

    private fun setBottomNavViewVisible(visible: Boolean = true, withAnim: Boolean = false) {

        val navView = hostActivity?.let { (it as MainActivity).bottomNavView }
        navView?.run {
            if (!withAnim)
                visibility = if (visible) View.VISIBLE else View.GONE
            else {
                if (visible) {
                    alpha = 0f
                    visibility = View.VISIBLE
                    animate().apply { duration = SCREEN_DELAY_TIME }.alpha(1f).start()
                } else
                    animate().apply { duration = SCREEN_DELAY_TIME }.alpha(0f).withEndAction {
                        visibility = View.GONE
                    }.start()
            }
        }
    }
}