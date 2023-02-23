package com.example.personalization_sample

import ViewModelViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personalization_sample.R
import com.example.personalization_sample.model.Model
import com.example.personalization_sample.model.ViewModel

class ViewModelAdapter(private val viewModelList: List<Model>) :
    RecyclerView.Adapter<ViewModelViewHolder>() {
//            listSize=20, screenName=screen1, eventName=tex, isRecyclerView=true, viewRegistry=[ViewModel(position=1, height=0, width=0, propertyId=text_prop)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModelViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewModelViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: ViewModelViewHolder, position: Int) {
        val viewModel = viewModelList[position]
        holder.bind(viewModel, position)
    }

    override fun getItemCount(): Int {
        return viewModelList.size
    }
}
