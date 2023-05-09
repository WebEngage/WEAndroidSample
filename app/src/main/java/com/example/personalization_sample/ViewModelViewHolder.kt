import android.app.ActionBar
import android.app.ActionBar.LayoutParams
import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.personalization_sample.R
import com.example.personalization_sample.Utils
import com.example.personalization_sample.model.Model
import com.example.personalization_sample.model.ViewModel
import com.google.android.material.internal.ViewUtils
import com.webengage.personalization.WEInlineView
import com.webengage.personalization.WEPersonalization
import com.webengage.personalization.callbacks.WEPlaceholderCallback
import com.webengage.personalization.data.WECampaignData
import com.webengage.sdk.android.Logger
import com.webengage.sdk.android.WebEngage
import java.lang.Exception

class ViewModelViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView),
    WEPlaceholderCallback {
    val viewModelContext = context
    private lateinit var inlineView: WEInlineView
//    val trackLayout = itemView.findViewById<LinearLayout>(R.id.eventLayout)
//    val trackEventText = itemView.findViewById<EditText>(R.id.trackRecyclerText)
//    val trackRandom = itemView.findViewById<Button>(R.id.trackRecyclerButton)
    val weInlineViewList: ArrayList<String> = ArrayList()
    val container = itemView.findViewById<LinearLayout>(R.id.recyclerItemLayout)
    var viewRegistry: ArrayList<ViewModel> = ArrayList<ViewModel>()
    var customString: String = "Nothing done yet"
    val customStringData = mutableListOf<Map<String, String>>()
    fun bind(viewModel: Model, position: Int) {
        val mainLayout = LinearLayout(viewModelContext)
        mainLayout.orientation = LinearLayout.VERTICAL
        mainLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        mainLayout.tag = "viewItemList"

//        addTrackEvent(container)


        viewRegistry = viewModel.viewRegistry
        var isInlineViewFound = false
        for(entry in viewRegistry) {
            Logger.d("WebEngage", "entry in holder $entry")
            if(entry.isCustomView) {
                addCustomTextView(entry,mainLayout)
                Logger.d("WebEngage", "entry in - Registering  $entry")
                WEPersonalization.Companion.get().registerWEPlaceholderCallback(entry.propertyId, this)
            } else {
                if (entry.position == position) {
                    isInlineViewFound = true
                        addWEInlineView(entry, position, mainLayout)
                }
            }
        }
//        println(customStringData)
        Logger.d("WebEngage", "customStringData - $customStringData")
        if(!isInlineViewFound) {
            addTextView(viewModel.listSize, mainLayout)
        }


        // s7


        container.addView(mainLayout)

//        trackRandom.setOnClickListener {
//            val eventToTrack = trackEventText.text.toString()
//            Logger.d("WebEngage", "tracking event - $eventToTrack")
//
//            WebEngage.get().analytics().track(eventToTrack)
//        }

        for (propertyId in weInlineViewList) {
            Log.d("WEP", "propertyId: " + propertyId)
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
            Logger.d("AKC", "Listener increased")
        }
        clickButton.setOnClickListener {
            Logger.d("AKC", "Click increased")
        }
    }

    private fun addCustomTextView(entry: ViewModel,mainLayout: LinearLayout) {
//        val customTextView = TextView(viewModelContext)
//        val propertyId = entry.propertyId
//        customTextView.text = "Welcome To Custom View"
//        customTextView.tag = propertyId + "customViewer"
//        customTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
//
//        val layoutParams = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            Utils.covertDpToPixel(100),
//            1f
//        )
//        layoutParams.setMargins(0,50,0,50)
//        customTextView.layoutParams = layoutParams
//        mainLayout.addView(customTextView)
        val customScreenView = TextView(viewModelContext)
//        customScreenView.text = "Lists - ${entry}"
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
        Log.d("WebEngage-Inline-App", "Register WEPlaceHolder for " + entry.propertyId)
        WEPersonalization.Companion.get().registerWEPlaceholderCallback(entry.propertyId, this)
    }


    fun addWEInlineView(entry: ViewModel,position: Int, mainLayout: LinearLayout) {
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
//        inlineView.setBackgroundResource(R.color.purple_500)
        mainLayout.addView(inlineView)
    }

    override fun onDataReceived(data: WECampaignData) {
        Log.d("WebEngage", "onDataReceived: " + data.targetViewId)
        val propertyReceived = data.targetViewId
        val isCustomProperty = checkIfCustomInlineViewPosition(propertyReceived)
        Log.d("WebEngage", "isCustomProperty:  "+isCustomProperty+" \n +for "+propertyReceived)

        if(isCustomProperty) {
            renderCustomData(data)
//            renderButtons(container, data)
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
        Logger.d("WebEngage", "renderCustomData - ")
        val mainLayout = container.findViewWithTag<LinearLayout>("viewItemList")
//        val customTextView = mainLayout.findViewWithTag<TextView>(propertyReceived)
//        customTextView?.text = data.toString()
        Logger.d("WebEngage", "renderCustomData1 - "+data.toString())


        val propertyReceived = data.targetViewId + "customViewer"
//        val container = findViewById<LinearLayout>(R.id.dynamicScreenLayout)
//        val scrollContainer = mainLayout.findViewWithTag<ScrollView>("scrollTag")
//        val customView = scrollContainer.findViewWithTag<LinearLayout>("viewItemList")
        Logger.d("WebEngage", "renderCustomData - "+data.toString())

//        TODO - crashing
        val impressionButton = mainLayout.findViewWithTag<Button>("impressionTag")
        val clickButton = mainLayout.findViewWithTag<Button>("clickTag")

        val customTextView = mainLayout.findViewWithTag<TextView>(propertyReceived)
        customTextView?.text = data.toString()
        customTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50.toFloat())


        customString = data.toString()

        impressionButton.setOnClickListener {
            Logger.d("WebEngage", "Listener increased in onData")
            data.trackImpression(null)
        }
        clickButton.setOnClickListener {
            Logger.d("WebEngage", "Click increased in onData")
            data.trackClick(null)
        }
    }

    fun renderError(campaignId: String?, targetViewId: String, errorString: String) {
        val propertyReceived = targetViewId + "customViewer"
        val containerLayout =  container.findViewById<LinearLayout>(R.id.recyclerItemLayout)
        Logger.d("WebEngage", "Exception occured - "+propertyReceived)

        val impressionButton = containerLayout.findViewWithTag<Button>("impressionTag")
        val clickButton = containerLayout.findViewWithTag<Button>("clickTag")

        val customTextView = containerLayout.findViewWithTag<TextView>(propertyReceived)
        customString = "Exception for $targetViewId \n campaignId - $campaignId  \n  Exception - $errorString"
        customTextView?.text = customString
        customTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50.toFloat())

        impressionButton.setOnClickListener {
            Toast.makeText(viewModelContext, "Not Valid when exception occuerd ", Toast.LENGTH_SHORT)
                .show()
        }
        clickButton.setOnClickListener {
            Toast.makeText(viewModelContext, "Not Valid when exception occuerd ", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onPlaceholderException(
        campaignId: String?,
        targetViewId: String,
        error: Exception
    ) {
        Log.d("WebEngage", "onPlaceholderException inside: " + targetViewId + " | Errro - "+error)
        renderError(campaignId, targetViewId, error.toString());
    }

    override fun onRendered(data: WECampaignData) {
        Log.d("WEP", "onRendered inside: " + data.targetViewId)
    }

    fun addTextView(position: Int?, mainLayout: LinearLayout) {
        val textView = TextView(viewModelContext)
        textView.text = "List -> ${position}"
//        textView.setBackgroundResource(R.color.teal_200)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            Utils.covertDpToPixel(100),
            1f
        )
        layoutParams.setMargins(0,50,0,50)
        textView.layoutParams = layoutParams
        mainLayout.addView(textView)
    }

    fun checkIfCustomInlineViewPosition(propertyReceived: String): Boolean {
        if (!viewRegistry.isNullOrEmpty()) {
            for ((index, i) in viewRegistry.withIndex()) {
                val currentEntry = viewRegistry[index]
                if(currentEntry.propertyId == propertyReceived && currentEntry.isCustomView) {
                    return true
                }
            }
        }
        return false
    }
}