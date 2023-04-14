package com.levi.tellmeajokeapp.data.source

import com.google.common.truth.Truth.assertThat
import com.levi.tellmeajokeapp.MainCoroutineRule
import com.levi.tellmeajokeapp.data.Joke
import com.levi.tellmeajokeapp.data.Result.Success
import com.levi.tellmeajokeapp.data.Result.Error
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class JokeRepositoryImplTest {

    private val remoteJoke = Joke(
        type = "general",
        setup = "What did the ocean say to the beach?",
        punchline = "Thanks for all the sediment.",
        id = 180
    )

    //subject under test
    private lateinit var jokeRepository: JokeRepositoryImpl

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    @Test
    fun `getJoke on success loads a joke from remote data source`() = runTest {
        // Given a jokeRepository with a fake dataSource
        jokeRepository = JokeRepositoryImpl(FakeDataSource(joke = remoteJoke))

        // When a joke is requested from the repository
        val joke = jokeRepository.getJoke() as Success

        // Then assert that joke is loaded
        assertThat(joke.data).isEqualTo(remoteJoke)
    }

    @Test
    fun `getJoke on error expect error result`() = runTest {
        // Given a jokeRepository with a fake dataSource that has no data
        jokeRepository = JokeRepositoryImpl(FakeDataSource(joke = null))

        // When a joke is requested from the repository
        val result = jokeRepository.getJoke() as Error

        // Then assert that an error result is received
        assertThat(result.exception.message).isEqualTo("Test exception")
    }
}