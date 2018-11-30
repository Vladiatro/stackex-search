package net.falsetrue.stackexchangequerier.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import net.falsetrue.stackexchangequerier.controllers.SEARCH_URL
import net.falsetrue.stackexchangequerier.data.SearchParams
import net.falsetrue.stackexchangequerier.data.SearchResponse
import net.falsetrue.stackexchangequerier.util.PaginationItem
import net.falsetrue.stackexchangequerier.util.PaginationItem.Companion.BACKWARD
import net.falsetrue.stackexchangequerier.util.PaginationItem.Companion.FORWARD
import net.falsetrue.stackexchangequerier.util.PaginationItem.Companion.SKIP
import org.apache.commons.io.IOUtils
import org.apache.http.HttpStatus
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.client.CloseableHttpClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import kotlin.math.max
import kotlin.math.min


/**
 * StackExchange query filter (include total results number and exclude some unused fields).
 */
private const val FILTER = "!FcbKgSQE-Clre*.gTOL6_L6FA0"

/**
 * The maximum number of the pages displayed on the sides of the current page (all further ones are skipped).
 */
private const val BORDERING_PAGES_COUNT = 2

@Service
class StackExchangeSearchService {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var httpClient: CloseableHttpClient

    /**
     * Loads the string content from `questionsUri`.
     */
    fun loadText(questionsUri: String): String {
        val request = HttpGet(questionsUri)
        try {
            val response = httpClient.execute(request)
            val contentStream = response.entity.content
            return IOUtils.toString(contentStream, StandardCharsets.UTF_8)
        } finally {
            request.releaseConnection()
        }
    }

    fun performQuery(params: SearchParams): SearchResponse {
        if (params.inTitle == null || params.inTitle.isBlank()) {
            return SearchResponse(emptyList(), false, 0)
        }
        val uriBuilder = URIBuilder("http://api.stackexchange.com/2.2/search")
        with(params) {
            uriBuilder
                    .setParameter("page", page.toString())
                    .setParameter("pagesize", pageSize.toString())
                    .setParameter("order", if (orderAsc) "asc" else "desc")
                    .setParameter("sort", sortBy)
                    .setParameter("intitle", inTitle)
                    .setParameter("site", "stackoverflow")
                    .setParameter("filter", FILTER)
        }
        return try {
            val stringResponse = loadText(uriBuilder.toString())
            val result = objectMapper.readValue<SearchResponse>(stringResponse)
            result.pageCount = result.total / params.pageSize + 1
            result
        } catch (e: Exception) {
            e.printStackTrace()
            SearchResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR,
                    "StackOverflow request error: ${e.message}.")
        }
    }

    /**
     * A simple pager. Returns the items of [PaginationItem] to render them on the view.
     * @return items for the pagination element.
     */
    fun pages(currentPage: Int, count: Int, params: SearchParams): List<PaginationItem> {
        val builder = URIBuilder(SEARCH_URL)
        params.asMap().forEach { p, v -> builder.setParameter(p, v.toString()) }

        val result = ArrayList<PaginationItem>()

        builder.setParameter(SearchParams.PAGE, (currentPage - 1).toString())
        result.add(PaginationItem(BACKWARD, builder.toString(), currentPage > 1))

        if (currentPage > BORDERING_PAGES_COUNT + 1) {
            builder.setParameter(SearchParams.PAGE, "1")
            result.add(PaginationItem("1", builder.toString(), true))
        }

        if (currentPage > BORDERING_PAGES_COUNT + 2) {
            result.add(PaginationItem(SKIP, null, false))
        }

        for (i in max(1, currentPage - BORDERING_PAGES_COUNT)..min(count, currentPage + BORDERING_PAGES_COUNT)) {
            builder.setParameter(SearchParams.PAGE, i.toString())
            result.add(PaginationItem(i.toString(), builder.toString(), true, i == currentPage))
        }

        if (currentPage < count - BORDERING_PAGES_COUNT - 1) {
            result.add(PaginationItem(SKIP, null, false))
        }

        if (currentPage < count - BORDERING_PAGES_COUNT) {
            builder.setParameter(SearchParams.PAGE, count.toString())
            result.add(PaginationItem(count.toString(), builder.toString(), true))
        }

        builder.setParameter(SearchParams.PAGE, (currentPage + 1).toString())
        result.add(PaginationItem(FORWARD, builder.toString(), currentPage < count))

        return result
    }
}