package morethermalevaporation.common.registries;

import mekanism.api.Upgrade;
import mekanism.common.item.ItemUpgrade;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.common.upgrade.MoreThermalEvaporationUpgrade;

public class MoreThermalEvaporationItems {

    private MoreThermalEvaporationItems() {
    }

    public static final ItemDeferredRegister REGISTRY_ITEMS = new ItemDeferredRegister(MoreThermalEvaporation.MODID);

    public static final ItemRegistryObject<ItemUpgrade> STRUCTURE_UPGRADE = registerUpgrade(MoreThermalEvaporationUpgrade.STRUCTURE);

    private static ItemRegistryObject<ItemUpgrade> registerUpgrade(Upgrade type) {
        return REGISTRY_ITEMS.register("upgrade_" + type.getRawName(), properties -> new ItemUpgrade(type, properties));
    }
}