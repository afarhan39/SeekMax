package com.example.seek_max.ui.joblist.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seek_max.ActiveJobsQuery
import com.example.seek_max.databinding.ListJobItemBinding
import com.example.seek_max.util.setOnSingleClickListener

class JobListAdapter internal constructor(
    val onClickJob: (jobId: String) -> Unit
) :
    RecyclerView.Adapter<JobListAdapter.ViewHolder>() {

    private lateinit var ctx: Context
    private var list: MutableList<ActiveJobsQuery.Job> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemListBinding = ListJobItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        ctx = parent.context
        return ViewHolder(itemListBinding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<ActiveJobsQuery.Job>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvJobTitle.text = "${item._id}: ${item.positionTitle}"
        holder.binding.tvJobDesc.text = item.description
        holder.binding.root.setOnSingleClickListener {
            item._id?.let {
                onClickJob(it)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: ListJobItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}