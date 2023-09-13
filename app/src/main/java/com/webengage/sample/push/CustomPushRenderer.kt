package com.webengage.sample.push

import android.app.Notification
import android.app.Notification.Builder
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.webengage.sample.Utils.Constants
import com.webengage.sdk.android.PendingIntentFactory
import com.webengage.sdk.android.WebEngage
import com.webengage.sdk.android.actions.render.CallToAction
import com.webengage.sdk.android.actions.render.PushNotificationData
import com.webengage.sdk.android.utils.WebEngageConstant
import com.webengage.sdk.android.utils.htmlspanner.WEHtmlParserInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomPushRenderer {
    fun renderPushNotification(context: Context, pushNotificationData: PushNotificationData) {
        if (pushNotificationData.style.equals(WebEngageConstant.STYLE.BIG_TEXT))
            constructTextPushNotification(context, pushNotificationData)
        else if (pushNotificationData.style.equals(WebEngageConstant.STYLE.BIG_TEXT)) {
            constructBannerPushNotification(context, pushNotificationData)
        }

    }

    fun show(
        context: Context,
        builder: Notification.Builder,
        pushNotificationData: PushNotificationData
    ) {

        val notificationManager: NotificationManager =
            context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = builder.build()
        if (pushNotificationData.accentColor != -1) {
            notification.color = pushNotificationData.accentColor
        }
        notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        notification.flags = notification.flags or Notification.FLAG_ONLY_ALERT_ONCE

        val hashedNotificationID = pushNotificationData.variationId.hashCode()
        try {
            notificationManager.notify(hashedNotificationID, notification)
        } catch (e: SecurityException) {
            notification.defaults = Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND
            notificationManager.notify(hashedNotificationID, notification)
        }

        Log.d(Constants.TAG, "Rendering notification")
    }

    fun setPushData(
        context: Context,
        builder: Builder,
        pushNotificationData: PushNotificationData
    ): Notification.Builder {
        builder.setContentTitle(WEHtmlParserInterface().fromHtml(pushNotificationData.title))
        builder.setContentText(WEHtmlParserInterface().fromHtml(pushNotificationData.contentText))

        if (!TextUtils.isEmpty(pushNotificationData.contentSummary))
            builder.setSubText(pushNotificationData.contentSummary)

        builder.setWhen(System.currentTimeMillis())
        builder.setShowWhen(true)
        builder.setAutoCancel(true)
        builder.setSmallIcon(pushNotificationData.smallIcon)
        builder.setLargeIcon(pushNotificationData.largeIcon)

        if (pushNotificationData.isSticky)
            builder.setOngoing(true)

        builder.setContentIntent(PendingIntentFactory.constructPushClickPendingIntent(context,pushNotificationData, pushNotificationData.primeCallToAction, true))
        builder.setDeleteIntent(PendingIntentFactory.constructPushDeletePendingIntent(context,pushNotificationData))

        if (pushNotificationData.callToActions.size > 0) {
            for (iterator in 0 until pushNotificationData.callToActions.size)
                if (!pushNotificationData.callToActions[iterator].isPrimeAction) {
                    val autoCancel =
                        pushNotificationData.callToActions[iterator].action.contains("we-snooze")

                    builder.addAction(
                        getClickAction(
                            context,
                            pushNotificationData,
                            pushNotificationData.callToActions[iterator],
                            !autoCancel
                        )
                    )
                }
        }
        if (pushNotificationData.accentColor != -1) {
            builder.setColor(pushNotificationData.accentColor)
        }

        return builder
    }

    fun getClickAction(
        context: Context,
        pushNotificationData: PushNotificationData,
        callToAction: CallToAction,
        autoCancel: Boolean
    ): Notification.Action {
        val pendingIntent =
            getClickPendingIntent(context, pushNotificationData, callToAction, autoCancel)
        val action = Notification.Action.Builder(
            null,
            WEHtmlParserInterface().fromHtml(callToAction.text),
            pendingIntent
        )

        return action.build()
    }

    fun getClickPendingIntent(
        context: Context,
        pushNotificationData: PushNotificationData,
        callToAction: CallToAction,
        autoCancel: Boolean
    ): PendingIntent {
        return PendingIntentFactory.constructPushClickPendingIntent(
            context,
            pushNotificationData,
            callToAction,
            autoCancel
        )
    }

    fun constructTextPushNotification(
        context: Context,
        pushNotificationData: PushNotificationData
    ) {
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        var channelId =
            WebEngage.get().webEngageConfig.defaultPushChannelConfiguration.notificationChannelId
        if (notificationManagerCompat.getNotificationChannel(pushNotificationData.channelId) != null)
            channelId = pushNotificationData.channelId

        var builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, channelId)
        } else {
            Notification.Builder(context)
        }

        setPushData(context, builder, pushNotificationData)

        val bigTextStyle = Notification.BigTextStyle()
        bigTextStyle.setBigContentTitle(WEHtmlParserInterface().fromHtml(pushNotificationData.title))
        bigTextStyle.bigText(WEHtmlParserInterface().fromHtml(pushNotificationData.contentText))
        if (!TextUtils.isEmpty(pushNotificationData.contentSummary))
            bigTextStyle.setSummaryText(WEHtmlParserInterface().fromHtml(pushNotificationData.contentText))

        show(context, builder, pushNotificationData)
    }

    fun constructBannerPushNotification(
        context: Context,
        pushNotificationData: PushNotificationData
    ) {
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        var channelId =
            WebEngage.get().webEngageConfig.defaultPushChannelConfiguration.notificationChannelId
        if (notificationManagerCompat.getNotificationChannel(pushNotificationData.channelId) != null)
            channelId = pushNotificationData.channelId

        var builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, channelId)
        } else {
            Notification.Builder(context)
        }

        setPushData(context, builder, pushNotificationData)

        val bigPictureStyle = Notification.BigPictureStyle()
        bigPictureStyle.setBigContentTitle(WEHtmlParserInterface().fromHtml(pushNotificationData.title))
        bigPictureStyle.setSummaryText(WEHtmlParserInterface().fromHtml(pushNotificationData.contentText))
        if (!TextUtils.isEmpty(pushNotificationData.contentSummary))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                bigPictureStyle.setContentDescription(
                    WEHtmlParserInterface().fromHtml(
                        pushNotificationData.contentText
                    )
                )
            }

        try {
            CoroutineScope(Dispatchers.Default).launch {
                val image = downloadImage(pushNotificationData.bigPictureStyleData.bigPictureUrl)
                bigPictureStyle.bigPicture(image)
            }
        } catch (e: Exception) {
            Log.e(Constants.TAG, "Error while setting image to notification")
        }

        show(context, builder, pushNotificationData)

    }

    private suspend fun downloadImage(url: String): Bitmap? {
        return ImageUtils().getBitmapFromURL(url)
    }


    fun getExpandedRemoteView(context: Context, pushNotificationData: PushNotificationData) {

    }

    fun getCollapsedRemoteView(context: Context, pushNotificationData: PushNotificationData) {

    }
}