package com.hu.bme.aut.chess.Util

data class Quad(var t1: Boolean, var t2: Boolean, var t3: Boolean, var t4: Boolean) {
    operator fun get(i: Int): Boolean {
        return when(i) {
            0 -> t1
            1 -> t2
            2 -> t3
            3 -> t4
            else -> throw IllegalArgumentException("Index out of bounds")
        }
    }

    operator fun set(i: Int, value: Boolean) {
         when(i) {
            0 -> t1 = false
            1 -> t2 = false
            2 -> t3 = false
            3 -> t4 = false
            else -> throw IllegalArgumentException("Index out of bounds")
         }
    }

    override fun equals(other: Any?): Boolean {
        if (other is Quad) {
            return this[0] == other [0] && this[1] == other [1] && this[2] == other [2] && this[3] == other [3]
        }



        return super.equals(other)
    }


    fun isFullOfFalse(): Boolean {
        return !(t1 || t2 || t3 || t4)
    }

    fun hasTrue(): Boolean {
        return !isFullOfFalse()
    }




}