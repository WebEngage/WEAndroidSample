package com.webengage.sample.inline

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.webengage.sample.R
import com.webengage.sample.Utils.Constants
import com.webengage.sample.Utils.Utils
import com.webengage.sample.inline.model.ScreenModel
import com.webengage.sample.inline.model.ViewModel
import com.webengage.personalization.WEInlineView
import com.webengage.personalization.WEPersonalization
import com.webengage.personalization.callbacks.WEPlaceholderCallback
import com.webengage.personalization.data.WECampaignData
import com.webengage.sdk.android.Logger
import java.lang.Exception

class ViewModelViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView),
    WEPlaceholderCallback {
    private val viewModelContext = context
    private lateinit var inlineView: WEInlineView
    private val weInlineViewList: ArrayList<String> = ArrayList()
    val container: LinearLayout = itemView.findViewById<LinearLayout>(R.id.recyclerItemLayout)
    private var viewRegistry: ArrayList<ViewModel> = ArrayList<ViewModel>()
    private var customString: String = "Nothing done yet"
    private val customStringData = mutableListOf<Map<String, String>>()

    // Renders UI for the RecyclerView - ViewHolder
    fun bind(viewScreenModel: ScreenModel, position: Int) {
        val mainLayout = LinearLayout(viewModelContext)
        mainLayout.orientation = LinearLayout.VERTICAL
        mainLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        mainLayout.tag = "viewItemList"
        viewRegistry = viewScreenModel.viewRegistry
        var isInlineViewFound = false
        // renders screen layouts (custom/Non-custom)
        for (entry in viewRegistry) {
            Logger.d(Constants.TAG, "entry in holder $entry")
            if (entry.isCustomView) {
                addCustomTextView(entry, mainLayout)
                WEPersonalization.Companion.get()
                    .registerWEPlaceholderCallback(entry.propertyId, this)
            } else {
                if (entry.position == position) {
                    isInlineViewFound = true
                    addWEInlineView(entry, position, mainLayout)
                }
            }
        }
        Logger.d(Constants.TAG, "customStringData - $customStringData")

        if (!isInlineViewFound) {
            addDefaultTextView(viewScreenModel.listSize, mainLayout)
        }
        container.addView(mainLayout)

        for (propertyId in weInlineViewList) {
            Logger.d(Constants.TAG, "propertyId: " + propertyId)
            val inView: WEInlineView = container.findViewWithTag(propertyId)

            if (inView != null) {
                inView.load(propertyId, this)
            }
        }
    }

    fun addTrackEvent(container: LinearLayout) {
        val eventLayout = LinearLayout(viewModelContext).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.HORIZONTAL
        }

        val trackRecyclerText = EditText(viewModelContext).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            hint = "Enter event to track"
            tag = "trackText"
        }

        val trackRecyclerButton = Button(viewModelContext).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            tag = "trackButton"
            text = "Track Event"
        }

        eventLayout.addView(trackRecyclerText)
        eventLayout.addView(trackRecyclerButton)

        container.addView(eventLayout)
    }

    private fun addButtonsView(itemListLayout: LinearLayout) {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val impressionButton = Button(viewModelContext)
        impressionButton.text = "Impression"
        impressionButton.tag = "impressionTag"
        impressionButton.layoutParams = layoutParams

        val clickButton = Button(viewModelContext)
        clickButton.text = "Click"
        clickButton.tag = "clickTag"
        clickButton.layoutParams = layoutParams

        val linearButtonLayout = LinearLayout(viewModelContext)
        linearButtonLayout.addView(impressionButton)
        linearButtonLayout.addView(clickButton)
        itemListLayout.addView(linearButtonLayout)

        impressionButton.setOnClickListener {
            Logger.d(Constants.TAG, "Listener increased")
        }
        clickButton.setOnClickListener {
            Logger.d(Constants.TAG, "Click increased")
        }
    }

    private fun addCustomTextView(entry: ViewModel, mainLayout: LinearLayout) {
        val customScreenView = TextView(viewModelContext)
        val entry1 = mapOf(entry.propertyId to customString)
        customStringData.add(entry1)

        customScreenView.text = customString
        customScreenView.tag = entry.propertyId + "customViewer"
        customScreenView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50.toFloat())
        customScreenView.layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                Utils.covertDpToPixel(200)
            )
        mainLayout.addView(customScreenView)
        addButtonsView(mainLayout)
        Logger.d(Constants.TAG, "Register WEPlaceHolder for " + entry.propertyId)
        WEPersonalization.Companion.get().registerWEPlaceholderCallback(entry.propertyId, this)
    }


    private fun addWEInlineView(entry: ViewModel, position: Int, mainLayout: LinearLayout) {
        var layoutHeight: Int = LayoutParams.MATCH_PARENT
        var layoutWidth: Int = LayoutParams.MATCH_PARENT
        val propertyId: String = entry.propertyId
        val height: Int = entry.height!!
        val width: Int = entry.width!!
        inlineView = WEInlineView(viewModelContext, propertyId)
        weInlineViewList.add(propertyId)
        if (height != 0) {
            layoutHeight = Utils.covertDpToPixel(height)
        }
        if (width != 0) {
            layoutWidth = Utils.covertDpToPixel(width)
        }
        val params =
            LinearLayout.LayoutParams(layoutWidth, layoutHeight)
        inlineView.layoutParams = params
        mainLayout.addView(inlineView)
    }

    override fun onDataReceived(data: WECampaignData) {
        Logger.d(Constants.TAG, "onDataReceived: " + data.targetViewId)
        val propertyReceived = data.targetViewId
        val isCustomProperty = checkIfCustomInlineViewPosition(propertyReceived)
        Logger.d(Constants.TAG, "isCustomProperty:  $isCustomProperty \n +for $propertyReceived")

        if (isCustomProperty) {
            renderCustomData(data)
        }
    }

    fun renderButtons(itemListLayout: LinearLayout, data: WECampaignData) {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val impressionButton = Button(viewModelContext)
        impressionButton.text = "Impression"
        impressionButton.tag = "impressionTag"
        impressionButton.layoutParams = layoutParams

        val clickButton = Button(viewModelContext)
        clickButton.text = "Click"
        clickButton.tag = "clickTag"
        clickButton.layoutParams = layoutParams

        val linearButtonLayout = LinearLayout(viewModelContext)
        linearButtonLayout.addView(impressionButton)
        linearButtonLayout.addView(clickButton)
        itemListLayout.addView(linearButtonLayout)

        impressionButton.setOnClickListener {
            data.trackImpression(null)
        }
        clickButton.setOnClickListener {
            data.trackClick(null)
        }

    }

    fun renderCustomData(data: WECampaignData) {
        val mainLayout = container.findViewWithTag<LinearLayout>("viewItemList")


        val propertyReceived = data.targetViewId + "customViewer"
        val impressionButton = mainLayout.findViewWithTag<Button>("impressionTag")
        val clickButton = mainLayout.findViewWithTag<Button>("clickTag")

        val customTextView = mainLayout.findViewWithTag<TextView>(propertyReceived)
        customTextView?.text = data.toString()
        customTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50.toFloat())
        customString = data.toString()

        impressionButton.setOnClickListener {
            Logger.d(Constants.TAG, "Listener increased in onData")
            data.trackImpression(null)
        }
        clickButton.setOnClickListener {
            Logger.d(Constants.TAG, "Click increased in onData")
            data.trackClick(null)
        }
    }

    private fun renderError(campaignId: String?, targetViewId: String, errorString: String) {
        val propertyReceived = targetViewId + "customViewer"
        val containerLayout = container.findViewById<LinearLayout>(R.id.recyclerItemLayout)
        Logger.d(Constants.TAG, "Exception occured - $propertyReceived")

        val impressionButton = containerLayout.findViewWithTag<Button>("impressionTag")
        val clickButton = containerLayout.findViewWithTag<Button>("clickTag")

        val customTextView = containerLayout.findViewWithTag<TextView>(propertyReceived)
        customString =
            "Exception for $targetViewId \n campaignId - $campaignId  \n  Exception - $errorString"
        customTextView?.text = customString
        customTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50.toFloat())

        impressionButton.setOnClickListener {
            Toast.makeText(
                viewModelContext,
                "Not Valid when exception occuerd ",
                Toast.LENGTH_SHORT
            )
                .show()
        }
        clickButton.setOnClickListener {
            Toast.makeText(
                viewModelContext,
                "Not Valid when exception occuerd ",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    override fun onPlaceholderException(
        campaignId: String?,
        targetViewId: String,
        error: Exception
    ) {
        Logger.d(Constants.TAG, "onPlaceholderException inside: $targetViewId | Errro - $error")
        renderError(campaignId, targetViewId, error.toString());
    }

    override fun onRendered(data: WECampaignData) {
        Logger.d(Constants.TAG, "onRendered inside: " + data.targetViewId)
    }

    // Display Default Text incase of non-inline view
    private fun addDefaultTextView(position: Int?, mainLayout: LinearLayout) {
        val textView = TextView(viewModelContext)
        textView.text = "List -> ${position}"
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            Utils.covertDpToPixel(100),
            1f
        )
        layoutParams.setMargins(0, 50, 0, 50)
        textView.layoutParams = layoutParams
        mainLayout.addView(textView)
    }

    // return true  for customInline View Position else false
    private fun checkIfCustomInlineViewPosition(propertyReceived: String): Boolean {
        if (!viewRegistry.isNullOrEmpty()) {
            for ((index, i) in viewRegistry.withIndex()) {
                val currentEntry = viewRegistry[index]
                if (currentEntry.propertyId == propertyReceived && currentEntry.isCustomView) {
                    return true
                }
            }
        }
        return false
    }
}