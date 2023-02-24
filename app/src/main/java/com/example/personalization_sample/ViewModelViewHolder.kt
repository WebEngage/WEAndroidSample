import android.app.ActionBar
import android.app.ActionBar.LayoutParams
import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personalization_sample.R
import com.example.personalization_sample.Utils
import com.example.personalization_sample.model.Model
import com.example.personalization_sample.model.ViewModel
import com.webengage.personalization.WEInlineView
import com.webengage.personalization.callbacks.WEPlaceholderCallback
import com.webengage.personalization.data.WECampaignData
import java.lang.Exception

class ViewModelViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView),
    WEPlaceholderCallback {
    val viewModelContext = context
    private lateinit var inlineView: WEInlineView
    val weInlineViewList: ArrayList<String> = ArrayList()
    val container = itemView.findViewById<LinearLayout>(R.id.recyclerItemLayout)

    fun bind(viewModel: Model, position: Int) {
        val mainLayout = LinearLayout(viewModelContext)
        mainLayout.orientation = LinearLayout.VERTICAL
        mainLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

        val viewRegistry = viewModel.viewRegistry
        var isInlineViewFound = false
        for(entry in viewRegistry) {
            if(entry.position == position) {
                isInlineViewFound = true
                addWEInlineView(entry,position, mainLayout)
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
        inlineView.setBackgroundResource(R.color.purple_500)
        mainLayout.addView(inlineView)
    }

    override fun onDataReceived(data: WECampaignData) {
        Log.d("WEP", "onDataReceived: " + data.targetViewId)
    }

    override fun onPlaceholderException(
        campaignId: String?,
        targetViewId: String,
        error: Exception
    ) {
        Log.d("WEP", "onPlaceholderException inside: " + targetViewId)

    }

    override fun onRendered(data: WECampaignData) {
        Log.d("WEP", "onRendered inside: " + data.targetViewId)
    }

    fun addTextView(position: Int?, mainLayout: LinearLayout) {
        val textView = TextView(viewModelContext)
        textView.text = "List -> ${position}"
        textView.setBackgroundResource(R.color.teal_200)
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
}