package com.example.seek_max.common

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seek_max.databinding.ListUserDetailItemBinding

class CommonDetailsAdapter internal constructor() :
    RecyclerView.Adapter<CommonDetailsAdapter.ViewHolder>() {

    private lateinit var ctx: Context
    private var list: MutableList<CommonDetailsListItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemListBinding = ListUserDetailItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        ctx = parent.context
        return ViewHolder(itemListBinding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<CommonDetailsListItem>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvTitle.text = item.title
        holder.binding.tvDesc.text = item.desc
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: ListUserDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

data class CommonDetailsListItem(
    val title: String,
    val desc: String
)