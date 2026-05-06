package morethermalevaporation.common.content.evaporation;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import mekanism.common.MekanismLang;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.math.voxel.VoxelCuboid.CuboidSide;
import mekanism.common.lib.math.voxel.VoxelCuboid.WallRelative;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.FormationProtocol;
import mekanism.common.lib.multiblock.FormationProtocol.CasingType;
import mekanism.common.lib.multiblock.FormationProtocol.FormationResult;
import mekanism.common.lib.multiblock.FormationProtocol.StructureRequirement;
import mekanism.common.lib.multiblock.StructureHelper;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlockTypes;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import morethermalevaporation.common.tile.multiblock.TileEntityMoreThermalEvaporationController;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.EnumSet;

public class MoreThermalEvaporationValidator extends CuboidStructureValidator<MoreThermalEvaporationMultiblockData> {

    private static final VoxelCuboid MIN_CUBOID = new VoxelCuboid(4, 3, 4);
    private final MoreThermalEvaporationTier tier;
    private boolean foundController = false;

    public MoreThermalEvaporationValidator(MoreThermalEvaporationTier tier) {
        this.tier = tier;
    }

    @Override
    protected FormationResult validateFrame(FormationProtocol<MoreThermalEvaporationMultiblockData> ctx, BlockPos pos, BlockState state, CasingType type, boolean needsFrame) {
        boolean controller = structure.getTile(pos) instanceof TileEntityMoreThermalEvaporationController;
        if (foundController && controller) {
            return FormationResult.fail(MekanismLang.MULTIBLOCK_INVALID_CONTROLLER_CONFLICT, pos, true);
        }
        foundController |= controller;
        return super.validateFrame(ctx, pos, state, type, needsFrame);
    }

    @Override
    protected StructureRequirement getStructureRequirement(BlockPos pos) {
        WallRelative relative = cuboid.getWallRelative(pos);
        if (pos.getY() == cuboid.getMaxPos().getY()) {
            if (relative.isOnCorner()) {
                return StructureRequirement.IGNORED;
            } else if (!relative.isOnEdge()) {
                return StructureRequirement.INNER;
            } else {
                return StructureRequirement.OTHER;
            }
        }
        return super.getStructureRequirement(pos);
    }

    @Override
    protected CasingType getCasingType(BlockState state) {
        Block block = state.getBlock();
        if (BlockType.is(block, MoreThermalEvaporationBlockTypes.BLOCKS.get(this.tier))) {
            return CasingType.FRAME;
        } else if (BlockType.is(block, MoreThermalEvaporationBlockTypes.VALVES.get(this.tier))) {
            return CasingType.VALVE;
        } else if (BlockType.is(block, MoreThermalEvaporationBlockTypes.CONTROLLERS.get(this.tier))) {
            return CasingType.OTHER;
        }
        return CasingType.INVALID;
    }

    @Override
    public boolean precheck() {
        VoxelCuboid maxCuboid = new VoxelCuboid(4, this.tier.getHeight(), 4);
        cuboid = StructureHelper.fetchCuboid(structure, MIN_CUBOID, maxCuboid, EnumSet.complementOf(EnumSet.of(CuboidSide.TOP)), 8);
        return cuboid != null;
    }

    @Override
    public FormationResult postcheck(MoreThermalEvaporationMultiblockData structure, Long2ObjectMap<ChunkAccess> chunkMap) {
        if (!foundController) {
            return FormationResult.fail(MekanismLang.MULTIBLOCK_INVALID_NO_CONTROLLER);
        }

        int allowedHeight = structure.getAllowedHeight();
        int currentHeight = structure.height();

        // プラントの高さが許容高さを超えた場合は無効化する。
        // プラントの最大高さとは別に、許容高さを設け、実質的な最大高さの制限を行う。
        if (currentHeight > allowedHeight) {
            return FormationResult.FAIL;
        }

        return FormationResult.SUCCESS;
    }

}