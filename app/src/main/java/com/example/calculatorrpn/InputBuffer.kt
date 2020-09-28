package com.example.calculatorrpn

import android.util.Log

import java.io.Serializable

class InputBuffer : Serializable {
    private val buffer = StringBuilder(INITIAL_CAPACITY)


    val isEmpty: Boolean
        get() = this.buffer != null && this.buffer.length == 0

    constructor(value: String) : super() {
        this.set(value)
    }

    constructor() : super() {}


    fun append(ich: Char) {
        when (ich) {
            '.' -> if (this.buffer.indexOf(".") == -1) {
                if (this.buffer.length == 0) {
                    this.buffer.append('0')
                }
                this.buffer.append('.')
            }
            /*'0' -> if ("0" !! this.buffer) {
                this.buffer.append('0')
            }*/
            '1', '2', '3', '4', '5', '6', '7', '8', '9' -> this.buffer.append(ich)
            else -> Log.e("append", "Ignoring character '$ich'")
        }
    }


    fun delete() {
        val len = this.buffer.length
        if (len > 0) {
            this.buffer.setLength(len - 1)
        }
    }


    fun zap() {
        this.buffer.setLength(0)
    }


    fun set(value: String) {
        this.buffer.setLength(0)
        this.buffer.append(value)
    }


    fun get(): String {
        return this.buffer.toString()
    }

    override fun toString(): String {
        return this.get()
    }

    companion object {


        private const val serialVersionUID = 1L


        private val INITIAL_CAPACITY = 32
    }

}