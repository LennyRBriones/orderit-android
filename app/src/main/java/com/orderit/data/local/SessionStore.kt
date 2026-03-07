package com.orderit.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("orderit_session", Context.MODE_PRIVATE)

    var currentWaiterId: Int
        get() = prefs.getInt("waiter_id", -1)
        set(value) = prefs.edit().putInt("waiter_id", value).apply()

    var currentWaiterName: String
        get() = prefs.getString("waiter_name", "") ?: ""
        set(value) = prefs.edit().putString("waiter_name", value).apply()

    var currentWaiterUsername: String
        get() = prefs.getString("waiter_username", "") ?: ""
        set(value) = prefs.edit().putString("waiter_username", value).apply()

    val isLoggedIn: Boolean get() = currentWaiterId != -1

    fun clear() = prefs.edit().clear().apply()
}
