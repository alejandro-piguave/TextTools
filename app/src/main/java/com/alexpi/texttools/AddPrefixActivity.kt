package com.alexpi.texttools

import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.alexpi.texttools.custom.*
import com.alexpi.texttools.databinding.ActivityAddPrefixBinding
import com.alexpi.texttools.extension.lineByLineTransform
import com.alexpi.texttools.extension.paragraphTransform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddPrefixActivity : BaseToolActivity<ActivityAddPrefixBinding>() {

    override fun setUp() {

        with(binding){
            lineByLineModeRadioButton.setOnCheckedChangeListener { _, isChecked ->
                skipEmptyLinesCheckbox.isVisible = isChecked
            }
            addPrefixButton.setOnClickListener {
                val inputText = inputEditText.text.toString()
                val prefixText = prefixEditText.text.toString()
                val prefixMode = when(textPrefixModeRadioGroup.checkedRadioButtonId){
                    R.id.singleLineModeRadioButton -> AffixMode.SingleLineMode
                    R.id.lineByLineModeRadioButton -> AffixMode.LineByLineMode
                    else -> AffixMode.ParagraphMode
                }
                val skipEmptyLines = skipEmptyLinesCheckbox.isChecked

                addPrefix(inputText, prefixText, prefixMode, skipEmptyLines)
            }
        }
    }

    private fun addPrefix(inputText: String, prefixText: String, prefixMode: AffixMode, skipEmptyLines: Boolean){
        binding.progressView.isVisible = true
        lifecycleScope.launch(Dispatchers.Default) {
            val resultText: String = when(prefixMode){
                AffixMode.SingleLineMode -> prefixText + inputText
                AffixMode.LineByLineMode -> if(skipEmptyLines) inputText.lineByLineTransform { if(it.isEmpty()) it else prefixText + it }
                else inputText.lineByLineTransform { prefixText + it }
                AffixMode.ParagraphMode -> inputText.paragraphTransform { prefixText + it }
            }

            withContext(Dispatchers.Main){
                binding.resultLabel.text = resultText
                binding.progressView.isVisible = false
            }
        }
    }

    override fun getViewBinding(): ActivityAddPrefixBinding = ActivityAddPrefixBinding.inflate(layoutInflater)

    override fun getResultString(): String  = binding.resultLabel.text.toString()
}