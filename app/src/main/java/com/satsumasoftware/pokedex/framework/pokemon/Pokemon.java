package com.satsumasoftware.pokedex.framework.pokemon;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.util.SparseIntArray;
import android.widget.ImageView;

import com.satsumasoftware.pokedex.db.PokemonDBHelper;
import com.satsumasoftware.pokedex.util.ActionUtils;
import com.satsumasoftware.pokedex.util.AppConfig;

import java.util.ArrayList;
import java.util.Collections;

public class Pokemon {

    // TODO: Sort stuff out here (methods and all that)

    private Context mContext;
    private int mId, mSpeciesId, mFormId, mNationalNumber;
    private String mName, mFormName, mFormPokemonName;

    private ArrayList<String> mIdColumns;


    public Pokemon(Context context, int id, int speciesId, int formId, String name,
                   String formName, String formPokemonName, int nationalNumber) {
        // TODO how about passing MiniPokemon object here to reference?
        mContext = context;
        mId = id;
        mSpeciesId = speciesId;
        mFormId = formId;
        mName = name;
        mFormName = formName;
        mFormPokemonName = formPokemonName;
        mNationalNumber = nationalNumber;
    }


    public MiniPokemon toMiniPokemon() {
        return new MiniPokemon(mId, mSpeciesId, mFormId, mName, mFormName, mFormPokemonName,
                mNationalNumber);
    }


    public int getId() {
        return mId;
    }

    public int getSpeciesId() {
        return mSpeciesId;
    }

    public int getFormId() {
        return mFormId;
    }

    public int getPokemonOrderNumber() {
        return -1;  // TODO FIXME actually return the order number
    }

    public String getName() {
        //return (mName == null) ? findPokemonName() : mName; TODO: get name from db if var is null
        return mName;
    }

    public String getFormName() {
        return mFormName; // TODO see getName()
    }

    public String getFormAndPokemonName() {
        // mFormPokemonName could be "" (if it is the default form)
        return (mFormPokemonName == null) ? mName : mFormPokemonName; // TODO see getName()
    }

    public int getNationalDexNumber() {
        return mNationalNumber;
    }


    private ArrayList<String> createIdColumns() {
        mIdColumns = new ArrayList<>();
        mIdColumns.add(PokemonDBHelper.COL_ID);
        mIdColumns.add(PokemonDBHelper.COL_FORM_ID);
        return mIdColumns;
    }

    private Cursor makeCursor(String[] columnsToSearch) {
        ArrayList<String> colList = (mIdColumns == null ? createIdColumns() : mIdColumns);
        Collections.addAll(colList, columnsToSearch);

        String[] columns = colList.toArray(new String[colList.size()]);
        PokemonDBHelper helper = new PokemonDBHelper(mContext);

        return helper.getReadableDatabase().query(
                PokemonDBHelper.TABLE_NAME,
                columns,
                PokemonDBHelper.COL_ID + "=? AND " + PokemonDBHelper.COL_FORM_ID + "=?",
                new String[] {String.valueOf(mId), String.valueOf(mFormId)},
                null, null, null);
    }


    public SparseIntArray getAbilityIds() {
        SparseIntArray abilityArray = new SparseIntArray(3);
        Cursor cursor = makeCursor(new String[] {
                PokemonDBHelper.COL_ABILITY_1_ID,
                PokemonDBHelper.COL_ABILITY_2_ID,
                PokemonDBHelper.COL_ABILITY_HIDDEN_ID});
        cursor.moveToFirst();
        abilityArray.put(1, cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_ABILITY_1_ID)));
        abilityArray.put(2, cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_ABILITY_2_ID)));
        abilityArray.put(3, cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_ABILITY_HIDDEN_ID)));
        cursor.close();
        return abilityArray;
    }


    public SparseIntArray getTypeIds() {
        SparseIntArray typeArray = new SparseIntArray(2);
        Cursor cursor = makeCursor(new String[] {
                PokemonDBHelper.COL_TYPE_1_ID, PokemonDBHelper.COL_TYPE_2_ID});
        cursor.moveToFirst();
        typeArray.put(1, cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_TYPE_1_ID)));
        typeArray.put(2, cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_TYPE_2_ID)));
        cursor.close();
        return typeArray;
    }

    public ArrayMap<String, Integer> getGenderValues() {
        ArrayMap<String, Integer> genderValues = new ArrayMap<>();
        Cursor cursor = makeCursor(new String[] {
                PokemonDBHelper.COL_HAS_GENDER_DIFFERENCES,
                PokemonDBHelper.COL_GENDER_RATE});
        cursor.moveToFirst();
        genderValues.put("has_gender_differences",
                cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_HAS_GENDER_DIFFERENCES)));
        genderValues.put("gender_rate", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_GENDER_RATE)));
        cursor.close();
        return genderValues;
    }

    public static boolean hasGenderDifferences(ArrayMap<String, Integer> genderValues) {
        return genderValues.get("has_gender_differences") == 1;
    }

    public static int getGenderRate(ArrayMap<String, Integer> genderValues) {
        return genderValues.get("gender_rate");
    }


    public ArrayMap<String, Integer> getStats() {
        ArrayMap<String, Integer> statValues = new ArrayMap<>(6);
        Cursor cursor = makeCursor(new String[] {
                PokemonDBHelper.COL_STAT_HP,
                PokemonDBHelper.COL_STAT_ATK,
                PokemonDBHelper.COL_STAT_DEF,
                PokemonDBHelper.COL_STAT_SPA,
                PokemonDBHelper.COL_STAT_SPD,
                PokemonDBHelper.COL_STAT_SPE});
        cursor.moveToFirst();
        statValues.put("hp", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_STAT_HP)));
        statValues.put("atk", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_STAT_ATK)));
        statValues.put("def", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_STAT_DEF)));
        statValues.put("spa", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_STAT_SPA)));
        statValues.put("spd", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_STAT_SPD)));
        statValues.put("spe", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_STAT_SPE)));
        Log.d("Pokemon stats", "hp = " + cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_STAT_HP)));
        Log.d("Pokemon stats", "atk = " + cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_STAT_ATK)));
        Log.d("Pokemon stats", "spa = " + cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_STAT_SPA)));
        cursor.close();
        return statValues;
    }

    public ArrayMap<String, Integer> getStatEVs() {
        ArrayMap<String, Integer> statEvValues = new ArrayMap<>(6);
        Cursor cursor = makeCursor(new String[] {
                PokemonDBHelper.COL_STAT_HP_EV,
                PokemonDBHelper.COL_STAT_ATK_EV,
                PokemonDBHelper.COL_STAT_DEF_EV,
                PokemonDBHelper.COL_STAT_SPA_EV,
                PokemonDBHelper.COL_STAT_SPD_EV,
                PokemonDBHelper.COL_STAT_SPE_EV});
        cursor.moveToFirst();
        statEvValues.put("hp", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_STAT_HP_EV)));
        statEvValues.put("atk", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_STAT_ATK_EV)));
        statEvValues.put("def", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_STAT_DEF_EV)));
        statEvValues.put("spa", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_STAT_SPA_EV)));
        statEvValues.put("spd", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_STAT_SPD_EV)));
        statEvValues.put("spe", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_STAT_SPE_EV)));
        cursor.close();
        return statEvValues;
    }


    public ArrayMap<String, Integer> getTrainingValues() {
        ArrayMap<String, Integer> trainingValues = new ArrayMap<>(4);
        Cursor cursor = makeCursor(new String[] {
                PokemonDBHelper.COL_CAPTURE_RATE,
                PokemonDBHelper.COL_BASE_HAPPINESS,
                PokemonDBHelper.COL_GROWTH_RATE_ID,
                PokemonDBHelper.COL_BASE_EXPERIENCE});
        cursor.moveToFirst();
        trainingValues.put("capture_rate", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_CAPTURE_RATE)));
        trainingValues.put("base_happiness", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_BASE_HAPPINESS)));
        trainingValues.put("growth_rate", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_GROWTH_RATE_ID)));
        trainingValues.put("base_experience", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_BASE_EXPERIENCE)));
        cursor.close();
        return trainingValues;
    }

    public static int getCaptureRate(ArrayMap<String, Integer> trainingValues) {
        return trainingValues.get("capture_rate");
    }

    public static int getBaseHappiness(ArrayMap<String, Integer> trainingValues) {
        return trainingValues.get("base_happiness");
    }

    public static int getGrowthRateId(ArrayMap<String, Integer> trainingValues) {
        return trainingValues.get("growth_rate");
    }

    public static int getBaseExperience(ArrayMap<String, Integer> trainingValues) {
        return trainingValues.get("base_experience");
    }


    public ArrayMap<String, Integer> getPhysicalAttrs() {
        ArrayMap<String, Integer> physicalValues = new ArrayMap<>(4);
        Cursor cursor = makeCursor(new String[] {
                PokemonDBHelper.COL_HEIGHT,
                PokemonDBHelper.COL_WEIGHT,
                PokemonDBHelper.COL_COLOR_ID,
                PokemonDBHelper.COL_SHAPE_ID});
        cursor.moveToFirst();
        physicalValues.put("height", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_HEIGHT)));
        physicalValues.put("weight", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_WEIGHT)));
        physicalValues.put("color", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_COLOR_ID)));
        physicalValues.put("shape", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_SHAPE_ID)));
        cursor.close();
        return physicalValues;
    }

    public static double getHeight(ArrayMap<String, Integer> physicalValues) {
        return physicalValues.get("height") / 10.0;
    }

    public static double getWeight(ArrayMap<String, Integer> physicalValues) {
        return physicalValues.get("weight") / 10.0;
    }

    public static int getColorId(ArrayMap<String, Integer> physicalValues) {
        return physicalValues.get("color");
    }

    public static int getShapeId(ArrayMap<String, Integer> physicalValues) {
        return physicalValues.get("shape");
    }


    public ArrayMap<String, Integer> getMoreValues() {
        ArrayMap<String, Integer> moreValues = new ArrayMap<>(3);
        Cursor cursor = makeCursor(new String[] {
                PokemonDBHelper.COL_GENERATION_ID,
                PokemonDBHelper.COL_HABITAT_ID,
                PokemonDBHelper.COL_HATCH_COUNTER});
        cursor.moveToFirst();
        moreValues.put("generation", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_GENERATION_ID)));
        moreValues.put("habitat", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_HABITAT_ID)));
        moreValues.put("hatch_counter", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_HATCH_COUNTER)));
        cursor.close();
        return moreValues;
    }

    public static int getGenerationId(ArrayMap<String, Integer> moreValues) {
        return moreValues.get("generation");
    }

    public static int getHabitatId(ArrayMap<String, Integer> moreValues) {
        return moreValues.get("habitat");
    }

    public static int getBaseEggSteps(ArrayMap<String, Integer> moreValues) {
        return moreValues.get("hatch_counter") * AppConfig.EGG_CYCLE_STEPS;
    }

    public static int getBaseEggCycles(ArrayMap<String, Integer> moreValues) {
        return moreValues.get("hatch_counter");
    }


    public ArrayMap<String, Integer> getMiscValues() {
        ArrayMap<String, Integer> miscValues = new ArrayMap<>(4);
        Cursor cursor = makeCursor(new String[] {
                PokemonDBHelper.COL_IS_DEFAULT,
                PokemonDBHelper.COL_IS_BABY,
                PokemonDBHelper.COL_SPECIES_ORDER,
                PokemonDBHelper.COL_SPECIES_CONQUEST_ORDER});
        cursor.moveToFirst();
        miscValues.put("is_default", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_IS_DEFAULT)));
        miscValues.put("is_baby", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_IS_BABY)));
        miscValues.put("species_order", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_SPECIES_ORDER)));
        miscValues.put("species_conquest_order", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_SPECIES_CONQUEST_ORDER)));
        cursor.close();
        return miscValues;
    }

    public static boolean isDefault(ArrayMap<String, Integer> miscValues) {
        return miscValues.get("is_default") == 1;
    }

    public static boolean isBaby(ArrayMap<String, Integer> miscValues) {
        return miscValues.get("is_baby") == 1;
    }

    public static int getSpeciesOrder(ArrayMap<String, Integer> miscValues) {
        return miscValues.get("species_order");
    }

    public static int getSpeciesConquestOrder(ArrayMap<String, Integer> miscValues) {
        return miscValues.get("species_conquest_order");
    }


    public SparseIntArray getEggGroupIds() {
        SparseIntArray eggGroupValues = new SparseIntArray(2);
        Cursor cursor = makeCursor(new String[] {
                PokemonDBHelper.COL_EGG_GROUP_1_ID, PokemonDBHelper.COL_EGG_GROUP_2_ID});
        cursor.moveToFirst();
        eggGroupValues.put(1, cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_EGG_GROUP_1_ID)));
        eggGroupValues.put(2, cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_EGG_GROUP_2_ID)));
        cursor.close();
        return eggGroupValues;
    }


    public static final String DEX_NATIONAL = "national";
    public static final String DEX_KANTO = "kanto";
    public static final String DEX_ORIGINAL_JOHTO = "original_johto";
    public static final String DEX_HOENN = "hoenn";
    public static final String DEX_ORIGINAL_SINNOH = "original_sinnoh";
    public static final String DEX_EXTENDED_SINNOH = "extended_sinnoh";
    public static final String DEX_UPDATED_JOHTO = "updated_johto";
    public static final String DEX_ORIGINAL_UNOVA = "original_unova";
    public static final String DEX_UPDATED_UNOVA = "updated_unova";
    public static final String DEX_CONQUEST_GALLERY = "conquest_gallery";
    public static final String DEX_KALOS_CENTRAL = "kalos_central";
    public static final String DEX_KALOS_COASTAL = "kalos_coastal";
    public static final String DEX_KALOS_MOUNTAIN = "kalos_mountain";
    public static final String DEX_UPDATED_HOENN = "updated_hoenn";

    public ArrayMap<String, Integer> getPokedexNumbers() {
        ArrayMap<String, Integer> pokedexValues = new ArrayMap<>(15);
        Cursor cursor = makeCursor(new String[] {
                PokemonDBHelper.COL_POKEDEX_NATIONAL,
                PokemonDBHelper.COL_POKEDEX_KANTO,
                PokemonDBHelper.COL_POKEDEX_ORIGINAL_JOHTO,
                PokemonDBHelper.COL_POKEDEX_HOENN,
                PokemonDBHelper.COL_POKEDEX_ORIGINAL_SINNOH,
                PokemonDBHelper.COL_POKEDEX_EXTENDED_SINNOH,
                PokemonDBHelper.COL_POKEDEX_UPDATED_JOHTO,
                PokemonDBHelper.COL_POKEDEX_ORIGINAL_UNOVA,
                PokemonDBHelper.COL_POKEDEX_UPDATED_UNOVA,
                PokemonDBHelper.COL_POKEDEX_CONQUEST_GALLERY,
                PokemonDBHelper.COL_POKEDEX_KALOS_CENTRAL,
                PokemonDBHelper.COL_POKEDEX_KALOS_COASTAL,
                PokemonDBHelper.COL_POKEDEX_KALOS_MOUNTAIN,
                PokemonDBHelper.COL_POKEDEX_UPDATED_HOENN});
        cursor.moveToFirst();
        pokedexValues.put(DEX_NATIONAL, cursor.getInt(
                cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_NATIONAL)));
        pokedexValues.put(DEX_KANTO, cursor.getInt(
                cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_KANTO)));
        pokedexValues.put(DEX_ORIGINAL_JOHTO, cursor.getInt(
                cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_ORIGINAL_JOHTO)));
        pokedexValues.put(DEX_HOENN, cursor.getInt
                (cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_HOENN)));
        pokedexValues.put(DEX_ORIGINAL_SINNOH, cursor.getInt(
                cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_ORIGINAL_SINNOH)));
        pokedexValues.put(DEX_EXTENDED_SINNOH, cursor.getInt(
                cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_EXTENDED_SINNOH)));
        pokedexValues.put(DEX_UPDATED_JOHTO, cursor.getInt(
                cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_UPDATED_JOHTO)));
        pokedexValues.put(DEX_ORIGINAL_UNOVA, cursor.getInt(
                cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_ORIGINAL_UNOVA)));
        pokedexValues.put(DEX_UPDATED_UNOVA, cursor.getInt(
                cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_UPDATED_UNOVA)));
        pokedexValues.put(DEX_CONQUEST_GALLERY, cursor.getInt(
                cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_CONQUEST_GALLERY)));
        pokedexValues.put(DEX_KALOS_CENTRAL, cursor.getInt(
                cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_KALOS_CENTRAL)));
        pokedexValues.put(DEX_KALOS_COASTAL, cursor.getInt(
                cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_KALOS_COASTAL)));
        pokedexValues.put(DEX_KALOS_MOUNTAIN, cursor.getInt(
                cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_KALOS_MOUNTAIN)));
        pokedexValues.put(DEX_UPDATED_HOENN, cursor.getInt(
                cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_UPDATED_HOENN)));
        cursor.close();
        return pokedexValues;
    }

    public static boolean hasPokedexNumber(ArrayMap<String, Integer> map, String pokedexKey) {
        return map.get(pokedexKey) != 0;
    }

    public static final String[] POKEDEX_KEYS = new String[] {
            DEX_NATIONAL,
            DEX_KANTO,
            DEX_ORIGINAL_JOHTO,
            DEX_HOENN,
            DEX_ORIGINAL_SINNOH,
            DEX_EXTENDED_SINNOH,
            DEX_UPDATED_JOHTO,
            DEX_ORIGINAL_UNOVA,
            DEX_UPDATED_UNOVA,
            DEX_CONQUEST_GALLERY,
            DEX_KALOS_CENTRAL,
            DEX_KALOS_COASTAL,
            DEX_KALOS_MOUNTAIN,
            DEX_UPDATED_HOENN
    };


    public ArrayMap<String, String> getNames() {
        ArrayMap<String, String> nameValues = new ArrayMap<>(9);
        Cursor cursor = makeCursor(new String[] {
                PokemonDBHelper.COL_NAME_JAPANESE,
                PokemonDBHelper.COL_NAME_ROMAJI,
                PokemonDBHelper.COL_NAME_KOREAN,
                PokemonDBHelper.COL_NAME_CHINESE,
                PokemonDBHelper.COL_NAME_FRENCH,
                PokemonDBHelper.COL_NAME_GERMAN,
                PokemonDBHelper.COL_NAME_SPANISH,
                PokemonDBHelper.COL_NAME_ITALIAN});
        cursor.moveToFirst();
        nameValues.put("en", mName);
        nameValues.put("ja", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_NAME_JAPANESE)));
        nameValues.put("romaji", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_NAME_ROMAJI)));
        nameValues.put("ko", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_NAME_KOREAN)));
        nameValues.put("zh", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_NAME_CHINESE)));
        nameValues.put("fr", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_NAME_FRENCH)));
        nameValues.put("de", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_NAME_GERMAN)));
        nameValues.put("es", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_NAME_SPANISH)));
        nameValues.put("it", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_NAME_ITALIAN)));
        cursor.close();
        return nameValues;
    }


    public ArrayMap<String, String> getGenera() {
        ArrayMap<String, String> generaValues = new ArrayMap<>(9);
        Cursor cursor = makeCursor(new String[] {
                PokemonDBHelper.COL_GENUS,
                PokemonDBHelper.COL_GENUS_JAPANESE,
                PokemonDBHelper.COL_GENUS_ROMAJI,
                PokemonDBHelper.COL_GENUS_KOREAN,
                PokemonDBHelper.COL_GENUS_CHINESE,
                PokemonDBHelper.COL_GENUS_FRENCH,
                PokemonDBHelper.COL_GENUS_GERMAN,
                PokemonDBHelper.COL_GENUS_SPANISH,
                PokemonDBHelper.COL_GENUS_ITALIAN});
        cursor.moveToFirst();
        generaValues.put("en", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_GENUS)));
        generaValues.put("ja", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_GENUS_JAPANESE)));
        generaValues.put("romaji", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_GENUS_ROMAJI)));
        generaValues.put("ko", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_GENUS_KOREAN)));
        generaValues.put("zh", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_GENUS_CHINESE)));
        generaValues.put("fr", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_GENUS_FRENCH)));
        generaValues.put("de", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_GENUS_GERMAN)));
        generaValues.put("es", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_GENUS_SPANISH)));
        generaValues.put("it", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_GENUS_ITALIAN)));
        cursor.close();
        return generaValues;
    }


    public ArrayMap<String, String> getFormNames() {
        ArrayMap<String, String> formNameValues = new ArrayMap<>(7);
        Cursor cursor = makeCursor(new String[] {
                PokemonDBHelper.COL_FORM_NAME_JAPANESE,
                PokemonDBHelper.COL_FORM_NAME_KOREAN,
                PokemonDBHelper.COL_FORM_NAME_FRENCH,
                PokemonDBHelper.COL_FORM_NAME_GERMAN,
                PokemonDBHelper.COL_FORM_NAME_SPANISH,
                PokemonDBHelper.COL_FORM_NAME_ITALIAN});
        cursor.moveToFirst();
        formNameValues.put("en", mFormName);
        formNameValues.put("ja", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_NAME_JAPANESE)));
        formNameValues.put("ko", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_NAME_KOREAN)));
        formNameValues.put("fr", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_NAME_FRENCH)));
        formNameValues.put("de", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_NAME_GERMAN)));
        formNameValues.put("es", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_NAME_SPANISH)));
        formNameValues.put("it", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_NAME_ITALIAN)));
        cursor.close();
        return formNameValues;
    }


    public ArrayMap<String, Integer> getFormSpecificValues() {
        ArrayMap<String, Integer> formInfoValues = new ArrayMap<>(7);
        Cursor cursor = makeCursor(new String[] {
                PokemonDBHelper.COL_FORM_INTRODUCED_IN_VERSION_GROUP_ID,
                PokemonDBHelper.COL_FORM_ORDER,
                PokemonDBHelper.COL_FORM_ORDER_OF_FORM,
                PokemonDBHelper.COL_FORMS_SWITCHABLE,
                PokemonDBHelper.COL_FORM_IS_DEFAULT,
                PokemonDBHelper.COL_FORM_IS_BATTLE_ONLY,
                PokemonDBHelper.COL_FORM_IS_MEGA});
        cursor.moveToFirst();
        formInfoValues.put("form_introduced_in_version_group_id",
                cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_INTRODUCED_IN_VERSION_GROUP_ID)));
        formInfoValues.put("form_order", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_ORDER)));
        formInfoValues.put("form_order_of_form", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_ORDER_OF_FORM)));
        formInfoValues.put("has_switchable_forms", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_FORMS_SWITCHABLE)));
        formInfoValues.put("is_form_default", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_IS_DEFAULT)));
        formInfoValues.put("is_form_battle_only", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_IS_BATTLE_ONLY)));
        formInfoValues.put("is_form_mega", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_IS_MEGA)));
        cursor.close();
        return formInfoValues;
    }

    public static boolean hasSwitchableForms(ArrayMap<String, Integer> formInfoValues) {
        return formInfoValues.get("has_switchable_forms") == 1;
    }

    public static int getFormIntroducedInVersionGroupId(ArrayMap<String, Integer> formInfoValues) {
        return formInfoValues.get("form_introduced_in_version_group_id");
    }

    public static boolean isFormDefault(ArrayMap<String, Integer> formInfoValues) {
        return formInfoValues.get("is_form_default") == 1;
    }

    public static boolean isFormBattleOnly(ArrayMap<String, Integer> formInfoValues) {
        return formInfoValues.get("is_form_battle_only") == 1;
    }

    public static boolean isFormMega(ArrayMap<String, Integer> formInfoValues) {
        return formInfoValues.get("is_form_mega") == 1;
    }

    public static int getFormOrderOfFormNumber(ArrayMap<String, Integer> formInfoValues) {
        return formInfoValues.get("form_order_of_form");
    }

    public static int getFormOrderNumber(ArrayMap<String, Integer> formInfoValues) {
        return formInfoValues.get("form_order");
    }

    public ArrayMap<String, String> getFormAndPokemonNames() {
        ArrayMap<String, String> combinedNameValues = new ArrayMap<>(2);
        Cursor cursor = makeCursor(new String[] {
                PokemonDBHelper.COL_FORM_NAME_JAPANESE,
                PokemonDBHelper.COL_FORM_NAME_KOREAN,
                PokemonDBHelper.COL_FORM_NAME_FRENCH,
                PokemonDBHelper.COL_FORM_NAME_GERMAN,
                PokemonDBHelper.COL_FORM_NAME_SPANISH,
                PokemonDBHelper.COL_FORM_NAME_ITALIAN});
        cursor.moveToFirst();
        combinedNameValues.put("en", mFormPokemonName);
        combinedNameValues.put("fr", cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_POKEMON_NAME_FRENCH)));
        cursor.close();
        return combinedNameValues;
    }


    public ArrayMap<String, Integer> getEvolutionInfo() {
        ArrayMap<String, Integer> evolutionValues = new ArrayMap<>(2);
        Cursor cursor = makeCursor(new String[] {
                PokemonDBHelper.COL_EVOLVES_FROM_SPECIES_ID,
                PokemonDBHelper.COL_EVOLUTION_CHAIN_ID});
        cursor.moveToFirst();
        evolutionValues.put("evolves_from_species_id", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_EVOLVES_FROM_SPECIES_ID)));
        evolutionValues.put("evolution_chain_id", cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_EVOLUTION_CHAIN_ID)));
        cursor.close();
        return evolutionValues;
    }

    public static int getEvolvesFromSpeciesId(ArrayMap<String, Integer> evolutionValues) {
        return evolutionValues.get("evolves_from_species_id");
    }

    public static int getEvolutionChainId(ArrayMap<String, Integer> evolutionValues) {
        return evolutionValues.get("evolution_chain_id");
    }


    /*
     * Methods I added to make getting some values (e.g. booleans) easier
     */

    public static boolean hasSecondaryType(SparseIntArray typeIds) {
        return typeIds.get(2) != 0;
    }

    public static boolean hasSecondaryType(int secondaryTypeId) {
        return secondaryTypeId != 0;
    }

    public static boolean hasSecondaryAbility(SparseIntArray abilityIds) {
        return abilityIds.get(2) != 0;
    }

    public static boolean hasHiddenAbility(SparseIntArray abilityIds) {
        return abilityIds.get(3) != 0;
    }

    public static boolean isGenderless(ArrayMap<String, Integer> genderValues) {
        return genderValues.get("gender_rate") == -1;
    }

    public static boolean isGenderless(int genderRateId) {
        return genderRateId == -1;
    }

    public static int getStatTotal(ArrayMap<String, Integer> statValues) {
        String[] stats = new String[] {"hp", "atk", "def", "spa", "spd", "spe"};
        int count = 0;
        for (String stat : stats) {
            count += statValues.get(stat);
        }
        return count;
    }

    public static int getStatAvg(int statTotal) {
        return statTotal / 6;
    }

    public static boolean hasHabitatInfo(ArrayMap<String, Integer> moreValues) {
        return Pokemon.getHabitatId(moreValues) != -1;
    }

    public ArrayList<PokemonForm> getAlternateForms() {
        ArrayList<PokemonForm> list = new ArrayList<>();
        PokemonDBHelper helper = new PokemonDBHelper(mContext);
        Cursor cursor = helper.getReadableDatabase().query(
                PokemonDBHelper.TABLE_NAME,
                new String[] {PokemonDBHelper.COL_ID, PokemonDBHelper.COL_SPECIES_ID,
                        PokemonDBHelper.COL_FORM_ID, PokemonDBHelper.COL_FORM_NAME,
                        PokemonDBHelper.COL_FORM_POKEMON_NAME, PokemonDBHelper.COL_POKEDEX_NATIONAL,
                        PokemonDBHelper.COL_TYPE_1_ID, PokemonDBHelper.COL_IS_DEFAULT,
                        PokemonDBHelper.COL_FORM_IS_DEFAULT, PokemonDBHelper.COL_FORM_IS_MEGA},
                PokemonDBHelper.COL_SPECIES_ID + "=?",
                new String[] {String.valueOf(mSpeciesId)},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_ID));
            int formId = cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_ID));
            String formName =
                    cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_NAME));
            String formPokemonName =
                    cursor.getString(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_POKEMON_NAME));
            int pokedexNumber =
                    cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_POKEDEX_NATIONAL));
            int typeId = cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_TYPE_1_ID));
            boolean isDefault =
                    cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_IS_DEFAULT)) == 1;
            boolean isFormDefault =
                    cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_IS_DEFAULT)) == 1;
            boolean isFormMega =
                    cursor.getInt(cursor.getColumnIndex(PokemonDBHelper.COL_FORM_IS_MEGA)) == 1;

            Log.d("New addition", ": " + formPokemonName);

            list.add(new PokemonForm(id, mSpeciesId, formId, mName, formName, formPokemonName,
                    pokedexNumber, typeId, isDefault, isFormDefault, isFormMega));

            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }


    public void setPokemonImage(ImageView imageView) {
        ActionUtils.setPokemonImage(this, imageView);
    }
}
