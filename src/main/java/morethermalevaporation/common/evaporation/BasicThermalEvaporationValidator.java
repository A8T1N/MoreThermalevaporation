package morethermalevaporation.common.evaporation;

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
import morethermalevaporation.tile.multiblock.TileEntityBasicThermalEvaporationController;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.EnumSet;

public class BasicThermalEvaporationValidator extends CuboidStructureValidator<BasicThermalEvaporationMultiblockData> {

    private static final VoxelCuboid MIN_CUBOID = new VoxelCuboid(4, 3, 4);
    private static final VoxelCuboid MAX_CUBOID = new VoxelCuboid(4, 18, 4);

    private boolean foundController = false;

    @Override
    protected FormationResult validateFrame(FormationProtocol<BasicThermalEvaporationMultiblockData> ctx, BlockPos pos, BlockState state, CasingType type, boolean needsFrame) {
        boolean controller = structure.getTile(pos) instanceof TileEntityBasicThermalEvaporationController;
        if (foundController && controller) {
            //Ensure we don't allow ignoring the failure as if there are multiple in the corners which are ignored spots
            // it is possible then we will form with multiple controllers
            System.out.println("[DEBUG] validateFrame " + pos + " " + state.getBlock());
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
        if (BlockType.is(block, MoreThermalEvaporationBlockTypes.BASIC_THERMAL_EVAPORATION_BLOCK)) {
            return CasingType.FRAME;
        } else if (BlockType.is(block, MoreThermalEvaporationBlockTypes.BASIC_THERMAL_EVAPORATION_VALVE)) {
            return CasingType.VALVE;
        } else if (BlockType.is(block, MoreThermalEvaporationBlockTypes.BASIC_THERMAL_EVAPORATION_CONTROLLER)) {
            return CasingType.OTHER;
        }
        return CasingType.INVALID;
    }

    @Override
    public boolean precheck() {
        cuboid = StructureHelper.fetchCuboid(structure, MIN_CUBOID, MAX_CUBOID, EnumSet.complementOf(EnumSet.of(CuboidSide.TOP)), 8);
        System.out.println("[DEBUG] precheck cuboid = " + cuboid);
        return cuboid != null;
    }

    @Override
    public FormationResult postcheck(BasicThermalEvaporationMultiblockData structure, Long2ObjectMap<ChunkAccess> chunkMap) {
        System.out.println("[DEBUG] postcheck foundController=" + foundController);
        if (!foundController) {
            return FormationResult.fail(MekanismLang.MULTIBLOCK_INVALID_NO_CONTROLLER);
        }
        return FormationResult.SUCCESS;
    }
}