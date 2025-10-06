@file:Suppress("unused")

package com.emporiumz.app.utils

import android.content.Context
import androidx.core.content.edit

class Prefs(context: Context) {
    private val prefs = context.getSharedPreferences("emporiumz_prefs", Context.MODE_PRIVATE)
    var userId: Int
        get() = prefs.getInt("userId", 0)
        set(v) = prefs.edit { putInt("userId", v) }
    var userName: String?
        get() = prefs.getString("userName", null)
        set(v) = prefs.edit { putString("userName", v) }
    fun clear() = prefs.edit { clear() }
}