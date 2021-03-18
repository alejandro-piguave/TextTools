package com.alexpi.texttools

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.alexpi.texttools.databinding.ActivityTextSplitterBinding
import com.alexpi.texttools.extension.collapse
import com.alexpi.texttools.extension.expand
import com.alexpi.texttools.extension.insertPeriodically
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.PatternSyntaxException

class TextSplitterActivity : BaseToolActivity<ActivityTextSplitterBinding>() {

    override fun getViewBinding(): ActivityTextSplitterBinding  = ActivityTextSplitterBinding.inflate(layoutInflater)

    override fun getResultString(): String = binding.resultLabel.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding){
            splitButton.setOnClickListener {
                val inputText = inputEditText.text.toString()
                val outputChar = if(outputCharacterEditText.text.toString().isEmpty()) "\n" else outputCharacterEditText.text.toString()
                val charBeforeChunk = charBeforeChunkEditText.text.toString()
                val charAfterChunk = charAfterChunkEditText.text.toString()
                when(splitSeparatorOptionsRadioGroup.checkedRadioButtonId){
                    R.id.symbolRadioButton -> {
                        val delimiter = symbolEditText.text.toString()
                        if(delimiter.isEmpty()) symbolField.showError(getString(R.string.invalid_symbol))
                        else splitText(inputText,
                                SymbolSeparator(delimiter), outputChar, charBeforeChunk, charAfterChunk)
                    }
                    R.id.regexRadioButton -> {
                        try {
                            splitText(inputText, RegexSeparator(Regex(regexEditText.text.toString())), outputChar, charBeforeChunk, charAfterChunk)
                        } catch (exception: PatternSyntaxException) {
                            regexField.showError(getString(R.string.invalid_regex))
                        }
                    }
                    R.id.lengthRadioButton -> {
                        val lengthValue = lengthEditText.text.toString().toIntOrNull() ?: -1
                        if(lengthValue <= 0) lengthField.showError(getString(R.string.invalid_length))
                        else splitText(inputText, LengthSeparator(lengthValue), outputChar, charBeforeChunk, charAfterChunk)
                    }
                }
            }
            symbolRadioButton.setOnCheckedChangeListener { _, isChecked ->
                symbolField.isVisible = isChecked
            }

            regexRadioButton.setOnCheckedChangeListener { _, isChecked ->
                regexField.isVisible = isChecked
            }

            lengthRadioButton.setOnCheckedChangeListener { _, isChecked ->
                lengthField.isVisible = isChecked
            }

            splitSeparatorOptionsLabel.setOnClickListener {
                splitSeparatorOptionsLabel.toggle()
                if(splitSeparatorOptionsRadioGroup.isVisible)
                    splitSeparatorOptionsRadioGroup.collapse()
                else splitSeparatorOptionsRadioGroup.expand()
            }

            outputSeparatorOptionsLabel.setOnClickListener {
                outputSeparatorOptionsLabel.toggle()
                if(outputSeparatorOptionsLayout.isVisible)
                    outputSeparatorOptionsLayout.collapse()
                else outputSeparatorOptionsLayout.expand()
            }
        }
    }


    private fun splitText(inputText: String, splitSeparator: SplitSeparator, outputChar: String, charBeforeChunk: String, charAfterChunk: String){
        binding.progressView.isVisible = true
        lifecycleScope.launch(Dispatchers.Default) {
            val innerString = charAfterChunk + outputChar + charBeforeChunk
            val resultText: String = charBeforeChunk  + when(splitSeparator){
                is SymbolSeparator -> inputText.replace(splitSeparator.symbol, innerString)
                is RegexSeparator -> inputText.replace(splitSeparator.regex, innerString)
                is LengthSeparator -> inputText.insertPeriodically(innerString,splitSeparator.length)
            } + charAfterChunk

            withContext(Dispatchers.Main){
                binding.resultLabel.text = resultText
                binding.progressView.isVisible = false
            }
        }
    }
}