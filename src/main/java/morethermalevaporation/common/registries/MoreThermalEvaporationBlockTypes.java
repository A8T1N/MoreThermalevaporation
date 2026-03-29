package morethermalevaporation.common.registries;

import mekanism.api.Upgrade;
import mekanism.common.block.attribute.AttributeStateFacing;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.block.attribute.Attributes;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.BlockTypeTile.BlockTileBuilder;
import morethermalevaporation.common.MoreThermalEvaporationLang;
import morethermalevaporation.common.content.blocktype.BlockShapes;
import morethermalevaporation.common.content.blocktype.MoreThermalEvaporationMachine;
import morethermalevaporation.common.content.blocktype.MoreThermalEvaporationMachine.MoreThermalEvaporationMachineBuilder;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import morethermalevaporation.common.tile.machine.TileEntityMoreThermalEvaporationCompact;
import morethermalevaporation.common.tile.multiblock.TileEntityMoreThermalEvaporationBlock;
import morethermalevaporation.common.tile.multiblock.TileEntityMoreThermalEvaporationController;
import morethermalevaporation.common.tile.multiblock.TileEntityMoreThermalEvaporationValve;

import java.util.EnumMap;
import java.util.EnumSet;

public class MoreThermalEvaporationBlockTypes {

    public static final EnumMap<MoreThermalEvaporationTier, BlockTypeTile<TileEntityMoreThermalEvaporationBlock>> BLOCKS = new EnumMap<>(MoreThermalEvaporationTier.class);
    public static final EnumMap<MoreThermalEvaporationTier, BlockTypeTile<TileEntityMoreThermalEvaporationValve>> VALVES = new EnumMap<>(MoreThermalEvaporationTier.class);
    public static final EnumMap<MoreThermalEvaporationTier, BlockTypeTile<TileEntityMoreThermalEvaporationController>> CONTROLLERS = new EnumMap<>(MoreThermalEvaporationTier.class);
    public static final EnumMap<MoreThermalEvaporationTier, MoreThermalEvaporationMachine<TileEntityMoreThermalEvaporationCompact>> COMPACTS = new EnumMap<>(MoreThermalEvaporationTier.class);

    static {
        for (MoreThermalEvaporationTier tier : MoreThermalEvaporationTier.values()) {
            BLOCKS.put(tier, createMoreThermalEvaporationBlock(tier, MoreThermalEvaporationLang.getLangDescriptionBlock(tier)));
            VALVES.put(tier, createMoreThermalEvaporationValve(tier, MoreThermalEvaporationLang.getLangDescriptionValve(tier)));
            CONTROLLERS.put(tier, createMoreThermalEvaporationController(tier, MoreThermalEvaporationLang.getLangDescriptionController(tier)));
            COMPACTS.put(tier, createMoreThermalEvaporationCompact(tier, MoreThermalEvaporationLang.getLangDescriptionCompact(tier)));
        }
    }

    private MoreThermalEvaporationBlockTypes() {
    }

    private static BlockTypeTile<TileEntityMoreThermalEvaporationBlock> createMoreThermalEvaporationBlock(MoreThermalEvaporationTier tier, MoreThermalEvaporationLang lang) {
        return BlockTileBuilder.createBlock(() -> MoreThermalEvaporationTileEntityTypes.BLOCKS.get(tier), lang).
                with(new AttributeTier<>(tier), new Attributes.AttributeCustomResistance(9)).
                externalMultiblock()
                .build();
    }

    private static BlockTypeTile<TileEntityMoreThermalEvaporationValve> createMoreThermalEvaporationValve(MoreThermalEvaporationTier tier, MoreThermalEvaporationLang lang) {
        return BlockTileBuilder.createBlock(() -> MoreThermalEvaporationTileEntityTypes.VALVES.get(tier), lang)
                .with(new AttributeTier<>(tier), Attributes.COMPARATOR, new Attributes.AttributeCustomResistance(9))
                .externalMultiblock()
                .withComputerSupport(tier.getBaseTier().getLowerName() + "ThermalEvaporationValve")
                .build();
    }

    private static BlockTypeTile<TileEntityMoreThermalEvaporationController> createMoreThermalEvaporationController(MoreThermalEvaporationTier tier, MoreThermalEvaporationLang lang) {
        return BlockTileBuilder.createBlock(() -> MoreThermalEvaporationTileEntityTypes.CONTROLLERS.get(tier), lang)
                .withGui(() -> MoreThermalEvaporationContainerTypes.MORE_THERMAL_EVAPORATION_CONTROLLER.get(tier), MoreThermalEvaporationLang.getLangPlant(tier))
                .withSupportedUpgrades(EnumSet.of(Upgrade.ANCHOR))
                .with(new AttributeTier<>(tier), Attributes.INVENTORY, Attributes.ACTIVE, new AttributeStateFacing(), new Attributes.AttributeCustomResistance(9))
                .externalMultiblock()
                .withComputerSupport(tier.getBaseTier().getLowerName() + "ThermalEvaporationController")
                .build();
    }

    private static MoreThermalEvaporationMachine<TileEntityMoreThermalEvaporationCompact> createMoreThermalEvaporationCompact(MoreThermalEvaporationTier tier, MoreThermalEvaporationLang lang) {
        return MoreThermalEvaporationMachineBuilder.createMachine(() -> MoreThermalEvaporationTileEntityTypes.COMPACTS.get(tier), lang, tier)
                .withGui(() -> MoreThermalEvaporationContainerTypes.MORE_THERMAL_EVAPORATION_COMPACT.get(tier), MoreThermalEvaporationLang.getLangCompact(tier))
                .withSupportedUpgrades(EnumSet.of(Upgrade.ANCHOR))
                .with(new AttributeTier<>(tier), Attributes.INVENTORY, Attributes.ACTIVE, new AttributeStateFacing(), new Attributes.AttributeCustomResistance(9))
                .withCustomShape(BlockShapes.MORE_THERMAL_EVAPORATION_COMPACT)
                .withComputerSupport(tier.getBaseTier().getLowerName() + "ThermalEvaporationCompact")
                .build();
    }

}
