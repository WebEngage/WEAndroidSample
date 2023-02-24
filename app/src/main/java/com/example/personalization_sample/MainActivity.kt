package com.example.personalization_sample

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import com.example.personalization_sample.model.DataModel
import com.webengage.personalization.WEPersonalization
import com.webengage.sdk.android.WebEngage

class MainActivity : AppCompatActivity() {
    private lateinit var activityMain: RelativeLayout
    private lateinit var editText: EditText
    private lateinit var saveButton: Button
    private lateinit var logoutButton: Button
    private lateinit var customscreenButton: Button
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

        val arr = Utils.getModelData()
        DataModel.getInstance().updateData(arr)
        activityMain = findViewById(R.id.activity_main)
        editText = findViewById(R.id.editText)
        saveButton = findViewById(R.id.button)
        logoutButton = findViewById(R.id.logoutButton)
        customscreenButton = findViewById(R.id.customScreenButton);
        val strData = prefs.getString("registry", "")


        val cuid = prefs.getString("cuid", "")
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
        customscreenButton.setOnClickListener {
            val intent = Intent(this, CustomScreen::class.java)
            startActivity(intent)
        }

        // Handle the Logout button click
        logoutButton.setOnClickListener {
            prefs.put("cuid", "")
            weUser.logout()
            showLogin()
        }
    }
}