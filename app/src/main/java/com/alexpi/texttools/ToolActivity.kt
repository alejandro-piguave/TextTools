package com.alexpi.texttools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.commit
import com.alexpi.texttools.base.BaseToolFragment
import com.alexpi.texttools.custom.TextTool
import com.alexpi.texttools.databinding.ActivityToolBinding
import com.alexpi.texttools.fragment.*
import com.google.android.gms.ads.AdRequest
import com.google.android.material.snackbar.Snackbar

class ToolActivity : AppCompatActivity() {
    companion object{
        private const val MAX_CLIPBOARD_LABEL_LENGTH = 250
    }

    private lateinit var textTool: TextTool

    private lateinit var binding: ActivityToolBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityToolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolBanner.loadAd(AdRequest.Builder().build())

        val toolName = intent.getStringExtra(MainActivity.EXTRA_TOOL_NAME)
        val fragmentNumber = intent.getIntExtra(MainActivity.EXTRA_FRAGMENT_NUMER, 0)
        supportActionBar?.title = toolName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.commit {
            replace(R.id.container, getToolFragment(fragmentNumber).also { textTool = it })
        }
    }

    private fun getToolFragment(n: Int): BaseToolFragment<*> =
        when(n){
            0 -> SplitTextFragment()
            1 -> JoinTextFragment()
            2 -> RepeatTextFragment()
            3 -> ReverseTextFragment()
            4 -> TruncateTextFragment()
            5 -> TrimTextFragment()
            6 -> AddLeftPaddingFragment()
            7 -> AddRightPaddingFragment()
            8 -> AddPrefixFragment()
            9 -> AddSuffixFragment()
            else -> RemoveEmptyLinesFragment()
        }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val resultString = textTool.getResultString()
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