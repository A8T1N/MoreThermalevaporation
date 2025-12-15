package morethermalevaporation.common.registries;

import mekanism.common.block.interfaces.IHasDescription;
import mekanism.common.block.prefab.BlockBasicMultiblock;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.resource.BlockResourceInfo;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.tile.multiblock.TileEntityBasicThermalEvaporationBlock;
import morethermalevaporation.tile.multiblock.TileEntityBasicThermalEvaporationController;
import morethermalevaporation.tile.multiblock.TileEntityBasicThermalEvaporationValve;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class MoreThermalEvaporationBlocks {

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(MoreThermalEvaporation.MODID);
    // Tin
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityBasicThermalEvaporationBlock>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityBasicThermalEvaporationBlock>>> BASIC_THERMAL_EVAPORATION_BLOCK = registerBlock("basic_thermal_evaporation_block", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.BASIC_THERMAL_EVAPORATION_BLOCK, properties -> properties.mapColor(BlockResourceInfo.TIN.getMapColor())));
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityBasicThermalEvaporationValve>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityBasicThermalEvaporationValve>>> BASIC_THERMAL_EVAPORATION_VALVE = registerBlock("basic_thermal_evaporation_valve", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.BASIC_THERMAL_EVAPORATION_VALVE, properties -> properties.mapColor(BlockResourceInfo.TIN.getMapColor())));
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityBasicThermalEvaporationController>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityBasicThermalEvaporationController>>> BASIC_THERMAL_EVAPORATION_CONTROLLER = registerBlock("basic_thermal_evaporation_controller", () -> new BlockBasicMultiblock<>(MoreThermalEvaporationBlockTypes.BASIC_THERMAL_EVAPORATION_CONTROLLER, properties -> properties.mapColor(BlockResourceInfo.TIN.getMapColor())));

    private MoreThermalEvaporationBlocks() {
    }
    // Osmium
    // Steel
    // Bronze
    // RefinedObsidian

    private static <BLOCK extends Block & IHasDescription> BlockRegistryObject<BLOCK, ItemBlockTooltip<BLOCK>> registerBlock(String name,
                                                                                                                             Supplier<? extends BLOCK> blockSupplier) {
        return BLOCKS.registerDefaultProperties(name, blockSupplier, ItemBlockTooltip::new);
    }

    private static <BLOCK extends Block & IHasDescription> BlockRegistryObject<BLOCK, ItemBlockTooltip<BLOCK>> registerBlock(String name,
                                                                                                                             Supplier<? extends BLOCK> blockSupplier, Rarity rarity) {
        return BLOCKS.registerDefaultProperties(name, blockSupplier, (block, props) -> new ItemBlockTooltip<>(block, props.rarity(rarity)));
    }
}
