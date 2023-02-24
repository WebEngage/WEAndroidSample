package com.example.personalization_sample

import ViewModelViewHolder
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personalization_sample.R
import com.example.personalization_sample.model.Model
import com.example.personalization_sample.model.ViewModel

class ViewModelAdapter(private val viewModelList: List<Model>) :
    RecyclerView.Adapter<ViewModelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModelViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewModelViewHolder(view, parent.context)
    }

    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onViewRecycled(holder: ViewModelViewHolder) {
        super.onViewRecycled(holder)
        holder.container.removeAllViews()
    }


    override fun onBindViewHolder(holder: ViewModelViewHolder, position: Int) {
        val viewModel = viewModelList[position]
        holder.bind(viewModel, position)
    }

    override fun getItemCount(): Int {
        return viewModelList.size
    }
}
