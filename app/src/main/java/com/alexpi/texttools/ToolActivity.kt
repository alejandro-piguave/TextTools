package com.alexpi.texttools

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.alexpi.texttools.databinding.ActivityToolBinding
import com.alexpi.texttools.extension.getToolFragment
import com.google.android.gms.ads.AdRequest

class ToolActivity : AppCompatActivity() {

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
            replace(R.id.container, getToolFragment(fragmentNumber))
        }
    }
}