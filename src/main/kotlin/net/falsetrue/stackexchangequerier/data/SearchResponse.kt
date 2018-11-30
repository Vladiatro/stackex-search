package net.falsetrue.stackexchangequerier.data

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import data.Question

/**
 * StackExchange API /search response fields.
 */
class SearchResponse(val items: List<Question>?,
                     @JsonProperty("has_more")
                     val hasMore: Boolean?,
                     val total: Int = 0,
                     @JsonProperty("error_id")
                     val errorId: Int = 200,
                     @JsonProperty("error_message")
                     val errorMessage: String = "") {
    @JsonIgnore
    var pageCount: Int = 0;

    constructor(errorId: Int, errorMessage: String) : this(emptyList(), false, 0, errorId, errorMessage)
}
