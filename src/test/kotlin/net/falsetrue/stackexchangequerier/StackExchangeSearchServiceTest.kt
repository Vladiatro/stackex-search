package net.falsetrue.stackexchangequerier

import net.falsetrue.stackexchangequerier.data.SearchParams
import net.falsetrue.stackexchangequerier.services.StackExchangeSearchService
import net.falsetrue.stackexchangequerier.util.PaginationItem
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class StackExchangeSearchServiceTest {
    @Autowired
    private lateinit var searchService: StackExchangeSearchService

    @Test
    fun `pagesTest, 1 of 1`() {
        assertPaginator("<<! 1_ >>!", 1, 1)
    }

    @Test
    fun `pagesTest, 1 of 3`() {
        assertPaginator("<<! 1_ 2 3 >>", 1, 3)
    }

    @Test
    fun `pagesTest, 2 of 3`() {
        assertPaginator("<< 1 2_ 3 >>", 2, 3)
    }

    @Test
    fun `pagesTest, 4 of 10`() {
        assertPaginator("<< 1 2 3 4_ 5 6 ... 10 >>", 4, 10)
    }

    @Test
    fun `pagesTest, 6 of 12`() {
        assertPaginator("<< 1 ... 4 5 6_ 7 8 ... 12 >>", 6, 12)
    }

    @Test
    fun `pagesTest, 12 of 12`() {
        assertPaginator("<< 1 ... 10 11 12_ >>!", 12, 12)
    }

    /**
     * Put ASCII paginator simulation into `expected` as shown in the tests above.
     * _ means selected, ! means disabled.
     */
    private fun assertPaginator(expected: String, currentPage: Int, pagesCount: Int) {
        val params = SearchParams("", 1, 1, true, "")
        val pages = searchService.pages(currentPage, pagesCount, params)
        val actualAsString = pages.joinToString(separator = " ") {
            when (it.label) {
                PaginationItem.BACKWARD -> "<<"
                PaginationItem.FORWARD -> ">>"
                PaginationItem.SKIP -> "..."
                else -> it.label
            } + (if (it.current) "_" else "") +
                    (if (it.enabled || it.label == PaginationItem.SKIP) "" else "!")
        }
        Assert.assertEquals(expected, actualAsString)
    }
}