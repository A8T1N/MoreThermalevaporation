package morethermalevaporation;

import mekanism.common.command.builders.BuildCommand;
import mekanism.common.lib.multiblock.MultiblockCache;
import mekanism.common.lib.multiblock.MultiblockManager;
import morethermalevaporation.common.MoreThermalEvaporationLang;
import morethermalevaporation.common.command.builders.MoreThermalEvaporationBuilders.MoreEvaporationBuilder;
import morethermalevaporation.common.config.MoreThermalEvaporationConfig;
import morethermalevaporation.common.content.evaporation.MoreThermalEvaporationMultiblockData;
import morethermalevaporation.common.content.evaporation.MoreThermalEvaporationValidator;
import morethermalevaporation.common.registries.*;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.EnumMap;

@Mod(MoreThermalEvaporation.MODID)
public class MoreThermalEvaporation {

    public static final String MODID = "morethermalevaporation";
    public static boolean JustEnoughMekanismMultiblocksLoaded = false;
    public static boolean PipezLoaded = false;

    public static final EnumMap<MoreThermalEvaporationTier, MultiblockManager<MoreThermalEvaporationMultiblockData>> MoreThermalEvaporationManagers = new EnumMap<>(MoreThermalEvaporationTier.class);

    static {
        for (MoreThermalEvaporationTier tier : MoreThermalEvaporationTier.values()) {
            MoreThermalEvaporationManagers.put(tier, new MultiblockManager<>(tier.getBaseTier().getSimpleName() + "ThermalEvaporation", MultiblockCache::new, () -> new MoreThermalEvaporationValidator(tier)));
        }
    }

    public MoreThermalEvaporation() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MoreThermalEvaporationBlocks.REGISTRY_BLOCKS.register(modEventBus);
        MoreThermalEvaporationItems.REGISTRY_ITEMS.register(modEventBus);
        MoreThermalEvaporationTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        MoreThermalEvaporationContainerTypes.CONTAINER_TYPES.register(modEventBus);
        MoreThermalEvaporationCreativeTabs.register(modEventBus);
        MoreThermalEvaporationConfig.registerConfig(ModLoadingContext.get());
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, this::registerCommands);
        modEventBus.addListener(MoreThermalEvaporation::onCommonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void onCommonSetup(FMLCommonSetupEvent e) {
        ModList modList = ModList.get();
        JustEnoughMekanismMultiblocksLoaded = modList.isLoaded("jei_mekanism_multiblocks");
        PipezLoaded = modList.isLoaded("pipez");
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation("morethermalevaporation", path);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        BuildCommand.register("evaporation_basic", MoreThermalEvaporationLang.BASIC_EVAPORATION_PLANT, new MoreEvaporationBuilder(MoreThermalEvaporationTier.BASIC));
        BuildCommand.register("evaporation_advanced", MoreThermalEvaporationLang.ADVANCED_EVAPORATION_PLANT, new MoreEvaporationBuilder(MoreThermalEvaporationTier.ADVANCED));
        BuildCommand.register("evaporation_elite", MoreThermalEvaporationLang.ELITE_EVAPORATION_PLANT, new MoreEvaporationBuilder(MoreThermalEvaporationTier.ELITE));
        BuildCommand.register("evaporation_ultimate", MoreThermalEvaporationLang.ULTIMATE_EVAPORATION_PLANT, new MoreEvaporationBuilder(MoreThermalEvaporationTier.ULTIMATE));
        BuildCommand.register("evaporation_creative", MoreThermalEvaporationLang.CREATIVE_EVAPORATION_PLANT, new MoreEvaporationBuilder(MoreThermalEvaporationTier.CREATIVE));
    }
}