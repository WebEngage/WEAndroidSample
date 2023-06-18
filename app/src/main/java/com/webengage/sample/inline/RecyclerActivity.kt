package com.webengage.sample.inline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webengage.sample.R
import com.webengage.sample.Utils.Constants
import com.webengage.sample.Utils.Utils
import com.webengage.sample.inline.model.DataModel
import com.webengage.sample.inline.model.ScreenModel
import com.webengage.sample.inline.model.ViewModel
import com.webengage.sdk.android.Logger
import com.webengage.sdk.android.WebEngage

// Opens when screen is marked with recyclerView checkbox
class RecyclerActivity : AppCompatActivity() {
    private lateinit var screenModelData: ScreenModel
    private var viewScreenModelList = ArrayList<ScreenModel>()
    private var listSize: Int = 0
    private var screenName: String = ""
    var eventName: String = ""
    var viewRegistry: ArrayList<ViewModel> = ArrayList<ViewModel>()
    private val dataModel = DataModel.getInstance()
    private lateinit var trackEventText: TextView
    private lateinit var trackEvent: Button
    private lateinit var navigationButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // screenData of particular screen passed to render screen
        val ss: String = intent.getStringExtra("pageData").toString()
        screenModelData = Utils.convertStringToModel(ss)
        listSize = screenModelData?.listSize!!
        screenName = screenModelData.screenName
        eventName = screenModelData.eventName
        viewRegistry = screenModelData.viewRegistry
        WebEngage.get().analytics().screenNavigated(screenName)
        if (!eventName.isNullOrEmpty()) {
            WebEngage.get().analytics().track(eventName)
        }

        navigationButton = findViewById(R.id.navigation)
        trackEventText = findViewById<EditText>(R.id.trackRecyclerText)
        trackEvent = findViewById<Button>(R.id.trackRecyclerButton)

        navigationButton.setOnClickListener {
            turnOnModal()
        }

        // Tracks Event
        trackEvent.setOnClickListener {
            val eventToTrack = trackEventText.text.toString()
            Logger.d(Constants.TAG, "tracking event - $eventToTrack")
            WebEngage.get().analytics().track(eventToTrack)
            WebEngage.get().analytics().track(eventToTrack)
        }

        // Renders views based on the size provided
        for (i in 0 until listSize) {
            var newScreenModelData = ScreenModel(
                i,
                screenModelData.screenName,
                screenModelData.eventName,
                screenModelData.idName,
                screenModelData.idValue,
                screenModelData.isRecyclerView,
                screenModelData.viewRegistry
            )
            viewScreenModelList.add(newScreenModelData)
        }
        val adapter = ViewModelAdapter(viewScreenModelList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    // Modal with list of screens where user can naviagte to
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
            Toast.makeText(this, "Let's Navigate to $navigationScreen", Toast.LENGTH_SHORT).show()
        }
    }

    // Navigation to screen by passing screen details
    private fun navigateToScreen(screenName: String, builder: AlertDialog) {
        val list = dataModel.getScreenData()
        var isScreenFound = false
        for (entry in list) {
            if (entry.screenName == screenName) {
                if (entry.isRecyclerView) {
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
        if (!isScreenFound) {
            Toast.makeText(this, "Screen Not Found Enter valid screen", Toast.LENGTH_SHORT).show()
        }

    }
}