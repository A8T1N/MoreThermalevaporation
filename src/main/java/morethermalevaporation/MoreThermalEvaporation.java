package morethermalevaporation;

import mekanism.common.command.builders.BuildCommand;
import mekanism.common.lib.multiblock.MultiblockCache;
import mekanism.common.lib.multiblock.MultiblockManager;
import morethermalevaporation.common.MoreThermalEvaporationLang;
import morethermalevaporation.common.command.builders.MTEBuilders.AdvancedEvaporationBuilder;
import morethermalevaporation.common.command.builders.MTEBuilders.BasicEvaporationBuilder;
import morethermalevaporation.common.command.builders.MTEBuilders.EliteEvaporationBuilder;
import morethermalevaporation.common.command.builders.MTEBuilders.UltimateEvaporationBuilder;
import morethermalevaporation.common.config.MoreThermalEvaporationConfig;
import morethermalevaporation.common.evaporation.*;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import morethermalevaporation.common.registries.MoreThermalEvaporationContainerTypes;
import morethermalevaporation.common.registries.MoreThermalEvaporationCreativeTabs;
import morethermalevaporation.common.registries.MoreThermalEvaporationTileEntityTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
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
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        BuildCommand.register("evaporation_basic", MoreThermalEvaporationLang.BASIC_EVAPORATION_PLANT, new BasicEvaporationBuilder());
        BuildCommand.register("evaporation_advanced", MoreThermalEvaporationLang.ADVANCED_EVAPORATION_PLANT, new AdvancedEvaporationBuilder());
        BuildCommand.register("evaporation_elite", MoreThermalEvaporationLang.ELITE_EVAPORATION_PLANT, new EliteEvaporationBuilder());
        BuildCommand.register("evaporation_ultimate", MoreThermalEvaporationLang.ULTIMATE_EVAPORATION_PLANT, new UltimateEvaporationBuilder());
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation("morethermalevaporation", path);
    }
}