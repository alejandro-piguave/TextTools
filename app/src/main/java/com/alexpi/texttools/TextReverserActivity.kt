package com.alexpi.texttools

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.alexpi.texttools.databinding.ActivityTextReverserBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TextReverserActivity : BaseToolActivity<ActivityTextReverserBinding>() {

    override fun getViewBinding(): ActivityTextReverserBinding = ActivityTextReverserBinding.inflate(layoutInflater)

    override fun getResultString(): String = binding.resultLabel.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            val resultText = if (lineByLine) inputText.split("\n")
                .joinToString(separator = "\n") { it.reversed() } else inputText.reversed()

            withContext(Dispatchers.Main){
                binding.resultLabel.text = resultText
                binding.progressView.isVisible = false
            }
        }
    }
}