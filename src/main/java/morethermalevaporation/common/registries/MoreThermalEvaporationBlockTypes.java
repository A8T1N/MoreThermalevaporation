package morethermalevaporation.common.registries;

import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.AttributeStateFacing;
import mekanism.common.block.attribute.Attributes;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.BlockTypeTile.BlockTileBuilder;
import morethermalevaporation.tile.multiblock.TileEntityBasicThermalEvaporationBlock;
import morethermalevaporation.tile.multiblock.TileEntityBasicThermalEvaporationController;
import morethermalevaporation.tile.multiblock.TileEntityBasicThermalEvaporationValve;

public class MoreThermalEvaporationBlockTypes {
    // Tin
    // Block
    public static final BlockTypeTile<TileEntityBasicThermalEvaporationBlock> BASIC_THERMAL_EVAPORATION_BLOCK = BlockTileBuilder.createBlock(() -> MoreThermalEvaporationTileEntityTypes.BASIC_THERMAL_EVAPORATION_BLOCK, MekanismLang.DESCRIPTION_THERMAL_EVAPORATION_BLOCK).with(new Attributes.AttributeCustomResistance(9)).externalMultiblock().build();
    // Valve
    public static final BlockTypeTile<TileEntityBasicThermalEvaporationValve> BASIC_THERMAL_EVAPORATION_VALVE = BlockTileBuilder.createBlock(() -> MoreThermalEvaporationTileEntityTypes.BASIC_THERMAL_EVAPORATION_VALVE, MekanismLang.DESCRIPTION_THERMAL_EVAPORATION_VALVE).with(Attributes.COMPARATOR, new Attributes.AttributeCustomResistance(9)).externalMultiblock().withComputerSupport("BasicThermalEvaporationValve").build();
    // Controller
    public static final BlockTypeTile<TileEntityBasicThermalEvaporationController> BASIC_THERMAL_EVAPORATION_CONTROLLER = BlockTileBuilder.createBlock(() -> MoreThermalEvaporationTileEntityTypes.BASIC_THERMAL_EVAPORATION_CONTROLLER, MekanismLang.DESCRIPTION_THERMAL_EVAPORATION_CONTROLLER).withGui(() -> MoreThermalEvaporationContainerTypes.BASIC_THERMAL_EVAPORATION_CONTROLLER, MekanismLang.EVAPORATION_PLANT).with(Attributes.INVENTORY, Attributes.ACTIVE, new AttributeStateFacing(), new Attributes.AttributeCustomResistance(9)).externalMultiblock().withComputerSupport("BasicThermalEvaporationController").build();

    private MoreThermalEvaporationBlockTypes() {
    }

    // Controller
    // Osmium
    // Steel
    // Bronze
    // RefinedObsidian
}
