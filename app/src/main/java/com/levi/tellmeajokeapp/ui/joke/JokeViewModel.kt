package com.levi.tellmeajokeapp.ui.joke

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.levi.tellmeajokeapp.JokeApplication
import com.levi.tellmeajokeapp.data.Joke
import com.levi.tellmeajokeapp.data.source.JokeRepository
import kotlinx.coroutines.launch
import com.levi.tellmeajokeapp.data.Result.Success
import com.levi.tellmeajokeapp.data.Result.Error
import kotlinx.coroutines.flow.*

class JokeViewModel(private val jokeRepository: JokeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _uiActions = MutableSharedFlow<UiActions>()
    val uiEvent = _uiActions.asSharedFlow()


    init {
        next()
    }

    fun next() {
        _uiState.update {
            it.copy(
                isLoading = true,
                joke = null,
                errorMessage = null,
                shouldPlayAnimationLoop = false
            )
        }
        viewModelScope.launch {
            val result = jokeRepository.getJoke()
            _uiState.update {
                when (result) {
                    is Success -> it.copy(
                        isLoading = false,
                        joke = result.data,
                        shouldPlayAnimationLoop = true,
                    ).also {
                        _uiActions.emit(UiActions.Next)
                    }
                    is Error -> it.copy(isLoading = false, errorMessage = result.exception.message)
                }
            }
        }
    }

    fun revealPunchline() {
        _uiState.update { it.copy(shouldPlayAnimationLoop = false) }
        viewModelScope.launch { _uiActions.emit(UiActions.RevealPunchline) }
    }

    fun back() {
        _uiState.update { it.copy(shouldPlayAnimationLoop = true) }
        viewModelScope.launch { _uiActions.emit(UiActions.Back) }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])

                return JokeViewModel(
                    jokeRepository = (application as JokeApplication).appContainer.jokeRepository
                ) as T
            }
        }

    }
}

data class UiState(
    val isLoading: Boolean = false,
    val shouldPlayAnimationLoop: Boolean = false,
    val errorMessage: String? = null,
    val joke: Joke? = null,
)

sealed class UiActions {
    object RevealPunchline : UiActions()
    object Next : UiActions()
    object Back : UiActions()
}