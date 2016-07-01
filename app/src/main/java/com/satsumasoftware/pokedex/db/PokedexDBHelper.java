package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.satsumasoftware.pokedex.object.MiniPokemon;
import com.satsumasoftware.pokedex.util.CSVUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PokedexDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "pokedex.db";
    public static final String TABLE_NAME = "national_pokedex";
    public static final int DATABASE_VERSION = 4;

    /* All Column Names */
    public static final String COL_ID = "nat_id";
    public static final String COL_NAME = "name";
    public static final String COL_FORM = "form";
    public static final String COL_SPECIES = "species";
    public static final String COL_STAT_HP = "stat_hp";
    public static final String COL_STAT_ATK = "stat_atk";
    public static final String COL_STAT_DEF = "stat_def";
    public static final String COL_STAT_SPA = "stat_spa";
    public static final String COL_STAT_SPD = "stat_spd";
    public static final String COL_STAT_SPE = "stat_spe";
    public static final String COL_TYPE_1 = "type_1";
    public static final String COL_TYPE_2 = "type_2";
    public static final String COL_ABILITY_1 = "ability_1";
    public static final String COL_ABILITY_2 = "ability_2";
    public static final String COL_ABILITY_H = "ability_hidden";
    public static final String COL_CATCH_RATE = "catch_rate";
    public static final String COL_MASS = "mass_kg";
    public static final String COL_HEIGHT = "height_m";
    public static final String COL_BASE_EGG_STEPS = "base_egg_steps";
    public static final String COL_GENDER = "gender_male_percentage";
    public static final String COL_EXP = "exp_to_100";
    public static final String COL_LEVELLING_RATE = "growth_abbr";
    public static final String COL_GENERATION = "generation";
    public static final String COL_EVOLVES_FROM = "evolves_from_id";
    public static final String COL_EVOLUTION_CHAIN = "evolution_chain_id";
    public static final String COL_COLOUR = "colour_id";
    public static final String COL_SHAPE = "shape_id";
    public static final String COL_HABITAT = "habitat_id";
    public static final String COL_BASE_HAPPINESS = "base_happiness";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_ID + " INTEGER, " +
            COL_NAME + " TEXT, " +
            COL_FORM + " TEXT, " +
            COL_SPECIES + " TEXT, " +
            COL_STAT_HP + " INTEGER, " +
            COL_STAT_ATK + " INTEGER, " +
            COL_STAT_DEF + " INTEGER, " +
            COL_STAT_SPA + " INTEGER, " +
            COL_STAT_SPD + " INTEGER, " +
            COL_STAT_SPE + " INTEGER, " +
            COL_TYPE_1 + " TEXT, " +
            COL_TYPE_2 + " TEXT, " +
            COL_ABILITY_1 + " TEXT, " +
            COL_ABILITY_2 + " TEXT, " +
            COL_ABILITY_H + " TEXT, " +
            COL_CATCH_RATE + " INTEGER, " +
            COL_MASS + " REAL, " +
            COL_HEIGHT + " REAL, " +
            COL_BASE_EGG_STEPS + " INTEGER, " +
            COL_GENDER + " TEXT, " + // TODO: Change this to gender ID? (it is string because of "Genderless" value)
            COL_EXP + " INTEGER, " +
            COL_LEVELLING_RATE + " TEXT, " +
            COL_GENERATION + " INTEGER, " +
            COL_EVOLVES_FROM + " INTEGER, " +
            COL_EVOLUTION_CHAIN + " INTEGER, " +
            COL_COLOUR + " INTEGER, " +
            COL_SHAPE + " INTEGER, " +
            COL_HABITAT + " INTEGER, " +
            COL_BASE_HAPPINESS + " INTEGER" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public PokedexDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
        populateDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE);
        onCreate(db);
    }

    private void populateDatabase(SQLiteDatabase db) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(CSVUtils.POKEDEX)));
            reader.readLine(); // Ignores the first line
            String data;
            while ((data=reader.readLine()) != null)  {
                String[] line = data.split(CSVUtils.POKEDEX_SEP); // Splits the line up into a string array
                if (line.length > 1) {

                    ContentValues values = new ContentValues();

                    values.put(COL_ID, Integer.parseInt(line[0]));
                    values.put(COL_NAME, line[1]);
                    values.put(COL_FORM, line[2]);
                    values.put(COL_SPECIES, line[3]);

                    values.put(COL_STAT_HP, Integer.parseInt(line[4]));
                    values.put(COL_STAT_ATK, Integer.parseInt(line[5]));
                    values.put(COL_STAT_DEF, Integer.parseInt(line[6]));
                    values.put(COL_STAT_SPA, Integer.parseInt(line[7]));
                    values.put(COL_STAT_SPD, Integer.parseInt(line[8]));
                    values.put(COL_STAT_SPE, Integer.parseInt(line[9]));

                    values.put(COL_TYPE_1, line[10]);
                    values.put(COL_TYPE_2, line[11]);
                    values.put(COL_ABILITY_1, line[12]);
                    values.put(COL_ABILITY_2, line[13]);
                    values.put(COL_ABILITY_H, line[14]);

                    values.put(COL_CATCH_RATE, Integer.parseInt(line[15]));
                    values.put(COL_MASS, Double.parseDouble(line[16]));
                    values.put(COL_HEIGHT, Double.parseDouble(line[17]));
                    values.put(COL_BASE_EGG_STEPS, Integer.parseInt(line[18]));
                    values.put(COL_GENDER, line[19]);

                    values.put(COL_EXP, Integer.parseInt(line[20]));
                    values.put(COL_LEVELLING_RATE, line[21]);
                    values.put(COL_GENERATION, Integer.parseInt(line[22]));
                    values.put(COL_EVOLVES_FROM, Integer.parseInt(line[23]));
                    values.put(COL_EVOLUTION_CHAIN, Integer.parseInt(line[24]));

                    values.put(COL_COLOUR, Integer.parseInt(line[25]));
                    values.put(COL_SHAPE, Integer.parseInt(line[26]));
                    values.put(COL_HABITAT, Integer.parseInt(line[27]));
                    values.put(COL_BASE_HAPPINESS, Integer.parseInt(line[28]));

                    db.insert(TABLE_NAME, null, values);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<MiniPokemon> getAllPokemon() {
        // See http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html#query
        // (java.lang.String, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String, java.lang.String, java.lang.String)
        ArrayList<MiniPokemon> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[] {COL_ID, COL_NAME, COL_FORM},
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
            String form = cursor.getString(cursor.getColumnIndex(COL_FORM));
            MiniPokemon pokemon = new MiniPokemon(id, name, form);
            list.add(pokemon);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
