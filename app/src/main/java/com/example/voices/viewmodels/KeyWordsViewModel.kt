package com.example.voices.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class KeyWordsViewModel: ViewModel() {
    var keyWords = mutableStateListOf<String>("Москва", "Дороги")

    fun addWord(word:String){
        keyWords.add(word)
    }

    fun removeWord(id: Int){
        keyWords.removeAt(id)
    }
}