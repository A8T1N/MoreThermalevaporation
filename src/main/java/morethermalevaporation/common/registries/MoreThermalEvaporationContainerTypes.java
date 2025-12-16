package morethermalevaporation.common.registries;

import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.tile.multiblock.TileEntityAdvancedThermalEvaporationController;
import morethermalevaporation.tile.multiblock.TileEntityBasicThermalEvaporationController;
import morethermalevaporation.tile.multiblock.TileEntityEliteThermalEvaporationController;
import morethermalevaporation.tile.multiblock.TileEntityUltimateThermalEvaporationController;

public class MoreThermalEvaporationContainerTypes {
    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(MoreThermalEvaporation.MODID);

    // Basic
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityBasicThermalEvaporationController>> BASIC_THERMAL_EVAPORATION_CONTROLLER = CONTAINER_TYPES.register(MoreThermalEvaporationBlocks.BASIC_THERMAL_EVAPORATION_CONTROLLER, TileEntityBasicThermalEvaporationController.class);

    // Advanced
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityAdvancedThermalEvaporationController>> ADVANCED_THERMAL_EVAPORATION_CONTROLLER = CONTAINER_TYPES.register(MoreThermalEvaporationBlocks.ADVANCED_THERMAL_EVAPORATION_CONTROLLER, TileEntityAdvancedThermalEvaporationController.class);

    // Elite
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityEliteThermalEvaporationController>> ELITE_THERMAL_EVAPORATION_CONTROLLER = CONTAINER_TYPES.register(MoreThermalEvaporationBlocks.ELITE_THERMAL_EVAPORATION_CONTROLLER, TileEntityEliteThermalEvaporationController.class);

    // Ultimate
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityUltimateThermalEvaporationController>> ULTIMATE_THERMAL_EVAPORATION_CONTROLLER = CONTAINER_TYPES.register(MoreThermalEvaporationBlocks.ULTIMATE_THERMAL_EVAPORATION_CONTROLLER, TileEntityUltimateThermalEvaporationController.class);

    private MoreThermalEvaporationContainerTypes() {

    }
}
