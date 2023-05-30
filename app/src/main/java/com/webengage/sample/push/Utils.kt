package com.webengage.sample.push

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.webengage.sample.Utils.Constants
import com.webengage.sdk.android.WebEngage

class Utils {

    companion object {
        val PUSH_NOTIFICATIONS = "android.permission.POST_NOTIFICATIONS"
    }

    fun registerFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            try {
                val token: String? = task.result
                WebEngage.get().setRegistrationID(token)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setDevicePushOptIn(status: Boolean) {
        WebEngage.get().user().setDevicePushOptIn(status)
    }
}