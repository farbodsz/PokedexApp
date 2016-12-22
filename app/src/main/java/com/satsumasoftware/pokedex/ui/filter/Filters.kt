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
        fun and(filter1: Filter, filter2: Filter): Filter {
            return Filter("$filter1 AND $filter2")
        }

        @JvmStatic
        fun or(filter1: Filter, filter2: Filter): Filter {
            return Filter("$filter1 OR $filter2")
        }

        @JvmStatic
        fun lowercase(property: String): Filter {
            return Filter("LOWER($property)")
        }

    }
}
