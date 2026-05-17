package morethermalevaporation.common.upgrade;

import mekanism.api.NBTConstants;
import mekanism.api.Upgrade;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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
        if (nbtTags != null && nbtTags.contains(MTE_UPGRADE_NBT, Tag.TAG_LIST)) {
            ListTag list = nbtTags.getList(MTE_UPGRADE_NBT, Tag.TAG_COMPOUND);
            for (int tagCount = 0; tagCount < list.size(); tagCount++) {
                CompoundTag compound = list.getCompound(tagCount);
                int installed = Mth.clamp(compound.getInt(NBTConstants.AMOUNT), 0, STRUCTURE.getMax());

                if (installed > 0) {
                    upgrades.put(STRUCTURE, installed);
                }
            }
        }
        return upgrades;
    }

    public static Set<Map.Entry<Upgrade, Integer>> saveMap(Set<Map.Entry<Upgrade, Integer>> upgrades,
                                                           CompoundTag nbtTags) {
        ListTag list = new ListTag();
        for (Map.Entry<Upgrade, Integer> entry : upgrades) {
            if (entry.getKey() == STRUCTURE) {
                list.add(STRUCTURE.getTag(entry.getValue()));
            }
        }
        nbtTags.put(MTE_UPGRADE_NBT, list);
        return upgrades.stream()
                .filter(Objects::nonNull)
                .filter(entry -> entry.getKey() != STRUCTURE)
                .collect(Collectors.toUnmodifiableSet());
    }
}