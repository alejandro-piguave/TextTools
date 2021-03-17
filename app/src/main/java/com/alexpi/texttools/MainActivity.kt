package com.alexpi.texttools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.*
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.alexpi.texttools.databinding.ActivityMainBinding
import com.alexpi.texttools.textsplitter.TextSplitterActivity
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
            val adapter = TextToolsAdapter(toolsList.toList()){ position ->
                when(position){
                    0 -> startActivity<TextSplitterActivity>()
                    2 -> startActivity<TextRepeaterActivity>()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
}