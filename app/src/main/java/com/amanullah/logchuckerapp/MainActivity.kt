package com.amanullah.logchuckerapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amanullah.logchuckerapp.databinding.ActivityMainBinding
import com.amanullah.logchuckerapp.extensions.showToast
import com.amanullah.logchuckerapp.service.FloatingWindowService
import com.amanullah.logchuckerapp.utils.LoggerUtil
import com.amanullah.logchuckerapp.utils.ForegroundServiceUtil

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