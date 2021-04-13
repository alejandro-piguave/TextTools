package com.alexpi.texttools.base

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.alexpi.texttools.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseToolFragment<B: ViewBinding> : BaseFragment<B>(){
    companion object{
        private const val MAX_CLIPBOARD_LABEL_LENGTH = 250
    }

    abstract fun getResultString(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val resultString = getResultString()
        when(item.itemId){
            android.R.id.home -> {
                requireActivity().finish()
                return true
            }
            R.id.share -> {
                if (resultString.isEmpty()) {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.invalid_output_text),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    try {
                        val sendIntent = Intent()
                        sendIntent.action = Intent.ACTION_SEND
                        sendIntent.putExtra(Intent.EXTRA_TEXT, resultString)
                        sendIntent.type = "text/plain"
                        startActivity(sendIntent)
                    }catch (e: Exception){
                        Snackbar.make(binding.root,R.string.text_too_long_to_be_shared,
                            Snackbar.LENGTH_LONG).show()
                    }
                }
                return true
            }
            R.id.copy -> {
                if (resultString.isEmpty()) {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.invalid_output_text),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val manager = requireActivity().getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
                    try{
                        manager.setPrimaryClip(ClipData.newPlainText(resultString.take(MAX_CLIPBOARD_LABEL_LENGTH), resultString))
                        Toast.makeText(context, getString(R.string.clipboard_copied), Toast.LENGTH_SHORT)
                            .show()
                    }catch (e: Exception){
                        Snackbar.make(binding.root,R.string.text_too_long_to_be_copied, Snackbar.LENGTH_LONG).show()
                    }
                }
                return true
            }
            else -> return false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.text_tool_menu, menu)
    }
}

