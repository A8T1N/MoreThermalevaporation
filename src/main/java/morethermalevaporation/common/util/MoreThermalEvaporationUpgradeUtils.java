package morethermalevaporation.common.util;

import mekanism.api.Upgrade;
import mekanism.common.tile.interfaces.IUpgradeTile;
import morethermalevaporation.common.upgrade.MoreThermalEvaporationUpgrade;
import net.minecraft.network.chat.Component;

import java.util.List;

public class MoreThermalEvaporationUpgradeUtils {

    public static List<Component> getMultScaledInfo(List<Component> ret, IUpgradeTile tile, Upgrade upgrade) {
        if (tile.supportsUpgrades()) {
            if (upgrade == MoreThermalEvaporationUpgrade.STRUCTURE) {
                ret.clear();
            }
        }
        return ret;
    }
}
