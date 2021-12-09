package com.example.testpractice

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.testpractice.data.PlaceRepository
import com.example.testpractice.data.local.Place
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {
    private lateinit var repository: PlaceRepository

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun init() {
        repository = ServiceLocator.providePlaceRepository(getApplicationContext())
    }

    @After
    fun reset() {
        ServiceLocator.resetRepository()
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun filterAll_observeAllPlace() = runBlocking {
        repository.insertPlace(Place("title1", "comment"))
        repository.insertPlace(Place("title2", "comment", true))
        repository.insertPlace(Place("title3", "comment"))
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText("すべて")).perform(click())

        onView(withText("title1")).check(matches(isDisplayed()))
        onView(withText("title2")).check(matches(isDisplayed()))
        onView(withText("title3")).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun filterGone_observeHasBeenToPlace() = runBlocking {
        repository.insertPlace(Place("title1", "comment"))
        repository.insertPlace(Place("title2", "comment", true))
        repository.insertPlace(Place("title3", "comment"))
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText("行った！")).perform(click())

        onView(withText("title1")).check(doesNotExist())
        onView(withText("title2")).check(matches(isDisplayed()))
        onView(withText("title3")).check(doesNotExist())

        activityScenario.close()
    }

    @Test
    fun filterSomeday_observeSomedayWantToGoPlace() = runBlocking {
        repository.insertPlace(Place("title1", "comment"))
        repository.insertPlace(Place("title2", "comment", true))
        repository.insertPlace(Place("title3", "comment"))
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText("いつか行きたい")).perform(click())

        onView(withText("title1")).check(matches(isDisplayed()))
        onView(withText("title2")).check(doesNotExist())
        onView(withText("title3")).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun add() = runBlocking {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.add_fb)).perform(click())

        onView(withId(R.id.et_title)).check(matches(isDisplayed()))
        onView(withId(R.id.et_comment)).check(matches(isDisplayed()))
        onView(withId(R.id.cb_has_been_to)).check(matches(isNotChecked()))

        onView(withId(R.id.et_title)).perform(click()).perform(replaceText("title"), closeSoftKeyboard())
        onView(withId(R.id.et_comment)).perform(click()).perform(replaceText("comment"), closeSoftKeyboard())
        onView(withId(R.id.cb_has_been_to)).perform(click())
        onView(withId(R.id.bt_save)).perform(click())

        onView(withText("title")).check(matches(isDisplayed()))
        onView(withText("comment")).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun edit() = runBlocking {
        repository.insertPlace(Place("title1", "comment"))
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.list_rv))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, click()
                )
            )

        onView(withId(R.id.et_title)).check(matches(isDisplayed()))
        onView(withId(R.id.et_title)).check(matches(withText("title1")))
        onView(withId(R.id.et_comment)).check(matches(isDisplayed()))
        onView(withId(R.id.et_comment)).check(matches(withText("comment")))

        onView(withId(R.id.et_title)).perform(click()).perform(replaceText("updateTitle"), closeSoftKeyboard())
        onView(withId(R.id.et_comment)).perform(click()).perform(replaceText("updateComment"), closeSoftKeyboard())
        onView(withId(R.id.cb_has_been_to)).perform(click())

        onView(withText("updateTitle")).check(matches(isDisplayed()))
        onView(withText("updateComment")).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun delete() = runBlocking {
        repository.insertPlace(Place("title1", "comment"))
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.list_rv))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, click()
                )
            )

        onView(withId(R.id.et_title)).check(matches(isDisplayed()))
        onView(withId(R.id.et_title)).check(matches(withText("title1")))
        onView(withId(R.id.et_comment)).check(matches(isDisplayed()))
        onView(withId(R.id.et_comment)).check(matches(withText("comment")))

        onView(withId(R.id.bt_delete)).perform(click())

        onView(withText("title1")).check(doesNotExist())
        onView(withText("comment")).check(doesNotExist())

        activityScenario.close()
    }
}