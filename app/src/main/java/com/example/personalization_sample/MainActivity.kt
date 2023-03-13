package com.example.personalization_sample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.personalization_sample.model.DataModel
import com.webengage.sdk.android.WebEngage


class MainActivity : AppCompatActivity() {
    private lateinit var activityMain: RelativeLayout
    private lateinit var editText: EditText
    private lateinit var saveButton: Button
    private lateinit var logoutButton: Button
    private lateinit var customscreenButton: Button
    val weUser = WebEngage.get().user()
    var weAnalytics = WebEngage.get().analytics()

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
            weAnalytics.track("testEvent")
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

    override fun onStart() {
        super.onStart()
        WebEngage.get().analytics().screenNavigated("main-screen")

    }
}