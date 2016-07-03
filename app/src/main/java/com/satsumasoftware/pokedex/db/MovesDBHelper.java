package com.satsumasoftware.pokedex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.satsumasoftware.pokedex.entities.move.MiniMove;
import com.satsumasoftware.pokedex.entities.move.Move;

import java.util.ArrayList;

public class MovesDBHelper extends SQLiteOpenHelper {

    /* General Database and Table information */
    private static final String DATABASE_NAME = "moves.db";
    public static final String TABLE_NAME = "moves";
    public static final int DATABASE_VERSION = 2;

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
        PokeDB pokeDB = new PokeDB(mContext);
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.Moves.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        db.beginTransaction();
        while (!cursor.isAfterLast()) {
            ContentValues values = new ContentValues();

            int moveId = cursor.getInt(cursor.getColumnIndex(PokeDB.Moves.COL_ID));
            values.put(COL_ID, moveId);

            // the identifier is not used thus ignored here

            values.put(COL_GENERATION_ID,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.Moves.COL_GENERATION_ID)));
            values.put(COL_TYPE_ID,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.Moves.COL_TYPE_ID)));

            int powerColIndex = cursor.getColumnIndex(PokeDB.Moves.COL_POWER);
            values.put(COL_POWER, cursor.isNull(powerColIndex) ? -1 : cursor.getInt(powerColIndex));

            int ppColIndex = cursor.getColumnIndex(PokeDB.Moves.COL_PP);
            values.put(COL_PP, cursor.isNull(ppColIndex) ? -1 : cursor.getInt(ppColIndex));

            int accuracyColIndex = cursor.getColumnIndex(PokeDB.Moves.COL_ACCURACY);
            values.put(COL_ACCURACY, cursor.isNull(accuracyColIndex) ?
                    -1 : cursor.getInt(accuracyColIndex));

            values.put(COL_PRIORITY,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.Moves.COL_PRIORITY)));
            values.put(COL_TARGET_ID,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.Moves.COL_TARGET_ID)));
            values.put(COL_DAMAGE_CLASS_ID,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.Moves.COL_DAMAGE_CLASS_ID)));
            values.put(COL_EFFECT_ID,
                    cursor.getInt(cursor.getColumnIndex(PokeDB.Moves.COL_EFFECT_ID)));

            int effectChanceColIndex = cursor.getColumnIndex(PokeDB.Moves.COL_EFFECT_CHANCE);
            values.put(COL_EFFECT_CHANCE, cursor.isNull(effectChanceColIndex) ?
                    -1 : cursor.getInt(effectChanceColIndex));

            int contestTypeIdColIndex = cursor.getColumnIndex(PokeDB.Moves.COL_CONTEST_TYPE_ID);
            values.put(COL_CONTEST_TYPE_ID, cursor.isNull(contestTypeIdColIndex) ?
                    -1 : cursor.getInt(contestTypeIdColIndex));

            int contestEffectIdColIndex = cursor.getColumnIndex(PokeDB.Moves.COL_CONTEST_EFFECT_ID);
            values.put(COL_CONTEST_EFFECT_ID, cursor.isNull(contestEffectIdColIndex) ?
                    -1 : cursor.getInt(contestEffectIdColIndex));

            int superContestEffectIdColIndex =
                    cursor.getColumnIndex(PokeDB.Moves.COL_SUPER_CONTEST_EFFECT_ID);
            values.put(COL_SUPER_CONTEST_EFFECT_ID, cursor.isNull(superContestEffectIdColIndex) ?
                    -1 : cursor.getInt(superContestEffectIdColIndex));

            putNameValues(values, moveId, pokeDB);

            db.insert(TABLE_NAME, null, values);

            cursor.moveToNext();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        pokeDB.close();
        db.close();
    }

    private void putNameValues(ContentValues values, int moveId, PokeDB pokeDB) {
        Cursor cursor = pokeDB.getReadableDatabase().query(
                PokeDB.MoveNames.TABLE_NAME,
                null,
                PokeDB.MoveNames.COL_MOVE_ID + "=?",
                new String[] {String.valueOf(moveId)},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int languageId =
                    cursor.getInt(cursor.getColumnIndex(PokeDB.MoveNames.COL_LOCAL_LANGUAGE_ID));
            String name =
                    cursor.getString(cursor.getColumnIndex(PokeDB.MoveNames.COL_NAME));

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
                    break;
                default:
                    throw new IllegalArgumentException("language id '" +
                            String.valueOf(languageId) + "' is invalid");
            }
            cursor.moveToNext();
        }
        cursor.close();
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
