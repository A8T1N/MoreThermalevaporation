package morethermalevaporation.tile.multiblock;

import mekanism.api.providers.IBlockProvider;
import mekanism.common.lib.multiblock.MultiblockManager;
import mekanism.common.tile.prefab.TileEntityMultiblock;
import mekanism.common.util.WorldUtils;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.common.evaporation.UltimateThermalEvaporationMultiblockData;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityUltimateThermalEvaporationBlock extends TileEntityMultiblock<UltimateThermalEvaporationMultiblockData> {

    public TileEntityUltimateThermalEvaporationBlock(BlockPos pos, BlockState state) {
        this(MoreThermalEvaporationBlocks.ULTIMATE_THERMAL_EVAPORATION_BLOCK, pos, state);
    }

    public TileEntityUltimateThermalEvaporationBlock(IBlockProvider provider, BlockPos pos, BlockState state) {
        super(provider, pos, state);
    }

    @Override
    public void onNeighborChange(Block block, BlockPos neighborPos) {
        super.onNeighborChange(block, neighborPos);
        if (!isRemote() && WorldUtils.sideDifference(worldPosition, neighborPos) == Direction.DOWN) {
            UltimateThermalEvaporationMultiblockData multiblock = getMultiblock();
            if (multiblock.isFormed()) {
                multiblock.updateSolarSpot(getLevel(), neighborPos);
            }
        }
    }

    @Override
    public UltimateThermalEvaporationMultiblockData createMultiblock() {
        return new UltimateThermalEvaporationMultiblockData(this);
    }

    @Override
    public MultiblockManager<UltimateThermalEvaporationMultiblockData> getManager() {
        return MoreThermalEvaporation.UltimateThermalEvaporationManager;
    }

    @Override
    public boolean canBeMaster() {
        return false;
    }

}