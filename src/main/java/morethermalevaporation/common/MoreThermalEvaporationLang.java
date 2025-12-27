package morethermalevaporation.common;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.text.ILangEntry;
import morethermalevaporation.MoreThermalEvaporation;
import net.minecraft.Util;

@NothingNullByDefault
public enum MoreThermalEvaporationLang implements ILangEntry {

    // Basic
    DESCRIPTION_BASIC_THERMAL_EVAPORATION_CONTROLLER("description", "basic_thermal_evaporation_controller"),
    DESCRIPTION_BASIC_THERMAL_EVAPORATION_VALVE("description", "basic_thermal_evaporation_valve"),
    DESCRIPTION_BASIC_THERMAL_EVAPORATION_BLOCK("description", "basic_thermal_evaporation_block"),

    BASIC_EVAPORATION_HEIGHT("basic_evaporation", "basic_height"),
    BASIC_FLUID_PRODUCTION("basic_evaporation", "basic_fluid_production"),
    BASIC_EVAPORATION_PLANT("basic_evaporation", "basic_evaporation_plant"),

    // Advanced
    DESCRIPTION_ADVANCED_THERMAL_EVAPORATION_CONTROLLER("description", "advanced_thermal_evaporation_controller"),
    DESCRIPTION_ADVANCED_THERMAL_EVAPORATION_VALVE("description", "advanced_thermal_evaporation_valve"),
    DESCRIPTION_ADVANCED_THERMAL_EVAPORATION_BLOCK("description", "advanced_thermal_evaporation_block"),

    ADVANCED_EVAPORATION_HEIGHT("advanced_evaporation", "advanced_height"),
    ADVANCED_FLUID_PRODUCTION("advanced_evaporation", "advanced_fluid_production"),
    ADVANCED_EVAPORATION_PLANT("advanced_evaporation", "advanced_evaporation_plant"),

    // Elite
    DESCRIPTION_ELITE_THERMAL_EVAPORATION_CONTROLLER("description", "elite_thermal_evaporation_controller"),
    DESCRIPTION_ELITE_THERMAL_EVAPORATION_VALVE("description", "elite_thermal_evaporation_valve"),
    DESCRIPTION_ELITE_THERMAL_EVAPORATION_BLOCK("description", "elite_thermal_evaporation_block"),

    ELITE_EVAPORATION_HEIGHT("elite_evaporation", "elite_height"),
    ELITE_FLUID_PRODUCTION("elite_evaporation", "elite_fluid_production"),
    ELITE_EVAPORATION_PLANT("elite_evaporation", "elite_evaporation_plant"),

    // Ultimate
    DESCRIPTION_ULTIMATE_THERMAL_EVAPORATION_CONTROLLER("description", "ultimate_thermal_evaporation_controller"),
    DESCRIPTION_ULTIMATE_THERMAL_EVAPORATION_VALVE("description", "ultimate_thermal_evaporation_valve"),
    DESCRIPTION_ULTIMATE_THERMAL_EVAPORATION_BLOCK("description", "ultimate_thermal_evaporation_block"),

    ULTIMATE_EVAPORATION_HEIGHT("ultimate_evaporation", "ultimate_height"),
    ULTIMATE_FLUID_PRODUCTION("ultimate_evaporation", "ultimate_fluid_production"),
    ULTIMATE_EVAPORATION_PLANT("ultimate_evaporation", "ultimate_evaporation_plant"),
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
}
