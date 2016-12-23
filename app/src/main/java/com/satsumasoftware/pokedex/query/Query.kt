package com.satsumasoftware.pokedex.query

import android.util.Log
import java.util.*

/**
 * The query object is used to filter results when searching through the database
 */
class Query(val filter: Filter) {

    class Builder {

        companion object {
            @JvmField val LOG_TAG = "Query.Builder"
        }

        private val filters: ArrayList<Filter> = ArrayList()

        fun addFilter(filter: Filter): Builder {
            filters.add(filter)
            Log.v(LOG_TAG, "Added filter: ${filter.sqlStatement}")
            return this
        }

        fun removeFilter(filter: Filter): Builder {
            if (!filters.remove(filter)) {
                Log.w(LOG_TAG, "Unable to find filter to remove")
            } else {
                Log.v(LOG_TAG, "Removed filter: ${filter.sqlStatement}")
            }
            return this
        }

        fun removePropertyFilters(property: String) {
            for (filter in filters) {
                if (property in filter.sqlStatement) {
                    removeFilter(filter)
                }
            }
        }

        fun hasNoFilters() = filters.isEmpty()

        private fun combineFilters(): Filter {
            Log.v(LOG_TAG, "@combineFilters [start], size = " + filters.size)
            when (filters.size) {
                0 -> throw IllegalStateException("you must add at least one filter to the query builder")
                1 -> return filters[0]
                2 -> return Filters.and(filters[0], filters[1])
                else -> {
                    // We need to make an array excluding the first two elements for the vararg
                    val moreFilters = filters.slice(IntRange(2, filters.size - 1)).toTypedArray()

                    return Filters.and(filters[0], filters[1], *moreFilters)
                }
            }
        }

        fun build(): Query {
            return Query(combineFilters())
        }
    }
}
