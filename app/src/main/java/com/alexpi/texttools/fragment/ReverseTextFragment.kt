package com.alexpi.texttools.fragment

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.alexpi.texttools.base.BaseToolFragment
import com.alexpi.texttools.databinding.FragmentReverseTextBinding
import com.alexpi.texttools.extension.lineByLineTransform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReverseTextFragment : BaseToolFragment<FragmentReverseTextBinding>() {

    override fun getViewBinding(container: ViewGroup?): FragmentReverseTextBinding = FragmentReverseTextBinding.inflate(layoutInflater, container, false)

    override fun getResultString(): String = binding.resultLabel.text.toString()

    override fun setUp() {
        with(binding){
            reverseButton.setOnClickListener {
                val inputText = inputEditText.text.toString()
                val lineByLine = lineByLineModeCheckbox.isChecked
                reverse(inputText, lineByLine)
            }
        }
    }

    private fun reverse(inputText: String, lineByLine: Boolean){
        binding.progressView.isVisible = true
        lifecycleScope.launch(Dispatchers.Default) {
            val resultText = if (lineByLine) inputText.lineByLineTransform{ it.reversed() } else inputText.reversed()

            withContext(Dispatchers.Main){
                binding.resultLabel.text = resultText
                binding.progressView.isVisible = false
            }
        }
    }
}