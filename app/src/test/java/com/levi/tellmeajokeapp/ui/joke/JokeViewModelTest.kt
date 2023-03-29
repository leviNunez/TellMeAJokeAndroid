package com.levi.tellmeajokeapp.ui.joke

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.levi.tellmeajokeapp.MainCoroutineRule
import com.levi.tellmeajokeapp.data.Joke
import com.levi.tellmeajokeapp.data.source.FakeTestJokeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class JokeViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule(dispatcher = UnconfinedTestDispatcher())

    private lateinit var jokeRepository: FakeTestJokeRepository

    @Before
    fun setupRepository() {
        jokeRepository = FakeTestJokeRepository()
        val joke = Joke(
            type = "general",
            setup = "What did the ocean say to the beach?",
            punchline = "Thanks for all the sediment.",
            id = 180
        )
        jokeRepository.setJoke(joke)

    }

    @Test
    fun `refreshJoke updates the ui to the correct state on success`() = runTest {
        // GIVEN: A fresh viewmodel
        val viewModel = JokeViewModel(jokeRepository)

        // WHEN: refreshJoke is called
        viewModel.next()

        // THEN: Verify that the ui is in the correct state
        assertThat(viewModel.uiState.value.joke).isNotNull()
        assertThat(viewModel.uiState.value.isLoading).isFalse()
        assertThat(viewModel.uiState.value.errorMessage).isNull()
    }

    @Test
    fun `refreshJoke updates the ui to the correct state on error`() = runTest {
        // GIVEN: A fresh viewmodel
        jokeRepository.setReturnError(value = true)
        val viewModel = JokeViewModel(jokeRepository)

        // WHEN: refreshJoke is called
        viewModel.next()

        // THEN: Verify that the ui is in the correct state
        assertThat(viewModel.uiState.value.errorMessage).isNotNull()
        assertThat(viewModel.uiState.value.joke).isNull()
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }
}