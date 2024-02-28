package com.hu.bme.aut.chess.Util


data class CastlingRights(var kw: Boolean, var qw: Boolean, var kb: Boolean, var qb: Boolean) {
    operator fun get(i: Int): Boolean {
        return when(i) {
            0 -> kw
            1 -> qw
            2 -> kb
            3 -> qb
            else -> throw IllegalArgumentException("Index out of bounds")
        }
    }



    operator fun set(i: Int, value: Boolean) {

            when (i) {
                0 -> kw = false
                1 -> qw = false
                2 -> kb = false
                3 -> qb = false
                else -> throw IllegalArgumentException("Index out of bounds")
            }

    }

    override fun equals(other: Any?): Boolean {
        if (other is CastlingRights) {
            return this[0] == other [0] && this[1] == other [1] && this[2] == other [2] && this[3] == other [3]
        }



        return super.equals(other)
    }


    fun isFullOfFalse(): Boolean {
        return !(kw || qw || kb || qb)
    }

    fun hasTrue(): Boolean {
        return !isFullOfFalse()
    }

}