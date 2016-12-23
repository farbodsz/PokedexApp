package com.satsumasoftware.pokedex.query

/**
 * Used to store an SQLite statement that can be used to filter database queries
 */
data class Filter(var sqlStatement: String) {

    init {
        sqlStatement = "($sqlStatement)"
    }

}
