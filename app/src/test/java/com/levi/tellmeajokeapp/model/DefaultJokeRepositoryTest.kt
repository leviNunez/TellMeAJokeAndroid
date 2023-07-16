package com.levi.tellmeajokeapp.model

import com.google.common.truth.Truth.assertThat
import com.levi.tellmeajokeapp.MainCoroutineRule
import com.levi.tellmeajokeapp.model.network.Result.Success
import com.levi.tellmeajokeapp.model.network.Result.Error
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class DefaultJokeRepositoryTest {

    private val remoteJoke = Joke(
        type = "general",
        setup = "What did the ocean say to the beach?",
        punchline = "Thanks for all the sediment.",
        id = 180
    )
z
    private lateinit var repository: DefaultJokeRepository

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    @Test
    fun `getJoke on success loads a joke from the data source`() = runTest {
        // Given
        repository = DefaultJokeRepository(FakeDataSource(joke = remoteJoke))

        // When
        val joke = repository.getJoke() as Success

        // Then
        assertThat(joke.data).isEqualTo(remoteJoke)
    }

    @Test
    fun `getJoke on error expect error result`() = runTest {
        // Given
        repository = DefaultJokeRepository(FakeDataSource(joke = null))

        // When
        val result = repository.getJoke() as Error

        // Then
        assertThat(result.exception.message).isEqualTo("Test exception")
    }
}