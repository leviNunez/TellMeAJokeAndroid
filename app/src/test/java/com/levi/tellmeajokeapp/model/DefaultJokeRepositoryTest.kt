package com.levi.tellmeajokeapp.model

import com.google.common.truth.Truth.assertThat
import com.levi.tellmeajokeapp.model.network.JokeApiService
import com.levi.tellmeajokeapp.model.network.Result.Error
import com.levi.tellmeajokeapp.model.network.Result.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
internal class DefaultJokeRepositoryTest {

    private val remoteJoke = Joke(
        type = "general",
        setup = "What did the ocean say to the beach?",
        punchline = "Thanks for all the sediment.",
        id = 180
    )

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: DefaultJokeRepository

    @Test
    fun `getJoke on success loads a joke from the network`() = runTest(testDispatcher.scheduler) {
        // Given
        val service = mockk<JokeApiService>()
        coEvery { service.getRandomJoke() } returns remoteJoke
        repository = DefaultJokeRepository(service, testDispatcher)

        // When
        val joke = repository.getJoke() as Success

        // Then
        assertThat(joke.data).isEqualTo(remoteJoke)
    }

    @Test
    fun `getJoke on error expect error result`() = runTest(testDispatcher.scheduler) {
        // Given
        val service = mockk<JokeApiService>()
        coEvery { service.getRandomJoke() } throws Exception("Test exception")
        repository = DefaultJokeRepository(service, testDispatcher)

        // When
        val result = repository.getJoke() as Error

        // Then
        assertThat(result.exception.message).isEqualTo("Test exception")
    }
}