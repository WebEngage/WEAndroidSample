package com.example.personalization_sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.personalization_sample.model.DataModel
import com.example.personalization_sample.model.Model

class CustomScreen : AppCompatActivity() {
    private lateinit var addScreenButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemList: ArrayList<String>
    val dataModel = DataModel.getInstance()

    private lateinit var listView: ScrollView
    private lateinit var listAdapter: ArrayAdapter<String>
//    private lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_screen)
//        val inflater = LayoutInflater.from(applicationContext)
//        val layout = inflater.inflate(R.layout.activity_custom_screen, null)
//        val container = findViewById<LinearLayout>(R.id.cusomLinearLayout)




        val customView = layoutInflater.inflate(R.layout.activity_custom_screen, null)
        addScreenButton = findViewById<Button>(R.id.addScreen)
//        listView = findViewById(R.id.screen_list_scrollView)

//        val items = arrayOf("Item 1", "Item 2", "Item 3")
//        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
//        listView.adapter = listAdapter

        addScreenButton.setOnClickListener {
            Log.d("Ak", "add screen clicked")
//            Intent intent = Inten
            val intent = Intent(this, ScreenDetails::class.java)
            startActivity(intent)
//            Toast.makeText(applicationContext, " add screen", Toast.LENGTH_LONG).show()
//            createList(container)
            createList()

        }
    }

    fun createList() {
        Log.d("AKSHAY", "create List called inside custom screen")
        val container = findViewById<LinearLayout>(R.id.cusomLinearLayout)
        container.removeAllViews()

        val list = dataModel.getData()
        Log.d("AKSHAY", "list inside custom screen "+list)

        val itemListLayout = LinearLayout(this)
        itemListLayout.orientation = LinearLayout.VERTICAL
        itemListLayout.setBackgroundResource(R.color.teal_700)
        itemListLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        for (entry in list) {
            val itemLayout = LinearLayout(this)

            itemLayout.orientation = LinearLayout.VERTICAL
            itemLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//            itemLayout.setBackgroundResource(R.color.teal_200) // set the background to a border drawable

            // Add the screen name TextView
            val screenNameTextView = TextView(this)
            screenNameTextView.text = "List size ${entry.listSize}"
            screenNameTextView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            itemLayout.addView(screenNameTextView)

            // Add the value TextView
            val valueTextView = TextView(this)
            valueTextView.text = "Screen Name ${entry.screenName}"
            valueTextView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            itemLayout.addView(valueTextView)

            // Add the value TextView
            val eventTextView = TextView(this)
            eventTextView.text = "Event Name ${entry.eventName}"
            eventTextView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            itemLayout.addView(eventTextView)



            val itemButtonLayout = LinearLayout(this)
            itemButtonLayout.orientation = LinearLayout.VERTICAL
            // Add the edit button
            val openButton = Button(this)
            openButton.text = "Open"
            val layoutParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParam.gravity = Gravity.END // TODO - Move button to Right
            openButton.layoutParams = layoutParam
            openButton.setOnClickListener {
                openScreen(entry)
            }
            itemButtonLayout.addView(openButton)

            // Add the edit button
            val deleteButton = Button(this)
            deleteButton.text = "Delete"
            val deleteLayoutParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            deleteLayoutParam.gravity = Gravity.END // TODO - Move button to Right
            deleteButton.layoutParams = layoutParam
            deleteButton.setOnClickListener {
                deleteEntry(entry.screenName)
            }
//            openButton.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            itemButtonLayout.addView(deleteButton)

            // Add the list item to the parent LinearLayout
            itemListLayout.addView(itemLayout)
            itemListLayout.addView(itemButtonLayout)
        }

// Add the horizontal LinearLayout to the parent layout inside a ScrollView
        val scrollView = ScrollView(this)
        scrollView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        scrollView.addView(itemListLayout)
//        container.addView(scrollView)
        container.addView(scrollView)
    }

    fun openScreen(entry: Model) {
        val intent = Intent(this, DynamicScreen::class.java)
        intent.putExtra("pageData", Utils.convertModelToString(entry))
        startActivity(intent)
    }

    fun deleteEntry(screenName: String) {
        dataModel.removeScreenEntry(screenName)
        createList()
    }

    override fun onResume() {
        super.onResume()
        Log.d("AKC", "Resumed")
        Log.d("AKC", " size - "+dataModel.getListSize())
        val list = dataModel.getData()
        if (list != null) {
            for (item in list) {
                //            println(item)
                Log.d("AKC", "item - "+item)
            }
        } else {
            Log.d("AKC", "no list  - ")
        }
        createList()
    }
}