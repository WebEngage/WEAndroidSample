package com.webengage.sample.inline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.webengage.sample.R
import com.webengage.sample.inline.model.ScreenModel

class ViewModelAdapter(private val viewScreenModelList: List<ScreenModel>) :
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
        val viewModel = viewScreenModelList[position]
        holder.bind(viewModel, position)
    }

    override fun getItemCount(): Int {
        return viewScreenModelList.size
    }
}
