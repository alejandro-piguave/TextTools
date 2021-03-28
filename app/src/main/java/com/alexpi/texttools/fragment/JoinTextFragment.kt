package com.alexpi.texttools.fragment

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.alexpi.texttools.base.BaseToolFragment
import com.alexpi.texttools.databinding.FragmentJoinTextBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JoinTextFragment : BaseToolFragment<FragmentJoinTextBinding>() {
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

    override fun getViewBinding(container: ViewGroup?): FragmentJoinTextBinding = FragmentJoinTextBinding.inflate(layoutInflater, container, false)
}