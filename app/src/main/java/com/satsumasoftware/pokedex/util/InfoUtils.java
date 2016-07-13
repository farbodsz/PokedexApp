package com.satsumasoftware.pokedex.util;

import android.support.annotation.ColorRes;
import android.text.TextUtils;

import com.satsumasoftware.pokedex.R;

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
}
