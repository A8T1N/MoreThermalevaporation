package morethermalevaporation.common.registries;

import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.AttributeStateFacing;
import mekanism.common.block.attribute.Attributes;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.BlockTypeTile.BlockTileBuilder;
import morethermalevaporation.tile.multiblock.*;

public class MoreThermalEvaporationBlockTypes {
    // Basic
    public static final BlockTypeTile<TileEntityBasicThermalEvaporationBlock> BASIC_THERMAL_EVAPORATION_BLOCK = BlockTileBuilder.createBlock(() -> MoreThermalEvaporationTileEntityTypes.BASIC_THERMAL_EVAPORATION_BLOCK, MekanismLang.DESCRIPTION_THERMAL_EVAPORATION_BLOCK).with(new Attributes.AttributeCustomResistance(9)).externalMultiblock().build();
    public static final BlockTypeTile<TileEntityBasicThermalEvaporationValve> BASIC_THERMAL_EVAPORATION_VALVE = BlockTileBuilder.createBlock(() -> MoreThermalEvaporationTileEntityTypes.BASIC_THERMAL_EVAPORATION_VALVE, MekanismLang.DESCRIPTION_THERMAL_EVAPORATION_VALVE).with(Attributes.COMPARATOR, new Attributes.AttributeCustomResistance(9)).externalMultiblock().withComputerSupport("BasicThermalEvaporationValve").build();
    public static final BlockTypeTile<TileEntityBasicThermalEvaporationController> BASIC_THERMAL_EVAPORATION_CONTROLLER = BlockTileBuilder.createBlock(() -> MoreThermalEvaporationTileEntityTypes.BASIC_THERMAL_EVAPORATION_CONTROLLER, MekanismLang.DESCRIPTION_THERMAL_EVAPORATION_CONTROLLER).withGui(() -> MoreThermalEvaporationContainerTypes.BASIC_THERMAL_EVAPORATION_CONTROLLER, MekanismLang.EVAPORATION_PLANT).with(Attributes.INVENTORY, Attributes.ACTIVE, new AttributeStateFacing(), new Attributes.AttributeCustomResistance(9)).externalMultiblock().withComputerSupport("BasicThermalEvaporationController").build();

    // Advanced
    public static final BlockTypeTile<TileEntityAdvancedThermalEvaporationBlock> ADVANCED_THERMAL_EVAPORATION_BLOCK = BlockTileBuilder.createBlock(() -> MoreThermalEvaporationTileEntityTypes.ADVANCED_THERMAL_EVAPORATION_BLOCK, MekanismLang.DESCRIPTION_THERMAL_EVAPORATION_BLOCK).with(new Attributes.AttributeCustomResistance(9)).externalMultiblock().build();
    public static final BlockTypeTile<TileEntityAdvancedThermalEvaporationValve> ADVANCED_THERMAL_EVAPORATION_VALVE = BlockTileBuilder.createBlock(() -> MoreThermalEvaporationTileEntityTypes.ADVANCED_THERMAL_EVAPORATION_VALVE, MekanismLang.DESCRIPTION_THERMAL_EVAPORATION_VALVE).with(Attributes.COMPARATOR, new Attributes.AttributeCustomResistance(9)).externalMultiblock().withComputerSupport("AdvancedThermalEvaporationValve").build();
    public static final BlockTypeTile<TileEntityAdvancedThermalEvaporationController> ADVANCED_THERMAL_EVAPORATION_CONTROLLER = BlockTileBuilder.createBlock(() -> MoreThermalEvaporationTileEntityTypes.ADVANCED_THERMAL_EVAPORATION_CONTROLLER, MekanismLang.DESCRIPTION_THERMAL_EVAPORATION_CONTROLLER).withGui(() -> MoreThermalEvaporationContainerTypes.ADVANCED_THERMAL_EVAPORATION_CONTROLLER, MekanismLang.EVAPORATION_PLANT).with(Attributes.INVENTORY, Attributes.ACTIVE, new AttributeStateFacing(), new Attributes.AttributeCustomResistance(9)).externalMultiblock().withComputerSupport("AdvancedThermalEvaporationController").build();

    // Elite
    public static final BlockTypeTile<TileEntityEliteThermalEvaporationBlock> ELITE_THERMAL_EVAPORATION_BLOCK = BlockTileBuilder.createBlock(() -> MoreThermalEvaporationTileEntityTypes.ELITE_THERMAL_EVAPORATION_BLOCK, MekanismLang.DESCRIPTION_THERMAL_EVAPORATION_BLOCK).with(new Attributes.AttributeCustomResistance(9)).externalMultiblock().build();
    public static final BlockTypeTile<TileEntityEliteThermalEvaporationValve> ELITE_THERMAL_EVAPORATION_VALVE = BlockTileBuilder.createBlock(() -> MoreThermalEvaporationTileEntityTypes.ELITE_THERMAL_EVAPORATION_VALVE, MekanismLang.DESCRIPTION_THERMAL_EVAPORATION_VALVE).with(Attributes.COMPARATOR, new Attributes.AttributeCustomResistance(9)).externalMultiblock().withComputerSupport("EliteThermalEvaporationValve").build();
    public static final BlockTypeTile<TileEntityEliteThermalEvaporationController> ELITE_THERMAL_EVAPORATION_CONTROLLER = BlockTileBuilder.createBlock(() -> MoreThermalEvaporationTileEntityTypes.ELITE_THERMAL_EVAPORATION_CONTROLLER, MekanismLang.DESCRIPTION_THERMAL_EVAPORATION_CONTROLLER).withGui(() -> MoreThermalEvaporationContainerTypes.ELITE_THERMAL_EVAPORATION_CONTROLLER, MekanismLang.EVAPORATION_PLANT).with(Attributes.INVENTORY, Attributes.ACTIVE, new AttributeStateFacing(), new Attributes.AttributeCustomResistance(9)).externalMultiblock().withComputerSupport("EliteThermalEvaporationController").build();

    // Ultimate
    public static final BlockTypeTile<TileEntityUltimateThermalEvaporationBlock> ULTIMATE_THERMAL_EVAPORATION_BLOCK = BlockTileBuilder.createBlock(() -> MoreThermalEvaporationTileEntityTypes.ULTIMATE_THERMAL_EVAPORATION_BLOCK, MekanismLang.DESCRIPTION_THERMAL_EVAPORATION_BLOCK).with(new Attributes.AttributeCustomResistance(9)).externalMultiblock().build();
    public static final BlockTypeTile<TileEntityUltimateThermalEvaporationValve> ULTIMATE_THERMAL_EVAPORATION_VALVE = BlockTileBuilder.createBlock(() -> MoreThermalEvaporationTileEntityTypes.ULTIMATE_THERMAL_EVAPORATION_VALVE, MekanismLang.DESCRIPTION_THERMAL_EVAPORATION_VALVE).with(Attributes.COMPARATOR, new Attributes.AttributeCustomResistance(9)).externalMultiblock().withComputerSupport("UltimateThermalEvaporationValve").build();
    public static final BlockTypeTile<TileEntityUltimateThermalEvaporationController> ULTIMATE_THERMAL_EVAPORATION_CONTROLLER = BlockTileBuilder.createBlock(() -> MoreThermalEvaporationTileEntityTypes.ULTIMATE_THERMAL_EVAPORATION_CONTROLLER, MekanismLang.DESCRIPTION_THERMAL_EVAPORATION_CONTROLLER).withGui(() -> MoreThermalEvaporationContainerTypes.ULTIMATE_THERMAL_EVAPORATION_CONTROLLER, MekanismLang.EVAPORATION_PLANT).with(Attributes.INVENTORY, Attributes.ACTIVE, new AttributeStateFacing(), new Attributes.AttributeCustomResistance(9)).externalMultiblock().withComputerSupport("UltimateThermalEvaporationController").build();

    private MoreThermalEvaporationBlockTypes() {
    }

    // Controller
    // Osmium
    // Steel
    // Bronze
    // RefinedObsidian
}
