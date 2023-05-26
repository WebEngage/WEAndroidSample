package com.example.personalization_sample.Inline

import android.app.ActionBar.LayoutParams
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalization_sample.R
import com.example.personalization_sample.Utils.Constants
import com.example.personalization_sample.Utils.Utils
import com.example.personalization_sample.model.DataModel
import com.example.personalization_sample.model.Model
import com.example.personalization_sample.model.ViewModel
import com.webengage.sdk.android.Logger
import com.webengage.sdk.android.WebEngage

class RecyclerActivity : AppCompatActivity() {
    private lateinit var modelData: Model
    private var viewModelList = ArrayList<Model>()
    private var listSize: Int = 0
    private var screenName: String = ""
    var eventName: String = ""
    var viewRegistry: ArrayList<ViewModel> = ArrayList<ViewModel>()
    private val dataModel = DataModel.getInstance()
    private lateinit var trackEventText: TextView
    private lateinit var trackRandom: Button
    private lateinit var navigationButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val ss: String = intent.getStringExtra("pageData").toString()
        modelData = Utils.convertStringToModel(ss)
        listSize = modelData?.listSize!!
        screenName = modelData.screenName
        eventName = modelData.eventName
        viewRegistry = modelData.viewRegistry
        WebEngage.get().analytics().screenNavigated(screenName)
        if (!eventName.isNullOrEmpty()) {
            WebEngage.get().analytics().track(eventName)
        }

        navigationButton = findViewById(R.id.navigation)
        trackEventText = findViewById<EditText>(R.id.trackRecyclerText)
        trackRandom = findViewById<Button>(R.id.trackRecyclerButton)

        navigationButton.setOnClickListener {
            turnOnModal()
        }

        trackRandom.setOnClickListener {
            val eventToTrack = trackEventText.text.toString()
            Logger.d(Constants.TAG, "tracking event - $eventToTrack")
            WebEngage.get().analytics().track(eventToTrack)
        }

        Logger.d(Constants.TAG, "Recycler: intent data " + modelData)


        for (i in 0 until listSize) {

            var newModelData = Model(
                i,
                modelData.screenName,
                modelData.eventName,
                modelData.idName,
                modelData.idValue,
                modelData.isRecyclerView,
                modelData.viewRegistry
            )
            viewModelList.add(newModelData)
        }
        val adapter = ViewModelAdapter(viewModelList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
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
            Toast.makeText(this, "Let's Navigate to $navigationScreen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToScreen(screenName: String, builder: AlertDialog) {
        val list = dataModel.getData()
        var isScreenFound = false
        for (entry in list) {
            if (entry.screenName.equals(screenName)) {
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