package com.example.dictionary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.model.DictionaryResponse
import com.example.dictionary.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DictionaryViewModel : ViewModel() {

    private val _result = MutableStateFlow<DictionaryResponse?>(null)
    val result: StateFlow<DictionaryResponse?> = _result

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun showError(message: String) {
        _error.value = message
    }

    fun searchWord(word: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                _result.value = null

                val response = RetrofitInstance.api.getWord(word)
                _result.value = response.firstOrNull()

            } catch (e: HttpException) {
                _result.value = null

                _error.value = when (e.code()) {
                    404 -> "Word not found"
                    522 -> "Dictionary service is temporarily unavailable"
                    else -> "Unable to get the word"
                }

            } catch (e: Exception) {
                _result.value = null
                _error.value = "Unable to connect. Please try again."

            } finally {
                _loading.value = false
            }
        }
    }
}