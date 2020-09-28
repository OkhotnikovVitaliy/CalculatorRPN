package com.example.calculatorrpn

import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Stack


class CalculatorStack : Serializable {
    private val stack: Stack<BigDecimal>?
    val scale = 2

    val isEmpty: Boolean
        get() = this.stack!!.isEmpty()

    init {
        this.stack = Stack()
    }

    fun push(number: String) {
        val newnum = BigDecimal(number)
        this.stack!!.push(newnum)
    }

    fun toString(levels: Int): StringBuilder {
        val result = StringBuilder(TYPICAL_LENGTH_X4)
        if (this.stack != null) {
            val depth = this.stack.size
            for (i in 0 until levels) {
                if (i != 0) {
                    result.append('\n')
                }
                val idx = depth - levels + i
                if (idx >= 0) {
                    result.append(formatNumber(this.stack[idx]))
                }
            }
        }
        return result
    }


    override fun toString(): String {
        return this.toString(1).toString().replace(",".toRegex(), "")
    }

    private fun formatNumber(number: BigDecimal): String {
        val result = StringBuilder(TYPICAL_LENGTH)
        result.append(
            number.setScale(
                this.scale,
                RoundingMode.HALF_UP
            ).toPlainString()
        )
        if (this.scale > 0) {
            if (result.indexOf(".") == -1) {
                result.append('.')
            }
            val zerosAfterPoint = result.length - result.indexOf(".") - 1
            for (i in zerosAfterPoint until this.scale) {
                result.append('0')
            }
        }

        var dot = result.indexOf(".")
        if (dot < 1) {
            dot = result.length
        }
        var lowindex = 0
        if (result[0] == '-') {
            lowindex = 1
        }
        var i = dot - 3
        while (i > lowindex) {
            result.insert(i, ',')
            i -= 3
        }
        return result.toString()
    }

    fun chs() {
        if (!this.stack!!.isEmpty()) {
            val topnum = this.stack.pop()
            this.stack.push(topnum.negate())
        }
    }


    fun drop() {
        if (!this.stack!!.isEmpty()) {
            this.stack.pop()
        }
    }


    fun dup() {
        if (!this.stack!!.isEmpty()) {
            val topnum = this.stack.peek()
            this.stack.push(topnum)
        }
    }

    fun add() {
        if (this.stack!!.size > 1) {
            val x = this.stack.pop()
            val y = this.stack.pop()
            val r = y.add(x)
            this.stack.push(r)
        }
    }

    fun subtract() {
        if (this.stack!!.size > 1) {
            val x = this.stack.pop()
            val y = this.stack.pop()
            val r = y.subtract(x)
            this.stack.push(r)
        }
    }

    fun multiply() {
        if (this.stack!!.size > 1) {
            val x = this.stack.pop()
            val y = this.stack.pop()
            val r = y.multiply(x)
            this.stack.push(r)
        }
    }

    fun divide(): String? {
        var result: String? = null
        if (this.stack!!.size > 1) {
            val x = this.stack.pop()
            val y = this.stack.pop()
            try {
                val r = y.divide(
                    x, INTERNAL_SCALE,
                    RoundingMode.HALF_EVEN
                )
                this.stack.push(r)
            } catch (e: ArithmeticException) {
                result = e.message
            }

        }
        return result
    }
    fun getScaleCalc(): Int {
        return this.scale
    }
    companion object {


        private const val serialVersionUID = 1L
        private val TYPICAL_LENGTH = 32
        private val TYPICAL_LENGTH_X4 = 128
        private val INTERNAL_SCALE = 32
    }
}