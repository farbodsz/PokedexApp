package com.phoenixenterprise.pokedex.object;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.ImageView;

import com.phoenixenterprise.pokedex.R;
import com.phoenixenterprise.pokedex.db.PokedexDBHelper;
import com.phoenixenterprise.pokedex.util.AppConfig;
import com.phoenixenterprise.pokedex.util.InfoUtils;
import com.phoenixenterprise.pokedex.util.PrefUtils;

import java.util.ArrayList;

public class Pokemon {

    private Context mContext;
    private double mMassKg, mHeight, mGenderMale, mGenderFemale;
    private String mPokemon, mForm, mSpecies, mType1, mType2, mAbility1, mAbility2, mAbilityH, mLevellingRate;
    private int mNationalId, mStatHP, mStatAtk, mStatDef, mStatSpA, mStatSpD, mStatSpe, mStatTotal,
            mCatchRate, mExpGrowth, mBaseEggSteps, mGeneration, mEvolvesFromId, mEvolutionChainId, mColour, mShape, mHabitat, mHappiness;

    public Pokemon(Context context, String pokemon) {
        mContext = context;
        mPokemon = pokemon;

        loadInformation(0);
    }

    public Pokemon(Context context, int nationalID) {
        mContext = context;
        mNationalId = nationalID;

        loadInformation(1);
    }

    public Pokemon(Context context, String pokemon, String form) {
        mContext = context;
        mPokemon = pokemon;
        mForm = form;

        loadInformation(2);
    }

    public Pokemon(Context context, int nationalID, String pokemon, String form) {
        mContext = context;
        mNationalId = nationalID;
        mPokemon = pokemon;
        mForm = form;

        loadInformation(3);
    }

    private void loadInformation(int checkMethod) {
        SQLiteOpenHelper helper = new PokedexDBHelper(mContext);

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;

        switch (checkMethod) {
            case 0:
                // If 'name' column = mPokemon
                cursor = db.query(
                        PokedexDBHelper.TABLE_NAME,
                        null,
                        PokedexDBHelper.COL_NAME + "=?",
                        new String[] {mPokemon},
                        null,
                        null,
                        null);
                break;
            case 1:
                // If 'id' column = mNationalId
                cursor = db.query(
                        PokedexDBHelper.TABLE_NAME,
                        null,
                        PokedexDBHelper.COL_ID + "=?",
                        new String[] {String.valueOf(mNationalId)},
                        null,
                        null,
                        null);
                break;
            case 2:
                // If 'name' column = mPokemon and 'form' column = mForm
                cursor = db.query(
                        PokedexDBHelper.TABLE_NAME,
                        null,
                        PokedexDBHelper.COL_NAME + "=? AND " + PokedexDBHelper.COL_FORM + "=?",
                        new String[] {mPokemon, mForm},
                        null,
                        null,
                        null);
                break;
            case 3:
                // If 'name' column = mPokemon and 'form' column = mForm
                cursor = db.query(
                        PokedexDBHelper.TABLE_NAME,
                        null,
                        PokedexDBHelper.COL_ID + "=? AND " + PokedexDBHelper.COL_NAME + "=? AND " + PokedexDBHelper.COL_FORM + "=?",
                        new String[] {String.valueOf(mNationalId), mPokemon, mForm},
                        null,
                        null,
                        null);
                break;
        }

        if (cursor == null) {
            throw new NullPointerException("Null cursor");
        }

        cursor.moveToFirst();

        mNationalId = cursor.getInt(cursor.getColumnIndex(PokedexDBHelper.COL_ID));
        mPokemon = cursor.getString(cursor.getColumnIndex(PokedexDBHelper.COL_NAME));
        mForm = cursor.getString(cursor.getColumnIndex(PokedexDBHelper.COL_FORM));
        mSpecies = cursor.getString(cursor.getColumnIndex(PokedexDBHelper.COL_SPECIES));
        mStatHP = cursor.getInt(cursor.getColumnIndex(PokedexDBHelper.COL_STAT_HP));
        mStatAtk = cursor.getInt(cursor.getColumnIndex(PokedexDBHelper.COL_STAT_ATK));
        mStatDef = cursor.getInt(cursor.getColumnIndex(PokedexDBHelper.COL_STAT_DEF));
        mStatSpA = cursor.getInt(cursor.getColumnIndex(PokedexDBHelper.COL_STAT_SPA));
        mStatSpD = cursor.getInt(cursor.getColumnIndex(PokedexDBHelper.COL_STAT_SPD));
        mStatSpe = cursor.getInt(cursor.getColumnIndex(PokedexDBHelper.COL_STAT_SPE));
        mStatTotal = mStatHP + mStatAtk + mStatDef + mStatSpA + mStatSpD + mStatSpe;
        mType1 = cursor.getString(cursor.getColumnIndex(PokedexDBHelper.COL_TYPE_1));
        mType2 = cursor.getString(cursor.getColumnIndex(PokedexDBHelper.COL_TYPE_2));
        mAbility1 = cursor.getString(cursor.getColumnIndex(PokedexDBHelper.COL_ABILITY_1));
        mAbility2 = cursor.getString(cursor.getColumnIndex(PokedexDBHelper.COL_ABILITY_2));
        mAbilityH = cursor.getString(cursor.getColumnIndex(PokedexDBHelper.COL_ABILITY_H));
        mCatchRate = cursor.getInt(cursor.getColumnIndex(PokedexDBHelper.COL_CATCH_RATE));
        mMassKg = cursor.getDouble(cursor.getColumnIndex(PokedexDBHelper.COL_MASS));
        mHeight = cursor.getDouble(cursor.getColumnIndex(PokedexDBHelper.COL_HEIGHT));
        mBaseEggSteps = cursor.getInt(cursor.getColumnIndex(PokedexDBHelper.COL_BASE_EGG_STEPS));
        String genderValue = cursor.getString(cursor.getColumnIndex(PokedexDBHelper.COL_GENDER));
        if (genderValue.equalsIgnoreCase("Genderless")) {
            mGenderMale = 0.0;
            mGenderFemale = 0.0;
        } else {
            mGenderMale = Double.parseDouble(genderValue);
            mGenderFemale = 100 - mGenderMale;
        }
        mExpGrowth = cursor.getInt(cursor.getColumnIndex(PokedexDBHelper.COL_EXP));
        mLevellingRate = cursor.getString(cursor.getColumnIndex(PokedexDBHelper.COL_LEVELLING_RATE));
        mGeneration = cursor.getInt(cursor.getColumnIndex(PokedexDBHelper.COL_GENERATION));
        mColour = cursor.getInt(cursor.getColumnIndex(PokedexDBHelper.COL_COLOUR));
        mShape = cursor.getInt(cursor.getColumnIndex(PokedexDBHelper.COL_SHAPE));
        mHabitat = cursor.getInt(cursor.getColumnIndex(PokedexDBHelper.COL_HABITAT));
        mHappiness = cursor.getInt(cursor.getColumnIndex(PokedexDBHelper.COL_BASE_HAPPINESS));
        mEvolvesFromId = cursor.getInt(cursor.getColumnIndex(PokedexDBHelper.COL_EVOLVES_FROM));
        mEvolutionChainId = cursor.getInt(cursor.getColumnIndex(PokedexDBHelper.COL_EVOLUTION_CHAIN));

        cursor.close();
    }

    public MiniPokemon toMiniPokemon() {
        return new MiniPokemon(mNationalId, mPokemon, mForm);
    }

    public int getNationalId() {
        return mNationalId;
    }

    public String getNationalIdFormatted() {
        return InfoUtils.formatPokemonId(mNationalId);
    }

    public String getPokemon() {
        return mPokemon;
    }

    public String getForm() {
        return mForm;
    }

    public String getFormFormatted() {
        if (mForm.equalsIgnoreCase("Mega")) {
            return "Mega Evolution";
        } else if (mForm.equalsIgnoreCase("Mega X")) {
            return "Mega Evolution X";
        } else if (mForm.equalsIgnoreCase("Mega Y")) {
            return "Mega Evolution Y";
        } else if (mForm.equalsIgnoreCase("Primal")) {
            return "Primal " + mPokemon;
        } else {
            return mForm;
        }
    }

    public boolean isNormalForm() {
        return mForm.equals("");
    }

    public boolean isMegaForm() {
        return mForm.equalsIgnoreCase("Mega") || mForm.equals("Mega X") || mForm.equals("Mega Y");
    }

    public boolean hasAlternateForms() {
        PokedexDBHelper helper = new PokedexDBHelper(mContext);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(
                PokedexDBHelper.TABLE_NAME,
                new String[] {PokedexDBHelper.COL_ID},
                PokedexDBHelper.COL_ID + "=?",
                new String[] {String.valueOf(mNationalId)},
                null,
                null,
                null);
        cursor.moveToFirst();
        int pokemonForms = 0;
        while (!cursor.isAfterLast()) {
            pokemonForms++;
            cursor.moveToNext();
        }
        cursor.close();

        return pokemonForms > 1;
    }

    public ArrayList<MiniPokemon> getAlternateForms(boolean includeCurrent) {
        PokedexDBHelper helper = new PokedexDBHelper(mContext);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(
                PokedexDBHelper.TABLE_NAME,
                new String[] {PokedexDBHelper.COL_ID, PokedexDBHelper.COL_NAME, PokedexDBHelper.COL_FORM},
                PokedexDBHelper.COL_ID + "=?",
                new String[] {String.valueOf(mNationalId)},
                null,
                null,
                null);
        cursor.moveToFirst();
        ArrayList<MiniPokemon> pokemonList = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(PokedexDBHelper.COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(PokedexDBHelper.COL_NAME));
            String form = cursor.getString(cursor.getColumnIndex(PokedexDBHelper.COL_FORM));
            MiniPokemon aMiniPokemon = new MiniPokemon(id, name, form);
            if (includeCurrent) {
                pokemonList.add(aMiniPokemon);
            } else {
                if (!form.equals(mForm)) {
                    pokemonList.add(aMiniPokemon);
                }
            }
            cursor.moveToNext();
        }
        cursor.close();

        return pokemonList;
    }

    public String toString() {
        return mNationalId + "_" + mPokemon + "_" + mForm;
    }

    public static MiniPokemon stringToPokemon(String string) {
        String[] info = string.split("_");
        if (info.length == 2) {
            // I.e. no form
            return new MiniPokemon(Integer.parseInt(info[0]), info[1], "");
        } else {
            return new MiniPokemon(Integer.parseInt(info[0]), info[1], info[2]);
        }
    }

    public String getSpecies() {
        return mSpecies;
    }

    public int getStatHP() {
        return mStatHP;
    }

    public int getStatAtk() {
        return mStatAtk;
    }

    public int getStatDef() {
        return mStatDef;
    }

    public int getStatSpA() {
        return mStatSpA;
    }

    public int getStatSpD() {
        return mStatSpD;
    }

    public int getStatSpe() {
        return mStatSpe;
    }

    public int getStatTotal() {
        return mStatTotal;
    }

    public int getStatAvg() {
        return mStatTotal / 6;
    }

    public String getType1() {
        return mType1;
    }

    public String getType2() {
        return mType2;
    }

    public boolean hasSecondaryType() {
        return mType2.equals("");
    }

    public String getAbility1() {
        return mAbility1;
    }

    public String getAbility2() {
        return mAbility2;
    }

    public String getAbilityH() {
        return mAbilityH;
    }

    public boolean hasSecondaryAbility() {
        return mAbility2.equals("");
    }

    public boolean hasHiddenAbility() {
        return mAbilityH.equals("");
    }

    public int getCatchRate() {
        return mCatchRate;
    }

    public double getMass() {
        return mMassKg;
    }

    public double getHeight() {
        return mHeight;
    }

    public int getBaseEggSteps() {
        return mBaseEggSteps;
    }

    public int getBaseEggCycles() {
        return mBaseEggSteps / AppConfig.EGG_CYCLE_STEPS;
    }

    public double getGenderMale() {
        return mGenderMale;
    }

    public double getGenderFemale() {
        return mGenderFemale;
    }

    public int getExpGrowth() {
        return mExpGrowth;
    }

    public String getLevellingRate() {
        return mLevellingRate;
    }

    public int getGeneration() {
        return mGeneration;
    }

    public Pokemon getPreviousEvolution() {
        if (mEvolvesFromId == 0) {
            return null;
        } else {
            return new Pokemon(mContext, mEvolvesFromId);
        }
    }

    public String getColour() {
        switch (mColour) {
            case 1: return "Black";
            case 2: return "Blue";
            case 3: return "Brown";
            case 4: return "Grey";
            case 5: return "Green";
            case 6: return "Pink";
            case 7: return "Purple";
            case 8: return "Red";
            case 9: return "White";
            case 10: return "Yellow";
            default:
                throw new NullPointerException("colour '" + mColour +
                    "' is invalid with Pokemon: " + mNationalId + ":" + mForm);
        }
    }

    public String getShape(boolean technicalTerm) {
        switch (mShape) {
            case 1:
                if (technicalTerm) {
                    return "Pomaceous";
                } else {
                    return "Ball";
                }
            case 2:
                if (technicalTerm) {
                    return "Caudal";
                } else {
                    return "Squiggle";
                }
            case 3:
                if (technicalTerm) {
                    return "Ichthyic";
                } else {
                    return "Fish";
                }
            case 4:
                if (technicalTerm) {
                    return "Brachial";
                } else {
                    return "Arms";
                }
            case 5:
                if (technicalTerm) {
                    return "Alvine";
                } else {
                    return "Blob";
                }
            case 6:
                if (technicalTerm) {
                    return "Sciurine";
                } else {
                    return "Upright";
                }
            case 7:
                if (technicalTerm) {
                    return "Crural";
                } else {
                    return "Legs";
                }
            case 8:
                if (technicalTerm) {
                    return "Mensal";
                } else {
                    return "Quadruped";
                }
            case 9:
                if (technicalTerm) {
                    return "Alar";
                } else {
                    return "Wings";
                }
            case 10:
                if (technicalTerm) {
                    return "Cilial";
                } else {
                    return "Tentacles";
                }
            case 11:
                if (technicalTerm) {
                    return "Polycephalic";
                } else {
                    return "Heads";
                }
            case 12:
                if (technicalTerm) {
                    return "Anthropomorphic";
                } else {
                    return "Humanoid";
                }
            case 13:
                if (technicalTerm) {
                    return "Lepidopterous";
                } else {
                    return "Bug wings";
                }
            case 14:
                if (technicalTerm) {
                    return "Chitinous";
                } else {
                    return "Armor";
                }
            default:
                throw new NullPointerException("shape '" + mShape +
                        "' is invalid with Pokemon: " + mNationalId + ":" + mForm);
        }
    }

    public String getHabitat() {
        // Habitats are something only in Pokemon FireRed and LeafGreen
        switch (mHabitat) {
            case 0:
                return null;
            case 1:
                return "Cave";
            case 2:
                return "Forest";
            case 3:
                return "Grassland";
            case 4:
                return "Mountain";
            case 5:
                return "Rare";
            case 6:
                return "Rough Terrain";
            case 7:
                return "Sea";
            case 8:
                return "Urban";
            case 9:
                return "Water's Edge";
            default:
                throw new NullPointerException("habitat '" + mHabitat +
                        "'is invalid with Pokemon: " + mNationalId + ":" + mForm);
        }
    }

    public int getHappiness() {
        // Base happiness
        return mHappiness;
    }

    public void setPokemonImage(ImageView imageView) {
        if (!PrefUtils.showPokemonImages(imageView.getContext())) {
            imageView.setVisibility(View.GONE);
        }
        // TODO: Pokemon images (maybe have a circle around it like in PokeInfo app - consider copyrights)
        int resId;
        String pkmnId = getNationalIdFormatted();
        String local_form = getFormFormatted();
        switch (pkmnId) {
            case "382":
                if (local_form.equalsIgnoreCase("Primal Kyogre")) {
                    resId = R.drawable.img_pkmn_382_kyogre_primal;
                } else {
                    resId = R.drawable.img_pkmn_382_kyogre;
                }
                break;
            case "383":
                if (local_form.equalsIgnoreCase("Primal Groudon")) {
                    resId = R.drawable.img_pkmn_383_groudon_primal;
                } else {
                    resId = R.drawable.img_pkmn_383_groudon;
                }
                break;
            case "386":
                if (local_form.equalsIgnoreCase("Normal Forme")) {
                    resId = R.drawable.img_pkmn_386_deoxys;
                } else if (local_form.equalsIgnoreCase("Attack Forme")) {
                    resId = R.drawable.img_pkmn_386_deoxys_attack;
                } else if (local_form.equalsIgnoreCase("Defense Forme")) {
                    resId = R.drawable.img_pkmn_386_deoxys_defense;
                } else {
                    resId = R.drawable.img_pkmn_386_deoxys_speed;
                }
                break;
            case "412":
                if (local_form.equalsIgnoreCase("Plant Cloak")) {
                    resId = R.drawable.img_pkmn_412_burmy_plant;
                } else if (local_form.equalsIgnoreCase("Sandy Cloak")) {
                    resId = R.drawable.img_pkmn_412_burmy_sandy;
                } else {
                    resId = R.drawable.img_pkmn_412_burmy_trash;
                }
            case "413":
                if (local_form.equalsIgnoreCase("Plant Cloak")) {
                    resId = R.drawable.img_pkmn_413_wormadam_plant;
                } else if (local_form.equalsIgnoreCase("Sandy Cloak")) {
                    resId = R.drawable.img_pkmn_413_wormadam_sandy;
                } else {
                    resId = R.drawable.img_pkmn_413_wormadam_trash;
                }
                break;
            case "479":
                if (local_form.equalsIgnoreCase("Normal")) {
                    resId = R.drawable.img_pkmn_479_rotom;
                } else if (local_form.equalsIgnoreCase("Heat")) {
                    resId = R.drawable.img_pkmn_479_rotom_heat;
                } else if (local_form.equalsIgnoreCase("Wash")) {
                    resId = R.drawable.img_pkmn_479_rotom_wash;
                } else if (local_form.equalsIgnoreCase("Frost")) {
                    resId = R.drawable.img_pkmn_479_rotom_frost;
                } else if (local_form.equalsIgnoreCase("Fan")) {
                    resId = R.drawable.img_pkmn_479_rotom_fan;
                } else {
                    resId = R.drawable.img_pkmn_479_rotom_mow;
                }
                break;
            case "487":
                if (local_form.equalsIgnoreCase("Altered Forme")) {
                    resId = R.drawable.img_pkmn_487_giratina_altered;
                } else {
                    resId = R.drawable.img_pkmn_487_giratina_origin;
                }
                break;
            case "492":
                if (local_form.equalsIgnoreCase("Land Forme")) {
                    resId = R.drawable.img_pkmn_492_shaymin_land;
                } else {
                    resId = R.drawable.img_pkmn_492_shaymin_sky;
                }
                break;
            case "555":
                if (local_form.equalsIgnoreCase("Standard Mode")) {
                    resId = R.drawable.img_pkmn_555_darmanitan;
                } else {
                    resId = R.drawable.img_pkmn_555_darmanitan_zen;
                }
                break;
            case "641":
                if (local_form.equalsIgnoreCase("Incarnate Forme")) {
                    resId = R.drawable.img_pkmn_641_tornadus;
                } else {
                    resId = R.drawable.img_pkmn_641_tornadus_therian;
                }
                break;
            case "642":
                if (local_form.equalsIgnoreCase("Incarnate Forme")) {
                    resId = R.drawable.img_pkmn_642_thundurus;
                } else {
                    resId = R.drawable.img_pkmn_642_thundurus_therian;
                }
                break;
            case "645":
                if (local_form.equalsIgnoreCase("Incarnate Forme")) {
                    resId = R.drawable.img_pkmn_645_landorus;
                } else {
                    resId = R.drawable.img_pkmn_645_landorus_therian;
                }
                break;
            case "646":
                if (local_form.equalsIgnoreCase("Normal Kyurem")) {
                    resId = R.drawable.img_pkmn_646_kyurem;
                } else if (local_form.equalsIgnoreCase("White Kyurem")) {
                    resId = R.drawable.img_pkmn_646_kyurem_white;
                } else {
                    resId = R.drawable.img_pkmn_646_kyurem_black;
                }
                break;
            case "647":
                if (local_form.equalsIgnoreCase("Ordinary Form")) {
                    resId = R.drawable.img_pkmn_647_keldeo;
                } else {
                    resId = R.drawable.img_pkmn_647_keldeo_resolute;
                }
                break;
            case "648":
                if (local_form.equalsIgnoreCase("Aria Forme")) {
                    resId = R.drawable.img_pkmn_648_meloetta;
                } else {
                    resId = R.drawable.img_pkmn_648_meloetta_pirouette;
                }
                break;
            case "681":
                if (local_form.equalsIgnoreCase("Shield Forme")) {
                    resId = R.drawable.img_pkmn_681_aegislash;
                } else {
                    resId = R.drawable.img_pkmn_681_aegislash; // The one image is of both forms
                }
                break;
            case "710":
                if (local_form.equalsIgnoreCase("Average Size")) {
                    resId = R.drawable.img_pkmn_710_pumpkaboo;
                } else {
                    // Small Size, Large Size, Super Size
                    resId = R.drawable.img_pkmn_710_pumpkaboo; // The one image is of all forms
                }
            case "711":
                if (local_form.equalsIgnoreCase("Average Size")) {
                    resId = R.drawable.img_pkmn_711_gourgeist;
                } else {
                    // Small Size, Large Size, Super Size
                    resId = R.drawable.img_pkmn_711_gourgeist; // The one image is of all forms
                }
            case "720":
                if (local_form.equalsIgnoreCase("Confined Hoopa")) {
                    resId = R.drawable.img_pkmn_720_hoopa;
                } else {
                    resId = R.drawable.img_unknown; // No image for Unbound Hoopa yet
                }
            default:
                String pokemonImage;
                if (local_form.equalsIgnoreCase("Mega Evolution")) {
                    pokemonImage = "img_pkmn_" + pkmnId + "_" + getPokemon().toLowerCase() + "_mega";
                } else if (local_form.equalsIgnoreCase("Mega Evolution X")) {
                    pokemonImage = "img_pkmn_" + pkmnId + "_" + getPokemon().toLowerCase() + "_mega_x";
                } else if (local_form.equalsIgnoreCase("Mega Evolution Y")) {
                    pokemonImage = "img_pkmn_" + pkmnId + "_" + getPokemon().toLowerCase() + "_mega_y";
                } else {
                    pokemonImage = "img_pkmn_" + pkmnId + "_" + getPokemon().toLowerCase();
                }
                Context mContext = imageView.getContext();
                resId = mContext.getResources().getIdentifier(pokemonImage, "drawable", mContext.getPackageName());
                break;
        }
        imageView.setImageResource(resId);
    }

}