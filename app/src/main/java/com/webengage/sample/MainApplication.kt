package com.webengage.sample

import android.app.Application
import android.content.Context
import com.google.firebase.messaging.FirebaseMessaging
import com.webengage.personalization.WEPersonalization
import com.webengage.sdk.android.WebEngage
import com.webengage.sdk.android.WebEngageActivityLifeCycleCallbacks
import com.webengage.sdk.android.WebEngageConfig
import com.webengage.sdk.android.actions.database.ReportingStrategy


class MainApplication: Application() {
    companion object {
        private lateinit var context: Context

        fun getContext(): Context {
            return context
        }
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        val webEngageConfig = WebEngageConfig.Builder()
            .setWebEngageKey("aa131d2c")// 311c5274 //47b66161
            .setDebugMode(true) // only in development mode
            .setEventReportingStrategy(ReportingStrategy.FORCE_SYNC)
            .build()
        WEPersonalization.get().init()
        registerActivityLifecycleCallbacks(
            WebEngageActivityLifeCycleCallbacks(
                this,
                webEngageConfig
            )
        )
    }
}

