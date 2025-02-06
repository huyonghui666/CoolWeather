package com.example.test2

object Demo {

    fun String.letterCount():Int{
        var count=0
        for (char in this){
            if (char.isLetter()){
                count++
            }
        }
        return count
    }
}