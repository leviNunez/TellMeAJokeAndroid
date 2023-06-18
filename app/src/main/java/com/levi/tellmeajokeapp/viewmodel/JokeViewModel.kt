package com.levi.tellmeajokeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.levi.tellmeajokeapp.JokeApplication
import com.levi.tellmeajokeapp.model.Joke
import com.levi.tellmeajokeapp.model.JokeRepository
import kotlinx.coroutines.launch
import com.levi.tellmeajokeapp.model.network.Result.Success
import com.levi.tellmeajokeapp.model.network.Result.Error
import kotlinx.coroutines.flow.*

sealed interface UiState {
    object Loading : UiState
    data class ShowSetup(val setup: String) : UiState
    data class ShowPunchline(val punchline: String) : UiState
    object Error : UiState
}

class JokeViewModel(private val jokeRepository: JokeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private var joke: Joke? = null
    var punchlineAnimationFinished = false
        private set

    init {
        getJoke()
    }

    fun getJoke() {
        _uiState.update { UiState.Loading }
        viewModelScope.launch {
            val result = jokeRepository.getJoke()
            when (result) {
                is Success -> {
                    joke = result.data
                    revealSetup()
                }

                is Error -> {
                    _uiState.update { UiState.Error }
                }
            }
        }
    }

    fun revealSetup() {
        punchlineAnimationFinished = false
        _uiState.update {
            if (joke != null) {
                UiState.ShowSetup(setup = joke!!.setup)
            } else {
                UiState.Error
            }
        }
    }

    fun revealPunchline() {
        _uiState.update {
            if (joke != null) {
                UiState.ShowPunchline(punchline = joke!!.punchline)
            } else {
                UiState.Error
            }
        }
    }

    fun punchlineAnimationDidFinish() {
        punchlineAnimationFinished = true
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
                    jokeRepository = (application as JokeApplication).appContainer.repository
                ) as T
            }
        }
    }
}