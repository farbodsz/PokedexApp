package com.satsumasoftware.pokedex.util;

import android.support.annotation.ColorRes;
import android.text.TextUtils;

import com.satsumasoftware.pokedex.R;
import com.satsumasoftware.pokedex.entities.pokemon.MiniPokemon;

@Deprecated
/** Move to {@link DataUtils} */
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

    @Deprecated
    public static String getGrowthFromAbbreviation(String abbreviation) {
        switch (abbreviation) {
            case "Fluctuating":
            case "Slow":
            case "Medium Slow":
            case "Medium Fast":
            case "Fast":
            case "Erratic":
                return abbreviation;
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
                throw new IllegalArgumentException("value: '" + abbreviation + "' for parameter levellingRate is invalid");
        }
    }


    public static int growthToId(String growthRate) {
        switch (growthRate.toLowerCase()) {
            case "fluctuating": return 6;
            case "slow": return 1;
            case "medium slow": return 4;
            case "medium fast": return 2;
            case "fast": return 3;
            case "erratic": return 5;
            default:
                throw new IllegalArgumentException("value: '" + growthRate + "' for parameter growthRate is invalid");
        }
    }

    public static String idToGrowth(int id) {
        switch (id) {
            case 6: return "Fluctuating";
            case 1: return "Slow";
            case 4: return "Medium Slow";
            case 2: return "Medium Fast";
            case 3: return "Fast";
            case 5: return "Erratic";
            default:
                throw new IllegalArgumentException("value: '" + id + "' for parameter id is invalid");
        }
    }

    public static int typeToId(String type) {
        switch (type.toLowerCase()) {  // TODO other languages?
            case "normal": return 1;
            case "fighting": return 2;
            case "flying": return 3;
            case "poison": return 4;
            case "ground": return 5;
            case "rock": return 6;
            case "bug": return 7;
            case "ghost": return 8;
            case "steel": return 9;
            case "fire": return 10;
            case "water": return 11;
            case "grass": return 12;
            case "electric": return 13;
            case "psychic": return 14;
            case "ice": return 15;
            case "dragon": return 16;
            case "dark": return 17;
            case "fairy": return 18;
            case "unknown": return 10001;
            case "shadow": return 10002;
            default:
                throw new IllegalArgumentException("value: '" + type + "' for parameter type is invalid");
        }
    }

    @Deprecated
    public static String getAbbreviationFromGrowth(String levellingRate) {
        switch (levellingRate) {
            case "Fl":
            case "S":
            case "MS":
            case "MF":
            case "F":
            case "E":
                return levellingRate;
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
                throw new IllegalArgumentException("value: '" + levellingRate + "' for parameter levellingRate is invalid");
        }
    }

    public static String getColourFromId(int id) {
        switch (id) {
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
                throw new IllegalArgumentException("value: '" + id + "' for parameter id is invalid");
        }
    }

    public static int getIdFromColour(String colour) {
        switch (colour) {
            case "Black": return 1;
            case "Blue": return 2;
            case "Brown": return 3;
            case "Grey": return 4;
            case "Green": return 5;
            case "Pink": return 6;
            case "Purple": return 7;
            case "Red": return 8;
            case "White": return 9;
            case "Yellow": return 10;
            default:
                throw new IllegalArgumentException("value: '" + colour + "' for parameter colour is invalid");
        }
    }

    public static String getShapeFromId(int id, boolean findTechnicalTerm) {
        switch (id) {
            case 1:
                if (findTechnicalTerm) {
                    return "Pomaceous";
                } else {
                    return "Ball";
                }
            case 2:
                if (findTechnicalTerm) {
                    return "Caudal";
                } else {
                    return "Squiggle";
                }
            case 3:
                if (findTechnicalTerm) {
                    return "Ichthyic";
                } else {
                    return "Fish";
                }
            case 4:
                if (findTechnicalTerm) {
                    return "Brachial";
                } else {
                    return "Arms";
                }
            case 5:
                if (findTechnicalTerm) {
                    return "Alvine";
                } else {
                    return "Blob";
                }
            case 6:
                if (findTechnicalTerm) {
                    return "Sciurine";
                } else {
                    return "Upright";
                }
            case 7:
                if (findTechnicalTerm) {
                    return "Crural";
                } else {
                    return "Legs";
                }
            case 8:
                if (findTechnicalTerm) {
                    return "Mensal";
                } else {
                    return "Quadruped";
                }
            case 9:
                if (findTechnicalTerm) {
                    return "Alar";
                } else {
                    return "Wings";
                }
            case 10:
                if (findTechnicalTerm) {
                    return "Cilial";
                } else {
                    return "Tentacles";
                }
            case 11:
                if (findTechnicalTerm) {
                    return "Polycephalic";
                } else {
                    return "Heads";
                }
            case 12:
                if (findTechnicalTerm) {
                    return "Anthropomorphic";
                } else {
                    return "Humanoid";
                }
            case 13:
                if (findTechnicalTerm) {
                    return "Lepidopterous";
                } else {
                    return "Bug wings";
                }
            case 14:
                if (findTechnicalTerm) {
                    return "Chitinous";
                } else {
                    return "Armor";
                }
            default:
                throw new IllegalArgumentException("value: '" + id + "' for parameter id is invalid");
        }
    }

    public static int getIdFromShape(String shape) {
        switch (shape) {
            case "Pomaceous":
            case "Ball":
                return 1;
            case "Caudal":
            case "Squiggle":
                return 2;
            case "Ichthyic":
            case "Fish":
                return 3;
            case "Brachial":
            case "Arms":
                return 4;
            case "Alvine":
            case "Blob":
                return 5;
            case "Sciurine":
            case "Upright":
                return 6;
            case "Crural":
            case "Legs":
                return 7;
            case "Mensal":
            case "Quadruped":
                return 8;
            case "Alar":
            case "Wings":
                return 9;
            case "Cilial":
            case "Tentacles":
                return 10;
            case "Polycephalic":
            case "Heads":
                return 11;
            case "Anthropomorphic":
            case "Humanoid":
                return 12;
            case "Lepidopterous":
            case "Bug wings":
                return 13;
            case "Chitinous":
            case "Armor":
                return 14;
            default:
                throw new IllegalArgumentException("value: '" + shape + "' for parameter shape is invalid");
        }
    }

    public static String getHabitatFromId(int id) {
        // Habitats are something only in Pokemon FireRed and LeafGreen
        switch (id) {
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
                throw new IllegalArgumentException("value: '" + id + "' for parameter id is invalid");
        }
    }

    public static int getIdFromHabitat(String habitat) {
        // Habitats are something only in Pokemon FireRed and LeafGreen
        switch (habitat) {
            case "Cave": return 1;
            case "Forest": return 2;
            case "Grassland": return 3;
            case "Mountain": return 4;
            case "Rare": return 5;
            case "Rough Terrain": return 6;
            case "Sea": return 7;
            case "Urban": return 8;
            case "Water's Edge": return 9;
            default:
                throw new IllegalArgumentException("value: '" + habitat + "' for parameter habitat is invalid");
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

    @ColorRes
    public static int getTypeBkgdColorRes(int typeId) {
        // TODO: Generate programmatically
        switch (typeId) {
            case 1: return R.color.type_normal;
            case 2: return R.color.type_fighting;
            case 3: return R.color.type_flying;
            case 4: return R.color.type_poison;
            case 5: return R.color.type_ground;
            case 6: return R.color.type_rock;
            case 7: return R.color.type_bug;
            case 8: return R.color.type_ghost;
            case 9: return R.color.type_steel;
            case 10: return R.color.type_fire;
            case 11: return R.color.type_water;
            case 12: return R.color.type_grass;
            case 13: return R.color.type_electric;
            case 14: return R.color.type_psychic;
            case 15: return R.color.type_ice;
            case 16: return R.color.type_dragon;
            case 17: return R.color.type_dark;
            case 18: return R.color.type_fairy;
            default: return R.color.mdu_text_black;
        }
    }

    @Deprecated
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
            return R.color.mdu_text_black;
        } else {
            return R.color.mdu_text_black;
        }
    }

    @Deprecated
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

    @Deprecated
    public static String getNameAndForm(MiniPokemon pokemon) {
        return getNameAndForm(pokemon.getName(), pokemon.getFormName());
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
