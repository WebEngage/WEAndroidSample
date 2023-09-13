package com.webengage.sample

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import com.webengage.personalization.WEPersonalization
import com.webengage.pushtemplates.CustomCallback
import com.webengage.sample.Utils.Constants
import com.webengage.sample.push.CustomCallbackAlarm
import com.webengage.sample.push.Utils
import com.webengage.sdk.android.WebEngage
import com.webengage.sdk.android.WebEngageActivityLifeCycleCallbacks
import com.webengage.sdk.android.WebEngageConfig
import com.webengage.sdk.android.actions.database.ReportingStrategy
import com.webengage.sdk.android.actions.render.InAppNotificationData
import com.webengage.sdk.android.actions.render.PushNotificationData
import com.webengage.sdk.android.callbacks.InAppNotificationCallbacks
import com.webengage.sdk.android.callbacks.PushNotificationCallbacks
import com.webengage.sdk.android.callbacks.WESecurityCallback


class MainApplication : Application(), PushNotificationCallbacks, InAppNotificationCallbacks,
    WESecurityCallback {
    companion object {
        private lateinit var context: Context
        fun getContext(): Context {
            return context
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        //Create WebEngage Config
        val webEngageConfig = WebEngageConfig.Builder()
            .setWebEngageKey("~2024bb40")
            .setDebugMode(true) // only in development mode
            .setEventReportingStrategy(ReportingStrategy.FORCE_SYNC)
            .build()
        //Initialize WebEngage Personalization SDK
        WEPersonalization.get().init()

        //Initialize WebEngage SDK
        registerActivityLifecycleCallbacks(
            WebEngageActivityLifeCycleCallbacks(
                this,
                webEngageConfig
            )
        )

        //Register InApp callbacks
        WebEngage.registerInAppNotificationCallback(this)

        //Register Push Notification callbacks
        WebEngage.registerPushNotificationCallback(this)

        //Register Push Template Callback
        WebEngage.registerCustomPushRenderCallback(CustomCallbackAlarm())
        WebEngage.registerCustomPushRerenderCallback(CustomCallbackAlarm())

        //Register FCM token
        Utils().registerFCMToken()
    }


    //Push Callbacks
    override fun onPushNotificationReceived(
        context: Context?,
        pushNotificationData: PushNotificationData?
    ): PushNotificationData {
        Log.d(Constants.TAG, "onPushNotificationReceived ${pushNotificationData!!.experimentId}")
//        modify the data here if necessary and return the modified PushNotificationData object
        return pushNotificationData
    }

    override fun onPushNotificationShown(
        context: Context?,
        pushNotificationData: PushNotificationData?
    ) {
        Log.d(Constants.TAG, "onPushNotificationShown ${pushNotificationData!!.experimentId}")
    }

    override fun onPushNotificationClicked(
        context: Context?,
        pushNotificationData: PushNotificationData?
    ): Boolean {
        Log.d(Constants.TAG, "onPushNotificationClicked ${pushNotificationData!!.experimentId}")
//        return true if you are handling the clicks
//        return false to let WebEngage handle the clicks
        return false
    }

    override fun onPushNotificationDismissed(
        context: Context?,
        pushNotificationData: PushNotificationData?
    ) {
        Log.d(Constants.TAG, "onPushNotificationDismissed ${pushNotificationData!!.experimentId}")
    }

    override fun onPushNotificationActionClicked(
        context: Context,
        pushNotificationData: PushNotificationData?,
        actionID: String?
    ): Boolean {
//        return true if you are handling the clicks
//        return false to let WebEngage handle the clicks

        Log.d(
            Constants.TAG,
            "onPushNotificationActionClicked ${pushNotificationData!!.experimentId}"
        )

        if (pushNotificationData.getCallToActionById(actionID).action.equals(Constants.COPY_OTP)) {
            //Fetch otp from key value pair
            if (pushNotificationData.customData.containsKey(Constants.COPY_OTP)) {
                Utils().copyToClipBoard(
                    context,
                    pushNotificationData.customData.getString(Constants.COPY_OTP)!!
                )
                return false
            }
        }

        if (pushNotificationData.getCallToActionById(actionID).action.equals(Constants.TRIGGER_EVENT)) {
            //Fetch Event Name and Event Value Map from key value pair
            if (pushNotificationData.customData.containsKey(Constants.EVENT_NAME)) {
                if (pushNotificationData.customData.containsKey(Constants.EVENT_VALUE)) {
                    val customMap = com.webengage.sample.Utils.Utils.convertJsonStringToMap(
                        pushNotificationData.customData.getString(Constants.EVENT_VALUE)!!
                    )
                    Utils().triggerEvent(
                        context,
                        pushNotificationData.customData.getString(Constants.EVENT_NAME)!!,
                        customMap
                    )
                    return false
                } else {
                    Utils().triggerEvent(
                        context,
                        pushNotificationData.customData.getString(Constants.EVENT_NAME)!!,
                        null
                    )
                    return false
                }
            }
        }

        if (pushNotificationData.getCallToActionById(actionID).action.equals(Constants.UPDATE_USER_ATTRIBUTE)) {
            //Fetch Attribute name and Value from key value pair
            if (pushNotificationData.customData.containsKey(Constants.ATTRIBUTE_NAME)) {
                if (pushNotificationData.customData.containsKey(Constants.ATTRIBUTE_VALUE)) {
                    val attributeValue =
                        pushNotificationData.customData.getString(Constants.ATTRIBUTE_VALUE)
                    Utils().updateUserAttribute(
                        context,
                        pushNotificationData.customData.getString(Constants.ATTRIBUTE_NAME)!!,
                        attributeValue
                    )
                    return false
                }
            }
        }

        if (pushNotificationData.getCallToActionById(actionID).action.equals(Constants.SNOOZE) &&
            pushNotificationData.customData.containsKey(Constants.SNOOZE_TIME) &&
            pushNotificationData.customData.containsKey(Constants.WE_CUSTOM_RENDER) &&
            pushNotificationData.customData.getString(Constants.WE_CUSTOM_RENDER).equals("true", true) &&
            pushNotificationData.customData.containsKey(Constants.TEMPLATE_TYPE) &&
            pushNotificationData.customData.getString(Constants.TEMPLATE_TYPE).equals(Constants.SNOOZE_TEMPLATE, true)
        ) {
            val durationInMinutes =
                pushNotificationData.customData.getString(Constants.SNOOZE_TIME)!!.toLong()
            val durationInMillis = 1000 * 30L * durationInMinutes
            Utils().scheduleAlarm(context, durationInMillis, pushNotificationData, Constants.SNOOZE_TEMPLATE)
            val notificationManager: NotificationManager =
                context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(pushNotificationData.variationId.hashCode())
            return true
        }
        return false
    }


    //InApp Callbacks
    override fun onInAppNotificationPrepared(
        context: Context?,
        inAppNotificationData: InAppNotificationData?
    ): InAppNotificationData {
        Log.d(Constants.TAG, "onInAppNotificationPrepared ${inAppNotificationData!!.experimentId}")
        //modify the data here if necessary and return the modified InAppNotificationData object
        return inAppNotificationData
    }

    override fun onInAppNotificationShown(
        context: Context?,
        inAppNotificationData: InAppNotificationData?
    ) {
        Log.d(Constants.TAG, "onInAppNotificationShown ${inAppNotificationData!!.experimentId}")
    }

    override fun onInAppNotificationClicked(
        context: Context?,
        inAppNotificationData: InAppNotificationData?,
        experimentID: String?
    ): Boolean {
        Log.d(Constants.TAG, "onInAppNotificationClicked ${inAppNotificationData!!.experimentId}")
//        return true if you are handling the clicks
//        return false to let WebEngage handle the clicks
        return false
    }

    override fun onInAppNotificationDismissed(
        context: Context?,
        inAppNotificationData: InAppNotificationData?
    ) {
        Log.d(Constants.TAG, "onInAppNotificationDismissed ${inAppNotificationData!!.experimentId}")
    }

    //Security Callbacks
    override fun onSecurityException(exceptionMap: MutableMap<String, Any>?) {
//        Set new jwt token here
//        WebEngage.get().setSecurityToken(CUID, JWT_TOKEN)
    }
}

