package com.webengage.sample.push

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.webengage.sample.Utils.Constants
import com.webengage.sdk.android.WebEngage
import java.util.Date


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

    fun copyToClipBoard(context: Context, string: String) {
        val clipboard: ClipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(string + " copied", string)
        clipboard.setPrimaryClip(clip)
    }

    fun triggerEvent(context: Context, eventName: String, eventValue: Map<String, Any?>?) {
        if (eventValue != null)
            WebEngage.get().analytics().track(eventName, eventValue)
        else
            WebEngage.get().analytics().track(eventName)
    }

    fun updateUserAttribute(context: Context, userAttribute: String, attributeValue: Any?) {
        when (attributeValue) {
            is String -> WebEngage.get().user().setAttribute(userAttribute, attributeValue)

            is Date -> WebEngage.get().user().setAttribute(userAttribute, attributeValue)

            is Boolean -> WebEngage.get().user().setAttribute(userAttribute, attributeValue)

            is Number -> WebEngage.get().user().setAttribute(userAttribute, attributeValue)

            else -> Log.e(Constants.TAG,"Unable to update user attribute because of value type")

        }
    }

}