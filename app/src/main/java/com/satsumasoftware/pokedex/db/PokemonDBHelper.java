package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.satsumasoftware.pokedex.framework.pokemon.BasePokemon;
import com.satsumasoftware.pokedex.framework.pokemon.MiniPokemon;

import java.util.ArrayList;

public final class PokemonDBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "PokemonDBHelper";

    /*
     * General Database and Table information
     */

    private static final String DATABASE_NAME = "pokemon.db";
    public static final String TABLE_NAME = "pokemon";
    public static final int DATABASE_VERSION = 10;

    /*
     * All Column Names
     */

    public static final String COL_ID = "id";
    public static final String COL_SPECIES_ID = "species_id";
    public static final String COL_HEIGHT = "height";
    public static final String COL_WEIGHT = "weight";
    public static final String COL_BASE_EXPERIENCE = "base_experience";
    public static final String COL_POKEMON_ORDER = "pokemon_order";
    public static final String COL_IS_DEFAULT = "is_default";

    public static final String COL_ABILITY_1_ID = "ability_1";
    public static final String COL_ABILITY_2_ID = "ability_2";
    public static final String COL_ABILITY_HIDDEN_ID = "ability_hidden";

    public static final String COL_TYPE_1_ID = "type_1";
    public static final String COL_TYPE_2_ID = "type_2";

    public static final String COL_STAT_HP = "stat_hp";
    public static final String COL_STAT_ATK = "stat_atk";
    public static final String COL_STAT_DEF = "stat_def";
    public static final String COL_STAT_SPA = "stat_spa";
    public static final String COL_STAT_SPD = "stat_spd";
    public static final String COL_STAT_SPE = "stat_spe";
    public static final String COL_STAT_HP_EV = "stat_hp_effort";
    public static final String COL_STAT_ATK_EV = "stat_atk_effort";
    public static final String COL_STAT_DEF_EV = "stat_def_effort";
    public static final String COL_STAT_SPA_EV = "stat_spa_effort";
    public static final String COL_STAT_SPD_EV = "stat_spd_effort";
    public static final String COL_STAT_SPE_EV = "stat_spe_effort";

    public static final String COL_GENERATION_ID = "generation_id";
    public static final String COL_EVOLVES_FROM_SPECIES_ID = "evolves_from_species_id";
    public static final String COL_EVOLUTION_CHAIN_ID = "evolution_chain_id";
    public static final String COL_COLOR_ID = "color_id";
    public static final String COL_SHAPE_ID = "shape_id";
    public static final String COL_HABITAT_ID = "habitat_id";
    public static final String COL_GENDER_RATE = "gender_rate";
    public static final String COL_CAPTURE_RATE = "capture_rate";
    public static final String COL_BASE_HAPPINESS = "base_happiness";
    public static final String COL_IS_BABY = "is_baby";
    public static final String COL_HATCH_COUNTER = "hatch_counter";
    public static final String COL_HAS_GENDER_DIFFERENCES = "has_gender_differences";
    public static final String COL_GROWTH_RATE_ID = "growth_rate_id";
    public static final String COL_FORMS_SWITCHABLE = "forms_switchable";
    public static final String COL_SPECIES_ORDER = "order_species";
    public static final String COL_SPECIES_CONQUEST_ORDER = "conquest_order_species";

    public static final String COL_EGG_GROUP_1_ID = "egg_group_1";
    public static final String COL_EGG_GROUP_2_ID = "egg_group_2";

    public static final String COL_POKEDEX_NATIONAL = "pokedex_number_national";
    public static final String COL_POKEDEX_KANTO = "pokedex_number_kanto";
    public static final String COL_POKEDEX_ORIGINAL_JOHTO = "pokedex_number_original_johto";
    public static final String COL_POKEDEX_HOENN = "pokedex_number_hoenn";
    public static final String COL_POKEDEX_ORIGINAL_SINNOH = "pokedex_number_original_sinnoh";
    public static final String COL_POKEDEX_EXTENDED_SINNOH = "pokedex_number_extended_sinnoh";
    public static final String COL_POKEDEX_UPDATED_JOHTO = "pokedex_number_updated_johto";
    public static final String COL_POKEDEX_ORIGINAL_UNOVA = "pokedex_number_original_unova";
    public static final String COL_POKEDEX_UPDATED_UNOVA = "pokedex_number_updated_unova";
    public static final String COL_POKEDEX_CONQUEST_GALLERY = "pokedex_number_conquest_gallery";
    public static final String COL_POKEDEX_KALOS_CENTRAL = "pokedex_number_kalos_central";
    public static final String COL_POKEDEX_KALOS_COASTAL = "pokedex_number_kalos_coastal";
    public static final String COL_POKEDEX_KALOS_MOUNTAIN = "pokedex_number_kalos_mountain";
    public static final String COL_POKEDEX_UPDATED_HOENN = "pokedex_number_updated_hoenn";

    public static final String COL_NAME = "name_en";
    public static final String COL_NAME_JAPANESE = "name_ja";
    public static final String COL_NAME_ROMAJI = "name_romaji";
    public static final String COL_NAME_KOREAN = "name_ko";
    public static final String COL_NAME_CHINESE = "name_zh";
    public static final String COL_NAME_FRENCH = "name_fr";
    public static final String COL_NAME_GERMAN = "name_de";
    public static final String COL_NAME_SPANISH = "name_es";
    public static final String COL_NAME_ITALIAN = "name_it";
    public static final String COL_GENUS = "genus_en";
    public static final String COL_GENUS_JAPANESE = "genus_ja";
    public static final String COL_GENUS_ROMAJI = "genus_romaji";
    public static final String COL_GENUS_KOREAN = "genus_ko";
    public static final String COL_GENUS_CHINESE = "genus_zh";
    public static final String COL_GENUS_FRENCH = "genus_fr";
    public static final String COL_GENUS_GERMAN = "genus_de";
    public static final String COL_GENUS_SPANISH = "genus_es";
    public static final String COL_GENUS_ITALIAN = "genus_it";

    public static final String COL_FORM_ID = "form_id";
    public static final String COL_FORM_INTRODUCED_IN_VERSION_GROUP_ID =
            "form_introduced_in_version_group_id";
    public static final String COL_FORM_IS_DEFAULT = "form_is_default";
    public static final String COL_FORM_IS_BATTLE_ONLY = "form_is_battle_only";
    public static final String COL_FORM_IS_MEGA = "form_is_mega";
    public static final String COL_FORM_ORDER_OF_FORM = "form_order_of_form";
    public static final String COL_FORM_ORDER = "form_order";

    public static final String COL_FORM_NAME = "form_name_en";
    public static final String COL_FORM_NAME_JAPANESE = "form_name_ja";
    public static final String COL_FORM_NAME_KOREAN = "form_name_ko";
    public static final String COL_FORM_NAME_FRENCH = "form_name_fr";
    public static final String COL_FORM_NAME_GERMAN = "form_name_de";
    public static final String COL_FORM_NAME_SPANISH = "form_name_es";
    public static final String COL_FORM_NAME_ITALIAN = "form_name_it";
    public static final String COL_FORM_POKEMON_NAME = "pokemon_name_en";
    public static final String COL_FORM_POKEMON_NAME_FRENCH = "pokemon_name_fr";


    /*
     * SQL CREATE command creates all columns as defined above
     */

    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +

            COL_ID + " INTEGER, " +
            COL_SPECIES_ID + " INTEGER, " +
            COL_HEIGHT + " INTEGER, " +
            COL_WEIGHT + " INTEGER, " +
            COL_BASE_EXPERIENCE + " INTEGER, " +
            COL_POKEMON_ORDER + " INTEGER, " +
            COL_IS_DEFAULT + " INTEGER, " +

            COL_ABILITY_1_ID + " INTEGER, " +
            COL_ABILITY_2_ID + " INTEGER, " +
            COL_ABILITY_HIDDEN_ID + " INTEGER, " +

            COL_TYPE_1_ID + " INTEGER, " +
            COL_TYPE_2_ID + " INTEGER, " +

            COL_STAT_HP + " INTEGER, " +
            COL_STAT_ATK + " INTEGER, " +
            COL_STAT_DEF + " INTEGER, " +
            COL_STAT_SPA + " INTEGER, " +
            COL_STAT_SPD + " INTEGER, " +
            COL_STAT_SPE + " INTEGER, " +
            COL_STAT_HP_EV + " INTEGER, " +
            COL_STAT_ATK_EV + " INTEGER, " +
            COL_STAT_DEF_EV + " INTEGER, " +
            COL_STAT_SPA_EV + " INTEGER, " +
            COL_STAT_SPD_EV + " INTEGER, " +
            COL_STAT_SPE_EV + " INTEGER, " +

            COL_GENERATION_ID + " INTEGER, " +
            COL_EVOLVES_FROM_SPECIES_ID + " INTEGER, " +
            COL_EVOLUTION_CHAIN_ID + " INTEGER, " +
            COL_COLOR_ID + " INTEGER, " +
            COL_SHAPE_ID + " INTEGER, " +
            COL_HABITAT_ID + " INTEGER, " +
            COL_GENDER_RATE + " INTEGER, " +
            COL_CAPTURE_RATE + " INTEGER, " +
            COL_BASE_HAPPINESS + " INTEGER, " +
            COL_IS_BABY + " INTEGER, " +
            COL_HATCH_COUNTER + " INTEGER, " +
            COL_HAS_GENDER_DIFFERENCES + " INTEGER, " +
            COL_GROWTH_RATE_ID + " INTEGER, " +
            COL_FORMS_SWITCHABLE + " INTEGER, " +
            COL_SPECIES_ORDER + " INTEGER, " +
            COL_SPECIES_CONQUEST_ORDER + " INTEGER, " +

            COL_EGG_GROUP_1_ID + " INTEGER, " +
            COL_EGG_GROUP_2_ID + " INTEGER, " +

            COL_POKEDEX_NATIONAL + " INTEGER, " +
            COL_POKEDEX_KANTO + " INTEGER, " +
            COL_POKEDEX_ORIGINAL_JOHTO + " INTEGER, " +
            COL_POKEDEX_HOENN + " INTEGER, " +
            COL_POKEDEX_ORIGINAL_SINNOH + " INTEGER, " +
            COL_POKEDEX_EXTENDED_SINNOH + " INTEGER, " +
            COL_POKEDEX_UPDATED_JOHTO + " INTEGER, " +
            COL_POKEDEX_ORIGINAL_UNOVA + " INTEGER, " +
            COL_POKEDEX_UPDATED_UNOVA + " INTEGER, " +
            COL_POKEDEX_CONQUEST_GALLERY + " INTEGER, " +
            COL_POKEDEX_KALOS_CENTRAL + " INTEGER, " +
            COL_POKEDEX_KALOS_COASTAL + " INTEGER, " +
            COL_POKEDEX_KALOS_MOUNTAIN + " INTEGER, " +
            COL_POKEDEX_UPDATED_HOENN + " INTEGER, " +

            COL_NAME + " TEXT, " +
            COL_NAME_JAPANESE + " TEXT, " +
            COL_NAME_ROMAJI + " TEXT, " +
            COL_NAME_KOREAN + " TEXT, " +
            COL_NAME_CHINESE + " TEXT, " +
            COL_NAME_FRENCH + " TEXT, " +
            COL_NAME_GERMAN + " TEXT, " +
            COL_NAME_SPANISH + " TEXT, " +
            COL_NAME_ITALIAN + " TEXT, " +
            COL_GENUS + " TEXT, " +
            COL_GENUS_JAPANESE + " TEXT, " +
            COL_GENUS_ROMAJI + " TEXT, " +
            COL_GENUS_KOREAN + " TEXT, " +
            COL_GENUS_CHINESE + " TEXT, " +
            COL_GENUS_FRENCH + " TEXT, " +
            COL_GENUS_GERMAN + " TEXT, " +
            COL_GENUS_SPANISH + " TEXT, " +
            COL_GENUS_ITALIAN + " TEXT, " +

            COL_FORM_ID + " INTEGER, " +
            COL_FORM_INTRODUCED_IN_VERSION_GROUP_ID + " INTEGER, " +
            COL_FORM_IS_DEFAULT + " INTEGER, " +
            COL_FORM_IS_BATTLE_ONLY + " INTEGER, " +
            COL_FORM_IS_MEGA + " INTEGER, " +
            COL_FORM_ORDER_OF_FORM + " INTEGER, " +
            COL_FORM_ORDER + " INTEGER, " +

            COL_FORM_NAME + " TEXT, " +
            COL_FORM_NAME_JAPANESE + " TEXT, " +
            COL_FORM_NAME_KOREAN + " TEXT, " +
            COL_FORM_NAME_FRENCH + " TEXT, " +
            COL_FORM_NAME_GERMAN + " TEXT, " +
            COL_FORM_NAME_SPANISH + " TEXT, " +
            COL_FORM_NAME_ITALIAN + " TEXT, " +
            COL_FORM_POKEMON_NAME + " TEXT, " +
            COL_FORM_POKEMON_NAME_FRENCH + " TEXT" +
            ")";

    /*
     * SQL DROP command deletes the SQL table
     */

    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    /*
     * Main part of the class
     */

    private Context mContext;

    private static PokemonDBHelper sInstance;

    public static synchronized PokemonDBHelper getInstance(Context context) {
        if (sInstance == null) {
            // using the application context prevents accidentally leaking an Activity's context
            sInstance = new PokemonDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private PokemonDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "Creating database");
        db.execSQL(SQL_CREATE);
        populateDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "Upgrading database");
        db.execSQL(SQL_DELETE);
        onCreate(db);
    }

    private void populateDatabase(SQLiteDatabase db) {
        /*
         * The structure of the tables goes like this:
         *      forms --> pokemon --> species
         * In the first two tables, there are ids that are used to lead to the next tables
         */

        PokeDB pokeDB = PokeDB.getInstance(mContext);
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.PokemonForms.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        db.beginTransaction();
        while (!cursor.isAfterLast()) {
            ContentValues values = new ContentValues();

            int formId = cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonForms.COL_ID));
            int pokemonId = cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonForms.COL_POKEMON_ID));

            values.put(COL_FORM_ID, formId);
            // pokemon identifier will not be used so it's not put in the db
            // form identifier will not be used so it's not put in the db
            values.put(COL_ID, pokemonId);

            values.put(COL_FORM_INTRODUCED_IN_VERSION_GROUP_ID,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonForms.COL_INTRODUCED_IN_VERSION_GROUP_ID)));
            values.put(COL_FORM_IS_DEFAULT,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonForms.COL_IS_DEFAULT)));
            values.put(COL_FORM_IS_BATTLE_ONLY,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonForms.COL_IS_BATTLE_ONLY)));
            values.put(COL_FORM_IS_MEGA,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonForms.COL_IS_MEGA)));

            values.put(COL_FORM_ORDER_OF_FORM,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonForms.COL_FORM_ORDER)));
            values.put(COL_FORM_ORDER,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonForms.COL_ORDER)));

            putPokemonValues(values, pokemonId, pokeDB);
            putFormNameValues(values, formId, pokeDB);

            db.insert(TABLE_NAME, null, values);

            Log.d(LOG_TAG, "Added Pokemon (form) entry of id " + formId);

            cursor.moveToNext();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        pokeDB.close();  // after all Pokemon data has been inserted
    }

    private void putPokemonValues(ContentValues values, int pokemonId, PokeDB pokeDB) {
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.Pokemon.TABLE_NAME,
                null,
                PokeDB.Pokemon.COL_ID + "=?",
                new String[] {String.valueOf(pokemonId)},
                null, null, null);
        cursor.moveToFirst();

        // the id (pokemonId) has already been added so we don't need to worry about it
        // the identifier is not used so it's not added

        int speciesId = cursor.getInt(cursor.getColumnIndex(PokeDB.Pokemon.COL_SPECIES_ID));
        values.put(COL_SPECIES_ID, speciesId);

        values.put(COL_HEIGHT,
                cursor.getInt(cursor.getColumnIndex(PokeDB.Pokemon.COL_HEIGHT)));
        values.put(COL_WEIGHT,
                cursor.getInt(cursor.getColumnIndex(PokeDB.Pokemon.COL_WEIGHT)));

        values.put(COL_BASE_EXPERIENCE,
                cursor.getInt(cursor.getColumnIndex(PokeDB.Pokemon.COL_BASE_EXP)));
        values.put(COL_POKEMON_ORDER,
                cursor.getInt(cursor.getColumnIndex(PokeDB.Pokemon.COL_ORDER)));
        values.put(COL_IS_DEFAULT,
                cursor.getInt(cursor.getColumnIndex(PokeDB.Pokemon.COL_IS_DEFAULT)));

        putAbilityValues(values, pokemonId, pokeDB);
        putTypeValues(values, pokemonId, pokeDB);
        putStatValues(values, pokemonId, pokeDB);

        putSpeciesValues(values, speciesId, pokeDB);
        putSpeciesEggGroupValues(values, speciesId, pokeDB);
        putSpeciesPokedexValues(values, speciesId, pokeDB);
        putSpeciesNameValues(values, speciesId, pokeDB);

        cursor.close();
    }

    private void putFormNameValues(ContentValues values, int formId, PokeDB pokeDB) {
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.PokemonFormNames.TABLE_NAME,
                null,
                PokeDB.PokemonFormNames.COL_POKEMON_FORM_ID + "=?",
                new String[] {String.valueOf(formId)},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int languageId =
                    cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonFormNames.COL_LOCAL_LANGUAGE_ID));
            String formName =
                    cursor.getString(cursor.getColumnIndex(PokeDB.PokemonFormNames.COL_FORM_NAME));
            String pokemonName =
                    cursor.getString(cursor.getColumnIndex(PokeDB.PokemonFormNames.COL_POKEMON_NAME));

            switch (languageId) {
                case 1:
                    values.put(COL_FORM_NAME_JAPANESE, formName);
                    break;
                case 3:
                    values.put(COL_FORM_NAME_KOREAN, formName);
                    break;
                case 5:
                    values.put(COL_FORM_NAME_FRENCH, formName);
                    values.put(COL_FORM_POKEMON_NAME_FRENCH, pokemonName);
                    break;
                case 6:
                    values.put(COL_FORM_NAME_GERMAN, formName);
                    break;
                case 7:
                    values.put(COL_FORM_NAME_SPANISH, formName);
                    break;
                case 8:
                    values.put(COL_FORM_NAME_ITALIAN, formName);
                    break;
                case 9:
                    values.put(COL_FORM_NAME, formName);
                    values.put(COL_FORM_POKEMON_NAME, pokemonName);
                    break;
                default:
                    throw new IllegalArgumentException("language id '" + languageId +
                            "' is invalid");
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    private void putAbilityValues(ContentValues values, int pokemonId, PokeDB pokeDB) {
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.PokemonAbilities.TABLE_NAME,
                null,
                PokeDB.PokemonAbilities.COL_POKEMON_ID + "=?",
                new String[] {String.valueOf(pokemonId)},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int abilityId = cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonAbilities.COL_ABILITY_ID));
            int slot = cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonAbilities.COL_SLOT));
            switch (slot) {
                case 1:
                    values.put(COL_ABILITY_1_ID, abilityId);
                    break;
                case 2:
                    values.put(COL_ABILITY_2_ID, abilityId);
                    break;
                case 3:
                    values.put(COL_ABILITY_HIDDEN_ID, abilityId);
                    break;
                default:
                    throw new IllegalArgumentException("slot '" + slot + "' is invalid");
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    private void putTypeValues(ContentValues values, int pokemonId, PokeDB pokeDB) {
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.PokemonTypes.TABLE_NAME,
                null,
                PokeDB.PokemonTypes.COL_POKEMON_ID + "=?",
                new String[] {String.valueOf(pokemonId)},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int typeId = cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonTypes.COL_TYPE_ID));
            int slot = cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonTypes.COL_SLOT));

            switch (slot) {
                case 1:
                    values.put(COL_TYPE_1_ID, typeId);
                    break;
                case 2:
                    values.put(COL_TYPE_2_ID, typeId);
                    break;
                default:
                    throw new IllegalArgumentException("slot '" + slot + "' is invalid");
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    private void putStatValues(ContentValues values, int pokemonId, PokeDB pokeDB) {
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.PokemonStats.TABLE_NAME,
                null,
                PokeDB.PokemonStats.COL_POKEMON_ID + "=?",
                new String[] {String.valueOf(pokemonId)},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int statId = cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonStats.COL_STAT_ID));
            int baseStat = cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonStats.COL_BASE_STAT));
            int statEV = cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonStats.COL_EFFORT));

            switch (statId) {
                case 1:
                    values.put(COL_STAT_HP, baseStat);
                    values.put(COL_STAT_HP_EV, statEV);
                    break;
                case 2:
                    values.put(COL_STAT_ATK, baseStat);
                    values.put(COL_STAT_ATK_EV, statEV);
                    break;
                case 3:
                    values.put(COL_STAT_DEF, baseStat);
                    values.put(COL_STAT_DEF_EV, statEV);
                    break;
                case 4:
                    values.put(COL_STAT_SPA, baseStat);
                    values.put(COL_STAT_SPA_EV, statEV);
                    break;
                case 5:
                    values.put(COL_STAT_SPD, baseStat);
                    values.put(COL_STAT_SPD_EV, statEV);
                    break;
                case 6:
                    values.put(COL_STAT_SPE, baseStat);
                    values.put(COL_STAT_SPE_EV, statEV);
                    break;
                default:
                    throw new IllegalArgumentException("slot (statId) '" + statId + "' is invalid");
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    private void putSpeciesValues(ContentValues values, int speciesId, PokeDB pokeDB) {
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.PokemonSpecies.TABLE_NAME,
                null,
                PokeDB.PokemonSpecies.COL_ID + "=?",
                new String[] {String.valueOf(speciesId)},
                null, null, null);
        cursor.moveToFirst();

        // species id already put in the database)
        // species identifier not used so it is not put in the db

        values.put(COL_GENERATION_ID,
                cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonSpecies.COL_GENERATION_ID)));

        values.put(COL_EVOLVES_FROM_SPECIES_ID,
                cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonSpecies.COL_EVOLVES_FROM_SPECIES_ID)));
        values.put(COL_EVOLUTION_CHAIN_ID,
                cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonSpecies.COL_EVOLUTION_CHAIN_ID)));

        values.put(COL_COLOR_ID,
                cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonSpecies.COL_COLOR_ID)));
        values.put(COL_SHAPE_ID,
                cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonSpecies.COL_SHAPE_ID)));
        values.put(COL_HABITAT_ID,
                cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonSpecies.COL_HABITAT_ID)));

        values.put(COL_GENDER_RATE,
                cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonSpecies.COL_GENDER_RATE)));
        values.put(COL_CAPTURE_RATE,
                cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonSpecies.COL_CAPTURE_RATE)));
        values.put(COL_BASE_HAPPINESS,
                cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonSpecies.COL_BASE_HAPPINESS)));
        values.put(COL_IS_BABY,
                cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonSpecies.COL_IS_BABY)));
        values.put(COL_HATCH_COUNTER,
                cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonSpecies.COL_HATCH_COUNTER)));
        values.put(COL_HAS_GENDER_DIFFERENCES,
                cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonSpecies.COL_HAS_GENDER_DIFFERENCES)));
        values.put(COL_GROWTH_RATE_ID,
                cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonSpecies.COL_GROWTH_RATE_ID)));
        values.put(COL_FORMS_SWITCHABLE,
                cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonSpecies.COL_FORMS_SWITCHABLE)));
        values.put(COL_SPECIES_ORDER,
                cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonSpecies.COL_ORDER)));
        values.put(COL_SPECIES_CONQUEST_ORDER,
                cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonSpecies.COL_CONQUEST_ORDER)));

        cursor.close();
    }

    private void putSpeciesEggGroupValues(ContentValues values, int speciesId, PokeDB pokeDB) {
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.PokemonEggGroups.TABLE_NAME,
                null,
                PokeDB.PokemonEggGroups.COL_SPECIES_ID + "=?",
                new String[] {String.valueOf(speciesId)},
                null, null, null);
        cursor.moveToFirst();
        boolean hasAddedEggGroup1 = false;
        while (!cursor.isAfterLast()) {
            int eggGroupId =
                    cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonEggGroups.COL_EGG_GROUP_ID));

            if (!hasAddedEggGroup1) {
                values.put(COL_EGG_GROUP_1_ID, eggGroupId);
                hasAddedEggGroup1 = true;
            } else {
                values.put(COL_EGG_GROUP_2_ID, eggGroupId);
                break;
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    private void putSpeciesPokedexValues(ContentValues values, int speciesId, PokeDB pokeDB) {
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.PokemonDexNumbers.TABLE_NAME,
                null,
                PokeDB.PokemonDexNumbers.COL_SPECIES_ID + "=?",
                new String[] {String.valueOf(speciesId)},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int pokedexId =
                    cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonDexNumbers.COL_POKEDEX_ID));
            int pokedexNumber =
                    cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonDexNumbers.COL_POKEDEX_NUMBER));

            switch (pokedexId) {
                case 1:
                    values.put(COL_POKEDEX_NATIONAL, pokedexNumber);
                    break;
                case 2:
                    values.put(COL_POKEDEX_KANTO, pokedexNumber);
                    break;
                case 3:
                    values.put(COL_POKEDEX_ORIGINAL_JOHTO, pokedexNumber);
                    break;
                case 4:
                    values.put(COL_POKEDEX_HOENN, pokedexNumber);
                    break;
                case 5:
                    values.put(COL_POKEDEX_ORIGINAL_SINNOH, pokedexNumber);
                    break;
                case 6:
                    values.put(COL_POKEDEX_EXTENDED_SINNOH, pokedexNumber);
                    break;
                case 7:
                    values.put(COL_POKEDEX_UPDATED_JOHTO, pokedexNumber);
                    break;
                case 8:
                    values.put(COL_POKEDEX_ORIGINAL_UNOVA, pokedexNumber);
                    break;
                case 9:
                    values.put(COL_POKEDEX_UPDATED_UNOVA, pokedexNumber);
                    break;
                case 11:
                    values.put(COL_POKEDEX_CONQUEST_GALLERY, pokedexNumber);
                    break;
                case 12:
                    values.put(COL_POKEDEX_KALOS_CENTRAL, pokedexNumber);
                    break;
                case 13:
                    values.put(COL_POKEDEX_KALOS_COASTAL, pokedexNumber);
                    break;
                case 14:
                    values.put(COL_POKEDEX_KALOS_MOUNTAIN, pokedexNumber);
                    break;
                case 15:
                    values.put(COL_POKEDEX_UPDATED_HOENN, pokedexNumber);
                    break;
                default:
                    throw new IllegalArgumentException("pokedex id '" + pokedexId + "' is invalid");
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    private void putSpeciesNameValues(ContentValues values, int speciesId, PokeDB pokeDB) {
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.PokemonSpeciesNames.TABLE_NAME,
                null,
                PokeDB.PokemonSpeciesNames.COL_POKEMON_SPECIES_ID + "=?",
                new String[] {String.valueOf(speciesId)},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int languageId =
                    cursor.getInt(cursor.getColumnIndex(PokeDB.PokemonSpeciesNames.COL_LOCAL_LANGUAGE_ID));
            String name =
                    cursor.getString(cursor.getColumnIndex(PokeDB.PokemonSpeciesNames.COL_NAME));
            String genus =
                    cursor.getString(cursor.getColumnIndex(PokeDB.PokemonSpeciesNames.COL_GENUS));

            switch (languageId) {
                case 1:
                    values.put(COL_NAME_JAPANESE, name);
                    values.put(COL_GENUS_JAPANESE, genus);
                    break;
                case 2:
                    values.put(COL_NAME_ROMAJI, name);
                    values.put(COL_GENUS_ROMAJI, genus);
                    break;
                case 3:
                    values.put(COL_NAME_KOREAN, name);
                    values.put(COL_GENUS_KOREAN, genus);
                    break;
                case 4:
                    values.put(COL_NAME_CHINESE, name);
                    values.put(COL_GENUS_CHINESE, genus);
                    break;
                case 5:
                    values.put(COL_NAME_FRENCH, name);
                    values.put(COL_GENUS_FRENCH, genus);
                    break;
                case 6:
                    values.put(COL_NAME_GERMAN, name);
                    values.put(COL_GENUS_GERMAN, genus);
                    break;
                case 7:
                    values.put(COL_NAME_SPANISH, name);
                    values.put(COL_GENUS_SPANISH, genus);
                    break;
                case 8:
                    values.put(COL_NAME_ITALIAN, name);
                    values.put(COL_GENUS_ITALIAN, genus);
                    break;
                case 9:
                    values.put(COL_NAME, name);
                    values.put(COL_GENUS, genus);
                    break;
                default:
                    throw new IllegalArgumentException("language id '" + languageId +
                            "' is invalid");
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    public ArrayList<MiniPokemon> getAllPokemon() {
        ArrayList<MiniPokemon> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                BasePokemon.DB_COLUMNS,
                COL_FORM_IS_DEFAULT + "=1",
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MiniPokemon pokemon = new MiniPokemon(cursor);
            list.add(pokemon);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
