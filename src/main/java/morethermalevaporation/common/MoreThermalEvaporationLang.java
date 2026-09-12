package morethermalevaporation.common;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.text.ILangEntry;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import net.minecraft.Util;

import java.util.EnumMap;
import java.util.Locale;

@NothingNullByDefault
public enum MoreThermalEvaporationLang implements ILangEntry {

    // Basic
    DESCRIPTION_BASIC_THERMAL_EVAPORATION_BLOCK("description", "basic_thermal_evaporation_block"),
    DESCRIPTION_BASIC_THERMAL_EVAPORATION_VALVE("description", "basic_thermal_evaporation_valve"),
    DESCRIPTION_BASIC_THERMAL_EVAPORATION_CONTROLLER("description", "basic_thermal_evaporation_controller"),

    BASIC_EVAPORATION_HEIGHT("evaporation", "basic_height"),
    BASIC_FLUID_PRODUCTION("evaporation", "basic_fluid_production"),
    BASIC_EVAPORATION_PLANT("evaporation", "basic_evaporation_plant"),

    DESCRIPTION_BASIC_THERMAL_EVAPORATION_COMPACT("description", "basic_thermal_evaporation_compact"),
    BASIC_EVAPORATION_COMPACT("evaporation", "basic_evaporation_compact"),

    // Advanced
    DESCRIPTION_ADVANCED_THERMAL_EVAPORATION_BLOCK("description", "advanced_thermal_evaporation_block"),
    DESCRIPTION_ADVANCED_THERMAL_EVAPORATION_VALVE("description", "advanced_thermal_evaporation_valve"),
    DESCRIPTION_ADVANCED_THERMAL_EVAPORATION_CONTROLLER("description", "advanced_thermal_evaporation_controller"),

    ADVANCED_EVAPORATION_HEIGHT("evaporation", "advanced_height"),
    ADVANCED_FLUID_PRODUCTION("evaporation", "advanced_fluid_production"),
    ADVANCED_EVAPORATION_PLANT("evaporation", "advanced_evaporation_plant"),

    DESCRIPTION_ADVANCED_THERMAL_EVAPORATION_COMPACT("description", "advanced_thermal_evaporation_compact"),
    ADVANCED_EVAPORATION_COMPACT("evaporation", "advanced_evaporation_compact"),

    // Elite
    DESCRIPTION_ELITE_THERMAL_EVAPORATION_BLOCK("description", "elite_thermal_evaporation_block"),
    DESCRIPTION_ELITE_THERMAL_EVAPORATION_VALVE("description", "elite_thermal_evaporation_valve"),
    DESCRIPTION_ELITE_THERMAL_EVAPORATION_CONTROLLER("description", "elite_thermal_evaporation_controller"),

    ELITE_EVAPORATION_HEIGHT("evaporation", "elite_height"),
    ELITE_FLUID_PRODUCTION("evaporation", "elite_fluid_production"),
    ELITE_EVAPORATION_PLANT("evaporation", "elite_evaporation_plant"),

    DESCRIPTION_ELITE_THERMAL_EVAPORATION_COMPACT("description", "elite_thermal_evaporation_compact"),
    ELITE_EVAPORATION_COMPACT("evaporation", "elite_evaporation_compact"),

    // Ultimate
    DESCRIPTION_ULTIMATE_THERMAL_EVAPORATION_BLOCK("description", "ultimate_thermal_evaporation_block"),
    DESCRIPTION_ULTIMATE_THERMAL_EVAPORATION_VALVE("description", "ultimate_thermal_evaporation_valve"),
    DESCRIPTION_ULTIMATE_THERMAL_EVAPORATION_CONTROLLER("description", "ultimate_thermal_evaporation_controller"),

    ULTIMATE_EVAPORATION_HEIGHT("evaporation", "ultimate_height"),
    ULTIMATE_FLUID_PRODUCTION("evaporation", "ultimate_fluid_production"),
    ULTIMATE_EVAPORATION_PLANT("evaporation", "ultimate_evaporation_plant"),

    DESCRIPTION_ULTIMATE_THERMAL_EVAPORATION_COMPACT("description", "ultimate_thermal_evaporation_compact"),
    ULTIMATE_EVAPORATION_COMPACT("evaporation", "ultimate_evaporation_compact"),

    // Creative
    DESCRIPTION_CREATIVE_THERMAL_EVAPORATION_BLOCK("description", "creative_thermal_evaporation_block"),
    DESCRIPTION_CREATIVE_THERMAL_EVAPORATION_VALVE("description", "creative_thermal_evaporation_valve"),
    DESCRIPTION_CREATIVE_THERMAL_EVAPORATION_CONTROLLER("description", "creative_thermal_evaporation_controller"),

    CREATIVE_EVAPORATION_HEIGHT("evaporation", "creative_height"),
    CREATIVE_FLUID_PRODUCTION("evaporation", "creative_fluid_production"),
    CREATIVE_EVAPORATION_PLANT("evaporation", "creative_evaporation_plant"),

    DESCRIPTION_CREATIVE_THERMAL_EVAPORATION_COMPACT("description", "creative_thermal_evaporation_compact"),
    CREATIVE_EVAPORATION_COMPACT("evaporation", "creative_evaporation_compact"),

    // Evolved Mekanism
    // Overclocked
    DESCRIPTION_OVERCLOCKED_THERMAL_EVAPORATION_BLOCK("description", "overclocked_thermal_evaporation_block"),
    DESCRIPTION_OVERCLOCKED_THERMAL_EVAPORATION_VALVE("description", "overclocked_thermal_evaporation_valve"),
    DESCRIPTION_OVERCLOCKED_THERMAL_EVAPORATION_CONTROLLER("description", "overclocked_thermal_evaporation_controller"),

    OVERCLOCKED_EVAPORATION_HEIGHT("evaporation", "overclocked_height"),
    OVERCLOCKED_FLUID_PRODUCTION("evaporation", "overclocked_fluid_production"),
    OVERCLOCKED_EVAPORATION_PLANT("evaporation", "overclocked_evaporation_plant"),

    DESCRIPTION_OVERCLOCKED_THERMAL_EVAPORATION_COMPACT("description", "overclocked_thermal_evaporation_compact"),
    OVERCLOCKED_EVAPORATION_COMPACT("evaporation", "overclocked_evaporation_compact"),

    // Quantum
    DESCRIPTION_QUANTUM_THERMAL_EVAPORATION_BLOCK("description", "quantum_thermal_evaporation_block"),
    DESCRIPTION_QUANTUM_THERMAL_EVAPORATION_VALVE("description", "quantum_thermal_evaporation_valve"),
    DESCRIPTION_QUANTUM_THERMAL_EVAPORATION_CONTROLLER("description", "quantum_thermal_evaporation_controller"),

    QUANTUM_EVAPORATION_HEIGHT("evaporation", "quantum_height"),
    QUANTUM_FLUID_PRODUCTION("evaporation", "quantum_fluid_production"),
    QUANTUM_EVAPORATION_PLANT("evaporation", "quantum_evaporation_plant"),

    DESCRIPTION_QUANTUM_THERMAL_EVAPORATION_COMPACT("description", "quantum_thermal_evaporation_compact"),
    QUANTUM_EVAPORATION_COMPACT("evaporation", "quantum_evaporation_compact"),

    // Dense
    DESCRIPTION_DENSE_THERMAL_EVAPORATION_BLOCK("description", "dense_thermal_evaporation_block"),
    DESCRIPTION_DENSE_THERMAL_EVAPORATION_VALVE("description", "dense_thermal_evaporation_valve"),
    DESCRIPTION_DENSE_THERMAL_EVAPORATION_CONTROLLER("description", "dense_thermal_evaporation_controller"),

    DENSE_EVAPORATION_HEIGHT("evaporation", "dense_height"),
    DENSE_FLUID_PRODUCTION("evaporation", "dense_fluid_production"),
    DENSE_EVAPORATION_PLANT("evaporation", "dense_evaporation_plant"),

    DESCRIPTION_DENSE_THERMAL_EVAPORATION_COMPACT("description", "dense_thermal_evaporation_compact"),
    DENSE_EVAPORATION_COMPACT("evaporation", "dense_evaporation_compact"),

    // Multiversal
    DESCRIPTION_MULTIVERSAL_THERMAL_EVAPORATION_BLOCK("description", "multiversal_thermal_evaporation_block"),
    DESCRIPTION_MULTIVERSAL_THERMAL_EVAPORATION_VALVE("description", "multiversal_thermal_evaporation_valve"),
    DESCRIPTION_MULTIVERSAL_THERMAL_EVAPORATION_CONTROLLER("description", "multiversal_thermal_evaporation_controller"),

    MULTIVERSAL_EVAPORATION_HEIGHT("evaporation", "multiversal_height"),
    MULTIVERSAL_FLUID_PRODUCTION("evaporation", "multiversal_fluid_production"),
    MULTIVERSAL_EVAPORATION_PLANT("evaporation", "multiversal_evaporation_plant"),

    DESCRIPTION_MULTIVERSAL_THERMAL_EVAPORATION_COMPACT("description", "multiversal_thermal_evaporation_compact"),
    MULTIVERSAL_EVAPORATION_COMPACT("evaporation", "multiversal_evaporation_compact"),

    // MultiBlock
    MULTIBLOCK_TYPE("multiblock", "type"),

    // Type
    TYPE_NORMAL("type", "normal"),
    TYPE_LARGE("type", "large"),
    ;

    private final String key;

    MoreThermalEvaporationLang(String type, String path) {
        this(Util.makeDescriptionId(type, MoreThermalEvaporation.rl(path)));
    }

    MoreThermalEvaporationLang(String key) {
        this.key = key;
    }

    @Override
    public String getTranslationKey() {
        return key;
    }

    private static final EnumMap<MoreThermalEvaporationTier, MoreThermalEvaporationLang> DESCRIPTION_BLOCK_LANGS = createTierMap("DESCRIPTION_%s_THERMAL_EVAPORATION_BLOCK");
    private static final EnumMap<MoreThermalEvaporationTier, MoreThermalEvaporationLang> DESCRIPTION_VALVE_LANGS = createTierMap("DESCRIPTION_%s_THERMAL_EVAPORATION_VALVE");
    private static final EnumMap<MoreThermalEvaporationTier, MoreThermalEvaporationLang> DESCRIPTION_CONTROLLER_LANGS = createTierMap("DESCRIPTION_%s_THERMAL_EVAPORATION_CONTROLLER");
    private static final EnumMap<MoreThermalEvaporationTier, MoreThermalEvaporationLang> PLANT_LANGS = createTierMap("%s_EVAPORATION_PLANT");
    private static final EnumMap<MoreThermalEvaporationTier, MoreThermalEvaporationLang> DESCRIPTION_COMPACT_LANGS = createTierMap("DESCRIPTION_%s_THERMAL_EVAPORATION_COMPACT");
    private static final EnumMap<MoreThermalEvaporationTier, MoreThermalEvaporationLang> COMPACT_LANGS = createTierMap("%s_EVAPORATION_COMPACT");

    private static EnumMap<MoreThermalEvaporationTier, MoreThermalEvaporationLang> createTierMap(String mapName) {
        EnumMap<MoreThermalEvaporationTier, MoreThermalEvaporationLang> map = new EnumMap<>(MoreThermalEvaporationTier.class);
        MoreThermalEvaporationTier.availableTiers().forEach(tier -> {
            String enumName = String.format(mapName, tier.name().toUpperCase(Locale.ROOT));
            map.put(tier, MoreThermalEvaporationLang.valueOf(enumName));
        });
        return map;
    }

    public static MoreThermalEvaporationLang getLangDescriptionBlock(MoreThermalEvaporationTier tier) {
        return DESCRIPTION_BLOCK_LANGS.get(tier);
    }

    public static MoreThermalEvaporationLang getLangDescriptionValve(MoreThermalEvaporationTier tier) {
        return DESCRIPTION_VALVE_LANGS.get(tier);
    }

    public static MoreThermalEvaporationLang getLangDescriptionController(MoreThermalEvaporationTier tier) {
        return DESCRIPTION_CONTROLLER_LANGS.get(tier);
    }

    public static MoreThermalEvaporationLang getLangPlant(MoreThermalEvaporationTier tier) {
        return PLANT_LANGS.get(tier);
    }

    public static MoreThermalEvaporationLang getLangDescriptionCompact(MoreThermalEvaporationTier tier) {
        return DESCRIPTION_COMPACT_LANGS.get(tier);
    }

    public static MoreThermalEvaporationLang getLangCompact(MoreThermalEvaporationTier tier) {
        return COMPACT_LANGS.get(tier);
    }

}
