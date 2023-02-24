package com.example.personalization_sample

import android.app.Application
import android.content.Context
import android.util.Log
import com.webengage.personalization.WEPersonalization
import com.webengage.sdk.android.WebEngageActivityLifeCycleCallbacks
import com.webengage.sdk.android.WebEngageConfig

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
        Log.d("WEH", "Main Application called");
        val webEngageConfig = WebEngageConfig.Builder()
            .setWebEngageKey("~311c5274")
            .setDebugMode(true) // only in development mode
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

