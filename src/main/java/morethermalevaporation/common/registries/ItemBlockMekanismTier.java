package morethermalevaporation.common.registries;

import mekanism.api.text.TextComponentUtil;
import mekanism.api.tier.BaseTier;
import mekanism.common.block.interfaces.IHasDescription;
import mekanism.common.item.block.ItemBlockTooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class ItemBlockMekanismTier<BLOCK extends Block & IHasDescription> extends ItemBlockTooltip<BLOCK> {

    private final BaseTier tier;

    public ItemBlockMekanismTier(BLOCK block, Properties properties, BaseTier tier) {
        super(block, properties);
        this.tier = tier;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return TextComponentUtil.color(super.getName(stack).copy(), tier.getColor().getValue());
    }
}