package morethermalevaporation.client.gui;

import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.GuiDownArrow;
import mekanism.client.gui.element.GuiDumpButton;
import mekanism.client.gui.element.GuiElement;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.bar.GuiBar.IBarInfoHandler;
import mekanism.client.gui.element.bar.GuiHorizontalRateBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.tab.GuiHeatTab;
import mekanism.client.gui.element.tab.GuiWarningTab;
import mekanism.client.recipe_viewer.type.RecipeViewerRecipeType;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.IWarningTracker;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import morethermalevaporation.common.MoreThermalEvaporationLang;
import morethermalevaporation.common.content.evaporation.MoreThermalEvaporationMultiblockData;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import morethermalevaporation.common.tile.multiblock.TileEntityMoreThermalEvaporationController;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;

public class GuiMoreThermalEvaporationController extends GuiMekanismTile<TileEntityMoreThermalEvaporationController, MekanismTileContainer<TileEntityMoreThermalEvaporationController>> {

    private final MoreThermalEvaporationTier tier;
    private GuiElement inputGauge, outputGauge;

    public GuiMoreThermalEvaporationController(MekanismTileContainer<TileEntityMoreThermalEvaporationController> container, Inventory inv, Component title) {
        super(container, inv, title);
        inventoryLabelY += 2;
        titleLabelY = 4;
        dynamicSlots = true;
        this.tier = tile.getTier();
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiInnerScreen(this, 48, 15, 80, 46, () -> {
                    MoreThermalEvaporationMultiblockData multiblock = tile.getMultiblock();
                    return List.of(
                            MekanismLang.MULTIBLOCK_FORMED.translate(),
                            MoreThermalEvaporationLang.MULTIBLOCK_TYPE.translate(multiblock.getType().getLang().translate()),
                            MekanismLang.EVAPORATION_HEIGHT.translate(multiblock.height()),
                            MekanismLang.TEMPERATURE.translate(MekanismUtils.getTemperatureDisplay(multiblock.getTemperature(), TemperatureUnit.KELVIN, true)),
                            MekanismLang.FLUID_PRODUCTION.translate(Math.round(multiblock.lastGain * 100D) / 100D));
                }).padding(3).clearSpacing().recipeViewerCategories(RecipeViewerRecipeType.EVAPORATING)
        );
        addRenderableWidget(new GuiDownArrow(this, 32, 39));
        addRenderableWidget(new GuiDownArrow(this, 136, 39));
        addRenderableWidget(new GuiHorizontalRateBar(this, new IBarInfoHandler() {
            @Override
            public Component getTooltip() {
                return MekanismUtils.getTemperatureDisplay(tile.getMultiblock().getTemperature(), TemperatureUnit.KELVIN, true);
            }

            @Override
            public double getLevel() {
                return Math.min(1, tile.getMultiblock().getTemperature() / tier.getMultiplierTemp());
            }
        }, 48, 63))
                .warning(WarningType.INPUT_DOESNT_PRODUCE_OUTPUT, getWarningCheck(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT));
        inputGauge = addRenderableWidget(new GuiFluidGauge(() -> tile.getMultiblock().inputTank, () -> tile.getMultiblock().getFluidTanks(null), GaugeType.STANDARD, this, 6, 13))
                .warning(WarningType.NO_MATCHING_RECIPE, getWarningCheck(RecipeError.NOT_ENOUGH_INPUT));
        outputGauge = addRenderableWidget(new GuiFluidGauge(() -> tile.getMultiblock().outputTank, () -> tile.getMultiblock().getFluidTanks(null), GaugeType.STANDARD, this, 152, 13))
                .warning(WarningType.NO_SPACE_IN_OUTPUT, getWarningCheck(RecipeError.NOT_ENOUGH_OUTPUT_SPACE));
        addRenderableWidget(new GuiHeatTab(this, () -> {
            Component environment = MekanismUtils.getTemperatureDisplay(tile.getMultiblock().lastEnvironmentLoss, TemperatureUnit.KELVIN, false);
            return Collections.singletonList(MekanismLang.DISSIPATED_RATE.translate(environment));
        }));
        addRenderableWidget(new GuiDumpButton<>(this, tile, 130, 71));
    }

    private BooleanSupplier getWarningCheck(RecipeError error) {
        return () -> tile.getMultiblock().hasWarning(error);
    }

    @Override
    protected void addWarningTab(IWarningTracker warningTracker) {
        addRenderableWidget(new GuiWarningTab(this, warningTracker, false));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleTextWithOffset(guiGraphics, inputGauge.getRelativeRight(), outputGauge.getRelativeX());
        renderInventoryText(guiGraphics);
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}