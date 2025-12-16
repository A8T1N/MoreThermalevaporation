package morethermalevaporation.common.registries;

import mekanism.common.block.interfaces.IHasDescription;
import mekanism.common.block.prefab.BlockBasicMultiblock;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.resource.BlockResourceInfo;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.tile.multiblock.*;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class MoreThermalEvaporationBlocks {

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(MoreThermalEvaporation.MODID);
    // Basic
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityBasicThermalEvaporationBlock>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityBasicThermalEvaporationBlock>>> BASIC_THERMAL_EVAPORATION_BLOCK = registerBlock("basic_thermal_evaporation_block", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.BASIC_THERMAL_EVAPORATION_BLOCK, properties -> properties.mapColor(BlockResourceInfo.TIN.getMapColor())));
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityBasicThermalEvaporationValve>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityBasicThermalEvaporationValve>>> BASIC_THERMAL_EVAPORATION_VALVE = registerBlock("basic_thermal_evaporation_valve", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.BASIC_THERMAL_EVAPORATION_VALVE, properties -> properties.mapColor(BlockResourceInfo.TIN.getMapColor())));
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityBasicThermalEvaporationController>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityBasicThermalEvaporationController>>> BASIC_THERMAL_EVAPORATION_CONTROLLER = registerBlock("basic_thermal_evaporation_controller", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.BASIC_THERMAL_EVAPORATION_CONTROLLER, properties -> properties.mapColor(BlockResourceInfo.TIN.getMapColor())));

    // Advanced
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityAdvancedThermalEvaporationBlock>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityAdvancedThermalEvaporationBlock>>> ADVANCED_THERMAL_EVAPORATION_BLOCK = registerBlock("advanced_thermal_evaporation_block", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.ADVANCED_THERMAL_EVAPORATION_BLOCK, properties -> properties.mapColor(BlockResourceInfo.TIN.getMapColor())));
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityAdvancedThermalEvaporationValve>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityAdvancedThermalEvaporationValve>>> ADVANCED_THERMAL_EVAPORATION_VALVE = registerBlock("advanced_thermal_evaporation_valve", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.ADVANCED_THERMAL_EVAPORATION_VALVE, properties -> properties.mapColor(BlockResourceInfo.TIN.getMapColor())));
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityAdvancedThermalEvaporationController>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityAdvancedThermalEvaporationController>>> ADVANCED_THERMAL_EVAPORATION_CONTROLLER = registerBlock("advanced_thermal_evaporation_controller", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.ADVANCED_THERMAL_EVAPORATION_CONTROLLER, properties -> properties.mapColor(BlockResourceInfo.TIN.getMapColor())));

    // Elite
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityEliteThermalEvaporationBlock>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityEliteThermalEvaporationBlock>>> ELITE_THERMAL_EVAPORATION_BLOCK = registerBlock("elite_thermal_evaporation_block", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.ELITE_THERMAL_EVAPORATION_BLOCK, properties -> properties.mapColor(BlockResourceInfo.TIN.getMapColor())));
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityEliteThermalEvaporationValve>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityEliteThermalEvaporationValve>>> ELITE_THERMAL_EVAPORATION_VALVE = registerBlock("elite_thermal_evaporation_valve", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.ELITE_THERMAL_EVAPORATION_VALVE, properties -> properties.mapColor(BlockResourceInfo.TIN.getMapColor())));
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityEliteThermalEvaporationController>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityEliteThermalEvaporationController>>> ELITE_THERMAL_EVAPORATION_CONTROLLER = registerBlock("elite_thermal_evaporation_controller", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.ELITE_THERMAL_EVAPORATION_CONTROLLER, properties -> properties.mapColor(BlockResourceInfo.TIN.getMapColor())));

    // Ultimate
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityUltimateThermalEvaporationBlock>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityUltimateThermalEvaporationBlock>>> ULTIMATE_THERMAL_EVAPORATION_BLOCK = registerBlock("ultimate_thermal_evaporation_block", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.ULTIMATE_THERMAL_EVAPORATION_BLOCK, properties -> properties.mapColor(BlockResourceInfo.TIN.getMapColor())));
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityUltimateThermalEvaporationValve>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityUltimateThermalEvaporationValve>>> ULTIMATE_THERMAL_EVAPORATION_VALVE = registerBlock("ultimate_thermal_evaporation_valve", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.ULTIMATE_THERMAL_EVAPORATION_VALVE, properties -> properties.mapColor(BlockResourceInfo.TIN.getMapColor())));
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityUltimateThermalEvaporationController>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityUltimateThermalEvaporationController>>> ULTIMATE_THERMAL_EVAPORATION_CONTROLLER = registerBlock("ultimate_thermal_evaporation_controller", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.ULTIMATE_THERMAL_EVAPORATION_CONTROLLER, properties -> properties.mapColor(BlockResourceInfo.TIN.getMapColor())));

    private MoreThermalEvaporationBlocks() {
    }

    private static <BLOCK extends Block & IHasDescription> BlockRegistryObject<BLOCK, ItemBlockTooltip<BLOCK>> registerBlock(String name,
                                                                                                                             Supplier<? extends BLOCK> blockSupplier) {
        return BLOCKS.registerDefaultProperties(name, blockSupplier, ItemBlockTooltip::new);
    }

    private static <BLOCK extends Block & IHasDescription> BlockRegistryObject<BLOCK, ItemBlockTooltip<BLOCK>> registerBlock(String name,
                                                                                                                             Supplier<? extends BLOCK> blockSupplier, Rarity rarity) {
        return BLOCKS.registerDefaultProperties(name, blockSupplier, (block, props) -> new ItemBlockTooltip<>(block, props.rarity(rarity)));
    }
}
