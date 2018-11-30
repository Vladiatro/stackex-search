package net.falsetrue.stackexchangequerier.controllers

import net.falsetrue.stackexchangequerier.data.SearchParams
import net.falsetrue.stackexchangequerier.data.SearchParams.Companion.ORDER_ASC
import net.falsetrue.stackexchangequerier.data.SearchParams.Companion.PAGE
import net.falsetrue.stackexchangequerier.data.SearchParams.Companion.PAGE_SIZE
import net.falsetrue.stackexchangequerier.data.SearchParams.Companion.QUERY
import net.falsetrue.stackexchangequerier.data.SearchParams.Companion.SORT_BY
import net.falsetrue.stackexchangequerier.services.StackExchangeSearchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * The query page request mapping.
 */
const val SEARCH_URL = "/"

@Controller
class StackExchangeSearchController {

    @Autowired
    private lateinit var stackExchangeSearchService: StackExchangeSearchService

    /**
     * Performs StackExchange search.
     */
    @GetMapping(SEARCH_URL)
    fun performQuery(@RequestParam(QUERY) inTitle: String?,
                     @RequestParam(PAGE, defaultValue = "1") page: Int,
                     @RequestParam(PAGE_SIZE, defaultValue = "10") pageSize: Int,
                     @RequestParam(ORDER_ASC, defaultValue = "false") orderAsc: Boolean,
                     @RequestParam(SORT_BY, defaultValue = "activity") sortBy: String,
                     model: Model): String {
        val params = SearchParams(inTitle, page, pageSize, orderAsc, sortBy)
        val response = stackExchangeSearchService.performQuery(params)
        model.addAttribute("response", response)
        model.addAttribute("params", params)
        model.addAttribute("pages", stackExchangeSearchService.pages(page, response.pageCount, params))
        return "search"
    }

}