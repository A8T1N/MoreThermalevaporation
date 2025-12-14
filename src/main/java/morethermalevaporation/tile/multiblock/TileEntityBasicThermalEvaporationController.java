package morethermalevaporation.tile.multiblock;

import morethermalevaporation.common.evaporation.BasicThermalEvaporationMultiblockData;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityBasicThermalEvaporationController extends TileEntityBasicThermalEvaporationBlock {

    public TileEntityBasicThermalEvaporationController(BlockPos pos, BlockState state) {
        super(MoreThermalEvaporationBlocks.BASIC_THERMAL_EVAPORATION_CONTROLLER, pos, state);
        delaySupplier = NO_DELAY;
    }

    @Override
    protected boolean onUpdateServer(BasicThermalEvaporationMultiblockData multiblock) {
        boolean needsPacket = super.onUpdateServer(multiblock);
        setActive(multiblock.isFormed());
        return needsPacket;
    }

    @Override
    public boolean canBeMaster() {
        return true;
    }
}