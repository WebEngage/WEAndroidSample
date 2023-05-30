package com.webengage.sample.push

import android.text.TextUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.webengage.sdk.android.WebEngage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data

        //Send FCM payload to WebEngage SDK
        if (data.containsKey("source") && "webengage" == data["source"]) {
            WebEngage.get().receive(data)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        //Send FCM Token to webengage SDK
        if (!TextUtils.isEmpty(token))
            WebEngage.get().setRegistrationID(token)
    }


}