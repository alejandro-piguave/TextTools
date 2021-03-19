package com.alexpi.texttools

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.alexpi.texttools.databinding.ActivityTruncateTextBinding
import com.alexpi.texttools.extension.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TruncateTextActivity : BaseToolActivity<ActivityTruncateTextBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding){

            truncateButton.setOnClickListener {
                val truncationLength = truncationLengthEditText.text.toString().toIntOrNull() ?: -1
                val truncationIndicator = if(addTruncationIndicatorCheckbox.isChecked) delimiterEditText.text.toString() else ""
                when {
                    truncationLength < 0 -> {
                        truncationLengthField.showError(getString(R.string.invalid_length))
                    }
                    truncationIndicator.length > truncationLength -> {
                        truncationLengthField.showError(getString(R.string.invalid_truncation_length))
                    }
                    else -> {
                        val inputText = inputEditText.text.toString()
                        val isLeftTruncation = truncationSideRadioGroup.checkedRadioButtonId == R.id.leftSideTruncationRadioButton
                        val lineByLineTruncation = lineByLineTruncatingCheckbox.isChecked
                        truncate(inputText, truncationLength, isLeftTruncation, lineByLineTruncation, truncationIndicator)
                    }
                }
            }

            addTruncationIndicatorCheckbox.setOnCheckedChangeListener { _, isChecked ->
                delimiterField.isVisible = isChecked
            }

            truncationSideLabel.setOnClickListener {
                truncationSideLabel.toggle()
                if(truncationSideRadioGroup.isVisible)
                    truncationSideRadioGroup.collapse()
                else truncationSideRadioGroup.expand()
            }

            lengthAndLinesOptionsLabel.setOnClickListener {
                lengthAndLinesOptionsLabel.toggle()
                if(lengthAndLinesOptionsLayout.isVisible)
                    lengthAndLinesOptionsLayout.collapse()
                else lengthAndLinesOptionsLayout.expand()
            }

            suffixAndAffixOptionsLabel.setOnClickListener {
                suffixAndAffixOptionsLabel.toggle()
                if(suffixAndAffixOptionsLayout.isVisible)
                    suffixAndAffixOptionsLayout.collapse()
                else suffixAndAffixOptionsLayout.expand()
            }

        }
    }

    private fun truncate(inputText: String, truncationLength: Int, isLeftTruncation: Boolean, lineByLineTruncation: Boolean, truncationIndicator: String){
        binding.progressView.isVisible = true
        lifecycleScope.launch(Dispatchers.Default) {
            val resultText = when {
                lineByLineTruncation -> inputText.split("\n")
                    .joinToString(separator = "\n") {
                        if(isLeftTruncation) it.takeLastAndAddPrefix(truncationLength, truncationIndicator)
                        else it.takeAndAddSuffix(truncationLength, truncationIndicator)
                    }
                isLeftTruncation -> inputText.takeLastAndAddPrefix(truncationLength, truncationIndicator)
                else -> inputText.takeAndAddSuffix(truncationLength, truncationIndicator)
            }
            withContext(Dispatchers.Main){
                binding.resultLabel.text = resultText
                binding.progressView.isVisible = false
            }
        }
    }

    override fun getViewBinding(): ActivityTruncateTextBinding = ActivityTruncateTextBinding.inflate(layoutInflater)

    override fun getResultString(): String = binding.resultLabel.text.toString()
}