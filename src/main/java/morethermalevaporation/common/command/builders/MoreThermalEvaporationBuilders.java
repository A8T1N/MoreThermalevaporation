package morethermalevaporation.common.command.builders;

import mekanism.common.command.builders.StructureBuilder;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MoreThermalEvaporationBuilders {
    protected MoreThermalEvaporationBuilders() {
    }

    public static class MoreEvaporationBuilder extends StructureBuilder {

        private final MoreThermalEvaporationTier tier;

        public MoreEvaporationBuilder(MoreThermalEvaporationTier tier) {
            super(4, tier.getHeight(), 4);
            this.tier = tier;
        }

        @Override
        public void build(Level world, BlockPos start, boolean empty) {
            buildFrame(world, start);
            buildWalls(world, start);
            buildInteriorLayers(world, start, 1, this.tier.getHeight() - 1, Blocks.AIR.defaultBlockState());
            world.setBlockAndUpdate(start.offset(1, 1, 0), MoreThermalEvaporationBlocks.CONTROLLERS.get(this.tier).defaultState());
        }

        @Override
        protected BlockState getCasing() {
            return MoreThermalEvaporationBlocks.BLOCKS.get(this.tier).defaultState();
        }
    }

}
