package com.alexpi.texttools.custom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexpi.texttools.databinding.ToolItemBinding

class TextToolsAdapter(private val toolList: List<String>,
                       private val itemClickListener: ((Int, String)->Unit)) : RecyclerView.Adapter<TextToolsAdapter.ToolItemHolder>() {


    inner class ToolItemHolder(val toolItemBinding: ToolItemBinding) : RecyclerView.ViewHolder(toolItemBinding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ToolItemHolder(ToolItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ToolItemHolder, position: Int) {
        holder.toolItemBinding.toolName.text = toolList[position]
        holder.toolItemBinding.toolCard.setOnClickListener {
            itemClickListener.invoke(position, toolList[position])
        }
    }

    override fun getItemCount() = toolList.size
}