package com.example.tp1

class Case(val Invalue: Int, val index: Int) {
    var visible: Boolean = false
    var found: Boolean = false
    var value: Int? = null
    var idx: Int? = null
    init {
        value = Invalue
        idx = index
    }

}