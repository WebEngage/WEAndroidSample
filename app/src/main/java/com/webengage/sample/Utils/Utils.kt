package com.webengage.sample.Utils

import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.webengage.sample.MainApplication
import com.webengage.sample.inline.ListScreenActivity
import com.webengage.sample.inline.model.ScreenModel
import com.webengage.sample.user.UserActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date


class Utils {

    companion object {
        private val prefs: SharedPrefsManager = SharedPrefsManager.get()
        fun covertDpToPixel(dpValue: Int): Int {
            val scale = MainApplication.getContext().resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()

        }

        fun storeModelData(latestList: ArrayList<ScreenModel>) {
            val gson = Gson()
            val jsonString = gson.toJson(latestList)
            prefs.put("registry", jsonString)
        }

        fun getModelData(): ArrayList<ScreenModel> {
            val jsonString = prefs.getString("registry", null)
            val gson = Gson()
            var screenModelList: ArrayList<ScreenModel> = arrayListOf()
            if (!jsonString.isNullOrEmpty()) {
                screenModelList =
                    gson.fromJson(jsonString, object : TypeToken<ArrayList<ScreenModel>>() {}.type)
            }
            return screenModelList
        }

        fun convertModelToString(screenModelData: ScreenModel): String? {
            val gson = Gson()
            return gson.toJson(screenModelData)
        }

        fun convertStringToModel(jsonString: String): ScreenModel {
            val gson = Gson()
            var screenModelData: ScreenModel =
                ScreenModel(0, "Screen Name", "Event Name", "", null, false, ArrayList())
            if (!jsonString.isNullOrEmpty()) {
                screenModelData = gson.fromJson(jsonString, ScreenModel::class.java)
            }
            return screenModelData
        }

        fun validateUserName(username: String): Boolean {
            return username.isNotEmpty()
        }

        fun validateJWT(jwt: String): Boolean{
            return jwt.isNotEmpty()
        }
        fun checkIsUserLoggedIn(): Boolean {
            return SharedPrefsManager.get().getString(Constants.CUID, "").isNotEmpty()
        }

        fun isDateValid(date: String): Boolean {

            val sdf = SimpleDateFormat("yyyy-MM-dd")
            sdf.isLenient = false
            try {
                val javaDate: Date = sdf.parse(date)
            } catch (e: ParseException) {
                return false
            }
            return true
        }

        fun getUserActivityIntent(context: Context):Intent{
            return Intent(context, UserActivity::class.java)
        }

        fun getInlineActivityIntent(context: Context): Intent{
            return Intent(context, ListScreenActivity::class.java)
        }
    }
}