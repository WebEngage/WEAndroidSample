package com.example.personalization_sample.model

import android.util.Log
import android.view.Display.Mode
import android.view.View
import android.widget.Button
import androidx.cardview.widget.CardView
import com.example.personalization_sample.Utils

class DataModel {
//    val listSize: int
    private var listSize: Int? = 0
    private var screenName: String = ""
    private var eventName: String = ""
    private var isRecyclerView: Boolean = false

    private var viewPosition: Int? = 0
    private var viewHeight: Int? = 0
    private var viewWidth: Int? = 0
    private var viewPropertyId: String = ""
//    model: Model
//    private var registryMap: HashMap<String, Model> = hashMapOf()
    val registryMap = ArrayList<Model>()
    var viewRegistry = ArrayList<ViewModel>()

    companion object {
        @Volatile
        private var instance: DataModel? = null

        fun getInstance(): DataModel {
            return instance ?: synchronized(this) {
                instance ?: DataModel().also { instance = it }
            }
        }
    }


//    init {
//        listSize = cardView ?: CardView(context)
//        cardView = mView as CardView
//        cardView!!.visibility = View.VISIBLE
//    }
    fun addData() {}
    fun saveData() {}

    fun getListSize(): Int? {
        return listSize
    }
    fun getScreenName(): String {
        return screenName
    }
    fun getEventName(): String {
        return eventName
    }
    fun getData(): ArrayList<Model> {
        return registryMap
    }
    fun setData(size: Int?, screen: String, event: String, idName: String, idValue: Int?, isChecked: Boolean) {
        listSize = size
        screenName = screen
        eventName = event
        isRecyclerView = isChecked
//        viewRegistry = viewRegistry
        val newViewRegistry = ArrayList<ViewModel>()

        val model = Model(listSize, screenName, eventName, idName, idValue, isRecyclerView, viewRegistry)
        registryMap.add(model)

        Utils.storeModelData(registryMap)
        Utils.getModelData()
        viewRegistry = newViewRegistry
    }
    fun removeScreenEntry(screenName: String) {
        var indexToRemove: Int = -1
//        removeEntry
        for ((index,entry) in registryMap.withIndex()) {
            if(entry.screenName.equals(screenName)) {
                Log.d("WebEngage-Inline-App", "Removing index from - "+index)
//                registryMap.removeAt(index)
                indexToRemove = index
            }
        }
        if (indexToRemove != -1) {
            registryMap.removeAt(indexToRemove)
            Utils.storeModelData(registryMap, )
        }

    }

    fun setViewData(position: Int?, isCustomView: Boolean, height: Int?, width: Int?, propertyId: String) {
        viewPosition = position
        viewHeight= height
        viewWidth = width
        viewPropertyId = propertyId
        val viewModel = ViewModel(viewPosition, isCustomView, viewHeight, viewWidth, viewPropertyId)
        viewRegistry.add(viewModel)
    }

    fun getViewData(): ArrayList<ViewModel> {
        return viewRegistry
    }

    fun updateData(modelList: ArrayList<Model>) {
        registryMap.clear()
        registryMap.addAll(modelList)
        Log.d("AKA", "Updated Data")
    }

    fun removeViewEntry(propertyId: String) {
        var indexToRemove: Int = -1
//        removeEntry
        for ((index,entry) in viewRegistry.withIndex()) {
            if(entry.propertyId.equals(propertyId)) {
                Log.d("WebEngage-Inline-App", "Removing index from - "+index)
//                registryMap.removeAt(index)
                indexToRemove = index
            }
        }
        if (indexToRemove != -1) {
            viewRegistry.removeAt(indexToRemove)
//            Utils.storeModelData(viewRegistry )
        }

    }

}