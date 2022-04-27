package it.niko.mywatchlistandroid

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER = "username"
        const val USER_TOKEN = "token"
        const val WATCHING = "WATCHING"
        const val COMPLETED = "COMPLETED"
        const val ON_HOLD = "ON_HOLD"
        const val DROPPED = "DROPPED"
        const val PLAN_TO_WATCH = "PLAN_TO_WATCH"
    }

    fun saveAuthToken(username:String, token: String) {
        val editor = prefs.edit()
        editor.putString(USER, username)
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun fetchUsername(): String? {
        return prefs.getString(USER, null)
    }
}