package morethermalevaporation.common.item.block;

import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.item.block.ItemBlockTooltip;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class ItemBlockMoreThermalEvaporationCompact extends ItemBlockTooltip<BlockTile<?, ?>> {

    public ItemBlockMoreThermalEvaporationCompact(BlockTile<?, ?> block, Item.Properties properties) {
        super(block, properties);
    }

    @NotNull
    @Override
    public MoreThermalEvaporationTier getTier() {
        return Attribute.getTier(getBlock(), MoreThermalEvaporationTier.class);
    }
}