package com.alexpi.texttools.base

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.alexpi.texttools.R
import com.alexpi.texttools.databinding.FragmentAddPaddingBinding
import com.alexpi.texttools.extension.lineByLineTransform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseAddPaddingFragment : BaseToolFragment<FragmentAddPaddingBinding>() {
    abstract val addPaddingButtonTextId: Int
    abstract fun addPadding(inputText: String, textLength: Int, paddingString: String): String

    override fun setUp() {
        with(binding){
            addPaddingButton.text = getString(addPaddingButtonTextId)
            addPaddingButton.setOnClickListener {
                val textLength = textLengthEditText.text.toString().toIntOrNull() ?: -1

                if(textLength < 0 ){
                    textLengthField.showError(getString(R.string.invalid_length))
                }else {
                    val inputText = inputEditText.text.toString()
                    val leftPaddingString = paddingCharEditText.text.toString()
                    val lineByLinePadding = lineByLinePaddingCheckbox.isChecked

                    addPadding(inputText, textLength, leftPaddingString, lineByLinePadding)
                }
            }
        }
    }

    private fun addPadding(text: String, textLength: Int, paddingString: String, lineByLinePadding: Boolean) {
        binding.progressView.isVisible = true
        lifecycleScope.launch(Dispatchers.Default) {
            val resultText = if(lineByLinePadding) text.lineByLineTransform { addPadding(text, textLength, paddingString) } else addPadding(text, textLength, paddingString)

            withContext(Dispatchers.Main){
                binding.resultLabel.text = resultText
                binding.progressView.isVisible = false
            }
        }
    }

    override fun getViewBinding(container: ViewGroup?): FragmentAddPaddingBinding = FragmentAddPaddingBinding.inflate(layoutInflater, container, false)

    override fun getResultString(): String  = binding.resultLabel.text.toString()
}