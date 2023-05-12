package com.amanullah.logchuckerapp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.amanullah.logchuckerapp.extensions.showToast
import com.amanullah.logchuckerapp.service.FloatingWindowService
import com.amanullah.logchuckerapp.utils.ForegroundServiceUtil

class App : Application() {
    private var numRunningActivities = 0
    override fun onCreate() {
        super.onCreate()
        this.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
            }

            override fun onActivityStarted(p0: Activity) {
                numRunningActivities++
                if (numRunningActivities == 1 && ForegroundServiceUtil.allowForegroundService) {
                    FloatingWindowService.Factory.startService(this@App)

                    "Foreground Service for Log Window is Running".showToast(this@App)
                }
            }

            override fun onActivityResumed(p0: Activity) {
            }

            override fun onActivityPaused(p0: Activity) {
            }

            override fun onActivityStopped(p0: Activity) {
                numRunningActivities--
                if (numRunningActivities == 0) {
                    FloatingWindowService.Factory.stopService(this@App)

                    "Foreground Service for Log Window is Closed".showToast(this@App)
                }
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }

            override fun onActivityDestroyed(p0: Activity) {
            }

        })
    }
}