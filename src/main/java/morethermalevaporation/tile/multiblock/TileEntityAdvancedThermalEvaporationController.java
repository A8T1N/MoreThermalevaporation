package morethermalevaporation.tile.multiblock;

import mekanism.api.text.TextComponentUtil;
import mekanism.api.tier.BaseTier;
import morethermalevaporation.common.evaporation.AdvancedThermalEvaporationMultiblockData;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TileEntityAdvancedThermalEvaporationController extends TileEntityAdvancedThermalEvaporationBlock {

    public TileEntityAdvancedThermalEvaporationController(BlockPos pos, BlockState state) {
        super(MoreThermalEvaporationBlocks.ADVANCED_THERMAL_EVAPORATION_CONTROLLER, pos, state);
        delaySupplier = NO_DELAY;
    }

    @Override
    protected boolean onUpdateServer(AdvancedThermalEvaporationMultiblockData multiblock) {
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
        return TextComponentUtil.color(super.getDisplayName().copy(), BaseTier.ADVANCED.getColor().getValue());
    }
}