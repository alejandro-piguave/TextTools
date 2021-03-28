package com.alexpi.texttools

import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.alexpi.texttools.databinding.ActivityTrimTextBinding
import com.alexpi.texttools.extension.lineByLineTransform
import com.alexpi.texttools.extension.trim
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrimTextActivity : BaseToolActivity<ActivityTrimTextBinding>() {
    override fun setUp() {
        with(binding){
            trimButton.setOnClickListener {
                val inputText = inputEditText.text.toString()
                val leftSideTrimming = leftSideTrimmingCheckbox.isChecked
                val rightSideTrimming = righrSideTrimmingCheckbox.isChecked
                val lineByLineTrimming = lineByLineTrimmingCheckbox.isChecked

                trimText(inputText, leftSideTrimming, rightSideTrimming, lineByLineTrimming)
            }
        }
    }

    private fun trimText(inputText: String, leftSideTrimming: Boolean, rightSideTrimming: Boolean, lineByLineTrimming: Boolean){
        binding.progressView.isVisible = true
        lifecycleScope.launch(Dispatchers.Default) {
            val resultText = if(lineByLineTrimming) inputText.lineByLineTransform{
                    it.trim(leftSideTrimming, rightSideTrimming)
                } else inputText.trim(leftSideTrimming, rightSideTrimming)

            withContext(Dispatchers.Main){
                binding.resultLabel.text = resultText
                binding.progressView.isVisible = false
            }
        }
    }

    override fun getViewBinding(): ActivityTrimTextBinding = ActivityTrimTextBinding.inflate(layoutInflater)

    override fun getResultString(): String = binding.resultLabel.text.toString()
}