package morethermalevaporation.client.gui;

import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.GuiConfigurableTile;
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
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import morethermalevaporation.common.tile.machine.TileEntityMoreThermalEvaporationCompact;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;

public class GuiMoreThermalEvaporationCompact extends GuiConfigurableTile<TileEntityMoreThermalEvaporationCompact, MekanismTileContainer<TileEntityMoreThermalEvaporationCompact>> {

    private final MoreThermalEvaporationTier tier;
    private GuiElement inputGauge, outputGauge;

    public GuiMoreThermalEvaporationCompact(MekanismTileContainer<TileEntityMoreThermalEvaporationCompact> container, Inventory inv, Component title) {
        super(container, inv, title);
        inventoryLabelY += 2;
        titleLabelY = 4;
        dynamicSlots = true;
        this.tier = getTileEntity().getTier();
    }

    @Override
    protected void addGuiElements() {

        super.addGuiElements();
        addRenderableWidget(new GuiInnerScreen(this, 48, 19, 80, 40, () -> List.of(
                MekanismLang.TEMPERATURE.translate(MekanismUtils.getTemperatureDisplay(tile.getTemperature(), TemperatureUnit.KELVIN, true)),
                MekanismLang.FLUID_PRODUCTION.translate(Math.round(tile.lastGain * 100D) / 100D))).padding(3).clearSpacing().recipeViewerCategories(RecipeViewerRecipeType.EVAPORATING)
        );
        addRenderableWidget(new GuiDownArrow(this, 32, 39));
        addRenderableWidget(new GuiDownArrow(this, 136, 39));
        addRenderableWidget(new GuiHorizontalRateBar(this, new IBarInfoHandler() {
            @Override
            public Component getTooltip() {
                return MekanismUtils.getTemperatureDisplay(tile.getTemperature(), TemperatureUnit.KELVIN, true);
            }

            @Override
            public double getLevel() {
                return Math.min(1, tile.getTemperature() / tier.getMultiplierTemp());
            }
        }, 48, 63))
                .warning(WarningType.INPUT_DOESNT_PRODUCE_OUTPUT, getWarningCheck(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT));
        inputGauge = addRenderableWidget(new GuiFluidGauge(() -> tile.inputTank, () -> tile.getFluidTanks(null), GaugeType.STANDARD, this, 6, 13))
                .warning(WarningType.NO_MATCHING_RECIPE, getWarningCheck(RecipeError.NOT_ENOUGH_INPUT));
        outputGauge = addRenderableWidget(new GuiFluidGauge(() -> tile.outputTank, () -> tile.getFluidTanks(null), GaugeType.STANDARD, this, 152, 13))
                .warning(WarningType.NO_SPACE_IN_OUTPUT, getWarningCheck(RecipeError.NOT_ENOUGH_OUTPUT_SPACE));
        addRenderableWidget(new GuiHeatTab(this, () -> {
            Component environment = MekanismUtils.getTemperatureDisplay(tile.lastEnvironmentLoss, TemperatureUnit.KELVIN, false);
            return Collections.singletonList(MekanismLang.DISSIPATED_RATE.translate(environment));
        }));
        addRenderableWidget(new GuiDumpButton<>(this, tile, 130, 71));
    }

    private BooleanSupplier getWarningCheck(RecipeError error) {
        return () -> tile.hasWarning(error);
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