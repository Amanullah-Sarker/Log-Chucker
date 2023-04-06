package com.amanullah.logchucker.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.amanullah.logchucker.service.window.LogWindow

class FloatingWindowService : Service() {
    object Factory {
        private fun getIntent(context: Context) = Intent(context, FloatingWindowService::class.java)
        fun startService(context: Context) {
            try {
                context.startService(getIntent(context))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun stopService(context: Context) {
            try {
                context.stopService(getIntent(context))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private lateinit var logWindow: LogWindow
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        logWindow = LogWindow(this) {
            stopSelf()
        }
        logWindow.open()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
        logWindow.close()
    }
}
