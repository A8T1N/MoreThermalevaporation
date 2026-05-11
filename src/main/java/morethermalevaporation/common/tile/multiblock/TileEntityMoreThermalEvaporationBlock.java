package morethermalevaporation.common.tile.multiblock;

import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.lib.multiblock.MultiblockManager;
import mekanism.common.tile.prefab.TileEntityMultiblock;
import mekanism.common.util.UpgradeUtils;
import mekanism.common.util.WorldUtils;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.common.content.evaporation.MoreThermalEvaporationMultiblockData;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import morethermalevaporation.common.upgrade.MoreThermalEvaporationUpgrade;
import morethermalevaporation.common.util.MoreThermalEvaporationUpgradeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TileEntityMoreThermalEvaporationBlock extends TileEntityMultiblock<MoreThermalEvaporationMultiblockData> {

    public MoreThermalEvaporationTier tier;
    private static final int DEFAULT_HEIGHT = 18;
    private int allowedHeight = DEFAULT_HEIGHT;

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
        MoreThermalEvaporationMultiblockData multiblock = getMultiblock();

        // ストラクチャーアップグレード数に応じて許容高さを設定する。
        // 現在の高さが更新後の許容高さを超えている場合はプラントを無効化する。
        if (upgrade == MoreThermalEvaporationUpgrade.STRUCTURE) {
            this.allowedHeight = this.tier.getHeight() + upgradeComponent.getUpgrades(MoreThermalEvaporationUpgrade.STRUCTURE);
            // 動的変更時チェック
            if (multiblock.height() > this.allowedHeight) {
                multiblock.setFormedForce(false);
            }
        }
    }

    @NotNull
    @Override
    public List<Component> getInfo(@NotNull Upgrade upgrade) {
        List<Component> ret = UpgradeUtils.getMultScaledInfo(this, upgrade);
        return MoreThermalEvaporationUpgradeUtils.getMultScaledInfo(ret, this, upgrade);
    }

    public int getAllowedHeight() {
        return this.allowedHeight;
    }

}