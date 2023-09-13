package com.webengage.sample.push

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.webengage.pushtemplates.CustomCallback
import com.webengage.sample.Utils.Constants
import com.webengage.sdk.android.actions.render.PushNotificationData

class CustomCallbackAlarm : CustomCallback() {

    override fun onRerender(
        context: Context?,
        pushNotificationData: PushNotificationData?,
        extras: Bundle?
    ): Boolean {
        Log.d(Constants.TAG,"Received rerender callback")
        if (extras != null) {
            if (extras.containsKey("RENDER_TYPE") && extras.getString("RENDER_TYPE")
                    .equals(Constants.SNOOZE_TEMPLATE)
            ) {
                Log.d(Constants.TAG,"Received snooze rerender callback")
                CustomPushRenderer().renderPushNotification(context!!, pushNotificationData!!)
                return true
            }
        }
        return super.onRerender(context, pushNotificationData, extras)
    }

    override fun onRender(context: Context?, pushNotificationData: PushNotificationData?): Boolean {
        if (pushNotificationData!!.customData.containsKey(Constants.TEMPLATE_TYPE) && pushNotificationData.customData.getString(
                Constants.TEMPLATE_TYPE
            )!!.equals(Constants.SNOOZE_TEMPLATE, true)
        ) {
            CustomPushRenderer().renderPushNotification(context!!, pushNotificationData)
            return true
        }
        return super.onRender(context, pushNotificationData)
    }
}