package com.example.personalization_sample

import android.app.ActionBar.LayoutParams
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalization_sample.model.DataModel
import com.example.personalization_sample.model.Model
import com.example.personalization_sample.model.ViewModel
import com.webengage.personalization.WEInlineView
import com.webengage.personalization.WEPersonalization
import com.webengage.personalization.callbacks.WECampaignCallback
import com.webengage.personalization.callbacks.WEPlaceholderCallback
import com.webengage.personalization.data.WECampaignData
import com.webengage.sdk.android.WebEngage

class DynamicScreen : AppCompatActivity() {
    val TAG_CAMPAGIN: String = "WebEngage-Campaign"
    val TAG_VIEW: String = "WebEngage-View"
    private lateinit var modelData: Model
    var listSize: Int = 0
    var screenName: String = ""
    var eventName: String = ""
    var viewRegistry: ArrayList<ViewModel> = ArrayList<ViewModel>()
    var isRecyclerView: Boolean = false
    private lateinit var navigationButton: Button
    private lateinit var inlineView: WEInlineView
    val dataModel = DataModel.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_screen)
        val ss: String = intent.getStringExtra("pageData").toString()
        modelData = Utils.convertStringToModel(ss)
        Log.d("AKSHAY", "intent data " + modelData)
        listSize = modelData?.listSize!!
        screenName = modelData.screenName
        eventName = modelData.eventName
        viewRegistry = modelData.viewRegistry
        isRecyclerView = modelData.isRecyclerView
        navigationButton = findViewById(R.id.navigation)

        navigationButton.setOnClickListener {
            turnOnModal()
        }
        WebEngage.get().analytics().screenNavigated(screenName)
        attachScreen()

    }

    override fun onStart() {
        super.onStart()
        WEPersonalization.Companion.get().registerWECampaignCallback(object : WECampaignCallback {

            override fun onCampaignPrepared(data: WECampaignData): WECampaignData? {
                Log.d(TAG_CAMPAGIN, "onCampaignPrepared called for "+data.targetViewId)
                return data
            }

            override fun onCampaignClicked(actionId: String, deepLink: String, data: WECampaignData): Boolean {
                Log.d(TAG_CAMPAGIN, "onCampaignShown called for "+data.targetViewId)
                return false
            }

            override fun onCampaignShown(data: WECampaignData) {
                Log.d(TAG_CAMPAGIN, "onCampaignShown called for "+data.targetViewId)
            }

            override fun onCampaignException(campaignId: String?, targetViewId: String, error: Exception) {
                Log.d(TAG_CAMPAGIN, "onCampaignException called for "+targetViewId)
            }

        });
    }

    fun attachScreen() {
        val container = findViewById<LinearLayout>(R.id.dynamicScreenLayout)
        container.removeAllViews()

//        val list = dataModel.getData()

        Log.d("AKSHAY", "list inside custom screen " + listSize)

        val itemListLayout = LinearLayout(this)
        itemListLayout.orientation = LinearLayout.VERTICAL
        itemListLayout.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

//        val dpValue = 200 // set your dp value here
//        val scale = resources.displayMetrics.density
//        val pixelValue = (dpValue * scale + 0.5f).toInt()
        val scale = resources.displayMetrics.density

        val weInlineViewList: ArrayList<String> = ArrayList()

        for (entry in 0..listSize) {
            val inlinePosition = checkIfInlineViewPosition(entry)
            Log.d("AKSHAY", "List entry - " + entry + " | Inline pos - " + inlinePosition)
            if (inlinePosition == -1) {
                val screenNameTextView = TextView(this)
                screenNameTextView.text = "List - ${entry}"
                screenNameTextView.setBackgroundResource(R.color.teal_200)
                screenNameTextView.layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        Utils.covertDpToPixel(scale, 200)
                    )
                itemListLayout.addView(screenNameTextView)
            } else {
                var layoutHeight: Int = LayoutParams.MATCH_PARENT
                var layoutWidth: Int = LayoutParams.MATCH_PARENT
                val viewRegistryData = viewRegistry.get(inlinePosition)
                val propertyId: String = viewRegistryData.propertyId
                val height: Int = viewRegistryData.height!!
                val width: Int = viewRegistryData.width!!
                Log.d("AKSHAY", "Loading else - " + propertyId)
                inlineView = WEInlineView(applicationContext, propertyId)
                weInlineViewList.add(propertyId)
                if (height != 0) {
                    layoutHeight = Utils.covertDpToPixel(scale, height)
                }
                if (width != 0) {
                    layoutWidth = Utils.covertDpToPixel(scale, width)
                    Log.d("AKSHAY", "layoutWidth- " + layoutWidth+" | PID "+propertyId+inlinePosition)
                }
//TODO
                val params =
                    LinearLayout.LayoutParams(layoutWidth, layoutHeight)
//                Utils.covertDpToPixel(scale, 200)
                inlineView.layoutParams = params
                inlineView.setBackgroundResource(R.color.purple_500)
                itemListLayout.addView(inlineView)
                Log.d("AKSHAY", "Loading - " + propertyId)
            }

        }
        if (isRecyclerView) {
            val recyclerView: RecyclerView = RecyclerView(this)
            val layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            recyclerView.setBackgroundResource(R.color.teal_700)
            recyclerView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
//            adapter
//


            recyclerView.addView(itemListLayout)
            container.addView(recyclerView)
        }
        else {
            val scrollViewLayout = ScrollView(this)

            scrollViewLayout.setBackgroundResource(R.color.teal_700)
            scrollViewLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT   // changed to MATCH
            )

            scrollViewLayout.addView(itemListLayout)
            container.addView(scrollViewLayout)
        }

        for (propertyId in weInlineViewList) {
            Log.d("TAG", "propertyId: " + propertyId)
            val inView: WEInlineView = container.findViewWithTag(propertyId)
//        inView.setBackgroundResource(R.color.black)
            if (inView != null) {
                inView.load(propertyId, object : WEPlaceholderCallback {
                    override fun onDataReceived(data: WECampaignData) {
                        Log.d(TAG_VIEW, "onDataReceived: " + propertyId)
                    }

                    override fun onPlaceholderException(
                        campaignId: String?,
                        targetViewId: String,
                        error: Exception
                    ) {
                        Log.d(TAG_VIEW, "onPlaceholderException inside: " + propertyId)

                    }

                    override fun onRendered(data: WECampaignData) {
                        Log.d(TAG_VIEW, "onRendered inside: " + propertyId)
                    }

                })
            }
        }

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

    fun turnOnModal() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.modal_navigation, null)
        val builder = AlertDialog.Builder(this).create()
        val navigationText = dialogView.findViewById<EditText>(R.id.navigationTextBox)
        val navigateButton = dialogView.findViewById<Button>(R.id.navigateToButton)
        builder.setView(dialogView)
        // Add any desired dialog options
        builder.setTitle("Add Screen Navigation Data")
        // Call show() on the dialog builder to display the modal dialog
        builder.show()

        navigateButton.setOnClickListener {
            val navigationScreen = navigationText.text.toString()
            navigateToScreen(navigationScreen)
            Toast.makeText(this, "Let's Navigate to " + navigationScreen, Toast.LENGTH_SHORT).show()
            builder.dismiss()
        }
    }

    fun navigateToScreen(screenName: String) {
        val list = dataModel.getData()
        for (entry in list) {
            if (entry.screenName.equals(screenName)) {
                val intent = Intent(this, DynamicScreen::class.java)
                intent.putExtra("pageData", Utils.convertModelToString(entry))
                startActivity(intent)
            }
        }

    }


    fun checkIfInlineViewPosition(entry: Int): Int {
        if (!viewRegistry.isNullOrEmpty()) {
            for ((index, i) in viewRegistry.withIndex()) {
                if (entry == i.position) {
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