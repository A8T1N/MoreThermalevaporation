package morethermalevaporation.common.tile.multiblock;

import mekanism.common.block.attribute.Attribute;
import mekanism.common.lib.multiblock.MultiblockManager;
import mekanism.common.tile.prefab.TileEntityMultiblock;
import mekanism.common.util.WorldUtils;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.common.content.evaporation.MoreThermalEvaporationMultiblockData;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityMoreThermalEvaporationBlock extends TileEntityMultiblock<MoreThermalEvaporationMultiblockData> {

    private MoreThermalEvaporationTier tier;
    private static final int DEFAULT_HEIGHT = 18;

    public TileEntityMoreThermalEvaporationBlock(MoreThermalEvaporationTier tier, BlockPos pos, BlockState state) {
        this(MoreThermalEvaporationBlocks.BLOCKS.get(tier), pos, state);
    }

    public TileEntityMoreThermalEvaporationBlock(Holder<Block> provider, BlockPos pos, BlockState state) {
        super(provider, pos, state);
    }

    @Override
    public void onNeighborChange(Block block, BlockPos neighborPos) {
        super.onNeighborChange(block, neighborPos);
        if (!isRemote() && WorldUtils.sideDifference(worldPosition, neighborPos) == Direction.DOWN) {
            MoreThermalEvaporationMultiblockData multiblock = getMultiblock();
            if (multiblock.isFormed()) {
                multiblock.updateSolarSpot(getLevel(), neighborPos);
            }
        }
    }

    @Override
    public MoreThermalEvaporationMultiblockData createMultiblock() {
        return new MoreThermalEvaporationMultiblockData(this, this.tier);
    }

    @Override
    public MultiblockManager<MoreThermalEvaporationMultiblockData> getManager() {
        return MoreThermalEvaporation.MoreThermalEvaporationManagers.get(this.tier);
    }

    @Override
    public boolean canBeMaster() {
        return false;
    }

    @Override
    protected void presetVariables() {
        super.presetVariables();
        this.tier = Attribute.getTier(getBlockHolder(), MoreThermalEvaporationTier.class);
    }

    public MoreThermalEvaporationTier getTier() {
        return tier;
    }

    public int getAllowedHeight() {
        return DEFAULT_HEIGHT;
    }

}