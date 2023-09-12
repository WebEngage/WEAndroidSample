package com.webengage.sample.Utils

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.webengage.sample.MainApplication
import com.webengage.sample.event.EventActivity
import com.webengage.sample.inline.ListScreenActivity
import com.webengage.sample.inline.model.ScreenModel
import com.webengage.sample.user.UserActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.Locale
import java.util.TimeZone


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

        fun validateJWT(jwt: String): Boolean {
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

        fun getUserActivityIntent(context: Context): Intent {
            return Intent(context, UserActivity::class.java)
        }

        fun getInlineActivityIntent(context: Context): Intent {
            return Intent(context, ListScreenActivity::class.java)
        }

        fun getEventActivityIntent(context: Context): Intent {
            return Intent(context, EventActivity::class.java)
        }

        fun convertJsonStringToMap(jsonString: String): Map<String, Any?>? {
            var map: Map<String, Any?>? = null
            try {
                val json = convertStringToJson(jsonString)
                if (json != null)
                    map = toMap(json)
            } catch (e: Exception) {
                Log.e(Constants.TAG, "Exception while converting json to map. Returning NULL")
            }
            return map
        }

        fun convertStringToJson(jsonString: String): JSONObject? {
            var jsonObject: JSONObject? = null
            try {
                jsonObject = JSONObject(jsonString)
            } catch (e: JSONException) {
                Log.e(Constants.TAG, "Error while converting string to Json. Returning NULL")
            }
            return jsonObject
        }

        fun convertJsonToMap(jsonObject: JSONObject): Map<String, Any?>? {
            var map: Map<String, Any?>? = null
            try {
                map = toMap(jsonObject)
            } catch (e: Exception) {
                Log.e(Constants.TAG, "Exception while converting json to map. Returning NULL")
            }
            return map
        }


        @Throws(JSONException::class)
        fun fromJSON(obj: Any?): Any? {
            if (obj == null || obj === JSONObject.NULL) {
                return null
            } else if (obj is JSONObject) {
                return toMap(obj)
            } else if (obj is JSONArray) {
                return toList(obj)
            } else if (obj is String) {
                if (obj.length == "yyyy-MM-ddTHH:mm:ss.SSSZ".length) {
                    return try {
                        val simpleDateFormat =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
                        simpleDateFormat.parse(obj)
                    } catch (e: Exception) {
                        obj
                    }
                }
            }
            return obj
        }

        @Throws(JSONException::class)
        fun toMap(json: JSONObject): Map<String, Any?> {
            val map: MutableMap<String, Any?> = HashMap()
            val iterator = json.keys()
            while (iterator.hasNext()) {
                val key = iterator.next()
                val value = fromJSON(json[key])
                map[key] = value
            }
            return map
        }

        @Throws(JSONException::class)
        fun toList(jsonArray: JSONArray): List<Any?> {
            val list: MutableList<Any?> = java.util.ArrayList()
            for (i in 0 until jsonArray.length()) {
                val value = fromJSON(jsonArray[i])
                list.add(value)
            }
            return list
        }

    }
}