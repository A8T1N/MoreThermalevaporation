package morethermalevaporation.common.item.block;

import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.prefab.BlockBasicMultiblock;
import mekanism.common.item.block.ItemBlockTooltip;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class ItemBlockMoreThermalEvaporation extends ItemBlockTooltip<BlockBasicMultiblock<?>> {

    public ItemBlockMoreThermalEvaporation(BlockBasicMultiblock<?> block, Item.Properties properties) {
        super(block, properties);
    }

    @NotNull
    @Override
    public MoreThermalEvaporationTier getTier() {
        return Attribute.getTier(getBlock(), MoreThermalEvaporationTier.class);
    }
}