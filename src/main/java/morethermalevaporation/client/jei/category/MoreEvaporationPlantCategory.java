package morethermalevaporation.client.jei.category;

import giselle.jei_mekanism_multiblocks.client.gui.CheckBoxWidget;
import giselle.jei_mekanism_multiblocks.client.gui.IntSliderWidget;
import giselle.jei_mekanism_multiblocks.client.gui.IntSliderWithButtons;
import giselle.jei_mekanism_multiblocks.client.jei.MultiblockCategory;
import giselle.jei_mekanism_multiblocks.client.jei.MultiblockWidget;
import giselle.jei_mekanism_multiblocks.client.jei.ResultWidget;
import giselle.jei_mekanism_multiblocks.client.jei.category.ICostConsumer;
import giselle.jei_mekanism_multiblocks.client.jei.category.ResistiveHeaterCategory;
import giselle.jei_mekanism_multiblocks.client.preview.IPreviewBuilder;
import giselle.jei_mekanism_multiblocks.client.preview.PreviewSelectors;
import giselle.jei_mekanism_multiblocks.common.JEI_MekanismMultiblocks;
import giselle.jei_mekanism_multiblocks.common.util.VolumeTextHelper;
import mekanism.api.heat.HeatAPI;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.config.MekanismConfig;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import mekanism.common.util.text.EnergyDisplay;
import mekanism.common.util.text.TextUtils;
import mekanism.generators.common.registries.GeneratorsBlocks;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import morethermalevaporation.common.MoreThermalEvaporationLang;
import morethermalevaporation.common.content.evaporation.MoreThermalEvaporationMultiblockData;
import morethermalevaporation.common.content.evaporation.MoreThermalEvaporationType;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public class MoreEvaporationPlantCategory extends MultiblockCategory<MoreEvaporationPlantCategory.MoreEvaporationPlantWidget> {
    private final MoreThermalEvaporationTier tier;

    public MoreEvaporationPlantCategory(IGuiHelper helper, MoreThermalEvaporationTier tier, RecipeType<MoreEvaporationPlantWidget> recipeType) {
        super(
                helper,
                recipeType,
                MoreThermalEvaporationLang.getLangPlant(tier).translate(),
                new ItemStack(MoreThermalEvaporationBlocks.CONTROLLERS.get(tier))
        );
        this.tier = tier;
    }

    @Override
    protected void getRecipeCatalystItemStacks(Consumer<ItemStack> consumer) {
        super.getRecipeCatalystItemStacks(consumer);
        consumer.accept(new ItemStack(MoreThermalEvaporationBlocks.BLOCKS.get(this.tier)));
        consumer.accept(new ItemStack(MoreThermalEvaporationBlocks.VALVES.get(this.tier)));
        consumer.accept(new ItemStack(MoreThermalEvaporationBlocks.CONTROLLERS.get(this.tier)));
        consumer.accept(new ItemStack(MekanismBlocks.STRUCTURAL_GLASS));

        if (JEI_MekanismMultiblocks.MekanismGeneratorsLoaded) {
            consumer.accept(new ItemStack(GeneratorsBlocks.ADVANCED_SOLAR_GENERATOR));
        }

    }

    public abstract static class MoreEvaporationPlantWidget extends MultiblockWidget {
        protected CheckBoxWidget useAdvancedSolarGeneratorCheckBox;
        protected CheckBoxWidget useFuelwoodHeaterCheckBox;
        protected CheckBoxWidget useLargeTypesCheckBox;
        protected IntSliderWithButtons valvesWidget;

        private boolean needHeatSource;
        private int fuelwoodHeaters;

        public MoreEvaporationPlantWidget() {

        }

        protected abstract MoreThermalEvaporationTier getTier();

        @Override
        public int getSideBlocks() {
            // 1 Controller
            // 4 Empty top inner
            return super.getSideBlocks() - 5;
        }

        public int getFreeFrameCount() {
            // 自由フレーム - コントローラ1個分
            return 12 * (this.getDimensionHeight() - 4) - 1;
        }

        @Override
        protected void collectOtherConfigs(Consumer<AbstractWidget> consumer) {
            super.collectOtherConfigs(consumer);

            if (JEI_MekanismMultiblocks.MekanismGeneratorsLoaded) {
                consumer.accept(this.useAdvancedSolarGeneratorCheckBox = new CheckBoxWidget(0, 0, 0, 0, Component.translatable("text.jei_mekanism_multiblocks.specs.use_things", new ItemStack(GeneratorsBlocks.ADVANCED_SOLAR_GENERATOR).getHoverName()), true));
                this.useAdvancedSolarGeneratorCheckBox.addSelectedChangedHandler(this::onUseAdvancedSolarGeneratorChanged);
            } else {
                this.useAdvancedSolarGeneratorCheckBox = new CheckBoxWidget(0, 0, 0, 0, Component.empty(), false);
                this.useAdvancedSolarGeneratorCheckBox.addSelectedChangedHandler(this::onUseAdvancedSolarGeneratorChanged);
            }

            consumer.accept(this.useFuelwoodHeaterCheckBox = new CheckBoxWidget(0, 0, 0, 0, Component.translatable("text.jei_mekanism_multiblocks.specs.use_things", new ItemStack(MekanismBlocks.FUELWOOD_HEATER).getHoverName()), true));
            this.useFuelwoodHeaterCheckBox.addSelectedChangedHandler(this::onUseFuelwoodHeaterChanged);

            consumer.accept(this.useLargeTypesCheckBox = new CheckBoxWidget(0, 0, 0, 0, Component.translatable("text.jei_mekanism_multiblocks.specs.use_things", MoreThermalEvaporationLang.MULTIBLOCK_TYPE.translate(MoreThermalEvaporationLang.TYPE_LARGE.translate())), false));
            this.useLargeTypesCheckBox.addSelectedChangedHandler(this::onUseLargeTypeChanged);

            consumer.accept(this.valvesWidget = new IntSliderWithButtons(0, 0, 0, 0, "text.jei_mekanism_multiblocks.specs.valves", 0, 2, 0));
            this.valvesWidget.getSlider().addValueChangeHanlder(this::onValvesChanged);

            this.onThermalModelChanged();
        }

        @Override
        public void load(CompoundTag tag) {
            super.load(tag);

            this.setUseAdvancedSolarGenerator(tag.getBoolean("UseAdvancedSolarGenerator"));
            this.setUseLargeTypes(tag.getBoolean("UseLargeType"));
            this.setUseFuelwoodHeater(tag.getBoolean("UseFuelwoodHeater"));
            this.setValveCount(tag.getInt("ValveCount"));
        }

        @Override
        public void save(CompoundTag tag) {
            super.save(tag);

            tag.putBoolean("UseAdvancedSolarGenerator", this.isUseAdvancedSolarGenerator());
            tag.putBoolean("UseLargeType", this.isUseLargeType());
            tag.putBoolean("UseFuelwoodHeater", this.isUseFuelwoodHeater());
            tag.putInt("ValveCount", this.getValveCount());
        }

        @Override
        protected void onDimensionChanged() {
            super.onDimensionChanged();

            this.onThermalModelChanged();
        }

        public void updateValveSliderLimit() {
            IntSliderWidget valvesSlider = this.valvesWidget.getSlider();
            int minValves = valvesSlider.getMinValue();
            int valves = valvesSlider.getValue();
            valvesSlider.setMinValue(2 + (this.isNeedHeatSource() ? (this.isUseFuelwoodHeater() ? this.getFuelwoodHeaters() : 1) : 0));
            valvesSlider.setMaxValue(this.isUseLargeType() ? this.getFreeFrameCount() : this.getSideBlocks());
            valvesSlider.setValue(valves + (valvesSlider.getMinValue() - minValves));
        }

        protected void onThermalModelChanged() {
            double requiredHeat = this.getMaxMultiplierHeat(0.0D);
            this.needHeatSource = requiredHeat > 0.0D;
            this.fuelwoodHeaters = 0;

            if (this.isNeedHeatSource()) {
                if (this.isUseFuelwoodHeater()) {
                    double heatPerTick = MekanismConfig.general.heatPerFuelTick.get() * MekanismConfig.general.fuelwoodTickMultiplier.get();
                    this.fuelwoodHeaters = Mth.ceil(requiredHeat / heatPerTick);
                }

            }

            this.updateValveSliderLimit();
        }

        protected void onValvesChanged(int valves) {
            this.markNeedUpdate();
        }

        @Override
        protected void onUseGlassChanged(boolean useGlass) {
            super.onUseGlassChanged(useGlass);
        }

        protected void onUseAdvancedSolarGeneratorChanged(boolean useAdvancedSolarGenerator) {
            this.markNeedUpdate();

            this.onThermalModelChanged();
        }

        protected void onUseFuelwoodHeaterChanged(boolean useFuelwoodHeater) {
            this.markNeedUpdate();

            this.onThermalModelChanged();
        }

        @Override
        public boolean canCreatePreview() {
            return true;
        }

        // TODO LargeType対応
        @Override
        protected void fillPreview(IPreviewBuilder builder) {
            super.fillPreview(builder);

            MoreThermalEvaporationTier tier = getTier();

            Vec3i dimension = this.getDimension();
            BlockPos controllerPos = new BlockPos(dimension.getX() - 2, 1, dimension.getZ() - 1);

            boolean useGlass = this.isUseGlass();
            boolean useAdvancedSolarGenerator = this.isUseAdvancedSolarGenerator();
            BlockState edgeState = MoreThermalEvaporationBlocks.BLOCKS.get(tier).defaultState();
            BlockState valveState = MoreThermalEvaporationBlocks.VALVES.get(tier).defaultState();
            BlockState sideState = useGlass ? this.getGlassBlock().defaultBlockState() : edgeState;

            builder.setBlockShell(edgeState, sideState);
            builder.setBlock(PreviewSelectors.top(), Blocks.AIR.defaultBlockState());
            builder.setBlock(controllerPos, Attribute.setFacing(Attribute.setActive(MoreThermalEvaporationBlocks.CONTROLLERS.get(tier).defaultState(), true), Direction.SOUTH));
            builder.replaceBlock(PreviewSelectors.shellSidesCCW(), sideState, valveState, this.getValveCount());

            if (useGlass && !useAdvancedSolarGenerator) {
                builder.setBlock(PreviewSelectors.topCorners(), edgeState);
                builder.setBlock(PreviewSelectors.topEdges(), sideState);
            } else {
                builder.setBlock(PreviewSelectors.topEdges(), edgeState);
            }

            if (useAdvancedSolarGenerator) {
                builder.setBlock(PreviewSelectors.topCorners(), GeneratorsBlocks.ADVANCED_SOLAR_GENERATOR.defaultState());
            }

        }

        protected void onUseLargeTypeChanged(boolean useLargeTypes) {
            this.markNeedUpdate();

            int currentHeight = this.getDimensionHeight();

            this.heightWidget.getSlider().setMinValue(
                    useLargeTypes ? 5 : 3
            );

            this.heightWidget.getSlider().setValue(currentHeight);

            this.updateValveSliderLimit();
        }

        @Override
        protected void collectCost(ICostConsumer consumer) {
            super.collectCost(consumer);
            MoreThermalEvaporationTier tier = getTier();

            int edges = this.getEdgeBlocks();
            int sides = this.getSideBlocks();
            int valves = this.getValveCount();
            sides -= valves;

            int casing = 0;
            int glasses = 0;
            int advancedSolarGenerators = 0;
            int upgrades = 0;

            if (isUseLargeType()) {
                final int top = 24;
                final int centerFrame = 32 * 2;
                final int bottom = 49;
                int frame = 20 * (this.getDimensionHeight() - 4);

                if (this.isUseGlass()) {
                    // getLargeSideBlocks() は「コントローラー1個分」が除外済みの自由枠数
                    // 自由枠のうちバルブ以外の場所をすべてガラスにする
                    glasses = this.getFreeFrameCount() - valves;
                } else {
                    // 自由枠のうちバルブ以外の場所をすべてCasingとして加算する
                    casing += this.getFreeFrameCount() - valves;
                }

                if (this.isUseAdvancedSolarGenerator()) {
                    advancedSolarGenerators += 4;
                }

                casing += top + centerFrame + frame + bottom;

            } else {

                if (this.isUseGlass()) {
                    casing = edges;
                    glasses = sides;

                    if (this.isUseAdvancedSolarGenerator()) {
                        // Replace top corner to solar generator
                        casing -= 4;
                        advancedSolarGenerators += 4;
                    } else {
                        // Replace top side to glass
                        casing -= 8;
                        glasses += 8;
                    }

                } else {
                    // Remove top vertices
                    casing = edges + sides - 4;

                    if (this.isUseAdvancedSolarGenerator()) {
                        advancedSolarGenerators += 4;
                    }

                }
            }

            consumer.accept(new ItemStack(MoreThermalEvaporationBlocks.CONTROLLERS.get(tier), 1));
            consumer.accept(new ItemStack(MoreThermalEvaporationBlocks.VALVES.get(tier), valves));
            consumer.accept(new ItemStack(MoreThermalEvaporationBlocks.BLOCKS.get(tier), casing));
            consumer.accept(new ItemStack(this.getGlassBlock(), glasses));

            if (JEI_MekanismMultiblocks.MekanismGeneratorsLoaded) {
                consumer.accept(new ItemStack(GeneratorsBlocks.ADVANCED_SOLAR_GENERATOR, advancedSolarGenerators));
            }


            if (this.isNeedHeatSource()) {
                if (this.isUseFuelwoodHeater()) {
                    consumer.accept(new ItemStack(MekanismBlocks.FUELWOOD_HEATER, this.getFuelwoodHeaters()));
                } else {
                    consumer.accept(new ItemStack(MekanismBlocks.RESISTIVE_HEATER));
                }

            }

//            consumer.accept(new ItemStack(MoreThermalEvaporationItems.STRUCTURE_UPGRADE.get(), upgrades));

        }

        @Override
        protected void collectResult(Consumer<AbstractWidget> consumer) {
            super.collectResult(consumer);
            MoreThermalEvaporationTier tier = getTier();
            long dimHeight = this.getDimensionHeight();
            long inputCapacity = tier == MoreThermalEvaporationTier.CREATIVE ? Integer.MAX_VALUE : getInputCapacity(tier, dimHeight);
            long outputCapacity = Math.min(Integer.MAX_VALUE, (long) tier.getOutputTankCapacity() * (isUseLargeType() ? MoreThermalEvaporationType.LARGE.getMultiplier() : MoreThermalEvaporationType.NORMAL.getMultiplier()));
            double maxTemp = tier.getMultiplierTemp() * (isUseLargeType() ? MoreThermalEvaporationType.LARGE.getMultiplier() : MoreThermalEvaporationType.NORMAL.getMultiplier());
            double maxSpeed = (maxTemp - HeatAPI.AMBIENT_TEMP) * MekanismConfig.general.evaporationTempMultiplier.get() * ((double) dimHeight / MoreThermalEvaporationMultiblockData.MAX_HEIGHT);
            ResultWidget speedWidget = new ResultWidget(Component.translatable("text.jei_mekanism_multiblocks.result.max_speed"), Component.literal("x" + TextUtils.format(maxSpeed)));
            speedWidget.setTooltipMessage(Component.translatable("text.jei_mekanism_multiblocks.tooltip.when_temp_ge", MekanismUtils.getTemperatureDisplay(maxTemp, TemperatureUnit.KELVIN, false)));
            consumer.accept(speedWidget);
            consumer.accept(new ResultWidget(Component.translatable("text.jei_mekanism_multiblocks.result.input_tank"), VolumeTextHelper.formatMB(inputCapacity)));
            consumer.accept(new ResultWidget(Component.translatable("text.jei_mekanism_multiblocks.result.output_tank"), VolumeTextHelper.formatMB(outputCapacity)));

            if (this.isNeedHeatSource() && !this.isUseFuelwoodHeater()) {
                this.createRequiredHeaterEnergyWidget(consumer);
            }
        }

        private void createRequiredHeaterEnergyWidget(Consumer<AbstractWidget> consumer) {

            MoreThermalEvaporationTier tier = getTier();
            long plainRequiredEnergy = this.getRequiredHeaterEnergy(HeatAPI.AMBIENT_TEMP);
            long coldestRequiredEnergy = this.getRequiredHeaterEnergy(HeatAPI.getAmbientTemp(Integer.MIN_VALUE));
            long hotestRequiredEnergy = this.getRequiredHeaterEnergy(HeatAPI.getAmbientTemp(Integer.MAX_VALUE));
            ResultWidget requiredEnergyWidget = new ResultWidget(Component.translatable("text.jei_mekanism_multiblocks.result.required_heater_usage"), Component.translatable("%s/t", EnergyDisplay.of(plainRequiredEnergy).getTextComponent()));
            Component heaterName = new ItemStack(MekanismBlocks.RESISTIVE_HEATER).getHoverName();
            Component valveName = new ItemStack(MoreThermalEvaporationBlocks.VALVES.get(tier)).getHoverName();
            requiredEnergyWidget.setTooltipMessage(
                    Component.translatable("text.jei_mekanism_multiblocks.tooltip.required_heater_usage.plain", Component.translatable("%s %s/t", TextUtils.format(plainRequiredEnergy), Component.translatable(MekanismLang.ENERGY_JOULES_SHORT.getTranslationKey()))), //
                    Component.translatable("text.jei_mekanism_multiblocks.tooltip.required_heater_usage.coldest", Component.translatable("%s %s/t", TextUtils.format(coldestRequiredEnergy), Component.translatable(MekanismLang.ENERGY_JOULES_SHORT.getTranslationKey()))), //
                    Component.translatable("text.jei_mekanism_multiblocks.tooltip.required_heater_usage.hottest", Component.translatable("%s %s/t", TextUtils.format(hotestRequiredEnergy), Component.translatable(MekanismLang.ENERGY_JOULES_SHORT.getTranslationKey()))), //
                    Component.translatable("text.jei_mekanism_multiblocks.tooltip.heater_near_and_1_sink_1", heaterName, valveName), //
                    Component.translatable("text.jei_mekanism_multiblocks.tooltip.heater_near_and_1_sink_2", heaterName, valveName));
            consumer.accept(requiredEnergyWidget);
        }

        public long getRequiredHeaterEnergy(double ambientTemp) {
            double heat = this.getMaxMultiplierHeat(ambientTemp);
            return (long) ResistiveHeaterCategory.getHeatTransferableEnergy(ambientTemp, heat, HeatAPI.DEFAULT_INVERSE_CONDUCTION);
        }

        public double getMaxMultiplierHeat(double ambientTemp) {
            int activeSolars = this.isUseAdvancedSolarGenerator() ? 4 : 0;
            double heatCapacity = this.getDimensionHeight() * MekanismConfig.general.evaporationHeatCapacity.get();
            double maxMultiplierTemp = this.getTier().getMultiplierTemp() * (isUseLargeType() ? MoreThermalEvaporationType.LARGE.getMultiplier() : MoreThermalEvaporationType.NORMAL.getMultiplier());
            double gain = activeSolars * MekanismConfig.general.evaporationSolarMultiplier.get() * heatCapacity;
            double loss = MekanismConfig.general.evaporationHeatDissipation.get() * Math.sqrt(Math.abs(maxMultiplierTemp - ambientTemp)) * heatCapacity;
            return loss - gain;
        }

        public long getInputCapacity(MoreThermalEvaporationTier tier, long dimHeight) {
            if (isUseLargeType()) {
                return ((dimHeight * 81) / 4) * tier.getInputTankCapacity();
            }
            return dimHeight * 4 * tier.getInputTankCapacity();
        }

        public int getValveCount() {
            return this.valvesWidget.getSlider().getValue();
        }

        public void setValveCount(int valveCount) {
            this.valvesWidget.getSlider().setValue(valveCount);
        }

        public boolean isUseAdvancedSolarGenerator() {
            return JEI_MekanismMultiblocks.MekanismGeneratorsLoaded && this.useAdvancedSolarGeneratorCheckBox.isSelected();
        }

        public void setUseAdvancedSolarGenerator(boolean useAdvancedSolarGenerator) {
            this.useAdvancedSolarGeneratorCheckBox.setSelected(useAdvancedSolarGenerator);
        }

        public boolean isUseFuelwoodHeater() {
            return this.useFuelwoodHeaterCheckBox.isSelected();
        }

        public void setUseFuelwoodHeater(boolean useFuelwoodHeater) {
            this.useFuelwoodHeaterCheckBox.setSelected(useFuelwoodHeater);
        }

        public boolean isNeedHeatSource() {
            return this.needHeatSource;
        }

        public int getFuelwoodHeaters() {
            return this.fuelwoodHeaters;
        }

        public boolean isUseLargeType() {
            return this.useLargeTypesCheckBox.isSelected();
        }

        public void setUseLargeTypes(boolean useLargeType) {
            this.useLargeTypesCheckBox.setSelected(useLargeType);
        }

        @Override
        public int getDimensionWidthMin() {
            return 4;
        }

        @Override
        public int getDimensionWidthMax() {
            return 4;
        }

        @Override
        public int getDimensionLengthMin() {
            return 4;
        }

        @Override
        public int getDimensionLengthMax() {
            return 4;
        }

        @Override
        public int getDimensionHeightMin() {
            return 3;
        }

        @Override
        public int getDimensionHeightMax() {
            MoreThermalEvaporationTier tier = getTier();
            return tier.getHeight();
        }

        @Override
        public Block getGlassBlock() {
            return MekanismBlocks.STRUCTURAL_GLASS.get();
        }

    }

    public static class BasicEvaporationPlantWidget extends MoreEvaporationPlantWidget {

        @Override
        protected MoreThermalEvaporationTier getTier() {
            return MoreThermalEvaporationTier.BASIC;
        }
    }

    public static class AdvancedEvaporationPlantWidget extends MoreEvaporationPlantWidget {

        @Override
        protected MoreThermalEvaporationTier getTier() {
            return MoreThermalEvaporationTier.ADVANCED;
        }
    }

    public static class EliteEvaporationPlantWidget extends MoreEvaporationPlantWidget {

        @Override
        protected MoreThermalEvaporationTier getTier() {
            return MoreThermalEvaporationTier.ELITE;
        }
    }

    public static class UltimateEvaporationPlantWidget extends MoreEvaporationPlantWidget {
        @Override
        protected MoreThermalEvaporationTier getTier() {
            return MoreThermalEvaporationTier.ULTIMATE;
        }
    }

    public static class CreativeEvaporationPlantWidget extends MoreEvaporationPlantWidget {
        @Override
        protected MoreThermalEvaporationTier getTier() {
            return MoreThermalEvaporationTier.CREATIVE;
        }
    }

    public static class OverclockedEvaporationPlantWidget extends MoreEvaporationPlantWidget {
        @Override
        protected MoreThermalEvaporationTier getTier() {
            return MoreThermalEvaporationTier.OVERCLOCKED;
        }
    }

    public static class QuantumEvaporationPlantWidget extends MoreEvaporationPlantWidget {
        @Override
        protected MoreThermalEvaporationTier getTier() {
            return MoreThermalEvaporationTier.QUANTUM;
        }
    }

    public static class DenseEvaporationPlantWidget extends MoreEvaporationPlantWidget {
        @Override
        protected MoreThermalEvaporationTier getTier() {
            return MoreThermalEvaporationTier.DENSE;
        }
    }

    public static class MultiversalEvaporationPlantWidget extends MoreEvaporationPlantWidget {
        @Override
        protected MoreThermalEvaporationTier getTier() {
            return MoreThermalEvaporationTier.MULTIVERSAL;
        }
    }

}