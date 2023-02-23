package com.example.personalization_sample

import android.app.ActionBar
import android.app.ActionBar.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalization_sample.model.Model
import com.example.personalization_sample.model.ViewModel
import com.webengage.sdk.android.WebEngage

class RecyclerActivity : AppCompatActivity() {
    private lateinit var modelData: Model
    var viewModelList = ArrayList<Model>()
    var listSize: Int = 0
    var screenName: String = ""
    var eventName: String = ""
    var viewRegistry: ArrayList<ViewModel> = ArrayList<ViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
//        recyclerView.setHasFixedSize(false)

        val ss: String = intent.getStringExtra("pageData").toString()
        modelData = Utils.convertStringToModel(ss)
        listSize = modelData?.listSize!!
        screenName = modelData.screenName
        eventName = modelData.eventName
        viewRegistry = modelData.viewRegistry
        WebEngage.get().analytics().screenNavigated(screenName)

        Log.d("WEP", "Recycler: intent data " + modelData)


        for (i in 0 until listSize) {
            val height = LayoutParams.MATCH_PARENT
            val width = LayoutParams.MATCH_PARENT

            var newModelData = Model(i, modelData.screenName, modelData.eventName, modelData.isRecyclerView,modelData.viewRegistry)
//            listSize=20, screenName=screen1, eventName=tex, isRecyclerView=true, viewRegistry=[ViewModel(position=1, height=0, width=0, propertyId=text_prop)
            viewModelList.add(newModelData)
        }
        Log.d("WEP", "viewModelList " + viewModelList)



        val adapter = ViewModelAdapter(viewModelList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

    }
}