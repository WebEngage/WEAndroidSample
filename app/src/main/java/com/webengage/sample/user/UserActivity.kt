package com.webengage.sample.user

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import com.google.android.material.switchmaterial.SwitchMaterial
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

    lateinit var mPushOptInSwitch: SwitchMaterial
    lateinit var mInAppOptInSwitch: SwitchMaterial
    lateinit var mSMSOptInSwitch: SwitchMaterial
    lateinit var mEmailOptInSwitch: SwitchMaterial
    lateinit var mWhatsappOptInSwitch: SwitchMaterial


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        screenNavigate()
    }

    private fun screenNavigate(){
        WebEngage.get().analytics().screenNavigated("UserProfile")
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
                    val firstName = mFirstNameEditText.editText?.text.toString()
                    SharedPrefsManager.get().put(Constants.FIRST_NAME, firstName)
                    updateFirstName(firstName)
                }
            }

        mLastNameUpdateButton = findViewById<Button?>(R.id.lastNameUpdate)
            .also {
                it.setOnClickListener {
                    val lastName = mFirstNameEditText.editText?.text.toString()
                    SharedPrefsManager.get().put(Constants.LAST_NAME, lastName)
                    updateLastName(lastName)
                }
            }

        mEmailUpdateButton = findViewById<Button?>(R.id.emailUpdate)
            .also {
                it.setOnClickListener {
                    val email = mEmailEditText.editText?.text.toString()
                    SharedPrefsManager.get().put(Constants.EMAIL, email)
                    updateEmail(email)
                }
            }

        mPhoneUpdateButton = findViewById<Button?>(R.id.phoneUpdate)
            .also {
                it.setOnClickListener {
                    val phoneNumber = mPhoneEditText.editText?.text.toString()
                    SharedPrefsManager.get().put(Constants.PHONE, phoneNumber)
                    updatePhoneNumber(phoneNumber)
                }
            }

        mGenderUpdateButton = findViewById<Button?>(R.id.genderUpdate)
            .also {
                it.setOnClickListener {
                    val gender = mGenderEditText.editText?.text.toString()
                    if (!TextUtils.isEmpty(gender)) {
                        if (gender.equals(Gender.MALE.name, true)) {
                            SharedPrefsManager.get().put(Constants.GENDER, gender)
                            updateGender(Gender.MALE)
                        } else if (gender.equals(Gender.FEMALE.name, true)) {
                            SharedPrefsManager.get().put(Constants.GENDER, gender)
                            updateGender(Gender.FEMALE)
                        } else if (gender.equals(Gender.OTHER.name, true)) {
                            SharedPrefsManager.get().put(Constants.GENDER, gender)
                            updateGender(Gender.OTHER)
                        } else Log.e(Constants.TAG, "Invalid value passed for gender")
                    } else {
                        Log.e(Constants.TAG, "Enter gender")
                    }
                }
            }

        mBirthDateUpdateButton = findViewById<Button?>(R.id.birthDateUpdate)
            .also {
                it.setOnClickListener {
                    val date = mBirthDateEditText.editText?.text.toString()
                    if (Utils.isDateValid(date)) {
                        updateBirthDate(date)
                    } else
                        Log.e(Constants.TAG, getString(R.string.USER_ATTRIBUTE_DATE_ERROR))
                }
            }



        mPushOptInSwitch = findViewById<SwitchMaterial?>(R.id.pushOptInSwitch)
            .also {
                it.isChecked = SharedPrefsManager.get().getBoolean(Constants.PUSH_OPTIN, false)
                it.setOnCheckedChangeListener { buttonView, isChecked ->
                    SharedPrefsManager.get().put(Constants.PUSH_OPTIN, isChecked)
                    updatePushOptIn(isChecked)
                }
            }

        mInAppOptInSwitch = findViewById<SwitchMaterial?>(R.id.inappOptInSwitch)
            .also {
                it.isChecked = SharedPrefsManager.get().getBoolean(Constants.INAPP_OPTIN, false)
                it.setOnCheckedChangeListener { buttonView, isChecked ->
                    SharedPrefsManager.get().put(Constants.INAPP_OPTIN, isChecked)
                    updateInAppOptIn(isChecked)
                }
            }

        mSMSOptInSwitch = findViewById<SwitchMaterial?>(R.id.smsOptInSwitch)
            .also {
                it.isChecked = SharedPrefsManager.get().getBoolean(Constants.SMS_OPTIN, false)
                it.setOnCheckedChangeListener { buttonView, isChecked ->
                    SharedPrefsManager.get().put(Constants.SMS_OPTIN, isChecked)
                    updateSmsOptIn(isChecked)
                }
            }

        mEmailOptInSwitch = findViewById<SwitchMaterial?>(R.id.emailOptInSwitch)
            .also {
                it.isChecked = SharedPrefsManager.get().getBoolean(Constants.EMAIL_OPTIN, false)
                it.setOnCheckedChangeListener { buttonView, isChecked ->
                    SharedPrefsManager.get().put(Constants.EMAIL_OPTIN, isChecked)
                    updateEmailOptIn(isChecked)
                }
            }

        mWhatsappOptInSwitch = findViewById<SwitchMaterial?>(R.id.whatsappOptInSwitch)
            .also {
                it.isChecked = SharedPrefsManager.get().getBoolean(Constants.WHATSAPP_OPTIN, false)
                it.setOnCheckedChangeListener { buttonView, isChecked ->
                    SharedPrefsManager.get().put(Constants.WHATSAPP_OPTIN, isChecked)
                    updateWhatsappOptIn(isChecked)
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

    private fun updateFirstName(firstName: String) {
        WebEngage.get().user().setFirstName(firstName)
    }

    private fun updateLastName(lastName: String) {
        WebEngage.get().user().setLastName(lastName)
    }

    private fun updateEmail(email: String) {
        WebEngage.get().user().setEmail(email)
    }

    private fun updatePhoneNumber(phoneNumber: String) {
        WebEngage.get().user().setPhoneNumber(phoneNumber)
    }

    private fun updateGender(gender: Gender) {
        WebEngage.get().user().setGender(gender)
    }

    private fun updateBirthDate(date: String) {
        WebEngage.get().user().setBirthDate(date)
    }

    private fun updatePushOptIn(boolean: Boolean) {
        WebEngage.get().user().setOptIn(Channel.PUSH, boolean)
    }

    private fun updateInAppOptIn(boolean: Boolean) {
        WebEngage.get().user().setOptIn(Channel.IN_APP, boolean)
    }

    private fun updateSmsOptIn(boolean: Boolean) {
        WebEngage.get().user().setOptIn(Channel.SMS, boolean)
    }

    private fun updateEmailOptIn(boolean: Boolean) {
        WebEngage.get().user().setOptIn(Channel.EMAIL, boolean)
    }

    private fun updateWhatsappOptIn(boolean: Boolean) {
        WebEngage.get().user().setOptIn(Channel.WHATSAPP, boolean)
    }

}