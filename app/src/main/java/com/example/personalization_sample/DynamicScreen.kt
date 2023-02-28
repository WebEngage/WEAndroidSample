package com.example.personalization_sample

import android.app.ActionBar.LayoutParams
import android.content.Intent
import android.net.Uri
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
import com.webengage.personalization.utils.TAG
import com.webengage.sdk.android.Logger
import com.webengage.sdk.android.WebEngage

class DynamicScreen : AppCompatActivity(), WECampaignCallback, WEPlaceholderCallback  {
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
        val uri = intent.data
        if(uri != null) {
            val parameters = uri!!.pathSegments
            val param = parameters[parameters.size - 1]
        }
        val ss = intent.getStringExtra("pageData")
        if(!ss.isNullOrEmpty()) {
            Log.d("WebEngage-Inline-App", "ss value " + ss)
            modelData = Utils.convertStringToModel(ss)
        }

        Log.d("WebEngage-Inline-App", "intent data " + modelData)



if(modelData != null) {
    listSize = modelData?.listSize!!
    screenName = modelData.screenName
    eventName = modelData.eventName
    viewRegistry = modelData.viewRegistry
    isRecyclerView = modelData.isRecyclerView
    navigationButton = findViewById(R.id.navigation)
    navigationButton.setOnClickListener {
        turnOnModal()
    }
}

        WebEngage.get().analytics().screenNavigated(screenName)
        if(!eventName.isNullOrEmpty()) {
            WebEngage.get().analytics().track(eventName)
        }
        attachScreen()

    }

    private fun checkAndNavigateDeepLink(param: String?, deepLink: String, hostName: String) {
        var isScreenFound = false
        if(hostName.equals("www.youtube.com")) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLink))
            intent.setPackage("com.google.android.youtube")
            startActivity(intent)
        } else {
            val list = dataModel.getData()
            var screenData: Model = Model(0, "", "", false, ArrayList<ViewModel>())
            for (entry in list) {
                if (entry.screenName.equals(param)) {
                    screenData = entry
                    isScreenFound = true
                }
            }
            Log.d(TAG, "screenData " + screenData)
            if (isScreenFound) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLink))
                intent.setPackage("com.example.personalization_sample")
                intent.putExtra("pageData", Utils.convertModelToString(screenData))
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "Screen is not Available", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        WEPersonalization.Companion.get().registerWECampaignCallback(this);
    }

    override fun onStop() {
        super.onStop()
        WEPersonalization.Companion.get().unregisterWECampaignCallback(this);
    }

    fun attachScreen() {
        val container = findViewById<LinearLayout>(R.id.dynamicScreenLayout)
        container.removeAllViews()

        Log.d("WebEngage-Inline-App", "List Size " + listSize)

        val itemListLayout = LinearLayout(this)
        itemListLayout.orientation = LinearLayout.VERTICAL
        itemListLayout.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

        val weInlineViewList: ArrayList<String> = ArrayList()

        for (entry in 0..listSize) {
            val inlinePosition = checkIfInlineViewPosition(entry)
            if (inlinePosition == -1) {
                val screenNameTextView = TextView(this)
                screenNameTextView.text = "List - ${entry}"
                screenNameTextView.setBackgroundResource(R.color.teal_200)
                screenNameTextView.layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        Utils.covertDpToPixel(200)
                    )
                itemListLayout.addView(screenNameTextView)
            } else {
                var layoutHeight: Int = LayoutParams.MATCH_PARENT
                var layoutWidth: Int = LayoutParams.MATCH_PARENT
                val viewRegistryData = viewRegistry.get(inlinePosition)
                val propertyId: String = viewRegistryData.propertyId
                val height: Int = viewRegistryData.height!!
                val width: Int = viewRegistryData.width!!
                inlineView = WEInlineView(applicationContext, propertyId)
                weInlineViewList.add(propertyId)
                if (height != 0) {
                    layoutHeight = Utils.covertDpToPixel(height)
                }
                if (width != 0) {
                    layoutWidth = Utils.covertDpToPixel( width)
                    Log.d("WebEngage-Inline-App", "layoutWidth- " + layoutWidth+" | PID "+propertyId)
                }
                val params =
                    LinearLayout.LayoutParams(layoutWidth, layoutHeight)
                inlineView.layoutParams = params
                inlineView.setBackgroundResource(R.color.purple_500)
                itemListLayout.addView(inlineView)
            }

        }
            val scrollViewLayout = ScrollView(this)

            scrollViewLayout.setBackgroundResource(R.color.teal_700)
            scrollViewLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

            scrollViewLayout.addView(itemListLayout)
            container.addView(scrollViewLayout)

        for (propertyId in weInlineViewList) {
            Log.d("TAG", "propertyId: " + propertyId)
            val inView: WEInlineView = container.findViewWithTag(propertyId)
            if (inView != null) {
                inView.load(propertyId, this)
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
            navigateToScreen(navigationScreen, builder)
            builder.dismiss()
        }
    }

    fun navigateToScreen(screenName: String, builder: AlertDialog) {
        val list = dataModel.getData()
        var isScreenFound = false
        for (entry in list) {
            if (entry.screenName.equals(screenName)) {
                if(entry.isRecyclerView) {
                    val intent = Intent(this, RecyclerActivity::class.java)
                    intent.putExtra("pageData", Utils.convertModelToString(entry))
                    startActivity(intent)
                } else {
                    val intent = Intent(this, DynamicScreen::class.java)
                    intent.putExtra("pageData", Utils.convertModelToString(entry))
                    startActivity(intent)
                }
                isScreenFound = true
                builder.dismiss()
            }
        }
        if(!isScreenFound) {
            Toast.makeText(this,"Screen Not Found Enter valid screen", Toast.LENGTH_SHORT).show()
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

    override fun onCampaignPrepared(data: WECampaignData): WECampaignData? {
        Log.d(TAG_CAMPAGIN, "onCampaignPrepared called for "+data.targetViewId)
        return data
    }

    override fun onCampaignClicked(actionId: String, deepLink: String, data: WECampaignData): Boolean {
        Log.d(TAG_CAMPAGIN, "onCampaignClicked called for "+data.targetViewId + " DL - "+deepLink)
        val deepLinkData = deepLink.split("/")
        val screenName = if (deepLinkData.size > 3 && deepLinkData[2].equals("www.webengage.com")) { deepLinkData[3] } else { "" }
//        val screenName = deepLinkData[3]
        val hostName = deepLinkData[2]
        Log.d(TAG, "deepLinkData "+deepLinkData)
        checkAndNavigateDeepLink(screenName, deepLink, hostName)
        return true
    }

    override fun onCampaignShown(data: WECampaignData) {
        Log.d(TAG_CAMPAGIN, "onCampaignShown called for "+data.targetViewId)
    }

    override fun onCampaignException(campaignId: String?, targetViewId: String, error: Exception) {
        Log.d(TAG_CAMPAGIN, "onCampaignException called for "+targetViewId)
    }

    override fun onDataReceived(data: WECampaignData) {
        Log.d(TAG_VIEW, "onDataReceived: " + data.targetViewId)
    }

    override fun onPlaceholderException(
        campaignId: String?,
        targetViewId: String,
        error: Exception
    ) {
        Log.d(TAG_VIEW, "onPlaceholderException inside: " + targetViewId)

    }

    override fun onRendered(data: WECampaignData) {
        Log.d(TAG_VIEW, "onRendered inside: " + data.targetViewId)
    }


}