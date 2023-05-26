package com.example.personalization_sample.Inline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personalization_sample.R
import com.example.personalization_sample.Utils.Utils
import com.example.personalization_sample.model.DataModel
import com.example.personalization_sample.model.Model
import com.webengage.sdk.android.WebEngage

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
            val intent = Intent(this, ScreenDetails::class.java)
            startActivity(intent)
            createList()

        }
    }

    override fun onStart() {
        super.onStart()
        WebEngage.get().analytics().screenNavigated("Custom-screen")
    }

    fun createList() {
        val container = findViewById<LinearLayout>(R.id.cusomLinearLayout)
        container.removeAllViews()

        val list = dataModel.getData()

        val itemListLayout = LinearLayout(this)
        itemListLayout.orientation = LinearLayout.VERTICAL
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
//        itemListLayout.setBackgroundResource(R.color.card)

        layoutParams.setMargins(0,50,0,50)
        itemListLayout.layoutParams = layoutParams

        for (entry in list) {
            val itemLayout = LinearLayout(this)

            itemLayout.orientation = LinearLayout.VERTICAL
            val itemLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            itemLayoutParams.setMargins(0,100,0,50)
            itemLayout.layoutParams = itemLayoutParams

            // Add the screen name TextView
            val screenNameTextView = TextView(this)

            screenNameTextView.text = "Screen Name ${entry.screenName}"
            screenNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)

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
            itemButtonLayout.addView(deleteButton)

            itemListLayout.addView(itemLayout)
            itemListLayout.addView(itemButtonLayout)
        }


// Add the horizontal LinearLayout to the parent layout inside a ScrollView
        val scrollView = ScrollView(this)
        scrollView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        scrollView.addView(itemListLayout)
        container.addView(scrollView)
    }

    fun openScreen(entry: Model) {
        if(entry.isRecyclerView) {
            val intent = Intent(this, RecyclerActivity::class.java)
            intent.putExtra("pageData", Utils.convertModelToString(entry))
            startActivity(intent)
        } else {
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
        createList()
    }
}