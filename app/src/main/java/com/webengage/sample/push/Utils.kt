package com.webengage.sample.push

import android.app.AlarmManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.webengage.sample.Utils.Constants
import com.webengage.sdk.android.PendingIntentFactory
import com.webengage.sdk.android.WebEngage
import com.webengage.sdk.android.actions.render.PushNotificationData
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

            else -> Log.e(Constants.TAG, "Unable to update user attribute because of value type")

        }
    }

    fun isAndroid12(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && context.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.S)
            return true
        return false
    }

    fun hasBackgroundColor(pushNotificationData: PushNotificationData): Boolean {
        if (pushNotificationData.getBackgroundColor() != android.graphics.Color.parseColor("#00000000"))
            return true
        return false
    }

    fun scheduleAlarm(context: Context, time: Long, data: PushNotificationData, renderType : String) {

        Log.d(Constants.TAG,"Scheduling alarm")

        val extras = Bundle()
        extras.putString("RENDER_TYPE", renderType)

        val intent = PendingIntentFactory.constructRerenderPendingIntent(context, data, Constants.SNOOZE_TEMPLATE, extras )

        val alarmManager =
            context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + time, intent)
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + time, intent)
        }
    }

}