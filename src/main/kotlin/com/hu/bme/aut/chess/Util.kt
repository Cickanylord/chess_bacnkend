package com.hu.bme.aut.chess

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object  hulladek {
    inline fun <reified T> fromGsonToList(body: List<T>?): List<T> {
        val json = Gson().toJson(body)
        val type = object : TypeToken<List<T>>() {}.type
        return Gson().fromJson(json, type)
    }
}

