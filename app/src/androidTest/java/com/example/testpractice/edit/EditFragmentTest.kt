package com.example.testpractice.edit

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
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
import org.mockito.Mockito
import org.mockito.Mockito.verify

@MediumTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class EditFragmentTest {

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
    fun inTheCaseOfEdit_displayClickedItem() = runBlockingTest {
        val place1 = Place("title1", "comment1", true)
        repository.insertPlace(place1)
        val bundle = EditFragmentArgs(place1.placeId).toBundle()
        launchFragmentInContainer<EditFragment>(bundle, R.style.Theme_TestPractice)

        onView(withId(R.id.et_title)).check(matches(isDisplayed()))
        onView(withId(R.id.et_title)).check(matches(withText("title1")))
        onView(withId(R.id.et_comment)).check(matches(isDisplayed()))
        onView(withId(R.id.et_comment)).check(matches(withText("comment1")))
        onView(withId(R.id.cb_has_been_to)).check(matches(isDisplayed()))
        onView(withId(R.id.cb_has_been_to)).check(matches(isChecked()))
    }

    @Test
    fun inTheCaseOfAdd_displayEmptyEditText() {
        val bundle = EditFragmentArgs(null).toBundle()
        launchFragmentInContainer<EditFragment>(bundle, R.style.Theme_TestPractice)

        onView(withId(R.id.et_title)).check(matches(isDisplayed()))
        onView(withId(R.id.et_title)).check(matches(withText("")))
        onView(withId(R.id.et_comment)).check(matches(isDisplayed()))
        onView(withId(R.id.et_comment)).check(matches(withText("")))
        onView(withId(R.id.cb_has_been_to)).check(matches(isDisplayed()))
        onView(withId(R.id.cb_has_been_to)).check(matches(isNotChecked()))
    }

    @Test
    fun inputEmptyTitleName_showErrorSnackBar() {
        val bundle = EditFragmentArgs(null).toBundle()
        launchFragmentInContainer<EditFragment>(bundle, R.style.Theme_TestPractice)

        onView(withId(R.id.et_title)).perform(click()).perform(typeText(""))
        onView(withId(R.id.bt_save)).perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(isDisplayed()))
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText("タイトルが空です。")))
    }

    @Test
    fun inTheCaseOfAdd_deleteAction_showErrorSnackBar() {
        val bundle = EditFragmentArgs(null).toBundle()
        launchFragmentInContainer<EditFragment>(bundle, R.style.Theme_TestPractice)

        onView(withId(R.id.bt_delete)).perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(isDisplayed()))
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText("この場所は登録されていません。")))
    }

    @Test
    fun inTheCaseOfEdit_deleteActionSuccess_backToListFragment() = runBlockingTest {
        val place1 = Place("title1", "comment1", true)
        repository.insertPlace(place1)
        val bundle = EditFragmentArgs(place1.placeId).toBundle()
        val scenario = launchFragmentInContainer<EditFragment>(bundle, R.style.Theme_TestPractice)
        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }
        onView(withId(R.id.bt_delete)).perform(click())
        verify(navController).popBackStack()
    }

    @Test
    fun validInsertPlace_backToListFragment() {
        val bundle = EditFragmentArgs(null).toBundle()
        val scenario = launchFragmentInContainer<EditFragment>(bundle, R.style.Theme_TestPractice)
        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.et_title)).perform(click()).perform(replaceText("title1"))
        onView(withId(R.id.et_comment)).perform(click()).perform(replaceText("comment1"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.bt_save)).perform(click())

        verify(navController).popBackStack()
    }

    @Test
    fun validUpdatePlace_backToListFragment() = runBlockingTest {
        val place1 = Place("title1", "comment1", true)
        repository.insertPlace(place1)
        val bundle = EditFragmentArgs(place1.placeId).toBundle()
        val scenario = launchFragmentInContainer<EditFragment>(bundle, R.style.Theme_TestPractice)
        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.et_title)).perform(click()).perform(replaceText("title"))
        onView(withId(R.id.et_comment)).perform(click()).perform(replaceText("comment"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.bt_save)).perform(click())

        verify(navController).popBackStack()
    }
}