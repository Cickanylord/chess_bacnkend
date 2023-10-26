package com.hu.bme.aut.chess.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id


@Entity
class ChessUser(
   // @Column(nullable = false, name = "userName")
    private var name: String,
    @Id
    @GeneratedValue
    //@Column(name="userId")
    private val id: Long?=null,
){
    fun getname():String{
        return name
    }

    fun setname(name: String){
        this.name = name
    }

    fun getId(): Long?{
        return id
    }
}

