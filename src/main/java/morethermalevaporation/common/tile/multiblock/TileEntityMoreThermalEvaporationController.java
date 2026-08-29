package morethermalevaporation.common.tile.multiblock;

import mekanism.common.tile.interfaces.IHasDumpButton;
import morethermalevaporation.common.content.evaporation.MoreThermalEvaporationMultiblockData;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityMoreThermalEvaporationController extends TileEntityMoreThermalEvaporationBlock implements IHasDumpButton {

    public TileEntityMoreThermalEvaporationController(MoreThermalEvaporationTier tier, BlockPos pos, BlockState state) {
        super(MoreThermalEvaporationBlocks.CONTROLLERS.get(tier), pos, state);
        delaySupplier = NO_DELAY;
    }

    @Override
    protected boolean onUpdateServer(MoreThermalEvaporationMultiblockData multiblock) {
        boolean needsPacket = super.onUpdateServer(multiblock);
        setActive(multiblock.isFormed());
        return needsPacket;
    }

    @Override
    public boolean canBeMaster() {
        return true;
    }

    @Override
    public void dump() {
        MoreThermalEvaporationMultiblockData multiblock = getMultiblock();
        multiblock.inputTank.setEmpty();    // InputTank
        multiblock.outputTank.setEmpty();   // OutputTank
    }
}