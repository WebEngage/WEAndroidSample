package com.webengage.sample

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.webengage.sample.R
import com.webengage.sample.inline.ListScreenActivity
import com.webengage.sample.Utils.Constants
import com.webengage.sample.Utils.SharedPrefsManager
import com.webengage.sample.Utils.Utils
import com.webengage.sdk.android.WebEngage


class MainActivity : AppCompatActivity() {
    private lateinit var mInlineButton: Button
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

    private fun initValues() {
        cuid = prefs.getString(Constants.CUID, "")
    }

    private fun initViews() {
        // UserGreeting TextView
        mUsernameTextView = findViewById(R.id.usernameTextView)
        // Inline
        mInlineButton = findViewById(R.id.personalizationButton)
        mInlineButton.setOnClickListener {
            val intent = Intent(this, ListScreenActivity::class.java)
            startActivity(intent)
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
                val passwordEditText =
                    dialogView.findViewById<View>(R.id.passwordEditText) as EditText
                val username =
                    (dialogView.findViewById<View>(R.id.usernameEditText) as EditText).text.toString()
                        .trim { it <= ' ' }
                if (Utils.validateUserName(username)) {
                    login(username)
                    alertDialog.dismiss()
                    return@OnClickListener
                }
            })
        }


    private fun login(username: String) {
        WebEngage.get().user().login(username)
        prefs.put(Constants.CUID, username)
        cuid = username
        mLoginMenuItem.isVisible = false
        mLogoutMenuItem.isVisible = true
        updateUserText()
    }

    private fun logout() {
        WebEngage.get().user().logout()
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
            Constants.EMAIL_OPTIN
        )
        mLoginMenuItem.isVisible = true
        mLogoutMenuItem.isVisible = false
        updateUserText()
    }


    private fun updateUserText() {
        if (Utils.checkIsUserLoggedIn()) {
            mUsernameTextView.setText("Hi!! " + this.cuid)
        } else {
            mUsernameTextView.setText("Hiiii")
        }
    }
}