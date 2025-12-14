package morethermalevaporation;

import mekanism.common.lib.multiblock.MultiblockCache;
import mekanism.common.lib.multiblock.MultiblockManager;
import morethermalevaporation.common.evaporation.BasicThermalEvaporationMultiblockData;
import morethermalevaporation.common.evaporation.BasicThermalEvaporationValidator;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import morethermalevaporation.common.registries.MoreThermalEvaporationContainerTypes;
import morethermalevaporation.common.registries.MoreThermalEvaporationCreativeTabs;
import morethermalevaporation.common.registries.MoreThermalEvaporationTileEntityTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MoreThermalevaporation.MODID)
public class MoreThermalevaporation {

    public static final String MODID = "morethermalevaporation";

    public static final MultiblockManager<BasicThermalEvaporationMultiblockData> moreThermalEvaporationManager = new MultiblockManager<>("moreThermalEvaporation", MultiblockCache::new, BasicThermalEvaporationValidator::new);

    public MoreThermalevaporation() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MoreThermalEvaporationBlocks.BLOCKS.register(modEventBus);
        MoreThermalEvaporationTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        MoreThermalEvaporationContainerTypes.CONTAINER_TYPES.register(modEventBus);
        MoreThermalEvaporationCreativeTabs.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }
}