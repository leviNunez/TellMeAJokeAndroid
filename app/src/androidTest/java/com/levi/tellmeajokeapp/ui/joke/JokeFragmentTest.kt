package com.levi.tellmeajokeapp.ui.joke

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.material.snackbar.Snackbar
import com.levi.tellmeajokeapp.JokeApplication
import com.levi.tellmeajokeapp.R
import com.levi.tellmeajokeapp.data.Joke
import com.levi.tellmeajokeapp.data.source.FakeAndroidTestJokeRepository
import com.levi.tellmeajokeapp.data.source.JokeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
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
        id = 180
    )

    private lateinit var repository: JokeRepository

    @Before
    fun setup() {
        repository = FakeAndroidTestJokeRepository(listOf(joke1, joke2))
        ApplicationProvider.getApplicationContext<JokeApplication>().appContainer.jokeRepository =
            repository
    }


    @Test
    fun jokeSetupAndQuestionMarkButton_DisplayedInUi() = runTest {
        // Given a newly launched JokeFragment
        launchFragmentInContainer<JokeFragment>(themeResId = R.style.Theme_TellMeAJokeApp)

        // Then verify the joke setup and question mark button are displayed
        onView(withId(R.id.setup_text)).check(matches(isDisplayed()))
        onView(withId(R.id.question_mark_button)).check(matches(isDisplayed()))
    }

    @Test
    fun clickQuestionMarkButton_showPunchlineAndControlButtons() = runTest {
        // Given a newly launched JokeFragment
        launchFragmentInContainer<JokeFragment>(themeResId = R.style.Theme_TellMeAJokeApp)

        // When the question mark button is pressed
        onView(withId(R.id.question_mark_button)).perform(click())

        // Then verify the joke punchline and control buttons are displayed
        onView(withId(R.id.punchline_text)).check(matches(isDisplayed()))
        onView(withId(R.id.back_button)).check(matches(isDisplayed()))
        onView(withId(R.id.next_button)).check(matches(isDisplayed()))
    }

    @Test
    fun clickBackButton_showJokeSetupAndQuestionMarkButton() = runTest {
        // Given a newly launched JokeFragment
        launchFragmentInContainer<JokeFragment>(themeResId = R.style.Theme_TellMeAJokeApp)

        // And the question mark button is clicked
        onView(withId(R.id.question_mark_button)).perform(click())

        // And the punchline is visible
        onView(withId(R.id.punchline_text)).check(matches(isDisplayed()))

        // When the "Back" button is clicked
        onView(withId(R.id.back_button)).perform(click())

        // Then verify the joke setup and question mark button are visible again
        onView(withId(R.id.setup_text)).check(matches(isDisplayed()))
        onView(withId(R.id.question_mark_button)).check(matches(isDisplayed()))

        // And check the punchline is no longer visible
        onView(withId(R.id.punchline_text)).check(matches(not(isDisplayed())))
    }

    @Test
    fun clickNextButton_refreshJoke() = runTest {
        // Given a newly launched JokeFragment
        launchFragmentInContainer<JokeFragment>(themeResId = R.style.Theme_TellMeAJokeApp)

        // And a joke is displayed
        onView(withId(R.id.setup_text)).check(matches(withText(joke1.setup)))

        // And the question mark button is clicked
        onView(withId(R.id.question_mark_button)).perform(click())

        // When The "Next" button is clicked
        onView(withId(R.id.next_button)).perform(click())

        // Then verify a new joke is displayed
        onView(withId(R.id.setup_text)).check(matches(withText(joke2.setup)))
    }

    @Test
    fun hasError_errorLayoutDisplayed() = runTest {
        // Given a repository that is set to return an error
        repository = FakeAndroidTestJokeRepository(shouldReturnError = true, jokeData = emptyList())
        ApplicationProvider.getApplicationContext<JokeApplication>().appContainer.jokeRepository =
            repository

        // When the JokeFragment is launched
        launchFragmentInContainer<JokeFragment>(themeResId = R.style.Theme_TellMeAJokeApp)

        // Verify the error layout is displayed
        onView(withId(R.id.error_image)).check(matches(isDisplayed()))
        onView(withId(R.id.error_text)).check(matches(isDisplayed()))
        onView(withId(R.id.retry_button)).check(matches(isDisplayed()))
    }
}