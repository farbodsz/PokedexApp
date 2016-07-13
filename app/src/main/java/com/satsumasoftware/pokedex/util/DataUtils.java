package com.satsumasoftware.pokedex.util;

public final class DataUtils {

    // TODO Use a db to handle langs

    public static final int NULL_INT = 0;

    public static String typeIdToString(int typeId) {
        switch (typeId) {
            case 1: return "Normal";
            case 2: return "Fighting";
            case 3: return "Flying";
            case 4: return "Poison";
            case 5: return "Ground";
            case 6: return "Rock";
            case 7: return "Bug";
            case 8: return "Ghost";
            case 9: return "Steel";
            case 10: return "Fire";
            case 11: return "Water";
            case 12: return "Grass";
            case 13: return "Electric";
            case 14: return "Psychic";
            case 15: return "Ice";
            case 16: return "Dragon";
            case 17: return "Dark";
            case 18: return "Fairy";
            case 10001: return "Unknown";
            case 10002: return "Shadow";
            default:
                throw new IllegalArgumentException("invalid entry for " +
                        "parameter typeId: '" + typeId + "'");
        }
    }

    public static int typeToId(String type) {
        switch (type.toLowerCase()) {
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
                throw new IllegalArgumentException("invalid entry for " +
                        "parameter: '" + type + "'");
        }
    }

    public static String colorIdToString(int colorId) {
        switch (colorId) {
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
                throw new IllegalArgumentException("value: '" + colorId + "' for parameter " +
                        "is invalid");
        }
    }

    public static String shapeIdToString(int shapeId, boolean technicalTerm) {
        switch (shapeId) {
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
                throw new IllegalArgumentException("value: '" + shapeId + "' for parameter " +
                        "is invalid");
        }
    }

    public static String habitatIdToString(int habitatId) {
        // Habitats are something only in Pokemon FireRed and LeafGreen
        switch (habitatId) {
            case 1: return "Cave";
            case 2: return "Forest";
            case 3: return "Grassland";
            case 4: return "Mountain";
            case 5: return "Rare";
            case 6: return "Rough Terrain";
            case 7: return "Sea";
            case 8: return "Urban";
            case 9: return "Water's Edge";
            default:
                throw new IllegalArgumentException("value: '" + habitatId + "' for parameter " +
                        "is invalid");
        }
    }

    public static double maleFromGenderRate(int genderRateId) {
        // TODO replace with methods in pokemon
        if (genderRateId == -1) {
            return -1;  // genderless
        } else if (genderRateId >= 0 && genderRateId <= 8){
            return ((100.0 / 8.0) * (8.0 - genderRateId));
        } else {
            throw new IllegalArgumentException("invalid entry for parameter genderRateId: '" +
                    genderRateId + "'");
        }
    }

    public static int maxExpFromGrowthId(int growthRateId) {
        // TODO replace with methods in pokemon
        switch (growthRateId) {
            case 1: return 1250000;
            case 2: return 1000000;
            case 3: return 800000;
            case 4: return 1059860;
            case 5: return 600000;
            case 6: return 1640000;
            default:
                throw new IllegalArgumentException("value: '" + growthRateId + "' for parameter " +
                        "is invalid");
        }
    }

    public static int growthIdFromMaxExp(int maxExp) {
        // TODO replace with methods in pokemon
        switch (maxExp) {
            case 1250000: return 1;
            case 1000000: return 2;
            case 800000: return 3;
            case 1059860: return 4;
            case 600000: return 5;
            case 1640000: return 6;
            default:
                throw new IllegalArgumentException("value: '" + maxExp + "' for parameter " +
                        "is invalid");
        }
    }

    public static String regionIdToString(int regionId) {
        switch(regionId) {
            case 1: return "Kanto";
            case 2: return "Johto";
            case 3: return "Hoenn";
            case 4: return "Sinnoh";
            case 5: return "Unova";
            case 6: return "Kalos";
            default:
                throw new IllegalArgumentException("value: '" + regionId + "' for parameter " +
                        "is invalid");
        }
    }

    @Deprecated
    public static int regionToId(String region) {
        switch(region.toLowerCase()) {
            case "kanto": return 1;
            case "johto": return 2;
            case "hoenn": return 3;
            case "sinnoh": return 4;
            case "unova": return 5;
            case "kalos": return 6;
            default:
                throw new IllegalArgumentException("value: '" + region + "' for parameter " +
                        "is invalid");
        }
    }

    public static String getStatFromId(int id) {
        switch (id) {
            case 1:
                return "HP";
            case 2:
                return "Attack";
            case 3:
                return "Defense";
            case 4:
                return "Special Attack";
            case 5:
                return "Special Defense";
            case 6:
                return "Speed";
            case 7:
                return "Accuracy";
            case 8:
                return "Evasion";
            default:
                throw new IllegalArgumentException("value: '" + id + "' for parameter " +
                        "is invalid");
        }
    }

    public static String getPokedexNameFromId(int id) {
        switch (id) {
            case 1:
                return "National";
            case 2:
                return "Kanto";
            case 3:
                return "Original Johto";
            case 4:
                return "Original Hoenn";
            case 5:
                return "Original Sinnoh";
            case 6:
                return "Extended Sinnoh";
            case 7:
                return "Updated Johto";
            case 8:
                return "Original Unova";
            case 9:
                return "Updated Unova";
            case 11:
                return "Conquest Gallery";
            case 12:
                return "Central Kalos";
            case 13:
                return "Coastal Kalos";
            case 14:
                return "Mountain Kalos";
            case 15:
                return "New Hoenn";
            default:
                throw new IllegalArgumentException("value: '" + id + "' for parameter " +
                        "is invalid");
        }
    }

}
