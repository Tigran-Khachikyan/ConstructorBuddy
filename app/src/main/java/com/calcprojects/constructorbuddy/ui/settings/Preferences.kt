package com.calcprojects.constructorbuddy.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.calcprojects.constructorbuddy.R

class Preferences : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
    }
}