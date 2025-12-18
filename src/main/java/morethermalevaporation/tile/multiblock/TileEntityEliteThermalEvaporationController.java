package morethermalevaporation.tile.multiblock;

import mekanism.api.text.TextComponentUtil;
import mekanism.api.tier.BaseTier;
import morethermalevaporation.common.evaporation.EliteThermalEvaporationMultiblockData;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TileEntityEliteThermalEvaporationController extends TileEntityEliteThermalEvaporationBlock {

    public TileEntityEliteThermalEvaporationController(BlockPos pos, BlockState state) {
        super(MoreThermalEvaporationBlocks.ELITE_THERMAL_EVAPORATION_CONTROLLER, pos, state);
        delaySupplier = NO_DELAY;
    }

    @Override
    protected boolean onUpdateServer(EliteThermalEvaporationMultiblockData multiblock) {
        boolean needsPacket = super.onUpdateServer(multiblock);
        setActive(multiblock.isFormed());
        return needsPacket;
    }

    @Override
    public boolean canBeMaster() {
        return true;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return TextComponentUtil.color(super.getDisplayName().copy(), BaseTier.ELITE.getColor().getValue());
    }
}