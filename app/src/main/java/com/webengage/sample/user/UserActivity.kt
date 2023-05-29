package com.webengage.sample.user

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import com.google.android.material.textfield.TextInputLayout
import com.webengage.sample.R
import com.webengage.sample.Utils.Constants
import com.webengage.sample.Utils.SharedPrefsManager
import com.webengage.sample.Utils.Utils
import com.webengage.sdk.android.Channel
import com.webengage.sdk.android.WebEngage
import com.webengage.sdk.android.utils.Gender

class UserActivity : Activity() {

    lateinit var mFirstNameEditText: TextInputLayout
    lateinit var mLastNameEditText: TextInputLayout
    lateinit var mEmailEditText: TextInputLayout
    lateinit var mPhoneEditText: TextInputLayout
    lateinit var mGenderEditText: TextInputLayout
    lateinit var mBirthDateEditText: TextInputLayout

    lateinit var mFirstNameUpdateButton: Button
    lateinit var mLastNameUpdateButton: Button
    lateinit var mEmailUpdateButton: Button
    lateinit var mPhoneUpdateButton: Button
    lateinit var mGenderUpdateButton: Button
    lateinit var mBirthDateUpdateButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        initViews()
    }

    fun initViews() {

        mFirstNameEditText = findViewById(R.id.firstNameEditTextInputLayout)
        mLastNameEditText = findViewById(R.id.lastNameEditTextInputLayout)
        mEmailEditText = findViewById(R.id.emailEditTextInputLayout)
        mPhoneEditText = findViewById(R.id.phoneEditTextInputLayout)
        mGenderEditText = findViewById(R.id.genderEditTextInputLayout)
        mBirthDateEditText = findViewById(R.id.birthDateEditTextInputLayout)

        mFirstNameUpdateButton = findViewById<Button?>(R.id.firstNameUpdate)
            .also {
                it.setOnClickListener {
                    updateFirstName(mFirstNameEditText.editText?.text.toString())
                }
            }

        mLastNameUpdateButton = findViewById<Button?>(R.id.lastNameUpdate)
            .also {
                it.setOnClickListener {
                    updateLastName(mLastNameEditText.editText?.text.toString())
                }
            }

        mEmailUpdateButton = findViewById<Button?>(R.id.emailUpdate)
            .also {
                it.setOnClickListener {
                    updateEmail(mEmailEditText.editText?.text.toString())
                }
            }

        mPhoneUpdateButton = findViewById<Button?>(R.id.phoneUpdate)
            .also {
                it.setOnClickListener {
                    updatePhoneNumber(mPhoneEditText.editText?.text.toString())
                }
            }

        mGenderUpdateButton = findViewById<Button?>(R.id.genderUpdate)
            .also {
                it.setOnClickListener {
                    val gender = mGenderEditText.editText?.text.toString()
                    if (!TextUtils.isEmpty(gender)) {
                        if (gender.equals(Gender.MALE.name, true))
                            updateGender(Gender.MALE)
                        else if (gender.equals(Gender.FEMALE.name, true))
                            updateGender(Gender.FEMALE)
                        else if (gender.equals(Gender.OTHER.name, true))
                            updateGender(Gender.OTHER)
                        else Log.e(Constants.TAG, "Invalid value passed for gender")
                    } else {
                        Log.e(Constants.TAG, "Enter gender")
                    }
                }
            }

        mBirthDateUpdateButton = findViewById<Button?>(R.id.birthDateUpdate)
            .also {
                it.setOnClickListener {
                    updateBirthDate(mBirthDateEditText.editText?.text.toString())
                }
            }


        mFirstNameEditText.editText?.setText(
            SharedPrefsManager.get().getString(Constants.FIRST_NAME, "")
        )
        mLastNameEditText.editText?.setText(
            SharedPrefsManager.get().getString(Constants.LAST_NAME, "")
        )
        mEmailEditText.editText?.setText(SharedPrefsManager.get().getString(Constants.EMAIL, ""))
        mPhoneEditText.editText?.setText(SharedPrefsManager.get().getString(Constants.PHONE, ""))
        mGenderEditText.editText?.setText(SharedPrefsManager.get().getString(Constants.GENDER, ""))
        mBirthDateEditText.editText?.setText(
            SharedPrefsManager.get().getString(Constants.BIRTHDATE, "")
        )

    }

    fun updateFirstName(firstName: String) {
        SharedPrefsManager.get().put(Constants.FIRST_NAME, firstName)
        WebEngage.get().user().setFirstName(firstName)
    }

    fun updateLastName(lastName: String) {
        SharedPrefsManager.get().put(Constants.LAST_NAME, lastName)
        WebEngage.get().user().setLastName(lastName)
    }

    fun updateEmail(email: String) {
        SharedPrefsManager.get().put(Constants.EMAIL, email)
        WebEngage.get().user().setEmail(email)
    }

    fun updatePhoneNumber(phoneNumber: String) {
        SharedPrefsManager.get().put(Constants.PHONE, phoneNumber)
        WebEngage.get().user().setPhoneNumber(phoneNumber)
    }

    fun updateGender(gender: Gender) {
        SharedPrefsManager.get().put(Constants.GENDER, gender.name)
        WebEngage.get().user().setGender(gender)
    }

    fun updateBirthDate(date: String) {
        if (Utils.isDateValid(date)) {
            SharedPrefsManager.get().put(Constants.BIRTHDATE,date)
            WebEngage.get().user().setBirthDate(date)
        } else
            Log.e(Constants.TAG, getString(R.string.USER_ATTRIBUTE_DATE_ERROR))
    }

    fun updatePushOptIn(boolean: Boolean) {
        WebEngage.get().user().setOptIn(Channel.PUSH, boolean)
    }

    fun updateInAppOptIn(boolean: Boolean) {
        WebEngage.get().user().setOptIn(Channel.IN_APP, boolean)
    }

    fun updateSmsOptIn(boolean: Boolean) {
        WebEngage.get().user().setOptIn(Channel.SMS, boolean)
    }

    fun updateEmailOptIn(boolean: Boolean) {
        WebEngage.get().user().setOptIn(Channel.EMAIL, boolean)
    }

    fun updateWhatsappOptIn(boolean: Boolean) {
        WebEngage.get().user().setOptIn(Channel.WHATSAPP, boolean)
    }

}