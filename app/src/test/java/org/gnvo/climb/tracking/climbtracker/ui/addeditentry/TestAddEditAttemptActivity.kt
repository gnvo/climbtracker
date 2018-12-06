package org.gnvo.climb.tracking.climbtracker.ui.addeditentry

import org.junit.Assert.assertEquals
import org.junit.Test as test

class TestAddEditAttemptActivity {
    @test fun regex1() {
        val matchResult = AddEditAttemptActivity.regexLocationExtractor.find("3,4.1")

        assertEquals(matchResult!!.groups[1]!!.value, "3")
        assertEquals(matchResult.groups[2]!!.value, "4.1")
    }

    @test fun regex2() {
        val matchResult = AddEditAttemptActivity.regexLocationExtractor.find("3, 4.1")

        assertEquals(matchResult!!.groups[1]!!.value, "3")
        assertEquals(matchResult.groups[2]!!.value, "4.1")
    }

    @test fun regex3() {
        val matchResult = AddEditAttemptActivity.regexLocationExtractor.find("3 ,4.1")

        assertEquals(matchResult!!.groups[1]!!.value, "3")
        assertEquals(matchResult.groups[2]!!.value, "4.1")
    }

    @test fun regex4() {
        val matchResult = AddEditAttemptActivity.regexLocationExtractor.find("3,4 (accuracy bla)")

        assertEquals(matchResult!!.groups[1]!!.value, "3")
        assertEquals(matchResult.groups[2]!!.value, "4")
    }
}
