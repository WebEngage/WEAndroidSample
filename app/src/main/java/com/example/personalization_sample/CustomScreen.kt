package com.example.personalization_sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
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
import androidx.core.view.marginTop
import androidx.core.view.setMargins
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

//        val itemTopLevelLayout = LinearLayout(this)
//        itemTopLevelLayout.orientation = LinearLayout.HORIZONTAL
//        itemTopLevelLayout.setBackgroundResource(R.color.teal_700)
//
////        itemMarginLayoutLayoutParams.setMargins(0,50,0,50)
//        itemTopLevelLayout.layoutParams = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.WRAP_CONTENT
//        )
//

        val itemListLayout = LinearLayout(this)
        itemListLayout.orientation = LinearLayout.VERTICAL
//        layoutParams.setMargins(16, 16, 16, 16)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        itemListLayout.setBackgroundResource(R.color.teal_200) // set the background to a border drawable

        layoutParams.setMargins(0,50,0,50)
        itemListLayout.layoutParams = layoutParams


//        2 Margin Layout


        for (entry in list) {
            val itemLayout = LinearLayout(this)

            itemLayout.orientation = LinearLayout.VERTICAL
            val itemLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
//            itemLayout.setBackgroundResource(R.color.teal_700)

            itemLayoutParams.setMargins(0,100,0,50)
            itemLayout.layoutParams = itemLayoutParams
//            itemLayout.setBackgroundResource(R.color.teal_200) // set the background to a border drawable

            // Add the screen name TextView
            val screenNameTextView = TextView(this)

            screenNameTextView.text = "Screen Name ${entry.screenName}"
            screenNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)

            screenNameTextView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            itemLayout.addView(screenNameTextView)

            // Add the value TextView
            val valueTextView = TextView(this)
            valueTextView.text = "List size ${entry.listSize}"
            screenNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)

            valueTextView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            itemLayout.addView(valueTextView)

            // Add the value TextView
            val eventTextView = TextView(this)
            eventTextView.text = "Event Name ${entry.eventName}"
            screenNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)

            eventTextView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            itemLayout.addView(eventTextView)



            val itemButtonLayout = LinearLayout(this)
            itemButtonLayout.orientation = LinearLayout.VERTICAL
            val itemButtonLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
//            itemButtonLayout.setBackgroundResource(R.color.teal_700)
            itemButtonLayout.layoutParams = itemButtonLayoutParams

            itemButtonLayoutParams.setMargins(0,50,0,50)
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
//        itemTopLevelLayout.addView(itemListLayout)


// Add the horizontal LinearLayout to the parent layout inside a ScrollView
        val scrollView = ScrollView(this)
        scrollView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//        scrollView.addView(itemTopLevelLayout)
        scrollView.addView(itemListLayout)
//        container.addView(scrollView)
        container.addView(scrollView)
    }

    fun openScreen(entry: Model) {
        if(entry.isRecyclerView) {
            Log.d("WEP", "opening DynamicScreen")
            val intent = Intent(this, RecyclerActivity::class.java)
            intent.putExtra("pageData", Utils.convertModelToString(entry))
            startActivity(intent)
        } else {
            Log.d("WEP", "opening Recycler screen")

            val intent = Intent(this, DynamicScreen::class.java)
            intent.putExtra("pageData", Utils.convertModelToString(entry))
            startActivity(intent)
        }
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