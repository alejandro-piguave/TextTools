package com.alexpi.texttools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.*
import android.text.TextUtils.replace
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexpi.texttools.custom.TextToolsAdapter
import com.alexpi.texttools.custom.ToolItemCallback
import com.alexpi.texttools.databinding.ActivityMainBinding
import com.alexpi.texttools.extension.getToolFragment
import com.alexpi.texttools.fragment.ToolListFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity(), ToolItemCallback {
    private var isDualPane = false

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this)

        with(binding){
            mainBanner.loadAd(AdRequest.Builder().build())

            supportFragmentManager.commit {
                replace(R.id.containerA, ToolListFragment())
            }

            isDualPane = containerB?.isVisible ?: false

            if(isDualPane){
                supportFragmentManager.commit {
                    replace(R.id.containerB, getToolFragment(0))
                }
            }
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

    override fun onToolItemClicked(position: Int, toolName: String) {
        if(isDualPane){
            supportFragmentManager.commit {
                replace(R.id.containerB, getToolFragment(position))
            }
        } else {
            startToolActivity(position, toolName)
        }
    }
}