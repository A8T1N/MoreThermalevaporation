package morethermalevaporation;

import mekanism.common.command.builders.BuildCommand;
import mekanism.common.lib.multiblock.MultiblockCache;
import mekanism.common.lib.multiblock.MultiblockManager;
import morethermalevaporation.common.MoreThermalEvaporationLang;
import morethermalevaporation.common.command.builders.MoreThermalEvaporationBuilders.MoreEvaporationBuilder;
import morethermalevaporation.common.config.MoreThermalEvaporationConfig;
import morethermalevaporation.common.content.evaporation.MoreThermalEvaporationMultiblockData;
import morethermalevaporation.common.content.evaporation.MoreThermalEvaporationValidator;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import morethermalevaporation.common.registries.MoreThermalEvaporationContainerTypes;
import morethermalevaporation.common.registries.MoreThermalEvaporationCreativeTabs;
import morethermalevaporation.common.registries.MoreThermalEvaporationTileEntityTypes;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.EnumMap;

@Mod(MoreThermalEvaporation.MODID)
public class MoreThermalEvaporation {

    public static final String MODID = "morethermalevaporation";

    public static final EnumMap<MoreThermalEvaporationTier, MultiblockManager<MoreThermalEvaporationMultiblockData>> MoreThermalEvaporationManagers = new EnumMap<>(MoreThermalEvaporationTier.class);

    static {
        for (MoreThermalEvaporationTier tier : MoreThermalEvaporationTier.values()) {
            MoreThermalEvaporationManagers.put(tier, new MultiblockManager<>(tier.getBaseTier().getSimpleName() + "ThermalEvaporation", MultiblockCache::new, () -> new MoreThermalEvaporationValidator(tier)));
        }
    }

    public MoreThermalEvaporation(IEventBus modEventBus, ModContainer modContainer) {
        MoreThermalEvaporationBlocks.REGISTRY_BLOCKS.register(modEventBus);
        MoreThermalEvaporationTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        MoreThermalEvaporationContainerTypes.CONTAINER_TYPES.register(modEventBus);
        MoreThermalEvaporationCreativeTabs.register(modEventBus);
        MoreThermalEvaporationConfig.registerConfig(modContainer);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, this::registerCommands);
        modEventBus.addListener(MoreThermalEvaporationConfig::onConfigLoad);
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        BuildCommand.register("evaporation_basic", MoreThermalEvaporationLang.BASIC_EVAPORATION_PLANT, new MoreEvaporationBuilder(MoreThermalEvaporationTier.BASIC));
        BuildCommand.register("evaporation_advanced", MoreThermalEvaporationLang.ADVANCED_EVAPORATION_PLANT, new MoreEvaporationBuilder(MoreThermalEvaporationTier.ADVANCED));
        BuildCommand.register("evaporation_elite", MoreThermalEvaporationLang.ELITE_EVAPORATION_PLANT, new MoreEvaporationBuilder(MoreThermalEvaporationTier.ELITE));
        BuildCommand.register("evaporation_ultimate", MoreThermalEvaporationLang.ULTIMATE_EVAPORATION_PLANT, new MoreEvaporationBuilder(MoreThermalEvaporationTier.ULTIMATE));
        BuildCommand.register("evaporation_creative", MoreThermalEvaporationLang.CREATIVE_EVAPORATION_PLANT, new MoreEvaporationBuilder(MoreThermalEvaporationTier.CREATIVE));
    }
}