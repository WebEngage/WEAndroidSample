package com.example.personalization_sample

import com.example.personalization_sample.model.Model
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Utils {

    companion object {
        val prefs: SharedPrefsManager = SharedPrefsManager.get()

        fun covertDpToPixel(dpValue: Float, scale: Int): Int {
            return (dpValue * scale + 0.5f).toInt()

        }
        fun storeModelData(latestList: ArrayList<Model>) {
            val gson = Gson()
            val jsonString = gson.toJson(latestList)
            prefs.put("registry", jsonString)
        }

        fun getModelData(): ArrayList<Model> {
            val jsonString = prefs.getString("registry", null)

// Convert the JSON string back to a Model object
            val gson = Gson()
            var modelList: ArrayList<Model> = arrayListOf()
//            val modelList: ArrayList<Model> = arrayListOf()

//            val model = gson.fromJson(jsonString, Model::class.java)
            if(!jsonString.isNullOrEmpty()) {
                modelList =
                    gson.fromJson(jsonString, object : TypeToken<ArrayList<Model>>() {}.type)
            }
//            Log.d("AKA","model - "+modelList[0].screenName)
//            return model;
            return modelList
        }

        fun convertModelToString(modelData: Model): String? {
            val gson = Gson()
            val jsonString = gson.toJson(modelData)
            return jsonString
        }

        fun convertStringToModel(jsonString: String): Model {
            val gson = Gson()
//            var modelList: ArrayList<Model> = arrayListOf()
//            val modelList: ArrayList<Model> = arrayListOf()
            var modelData: Model = Model(0, "Screen Name", "Event Name", false, ArrayList())

//            val model = gson.fromJson(jsonString, Model::class.java)
            if(!jsonString.isNullOrEmpty()) {
                modelData = gson.fromJson(jsonString, Model::class.java)

//                gson.fromJson(jsonString, object : TypeToken<ArrayList<Model>>() {}.type)
            }
            return modelData
        }
    }


}