package morethermalevaporation.common.registries;

import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import morethermalevaporation.common.tile.machine.TileEntityMoreThermalEvaporationCompact;
import morethermalevaporation.common.tile.multiblock.TileEntityMoreThermalEvaporationController;

import java.util.EnumMap;

public class MoreThermalEvaporationContainerTypes {
    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(MoreThermalEvaporation.MODID);

    public static final EnumMap<MoreThermalEvaporationTier, ContainerTypeRegistryObject<MekanismTileContainer<TileEntityMoreThermalEvaporationController>>> MORE_THERMAL_EVAPORATION_CONTROLLER = new EnumMap<>(MoreThermalEvaporationTier.class);
    public static final EnumMap<MoreThermalEvaporationTier, ContainerTypeRegistryObject<MekanismTileContainer<TileEntityMoreThermalEvaporationCompact>>> MORE_THERMAL_EVAPORATION_COMPACT = new EnumMap<>(MoreThermalEvaporationTier.class);

    static {
        for (MoreThermalEvaporationTier tier : MoreThermalEvaporationTier.values()) {
            MORE_THERMAL_EVAPORATION_CONTROLLER.put(tier, CONTAINER_TYPES.register(MoreThermalEvaporationBlocks.CONTROLLERS.get(tier), TileEntityMoreThermalEvaporationController.class));
            MORE_THERMAL_EVAPORATION_COMPACT.put(tier, CONTAINER_TYPES.register(MoreThermalEvaporationBlocks.COMPACTS.get(tier), TileEntityMoreThermalEvaporationCompact.class));
        }
    }

    private MoreThermalEvaporationContainerTypes() {
    }

}
