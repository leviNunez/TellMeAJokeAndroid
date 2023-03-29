package com.levi.tellmeajokeapp.ui.joke

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.levi.tellmeajokeapp.JokeApplication
import com.levi.tellmeajokeapp.R
import com.levi.tellmeajokeapp.data.Joke
import com.levi.tellmeajokeapp.data.source.FakeAndroidTestJokeRepository
import com.levi.tellmeajokeapp.data.source.JokeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
internal class JokeFragmentTest {

    private lateinit var repository: JokeRepository

    @Before
    fun setup() {
        val joke1 = Joke(
            type = "general",
            setup = "What did the ocean say to the beach?",
            punchline = "Thanks for all the sediment.",
            id = 180
        )
        val joke2 = Joke(
            type = "general",
            setup = "What do you call a fat psychic?",
            punchline = "A four-chin teller.",
            id = 180
        )
        repository = FakeAndroidTestJokeRepository(listOf(joke1, joke2))
        ApplicationProvider.getApplicationContext<JokeApplication>().appContainer.jokeRepository =
            repository
    }

    @Test
    fun launchFragment_displayJokeSetupAndQuestionMarkButton() = runTest {

        // WHEN: Joke fragment is launched
        launchFragmentInContainer<JokeFragment>(themeResId = R.style.Theme_TellMeAJokeApp)

        // THEN: verify the joke setup and question mark buttons are displayed
        onView(withId(R.id.setup_text)).check(matches(withText("What did the ocean say to the beach?")))
        onView(withId(R.id.question_mark_button)).check(matches(isDisplayed()))
    }

    @Test
    fun clickQuestionMarkButton_displayPunchlineAndBackNextButtons() = runTest {

        // WHEN: The question mark button is pressed
        launchFragmentInContainer<JokeFragment>(themeResId = R.style.Theme_TellMeAJokeApp)
        onView(withId(R.id.question_mark_button)).perform(click())

        // THEN: The joke punchline and the back and next buttons are displayed
        onView(withId(R.id.punchline_text)).check(matches(isDisplayed()))
        onView(withId(R.id.punchline_text)).check(matches(withText("Thanks for all the sediment.")))
        onView(withId(R.id.back_button)).check(matches(isDisplayed()))
        onView(withId(R.id.next_button)).check(matches(isDisplayed()))
    }

    @Test
    fun backButton_displayJokeSetupAndQuestionMarkButton() = runTest {

        // GIVEN: A loaded joke
        launchFragmentInContainer<JokeFragment>(themeResId = R.style.Theme_TellMeAJokeApp)

        // ...and the punchline is visible
        onView(withId(R.id.question_mark_button)).perform(click())
        onView(withId(R.id.punchline_text)).check(matches(isDisplayed()))

        // WHEN: The back button is pressed
        onView(withId(R.id.back_button)).perform(click())

        // THEN: The joke setup and question mark button are visible again
        onView(withId(R.id.setup_text)).check(matches(isDisplayed()))
        onView(withId(R.id.setup_text)).check(matches(withText("What did the ocean say to the beach?")))
        onView(withId(R.id.question_mark_button)).check(matches(isDisplayed()))
    }

    @Test
    fun nextButton_displayANewJoke() = runTest {

        // GIVEN: An already loaded joke
        launchFragmentInContainer<JokeFragment>(themeResId = R.style.Theme_TellMeAJokeApp)
        onView(withId(R.id.setup_text)).check(matches(withText("What did the ocean say to the beach?")))
        onView(withId(R.id.question_mark_button)).perform(click())
        onView(withId(R.id.punchline_text)).check(matches(withText("Thanks for all the sediment.")))

        // WHEN: The "Next" button is pressed
        onView(withId(R.id.next_button)).perform(click())

        // THEN: Verify a new joke is loaded
        onView(withId(R.id.setup_text)).check(matches(withText("What do you call a fat psychic?")))
        onView(withId(R.id.question_mark_button)).perform(click())
        onView(withId(R.id.punchline_text)).check(matches(withText("A four-chin teller.")))
    }
}