package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.satsumasoftware.pokedex.entities.move.MiniMove;
import com.satsumasoftware.pokedex.entities.move.Move;
import com.satsumasoftware.pokedex.util.CSVUtils;
import com.univocity.parsers.csv.CsvParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovesDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "moves.db";
    public static final String TABLE_NAME = "moves";
    public static final int DATABASE_VERSION = 1;

    /* All Column Names */
    public static final String COL_ID = "id";
    public static final String COL_GENERATION_ID = "generation_id";
    public static final String COL_TYPE_ID = "type_id";
    public static final String COL_POWER = "power";
    public static final String COL_PP = "pp";
    public static final String COL_ACCURACY = "accuracy";
    public static final String COL_PRIORITY = "priority";
    public static final String COL_TARGET_ID = "target_id";
    public static final String COL_DAMAGE_CLASS_ID = "damage_class_id";
    public static final String COL_EFFECT_ID = "effect_id";
    public static final String COL_EFFECT_CHANCE = "effect_chance";
    public static final String COL_CONTEST_TYPE_ID = "contest_type_id";
    public static final String COL_CONTEST_EFFECT_ID = "contest_effect_id";
    public static final String COL_SUPER_CONTEST_EFFECT_ID = "super_contest_effect_id";

    public static final String COL_NAME = "name_en";
    public static final String COL_NAME_JAPANESE = "name_ja";
    public static final String COL_NAME_KOREAN = "name_ko";
    public static final String COL_NAME_FRENCH = "name_fr";
    public static final String COL_NAME_GERMAN = "name_de";
    public static final String COL_NAME_SPANISH = "name_es";
    public static final String COL_NAME_ITALIAN = "name_it";

    /* SQL CREATE command creates all columns as defined above */
    private static final String SQL_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COL_ID + " INTEGER, " +
            COL_GENERATION_ID + " INTEGER, " +
            COL_TYPE_ID + " INTEGER, " +
            COL_POWER + " INTEGER, " +
            COL_PP + " INTEGER, " +
            COL_ACCURACY + " INTEGER, " +
            COL_PRIORITY + " INTEGER, " +
            COL_TARGET_ID + " INTEGER, " +
            COL_DAMAGE_CLASS_ID + " INTEGER, " +
            COL_EFFECT_ID + " INTEGER, " +
            COL_EFFECT_CHANCE + " INTEGER, " +
            COL_CONTEST_TYPE_ID + " INTEGER, " +
            COL_CONTEST_EFFECT_ID + " INTEGER, " +
            COL_SUPER_CONTEST_EFFECT_ID + " INTEGER, " +
            COL_NAME + " TEXT, " +
            COL_NAME_JAPANESE + " TEXT, " +
            COL_NAME_KOREAN + " TEXT, " +
            COL_NAME_FRENCH + " TEXT, " +
            COL_NAME_GERMAN + " TEXT, " +
            COL_NAME_SPANISH + " TEXT, " +
            COL_NAME_ITALIAN + " TEXT" +
            ")";

    /* SQL DROP command deletes the SQL table */
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private Context mContext;


    public MovesDBHelper(Context context) {
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
        CsvParser parser = CSVUtils.getMyParser();
        try {
            db.beginTransaction();
            List<String[]> allRows = parser.parseAll(mContext.getAssets().open(CSVUtils.MOVES));
            for (String[] line : allRows) {

                ContentValues values = new ContentValues();

                int moveId = Integer.parseInt(line[0]);
                values.put(COL_ID, moveId);
                // line[1] (identifier) is not used thus ignored here

                values.put(COL_GENERATION_ID, Integer.parseInt(line[2]));
                values.put(COL_TYPE_ID, Integer.parseInt(line[3]));

                values.put(COL_POWER, (line[4] == null) ? -1 : Integer.parseInt(line[4]));
                values.put(COL_PP, (line[5] == null) ? -1 : Integer.parseInt(line[5]));
                values.put(COL_ACCURACY, (line[6] == null) ? -1 : Integer.parseInt(line[6]));

                values.put(COL_PRIORITY, Integer.parseInt(line[7]));
                values.put(COL_TARGET_ID, Integer.parseInt(line[8]));
                values.put(COL_DAMAGE_CLASS_ID, Integer.parseInt(line[9]));
                values.put(COL_EFFECT_ID, Integer.parseInt(line[10]));
                values.put(COL_EFFECT_CHANCE, (line[11] == null) ? -1 : Integer.parseInt(line[11]));

                values.put(COL_CONTEST_TYPE_ID, (line[12] == null) ? -1 : Integer.parseInt(line[12]));
                values.put(COL_CONTEST_EFFECT_ID, (line[13] == null) ? -1 : Integer.parseInt(line[13]));
                values.put(COL_SUPER_CONTEST_EFFECT_ID, (line[14] == null) ? -1 : Integer.parseInt(line[14]));

                putNameValues(values, Integer.parseInt(line[0]));

                db.insert(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private void putNameValues(ContentValues values, int moveId) {
        CsvParser parser = CSVUtils.getMyParser();
        try {
            List<String[]> allRows = parser.parseAll(mContext.getAssets().open(CSVUtils.MOVE_NAMES));
            for (String[] line : allRows) {

                if (Integer.parseInt(line[0]) == moveId) {
                    int languageId = Integer.parseInt(line[1]);
                    String name = line[2];

                    switch (languageId) {
                        case 1:
                            values.put(COL_NAME_JAPANESE, name);
                            break;
                        case 3:
                            values.put(COL_NAME_KOREAN, name);
                            break;
                        case 5:
                            values.put(COL_NAME_FRENCH, name);
                            break;
                        case 6:
                            values.put(COL_NAME_GERMAN, name);
                            break;
                        case 7:
                            values.put(COL_NAME_SPANISH, name);
                            break;
                        case 8:
                            values.put(COL_NAME_ITALIAN, name);
                            break;
                        case 9:
                            values.put(COL_NAME, name);
                            return;
                        default:
                            throw new IllegalArgumentException("language id '" +
                                    String.valueOf(languageId) + "' is invalid");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Move> getAllMoves() {
        ArrayList<Move> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
            int generationId = cursor.getInt(cursor.getColumnIndex(COL_GENERATION_ID));
            int typeId = cursor.getInt(cursor.getColumnIndex(COL_TYPE_ID));
            int power = cursor.getInt(cursor.getColumnIndex(COL_POWER));
            int pp = cursor.getInt(cursor.getColumnIndex(COL_PP));
            int accuracy = cursor.getInt(cursor.getColumnIndex(COL_ACCURACY));
            int priority = cursor.getInt(cursor.getColumnIndex(COL_PRIORITY));
            int targetId = cursor.getInt(cursor.getColumnIndex(COL_TARGET_ID));
            int damageClassId = cursor.getInt(cursor.getColumnIndex(COL_DAMAGE_CLASS_ID));
            int effectId = cursor.getInt(cursor.getColumnIndex(COL_EFFECT_ID));
            int effectChance = cursor.getInt(cursor.getColumnIndex(COL_EFFECT_CHANCE));
            int contestTypeId = cursor.getInt(cursor.getColumnIndex(COL_CONTEST_TYPE_ID));
            int contestEffectId = cursor.getInt(cursor.getColumnIndex(COL_CONTEST_EFFECT_ID));
            int superContestEffectId = cursor.getInt(cursor.getColumnIndex(COL_SUPER_CONTEST_EFFECT_ID));
            String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
            String nameJa = cursor.getString(cursor.getColumnIndex(COL_NAME_JAPANESE));
            String nameKo = cursor.getString(cursor.getColumnIndex(COL_NAME_KOREAN));
            String nameFr = cursor.getString(cursor.getColumnIndex(COL_NAME_FRENCH));
            String nameDe = cursor.getString(cursor.getColumnIndex(COL_NAME_GERMAN));
            String nameEs = cursor.getString(cursor.getColumnIndex(COL_NAME_SPANISH));
            String nameIt = cursor.getString(cursor.getColumnIndex(COL_NAME_ITALIAN));
            Move move = new Move(id, generationId, typeId, power, pp, accuracy, priority, targetId,
                    damageClassId, effectId, effectChance, contestTypeId, contestEffectId,
                    superContestEffectId, name, nameJa, nameKo, nameFr, nameDe, nameEs, nameIt);
            list.add(move);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<MiniMove> getAllMiniMoves() {
        ArrayList<MiniMove> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                MiniMove.DB_COLUMNS,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
            MiniMove miniMove = new MiniMove(id, name);
            list.add(miniMove);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

}
