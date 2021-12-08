package com.example.testpractice.list

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.testpractice.AnimationTestRule
import com.example.testpractice.R
import com.example.testpractice.ServiceLocator
import com.example.testpractice.data.AndroidTestFakePlaceRepository
import com.example.testpractice.data.PlaceRepository
import com.example.testpractice.data.local.Place
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@MediumTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ListFragmentTest {
    private lateinit var repository: PlaceRepository

    @get:Rule
    var animationTestRule = AnimationTestRule()

    @Before
    fun createRepository() {
        repository = AndroidTestFakePlaceRepository()
        ServiceLocator.placeRepository = repository
    }

    @After
    fun cleanDb() = runBlockingTest {
        ServiceLocator.resetRepository()
    }


    @Test
    fun insertPlace_DisplayInUi() = runBlockingTest {
        val place1 = Place("title1", "comment1", true)
        repository.insertPlace(place1)
        launchFragmentInContainer<ListFragment>(Bundle(), R.style.Theme_TestPractice)

        onView(withId(R.id.title_tv)).check(matches(isDisplayed()))
        onView(withId(R.id.title_tv)).check(matches(withText("title1")))
        onView(withId(R.id.comment_tv)).check(matches(isDisplayed()))
        onView(withId(R.id.comment_tv)).check(matches(withText("comment1")))
        onView(withId(R.id.iv_been_here)).check(matches(isDisplayed()))
    }

    @Test
    fun clickPlace_navigateToEditFragment() = runBlockingTest {
        val place1 = Place("title1", "comment1", true)
        val place2 = Place("title2", "comment2")
        repository.insertPlace(place1)
        repository.insertPlace(place2)

        val scenario = launchFragmentInContainer<ListFragment>(Bundle(), R.style.Theme_TestPractice)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }
        onView(withId(R.id.list_rv))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, click()
                )
            )

        verify(navController).navigate(
            ListFragmentDirections.actionListFragmentToEditFragment(place1.placeId)
        )
    }

    @Test
    fun clickAddPlaceButton_navigateToEditFragment() {
        val scenario = launchFragmentInContainer<ListFragment>(Bundle(), R.style.Theme_TestPractice)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }
        onView(withId(R.id.add_fb)).perform(click())
        verify(navController).navigate(
            ListFragmentDirections.actionListFragmentToEditFragment(null)
        )
    }
}