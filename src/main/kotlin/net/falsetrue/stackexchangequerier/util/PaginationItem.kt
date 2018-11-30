package net.falsetrue.stackexchangequerier.util

/**
 * Represents an element in the pagination view.
 * @param label the element's label (either <<, >>, … or a number).
 * @param link the link to the page. Null if this is a 'skipping element' (…).
 * @param enabled true if this item is enabled (clickable), false otherwise.
 * @param current true if this is the current page (to highlight somehow), false otherwise.
 */
class PaginationItem(val label: String,
                     val link: String?,
                     val enabled: Boolean,
                     val current: Boolean = false) {
    companion object {
        const val SKIP = "…"
        const val BACKWARD = "«"
        const val FORWARD = "»"
    }
}
