package com.webengage.sample

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.webengage.sample.Utils.Constants
import com.webengage.sample.Utils.SharedPrefsManager
import com.webengage.sample.Utils.Utils
import com.webengage.sample.push.Utils.Companion.PUSH_NOTIFICATIONS
import com.webengage.sdk.android.WebEngage


class MainActivity : AppCompatActivity() {
    private lateinit var mInlineButton: Button
    private lateinit var mUserProfileButton: Button
    private lateinit var mLoginMenuItem: MenuItem
    private lateinit var mLogoutMenuItem: MenuItem
    private lateinit var mUsernameTextView: TextView
    val prefs: SharedPrefsManager = SharedPrefsManager.get()
    var cuid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initValues()
        updateUserText()
    }

    override fun onStart() {
        super.onStart()
        requestPermission()
    }

    fun requestPermission() {
        //For App's targeting below 33
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= 33) {
                Log.d(
                    Constants.TAG,
                    "onResume: checking for PUSH_NOTIFICATIONS: " + (checkSelfPermission(
                        PUSH_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED)
                )
                if (checkSelfPermission(PUSH_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                        arrayOf<String>(PUSH_NOTIFICATIONS),
                        102
                    )
                    com.webengage.sample.push.Utils().setDevicePushOptIn(false)
                } else {
                    com.webengage.sample.push.Utils().setDevicePushOptIn(true)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(
            Constants.TAG,
            "onRequestPermissionsResult permissions: $permissions grantResults: $grantResults"
        )
        if (checkSelfPermission(PUSH_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            com.webengage.sample.push.Utils().setDevicePushOptIn(true)
        }
    }

    override fun onResume() {
        super.onResume()
        screenNavigate()
    }

    private fun screenNavigate() {
        WebEngage.get().analytics().screenNavigated("Home")
    }

    private fun initValues() {
        cuid = prefs.getString(Constants.CUID, "")
    }

    private fun initViews() {
        // UserGreeting TextView
        mUsernameTextView = findViewById(R.id.usernameTextView)

        // Inline
        mInlineButton = findViewById(R.id.personalizationButton)
        mInlineButton.setOnClickListener {
            startActivity(Utils.getInlineActivityIntent(this))
        }


        mUserProfileButton = findViewById(R.id.profile_button)
        mUserProfileButton.setOnClickListener {
            startActivity(Utils.getUserActivityIntent(this))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.main, menu);
        mLoginMenuItem = menu.findItem(R.id.menu_login);
        mLogoutMenuItem = menu.findItem(R.id.menu_logout);
        if (Utils.checkIsUserLoggedIn()) {
            mLoginMenuItem.isVisible = false
        } else {
            mLogoutMenuItem.isVisible = false
        }
        return super.onCreateOptionsMenu(menu);
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.CUID)) {
            cuid = savedInstanceState.getString(Constants.CUID)!!
            updateUserText()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                //showLogoutDialog();
                logout()
                true
            }

            R.id.menu_login -> {
                showLoginDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoginDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_login, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        builder.setCancelable(true)
            .setPositiveButton(
                "LOGIN"
            ) { dialog, id ->
                // Do nothing here
            }
            .setNegativeButton(
                "CANCEL"
            ) { dialog, id ->
                // Do nothing here
            }
        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.getButton(-1).setOnClickListener(View.OnClickListener {
            val jwt =
                (dialogView.findViewById<View>(R.id.jwtTokenEditText) as EditText).text.toString()
                    .trim()
            val username =
                (dialogView.findViewById<View>(R.id.usernameEditText) as EditText).text.toString()
                    .trim { it <= ' ' }
            if (Utils.validateUserName(username)) {
                if (Utils.validateJWT(jwt))
                    login(username, jwt)
                else
                    login(username)
                alertDialog.dismiss()
                return@OnClickListener
            }
        })
    }


    private fun login(username: String) {
        prefs.put(Constants.CUID, username)
        cuid = username
        mLoginMenuItem.isVisible = false
        mLogoutMenuItem.isVisible = true
        updateUserText()
        loginToWebEngage(username)
    }

    private fun loginToWebEngage(username: String) {
        WebEngage.get().user().login(username)
    }

    private fun loginToWebEngage(username: String, jwt: String) {
        WebEngage.get().user().login(username, jwt)
    }

    private fun login(username: String, jwt: String) {
        prefs.put(Constants.CUID, username)
        prefs.put(Constants.JWT, jwt)
        cuid = username
        mLoginMenuItem.isVisible = false
        mLogoutMenuItem.isVisible = true
        updateUserText()
        loginToWebEngage(username, jwt)
    }

    private fun logoutFromWebEngage() {
        WebEngage.get().user().logout()
    }

    private fun logout() {
        prefs.put(
            Constants.PREV_LOGIN,
            prefs.getString(Constants.CUID, "")
        )
        prefs.remove(Constants.CUID)
        prefs.remove(
            Constants.FIRST_NAME,
            Constants.LAST_NAME,
            Constants.EMAIL,
            Constants.HASHED_EMAIL,
            Constants.PHONE,
            Constants.HASHED_PHONE,
            Constants.COMPANY,
            Constants.GENDER,
            Constants.BIRTHDATE,
            Constants.PUSH_OPTIN,
            Constants.INAPP_OPTIN,
            Constants.SMS_OPTIN,
            Constants.EMAIL_OPTIN,
            Constants.WHATSAPP_OPTIN
        )
        mLoginMenuItem.isVisible = true
        mLogoutMenuItem.isVisible = false
        updateUserText()
        logoutFromWebEngage()
    }


    private fun updateUserText() {
        if (Utils.checkIsUserLoggedIn()) {
            mUsernameTextView.text = "Hi ${this.cuid} !"
        } else {
            mUsernameTextView.text = "Hi"
        }
    }
}