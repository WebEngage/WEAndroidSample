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
import com.webengage.personalization.WEInlineView
import com.webengage.personalization.WEPersonalization
import com.webengage.personalization.callbacks.WEPlaceholderCallback
import com.webengage.personalization.data.WECampaignData
import com.webengage.sdk.android.Logger
import java.lang.Exception

class ViewModelViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView),
    WEPlaceholderCallback {
    val viewModelContext = context
    private lateinit var inlineView: WEInlineView
    val weInlineViewList: ArrayList<String> = ArrayList()
    val container = itemView.findViewById<LinearLayout>(R.id.recyclerItemLayout)
    var viewRegistry: ArrayList<ViewModel> = ArrayList<ViewModel>()

    fun bind(viewModel: Model, position: Int) {
        val mainLayout = LinearLayout(viewModelContext)
        mainLayout.orientation = LinearLayout.VERTICAL
        mainLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        mainLayout.tag = "viewItemList"
        viewRegistry = viewModel.viewRegistry
        var isInlineViewFound = false
        for(entry in viewRegistry) {
            if(entry.position == position) {
                isInlineViewFound = true
                if(entry.isCustomView) {
                    addCustomTextView(entry,mainLayout)
                    WEPersonalization.Companion.get().registerWEPlaceholderCallback(entry.propertyId, this)

                } else {
                    addWEInlineView(entry, position, mainLayout)
                }
            }
        }
        if(!isInlineViewFound) {
            addTextView(viewModel.listSize, mainLayout)
        }

        container.addView(mainLayout)

        for (propertyId in weInlineViewList) {
            Log.d("WEP", "propertyId: " + propertyId)
            val inView: WEInlineView = container.findViewWithTag(propertyId)
            if (inView != null) {
                inView.load(propertyId, this)
            }
        }
    }

    private fun addCustomTextView(entry: ViewModel,mainLayout: LinearLayout) {
        val customTextView = TextView(viewModelContext)
        val propertyId = entry.propertyId
        customTextView.text = "Welcome To Custom View"
        customTextView.tag = propertyId + "customViewer"
        customTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            Utils.covertDpToPixel(100),
            1f
        )
        layoutParams.setMargins(0,50,0,50)
        customTextView.layoutParams = layoutParams
        mainLayout.addView(customTextView)
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
        Log.d("WEP", "onDataReceived: " + data.targetViewId)
        val propertyReceived = data.targetViewId
        val isCustomProperty = checkIfCustomInlineViewPosition(propertyReceived)
        Log.d("AKC", "isCustomProperty:  "+isCustomProperty+" \n +for "+propertyReceived)

        if(isCustomProperty) {
            renderCustomData(data)
            renderButtons(container, data)
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
        val propertyReceived = data.targetViewId + "customViewer"
        val mainLayout = container.findViewWithTag<LinearLayout>("viewItemList")
        val customTextView = mainLayout.findViewWithTag<TextView>(propertyReceived)
        customTextView?.text = data.toString()

//        impressionButton.setOnClickListener {
//            Logger.d("AKC", "Listener increased in onData")
//            data.trackImpression(null)
//        }
//        clickButton.setOnClickListener {
//            Logger.d("AKC", "Click increased in onData")
//            data.trackClick(null)
//        }
    }

    override fun onPlaceholderException(
        campaignId: String?,
        targetViewId: String,
        error: Exception
    ) {
        Log.d("WEP", "onPlaceholderException inside: " + targetViewId + " | Errro - "+error)

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