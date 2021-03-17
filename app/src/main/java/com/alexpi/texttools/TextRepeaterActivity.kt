package com.alexpi.texttools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.alexpi.texttools.databinding.ActivityTextRepeaterBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TextRepeaterActivity  : BaseToolActivity<ActivityTextRepeaterBinding>() {
    private lateinit var shakeAnimation: Animation

    override fun getViewBinding(): ActivityTextRepeaterBinding  = ActivityTextRepeaterBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake_it)
        with(binding){
            customDelimiterCheckbox.setOnCheckedChangeListener { _, isChecked ->
                delimiterField.isVisible = !isChecked
            }
            repeatButton.setOnClickListener {
                val num = repetitionsInputText.text.toString().toIntOrNull() ?: -1
                when {
                    inputEditText.text.toString().isEmpty() -> {
                        inputField.showError(getString(R.string.invalid_input_text))
                        inputField.startAnimation(shakeAnimation)

                    }
                    num <= 0 -> {
                        repetitionsField.showError(getString(R.string.invalid_repetitions_number))
                        repetitionsField.startAnimation(shakeAnimation)
                    }
                    else -> {
                        val inputText = inputEditText.text.toString()
                        val delimiter = if(customDelimiterCheckbox.isChecked) "\n" else delimiterInputText.text.toString()
                        resultLabel.text = null
                        repeatText(inputText,num,delimiter)
                    }
                }
            }
        }

    }

    override fun getResultString(): String = binding.resultLabel.text.toString()



    private fun repeatText(text: String, repetitions: Int, delimiter: String) {
        binding.progressView.isVisible = true
        lifecycleScope.launch(Dispatchers.Default) {
            val processedText = (text) + delimiter
            val resultText = processedText.repeat(repetitions -1) + text

            withContext(Dispatchers.Main){
                binding.resultLabel.text = resultText
                binding.progressView.isVisible = false
            }
        }
    }

}