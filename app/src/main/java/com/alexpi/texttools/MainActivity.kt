package com.alexpi.texttools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.*
import androidx.recyclerview.widget.GridLayoutManager
import com.alexpi.texttools.custom.TextToolsAdapter
import com.alexpi.texttools.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this)

        with(binding){
            mainBanner.loadAd(AdRequest.Builder().build())
            val toolsList = resources.getStringArray(R.array.text_tools)
            val adapter =
                TextToolsAdapter(toolsList.toList()) { position, toolName ->
                    startToolActivity(position, toolName)
                }

            toolsView.adapter = adapter
            toolsView.layoutManager = GridLayoutManager(this@MainActivity,2)
            toolsView.setHasFixedSize(true)
        }
    }


    private fun startToolActivity(position: Int, toolName: String){
        val intent = Intent(this,ToolActivity::class.java)
        intent.putExtra(EXTRA_TOOL_NAME,toolName)
        intent.putExtra(EXTRA_FRAGMENT_NUMER, position)
        startActivity(intent)
    }

    companion object {
        const val EXTRA_TOOL_NAME = "com.alexpi.texttools.TOOL_NAME"
        const val EXTRA_FRAGMENT_NUMER = "com.alexpi.texttools.FRAGMENT_NUMBER"
    }
}