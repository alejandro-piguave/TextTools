package com.alexpi.texttools.fragment

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.alexpi.texttools.R
import com.alexpi.texttools.base.BaseToolFragment
import com.alexpi.texttools.custom.*
import com.alexpi.texttools.databinding.FragmentFilterTextBinding
import com.alexpi.texttools.extension.lineByLineFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.PatternSyntaxException

class FilterTextFragment : BaseToolFragment<FragmentFilterTextBinding>() {

    override fun setUp() {
        with(binding){
            filterButton.setOnClickListener {
                val inputText = inputEditText.text.toString()
                val reverseFilterMode = reverseFilterModeCheckbox.isChecked
                when(patternOptionsRadioGroup.checkedRadioButtonId){
                    R.id.textPatternRadioButton -> {
                        val textPattern = textPatternEditText.text.toString()
                        if(textPattern.isEmpty()) textPatternField.showError(getString(R.string.invalid_text_pattern))
                        else filterTextLines(inputText,
                            TextFilter(textPattern), reverseFilterMode)
                    }
                    R.id.regexRadioButton -> {
                        try {
                            filterTextLines(inputText, RegexFilter(Regex(regexEditText.text.toString())), reverseFilterMode)
                        } catch (exception: PatternSyntaxException) {
                            regexField.showError(getString(R.string.invalid_regex))
                        }
                    }
                }
            }

            textPatternRadioButton.setOnCheckedChangeListener { _, isChecked ->
                textPatternField.isVisible = isChecked
            }

            regexRadioButton.setOnCheckedChangeListener { _, isChecked ->
                regexField.isVisible = isChecked
            }
        }
    }

    private fun filterTextLines(text: String, filterPattern: FilterPattern, reverseFilterMode: Boolean) {
        binding.progressView.isVisible = true
        lifecycleScope.launch(Dispatchers.Default) {
            val resultText = when(filterPattern){
                is TextFilter -> if(reverseFilterMode) text.lineByLineFilter { !it.contains(filterPattern.text) }
                else text.lineByLineFilter { it.contains(filterPattern.text) }
                is RegexFilter -> if(reverseFilterMode) text.lineByLineFilter { !it.contains(filterPattern.regex) }
                else text.lineByLineFilter { it.contains(filterPattern.regex) }
            }

            withContext(Dispatchers.Main){
                binding.resultLabel.text = resultText
                binding.progressView.isVisible = false
            }
        }
    }
    override fun getViewBinding(container: ViewGroup?): FragmentFilterTextBinding = FragmentFilterTextBinding.inflate(layoutInflater, container, false)

    override fun getResultString(): String = binding.resultLabel.text.toString()
}