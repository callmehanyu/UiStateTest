package com.zhy.demo

import androidx.test.core.app.ActivityScenario
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.screenshot.Screenshot
import com.zhy.demo.mock.testUiStateCollection1_List
import org.junit.Rule
import org.junit.Test


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestActivityTest {

//    private val scenario: ActivityScenario<TestActivity> = ActivityScenario.launch(TestActivity::class.java)

    @get:Rule
    val mainActivityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    @Test
    fun navigateTest() {
        //        scenario.moveToState(Lifecycle.State.CREATED)

        testUiStateCollection1_List.forEach {
            //启动Activity
            mainActivityRule.activity.handle(it)
            delay(2000)
            // 目录：Pictures/screenshots
            Screenshot.capture().process()
        }
    }
}

