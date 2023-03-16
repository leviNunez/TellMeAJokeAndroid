package com.levi.tellmeajokeapp.data.source

import com.google.common.truth.Truth.assertThat
import com.levi.tellmeajokeapp.MainCoroutineRule
import com.levi.tellmeajokeapp.data.Joke
import com.levi.tellmeajokeapp.data.Result.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
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
    private lateinit var remoteDataSource: FakeDataSource

    //subject under test
    private lateinit var jokeRepository: JokeRepositoryImpl

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepository() {
        remoteDataSource = FakeDataSource(joke = remoteJoke)
        jokeRepository = JokeRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getJoke requests a joke from the remote data source`() = runTest {
        // WHEN: a joke is requested from the joke repository
        val joke = jokeRepository.getJoke() as Success

        // THEN: a joke is loaded from remote data source
        assertThat(joke.data).isEqualTo(remoteJoke)
    }
}