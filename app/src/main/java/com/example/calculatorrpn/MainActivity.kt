package com.example.calculatorrpn


import android.os.Bundle
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import android.app.Activity
import android.view.KeyEvent
import android.view.View
import android.view.View.OnKeyListener
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.TextView


class MainActivity : Activity(), OnKeyListener {

    private var buffer : InputBuffer? = null
    private var stack: CalculatorStack? = null
    private var error: String? = null
    private var screenlines: Int = 0


    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadState()
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        val disp = findViewById<TextView>(R.id.display) as TextView
        val hsv = findViewById<FrameLayout>(R.id.TopFrame) as FrameLayout
        this.screenlines = 1 + Math.round(hsv.height.toFloat() / disp.lineHeight.toFloat())
        updateDisplay()
    }

    fun updateDisplay() {
        val disp = findViewById<TextView>(R.id.display) as TextView
        val text: StringBuilder
        if (this.buffer!!.isEmpty && this.error == null) {
            if (this.stack!!.isEmpty) {
                text = StringBuilder()
                for (i in 1 until this.screenlines - 1) {
                    text.append('\n')
                }
                text.append('0')
                val scale = this.stack!!.getScaleCalc()
                if (scale > 0) {
                    text.append('.')
                    for (i in 0 until scale) {
                        text.append('0')
                    }
                }
            } else {
                text = this.stack!!.toString(this.screenlines)
            }
        } else {
            text = this.stack!!.toString(this.screenlines - 1)
            text.append("\n")
            if (this.error == null) {
                text.append(this.buffer!!.get())
            } else {
                text.append(this.error)
                this.error = null
            }
        }
        disp.setLines(this.screenlines)
        disp.text = text
        scrollToRight()
    }


    fun implicitPush() {
        if (!this.buffer!!.isEmpty) {
            val num = this.buffer!!.get()
            this.stack!!.push(num)
            this.buffer!!.zap()
        }
    }

    private fun keyDelete() {
        if (this.buffer!!.isEmpty) {
            this.stack!!.drop()
        } else {
            this.buffer!!.delete()
        }
        this.updateDisplay()
    }

    private fun keyEnter() {
        if (this.buffer!!.isEmpty) {
            this.stack!!.dup()
        } else {
            val num = this.buffer!!.get()
            this.stack!!.push(num)
            this.buffer!!.zap()
        }
        this.updateDisplay()
    }


    private fun keyOther(c: Char): Boolean {
        var handled = false
        when (c) {
            '+' -> {
                implicitPush()
                this.stack!!.add()
                this.updateDisplay()
                handled = true
            }
            '-' -> {
                implicitPush()
                this.stack!!.subtract()
                this.updateDisplay()
                handled = true
            }
            '*' -> {
                implicitPush()
                this.stack!!.multiply()
                this.updateDisplay()
                handled = true
            }
            '/' -> {
                implicitPush()
                this.error = this.stack!!.divide()
                this.updateDisplay()
                handled = true
            }
            else -> if (c >= '0' && c <= '9' || c == '.') {
                this.buffer!!.append(c)
                this.updateDisplay()
                handled = true
            }
        }
        return handled
    }


    private fun scrollToRight() {

        (findViewById(R.id.Scroll) as HorizontalScrollView).post {
            (findViewById(R.id.Scroll) as HorizontalScrollView).fullScroll(
                View.FOCUS_RIGHT
            )
        }
    }

    fun clickHandler(v: View) {
        val key = v.tag as String
        if ("bsp" == key) {
            keyDelete()
        } else if ("chs" == key) {
            implicitPush()
            this.stack!!.chs()
            updateDisplay()
        } else if ("enter" == key) {
            keyEnter()
        } else {
            val c = key[0]
            keyOther(c)
        }
    }


    override fun onKey(v: View, code: Int, event: KeyEvent): Boolean {
        var result = false
        handler@ run {
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (code == KeyEvent.KEYCODE_DEL) {
                    keyDelete()
                    result = true

                }
                if (code == KeyEvent.KEYCODE_ENTER) {
                    keyEnter()
                    result = true

                }
                val c = event.unicodeChar.toChar()
                result = keyOther(c)

            }
        }
        return result
    }

    private fun loadState() {
        val dir = cacheDir
        val data = File(dir, "stack")
        var fis: FileInputStream? = null
        var `in`: ObjectInputStream? = null
        try {
            fis = FileInputStream(data)
            `in` = ObjectInputStream(fis)
            this.stack = `in`.readObject() as CalculatorStack
            this.buffer = `in`.readObject() as InputBuffer
            `in`.close()
        } catch (ex: FileNotFoundException) {
            this.buffer = InputBuffer()
            this.stack = CalculatorStack()
        } catch (ex: IOException) {
        } catch (ex: ClassNotFoundException) {
        }

        if (this.buffer == null) {
            this.buffer = InputBuffer()
        }
        if (this.stack == null) {
            this.stack = CalculatorStack()
        }
    }
}