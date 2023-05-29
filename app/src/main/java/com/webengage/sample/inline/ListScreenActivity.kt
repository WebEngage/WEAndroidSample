package com.webengage.sample.inline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.webengage.sample.R
import com.webengage.sample.Utils.Utils
import com.webengage.sample.model.DataModel
import com.webengage.sample.model.Model
import com.webengage.sdk.android.WebEngage

class ListScreenActivity : AppCompatActivity() {
    private lateinit var addScreenButton: Button
    val dataModel = DataModel.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_screen)
        val arr = Utils.getModelData()
        DataModel.getInstance().updateData(arr)

        addScreenButton = findViewById<Button>(R.id.addScreen)
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

    private fun createList() {
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
        layoutParams.setMargins(0, 50, 0, 50)
        itemListLayout.layoutParams = layoutParams

        for (entry in list) {
            val itemLayout = LinearLayout(this)
            itemLayout.orientation = LinearLayout.VERTICAL
            val itemLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            itemLayoutParams.setMargins(0, 100, 0, 50)
            itemLayout.layoutParams = itemLayoutParams

            // Add the screen name TextView
            val screenNameTextView = TextView(this)
            screenNameTextView.text = "Screen Name ${entry.screenName}"
            screenNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
            screenNameTextView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            itemLayout.addView(screenNameTextView)

            // Add the value TextView
            val valueTextView = TextView(this)
            valueTextView.text = "List size ${entry.listSize}"
            screenNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
            valueTextView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            itemLayout.addView(valueTextView)

            // Add the value TextView
            val eventTextView = TextView(this)
            eventTextView.text = "Event Name ${entry.eventName}"
            screenNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
            eventTextView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            itemLayout.addView(eventTextView)

            val itemButtonLayout = LinearLayout(this)
            itemButtonLayout.orientation = LinearLayout.VERTICAL
            val itemButtonLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            itemButtonLayout.layoutParams = itemButtonLayoutParams

            itemButtonLayoutParams.setMargins(0, 50, 0, 50)
            // Add the edit button
            val openButton = Button(this)
            openButton.text = "Open"
            val layoutParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParam.gravity = Gravity.END
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
            deleteLayoutParam.gravity = Gravity.END
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
        scrollView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        scrollView.addView(itemListLayout)
        container.addView(scrollView)
    }

    private fun openScreen(entry: Model) {
        if (entry.isRecyclerView) {
            val intent = Intent(this, RecyclerActivity::class.java)
            intent.putExtra("pageData", Utils.convertModelToString(entry))
            startActivity(intent)
        } else {
            val intent = Intent(this, DynamicScreen::class.java)
            intent.putExtra("pageData", Utils.convertModelToString(entry))
            startActivity(intent)
        }
    }

    private fun deleteEntry(screenName: String) {
        dataModel.removeScreenEntry(screenName)
        createList()
    }

    override fun onResume() {
        super.onResume()
        createList()
    }
}