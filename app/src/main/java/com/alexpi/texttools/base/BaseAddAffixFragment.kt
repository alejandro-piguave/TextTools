package com.alexpi.texttools.base

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.alexpi.texttools.R
import com.alexpi.texttools.custom.AffixMode
import com.alexpi.texttools.databinding.FragmentAddAffixBinding
import com.alexpi.texttools.extension.lineByLineTransform
import com.alexpi.texttools.extension.paragraphTransform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseAddAffixFragment : BaseToolFragment<FragmentAddAffixBinding>() {

    override fun setUp() {
        with(binding){
            lineByLineModeRadioButton.setOnCheckedChangeListener { _, isChecked ->
                skipEmptyLinesCheckbox.isVisible = isChecked
            }
            affixField.hint = getString(affixEditTextHintId)
            addAffixButton.text = getString(addAffixButtonTextId)
            addAffixButton.setOnClickListener {
                val inputText = inputEditText.text.toString()
                val affixText = affixEditText.text.toString()
                val affixMode = when(textAffixModeRadioGroup.checkedRadioButtonId){
                    R.id.singleLineModeRadioButton -> AffixMode.SingleLineMode
                    R.id.lineByLineModeRadioButton -> AffixMode.LineByLineMode
                    else -> AffixMode.ParagraphMode
                }
                val skipEmptyLines = skipEmptyLinesCheckbox.isChecked

                addAffix(inputText, affixText, affixMode, skipEmptyLines)
            }
        }
    }

    abstract val affixEditTextHintId: Int
    abstract val addAffixButtonTextId: Int

    abstract fun addAffix(inputText: String, affix: String): String

    private fun addAffix(inputText: String, affixText: String, affixMode: AffixMode, skipEmptyLines: Boolean){
        binding.progressView.isVisible = true
        lifecycleScope.launch(Dispatchers.Default) {
            val resultText: String = when(affixMode){
                AffixMode.SingleLineMode -> addAffix(inputText, affixText)
                AffixMode.LineByLineMode -> if(skipEmptyLines) inputText.lineByLineTransform { if(it.isEmpty()) it else addAffix(it, affixText)}
                else inputText.lineByLineTransform { addAffix(it, affixText) }
                AffixMode.ParagraphMode -> inputText.paragraphTransform { addAffix(it, affixText) }
            }

            withContext(Dispatchers.Main){
                binding.resultLabel.text = resultText
                binding.progressView.isVisible = false
            }
        }
    }

    override fun getViewBinding(container: ViewGroup?): FragmentAddAffixBinding = FragmentAddAffixBinding.inflate(layoutInflater, container, false)

    override fun getResultString(): String  = binding.resultLabel.text.toString()
}