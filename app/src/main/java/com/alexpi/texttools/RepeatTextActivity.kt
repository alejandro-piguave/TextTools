package com.alexpi.texttools

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.alexpi.texttools.databinding.ActivityRepeatTextBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepeatTextActivity  : BaseToolActivity<ActivityRepeatTextBinding>() {
    private lateinit var shakeAnimation: Animation

    override fun getViewBinding(): ActivityRepeatTextBinding  = ActivityRepeatTextBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake_it)
        with(binding){
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
                        val delimiterInputText = delimiterEditText.text.toString()
                        val inputText = inputEditText.text.toString()
                        val delimiter = if(delimiterInputText == "\\n") "\n" else delimiterInputText
                        resultLabel.text = null
                        repeatText(inputText, num, delimiter)
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