package com.webengage.sample.Utils

import com.webengage.sample.MainApplication
import com.webengage.sample.model.Model
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Utils {

    companion object {
        private val prefs: SharedPrefsManager = SharedPrefsManager.get()
        fun covertDpToPixel(dpValue: Int): Int {
            val scale = MainApplication.getContext().resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()

        }

        fun storeModelData(latestList: ArrayList<Model>) {
            val gson = Gson()
            val jsonString = gson.toJson(latestList)
            prefs.put("registry", jsonString)
        }

        fun getModelData(): ArrayList<Model> {
            val jsonString = prefs.getString("registry", null)
            val gson = Gson()
            var modelList: ArrayList<Model> = arrayListOf()
            if (!jsonString.isNullOrEmpty()) {
                modelList =
                    gson.fromJson(jsonString, object : TypeToken<ArrayList<Model>>() {}.type)
            }
            return modelList
        }

        fun convertModelToString(modelData: Model): String? {
            val gson = Gson()
            val jsonString = gson.toJson(modelData)
            return jsonString
        }

        fun convertStringToModel(jsonString: String): Model {
            val gson = Gson()
            var modelData: Model =
                Model(0, "Screen Name", "Event Name", "", null, false, ArrayList())
            if (!jsonString.isNullOrEmpty()) {
                modelData = gson.fromJson(jsonString, Model::class.java)
            }
            return modelData
        }

        fun validateUserName(username: String): Boolean {
            return !username.isEmpty()
        }


        fun checkIsUserLoggedIn(): Boolean {
            return SharedPrefsManager.get().getString(Constants.CUID, "").isNotEmpty()
        }
    }
}