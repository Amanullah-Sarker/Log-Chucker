package com.amanullah.logchuckerapp.service.window

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.PixelFormat
import android.os.Build
import android.view.*
import android.view.View.OnTouchListener
import android.view.WindowManager.LayoutParams
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amanullah.logchuckerapp.R
import com.amanullah.logchuckerapp.extensions.showToast
import com.amanullah.logchuckerapp.utils.ForegroundServiceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("InflateParams")
class LogWindow(private val context: Context, private val onCloseCall: () -> Unit) {

    private var view: View? = null
    private var params: LayoutParams? = null
    private var windowManager: WindowManager? = null
    private var layoutInflater: LayoutInflater? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val metrics = context.applicationContext.resources.displayMetrics
            val width = metrics.widthPixels
            val height = metrics.heightPixels
            // set the layout parameters of the window
            params = LayoutParams(
                (width * .9f).toInt(),
                (height * 0.2f).toInt(),  // Display it on top of other application windows
                LayoutParams.TYPE_APPLICATION_OVERLAY,  // Don't let it grab the input focus
                LayoutParams.FLAG_NOT_FOCUSABLE,  // Make the underlying application window visible
                // through any transparent parts
                PixelFormat.TRANSLUCENT
            )
        }

        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = layoutInflater?.inflate(R.layout.floating_view_layout, null)
        view!!.findViewById<AppCompatImageView>(R.id.closeWindowButton).setOnClickListener {
            try {
                ForegroundServiceUtil.allowForegroundService = false

                "Log Window is Closed".showToast(context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            close()
        }
        params!!.gravity = Gravity.TOP
        params!!.verticalMargin = 0.25f
        params?.x = 0
        params?.y = 0
        windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
        configTouchListener(view)
        setupRecyclerView(view!!.findViewById(R.id.loggerRecyclerView))
    }

    private fun setupRecyclerView(rv: RecyclerView) {
        val adapter = LoggerAdapter()
        rv.apply {
            layoutManager = LinearLayoutManager(rv.context)
            this.adapter = adapter
        }
        CoroutineScope(Dispatchers.Main)
            .launch {
                val data = mutableListOf(
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "13",
                    "14",
                    "15",
                    "16",
                    "17",
                    "18",
                    "19",
                    "20",
                    "21",
                    "22",
                    "23",
                    "24",
                    "25",
                    "26",
                    "27",
                    "28",
                    "29",
                    "30"
                )
                adapter.items = data
                rv.smoothScrollToPosition(data.lastIndex)

                /*LoggerUtil.logs.collect {
                    data.add(it)
                    val items = adapter.items.toMutableList()
                    items.add(it)
                    adapter.items = items
                    rv.smoothScrollToPosition(items.lastIndex)
                }*/
            }
    }

    private fun configTouchListener(view: View?) {
        view?.setOnTouchListener(object : OnTouchListener {
            val floatWindowLayoutUpdateParam: LayoutParams = params!!
            var x = 0.0
            var y = 0.0
            var px = 0.0
            var py = 0.0

            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x = floatWindowLayoutUpdateParam.x.toDouble()
                        y = floatWindowLayoutUpdateParam.y.toDouble()

                        // returns the original raw X
                        // coordinate of this event
                        px = event.rawX.toDouble()

                        // returns the original raw Y
                        // coordinate of this event
                        py = event.rawY.toDouble()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        floatWindowLayoutUpdateParam.x = (x + event.rawX - px).toInt()
                        floatWindowLayoutUpdateParam.y = (y + event.rawY - py).toInt()

                        // updated parameter is applied to the WindowManager
                        windowManager!!.updateViewLayout(view, floatWindowLayoutUpdateParam)
                    }
                }
                return false
            }
        })
    }


    fun open() {
        try {
            // check if the view is already
            // inflated or present in the window
            if (view!!.windowToken == null) {
                if (view!!.parent == null) {
                    windowManager!!.addView(view, params)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun close() {
        try {
            // remove the view from the window
            (context.getSystemService(WINDOW_SERVICE) as WindowManager).removeView(view)
            // invalidate the view
            view!!.invalidate()
            // remove all views
            (view!!.parent as ViewGroup).removeAllViews()

            // the above steps are necessary when you are adding and removing
            // the view simultaneously, it might give some exceptions
        } catch (e: Exception) {
            e.printStackTrace()
        }
        onCloseCall.invoke()
    }
}
