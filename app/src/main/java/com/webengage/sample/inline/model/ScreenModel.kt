package com.webengage.sample.inline.model


data class ScreenModel(
    var listSize: Int? = 0,
    val screenName: String = "",
    val eventName: String = "",
    val idName: String = "",
    val idValue: Int? = null,
    val isRecyclerView: Boolean = false,
    val viewRegistry: ArrayList<ViewModel>
)
