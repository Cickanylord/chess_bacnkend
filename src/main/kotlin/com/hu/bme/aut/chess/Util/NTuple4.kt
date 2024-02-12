package com.hu.bme.aut.chess.Util

data class Quad<T1, T2, T3, T4>(val t1: T1, val t2: T2, val t3: T3, val t4: T4) {
    operator fun get(i: Int): Any? {
        return when(i) {
            0 -> t1
            1 -> t2
            2 -> t3
            3 -> t4
            else -> throw IllegalArgumentException("Index out of bounds")
        }
    }

    fun isFullOfFalse(): Boolean{
        if (t1 is Boolean && t2 is Boolean && t3 is Boolean && t4 is Boolean ) {
            return !(t1 || t2 || t3 || t4)
        }
        else throw IllegalArgumentException("this only works if the quad only has booleans")
    }

}