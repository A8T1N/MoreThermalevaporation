package morethermalevaporation.tile.multiblock;

import mekanism.api.providers.IBlockProvider;
import mekanism.api.text.TextComponentUtil;
import mekanism.api.tier.BaseTier;
import mekanism.common.lib.multiblock.MultiblockManager;
import mekanism.common.tile.prefab.TileEntityMultiblock;
import mekanism.common.util.WorldUtils;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.common.evaporation.BasicThermalEvaporationMultiblockData;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TileEntityBasicThermalEvaporationBlock extends TileEntityMultiblock<BasicThermalEvaporationMultiblockData> {

    public TileEntityBasicThermalEvaporationBlock(BlockPos pos, BlockState state) {
        this(MoreThermalEvaporationBlocks.BASIC_THERMAL_EVAPORATION_BLOCK, pos, state);
    }

    public TileEntityBasicThermalEvaporationBlock(IBlockProvider provider, BlockPos pos, BlockState state) {
        super(provider, pos, state);
    }

    @Override
    public void onNeighborChange(Block block, BlockPos neighborPos) {
        super.onNeighborChange(block, neighborPos);
        if (!isRemote() && WorldUtils.sideDifference(worldPosition, neighborPos) == Direction.DOWN) {
            BasicThermalEvaporationMultiblockData multiblock = getMultiblock();
            if (multiblock.isFormed()) {
                multiblock.updateSolarSpot(getLevel(), neighborPos);
            }
        }
    }

    @Override
    public BasicThermalEvaporationMultiblockData createMultiblock() {
        return new BasicThermalEvaporationMultiblockData(this);
    }

    @Override
    public MultiblockManager<BasicThermalEvaporationMultiblockData> getManager() {
        return MoreThermalEvaporation.BasicThermalEvaporationManager;
    }

    @Override
    public boolean canBeMaster() {
        return false;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return TextComponentUtil.color(super.getDisplayName().copy(), BaseTier.BASIC.getColor().getValue());
    }
}