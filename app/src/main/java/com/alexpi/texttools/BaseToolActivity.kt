package com.alexpi.texttools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.alexpi.texttools.MainActivity.Companion.EXTRA_TOOL_NAME
import com.google.android.material.snackbar.Snackbar

abstract class BaseToolActivity<B: ViewBinding>: AppCompatActivity() {

    companion object{
        private const val MAX_CLIPBOARD_LABEL_LENGTH = 250
    }

    lateinit var binding: B
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)

        val toolName = intent.getStringExtra(EXTRA_TOOL_NAME)
        supportActionBar?.title = toolName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setUp()
    }

    open fun setUp(){}

    abstract fun getViewBinding(): B
    abstract fun getResultString(): String

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val resultString = getResultString()
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.share -> {
                if (resultString.isEmpty()) {
                    Toast.makeText(
                            this,
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
                        Snackbar.make(findViewById(android.R.id.content),R.string.text_too_long_to_be_shared,
                                Snackbar.LENGTH_LONG).show()
                    }
                }
                return true
            }
            R.id.copy -> {
                if (resultString.isEmpty()) {
                    Toast.makeText(
                            this,
                            resources.getString(R.string.invalid_output_text),
                            Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val manager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    try{
                        manager.setPrimaryClip(ClipData.newPlainText(resultString.take(MAX_CLIPBOARD_LABEL_LENGTH), resultString))
                        Toast.makeText(this, getString(R.string.clipboard_copied), Toast.LENGTH_SHORT)
                                .show()
                    }catch (e: Exception){
                        Snackbar.make(findViewById(android.R.id.content),R.string.text_too_long_to_be_copied, Snackbar.LENGTH_LONG).show()
                    }
                }
                return true
            }
            else -> return false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.text_tool_menu, menu)
        return true
    }
}