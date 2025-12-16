package morethermalevaporation.common.registries;

import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.tile.multiblock.*;

public class MoreThermalEvaporationTileEntityTypes {
    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(MoreThermalEvaporation.MODID);

    // Basic
    public static final TileEntityTypeRegistryObject<TileEntityBasicThermalEvaporationBlock> BASIC_THERMAL_EVAPORATION_BLOCK = TILE_ENTITY_TYPES.register(MoreThermalEvaporationBlocks.BASIC_THERMAL_EVAPORATION_BLOCK, TileEntityBasicThermalEvaporationBlock::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    public static final TileEntityTypeRegistryObject<TileEntityBasicThermalEvaporationValve> BASIC_THERMAL_EVAPORATION_VALVE = TILE_ENTITY_TYPES.register(MoreThermalEvaporationBlocks.BASIC_THERMAL_EVAPORATION_VALVE, TileEntityBasicThermalEvaporationValve::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    public static final TileEntityTypeRegistryObject<TileEntityBasicThermalEvaporationController> BASIC_THERMAL_EVAPORATION_CONTROLLER = TILE_ENTITY_TYPES.register(MoreThermalEvaporationBlocks.BASIC_THERMAL_EVAPORATION_CONTROLLER, TileEntityBasicThermalEvaporationController::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);

    // Advanced
    public static final TileEntityTypeRegistryObject<TileEntityAdvancedThermalEvaporationBlock> ADVANCED_THERMAL_EVAPORATION_BLOCK = TILE_ENTITY_TYPES.register(MoreThermalEvaporationBlocks.ADVANCED_THERMAL_EVAPORATION_BLOCK, TileEntityAdvancedThermalEvaporationBlock::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    public static final TileEntityTypeRegistryObject<TileEntityAdvancedThermalEvaporationValve> ADVANCED_THERMAL_EVAPORATION_VALVE = TILE_ENTITY_TYPES.register(MoreThermalEvaporationBlocks.ADVANCED_THERMAL_EVAPORATION_VALVE, TileEntityAdvancedThermalEvaporationValve::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    public static final TileEntityTypeRegistryObject<TileEntityAdvancedThermalEvaporationController> ADVANCED_THERMAL_EVAPORATION_CONTROLLER = TILE_ENTITY_TYPES.register(MoreThermalEvaporationBlocks.ADVANCED_THERMAL_EVAPORATION_CONTROLLER, TileEntityAdvancedThermalEvaporationController::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);

    // Elite
    public static final TileEntityTypeRegistryObject<TileEntityEliteThermalEvaporationBlock> ELITE_THERMAL_EVAPORATION_BLOCK = TILE_ENTITY_TYPES.register(MoreThermalEvaporationBlocks.ELITE_THERMAL_EVAPORATION_BLOCK, TileEntityEliteThermalEvaporationBlock::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    public static final TileEntityTypeRegistryObject<TileEntityEliteThermalEvaporationValve> ELITE_THERMAL_EVAPORATION_VALVE = TILE_ENTITY_TYPES.register(MoreThermalEvaporationBlocks.ELITE_THERMAL_EVAPORATION_VALVE, TileEntityEliteThermalEvaporationValve::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    public static final TileEntityTypeRegistryObject<TileEntityEliteThermalEvaporationController> ELITE_THERMAL_EVAPORATION_CONTROLLER = TILE_ENTITY_TYPES.register(MoreThermalEvaporationBlocks.ELITE_THERMAL_EVAPORATION_CONTROLLER, TileEntityEliteThermalEvaporationController::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);

    // Ultimate
    public static final TileEntityTypeRegistryObject<TileEntityUltimateThermalEvaporationBlock> ULTIMATE_THERMAL_EVAPORATION_BLOCK = TILE_ENTITY_TYPES.register(MoreThermalEvaporationBlocks.ULTIMATE_THERMAL_EVAPORATION_BLOCK, TileEntityUltimateThermalEvaporationBlock::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    public static final TileEntityTypeRegistryObject<TileEntityUltimateThermalEvaporationValve> ULTIMATE_THERMAL_EVAPORATION_VALVE = TILE_ENTITY_TYPES.register(MoreThermalEvaporationBlocks.ULTIMATE_THERMAL_EVAPORATION_VALVE, TileEntityUltimateThermalEvaporationValve::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    public static final TileEntityTypeRegistryObject<TileEntityUltimateThermalEvaporationController> ULTIMATE_THERMAL_EVAPORATION_CONTROLLER = TILE_ENTITY_TYPES.register(MoreThermalEvaporationBlocks.ULTIMATE_THERMAL_EVAPORATION_CONTROLLER, TileEntityUltimateThermalEvaporationController::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);

}

