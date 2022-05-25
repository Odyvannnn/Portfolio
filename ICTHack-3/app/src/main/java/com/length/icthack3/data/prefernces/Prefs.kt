package com.length.icthack3.data.prefernces

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.length.icthack3.domain.model.User

class Prefs(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_USER, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getUser(): User? {
        val json = sharedPreferences.getString(PREF_NAME_CURRENT_USER, null) ?: return null
        return gson.fromJson(json, User::class.java)
    }

    fun saveAndEditUser(user: User) {
        val json = gson.toJson(user)
        sharedPreferences.edit()
            .putString(PREF_NAME_CURRENT_USER,  json)
            .apply()
    }

    companion object {
        private const val PREFS_USER = "User"
        private const val PREF_NAME_CURRENT_USER = "currentUser"
    }
}