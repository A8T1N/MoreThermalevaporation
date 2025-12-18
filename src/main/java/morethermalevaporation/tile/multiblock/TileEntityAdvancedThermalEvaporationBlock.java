package morethermalevaporation.tile.multiblock;

import mekanism.api.providers.IBlockProvider;
import mekanism.api.text.TextComponentUtil;
import mekanism.api.tier.BaseTier;
import mekanism.common.lib.multiblock.MultiblockManager;
import mekanism.common.tile.prefab.TileEntityMultiblock;
import mekanism.common.util.WorldUtils;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.common.evaporation.AdvancedThermalEvaporationMultiblockData;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TileEntityAdvancedThermalEvaporationBlock extends TileEntityMultiblock<AdvancedThermalEvaporationMultiblockData> {

    public TileEntityAdvancedThermalEvaporationBlock(BlockPos pos, BlockState state) {
        this(MoreThermalEvaporationBlocks.ADVANCED_THERMAL_EVAPORATION_BLOCK, pos, state);
    }

    public TileEntityAdvancedThermalEvaporationBlock(IBlockProvider provider, BlockPos pos, BlockState state) {
        super(provider, pos, state);
    }

    @Override
    public void onNeighborChange(Block block, BlockPos neighborPos) {
        super.onNeighborChange(block, neighborPos);
        if (!isRemote() && WorldUtils.sideDifference(worldPosition, neighborPos) == Direction.DOWN) {
            AdvancedThermalEvaporationMultiblockData multiblock = getMultiblock();
            if (multiblock.isFormed()) {
                multiblock.updateSolarSpot(getLevel(), neighborPos);
            }
        }
    }

    @Override
    public AdvancedThermalEvaporationMultiblockData createMultiblock() {
        return new AdvancedThermalEvaporationMultiblockData(this);
    }

    @Override
    public MultiblockManager<AdvancedThermalEvaporationMultiblockData> getManager() {
        return MoreThermalEvaporation.AdvancedThermalEvaporationManager;
    }

    @Override
    public boolean canBeMaster() {
        return false;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return TextComponentUtil.color(super.getDisplayName().copy(), BaseTier.ADVANCED.getColor().getValue());
    }
}