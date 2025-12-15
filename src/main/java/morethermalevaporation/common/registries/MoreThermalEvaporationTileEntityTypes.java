package morethermalevaporation.common.registries;

import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.tile.multiblock.TileEntityBasicThermalEvaporationBlock;
import morethermalevaporation.tile.multiblock.TileEntityBasicThermalEvaporationController;
import morethermalevaporation.tile.multiblock.TileEntityBasicThermalEvaporationValve;

public class MoreThermalEvaporationTileEntityTypes {
    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(MoreThermalEvaporation.MODID);

    // Basic
    // Block
    public static final TileEntityTypeRegistryObject<TileEntityBasicThermalEvaporationBlock> BASIC_THERMAL_EVAPORATION_BLOCK = TILE_ENTITY_TYPES.register(MoreThermalEvaporationBlocks.BASIC_THERMAL_EVAPORATION_BLOCK, TileEntityBasicThermalEvaporationBlock::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    // Valve
    public static final TileEntityTypeRegistryObject<TileEntityBasicThermalEvaporationValve> BASIC_THERMAL_EVAPORATION_VALVE = TILE_ENTITY_TYPES.register(MoreThermalEvaporationBlocks.BASIC_THERMAL_EVAPORATION_VALVE, TileEntityBasicThermalEvaporationValve::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    // Controller
    public static final TileEntityTypeRegistryObject<TileEntityBasicThermalEvaporationController> BASIC_THERMAL_EVAPORATION_CONTROLLER = TILE_ENTITY_TYPES.register(MoreThermalEvaporationBlocks.BASIC_THERMAL_EVAPORATION_CONTROLLER, TileEntityBasicThermalEvaporationController::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
}
