package com.alexpi.texttools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.*
import android.view.Menu
import android.view.MenuItem
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
                TextToolsAdapter(toolsList.toList()) { position ->
                    when (position) {
                        0 -> startActivity<TextSplitterActivity>()
                        1 -> startActivity<TextJoinerActivity>()
                        2 -> startActivity<TextRepeaterActivity>()
                        3 -> startActivity<TextReverserActivity>()
                    }
                }

            toolsView.adapter = adapter
            toolsView.layoutManager = GridLayoutManager(this@MainActivity,2)
            toolsView.setHasFixedSize(true)
        }
    }


    private inline fun <reified T: AppCompatActivity> startActivity(){
        val intent = Intent(this, T::class.java)
        startActivity(intent)
    }
}