package com.satsumasoftware.pokedex.util;

import android.text.TextUtils;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.object.MiniPokemon;
import com.satsumasoftware.pokedex.object.Pokemon;

public final class InfoUtils {

    public static String formatPokemonId(int id) {
        String str_num = String.valueOf(id);
        switch (str_num.length()) {
            case 1:
                str_num = "00" + str_num;
                break;
            case 2:
                str_num = "0" + str_num;
                break;
            case 3:
                // Leave it how it is;
                break;
        }
        return str_num;
    }

    public static String unformatPokemonId(String idFormatted) {
        if (!TextUtils.isDigitsOnly(idFormatted)) {
            return idFormatted;
        }

        while (idFormatted.startsWith("0")) {
            idFormatted = idFormatted.replace("0", "");
        }

        return idFormatted;
    }

    public static String formatStringIdentifier(String identifier) {
        return identifier.replace("-", " ");
    }

    public static int getGenFromRoman(String romanNumerals) {
        switch (romanNumerals.toUpperCase()) {
            case "I":
                return 1;
            case "II":
                return 2;
            case "III":
                return 3;
            case "IV":
                return 4;
            case "V":
                return 5;
            case "VI":
                return 6;
            default:
                return 0;
        }
    }

    public static String getRomanFromGen(int generationNumber) {
        switch (generationNumber) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            default:
                return null;
        }
    }

    public static String getGrowthFromAbbreviation(String abbreviation) {
        switch (abbreviation) {
            case "Fl":
                return "Fluctuating";
            case "S":
                return "Slow";
            case "MS":
                return "Medium Slow";
            case "MF":
                return "Medium Fast";
            case "F":
                return "Fast";
            case "E":
                return "Erratic";
            default:
                return null;
        }
    }

    public static String getAbbreviationFromGrowth(String levellingRate) {
        switch (levellingRate) {
            case "Fluctuating":
                return "Fl";
            case "Slow":
                return "S";
            case "Medium Slow":
                return "MS";
            case "Medium Fast":
                return "MF";
            case "Fast":
                return "F";
            case "Erratic":
                return "E";
            default:
                throw new NullPointerException("parameter 'levellingRate' is invalid");
        }
    }

    public static String getStatFromAbbreviation(String abbreviation) {
        switch (abbreviation) {
            case "A":
                return "Attack";
            case "D":
                return "Defense";
            case "SA":
                return "Special Attack";
            case "SD":
                return "Special Defense";
            case "Sp":
                return "Speed";
            case "":
                return "None";
            default:
                return null;
        }
    }

    public static String getAbbreviationFromMoveCategory(String moveCategory) {
        switch (moveCategory.toLowerCase()) {
            case "physical": return "P";
            case "special": return "Sp";
            case "status": return "St";
            default:
                throw new IllegalArgumentException("move category '" + moveCategory +
                    "' is an invalid parameter");
        }
    }

    public static String getRegionFromId(int regionId) {
        switch (regionId) {
            case 0:
                // Not a mappable location (e.g. a person, day care, etc.)
                return "Not a location";
            case 1: return "Kanto";
            case 2: return "Johto";
            case 3: return "Hoenn";
            case 4: return "Sinnoh";
            case 5: return "Unova";
            case 6: return "Kalos";
            default:
                throw new NullPointerException("region id '" + regionId +
                        "' is an invalid parameter");
        }
    }

    public static int getIdFromRegion(String region) {
        switch (region.toLowerCase()) {
            case "not a location":
                // Not a mappable location (e.g. a person, day care, etc.)
                return 0;
            case "kanto": return 1;
            case "johto": return 2;
            case "hoenn": return 3;
            case "sinnoh": return 4;
            case "unova": return 5;
            case "kalos": return 6;
            default:
                throw new NullPointerException("region '" + region +
                        "' is an invalid parameter");
        }
    }

    public static int getTypeBkgdColorRes(String type) {
        if (type.equalsIgnoreCase("normal")) {
            return R.color.type_normal;
        } else if (type.equalsIgnoreCase("fire")) {
            return R.color.type_fire;
        } else if (type.equalsIgnoreCase("fighting")) {
            return R.color.type_fighting;
        } else if (type.equalsIgnoreCase("water")) {
            return R.color.type_water;
        } else if (type.equalsIgnoreCase("flying")) {
            return R.color.type_flying;
        } else if (type.equalsIgnoreCase("grass")) {
            return R.color.type_grass;
        } else if (type.equalsIgnoreCase("poison")) {
            return R.color.type_poison;
        } else if (type.equalsIgnoreCase("electric")) {
            return R.color.type_electric;
        } else if (type.equalsIgnoreCase("ground")) {
            return R.color.type_ground;
        } else if (type.equalsIgnoreCase("psychic")) {
            return R.color.type_psychic;
        } else if (type.equalsIgnoreCase("rock")) {
            return R.color.type_rock;
        } else if (type.equalsIgnoreCase("ice")) {
            return R.color.type_ice;
        } else if (type.equalsIgnoreCase("bug")) {
            return R.color.type_bug;
        } else if (type.equalsIgnoreCase("dragon")) {
            return R.color.type_dragon;
        } else if (type.equalsIgnoreCase("ghost")) {
            return R.color.type_ghost;
        } else if (type.equalsIgnoreCase("dark")) {
            return R.color.type_dark;
        } else if (type.equalsIgnoreCase("steel")) {
            return R.color.type_steel;
        } else if (type.equalsIgnoreCase("fairy")) {
            return R.color.type_fairy;
        } else if (type.equalsIgnoreCase("none")) {
            return R.color.text_black;
        } else {
            return R.color.text_black;
        }
    }

    public static String getNameAndForm(String pokemon, String form) {
        switch (form) {
            case "":
                return pokemon;
            case "Mega":
                return "Mega " + pokemon;
            case "Mega X":
                return "Mega " + pokemon + " X";
            case "Mega Y":
                return "Mega " + pokemon + " Y";
            case "Primal":
                return "Primal " + pokemon;
            default:
                return pokemon + " (" + form + ")";
        }
    }

    public static String getNameAndForm(MiniPokemon pokemon) {
        return getNameAndForm(pokemon.getPokemon(), pokemon.getForm());
    }

    public static String getNameAndForm(Pokemon pokemon) {
        return getNameAndForm(pokemon.getPokemon(), pokemon.getForm());
    }

    public static String formatDBDescription(String string) {
        string = removeDBQuotationMarks(string);
        string = formatAccents(string);
        return string;
    }

    public static String removeDBQuotationMarks(String string) {
        if (string.startsWith("\"")) {
            string = string.substring(1, string.length()-1);
        }
        return string;
    }

    public static String formatAccents(String string) {
        if (string.contains("{E[ACUTE]}")) {
            string = string.replace("{E[ACUTE]}", "\u00E9");
        }
        return string;
    }

    public static String getTechnicalShapeFromSimple(String shape) {
        switch (shape) {
            case "Ball": return "Pomaceous";
            case "Squiggle": return "Caudal";
            case "Fish": return "Ichthyic";
            case "Arms": return "Brachial";
            case "Blob": return "Alvine";
            case "Upright": return "Sciurine";
            case "Legs": return "Crural";
            case "Quadruped": return "Mensal";
            case "Wings": return "Alar";
            case "Tentacles": return "Cilial";
            case "Heads": return "Polycephalic";
            case "Humanoid": return "Anthropomorphic";
            case "Bug wings": return "Lepidopterous";
            case "Armor": return "Chitinous";
            default: return null;
        }
    }

    public static String getEncounterMethodFromId(int encounterMethodId) {
        switch (encounterMethodId) {
            case 1: return "Walking in tall grass or cave";
            case 2: return "Fishing with an Old Rod";
            case 3: return "Fishing with a Good Rod";
            case 4: return "Fishing with a Super Rod";
            case 5: return "Surfing";
            case 6: return "Smashing rocks";
            case 7: return "Headbutting trees";
            case 8: return "Walking in dark grass";
            case 9: return "Walking in rustling grass";
            case 10: return "Walking in dust clouds";
            case 11: return "Walking in bridge shadows";
            case 12: return "Fishing in dark spots";
            case 13: return "Surfing in dark spots";
            default:
                throw new NullPointerException("encounterMethodId '" + encounterMethodId +
                    "' is an invalid parameter");
        }
    }
}
