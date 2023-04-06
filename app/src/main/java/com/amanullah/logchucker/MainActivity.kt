package com.amanullah.logchucker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amanullah.logchucker.databinding.ActivityMainBinding
import com.amanullah.logchucker.extensions.showToast
import com.amanullah.logchucker.service.FloatingWindowService
import com.amanullah.logchucker.utils.LoggerUtil
import com.amanullah.logchucker.utils.ForegroundServiceUtil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            showLogWindowButton.setOnClickListener {
                if (LoggerUtil.checkOverlayDisplayPermission(this@MainActivity)) {
                    ForegroundServiceUtil.allowForegroundService = true
                    if (ForegroundServiceUtil.allowForegroundService) {
                        FloatingWindowService.Factory.startService(applicationContext)

                        "Foreground Service for Log Window is Started".showToast(applicationContext)
                    }
                } else {
                    LoggerUtil.requestOverlayDisplayPermission(this@MainActivity)
                }
            }
        }
    }
}