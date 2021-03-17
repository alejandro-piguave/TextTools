package com.alexpi.texttools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.alexpi.texttools.base.BaseToolActivity
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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> {
                if (binding.resultLabel.text.toString().isEmpty()) {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.invalid_output_text),
                        Toast.LENGTH_LONG
                    ).show()
                    binding.resultLabel.startAnimation(shakeAnimation)
                } else {
                    try {
                        val sendIntent = Intent()
                        sendIntent.action = Intent.ACTION_SEND
                        sendIntent.putExtra(Intent.EXTRA_TEXT, binding.resultLabel.text.toString())
                        sendIntent.type = "text/plain"
                        startActivity(sendIntent)
                    }catch (e: Exception){
                        Snackbar.make(findViewById(android.R.id.content),R.string.text_too_long_to_be_shared,
                            Snackbar.LENGTH_LONG).show()
                    }
                }
                return true
            }
            R.id.copy -> {
                if (binding.resultLabel.text.toString().trim().isEmpty()) {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.invalid_output_text),
                        Toast.LENGTH_LONG
                    ).show()
                    binding.resultLabel.startAnimation(shakeAnimation)
                } else {
                    val manager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    try{
                        manager.setPrimaryClip(
                            ClipData.newPlainText(
                                binding.resultLabel.text.toString(),
                                binding.resultLabel.text.toString()
                            )
                        )
                        Toast.makeText(this, getString(R.string.clipboard_copied), Toast.LENGTH_SHORT)
                            .show()
                    }catch (e: Exception){
                        Snackbar.make(findViewById(android.R.id.content),R.string.text_too_long_to_be_copied,Snackbar.LENGTH_LONG).show()
                    }

                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.text_tool_menu, menu)
        return true
    }

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