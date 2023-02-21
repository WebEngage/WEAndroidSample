package com.example.personalization_sample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalization_sample.model.Model
import com.example.personalization_sample.model.ViewModel
import com.webengage.personalization.WEInlineView
import com.webengage.personalization.callbacks.WEPlaceholderCallback
import com.webengage.personalization.data.WECampaignData
import com.webengage.sdk.android.WebEngage

class DynamicScreen : AppCompatActivity() {
    private lateinit var modelData: Model
    var listSize: Int = 0
    var screenName: String = ""
    var eventName: String = ""
    var viewRegistry: ArrayList<ViewModel> = ArrayList<ViewModel>()
    var isRecyclerView: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_screen)
        val ss:String = intent.getStringExtra("pageData").toString()
        modelData = Utils.convertStringToModel(ss)
        Log.d("AKSHAY", "intent data "+modelData)
        listSize = modelData.listSize!!
        screenName = modelData.screenName
        eventName = modelData.eventName
        viewRegistry = modelData.viewRegistry
        isRecyclerView = modelData.isRecyclerView

        WebEngage.get().analytics().screenNavigated(screenName)
        attachScreen()

    }

    fun attachScreen() {
        val container = findViewById<LinearLayout>(R.id.dynamicScreenLayout)
        container.removeAllViews()

//        val list = dataModel.getData()

        Log.d("AKSHAY", "list inside custom screen "+listSize)

//        val scrollViewLayout = if (isRecyclerView) {
//            Toast.makeText(this, "If - RecyclerView",Toast.LENGTH_SHORT).show()
//            RecyclerView(this)
//        } else {
//            Toast.makeText(this, "Else - Scrollview",Toast.LENGTH_SHORT).show()
//            ScrollView(this)
//        }
//        scrollViewLayout.orientation = LinearLayout.VERTICAL

        val itemListLayout = LinearLayout(this)
        itemListLayout.orientation = LinearLayout.VERTICAL
        itemListLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200)

        val dpValue = 200 // set your dp value here
        val scale = resources.displayMetrics.density
        val pixelValue = (dpValue * scale + 0.5f).toInt()
        for (entry in 0..listSize) {
            val inlinePosition = checkIfInlineViewPosition(entry)
            Log.d("AKSHAY", "List entry - "+entry+" | Inline pos - "+inlinePosition)
            if(inlinePosition == -1) {
                val screenNameTextView = TextView(this)
                screenNameTextView.text = "List - ${entry}"
                screenNameTextView.setBackgroundResource(R.color.teal_700)
                screenNameTextView.layoutParams =
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, pixelValue)
                itemListLayout.addView(screenNameTextView)
            }
//            else {
////                Timer().schedule(timerTask {
////                    runOnUiThread {
//                        val propertyId: String = viewRegistry.get(inlinePosition).propertyId
//                        Log.d("AKSHAY", "Loading else - "+propertyId)
//                        val view = WEInlineView(applicationContext, propertyId)
//                        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pixelValue)
//                        view.layoutParams = params
//                        view.setBackgroundResource(R.color.purple_500)
//                        itemListLayout.addView(view)
//                        Log.d("AKSHAY", "Loading - "+propertyId)
////                        view.load(propertyId, this)
////                findViewById<LinearLayout>(R.id.linear).addView(view)
////                        view.load(propertyId, object : WEPlaceholderCallback {
////                            override fun onDataReceived(data: WECampaignData) {
////                                Log.d("TAG", "onDataReceived: flutter_text inside" )
////                            }
////
////                            override fun onPlaceholderException(
////                                campaignId: String?,
////                                targetViewId: String,
////                                error: Exception
////                            ) {
////                            }
////
////                            override fun onRendered(data: WECampaignData) {
////                                Log.d("TAG", "onRendered inside: " )
////                            }
////
////                        })
//                    }
//
//                }, 1000)

            }
        val scrollViewLayout = ScrollView(this)
//        val layoutManager = LinearLayoutManager(this)
//        scrollViewLayout.layoutManager = layoutManager
//        val scrollViewLayout = getLayoutManager(isRecyclerView)

        scrollViewLayout.setBackgroundResource(R.color.teal_700)
        scrollViewLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        scrollViewLayout.addView(itemListLayout)
                    container.addView(scrollViewLayout)



//        if(isRecyclerView) {
//            val recyclerView: RecyclerView = RecyclerView(this)
//            val layoutManager = LinearLayoutManager(this)
//            recyclerView.layoutManager = layoutManager
//            recyclerView.setBackgroundResource(R.color.teal_700)
//            recyclerView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//
//            recyclerView.addView(itemListLayout)
//            container.addView(recyclerView)
//        } else {
//            val scrollViewLayout: RecyclerView = RecyclerView(this)
//            scrollViewLayout.setBackgroundResource(R.color.teal_700)
//            scrollViewLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//
//            scrollViewLayout.addView(itemListLayout)
//            container.addView(scrollViewLayout)
//        }

    }

    fun getLayoutManager(isRecyclerView: Boolean): View {
        val topLevelView: View
        if (isRecyclerView) {
            topLevelView = RecyclerView(this)
        } else {
            topLevelView = ScrollView(this)
        }
        return topLevelView

    }


    fun checkIfInlineViewPosition(entry: Int): Int {
        if(!viewRegistry.isNullOrEmpty()) {
            for ((index,i) in viewRegistry.withIndex()) {
                if(entry == i.position) {
                    return index
                }
            }
        }

        return -1
    }

//    override fun onDataReceived(data: WECampaignData) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onPlaceholderException(
//        campaignId: String?,
//        targetViewId: String,
//        error: Exception
//    ) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onRendered(data: WECampaignData) {
//        TODO("Not yet implemented")
//    }

}