package com.alexpi.texttools

import com.alexpi.texttools.extension.takeAndAddSuffix
import com.alexpi.texttools.extension.takeLastAndAddPrefix
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun `takeAndReplaceRangeWithSuffix function works correctly`() {
        val a  = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae dui quam."
        val truncatedA = a.takeAndAddSuffix(20,"...")
        assert(truncatedA == "Lorem ipsum dolor...")
    }
    @Test
    fun `takeLastAndReplaceRangeWithAffix function works correctly`() {
        val a  = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae dui quam."
        val truncatedA = a.takeLastAndAddPrefix(10,"...")
        assert(truncatedA == "...i quam.")
    }
}