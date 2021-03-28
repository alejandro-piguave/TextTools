package com.alexpi.texttools.fragment

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.alexpi.texttools.base.BaseToolFragment
import com.alexpi.texttools.databinding.FragmentRemoveEmptyLinesBinding
import com.alexpi.texttools.extension.lineByLineFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RemoveEmptyLinesFragment: BaseToolFragment<FragmentRemoveEmptyLinesBinding>() {

    override fun setUp() {
        with(binding){
            removeEmptyLinesButton.setOnClickListener {
                val inputText = inputEditText.text.toString()
                val removeBlankLines = deleteBlankLinesCheckbox.isChecked
                removeEmptyLines(inputText, removeBlankLines)
            }
        }
    }

    private fun removeEmptyLines(text: String, removeBlankLines: Boolean) {
        binding.progressView.isVisible = true
        lifecycleScope.launch(Dispatchers.Default) {
            val resultText = if(removeBlankLines) text.lineByLineFilter { it.isNotBlank() }
            else text.lineByLineFilter { it.isNotEmpty() }

            withContext(Dispatchers.Main){
                binding.resultLabel.text = resultText
                binding.progressView.isVisible = false
            }
        }
    }
    override fun getViewBinding(container: ViewGroup?): FragmentRemoveEmptyLinesBinding = FragmentRemoveEmptyLinesBinding.inflate(layoutInflater, container, false)

    override fun getResultString(): String = binding.resultLabel.text.toString()
}