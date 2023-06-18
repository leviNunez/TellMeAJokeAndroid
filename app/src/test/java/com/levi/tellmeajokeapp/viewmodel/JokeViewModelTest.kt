package com.levi.tellmeajokeapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.levi.tellmeajokeapp.MainCoroutineRule
import com.levi.tellmeajokeapp.model.FakeJokeRepository
import com.levi.tellmeajokeapp.model.Joke
import kotlinx.coroutines.*
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

    private lateinit var viewModel: JokeViewModel
    private lateinit var repository: FakeJokeRepository

    @Before
    fun setupRepository() {
        repository = FakeJokeRepository(joke = joke)
        viewModel = JokeViewModel(jokeRepository = repository)
    }

    @Test
    fun `getJoke on success sets uiState to ShowSetup`() = runTest {
        // Given
        val viewModel = JokeViewModel(repository)

        // When
        viewModel.getJoke()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value).isEqualTo(UiState.ShowSetup(setup = joke.setup))
    }

    @Test
    fun `getJoke on error sets uiState to Error`() = runTest {
        // Given
        repository.setReturnError(value = true)

        // When
        viewModel.getJoke()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value).isEqualTo(UiState.Error)
    }

    @Test
    fun `revealPunchline sets uiState to ShowPunchline`() = runTest {
        // Given
        viewModel.getJoke()
        advanceUntilIdle()

        // When
        viewModel.revealPunchline()

        // Then
        assertThat(viewModel.uiState.value).isEqualTo(UiState.ShowPunchline(punchline = joke.punchline))
    }

    @Test
    fun `revealSetup sets uiState back to ShowSetup`() = runTest {
        // Given
        viewModel.getJoke()
        advanceUntilIdle()
        viewModel.revealPunchline()

        // Assert that the state was set to ShowPunchline first
        assertThat(viewModel.uiState.value).isEqualTo(UiState.ShowPunchline(punchline = joke.punchline))

        // When
        viewModel.revealSetup()

        // Then
        assertThat(viewModel.uiState.value).isEqualTo(UiState.ShowSetup(setup = joke.setup))
    }
}