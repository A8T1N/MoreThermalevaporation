package morethermalevaporation.common.registries;

import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.tile.multiblock.TileEntityBasicThermalEvaporationController;

public class MoreThermalEvaporationContainerTypes {
    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(MoreThermalEvaporation.MODID);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityBasicThermalEvaporationController>> BASIC_THERMAL_EVAPORATION_CONTROLLER = CONTAINER_TYPES.register(MoreThermalEvaporationBlocks.BASIC_THERMAL_EVAPORATION_CONTROLLER, TileEntityBasicThermalEvaporationController.class);

    private MoreThermalEvaporationContainerTypes() {

    }
}
