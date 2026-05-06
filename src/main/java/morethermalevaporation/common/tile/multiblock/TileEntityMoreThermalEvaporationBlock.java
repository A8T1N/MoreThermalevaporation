package morethermalevaporation.common.tile.multiblock;

import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.lib.multiblock.MultiblockManager;
import mekanism.common.tile.prefab.TileEntityMultiblock;
import mekanism.common.util.WorldUtils;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.common.content.evaporation.MoreThermalEvaporationMultiblockData;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import morethermalevaporation.common.upgrade.MoreThermalEvaporationUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import static morethermalevaporation.common.tile.machine.TileEntityMoreThermalEvaporationCompact.MAX_HEIGHT;

public class TileEntityMoreThermalEvaporationBlock extends TileEntityMultiblock<MoreThermalEvaporationMultiblockData> {

    public MoreThermalEvaporationTier tier;
    private int allowedHeight = MAX_HEIGHT;

    public TileEntityMoreThermalEvaporationBlock(MoreThermalEvaporationTier tier, BlockPos pos, BlockState state) {
        this(MoreThermalEvaporationBlocks.BLOCKS.get(tier), pos, state);
    }

    public TileEntityMoreThermalEvaporationBlock(IBlockProvider provider, BlockPos pos, BlockState state) {
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
        this.tier = Attribute.getTier(getBlockType(), MoreThermalEvaporationTier.class);
    }

    public MoreThermalEvaporationTier getTier() {
        return this.tier;
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        // 構造条件（高さ制限など）に影響するアップグレード適用時は、許容高さの設定及び構造の再チェックを要求する
        // TODO : recheckStructure時にGUI表示を維持する方法を探す
        if (upgrade == MoreThermalEvaporationUpgrade.STRUCTURE) {
            this.allowedHeight = this.tier.getBaseHeight() + upgradeComponent.getUpgrades(MoreThermalEvaporationUpgrade.STRUCTURE);
            getMultiblock().recheckStructure = true;
        }
    }

    public int getAllowedHeight() {
        return allowedHeight;
    }

}