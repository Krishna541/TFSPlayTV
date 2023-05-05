package `in`.fiberstory.tfsplaytv.utility

import android.content.Context
import androidx.preference.PreferenceManager

object CheckUserLogin {
    fun loadPrefs(context: Context?): Int? {
        val sp = context?.let {
            PreferenceManager.getDefaultSharedPreferences(it)
        }
        return sp?.getInt("pref_login", 0)
    }
}