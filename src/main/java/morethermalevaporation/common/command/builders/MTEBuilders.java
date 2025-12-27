package morethermalevaporation.common.command.builders;

import mekanism.common.command.builders.StructureBuilder;
import morethermalevaporation.common.config.MoreThermalEvaporationConfig;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class MTEBuilders {
    protected MTEBuilders() {
    }

    public static class BasicEvaporationBuilder extends StructureBuilder {

        public BasicEvaporationBuilder() {
            super(4, MoreThermalEvaporationConfig.BasicEvaporationPlantHeight.get(), 4);
        }

        @Override
        public void build(Level world, BlockPos start, boolean empty) {
            buildFrame(world, start);
            buildWalls(world, start);
            buildInteriorLayers(world, start, 1, MoreThermalEvaporationConfig.BasicEvaporationPlantHeight.get() - 1, Blocks.AIR);
            world.setBlockAndUpdate(start.offset(1, 1, 0), MoreThermalEvaporationBlocks.BASIC_THERMAL_EVAPORATION_CONTROLLER.getBlock().defaultBlockState());
        }

        @Override
        protected Block getCasing() {
            return MoreThermalEvaporationBlocks.BASIC_THERMAL_EVAPORATION_BLOCK.getBlock();
        }
    }

    public static class AdvancedEvaporationBuilder extends StructureBuilder {

        public AdvancedEvaporationBuilder() {
            super(4, MoreThermalEvaporationConfig.AdvancedEvaporationPlantHeight.get(), 4);
        }

        @Override
        public void build(Level world, BlockPos start, boolean empty) {
            buildFrame(world, start);
            buildWalls(world, start);
            buildInteriorLayers(world, start, 1, MoreThermalEvaporationConfig.AdvancedEvaporationPlantHeight.get() - 1, Blocks.AIR);
            world.setBlockAndUpdate(start.offset(1, 1, 0), MoreThermalEvaporationBlocks.ADVANCED_THERMAL_EVAPORATION_CONTROLLER.getBlock().defaultBlockState());
        }

        @Override
        protected Block getCasing() {
            return MoreThermalEvaporationBlocks.ADVANCED_THERMAL_EVAPORATION_BLOCK.getBlock();
        }
    }

    public static class EliteEvaporationBuilder extends StructureBuilder {

        public EliteEvaporationBuilder() {
            super(4, MoreThermalEvaporationConfig.EliteEvaporationPlantHeight.get(), 4);
        }

        @Override
        public void build(Level world, BlockPos start, boolean empty) {
            buildFrame(world, start);
            buildWalls(world, start);
            buildInteriorLayers(world, start, 1, MoreThermalEvaporationConfig.EliteEvaporationPlantHeight.get() - 1, Blocks.AIR);
            world.setBlockAndUpdate(start.offset(1, 1, 0), MoreThermalEvaporationBlocks.ELITE_THERMAL_EVAPORATION_CONTROLLER.getBlock().defaultBlockState());
        }

        @Override
        protected Block getCasing() {
            return MoreThermalEvaporationBlocks.ELITE_THERMAL_EVAPORATION_BLOCK.getBlock();
        }
    }

    public static class UltimateEvaporationBuilder extends StructureBuilder {

        public UltimateEvaporationBuilder() {
            super(4, MoreThermalEvaporationConfig.UltimateEvaporationPlantHeight.get(), 4);
        }

        @Override
        public void build(Level world, BlockPos start, boolean empty) {
            buildFrame(world, start);
            buildWalls(world, start);
            buildInteriorLayers(world, start, 1, MoreThermalEvaporationConfig.UltimateEvaporationPlantHeight.get() - 1, Blocks.AIR);
            world.setBlockAndUpdate(start.offset(1, 1, 0), MoreThermalEvaporationBlocks.ULTIMATE_THERMAL_EVAPORATION_CONTROLLER.getBlock().defaultBlockState());
        }

        @Override
        protected Block getCasing() {
            return MoreThermalEvaporationBlocks.ULTIMATE_THERMAL_EVAPORATION_BLOCK.getBlock();
        }
    }
}
