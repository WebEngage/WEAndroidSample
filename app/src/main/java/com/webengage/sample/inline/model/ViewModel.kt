package com.webengage.sample.inline.model

data class ViewModel(
    val position: Int? = 0,
    val isCustomView: Boolean = false,
    val height: Int? = 0,
    val width: Int? = 0,
    val propertyId: String = "",
) {
}