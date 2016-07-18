package com.satsumasoftware.pokedex.db;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

@SuppressWarnings("unused")
public class PokeDB extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "pokedex.db";
    private static final int DATABASE_VERSION = 10;


    public PokeDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public class Abilities {
        public static final String TABLE_NAME = "abilities";
        public static final String COL_ID = "id";
        public static final String COL_IDENTIFIER = "identifier";
        public static final String COL_GENERATION_ID = "generation_id";
        public static final String COL_IS_MAIN_SERIES = "is_main_series";
    }

    public class AbilityFlavorText {
        public static final String TABLE_NAME = "ability_flavor_text";
        public static final String COL_ABILITY_ID = "ability_id";
        public static final String COL_VERSION_GROUP_ID = "version_group_id";
        public static final String COL_LANGUAGE_ID = "language_id";
        public static final String COL_FLAVOR_TEXT = "flavor_text";
    }

    public class AbilityNames {
        public static final String TABLE_NAME = "ability_names";
        public static final String COL_ABILITY_ID = "ability_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_NAME = "name";
    }

    public class AbilityProse {
        public static final String TABLE_NAME = "ability_prose";
        public static final String COL_ABILITY_ID = "ability_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_SHORT_EFFECT = "short_effect";
        public static final String COL_EFFECT = "effect";
    }


    public class EncounterConditionProse {
        public static final String TABLE_NAME = "encounter_condition_prose";
        public static final String COL_ENCOUNTER_CONDITION_ID = "encounter_condition_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_NAME = "name";
    }

    public class EncounterConditionValueMap {
        public static final String TABLE_NAME = "encounter_condition_value_map";
        public static final String COL_ENCOUNTER_ID = "encounter_id";
        public static final String COL_ENCOUNTER_CONDITION_VALUE_ID = "encounter_condition_value_id";
    }

    public class EncounterConditionValueProse {
        public static final String TABLE_NAME = "encounter_condition_value_prose";
        public static final String COL_ENCOUNTER_CONDITION_VALUE_ID = "encounter_condition_value_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_NAME = "name";
    }

    public class EncounterConditionValues {
        public static final String TABLE_NAME = "encounter_condition_values";
        public static final String COL_ID = "id";
        public static final String COL_ENCOUNTER_CONDITION_ID = "encounter_condition_id";
        public static final String COL_IDENTIFIER = "identifier";
        public static final String COL_IS_DEFAULT = "is_default";
    }

    public class EncounterMethodProse {
        public static final String TABLE_NAME = "encounter_method_prose";
        public static final String COL_ENCOUNTER_METHOD_ID = "encounter_method_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_NAME = "name";
    }

    public class EncounterSlots {
        public static final String TABLE_NAME = "encounter_slots";
        public static final String COL_ID = "id";
        public static final String COL_VERSION_GROUP_ID = "version_group_id";
        public static final String COL_ENCOUNTER_METHOD_ID = "encounter_method_id";
        public static final String COL_SLOT = "slot";
        public static final String COL_RARITY = "rarity";
    }

    public class Encounters {
        public static final String TABLE_NAME = "encounters";
        public static final String COL_ID = "id";
        public static final String COL_VERSION_ID = "version_id";
        public static final String COL_LOCATION_AREA_ID = "location_area_id";
        public static final String COL_ENCOUNTER_SLOT_ID = "encounter_slot_id";
        public static final String COL_POKEMON_ID = "pokemon_id";
        public static final String COL_MIN_LEVEL = "min_level";
        public static final String COL_MAX_LEVEL = "max_level";
    }


    public class EvolutionChains {
        public static final String TABLE_NAME = "evolution_chains";
        public static final String COL_ID = "id";
        public static final String COL_BABY_TRIGGER_ITEM_ID = "baby_trigger_item_id";
    }


    public class Experience {
        public static final String TABLE_NAME = "experience";
        public static final String COL_GROWTH_RATE_ID = "growth_rate_id";
        public static final String COL_LEVEL = "level";
        public static final String COL_EXPERIENCE = "experience";
    }


    public class ItemCategories {
        public static final String TABLE_NAME = "item_categories";
        public static final String COL_ID = "id";
        public static final String COL_POCKET_ID = "pocket_id";
        public static final String COL_IDENTIFIER = "identifier";
    }

    public class ItemFlagMap {
        public static final String TABLE_NAME = "item_flag_map";
        public static final String COL_ITEM_ID = "item_id";
        public static final String COL_ITEM_FLAG_ID = "item_flag_id";
    }

    public class ItemFlavorText {
        public static final String TABLE_NAME = "item_flavor_text";
        public static final String COL_ITEM_ID = "item_id";
        public static final String COL_VERSION_GROUP_ID = "version_group_id";
        public static final String COL_LANGUAGE_ID = "language_id";
        public static final String COL_FLAVOR_TEXT = "flavor_text";
    }

    public class ItemNames {
        public static final String TABLE_NAME = "item_names";
        public static final String COL_ITEM_ID = "item_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_NAME = "name";
    }

    public class ItemProse {
        public static final String TABLE_NAME = "item_prose";
        public static final String COL_ITEM_ID = "item_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_SHORT_EFFECT = "short_effect";
        public static final String COL_EFFECT = "effect";
    }

    public class Items {
        public static final String TABLE_NAME = "items";
        public static final String COL_ID = "id";
        public static final String COL_IDENTIFIER = "identifier";
        public static final String COL_CATEGORY_ID = "category_id";
        public static final String COL_COST = "cost";
        public static final String COL_FLING_POWER = "fling_power";
        public static final String COL_FLING_EFFECT_ID = "fling_effect_id";
    }


    public class LocationAreaEncounterRates {
        public static final String TABLE_NAME = "location_area_encounter_rates";
        public static final String COL_LOCATION_AREA_ID = "location_area_id";
        public static final String COL_ENCOUNTER_METHOD_ID = "encounter_method_id";
        public static final String COL_VERSION_ID = "version_id";
        public static final String COL_RATE = "rate";
    }

    public class LocationAreaProse {
        public static final String TABLE_NAME = "location_area_prose";
        public static final String COL_LOCATION_AREA_ID = "location_area_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_NAME = "name";
    }

    public class LocationAreas {
        public static final String TABLE_NAME = "location_areas";
        public static final String COL_ID = "id";
        public static final String COL_LOCATION_ID = "location_id";
        public static final String COL_GAME_INDEX = "game_index";
        public static final String COL_IDENTIFIER = "identifier";
    }

    public class LocationNames {
        public static final String TABLE_NAME = "location_names";
        public static final String COL_LOCATION_ID = "location_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_NAME = "name";
    }

    public class Locations {
        public static final String TABLE_NAME = "locations";
        public static final String COL_ID = "id";
        public static final String COL_REGION_ID = "region_id";
        public static final String COL_IDENTIFIER = "identifier";
    }


    public class Machines {
        public static final String TABLE_NAME = "machines";
        public static final String COL_MACHINE_NUMBER = "machine_number";
        public static final String COL_VERSION_GROUP_ID = "version_group_id";
        public static final String COL_ITEM_ID = "item_id";
        public static final String COL_MOVE_ID = "move_id";
    }


    public class MoveEffectProse {
        public static final String TABLE_NAME = "move_effect_prose";
        public static final String COL_MOVE_EFFECT_ID = "move_effect_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_SHORT_EFFECT = "short_effect";
        public static final String COL_EFFECT = "effect";
    }

    public class MoveFlagMap {
        public static final String TABLE_NAME = "move_flag_map";
        public static final String COL_MOVE_ID = "move_id";
        public static final String COL_MOVE_FLAG_ID = "move_flag_id";
    }

    public class MoveFlagProse {
        public static final String TABLE_NAME = "move_flag_prose";
        public static final String COL_MOVE_FLAG_ID = "move_flag_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_NAME = "name";
        public static final String COL_DESCRIPTION = "description";
    }

    public class MoveFlavorText {
        public static final String TABLE_NAME = "move_flavor_text";
        public static final String COL_MOVE_ID = "move_id";
        public static final String COL_VERSION_GROUP_ID = "version_group_id";
        public static final String COL_LANGUAGE_ID = "language_id";
        public static final String COL_FLAVOR_TEXT = "flavor_text";
    }

    public class MoveMeta {
        public static final String TABLE_NAME = "move_meta";
        public static final String COL_MOVE_ID = "move_id";
        public static final String COL_META_CATEGORY_ID = "meta_category_id";
        public static final String COL_META_AILMENT_ID = "meta_ailment_id";
        public static final String COL_MIN_HITS = "min_hits";
        public static final String COL_MAX_HITS = "max_hits";
        public static final String COL_MIN_TURNS = "min_turns";
        public static final String COL_MAX_TURNS = "max_turns";
        public static final String COL_DRAIN = "drain";
        public static final String COL_HEALING = "healing";
        public static final String COL_CRIT_RATE = "crit_rate";
        public static final String COL_AILMENT_CHANCE = "ailment_chance";
        public static final String COL_FLINCH_CHANCE = "flinch_chance";
        public static final String COL_STAT_CHANCE = "stat_chance";
    }

    public class MoveMetaStatChanges {
        public static final String TABLE_NAME = "move_meta_stat_changes";
        public static final String COL_MOVE_ID = "move_id";
        public static final String COL_STAT_ID = "stat_id";
        public static final String COL_CHANGE = "change";
    }

    public class MoveNames {
        public static final String TABLE_NAME = "move_names";
        public static final String COL_MOVE_ID = "move_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_NAME = "name";
    }

    public class MoveTargetProse {
        public static final String TABLE_NAME = "move_target_prose";
        public static final String COL_MOVE_TARGET_ID = "move_target_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_NAME = "name";
        public static final String COL_DESCRIPTION = "description";
    }

    public class Moves {
        public static final String TABLE_NAME = "moves";
        public static final String COL_ID = "id";
        public static final String COL_IDENTIFIER = "identifier";
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
    }


    public class NatureBattleStylePreferences {
        public static final String TABLE_NAME = "nature_battle_style_preferences";
        public static final String COL_NATURE_ID = "nature_id";
        public static final String COL_MOVE_BATTLE_STYLE_ID = "move_battle_style_id";
        public static final String COL_LOW_HP_PREFERENCE = "low_hp_preference";
        public static final String COL_HIGH_HP_PREFERENCE = "high_hp_preference";
    }

    public class NatureNames {
        public static final String TABLE_NAME = "nature_names";
        public static final String COL_NATURE_ID = "nature_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_NAME = "name";
    }

    public class NaturePokeathlonStats {
        public static final String TABLE_NAME = "nature_pokeathlon_stats";
        public static final String COL_NATURE_ID = "nature_id";
        public static final String COL_POKEATHLON_STAT_ID = "pokeathlon_stat_id";
        public static final String COL_MAX_CHANGE = "max_change";
    }

    public class Natures {
        public static final String TABLE_NAME = "natures";
        public static final String COL_ID = "id";
        public static final String COL_IDENTIFIER = "identifier";
        public static final String COL_DECREASED_STAT_ID = "decreased_stat_id";
        public static final String COL_INCREASED_STAT_ID = "increased_stat_id";
        public static final String COL_HATES_FLAVOR_ID = "hates_flavor_id";
        public static final String COL_LIKES_FLAVOR_ID = "likes_flavor_id";
        public static final String COL_GAME_INDEX = "game_index";
    }


    public class PokedexProse {
        public static final String TABLE_NAME = "pokedex_prose";
        public static final String COL_POKEDEX_ID = "pokedex_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_NAME = "name";
        public static final String COL_DESCRIPTION = "description";
    }

    public class PokedexVersionGroups {
        public static final String TABLE_NAME = "pokedex_version_groups";
        public static final String COL_POKEDEX_ID = "pokedex_id";
        public static final String COL_VERSION_GROUP_ID = "version_group_id";
    }

    public class Pokedexes {
        public static final String TABLE_NAME = "pokedexes";
        public static final String COL_ID = "id";
        public static final String COL_REGION_ID = "region_id";
        public static final String COL_IDENTIFIER = "identifier";
        public static final String COL_IS_MAIN_SERIES = "is_main_series";
    }


    public class Pokemon {
        public static final String TABLE_NAME = "pokemon";
        public static final String COL_ID = "id";
        public static final String COL_IDENTIFIER = "identifier";
        public static final String COL_SPECIES_ID = "species_id";
        public static final String COL_HEIGHT = "height";
        public static final String COL_WEIGHT = "weight";
        public static final String COL_BASE_EXP = "base_experience";
        public static final String COL_ORDER = "pokemon_order";
        public static final String COL_IS_DEFAULT = "is_default";
    }

    public class PokemonAbilities {
        public static final String TABLE_NAME = "pokemon_abilities";
        public static final String COL_POKEMON_ID = "pokemon_id";
        public static final String COL_ABILITY_ID = "ability_id";
        public static final String COL_IS_HIDDEN = "is_hidden";
        public static final String COL_SLOT = "slot";
    }

    public class PokemonDexNumbers {
        public static final String TABLE_NAME = "pokemon_dex_numbers";
        public static final String COL_SPECIES_ID = "species_id";
        public static final String COL_POKEDEX_ID = "pokedex_id";
        public static final String COL_POKEDEX_NUMBER = "pokedex_number";
    }

    public class PokemonEggGroups {
        public static final String TABLE_NAME = "pokemon_egg_groups";
        public static final String COL_SPECIES_ID = "species_id";
        public static final String COL_EGG_GROUP_ID = "egg_group_id";
    }

    public class PokemonEvolution {
        public static final String TABLE_NAME = "pokemon_evolution";
        public static final String COL_ID = "id";
        public static final String COL_EVOLVED_SPECIES_ID = "evolved_species_id";
        public static final String COL_EVOLUTION_TRIGGER_ID = "evolution_trigger_id";
        public static final String COL_TRIGGER_ITEM_ID = "trigger_item_id";
        public static final String COL_MINIMUM_LEVEL = "minimum_level";
        public static final String COL_GENDER_ID = "gender_id";
        public static final String COL_LOCATION_ID = "location_id";
        public static final String COL_HELD_ITEM_ID = "held_item_id";
        public static final String COL_TIME_OF_DAY = "time_of_day";
        public static final String COL_KNOWN_MOVE_ID = "known_move_id";
        public static final String COL_KNOWN_MOVE_TYPE_ID = "known_move_type_id";
        public static final String COL_MINIMUM_HAPPINESS = "minimum_happiness";
        public static final String COL_MINIMUM_BEAUTY = "minimum_beauty";
        public static final String COL_MINIMUM_AFFECTION = "minimum_affection";
        public static final String COL_RELATIVE_PHYSICAL_STATS = "relative_physical_stats";
        public static final String COL_PARTY_SPECIES_ID = "party_species_id";
        public static final String COL_PARTY_TYPE_ID = "party_type_id";
        public static final String COL_TRADE_SPECIES_ID = "trade_species_id";
        public static final String COL_NEEDS_OVERWORLD_RAIN = "needs_overworld_rain";
        public static final String COL_TURN_UPSIDE_DOWN = "turn_upside_down";
    }

    public class PokemonFormNames {
        public static final String TABLE_NAME = "pokemon_form_names";
        public static final String COL_POKEMON_FORM_ID = "pokemon_form_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_FORM_NAME = "form_name";
        public static final String COL_POKEMON_NAME = "pokemon_name";
    }

    public class PokemonFormPokeathlonStats {
        public static final String TABLE_NAME = "pokemon_form_pokeathlon_stats";
        public static final String COL_POKEMON_FORM_ID = "pokemon_form_id";
        public static final String COL_POKEATHLON_STAT_ID = "pokeathlon_stat_id";
        public static final String COL_MINIMUM_STAT = "minimum_stat";
        public static final String COL_BASE_STAT = "base_stat";
        public static final String COL_MAXIMUM_STAT = "maximum_stat";
    }

    public class PokemonForms {
        public static final String TABLE_NAME = "pokemon_forms";
        public static final String COL_ID = "id";
        public static final String COL_IDENTIFIER = "identifier";
        public static final String COL_FORM_IDENTIFIER = "form_identifier";
        public static final String COL_POKEMON_ID = "pokemon_id";
        public static final String COL_INTRODUCED_IN_VERSION_GROUP_ID = "introduced_in_version_group_id";
        public static final String COL_IS_DEFAULT = "is_default";
        public static final String COL_IS_BATTLE_ONLY = "is_battle_only";
        public static final String COL_IS_MEGA = "is_mega";
        public static final String COL_FORM_ORDER = "form_order";
        public static final String COL_ORDER = "list_order";
    }

    public class PokemonItems {
        public static final String TABLE_NAME = "pokemon_items";
        public static final String COL_POKEMON_ID = "pokemon_id";
        public static final String COL_VERSION_ID = "version_id";
        public static final String COL_ITEM_ID = "item_id";
        public static final String COL_RARITY = "rarity";
    }

    public class PokemonMoves {
        public static final String TABLE_NAME = "pokemon_moves";
        public static final String COL_POKEMON_ID = "pokemon_id";
        public static final String COL_VERSION_GROUP_ID = "version_group_id";
        public static final String COL_MOVE_ID = "move_id";
        public static final String COL_POKEMON_MOVE_METHOD_ID = "pokemon_move_method_id";
        public static final String COL_LEVEL = "level";
        public static final String COL_ORDER = "move_order";
    }

    public class PokemonSpecies {
        public static final String TABLE_NAME = "pokemon_species";
        public static final String COL_ID = "id";
        public static final String COL_IDENTIFIER = "identifier";
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
        public static final String COL_ORDER = "species_order";
        public static final String COL_CONQUEST_ORDER = "conquest_order";
    }

    public class PokemonSpeciesFlavorText {
        public static final String TABLE_NAME = "pokemon_species_flavor_text";
        public static final String COL_SPECIES_ID = "species_id";
        public static final String COL_VERSION_ID = "version_id";
        public static final String COL_LANGUAGE_ID = "language_id";
        public static final String COL_FLAVOR_TEXT = "flavor_text";
    }

    public class PokemonSpeciesNames {
        public static final String TABLE_NAME = "pokemon_species_names";
        public static final String COL_POKEMON_SPECIES_ID = "pokemon_species_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_NAME = "name";
        public static final String COL_GENUS = "genus";
    }

    public class PokemonSpeciesProse {
        public static final String TABLE_NAME = "pokemon_species_prose";
        public static final String COL_POKEMON_SPECIES_ID = "pokemon_species_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_FORM_DESCRIPTION = "form_description";
    }

    public class PokemonStats {
        public static final String TABLE_NAME = "pokemon_stats";
        public static final String COL_POKEMON_ID = "pokemon_id";
        public static final String COL_STAT_ID = "stat_id";
        public static final String COL_BASE_STAT = "base_stat";
        public static final String COL_EFFORT = "effort";
    }

    public class PokemonTypes {
        public static final String TABLE_NAME = "pokemon_types";
        public static final String COL_POKEMON_ID = "pokemon_id";
        public static final String COL_TYPE_ID = "type_id";
        public static final String COL_SLOT = "slot";
    }


    public class RegionNames {
        public static final String TABLE_NAME = "region_names";
        public static final String COL_REGION_ID = "region_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_NAME = "name";
    }

    public class Regions {
        public static final String TABLE_NAME = "regions";
        public static final String COL_ID = "id";
        public static final String COL_IDENTIFIER = "identifier";
    }


    public class StatNames {
        public static final String TABLE_NAME = "stat_names";
        public static final String COL_STAT_ID = "stat_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_NAME = "name";
    }

    public class Stats {
        public static final String TABLE_NAME = "stats";
        public static final String COL_ID = "id";
        public static final String COL_DAMAGE_CLASS_ID = "damage_class_id";
        public static final String COL_IDENTIFIER = "identifier";
        public static final String COL_IS_BATTLE_ONLY = "is_battle_only";
        public static final String COL_GAME_INDEX = "game_index";
    }


    public class TypeEfficacy {
        public static final String TABLE_NAME = "type_efficacy";
        public static final String COL_DAMAGE_TYPE_ID = "damage_type_id";
        public static final String COL_TARGET_TYPE_ID = "target_type_id";
        public static final String COL_DAMAGE_FACTOR = "damage_factor";
    }

    public class TypeNames {
        public static final String TABLE_NAME = "type_names";
        public static final String COL_TYPE_ID = "type_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_NAME = "name";
    }

    public class Types {
        public static final String TABLE_NAME = "types";
        public static final String COL_ID = "id";
        public static final String COL_IDENTIFIER = "identifier";
        public static final String COL_GENERATION_ID = "generation_id";
        public static final String COL_DAMAGE_CLASS_ID = "damage_class_id";
    }


    public class VersionGroupPokemonMoveMethods {
        public static final String TABLE_NAME = "version_group_pokemon_move_methods";
        public static final String COL_VERSION_GROUP_ID = "version_group_id";
        public static final String COL_POKEMON_MOVE_METHOD_ID = "pokemon_move_method_id";
    }

    public class VersionGroupRegions {
        public static final String TABLE_NAME = "version_group_regions";
        public static final String COL_VERSION_GROUP_ID = "version_group_id";
        public static final String COL_REGION_ID = "region_id";
    }

    public class VersionGroups {
        public static final String TABLE_NAME = "version_groups";
        public static final String COL_ID = "id";
        public static final String COL_IDENTIFIER = "identifier";
        public static final String COL_GENERATION_ID = "generation_id";
        public static final String COL_ORDER = "group_order";
    }

    public class VersionNames {
        public static final String TABLE_NAME = "version_names";
        public static final String COL_VERSION_ID = "version_id";
        public static final String COL_LOCAL_LANGUAGE_ID = "local_language_id";
        public static final String COL_NAME = "name";
    }

    public class Versions {
        public static final String TABLE_NAME = "versions";
        public static final String COL_ID = "id";
        public static final String COL_VERSION_GROUP_ID = "version_group_id";
        public static final String COL_IDENTIFIER = "identifier";
    }

}
