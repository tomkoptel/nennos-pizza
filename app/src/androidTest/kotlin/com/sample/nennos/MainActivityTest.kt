package com.sample.nennos

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val launchActivity = ActivityTestRule(MainActivity::class.java, true, false)

    @Test
    fun start_page() {
        launchActivity.launchActivity(null)
    }
}