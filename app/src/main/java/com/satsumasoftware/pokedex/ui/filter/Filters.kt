package com.satsumasoftware.pokedex.ui.filter

/**
 * A collection of static helper functions for creating or combining filters
 */
class Filters {

    companion object {

        @JvmStatic
        fun equal(property: String, value: String): Filter {
            return Filter("$property = $value")
        }

        @JvmStatic
        fun notEqual(property: String, value: String): Filter {
            return Filter("$property != $value")
        }

        @JvmStatic
        fun like(property: String, value: String): Filter {
            return Filter("$property LIKE $value")
        }

        @JvmStatic
        fun likeIgnoreCase(property: String, value: String): Filter {
            return Filter("LOWER($property) LIKE ${value.toLowerCase()}")
        }

        @JvmStatic
        fun and(filter1: Filter, filter2: Filter, vararg moreFilters: Filter): Filter {
            var sql: String = "$filter1 AND $filter2"

            for (filter in moreFilters) {
                sql = "$sql AND $filter"
            }

            return Filter(sql)
        }

        @JvmStatic
        fun or(filter1: Filter, filter2: Filter, vararg moreFilters: Filter): Filter {
            var sql: String = "$filter1 OR $filter2"

            for (filter in moreFilters) {
                sql = "$sql OR $filter"
            }

            return Filter(sql)
        }

        @JvmStatic
        fun lowercase(property: String): Filter {
            return Filter("LOWER($property)")
        }

    }
}
