package com.webengage.sample.inline

import android.app.ActionBar.LayoutParams
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.webengage.sample.R
import com.webengage.sample.Utils.Constants
import com.webengage.sample.Utils.Utils
import com.webengage.sample.model.DataModel
import com.webengage.sample.model.Model
import com.webengage.sample.model.ViewModel
import com.webengage.personalization.WEInlineView
import com.webengage.personalization.WEPersonalization
import com.webengage.personalization.callbacks.WECampaignCallback
import com.webengage.personalization.callbacks.WEPlaceholderCallback
import com.webengage.personalization.data.WECampaignData
import com.webengage.sdk.android.Logger
import com.webengage.sdk.android.WebEngage

class DynamicScreen : AppCompatActivity(), WECampaignCallback, WEPlaceholderCallback  {
    private lateinit var modelData: Model
    private var listSize: Int = 0
    private var screenName: String = ""
    private var eventName: String = ""
    var viewRegistry: ArrayList<ViewModel> = ArrayList<ViewModel>()
    var isRecyclerView: Boolean = false
    var isClickHandledByUser: Boolean = false
    var count = 0;
    private lateinit var autoClickHandle: Switch
    private lateinit var trackEventText: TextView
    private lateinit var trackRandom: Button
    private lateinit var navigationButton: Button
    private lateinit var inlineView: WEInlineView

    private val dataModel = DataModel.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_screen)
    }

    private fun checkAndNavigateDeepLink(param: String?, deepLink: String, hostName: String) {
        var isScreenFound = false
        if(hostName == "www.youtube.com") {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLink))
            intent.setPackage("com.google.android.youtube")
            startActivity(intent)
        } else {
            val list = dataModel.getData()
            var screenData: Model = Model(0, "", "", "",null, false, ArrayList<ViewModel>())
            for (entry in list) {
                if (entry.screenName.equals(param)) {
                    screenData = entry
                    isScreenFound = true
                }
            }
            Logger.d(Constants.TAG, "screenData $screenData")
            if (isScreenFound) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLink))
                intent.setPackage("com.webengage.sample")
                intent.putExtra("pageData", Utils.convertModelToString(screenData))
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "Sorry! This Link is not handled by user/ Invalid ScreenName to navigate", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        WEPersonalization.Companion.get().registerWECampaignCallback(this);

        val uri = intent.data
        if(uri != null) {
            val parameters = uri!!.pathSegments
            val param = parameters[parameters.size - 1]
            Logger.d(Constants.TAG, " Params - $param");
        }
        val ss = intent.getStringExtra("pageData")
        if(!ss.isNullOrEmpty()) {
            modelData = Utils.convertStringToModel(ss)
        }
        Logger.d(Constants.TAG, "intent data $modelData")
        if(modelData != null) {
            listSize = modelData?.listSize!!
            screenName = modelData.screenName
            eventName = modelData.eventName
            viewRegistry = modelData.viewRegistry
            isRecyclerView = modelData.isRecyclerView
            navigationButton = findViewById(R.id.navigation)
            trackRandom = findViewById(R.id.trackRandom)
            trackEventText = findViewById(R.id.trackEventText)
            autoClickHandle = findViewById<Switch>(R.id.autoClickHandle)
            navigationButton.setOnClickListener {
                turnOnModal()
            }

            trackRandom.setOnClickListener {
                val eventToTrack = trackEventText.text.toString()
                Logger.d(Constants.TAG, "tracking event - $eventToTrack")
                WebEngage.get().analytics().track(eventToTrack)
            }

            autoClickHandle.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked != isClickHandledByUser) {
                    isClickHandledByUser = isChecked
                }
            }
        }

        if(!modelData.idName.isNullOrEmpty() && modelData.idValue != null && count == 0) {
            val screenData: MutableMap<String, Any> = HashMap()
            screenData[modelData.idName] = modelData.idValue!!
            count += 1
            WebEngage.get().analytics().screenNavigated(screenName,screenData )
        } else {
            WebEngage.get().analytics().screenNavigated(screenName)
        }
        if(!eventName.isNullOrEmpty()) {
            WebEngage.get().analytics().track(eventName)
        }
        attachScreen()

    }

    override fun onStop() {
        super.onStop()
        WEPersonalization.Companion.get().unregisterWECampaignCallback(this);
    }

    fun attachScreen() {
        val container = findViewById<LinearLayout>(R.id.dynamicScreenLayout)
        container.removeAllViews()
        val itemListLayout = LinearLayout(this)
        itemListLayout.tag = "viewItemList"
        itemListLayout.orientation = LinearLayout.VERTICAL
        itemListLayout.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

        val weInlineViewList: ArrayList<String> = ArrayList()

        for (entry in 0..listSize) {
            val inlinePosition = checkIfInlineViewPosition(entry)
            if (inlinePosition == -1) {
                val screenNameTextView = TextView(this)
                screenNameTextView.text = "List - ${entry}"
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
                if (viewRegistryData.isCustomView) {
                    val customScreenView = TextView(this)
                    customScreenView.text = "Lists - ${entry}"
                    customScreenView.tag = propertyId + "customViewer"
                    customScreenView.layoutParams =
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            Utils.covertDpToPixel(200)
                        )
                    itemListLayout.addView(customScreenView)
                    addButtonsView(itemListLayout)
                    Logger.d(Constants.TAG, "Register WEPlaceHolder for $propertyId")
                    WEPersonalization.Companion.get().registerWEPlaceholderCallback(propertyId, this)

                } else {
                    val height: Int = viewRegistryData.height!!
                val width: Int = viewRegistryData.width!!
                inlineView = WEInlineView(this, propertyId)
                weInlineViewList.add(propertyId)
                if (height != 0) {
                    layoutHeight = Utils.covertDpToPixel(height)
                }
                if (width != 0) {
                    layoutWidth = Utils.covertDpToPixel(width)
                    Logger.d(
                        Constants.TAG,
                        "layoutWidth- $layoutWidth | PID $propertyId"
                    )
                }
                val params =
                    LinearLayout.LayoutParams(layoutWidth, layoutHeight)
                inlineView.layoutParams = params
                itemListLayout.addView(inlineView)
            }
            }

        }
            val scrollViewLayout = ScrollView(this)
        scrollViewLayout.tag = "scrollTag"

            scrollViewLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

            scrollViewLayout.addView(itemListLayout)
            container.addView(scrollViewLayout)

        for (propertyId in weInlineViewList) {
            val inView: WEInlineView = container.findViewWithTag(propertyId)
            if (inView != null) {
                inView.load(propertyId, this)
            }
        }

    }

    private fun addButtonsView(itemListLayout: LinearLayout) {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val impressionButton = Button(this)
        impressionButton.text = "Impression"
        impressionButton.tag = "impressionTag"
        impressionButton.layoutParams = layoutParams

        val clickButton = Button(this)
        clickButton.text = "Click"
        clickButton.tag = "clickTag"
        clickButton.layoutParams = layoutParams

        val linearButtonLayout = LinearLayout(this)
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

    private fun turnOnModal() {
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

    private fun navigateToScreen(screenName: String, builder: AlertDialog) {
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

    private fun checkIfInlineViewPosition(entry: Int): Int {
        if (!viewRegistry.isNullOrEmpty()) {
            for ((index, i) in viewRegistry.withIndex()) {
                if (entry == i.position) {
                    return index
                }
            }
        }
        return -1
    }

    private fun checkIfCustomInlineViewPosition(propertyReceived: String): Boolean {
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

    override fun onCampaignPrepared(data: WECampaignData): WECampaignData? {
        Logger.d(Constants.TAG, "onCampaignPrepared called for "+data.targetViewId)
        return data
    }

    override fun onCampaignClicked(actionId: String, deepLink: String, data: WECampaignData): Boolean {
        Logger.d(Constants.TAG, "onCampaignClicked called for "+data.targetViewId + " DL - "+deepLink)
        if(isClickHandledByUser) {
            val deepLinkData = deepLink.split("/")
            val screenName =
                if (deepLinkData.size > 3 && deepLinkData[2].equals("www.webengage.com")) {
                    deepLinkData[3]
                } else {
                    ""
                }
            Logger.d(Constants.TAG, "deepLinkData $deepLinkData")
            val hostName = deepLinkData[2]
            checkAndNavigateDeepLink(screenName, deepLink, hostName)
        }
        return isClickHandledByUser
    }

    override fun onCampaignShown(data: WECampaignData) {
        Logger.d(Constants.TAG, "onCampaignShown called for "+data.targetViewId)
    }

    override fun onCampaignException(campaignId: String?, targetViewId: String, error: Exception) {
        Logger.d(Constants.TAG, "onCampaignException called for $targetViewId")
    }

    override fun onDataReceived(data: WECampaignData) {
        val propertyReceived = data.targetViewId
        Logger.d(Constants.TAG, "onDataReceived: " + data.targetViewId)
        val isCustomProperty = checkIfCustomInlineViewPosition(propertyReceived)
        if(isCustomProperty) {
            Logger.d(Constants.TAG, "onCustomDataReceived: for $propertyReceived \n +data")
            renderCustomData(data)
        }

    }

    private fun renderCustomData(data: WECampaignData) {
        val propertyReceived = data.targetViewId + "customViewer"
        val container = findViewById<LinearLayout>(R.id.dynamicScreenLayout)
        val scrollContainer = container.findViewWithTag<ScrollView>("scrollTag")
        val customView = scrollContainer.findViewWithTag<LinearLayout>("viewItemList")
        Logger.d(Constants.TAG, "getting property $propertyReceived")

        val impressionButton = scrollContainer.findViewWithTag<Button>("impressionTag")
        val clickButton = scrollContainer.findViewWithTag<Button>("clickTag")

        val customTextView = customView.findViewWithTag<TextView>(propertyReceived)
        customTextView?.text = data.toString()

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
        val container = findViewById<LinearLayout>(R.id.dynamicScreenLayout)
        val scrollContainer = container.findViewWithTag<ScrollView>("scrollTag")
        val customView = scrollContainer.findViewWithTag<LinearLayout>("viewItemList")
        Logger.d(Constants.TAG, "Exception occured for propertyId- $propertyReceived")

        val impressionButton = scrollContainer.findViewWithTag<Button>("impressionTag")
        val clickButton = scrollContainer.findViewWithTag<Button>("clickTag")

        val customTextView = customView.findViewWithTag<TextView>(propertyReceived)
        customTextView?.text = "Exception for $targetViewId \n campaignId - $campaignId  \n  Exception - $errorString"

        customTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 60.toFloat())

        impressionButton.setOnClickListener {
            Toast.makeText(applicationContext, "Not Valid when exception occuerd ", Toast.LENGTH_SHORT)
                .show()
        }
        clickButton.setOnClickListener {
            Toast.makeText(applicationContext, "Not Valid when exception occuerd ", Toast.LENGTH_SHORT)
                .show()
        }
    }


    override fun onPlaceholderException(
        campaignId: String?,
        targetViewId: String,
        error: Exception
    ) {
        Logger.d(Constants.TAG, "onPlaceholderException for: $targetViewId | Errro - $error")
        renderError(campaignId, targetViewId, error.toString())
    }

    override fun onRendered(data: WECampaignData) {
        Logger.d(Constants.TAG, "onRendered inside: " + data.targetViewId)
    }


}