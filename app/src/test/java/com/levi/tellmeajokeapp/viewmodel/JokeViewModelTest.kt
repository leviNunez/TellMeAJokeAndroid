package com.levi.tellmeajokeapp.viewmodel

import com.google.common.truth.Truth.assertThat
import com.levi.tellmeajokeapp.MainDispatcherRule
import com.levi.tellmeajokeapp.model.Joke
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private lateinit var viewModel: JokeViewModel
    private lateinit var repository: FakeJokeRepository

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

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

        // Then
        assertThat(viewModel.uiState.value).isEqualTo(UiState.ShowSetup(setup = joke.setup))
    }

    @Test
    fun `getJoke on error sets uiState to Error`() = runTest {
        // Given
        repository.setReturnError(true)

        // When
        viewModel.getJoke()

        // Then
        assertThat(viewModel.uiState.value).isEqualTo(UiState.Error)
    }

    @Test
    fun `revealPunchline sets uiState to ShowPunchline`() = runTest {
        // Given
        viewModel.getJoke()

        // When
        viewModel.revealPunchline()

        // Then
        assertThat(viewModel.uiState.value).isEqualTo(UiState.ShowPunchline(punchline = joke.punchline))
    }

    @Test
    fun `revealSetup sets uiState back to ShowSetup`() = runTest {
        // Given
        viewModel.getJoke()
        viewModel.revealPunchline()
        // Assert that the state was set to ShowPunchline
        assertThat(viewModel.uiState.value).isEqualTo(UiState.ShowPunchline(punchline = joke.punchline))

        // When
        viewModel.revealSetup()

        // Then
        assertThat(viewModel.uiState.value).isEqualTo(UiState.ShowSetup(setup = joke.setup))
    }
}