package com.alexpi.texttools

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

/**
 * Created by Alex on 05/01/2017.
 */
class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}