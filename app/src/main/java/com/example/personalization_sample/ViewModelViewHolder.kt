import android.app.ActionBar
import android.app.ActionBar.LayoutParams
import android.content.Context
import android.util.Log
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
        for(entry in viewRegistry) {
            if(entry.position == position) {
                addWEInlineView(entry,position, mainLayout)
            } else {
                addTextView(viewModel.listSize, mainLayout)
            }
        }
        container.addView(mainLayout)

        for (propertyId in weInlineViewList) {
            Log.d("TAG", "propertyId: " + propertyId)
            val inView: WEInlineView = container.findViewWithTag(propertyId)
//        inView.setBackgroundResource(R.color.black)
            if (inView != null) {
                inView.load(propertyId, this)
            }
        }
    }


    fun addWEInlineView(entry: ViewModel,position: Int, mainLayout: LinearLayout) {

        Log.d("WEP", "list inside custom screen " + entry)
        var layoutHeight: Int = LayoutParams.MATCH_PARENT
        var layoutWidth: Int = LayoutParams.MATCH_PARENT
        val propertyId: String = entry.propertyId
        val height: Int = entry.height!!
        val width: Int = entry.width!!
        Log.d("AKSHAY", "Loading else - " + propertyId)
        inlineView = WEInlineView(viewModelContext, propertyId)
        weInlineViewList.add(propertyId)
        if (height != 0) {
            layoutHeight = Utils.covertDpToPixel(height)
        }
        if (width != 0) {
            layoutWidth = Utils.covertDpToPixel(width)
            Log.d("AKSHAY", "layoutWidth- " + layoutWidth+" | PID "+propertyId)
        }
        val params =
            LinearLayout.LayoutParams(layoutWidth, layoutHeight)
//                Utils.covertDpToPixel(scale, 200)
        inlineView.layoutParams = params
        inlineView.setBackgroundResource(R.color.purple_500)
        Log.d("AKSHAY", "Loading - " + propertyId)
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
        textView.layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        mainLayout.addView(textView)
    }
}