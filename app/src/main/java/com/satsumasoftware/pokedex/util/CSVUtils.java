package com.satsumasoftware.pokedex.util;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

public final class CSVUtils {


    public static final String CHANGELOG_SEP = ",\",\",";

    public static final String CHANGELOG = "changelog.csv";

    public static final String EXP_INFO_DB = "data/ExperienceDB.csv"; // TODO remove


    public static final String ABILITIES = "data/abilities.csv";
    public static final String ABILITY_FLAVOR_TEXT = "data/ability_flavor_text.csv";
    public static final String ABILITY_NAMES = "data/ability_names.csv";
    public static final String ABILITY_PROSE = "data/ability_prose.csv";
    // public static final String BERRIES = "data/berries.csv"; etc...
    // public static final String CHARACTERISTIC_TEXT = "data/characteristic_text.csv"; etc...
    // public static final String CONQUEST_EPISODE_NAMES = "data/conquest_episode_names.csv"; etc...
    // public static final String CONTEST_COMBOS = "data/contest_combos.csv"; etc...
    public static final String EGG_GROUP_PROSE = "data/egg_group_prose.csv";
    public static final String ENCOUNTER_CONDITION_PROSE = "data/encounter_condition_prose.csv";
    public static final String ENCOUNTER_CONDITION_VALUE_MAP = "data/encounter_condition_value_map.csv";
    public static final String ENCOUNTER_CONDITION_VALUE_PROSE = "data/encounter_condition_value_prose.csv";
    public static final String ENCOUNTER_CONDITION_VALUES = "data/encounter_condition_values.csv";
    public static final String ENCOUNTER_CONDITIONS = "data/encounter_conditions.csv";
    public static final String ENCOUNTER_METHOD_PROSE = "data/encounter_method_prose.csv";
    public static final String ENCOUNTER_METHODS = "data/encounter_methods.csv";
    public static final String ENCOUNTER_SLOTS = "data/encounter_slots.csv";
    public static final String ENCOUNTERS = "data/encounters.csv";
    // public static final String EVOLUTION_CHAINS = "data/evolution_chains.csv"; etc...
    public static final String EXPERIENCE = "data/experience.csv";
    // public static final String GENDERS = "data/genders.csv"; etc...
    // public static final String GENERATION_NAMES = "data/generation_names.csv"; etc...
    // public static final String GROWTH_RATE_PROSE = "data/growth_rate_prose.csv"; etc...
    // public static final String ITEM_CATEGORIES = "data/item_categories.csv"; etc...
    // public static final String LANGUAGE_NAMES = "data/language_names.csv"; etc...
    public static final String LOCATION_AREA_ENCOUNTER_RATES = "data/location_area_encounter_rates.csv";
    public static final String LOCATION_AREA_PROSE = "data/location_area_prose.csv";
    public static final String LOCATION_AREAS = "data/location_areas.csv";
    public static final String LOCATION_NAMES = "data/location_names.csv";
    public static final String LOCATIONS = "data/locations.csv";
    // public static final String MACHINES = "data/machines.csv"; etc...
    public static final String MOVE_BATTLE_STYLE_PROSE = "data/move_battle_style_prose.csv";
    public static final String MOVE_BATTLE_STYLES = "data/move_battle_styles.csv";
    public static final String MOVE_DAMAGE_CLASS_PROSE = "data/move_damage_class_prose.csv";
    public static final String MOVE_DAMAGE_CLASSES = "data/move_damage_classes.csv";
    public static final String MOVE_EFFECT_PROSE = "data/move_effect_prose.csv";
    public static final String MOVE_EFFECTS = "data/move_effects.csv";
    public static final String MOVE_FLAG_MAP = "data/move_flag_map.csv";
    public static final String MOVE_FLAG_PROSE = "data/move_flag_prose.csv";
    public static final String MOVE_FLAGS = "data/move_flags.csv";
    public static final String MOVE_FLAVOR_SUMMARIES = "data/move_flavor_summaries.csv";
    public static final String MOVE_FLAVOR_TEXT = "data/move_flavor_text.csv";
    public static final String MOVE_META = "data/move_meta.csv";
    public static final String MOVE_META_AILMENT_NAMES = "data/move_meta_ailment_names.csv";
    public static final String MOVE_META_AILMENTS = "data/move_meta_ailments.csv";
    public static final String MOVE_META_CATEGORIES = "data/move_meta_categories.csv";
    public static final String MOVE_META_CATEGORY_PROSE = "data/move_meta_category_prose.csv";
    public static final String MOVE_META_STAT_CHANGES = "data/move_meta_stat_changes.csv";
    public static final String MOVE_NAMES = "data/move_names.csv";
    public static final String MOVE_TARGET_PROSE = "data/move_target_prose.csv";
    public static final String MOVE_TARGETS = "data/move_targets.csv";
    public static final String MOVES = "data/moves.csv";
    public static final String NATURE_BATTLE_STYLE_PREFERENCES = "data/nature_battle_style_preferences.csv";
    public static final String NATURE_NAMES = "data/nature_names.csv";
    public static final String NATURE_POKEATHLON_STATS = "data/nature_pokeathlon_stats.csv";
    public static final String NATURES = "data/natures.csv";
    // public static final String PAL_PARK = "data/pal_park.csv"; etc...
    // public static final String POKEATHLON_STAT_NAMES = "data/pokeathlon_stat_names.csv"; etc...
    // public static final String POKEDEX_PROSE = "data/pokedex_prose.csv"; etc...
    public static final String POKEMON = "data/pokemon.csv";
    public static final String POKEMON_ABILITIES = "data/pokemon_abilities.csv";
    public static final String POKEMON_COLOR_NAMES = "data/pokemon_color_names.csv";
    public static final String POKEMON_COLORS = "data/pokemon_colors.csv";
    public static final String POKEMON_DEX_NUMBERS = "data/pokemon_dex_numbers.csv";
    public static final String POKEMON_EGG_GROUPS = "data/pokemon_egg_groups.csv";
    public static final String POKEMON_EVOLUTION = "data/pokemon_evolution.csv";
    public static final String POKEMON_FORM_GENERATIONS = "data/pokemon_form_generations.csv";
    public static final String POKEMON_FORM_NAMES = "data/pokemon_form_names.csv";
    public static final String POKEMON_FORM_POKEATHLON_STATS = "data/pokemon_form_pokeathlon_stats.csv";
    public static final String POKEMON_FORMS = "data/pokemon_forms.csv";
    public static final String POKEMON_GAME_INDICES = "data/pokemon_game_indices.csv";
    public static final String POKEMON_HABITAT_NAMES = "data/pokemon_habitat_names.csv";
    public static final String POKEMON_HABITATS = "data/pokemon_habitats.csv";
    public static final String POKEMON_ITEMS = "data/pokemon_items.csv";
    public static final String POKEMON_MOVE_METHOD_PROSE = "data/pokemon_move_method_prose.csv";
    public static final String POKEMON_MOVE_METHODS = "data/pokemon_move_methods.csv";
    public static final String POKEMON_MOVES = "data/pokemon_moves.csv";
    public static final String POKEMON_SHAPE_PROSE = "data/pokemon_shape_prose.csv";
    public static final String POKEMON_SHAPES = "data/pokemon_shapes.csv";
    public static final String POKEMON_SPECIES = "data/pokemon_species.csv";
    public static final String POKEMON_SPECIES_FLAVOR_SUMMARIES = "data/pokemon_species_flavor_summaries.csv";
    public static final String POKEMON_SPECIES_FLAVOR_TEXT = "data/pokemon_species_flavor_text.csv";
    public static final String POKEMON_SPECIES_NAMES = "data/pokemon_species_names.csv";
    public static final String POKEMON_SPECIES_PROSE = "data/pokemon_species_prose.csv";
    public static final String POKEMON_STATS = "data/pokemon_stats.csv";
    public static final String POKEMON_TYPES = "data/pokemon_types.csv";
    // public static final String REGION_NAMES = "data/region_names.csv"; etc...
    // public static final String STAT_NAMES = "data/stat_names.csv"; etc...
    // public static final String SUPER_CONTEST_COMBOS = "data/super_contest_combos.csv"; etc...
    // public static final String TYPE_EFFICACY = "data/type_efficacy.csv"; etc...
    // public static final String VERSION_GROUP_POKEMON_MOVE_METHODS = "data/version_group_pokemon_move_methods.csv"; etc...


    public static CsvParser getMyParser() {
        CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        settings.setNumberOfRowsToSkip(1);
        return new CsvParser(settings);
    }

}
