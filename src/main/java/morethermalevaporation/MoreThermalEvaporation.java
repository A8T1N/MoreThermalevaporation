package morethermalevaporation;

import mekanism.common.lib.multiblock.MultiblockCache;
import mekanism.common.lib.multiblock.MultiblockManager;
import morethermalevaporation.common.config.MoreThermalEvaporationConfig;
import morethermalevaporation.common.evaporation.*;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import morethermalevaporation.common.registries.MoreThermalEvaporationContainerTypes;
import morethermalevaporation.common.registries.MoreThermalEvaporationCreativeTabs;
import morethermalevaporation.common.registries.MoreThermalEvaporationTileEntityTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MoreThermalEvaporation.MODID)
public class MoreThermalEvaporation {

    public static final String MODID = "morethermalevaporation";

    public static final MultiblockManager<BasicThermalEvaporationMultiblockData> BasicThermalEvaporationManager = new MultiblockManager<>("BasicThermalEvaporation", MultiblockCache::new, BasicThermalEvaporationValidator::new);
    public static final MultiblockManager<AdvancedThermalEvaporationMultiblockData> AdvancedThermalEvaporationManager = new MultiblockManager<>("AdvancedThermalEvaporation", MultiblockCache::new, AdvancedThermalEvaporationValidator::new);
    public static final MultiblockManager<EliteThermalEvaporationMultiblockData> EliteThermalEvaporationManager = new MultiblockManager<>("EliteThermalEvaporation", MultiblockCache::new, EliteThermalEvaporationValidator::new);
    public static final MultiblockManager<UltimateThermalEvaporationMultiblockData> UltimateThermalEvaporationManager = new MultiblockManager<>("UltimateThermalEvaporation", MultiblockCache::new, UltimateThermalEvaporationValidator::new);

    public MoreThermalEvaporation() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MoreThermalEvaporationBlocks.BLOCKS.register(modEventBus);
        MoreThermalEvaporationTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        MoreThermalEvaporationContainerTypes.CONTAINER_TYPES.register(modEventBus);
        MoreThermalEvaporationCreativeTabs.register(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MoreThermalEvaporationConfig.configSpec);

        MinecraftForge.EVENT_BUS.register(this);
    }
}