package com.alizaidi.busroute

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.bottomsheet.BottomSheetBehavior

import android.widget.LinearLayout
import com.alizaidi.busroute.models.BusInfo
import android.view.MotionEvent
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RelativeLayout
import com.alizaidi.busroute.utils.TimeConverter
import kotlinx.android.synthetic.main.bottom_sheet_dialog.*


class MainActivity : AppCompatActivity(){
    private lateinit var mBottomSheetLayout: RelativeLayout


    private lateinit var sheetBehavior: BottomSheetBehavior<*>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        bottomNavigationView.selectedItemId = R.id.nav_live
        mBottomSheetLayout = findViewById(R.id.bottom_sheet_layout);
        sheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout);

    }

    fun bottomSheetData(bus: BusInfo, end: String) {
        if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            busno.text = bus.id
            route.text = bus.route
            val min = TimeConverter().convert(bus.timestamp)
            time.text = "Updated $min minutes ago"
            destination.text = "towards ${end}"
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (sheetBehavior.getState() === BottomSheetBehavior.STATE_EXPANDED) {
                val outRect = Rect()
                mBottomSheetLayout.getGlobalVisibleRect(outRect)
                if (!outRect.contains(
                        event.rawX.toInt(),
                        event.rawY.toInt()
                    )
                )
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
            }

        }
        return super.dispatchTouchEvent(event)
    }
}


