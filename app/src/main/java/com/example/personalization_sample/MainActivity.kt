package com.example.personalization_sample

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import com.webengage.sdk.android.WebEngage

class MainActivity : AppCompatActivity() {
    private lateinit var activityMain: RelativeLayout
    private lateinit var editText: EditText
    private lateinit var saveButton: Button
    private lateinit var logoutButton: Button
    val weUser = WebEngage.get().user()
    val prefs: SharedPrefsManager = SharedPrefsManager.get()


    fun showLogin() {
        editText.visibility = EditText.VISIBLE
        saveButton.visibility = Button.VISIBLE
        logoutButton.visibility = Button.GONE
    }

    fun showLogoutButton() {
        editText.visibility = EditText.GONE
        saveButton.visibility = Button.GONE
        logoutButton.visibility = Button.VISIBLE
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        activityMain = findViewById(R.id.activity_main)
        editText = findViewById(R.id.editText)
        saveButton = findViewById(R.id.button)
        logoutButton = findViewById(R.id.logoutButton)
//        prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)


        // Get the CUID value from SharedPreferences
        val cuid = prefs.getString("cuid", "")

        // Check if CUID is available in SharedPreferences
        if (cuid.isNullOrEmpty()) {
            showLogin();

            saveButton.setOnClickListener {
                val text = editText.text.toString()
                prefs.put("cuid", text)
                showLogoutButton()
                weUser.login(text)
            }
        } else {
            showLogoutButton()
        }

        // Handle the Logout button click
        logoutButton.setOnClickListener {
            prefs.put("cuid", "")
            weUser.logout()
            showLogin()
        }
    }
}