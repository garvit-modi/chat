package com.example.chat

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.os.IBinder
import android.view.*
import android.view.View.OnTouchListener
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.RelativeLayout
import androidx.annotation.Nullable
import com.andremion.counterfab.CounterFab


class FloatingWidgetService : Service() {
    private var mWindowManager: WindowManager? = null
    private var mOverlayView: View? = null
    var mWidth = 0
    var counterFab: CounterFab? = null
    var activity_background = false

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent != null) {
            activity_background = intent.getBooleanExtra("activity_background", false)
        }
        if (mOverlayView == null) {
            mOverlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null)
            val LAYOUT_FLAG: Int
            LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }

            var params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )


            //Specify the view position
            params.gravity = Gravity.TOP or Gravity.LEFT //Initially view will be added to top-left corner
            params.x = 0
            params.y = 100
            mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
            mWindowManager!!.addView(mOverlayView, params)
            val display = mWindowManager!!.defaultDisplay
            val size = Point()
            display.getSize(size)
            counterFab = mOverlayView!!.findViewById<View>(R.id.fabHead) as CounterFab
            counterFab!!.setCount(1)
            val layout = mOverlayView!!.findViewById<View>(R.id.layout) as RelativeLayout
            val vto = layout.viewTreeObserver
            vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    layout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val width = layout.measuredWidth

                    //To get the accurate middle of the screen we subtract the width of the floating widget.
                    mWidth = size.x - width
                }
            })
            counterFab!!.setOnTouchListener(object : OnTouchListener {
                private var initialX = 0
                private var initialY = 0
                private var initialTouchX = 0f
                private var initialTouchY = 0f
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {

                            //remember the initial position.
                            initialX = params.x
                            initialY = params.y


                            //get the touch location
                            initialTouchX = event.rawX
                            initialTouchY = event.rawY
                            return true
                        }
                        MotionEvent.ACTION_UP -> {

                            //Only start the activity if the application is in background. Pass the current badge_count to the activity
                            if (activity_background) {
                                val xDiff = event.rawX - initialTouchX
                                val yDiff = event.rawY - initialTouchY
                                if (Math.abs(xDiff) < 5 && Math.abs(yDiff) < 5) {
                                    val intent = Intent(
                                        this@FloatingWidgetService,
                                        MainActivity2::class.java
                                    )
                                    intent.putExtra("badge_count", counterFab!!.getCount())
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)

                                    //close the service and remove the fab view
                                    stopSelf()
                                }
                            }
                            //Logic to auto-position the widget based on where it is positioned currently w.r.t middle of the screen.
                            val middle = mWidth / 2
                            val nearestXWall =
                                if (params.x >= middle) mWidth.toFloat() else 0.toFloat()
                            params.x = nearestXWall.toInt()
                            mWindowManager!!.updateViewLayout(mOverlayView, params)
                            return true

                        }
                        MotionEvent.ACTION_MOVE -> {
                            val xDiff = Math.round(event.rawX - initialTouchX)
                            val yDiff = Math.round(event.rawY - initialTouchY)


                            //Calculate the X and Y coordinates of the view.
                            params.x = initialX + xDiff
                            params.y = initialY + yDiff

                            //Update the layout with new X & Y coordinates
                            mWindowManager!!.updateViewLayout(mOverlayView, params)
                            return true
                        }
                    }
                    return false
                }
            })
        } else {
            counterFab?.increase()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.AppTheme)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mOverlayView != null) mWindowManager!!.removeView(mOverlayView)

    }
}
