package morethermalevaporation.common.registries;

import mekanism.common.capabilities.Capabilities;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import morethermalevaporation.common.tile.machine.TileEntityMoreThermalEvaporationCompact;
import morethermalevaporation.common.tile.multiblock.TileEntityMoreThermalEvaporationBlock;
import morethermalevaporation.common.tile.multiblock.TileEntityMoreThermalEvaporationController;
import morethermalevaporation.common.tile.multiblock.TileEntityMoreThermalEvaporationValve;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumMap;

public class MoreThermalEvaporationTileEntityTypes {

    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(MoreThermalEvaporation.MODID);
    public static final EnumMap<MoreThermalEvaporationTier, TileEntityTypeRegistryObject<TileEntityMoreThermalEvaporationBlock>> BLOCKS = new EnumMap<>(MoreThermalEvaporationTier.class);
    public static final EnumMap<MoreThermalEvaporationTier, TileEntityTypeRegistryObject<TileEntityMoreThermalEvaporationValve>> VALVES = new EnumMap<>(MoreThermalEvaporationTier.class);
    public static final EnumMap<MoreThermalEvaporationTier, TileEntityTypeRegistryObject<TileEntityMoreThermalEvaporationController>> CONTROLLERS = new EnumMap<>(MoreThermalEvaporationTier.class);
    public static final EnumMap<MoreThermalEvaporationTier, TileEntityTypeRegistryObject<TileEntityMoreThermalEvaporationCompact>> COMPACTS = new EnumMap<>(MoreThermalEvaporationTier.class);

    static {
        for (MoreThermalEvaporationTier tier : MoreThermalEvaporationTier.values()) {

            BLOCKS.put(
                    tier,
                    TILE_ENTITY_TYPES
                            .mekBuilder(
                                    MoreThermalEvaporationBlocks.BLOCKS.get(tier),
                                    (BlockPos pos, BlockState state) ->
                                            new TileEntityMoreThermalEvaporationBlock(tier, pos, state)
                            )
                            .clientTicker(TileEntityMekanism::tickClient)
                            .serverTicker(TileEntityMekanism::tickServer)
                            .withSimple(Capabilities.CONFIGURABLE)
                            .build()
            );

            VALVES.put(
                    tier,
                    TILE_ENTITY_TYPES
                            .mekBuilder(
                                    MoreThermalEvaporationBlocks.VALVES.get(tier),
                                    (BlockPos pos, BlockState state) ->
                                            new TileEntityMoreThermalEvaporationValve(tier, pos, state)
                            )
                            .clientTicker(TileEntityMekanism::tickClient)
                            .serverTicker(TileEntityMekanism::tickServer)
                            .withSimple(Capabilities.CONFIGURABLE)
                            .build()
            );

            CONTROLLERS.put(
                    tier,
                    TILE_ENTITY_TYPES
                            .mekBuilder(
                                    MoreThermalEvaporationBlocks.CONTROLLERS.get(tier),
                                    (BlockPos pos, BlockState state) ->
                                            new TileEntityMoreThermalEvaporationController(tier, pos, state)
                            )
                            .clientTicker(TileEntityMekanism::tickClient)
                            .serverTicker(TileEntityMekanism::tickServer)
                            .withSimple(Capabilities.CONFIGURABLE)
                            .build()
            );

            COMPACTS.put(
                    tier,
                    TILE_ENTITY_TYPES
                            .mekBuilder(
                                    MoreThermalEvaporationBlocks.COMPACTS.get(tier),
                                    (BlockPos pos, BlockState state) ->
                                            new TileEntityMoreThermalEvaporationCompact(tier, pos, state)
                            )
                            .clientTicker(TileEntityMekanism::tickClient)
                            .serverTicker(TileEntityMekanism::tickServer)
                            .build()
            );
        }
    }

    private MoreThermalEvaporationTileEntityTypes() {
    }
}