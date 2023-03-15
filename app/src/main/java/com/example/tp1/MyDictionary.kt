package com.example.tp1

object MyDictionary {
    private val dictionary = mutableMapOf<String, Boolean>()

    fun set(key: String, value: Boolean){
        dictionary[key] = value
    }

    fun get(key: String): Boolean?{
        return dictionary[key]
    }

    fun getFirstUnkownPersonName(): String?{
        for((key, value) in dictionary){
            if(value == false){
                return key
            }
        }
        return null
    }
}