package com.hu.bme.aut.chess.domain

import com.hu.bme.aut.chess.repository.user.UserService
import jakarta.persistence.*


@Entity
class ChessUser(
   // @Column(nullable = false, name = "userName")
    private var name: String,
    @Id
    @GeneratedValue
    //@Column(name="userId")
    private val id: Long?=null

    //TODO oneToMany

    
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

    override fun toString(): String {
        return "${id}_${name}"
    }

    override fun equals(other: Any?): Boolean {
        if(other is ChessUser?){
            return other.toString() == this.toString()
        }
        return super.equals(other)
    }
}

