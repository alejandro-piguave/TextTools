package com.alexpi.texttools.fragment

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.alexpi.texttools.base.BaseToolFragment
import com.alexpi.texttools.databinding.FragmentRemoveDuplicateLinesBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RemoveDuplicateLinesFragment : BaseToolFragment<FragmentRemoveDuplicateLinesBinding>() {

    override fun setUp() {
        with(binding){
            removeDuplicateLinesButton.setOnClickListener {
                val inputText = inputEditText.text.toString()
                val removeEmptyLines = deleteEmptyLinesCheckbox.isChecked
                removeDuplicateLines(inputText, removeEmptyLines)
            }
        }
    }

    private fun removeDuplicateLines(text: String, removeEmptyLines: Boolean) {
        binding.progressView.isVisible = true
        lifecycleScope.launch(Dispatchers.Default) {
            val resultText = if(removeEmptyLines) text.split("\n").filter{ it.isNotEmpty() }.distinct().joinToString(separator = "\n")
            else text.split("\n").distinct().joinToString(separator = "\n")

            withContext(Dispatchers.Main){
                binding.resultLabel.text = resultText
                binding.progressView.isVisible = false
            }
        }
    }
    override fun getViewBinding(container: ViewGroup?): FragmentRemoveDuplicateLinesBinding = FragmentRemoveDuplicateLinesBinding.inflate(layoutInflater, container, false)

    override fun getResultString(): String = binding.resultLabel.text.toString()
}