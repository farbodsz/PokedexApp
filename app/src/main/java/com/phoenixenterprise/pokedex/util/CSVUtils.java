package com.phoenixenterprise.pokedex.util;

public final class CSVUtils {


    /*
     * Types of comma separators in CSV files
     */

    private static final String SEP_SINGLE = ",";
    private static final String SEP_DOUBLE = ",,";
    private final static String SEP_CHANGELOG = ",\",\",";


    /*
     * CSV tables
     */

    public static final String POKEDEX = "data/PokedexDB_v9.csv";
    public static final String ABILITYDEX = "data/AbilityDB_v2.csv";
    public static final String MOVEDEX = "data/MoveDB_v2.csv";
    public static final String NATUREDEX = "data/NatureDB_v2.csv";

    public static final String LEARNSET_DB = "data/LearnsetDB_v3.csv";

    public static final String LOCATIONS_DB = "data/LocationsDB_v2.csv";
    public static final String LOCATION_AREAS_DB = "data/LocationAreasDB_v1.csv";
    public static final String LOCATION_AREA_ENCOUNTER_RATES_DB = "data/LocationAreaEncounterRatesDB_v1.csv";
    public static final String LOCATION_GAME_INDICES_DB = "data/LocationGameIndicesDB_v1.csv";
    public static final String ENCOUNTERS_DB = "data/EncountersDB_v1.csv";
    public static final String ENCOUNTER_SLOTS_DB = "data/EncounterSlotsDB_v2.csv";

    public static final String FORM_IDENTIFIERS_DB = "data/FormIdDB_v1.csv";

    public static final String EXP_INFO_DB = "data/ExperienceDB.csv";

    public static final String CHANGELOG = "data/Changelog.csv";


    /*
     * Separators defined for each CSV table/file
     */

    public static final String POKEDEX_SEP = SEP_SINGLE;
    public static final String ABILITYDEX_SEP = SEP_DOUBLE;
    public static final String MOVEDEX_SEP = SEP_DOUBLE;
    public static final String NATUREDEX_SEP = SEP_SINGLE;

    public static final String LEARNSET_SEP = SEP_SINGLE;

    public static final String LOCATIONS_SEP = SEP_SINGLE;
    public static final String LOCATION_AREAS_SEP = SEP_SINGLE;
    public static final String LOCATION_AREA_ENCOUNTER_RATES_SEP = SEP_SINGLE;
    public static final String LOCATION_GAME_INDICES_SEP = SEP_SINGLE;
    public static final String ENCOUNTERS_SEP = SEP_SINGLE;
    public static final String ENCOUNTER_SLOTS_SEP = SEP_SINGLE;

    public static final String FORM_IDENTIFIERS_SEP = SEP_SINGLE;

    public static final String EXP_INFO_SEP = SEP_SINGLE;

    public static final String CHANGELOG_SEP = SEP_CHANGELOG;
}
