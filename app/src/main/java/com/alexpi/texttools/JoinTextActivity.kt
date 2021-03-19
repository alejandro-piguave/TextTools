package com.alexpi.texttools

import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.alexpi.texttools.databinding.ActivityJoinTextBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JoinTextActivity : BaseToolActivity<ActivityJoinTextBinding>() {
    override fun setUp() {
        with(binding){
            joinButton.setOnClickListener {
                val inputText = inputEditText.text.toString()
                val joinChar = joinCharacterEditText.text.toString()
                val deleteBlankLines = deleteBlankLinesCheckbox.isChecked
                val trimLines = trimLinesCheckbox.isChecked
                joinText(inputText, joinChar, deleteBlankLines, trimLines)
            }
        }
    }

    override fun getViewBinding(): ActivityJoinTextBinding = ActivityJoinTextBinding.inflate(layoutInflater)

    override fun getResultString(): String = binding.resultLabel.text.toString()


    private fun joinText(inputText: String, joinChar: String, deleteBlankLines: Boolean, trimLines: Boolean) {
        binding.progressView.isVisible = true
        lifecycleScope.launch(Dispatchers.Default) {
            var stringList = inputText.split("\n")
            if(deleteBlankLines)
                stringList = stringList.filter { it.isNotEmpty() }
            if(trimLines)
                stringList = stringList.map { it.trim() }
            val resultText: String = stringList.joinToString(separator = joinChar)

            withContext(Dispatchers.Main){
                binding.resultLabel.text = resultText
                binding.progressView.isVisible = false
            }
        }
    }
}