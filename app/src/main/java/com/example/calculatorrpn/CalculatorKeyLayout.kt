package com.example.calculatorrpn

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.GridLayout


class CalculatorKeyLayout : GridLayout, OnTouchListener {

    private val mycontext: Context

    constructor(context: Context) : super(context) {
        this.mycontext = context
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.mycontext = context
    }

    constructor(
        context: Context, attrs: AttributeSet,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
        this.mycontext = context
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        resizeKeys(width)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun resizeKeys(keyboardWidth: Int) {
        val keyw = keyboardWidth / 5
        val assets = this.mycontext.assets
        val rpnfont = Typeface.createFromAsset(assets, "fonts/RPN.TTF")
        val roboto = Typeface.createFromAsset(assets, "fonts/Roboto-Light.ttf")

        for (i in 0 until childCount) {
            val key = getChildAt(i) as Button
            key.setOnTouchListener(this)
            val kid = key.id
            if (kid == R.id.enter) {
                key.height = keyw * 1
            }
            key.width = keyw
            if (kid == R.id.bsp) {
                key.setTypeface(rpnfont)
            } else {
                key.setTypeface(roboto)

            }
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            v.performHapticFeedback(
                HapticFeedbackConstants.VIRTUAL_KEY,
                HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
            )
        }
        return false
    }
}