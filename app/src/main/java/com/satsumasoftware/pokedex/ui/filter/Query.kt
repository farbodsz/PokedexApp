package com.satsumasoftware.pokedex.ui.filter

/**
 * The query object is used to filter results when searching through the database
 */
class Query(val filter: Filter) {

    class Builder {

        private var filter: Filter? = null

        fun addFilter(filter: Filter): Builder {
            this.filter = if (this.filter != null) {
                Filters.and(this.filter!!, filter)
            } else {
                filter
            }

            return this
        }

        fun hasNoFilters() = filter == null

        fun build(): Query {
            if (filter == null) {
                throw IllegalStateException("you must add at least one filter to the query builder")
            }

            return Query(filter!!)
        }
    }
}
