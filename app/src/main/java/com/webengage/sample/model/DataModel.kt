package com.webengage.sample.model

import com.webengage.sample.Utils.Constants
import com.webengage.sample.Utils.Utils
import com.webengage.sdk.android.Logger

class DataModel {
    private var listSize: Int? = 0
    private var screenName: String = ""
    private var eventName: String = ""
    private var isRecyclerView: Boolean = false

    private var viewPosition: Int? = 0
    private var viewHeight: Int? = 0
    private var viewWidth: Int? = 0
    private var viewPropertyId: String = ""
    private val registryMap = ArrayList<Model>()
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
    fun getData(): ArrayList<Model> {
        return registryMap
    }
    fun setData(size: Int?, screen: String, event: String, idName: String, idValue: Int?, isChecked: Boolean) {
        listSize = size
        screenName = screen
        eventName = event
        isRecyclerView = isChecked
        val newViewRegistry = ArrayList<ViewModel>()

        val model = Model(listSize, screenName, eventName, idName, idValue, isRecyclerView, viewRegistry)
        registryMap.add(model)

        Utils.storeModelData(registryMap)
        Utils.getModelData()
        viewRegistry = newViewRegistry
    }
    fun removeScreenEntry(screenName: String) {
        var indexToRemove: Int = -1
        //  removeEntry
        for ((index,entry) in registryMap.withIndex()) {
            if(entry.screenName.equals(screenName)) {
                Logger.d(Constants.TAG, "Removing index from - $index")
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
        Logger.d(Constants.TAG, "Updated Data")
    }

    fun removeViewEntry(propertyId: String) {
        var indexToRemove: Int = -1
        // removeEntry
        for ((index,entry) in viewRegistry.withIndex()) {
            if(entry.propertyId.equals(propertyId)) {
                Logger.d(Constants.TAG, "Removing index from - $index")
                indexToRemove = index
            }
        }
        if (indexToRemove != -1) {
            viewRegistry.removeAt(indexToRemove)
        }

    }

}