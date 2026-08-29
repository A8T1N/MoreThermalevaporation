package morethermalevaporation.common.registries;

import mekanism.common.block.prefab.BlockBasicMultiblock;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.common.content.blocktype.MoreThermalEvaporationMachine;
import morethermalevaporation.common.item.block.ItemBlockMoreThermalEvaporation;
import morethermalevaporation.common.item.block.ItemBlockMoreThermalEvaporationCompact;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import morethermalevaporation.common.tile.machine.TileEntityMoreThermalEvaporationCompact;
import morethermalevaporation.common.tile.multiblock.TileEntityMoreThermalEvaporationBlock;
import morethermalevaporation.common.tile.multiblock.TileEntityMoreThermalEvaporationController;
import morethermalevaporation.common.tile.multiblock.TileEntityMoreThermalEvaporationValve;
import net.minecraft.world.level.material.MapColor;

import java.util.EnumMap;

public class MoreThermalEvaporationBlocks {

    public static final BlockDeferredRegister REGISTRY_BLOCKS = new BlockDeferredRegister(MoreThermalEvaporation.MODID);

    public static final EnumMap<MoreThermalEvaporationTier, BlockRegistryObject<BlockBasicMultiblock<TileEntityMoreThermalEvaporationBlock>, ItemBlockMoreThermalEvaporation>> BLOCKS = new EnumMap<>(MoreThermalEvaporationTier.class);
    public static final EnumMap<MoreThermalEvaporationTier, BlockRegistryObject<BlockBasicMultiblock<TileEntityMoreThermalEvaporationValve>, ItemBlockMoreThermalEvaporation>> VALVES = new EnumMap<>(MoreThermalEvaporationTier.class);
    public static final EnumMap<MoreThermalEvaporationTier, BlockRegistryObject<BlockBasicMultiblock<TileEntityMoreThermalEvaporationController>, ItemBlockMoreThermalEvaporation>> CONTROLLERS = new EnumMap<>(MoreThermalEvaporationTier.class);
    public static final EnumMap<MoreThermalEvaporationTier, BlockRegistryObject<BlockTileModel<TileEntityMoreThermalEvaporationCompact, MoreThermalEvaporationMachine<TileEntityMoreThermalEvaporationCompact>>, ItemBlockMoreThermalEvaporationCompact>> COMPACTS = new EnumMap<>(MoreThermalEvaporationTier.class);


    static {
        for (MoreThermalEvaporationTier tier : MoreThermalEvaporationTier.values()) {
            BLOCKS.put(tier, registerMoreThermalEvaporationPart(tier, "block", MoreThermalEvaporationBlockTypes.BLOCKS.get(tier)));
            VALVES.put(tier, registerMoreThermalEvaporationPart(tier, "valve", MoreThermalEvaporationBlockTypes.VALVES.get(tier)));
            CONTROLLERS.put(tier, registerMoreThermalEvaporationPart(tier, "controller", MoreThermalEvaporationBlockTypes.CONTROLLERS.get(tier)));
            COMPACTS.put(tier, registerMoreThermalEvaporationCompact(tier, MoreThermalEvaporationBlockTypes.COMPACTS.get(tier)));
        }
    }

    private MoreThermalEvaporationBlocks() {
    }

    private static <TILE extends TileEntityMekanism> BlockRegistryObject<BlockBasicMultiblock<TILE>, ItemBlockMoreThermalEvaporation> registerMoreThermalEvaporationPart(MoreThermalEvaporationTier tier, String suffix, BlockTypeTile<TILE> type) {

        String name = tier.getBaseTier().getLowerName() + "_thermal_evaporation_" + suffix;
        MapColor color = tier.getBaseTier().getMapColor();

        return REGISTRY_BLOCKS.register(name, () -> new BlockBasicMultiblock<>(type, properties -> properties.mapColor(color)), ItemBlockMoreThermalEvaporation::new);
    }

    private static <TILE extends TileEntityMekanism> BlockRegistryObject<BlockTileModel<TILE, MoreThermalEvaporationMachine<TILE>>, ItemBlockMoreThermalEvaporationCompact> registerMoreThermalEvaporationCompact(MoreThermalEvaporationTier tier, MoreThermalEvaporationMachine<TILE> type) {

        String name = tier.getBaseTier().getLowerName() + "_thermal_evaporation_compact";
        MapColor color = tier.getBaseTier().getMapColor();

        return REGISTRY_BLOCKS.register(name, () -> new BlockTileModel<>(type, properties -> properties.mapColor(color).noOcclusion()), ItemBlockMoreThermalEvaporationCompact::new);
    }

}
