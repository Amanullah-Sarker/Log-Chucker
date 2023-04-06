package com.amanullah.logchucker.utils

import android.app.ActivityManager
import android.app.AlertDialog.Builder
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.viewbinding.BuildConfig
import com.amanullah.logchucker.service.FloatingWindowService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor

object LoggerUtil {

    private val logsSharedFlow =
        MutableSharedFlow<String>(replay = 10, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val logs = logsSharedFlow.asSharedFlow()

    private val scope = CoroutineScope(Dispatchers.Default)

    val httpLogging = HttpLoggingInterceptor { log ->
        if (log.contains("http") || log.startsWith("{"))
            emitLog(log)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
        redactHeader("Authorization")
        redactHeader("Cookie")
    }

    fun requestOverlayDisplayPermission(context: Context) {
        val log = ""

        val builder = Builder(context)
        builder.setCancelable(true)
        builder.setTitle("Screen Overlay Permission Needed")
        builder.setMessage("Enable 'Display over other apps' from System Settings.")
        builder.setPositiveButton("Open Settings") { _, _ -> // The app will redirect to the 'Display over other apps' in Settings.
            val intent =
                Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${context.applicationInfo.packageName}")
                )

            context.startActivity(intent)
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun checkOverlayDisplayPermission(context: Context): Boolean {
        // Android Version is lesser than Marshmallow
        // or the API is lesser than 23
        // doesn't need 'Display over other apps' permission enabling.
        return if (VERSION.SDK_INT > VERSION_CODES.M) {
            // If 'Display over other apps' is not enabled it
            // will return false or else true
            Settings.canDrawOverlays(context)
        } else {
            true
        }
    }

    fun isServiceRunning(context: Context): Boolean {
        val activityManager =
            context.getSystemService(ComponentActivity.ACTIVITY_SERVICE) as ActivityManager
        val services = activityManager.getRunningServices(Integer.MAX_VALUE)
        services.forEach {
            if (it.service.className == FloatingWindowService::class.qualifiedName) {
                return true
            }
        }
        return false
    }

    fun emitLog(data: String) {
        if (BuildConfig.DEBUG) {
            scope.launch { logsSharedFlow.emit(data) }
        }
    }
}
