package com.levi.tellmeajokeapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.levi.tellmeajokeapp.data.Joke
import com.levi.tellmeajokeapp.data.source.JokeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.levi.tellmeajokeapp.data.Result.Success
import com.levi.tellmeajokeapp.data.Result.Error

class JokeViewModel(private val jokeRepository: JokeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(JokeUiState())
    val uiState: StateFlow<JokeUiState> = _uiState

    init {
        refreshJoke()
    }

    fun refreshJoke() {
        _uiState.update { it.copy(isLoading = true, joke = null, errorMessage = null) }
        viewModelScope.launch {
            val result = jokeRepository.getJoke()
            _uiState.update {
                when (result) {
                    is Success -> it.copy(joke = result.data, isLoading = false)
                    is Error -> it.copy(errorMessage = result.exception.message, isLoading = false)
                }
            }
        }
    }

    data class JokeUiState(
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val joke: Joke? = null,
    )

}