package morethermalevaporation.common.registries;

import mekanism.api.tier.BaseTier;
import mekanism.common.block.interfaces.IHasDescription;
import mekanism.common.block.prefab.BlockBasicMultiblock;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.resource.BlockResourceInfo;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.tile.multiblock.*;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class MoreThermalEvaporationBlocks {

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(MoreThermalEvaporation.MODID);
    // Basic
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityBasicThermalEvaporationBlock>, ItemBlockMekanismTier<BlockBasicMultiblock<TileEntityBasicThermalEvaporationBlock>>> BASIC_THERMAL_EVAPORATION_BLOCK = registerBlock("basic_thermal_evaporation_block", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.BASIC_THERMAL_EVAPORATION_BLOCK, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), BaseTier.BASIC);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityBasicThermalEvaporationValve>, ItemBlockMekanismTier<BlockBasicMultiblock<TileEntityBasicThermalEvaporationValve>>> BASIC_THERMAL_EVAPORATION_VALVE = registerBlock("basic_thermal_evaporation_valve", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.BASIC_THERMAL_EVAPORATION_VALVE, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), BaseTier.BASIC);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityBasicThermalEvaporationController>, ItemBlockMekanismTier<BlockBasicMultiblock<TileEntityBasicThermalEvaporationController>>> BASIC_THERMAL_EVAPORATION_CONTROLLER = registerBlock("basic_thermal_evaporation_controller", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.BASIC_THERMAL_EVAPORATION_CONTROLLER, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), BaseTier.BASIC);

    // Advanced
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityAdvancedThermalEvaporationBlock>, ItemBlockMekanismTier<BlockBasicMultiblock<TileEntityAdvancedThermalEvaporationBlock>>> ADVANCED_THERMAL_EVAPORATION_BLOCK = registerBlock("advanced_thermal_evaporation_block", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.ADVANCED_THERMAL_EVAPORATION_BLOCK, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), BaseTier.ADVANCED);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityAdvancedThermalEvaporationValve>, ItemBlockMekanismTier<BlockBasicMultiblock<TileEntityAdvancedThermalEvaporationValve>>> ADVANCED_THERMAL_EVAPORATION_VALVE = registerBlock("advanced_thermal_evaporation_valve", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.ADVANCED_THERMAL_EVAPORATION_VALVE, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), BaseTier.ADVANCED);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityAdvancedThermalEvaporationController>, ItemBlockMekanismTier<BlockBasicMultiblock<TileEntityAdvancedThermalEvaporationController>>> ADVANCED_THERMAL_EVAPORATION_CONTROLLER = registerBlock("advanced_thermal_evaporation_controller", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.ADVANCED_THERMAL_EVAPORATION_CONTROLLER, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), BaseTier.ADVANCED);

    // Elite
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityEliteThermalEvaporationBlock>, ItemBlockMekanismTier<BlockBasicMultiblock<TileEntityEliteThermalEvaporationBlock>>> ELITE_THERMAL_EVAPORATION_BLOCK = registerBlock("elite_thermal_evaporation_block", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.ELITE_THERMAL_EVAPORATION_BLOCK, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), BaseTier.ELITE);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityEliteThermalEvaporationValve>, ItemBlockMekanismTier<BlockBasicMultiblock<TileEntityEliteThermalEvaporationValve>>> ELITE_THERMAL_EVAPORATION_VALVE = registerBlock("elite_thermal_evaporation_valve", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.ELITE_THERMAL_EVAPORATION_VALVE, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), BaseTier.ELITE);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityEliteThermalEvaporationController>, ItemBlockMekanismTier<BlockBasicMultiblock<TileEntityEliteThermalEvaporationController>>> ELITE_THERMAL_EVAPORATION_CONTROLLER = registerBlock("elite_thermal_evaporation_controller", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.ELITE_THERMAL_EVAPORATION_CONTROLLER, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), BaseTier.ELITE);

    // Ultimate
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityUltimateThermalEvaporationBlock>, ItemBlockMekanismTier<BlockBasicMultiblock<TileEntityUltimateThermalEvaporationBlock>>> ULTIMATE_THERMAL_EVAPORATION_BLOCK = registerBlock("ultimate_thermal_evaporation_block", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.ULTIMATE_THERMAL_EVAPORATION_BLOCK, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), BaseTier.ULTIMATE);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityUltimateThermalEvaporationValve>, ItemBlockMekanismTier<BlockBasicMultiblock<TileEntityUltimateThermalEvaporationValve>>> ULTIMATE_THERMAL_EVAPORATION_VALVE = registerBlock("ultimate_thermal_evaporation_valve", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.ULTIMATE_THERMAL_EVAPORATION_VALVE, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), BaseTier.ULTIMATE);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityUltimateThermalEvaporationController>, ItemBlockMekanismTier<BlockBasicMultiblock<TileEntityUltimateThermalEvaporationController>>> ULTIMATE_THERMAL_EVAPORATION_CONTROLLER = registerBlock("ultimate_thermal_evaporation_controller", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.ULTIMATE_THERMAL_EVAPORATION_CONTROLLER, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), BaseTier.ULTIMATE);

    private MoreThermalEvaporationBlocks() {
    }

    private static <BLOCK extends Block & IHasDescription> BlockRegistryObject<BLOCK, ItemBlockMekanismTier<BLOCK>> registerBlock(
            String name,
            Supplier<? extends BLOCK> blockSupplier,
            BaseTier tier) {

        return BLOCKS.registerDefaultProperties(name, blockSupplier, (block, props) -> new ItemBlockMekanismTier<>(block, props, tier));
    }
}
