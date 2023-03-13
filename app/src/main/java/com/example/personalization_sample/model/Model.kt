package com.example.personalization_sample.model

import android.view.View

data class Model (
    var listSize: Int? = 0,
    val screenName: String = "",
    val eventName: String = "",
    val idName: String = "",
    val idValue: Int? = null,
    val isRecyclerView: Boolean = false,
    val viewRegistry: ArrayList<ViewModel>
//    val viewRegistry: HashMap<String, ArrayList<ViewModel>>
//    kotlin.collections.hashMapOf<String, ArrayList<ViewModel>>()
) {
}

//data class WEGInline(
//    val id: Int,
//    val screenName: String,
//    val propertyID: String
//) {
//    var weCampaignData: WECampaignData? = null
//}