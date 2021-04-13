package com.alexpi.texttools.fragment

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexpi.texttools.R
import com.alexpi.texttools.base.BaseFragment
import com.alexpi.texttools.custom.TextToolsAdapter
import com.alexpi.texttools.custom.ToolItemCallback
import com.alexpi.texttools.databinding.FragmentToolListBinding

class ToolListFragment: BaseFragment<FragmentToolListBinding>() {
    private lateinit var toolItemCallback: ToolItemCallback

    override fun getViewBinding(container: ViewGroup?): FragmentToolListBinding  = FragmentToolListBinding.inflate(layoutInflater)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as? ToolItemCallback)?.let {
            toolItemCallback = it
        } ?: throw ClassCastException("Activity doesn't implement ToolItemCallback.")
    }

    override fun setUp() {
        with(binding){
            val toolsList = resources.getStringArray(R.array.text_tools)
            val adapter =
                TextToolsAdapter(toolsList.toList(), toolItemCallback)
            toolsView.adapter = adapter
            toolsView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            toolsView.setHasFixedSize(true)
        }
    }

}