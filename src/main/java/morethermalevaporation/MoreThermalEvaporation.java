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
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.EnumMap;

@Mod(MoreThermalEvaporation.MODID)
public class MoreThermalEvaporation {

    public static final String MODID = "morethermalevaporation";
    public static boolean JustEnoughMekanismMultiblocksLoaded = false;
    public static boolean EvolvedMekanismLoaded = false;

    public static final EnumMap<MoreThermalEvaporationTier, MultiblockManager<MoreThermalEvaporationMultiblockData>> MoreThermalEvaporationManagers = new EnumMap<>(MoreThermalEvaporationTier.class);

    static {
        MoreThermalEvaporationTier.availableTiers().forEach(tier -> {
            MoreThermalEvaporationManagers.put(tier, new MultiblockManager<>(tier.getBaseTier().getSimpleName() + "ThermalEvaporation", MultiblockCache::new, () -> new MoreThermalEvaporationValidator(tier)));
        });
    }

    public MoreThermalEvaporation(IEventBus modEventBus, ModContainer modContainer) {
        MoreThermalEvaporationBlocks.REGISTRY_BLOCKS.register(modEventBus);
        MoreThermalEvaporationTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        MoreThermalEvaporationContainerTypes.CONTAINER_TYPES.register(modEventBus);
        MoreThermalEvaporationCreativeTabs.register(modEventBus);
        MoreThermalEvaporationConfig.registerConfig(modContainer);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, this::registerCommands);
        modEventBus.addListener(MoreThermalEvaporation::onCommonSetup);
        modEventBus.addListener(MoreThermalEvaporationConfig::onConfigLoad);
    }

    public static void onCommonSetup(FMLCommonSetupEvent e) {
        ModList modList = ModList.get();
        JustEnoughMekanismMultiblocksLoaded = modList.isLoaded("jei_mekanism_multiblocks");
        EvolvedMekanismLoaded = modList.isLoaded("evolvedmekanism");
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        MoreThermalEvaporationTier.availableTiers().forEach(tier -> {
            BuildCommand.register("evaporation_" + tier.getBaseTier().getLowerName(), MoreThermalEvaporationLang.getLangPlant(tier), new MoreEvaporationBuilder(tier));
        });
    }
}