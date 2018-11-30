package net.falsetrue.stackexchangequerier.data

class SearchParams(val inTitle: String?,
                   val page: Int,
                   val pageSize: Int,
                   val orderAsc: Boolean,
                   val sortBy: String) {
    companion object {
        const val QUERY = "query"
        const val PAGE = "page"
        const val PAGE_SIZE = "pageSize"
        const val ORDER_ASC = "orderAsc"
        const val SORT_BY = "sortBy"
    }

    /**
     * @return map applicable for passing as GET form params to perform the search
     */
    fun asMap(vararg except: String): Map<String, Any?> {
        val result = HashMap(mapOf(QUERY to inTitle,
                PAGE to page,
                PAGE_SIZE to pageSize,
                ORDER_ASC to orderAsc,
                SORT_BY to sortBy))
        except.forEach { result.remove(it) }
        return result
    }
}
