package com.levi.tellmeajokeapp.view

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.levi.tellmeajokeapp.*
import com.levi.tellmeajokeapp.model.androidTestJokeRepository
import com.levi.tellmeajokeapp.model.FakeAppContainer
import com.levi.tellmeajokeapp.model.Joke
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
internal class JokeFragmentTest {

    private val joke1 = Joke(
        type = "general",
        setup = "What did the ocean say to the beach?",
        punchline = "Thanks for all the sediment.",
        id = 180
    )
    private val joke2 = Joke(
        type = "general",
        setup = "What do you call a fat psychic?",
        punchline = "A four-chin teller.",
        id = 280
    )

    @Before
    fun setup() {
        val repository = androidTestJokeRepository(joke1, joke2)
        val container = FakeAppContainer(repository)
        ApplicationProvider.getApplicationContext<JokeApplication>().appContainer = container
    }

    @Test
    fun test_setupTextAndQuestionMarkButtonAreDisplayed() = runTest {
        // When
        launchFragmentInContainer<JokeFragment>(themeResId = R.style.Theme_TellMeAJokeApp)

        // Then
        onView(withId(R.id.setup_text)).check(matches(isDisplayed()))
        onView(withId(R.id.question_mark_button)).check(matches(isDisplayed()))
    }

    @Test
    fun test_clickQuestionMarkButton_punchlineTextAndButtonsAreDisplayed() = runTest {
        // Given
        launchFragmentInContainer<JokeFragment>(themeResId = R.style.Theme_TellMeAJokeApp)

        // When
        onView(withId(R.id.question_mark_button)).perform(click())
        awaitPunchlineAnimation()

        // Then
        onView(withId(R.id.punchline_text)).check(matches(isDisplayed()))
        onView(withId(R.id.back_button)).check(matches(isDisplayed()))
        onView(withId(R.id.next_button)).check(matches(isDisplayed()))
    }

    @Test
    fun test_clickBackButton_setupTextAndQuestionMarkButtonAreDisplayed() = runTest {
        // Given
        launchFragmentInContainer<JokeFragment>(themeResId = R.style.Theme_TellMeAJokeApp)
        onView(withId(R.id.question_mark_button)).perform(click())
        awaitPunchlineAnimation()
        onView(withId(R.id.punchline_text)).check(matches(isDisplayed()))

        // When
        onView(withId(R.id.back_button)).perform(click())

        // Then
        onView(withId(R.id.setup_text)).check(matches(isDisplayed()))
        onView(withId(R.id.question_mark_button)).check(matches(isDisplayed()))
    }

    @Test
    fun test_clickNextButton_newJokeDisplayed() = runTest {
        // Given
        launchFragmentInContainer<JokeFragment>(themeResId = R.style.Theme_TellMeAJokeApp)
        onView(withId(R.id.setup_text)).check(matches(withText(joke1.setup)))
        onView(withId(R.id.question_mark_button)).perform(click())
        awaitPunchlineAnimation()
        onView(withId(R.id.punchline_text)).check(matches(isDisplayed()))

        // When
        onView(withId(R.id.next_button)).perform(click())

        // Then
        onView(withId(R.id.setup_text)).check(matches(withText(joke2.setup)))
    }

    @Test
    fun test_errorState_errorLayoutDisplayed() = runTest {
        // Given
        val newRepository = androidTestJokeRepository(joke1, shouldReturnError = true)
        val newContainer = FakeAppContainer(newRepository)
        ApplicationProvider.getApplicationContext<JokeApplication>().appContainer = newContainer

        // When
        launchFragmentInContainer<JokeFragment>(themeResId = R.style.Theme_TellMeAJokeApp)

        // Then
        onView(withId(R.id.error_image)).check(matches(isDisplayed()))
        onView(withId(R.id.error_text)).check(matches(isDisplayed()))
        onView(withId(R.id.retry_button)).check(matches(isDisplayed()))
    }

    private suspend fun awaitPunchlineAnimation() {
        coroutineScope {
            val deferred = async {
                withContext(Dispatchers.Default) {
                    // Wait for punchline animation to finish
                    delay(JokeFragment.PUNCHLINE_ANIMATION_DELAY_TIME)
                }
            }
            deferred.await()
        }
    }
}