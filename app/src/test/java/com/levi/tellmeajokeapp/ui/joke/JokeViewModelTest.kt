package com.levi.tellmeajokeapp.ui.joke

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.levi.tellmeajokeapp.MainCoroutineRule
import com.levi.tellmeajokeapp.data.Joke
import com.levi.tellmeajokeapp.data.source.FakeTestJokeRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class JokeViewModelTest {

    private val joke = Joke(
        type = "general",
        setup = "What did the ocean say to the beach?",
        punchline = "Thanks for all the sediment.",
        id = 180
    )

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var jokeRepository: FakeTestJokeRepository

    @Before
    fun setupRepository() {
        jokeRepository = FakeTestJokeRepository()

        jokeRepository.setJoke(joke)

    }

    @Test
    fun `next on success sets the joke and emits the correct value`() = runTest() {
        // Given a fresh viewmodel with a fake repository
        val viewModel = JokeViewModel(jokeRepository)

        // When next() is executed
        viewModel.next()

        // And uiAction emits a value
        val actionValue = viewModel.uiAction.first()

        // Then check if it is the expected value
        assertThat(actionValue).isEqualTo(UiAction.Next)

        // Advance time until next() completes
        advanceUntilIdle()

        // Get the value of uiState
        val stateValue = viewModel.uiState.value

        // Assert that the joke is set
        assertThat(stateValue.joke).isNotNull()
        assertThat(stateValue.joke?.id).isEqualTo(joke.id)
    }

    @Test
    fun `next on error sets the error message and emits the correct value`() = runTest {
        // Given a fresh viewmodel with a fake repository set to return an error
        jokeRepository.setReturnError(value = true)
        val viewModel = JokeViewModel(jokeRepository)

        // When next is executed
        viewModel.next()

        // And uiAction emits a value
        val actionValue = viewModel.uiAction.first()

        // Then check if it is the expected value
        assertThat(actionValue).isEqualTo(UiAction.ShowSnackBar("Test exception"))

        // Advance time until next() completes
        advanceUntilIdle()

        // Get the value of uiState
        val stateValue = viewModel.uiState.value

        // Assert that the error message is set
        assertThat(stateValue.errorMessage).isNotNull()
    }

    @Test
    fun `revealPunchline emits the correct value`() = runTest() {
        // Given a fresh viewmodel with a fake repository
        val viewModel = JokeViewModel(jokeRepository)

        // Advance time until all initialization tasks are completed
        advanceUntilIdle()

        // When revealPunchline() is executed
        viewModel.revealPunchline()

        // And uiAction emits a value
        val value = viewModel.uiAction.first()

        // Then check if it is the expected value
        assertThat(value).isEqualTo(UiAction.RevealPunchline)
    }

    @Test
    fun `back emits the correct value`() = runTest {
        // Given a fresh viewmodel
        val viewModel = JokeViewModel(jokeRepository)

        // Advance time until all initialization tasks are completed
        advanceUntilIdle()

        // When back() is executed
        viewModel.back()

        // And uiAction emits a value
        val value = viewModel.uiAction.first()

        // Then check if it is the expected value
        assertThat(value).isEqualTo(UiAction.Back)
    }
}