package com.alexpi.texttools.fragment

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.alexpi.texttools.R
import com.alexpi.texttools.base.BaseToolFragment
import com.alexpi.texttools.databinding.FragmentAddLeftPaddingBinding
import com.alexpi.texttools.extension.lineByLineTransform
import com.alexpi.texttools.extension.padStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddLeftPaddingFragment : BaseToolFragment<FragmentAddLeftPaddingBinding>() {
    override fun setUp() {
        with(binding){
            addLeftPaddingButton.setOnClickListener {
                val textLength = textLengthEditText.text.toString().toIntOrNull() ?: -1

                if(textLength < 0 ){
                    textLengthField.showError(getString(R.string.invalid_length))
                }else {
                    val inputText = inputEditText.text.toString()
                    val leftPaddingString = leftPaddingCharEditText.text.toString()
                    val lineByLinePadding = lineByLinePaddingCheckbox.isChecked

                    addLeftPadding(inputText, textLength, leftPaddingString, lineByLinePadding)
                }
            }
        }
    }

    private fun addLeftPadding(text: String, textLength: Int, paddingString: String, lineByLinePadding: Boolean) {
        binding.progressView.isVisible = true
        lifecycleScope.launch(Dispatchers.Default) {
            val resultText = if(lineByLinePadding) text.lineByLineTransform { it.padStart(textLength, paddingString) } else text.padStart(textLength, paddingString)

            withContext(Dispatchers.Main){
                binding.resultLabel.text = resultText
                binding.progressView.isVisible = false
            }
        }
    }

    override fun getResultString(): String  = binding.resultLabel.text.toString()
    override fun getViewBinding(container: ViewGroup?): FragmentAddLeftPaddingBinding = FragmentAddLeftPaddingBinding.inflate(layoutInflater, container, false)
}