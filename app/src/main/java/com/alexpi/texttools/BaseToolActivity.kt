package com.alexpi.texttools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar

abstract class BaseToolActivity<B: ViewBinding>: AppCompatActivity() {

    lateinit var binding: B
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    abstract fun getViewBinding(): B
    abstract fun getResultString(): String

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.share -> {
                if (getResultString().isEmpty()) {
                    Toast.makeText(
                            this,
                            resources.getString(R.string.invalid_output_text),
                            Toast.LENGTH_SHORT
                    ).show()
                } else {
                    try {
                        val sendIntent = Intent()
                        sendIntent.action = Intent.ACTION_SEND
                        sendIntent.putExtra(Intent.EXTRA_TEXT, getResultString())
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
                if (getResultString().isEmpty()) {
                    Toast.makeText(
                            this,
                            resources.getString(R.string.invalid_output_text),
                            Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val manager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    try{
                        manager.setPrimaryClip(ClipData.newPlainText(getResultString(), getResultString()))
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