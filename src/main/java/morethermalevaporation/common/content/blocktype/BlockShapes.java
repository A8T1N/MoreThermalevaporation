package morethermalevaporation.common.content.blocktype;

import mekanism.common.util.EnumUtils;
import mekanism.common.util.VoxelShapeUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockShapes {

    private BlockShapes() {
    }

    private static VoxelShape box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return Block.box(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static final VoxelShape[] MORE_THERMAL_EVAPORATION_COMPACT = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];

    static {
        VoxelShapeUtils.setShape(VoxelShapeUtils.combine(
                box(0, 14, 2, 16, 16, 16), // top
                box(0, 0, 2, 16, 2, 16), // bottom
                box(0, 0, 0, 16, 16, 2), // front
                box(14, 2, 14, 16, 14, 16), // column1
                box(0, 2, 14, 2, 14, 16), // column2
                box(2, 2, 15.75, 14, 14, 16), // glass_back
                box(15.75, 2, 2, 16, 14, 14), // glass_right
                box(0, 2, 2, 0.25, 14, 14) // glass_left
        ), MORE_THERMAL_EVAPORATION_COMPACT);
    }
}
