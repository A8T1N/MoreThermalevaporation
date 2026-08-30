package morethermalevaporation.client.jei.category;

import giselle.jei_mekanism_multiblocks.client.gui.CheckBoxWidget;
import giselle.jei_mekanism_multiblocks.client.gui.IntSliderWidget;
import giselle.jei_mekanism_multiblocks.client.gui.IntSliderWithButtons;
import giselle.jei_mekanism_multiblocks.client.jei.MultiblockCategory;
import giselle.jei_mekanism_multiblocks.client.jei.MultiblockWidget;
import giselle.jei_mekanism_multiblocks.client.jei.ResultWidget;
import giselle.jei_mekanism_multiblocks.client.jei.category.ICostConsumer;
import giselle.jei_mekanism_multiblocks.common.JEI_MekanismMultiblocks;
import giselle.jei_mekanism_multiblocks.common.util.VolumeTextHelper;
import mekanism.api.heat.HeatAPI;
import mekanism.common.config.MekanismConfig;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import mekanism.common.util.text.TextUtils;
import mekanism.generators.common.registries.GeneratorsBlocks;
import mezz.jei.api.helpers.IGuiHelper;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.common.MoreThermalEvaporationLang;
import morethermalevaporation.common.content.evaporation.MoreThermalEvaporationMultiblockData;
import morethermalevaporation.common.content.evaporation.MoreThermalEvaporationType;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class MoreEvaporationPlantCategory extends MultiblockCategory<MoreEvaporationPlantCategory.MoreEvaporationPlantWidget> {
    private final MoreThermalEvaporationTier tier;

    public MoreEvaporationPlantCategory(IGuiHelper helper, MoreThermalEvaporationTier tier, Class<? extends MoreEvaporationPlantWidget> widgetClass) {
        // NOTE JEI表示順の為にティア順をパスに追加
        super(
                helper,
                MoreThermalEvaporation.rl(tier.ordinal() + "_" + tier.getBaseTier().getLowerName() + "_evaporation_plant"),
                widgetClass,
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
        protected CheckBoxWidget useLargeTypesCheckBox;
        protected IntSliderWithButtons valvesWidget;

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

            consumer.accept(this.useLargeTypesCheckBox = new CheckBoxWidget(0, 0, 0, 0, Component.translatable("text.jei_mekanism_multiblocks.specs.use_things", MoreThermalEvaporationLang.MULTIBLOCK_TYPE.translate(MoreThermalEvaporationLang.TYPE_LARGE.translate())), false));
            this.useLargeTypesCheckBox.addSelectedChangedHandler(this::onUseLargeTypeChanged);

            consumer.accept(this.valvesWidget = new IntSliderWithButtons(0, 0, 0, 0, "text.jei_mekanism_multiblocks.specs.valves", 0, 2, 0));
            this.valvesWidget.getSlider().addValueChangeHanlder(this::onValvesChanged);

            this.updateValveSliderLimit();
        }

        @Override
        public void load(CompoundTag tag) {
            super.load(tag);

            this.setUseAdvancedSolarGenerator(tag.getBoolean("UseAdvancedSolarGenerator"));
            this.setUseLargeTypes(tag.getBoolean("UseLargeType"));
            this.setValveCount(tag.getInt("ValveCount"));
        }

        @Override
        public void save(CompoundTag tag) {
            super.save(tag);

            tag.putBoolean("UseAdvancedSolarGenerator", this.isUseAdvancedSolarGenerator());
            tag.putBoolean("UseLargeType", this.isUseLargeType());
            tag.putInt("ValveCount", this.getValveCount());
        }

        @Override
        protected void onDimensionChanged() {
            super.onDimensionChanged();

            this.updateValveSliderLimit();
        }

        public void updateValveSliderLimit() {
            IntSliderWidget valvesSlider = this.valvesWidget.getSlider();
            int minValves = valvesSlider.getMinValue();
            int valves = valvesSlider.getValue();
            valvesSlider.setMinValue(this.isUseAdvancedSolarGenerator() && !this.isUseLargeType() ? 2 : 3);
            valvesSlider.setMaxValue(this.isUseLargeType() ? this.getFreeFrameCount() : this.getSideBlocks());
            valvesSlider.setValue(valves + (valvesSlider.getMinValue() - minValves));
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

            this.updateValveSliderLimit();
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

            int corners = this.getCornerBlocks();
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
                    casing = corners;
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
                    casing = corners + sides - 4;

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
            speedWidget.setJeiTooltip(Component.translatable("text.jei_mekanism_multiblocks.tooltip.when_temp_ge", MekanismUtils.getTemperatureDisplay(maxTemp, TemperatureUnit.KELVIN, false)));
            consumer.accept(speedWidget);
            consumer.accept(new ResultWidget(Component.translatable("text.jei_mekanism_multiblocks.result.input_tank"), VolumeTextHelper.formatMB(inputCapacity)));
            consumer.accept(new ResultWidget(Component.translatable("text.jei_mekanism_multiblocks.result.output_tank"), VolumeTextHelper.formatMB(outputCapacity)));
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

}