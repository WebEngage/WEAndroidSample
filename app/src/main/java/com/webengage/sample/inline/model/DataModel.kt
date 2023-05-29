package com.webengage.sample.inline.model

import com.webengage.sample.Utils.Constants
import com.webengage.sample.Utils.Utils
import com.webengage.sdk.android.Logger

// 2 Types of data is maintained
// first is View Data - which holds data related to position,height, width and screen property (can be multiple entries per screen)
// second is (generic) data - size, screen Name, event data and list of ViewData (Can be single entry for each screen)
class DataModel {
    private var listSize: Int? = 0
    private var screenName: String = ""
    private var eventName: String = ""
    private var isRecyclerView: Boolean = false

    private var viewPosition: Int? = 0
    private var viewHeight: Int? = 0
    private var viewWidth: Int? = 0
    private var viewPropertyId: String = ""
    private val registryMap = ArrayList<ScreenModel>()
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
    fun getScreenData(): ArrayList<ScreenModel> {
        return registryMap
    }

    // listSize, screenName, eventData, typeOf screen is identified here
    fun setScreenData(size: Int?, screen: String, event: String, idName: String, idValue: Int?, isChecked: Boolean) {
        listSize = size
        screenName = screen
        eventName = event
        isRecyclerView = isChecked
        val newViewRegistry = ArrayList<ViewModel>()

        val screenModel = ScreenModel(listSize, screenName, eventName, idName, idValue, isRecyclerView, viewRegistry)
        registryMap.add(screenModel)

        Utils.storeModelData(registryMap)
        Utils.getModelData()
        viewRegistry = newViewRegistry
    }

    // Delete screen from the list
    fun removeScreenEntry(screenName: String) {
        var indexToRemove: Int = -1
        //  removeEntry
        for ((index,entry) in registryMap.withIndex()) {
            if(entry.screenName == screenName) {
                Logger.d(Constants.TAG, "Removing index from - $index")
                indexToRemove = index
            }
        }
        if (indexToRemove != -1) {
            registryMap.removeAt(indexToRemove)
            Utils.storeModelData(registryMap, )
        }
    }

    // Holds data of the view(position,height, width and propertyId)
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

    // generic screen data
    fun updateData(screenModelList: ArrayList<ScreenModel>) {
        registryMap.clear()
        registryMap.addAll(screenModelList)
        Logger.d(Constants.TAG, "Updated Data")
    }

    // deletes single view entry(Property Data)
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