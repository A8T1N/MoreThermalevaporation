package morethermalevaporation.common.upgrade;

import mekanism.api.Upgrade;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MoreThermalEvaporationUpgrade {

    public static Upgrade STRUCTURE;

    public static final String MTE_UPGRADE_NBT = "mte_upgrades";

    public static Map<Upgrade, Integer> buildMap(Map<Upgrade, Integer> upgrades, CompoundTag nbtTags) {
        if (upgrades == null) {
            upgrades = new HashMap<>();
        }

        int installed = Mth.clamp(nbtTags.getInt(MTE_UPGRADE_NBT), 0, STRUCTURE.getMax());

        if (installed > 0) {
            upgrades.put(STRUCTURE, installed);
        }

        return upgrades;
    }

    public static Set<Map.Entry<Upgrade, Integer>> saveMap(Set<Map.Entry<Upgrade, Integer>> upgrades, CompoundTag nbtTags) {
        for (Map.Entry<Upgrade, Integer> entry : upgrades) {
            if (entry.getKey() == STRUCTURE) {
                nbtTags.putInt(MTE_UPGRADE_NBT, entry.getValue());
            }
        }
        return upgrades.stream()
                .filter(Objects::nonNull)
                .filter(entry -> entry.getKey() != STRUCTURE)
                .collect(Collectors.toUnmodifiableSet());
    }
}