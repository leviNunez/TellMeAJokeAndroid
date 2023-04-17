package com.levi.tellmeajokeapp.ui.joke

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.levi.tellmeajokeapp.*
import com.levi.tellmeajokeapp.data.Joke
import com.levi.tellmeajokeapp.data.source.FakeAndroidTestJokeRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
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
        id = 280
    )

    private val animationDelayTime = 300L

    @Before
    fun setupRepository() {
        val repository = FakeAndroidTestJokeRepository(listOf(joke1, joke2))
        val container = FakeAppContainer(repository)
        ApplicationProvider.getApplicationContext<JokeApplication>().appContainer = container
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

        // When the question mark button is clicked
        onView(withId(R.id.question_mark_button)).perform(click())

        // Wait for the animation to finish
        val deferred = async {
            withContext(Dispatchers.Default) {
                delay(animationDelayTime)
            }
        }
        deferred.await()

        // Then verify the joke punchline and control buttons are displayed
        onView(withId(R.id.punchline_text)).check(matches(isDisplayed()))
        onView(withId(R.id.back_button)).check(matches(isDisplayed()))
        onView(withId(R.id.next_button)).check(matches(isDisplayed()))
    }

    @Test
    fun clickBackButton_showJokeSetupAndQuestionMarkButton() = runTest {
        // Given a newly launched JokeFragment
        launchFragmentInContainer<JokeFragment>(themeResId = R.style.Theme_TellMeAJokeApp)

        // Click on the question mark button
        onView(withId(R.id.question_mark_button)).perform(click())

        // Wait for the animation to finish
        val deferred = async {
            withContext(Dispatchers.Default) {
                delay(animationDelayTime)
            }
        }
        deferred.await()

        // Verify the punchline is visible
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

        // Verify that a joke is displayed
        onView(withId(R.id.setup_text)).check(matches(withText(joke1.setup)))

        // Click the question mark button
        onView(withId(R.id.question_mark_button)).perform(click())

        // Wait for the animation to finish
        val deferred = async {
            withContext(Dispatchers.Default) {
                delay(animationDelayTime)
            }
        }
        deferred.await()

        // When The "Next" button is clicked
        onView(withId(R.id.next_button)).perform(click())

        // Then verify that a new joke is displayed
        onView(withId(R.id.setup_text)).check(matches(withText(joke2.setup)))
    }

    @Test
    fun errorMessage_notNull_errorLayoutDisplayed() = runTest {
        // Given a repository that is set to return an error
        val repository =
            FakeAndroidTestJokeRepository(shouldReturnError = true, jokeData = emptyList())
        val container = FakeAppContainer(repository)
        ApplicationProvider.getApplicationContext<JokeApplication>().appContainer = container

        // When the JokeFragment is launched
        launchFragmentInContainer<JokeFragment>(themeResId = R.style.Theme_TellMeAJokeApp)

        // Verify that the error layout is displayed
        onView(withId(R.id.error_image)).check(matches(isDisplayed()))
        onView(withId(R.id.error_text)).check(matches(isDisplayed()))
        onView(withId(R.id.retry_button)).check(matches(isDisplayed()))
    }
}