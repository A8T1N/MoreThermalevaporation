package morethermalevaporation.common.config;

import mekanism.common.config.IMekanismConfig;
import mekanism.common.config.MekanismConfigHelper;
import morethermalevaporation.MoreThermalEvaporation;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.event.config.ModConfigEvent;

import java.util.HashMap;
import java.util.Map;

public class MoreThermalEvaporationConfig {

    private static final Map<IConfigSpec, IMekanismConfig> KNOWN_CONFIGS = new HashMap<>();

    public static final MoreThermalEvaporationPlantConfig config =
            new MoreThermalEvaporationPlantConfig();

    private MoreThermalEvaporationConfig() {
    }

    public static void registerConfig(ModContainer modContainer) {
        MekanismConfigHelper.registerConfig(KNOWN_CONFIGS, modContainer, config);
    }

    public static void onConfigLoad(ModConfigEvent event) {
        MekanismConfigHelper.onConfigLoad(event, MoreThermalEvaporation.MODID, KNOWN_CONFIGS);
    }
}