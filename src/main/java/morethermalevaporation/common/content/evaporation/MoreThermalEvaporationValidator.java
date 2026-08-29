package morethermalevaporation.common.content.evaporation;

import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import mekanism.common.MekanismLang;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.math.voxel.VoxelCuboid.CuboidSide;
import mekanism.common.lib.math.voxel.VoxelCuboid.WallRelative;
import mekanism.common.lib.math.voxel.VoxelPlane;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.FormationProtocol;
import mekanism.common.lib.multiblock.FormationProtocol.CasingType;
import mekanism.common.lib.multiblock.FormationProtocol.FormationResult;
import mekanism.common.lib.multiblock.FormationProtocol.StructureRequirement;
import mekanism.common.lib.multiblock.Structure.Axis;
import mekanism.common.lib.multiblock.StructureHelper;
import morethermalevaporation.common.config.MoreThermalEvaporationConfig;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlockTypes;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import morethermalevaporation.common.tile.multiblock.TileEntityMoreThermalEvaporationController;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.EnumSet;

public class MoreThermalEvaporationValidator extends CuboidStructureValidator<MoreThermalEvaporationMultiblockData> {

    private static final VoxelCuboid NORMAL_MIN_CUBOID = new VoxelCuboid(4, 3, 4);
    private static final VoxelCuboid LARGE_MIN_CUBOID = new VoxelCuboid(9, 5, 9);
    private static final byte[][] BOTTOM_ALLOWED_GRID = {
            {0, 0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1},
            {0, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 0, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0}
    };
    private static final byte[][] CENTER_FRAME_ALLOWED_GRID = {
            {0, 0, 1, 1, 1, 1, 1, 0, 0},
            {0, 1, 1, 3, 3, 3, 1, 1, 0},
            {1, 1, 3, 3, 3, 3, 3, 1, 1},
            {1, 3, 3, 3, 3, 3, 3, 3, 1},
            {1, 3, 3, 3, 3, 3, 3, 3, 1},
            {1, 3, 3, 3, 3, 3, 3, 3, 1},
            {1, 1, 3, 3, 3, 3, 3, 1, 1},
            {0, 1, 1, 3, 3, 3, 1, 1, 0},
            {0, 0, 1, 1, 1, 1, 1, 0, 0}
    };
    private static final byte[][] CENTER_ALLOWED_GRID = {
            {0, 0, 1, 2, 2, 2, 1, 0, 0},
            {0, 1, 1, 3, 3, 3, 1, 1, 0},
            {1, 1, 3, 3, 3, 3, 3, 1, 1},
            {2, 3, 3, 3, 3, 3, 3, 3, 2},
            {2, 3, 3, 3, 3, 3, 3, 3, 2},
            {2, 3, 3, 3, 3, 3, 3, 3, 2},
            {1, 1, 3, 3, 3, 3, 3, 1, 1},
            {0, 1, 1, 3, 3, 3, 1, 1, 0},
            {0, 0, 1, 2, 2, 2, 1, 0, 0}
    };
    private static final byte[][] TOP_ALLOWED_GRID = {
            {0, 0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 0, 0},
            {0, 1, 3, 3, 3, 3, 3, 1, 0},
            {0, 1, 3, 3, 3, 3, 3, 1, 0},
            {1, 1, 3, 3, 3, 3, 3, 1, 1},
            {0, 1, 3, 3, 3, 3, 3, 1, 0},
            {0, 1, 3, 3, 3, 3, 3, 1, 0},
            {0, 0, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0}
    };
    private final MoreThermalEvaporationTier tier;
    private MoreThermalEvaporationType type;
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
        if (type == MoreThermalEvaporationType.NORMAL) {
            return getNormalRequirement(pos);
        }

        if (type == MoreThermalEvaporationType.LARGE) {
            return getLargeRequirement(pos);
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
        VoxelCuboid normalMaxCuboid = new VoxelCuboid(4, this.tier.getHeight(), 4);
        cuboid = StructureHelper.fetchCuboid(structure, NORMAL_MIN_CUBOID, normalMaxCuboid, EnumSet.complementOf(EnumSet.of(CuboidSide.TOP)), 8);
        if (cuboid != null) {
            type = MoreThermalEvaporationType.NORMAL;
            return true;
        }

        if (!MoreThermalEvaporationConfig.config.enabledLargeType.get()) {
            return false;
        }

        VoxelCuboid largeMaxCuboid = new VoxelCuboid(9, this.tier.getHeight(), 9);
        cuboid = StructureHelper.fetchCuboid(structure, LARGE_MIN_CUBOID, largeMaxCuboid, EnumSet.of(CuboidSide.TOP, CuboidSide.BOTTOM), 96);
        if (cuboid != null) {

            // 9x9 立方体の「最小座標」と「最大座標」を取得
            BlockPos minPos = cuboid.getMinPos();
            BlockPos maxPos = cuboid.getMaxPos();

            // 東方向の軸＝X軸 を取得
            Axis xAxis = CuboidSide.EAST.getAxis();
            // X軸に沿ったブロックの配置マップを取得
            // ※取得した配置マップにははみ出している構成ブロック情報も保持していため判定を行うことが可能
            Int2ObjectSortedMap<VoxelPlane> xMinorMap = structure.getMinorAxisMap(xAxis);
            if (xMinorMap != null && !xMinorMap.isEmpty()) {
                // 実際に設置されているブロックが存在する「最小のX座標」と「最大のX座標」を取得
                int firstX = xMinorMap.firstEntry().getKey();   // 最も西（マイナス方向）にあるブロックのX座標
                int lastX = xMinorMap.lastEntry().getKey();     // 最も東（プラス方向）にあるブロックのX座標
                // 設置されているブロックが、判定された 9x9 枠（minPos.getX() ～ maxPos.getX()）からはみ出しているか判定
                if (firstX < minPos.getX() || lastX > maxPos.getX()) {
                    return false;
                }
            }

            // 北方向の軸＝Z軸 を取得
            Axis zAxis = CuboidSide.NORTH.getAxis();
            // Z軸に沿ったブロックの配置マップを取得
            Int2ObjectSortedMap<VoxelPlane> zMinorMap = structure.getMinorAxisMap(zAxis);
            if (zMinorMap != null && !zMinorMap.isEmpty()) {
                // 実際に設置されているブロックが存在する「最小のZ座標」と「最大のZ座標」を取得
                int firstZ = zMinorMap.firstEntry().getKey();   // 最も北（マイナス方向）にあるブロックのZ座標
                int lastZ = zMinorMap.lastEntry().getKey();     // 最も南（プラス方向）にあるブロックのZ座標
                // 設置されているブロックが、判定された 9x9 枠（minPos.getZ() ～ maxPos.getZ()）からはみ出しているか判定
                if (firstZ < minPos.getZ() || lastZ > maxPos.getZ()) {
                    return false;
                }
            }

            type = MoreThermalEvaporationType.LARGE;
            return true;
        }
        return false;
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

    private static byte[][] getAllowedGrid(int y, int maxY) {
        if (y == 0) {
            return BOTTOM_ALLOWED_GRID;
        }
        if (y == maxY) {
            return TOP_ALLOWED_GRID;
        }
        if (y == 1 || y == maxY - 1) {
            return CENTER_FRAME_ALLOWED_GRID;
        }
        return CENTER_ALLOWED_GRID;
    }

    private StructureRequirement getNormalRequirement(BlockPos pos) {
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

    private StructureRequirement getLargeRequirement(BlockPos pos) {
        int z = pos.getZ() - cuboid.getMinPos().getZ(); // 列
        int x = pos.getX() - cuboid.getMinPos().getX(); // 行
        int y = pos.getY() - cuboid.getMinPos().getY();
        int maxY = cuboid.getMaxPos().getY() - cuboid.getMinPos().getY();

        byte[][] grid = getAllowedGrid(y, maxY);
        return StructureRequirement.REQUIREMENTS[grid[z][x]];
    }
}