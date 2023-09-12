package com.webengage.sample

import android.app.Application
import android.content.Context
import android.util.Log
import com.webengage.personalization.WEPersonalization
import com.webengage.sample.Utils.Constants
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


        //Register FCM token
        Utils().registerFCMToken()
    }


    //Push Callbacks
    override fun onPushNotificationReceived(
        p0: Context?,
        p1: PushNotificationData?
    ): PushNotificationData {
        Log.d(Constants.TAG, "onPushNotificationReceived ${p1!!.experimentId}")
//        modify the data here if necessary and return the modified PushNotificationData object
        return p1
    }

    override fun onPushNotificationShown(p0: Context?, p1: PushNotificationData?) {
        Log.d(Constants.TAG, "onPushNotificationShown ${p1!!.experimentId}")
    }

    override fun onPushNotificationClicked(p0: Context?, p1: PushNotificationData?): Boolean {
        Log.d(Constants.TAG, "onPushNotificationClicked ${p1!!.experimentId}")
//        return true if you are handling the clicks
//        return false to let WebEngage handle the clicks
        return false
    }

    override fun onPushNotificationDismissed(p0: Context?, p1: PushNotificationData?) {
        Log.d(Constants.TAG, "onPushNotificationDismissed ${p1!!.experimentId}")
    }

    override fun onPushNotificationActionClicked(
        p0: Context?,
        p1: PushNotificationData?,
        p2: String?
    ): Boolean {
//        return true if you are handling the clicks
//        return false to let WebEngage handle the clicks

        Log.d(Constants.TAG, "onPushNotificationActionClicked ${p1!!.experimentId}")

        if (p1.getCallToActionById(p2).action.equals(Constants.COPY_OTP)) {
            //Fetch otp from key value pair
            if (p1.customData.containsKey(Constants.COPY_OTP)) {
                Utils().copyToClipBoard(context, p1.customData.getString(Constants.COPY_OTP)!!)
                return false
            }
        }

        if (p1.getCallToActionById(p2).action.equals(Constants.TRIGGER_EVENT)) {
            //Fetch Event Name and Event Value Map from key value pair
            if (p1.customData.containsKey(Constants.EVENT_NAME)) {
                if (p1.customData.containsKey(Constants.EVENT_VALUE)) {
                    val customMap = com.webengage.sample.Utils.Utils.convertJsonStringToMap(
                        p1.customData.getString(Constants.EVENT_VALUE)!!
                    )
                    Utils().triggerEvent(
                        context,
                        p1.customData.getString(Constants.EVENT_NAME)!!,
                        customMap
                    )
                    return false
                } else {
                    Utils().triggerEvent(
                        context,
                        p1.customData.getString(Constants.EVENT_NAME)!!,
                        null
                    )
                    return false
                }
            }
        }

        if (p1.getCallToActionById(p2).action.equals(Constants.UPDATE_USER_ATTRIBUTE)) {
            //Fetch Attribute name and Value from key value pair
            if (p1.customData.containsKey(Constants.ATTRIBUTE_NAME)) {
                if (p1.customData.containsKey(Constants.ATTRIBUTE_VALUE)) {
                    val attributeValue = p1.customData.getString(Constants.ATTRIBUTE_VALUE)
                    Utils().updateUserAttribute(
                        context,
                        p1.customData.getString(Constants.ATTRIBUTE_NAME)!!,
                        attributeValue
                    )
                    return false
                }
            }
        }

        return false
    }


    //InApp Callbacks
    override fun onInAppNotificationPrepared(
        p0: Context?,
        p1: InAppNotificationData?
    ): InAppNotificationData {
        Log.d(Constants.TAG, "onInAppNotificationPrepared ${p1!!.experimentId}")
        //modify the data here if necessary and return the modified InAppNotificationData object
        return p1
    }

    override fun onInAppNotificationShown(p0: Context?, p1: InAppNotificationData?) {
        Log.d(Constants.TAG, "onInAppNotificationShown ${p1!!.experimentId}")
    }

    override fun onInAppNotificationClicked(
        p0: Context?,
        p1: InAppNotificationData?,
        p2: String?
    ): Boolean {
        Log.d(Constants.TAG, "onInAppNotificationClicked ${p1!!.experimentId}")
//        return true if you are handling the clicks
//        return false to let WebEngage handle the clicks
        return false
    }

    override fun onInAppNotificationDismissed(p0: Context?, p1: InAppNotificationData?) {
        Log.d(Constants.TAG, "onInAppNotificationDismissed ${p1!!.experimentId}")
    }

    //Security Callbacks
    override fun onSecurityException(p0: MutableMap<String, Any>?) {
//        Set new jwt token here
//        WebEngage.get().setSecurityToken(CUID, JWT_TOKEN)
    }
}

