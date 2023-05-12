package com.amanullah.logchuckerapp.service.window

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amanullah.logchuckerapp.databinding.ItemSimpleTextBinding
import com.amanullah.logchuckerapp.service.window.LoggerAdapter.LoggerViewHolder

class LoggerAdapter : RecyclerView.Adapter<LoggerViewHolder>() {

    inner class LoggerViewHolder(val binding: ItemSimpleTextBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, diffCallback)
    var items: List<String>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoggerViewHolder {
        val binding = ItemSimpleTextBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return LoggerViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: LoggerViewHolder, position: Int) {
        val item = items[position]
        holder.binding.textView.text = item
    }
}
