package morethermalevaporation.common.tile.machine;

import mekanism.api.IContentsListener;
import mekanism.api.NBTConstants;
import mekanism.api.RelativeSide;
import mekanism.api.heat.HeatAPI;
import mekanism.api.recipes.FluidToFluidRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.cache.OneInputCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.Mekanism;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.heat.BasicHeatCapacitor;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.heat.HeatCapacitorHelper;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerFluidTankWrapper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerHeatCapacitorWrapper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.SyntheticComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.sync.dynamic.ContainerSync;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.ISingleRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.tile.component.ITileComponent;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.FluidSlotInfo;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.upgrade.IUpgradeData;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import mekanism.common.util.WorldUtils;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import morethermalevaporation.common.upgrade.MoreThermalEvaporationCompactUpgradeData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class TileEntityMoreThermalEvaporationCompact extends TileEntityRecipeMachine<FluidToFluidRecipe> implements ISingleRecipeLookupHandler.FluidRecipeLookupHandler<FluidToFluidRecipe> {
    public static final int MAX_HEIGHT = 18;
    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );
    @ContainerSync
    private final boolean[] trackedErrors = new boolean[TRACKED_ERROR_TYPES.size()];
    private final IOutputHandler<@NotNull FluidStack> outputHandler;
    private final IInputHandler<@NotNull FluidStack> inputHandler;
    private final double biomeAmbientTemp;
    @ContainerSync
    @WrappingComputerMethod(wrapper = ComputerFluidTankWrapper.class, methodNames = {"getInput", "getInputCapacity", "getInputNeeded", "getInputFilledPercentage"}, docPlaceholder = "input tank")
    public BasicFluidTank inputTank;
    @ContainerSync
    @WrappingComputerMethod(wrapper = ComputerFluidTankWrapper.class, methodNames = {"getOutput", "getOutputCapacity", "getOutputNeeded", "getOutputFilledPercentage"}, docPlaceholder = "output tank")
    public BasicFluidTank outputTank;
    @WrappingComputerMethod(wrapper = ComputerHeatCapacitorWrapper.class, methodNames = "getTemperature", docPlaceholder = "heater")
    public BasicHeatCapacitor heatCapacitor;
    public float prevScale;
    @ContainerSync
    @SyntheticComputerMethod(getter = "getProductionAmount")
    public double lastGain;
    @ContainerSync
    @SyntheticComputerMethod(getter = "getEnvironmentalLoss")
    public double lastEnvironmentLoss;
    public MoreThermalEvaporationTier tier;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getInputItemInput", docPlaceholder = "input side's input slot")
    FluidInventorySlot inputInputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getInputItemOutput", docPlaceholder = "input side's output slot")
    OutputInventorySlot outputInputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getOutputItemInput", docPlaceholder = "output side's input slot")
    FluidInventorySlot inputOutputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getOutputItemOutput", docPlaceholder = "output side's output slot")
    OutputInventorySlot outputOutputSlot;
    private double tempMultiplier;
    private int inputTankCapacity;
    private boolean needsPacket;
    private boolean updateClientLight = false;

    public TileEntityMoreThermalEvaporationCompact(MoreThermalEvaporationTier tier, BlockPos pos, BlockState state) {
        super(MoreThermalEvaporationBlocks.COMPACTS.get(tier), pos, state, TRACKED_ERROR_TYPES);
        biomeAmbientTemp = HeatAPI.getAmbientTemp(this.getLevel(), this.getTilePos());
        heatCapacitor.setHeatCapacity(
                MekanismConfig.general.evaporationHeatCapacity.get() * MAX_HEIGHT,
                true
        );
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.FLUID);

        ConfigInfo itemConfig = configComponent.getConfig(TransmissionType.ITEM);
        if (itemConfig != null) {
            itemConfig.addSlotInfo(DataType.INPUT_1, new InventorySlotInfo(true, false, inputInputSlot));
            itemConfig.addSlotInfo(DataType.INPUT_2, new InventorySlotInfo(true, false, inputOutputSlot));
            itemConfig.addSlotInfo(DataType.OUTPUT_1, new InventorySlotInfo(false, true, outputInputSlot));
            itemConfig.addSlotInfo(DataType.OUTPUT_2, new InventorySlotInfo(false, true, outputOutputSlot));
            itemConfig.addSlotInfo(DataType.INPUT_OUTPUT, new InventorySlotInfo(true, true, inputInputSlot, inputOutputSlot, outputInputSlot, outputOutputSlot));
            itemConfig.setDataType(DataType.INPUT_1, RelativeSide.TOP);
            itemConfig.setDataType(DataType.INPUT_2, RelativeSide.BOTTOM);
        }

        ConfigInfo fluidConfig = configComponent.getConfig(TransmissionType.FLUID);
        if (fluidConfig != null) {
            fluidConfig.addSlotInfo(DataType.INPUT, new FluidSlotInfo(true, false, inputTank));
            fluidConfig.addSlotInfo(DataType.OUTPUT, new FluidSlotInfo(false, true, outputTank));
            fluidConfig.addSlotInfo(DataType.INPUT_OUTPUT, new FluidSlotInfo(true, true, inputTank, outputTank));
            fluidConfig.setDataType(DataType.INPUT, RelativeSide.LEFT);
            fluidConfig.setDataType(DataType.OUTPUT, RelativeSide.RIGHT);
            configComponent.setupIOConfig(TransmissionType.FLUID, inputTank, outputTank, RelativeSide.RIGHT).setEjecting(true);
        }

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.FLUID).setCanTankEject(tank -> tank != inputTank);

        inputHandler = InputHelper.getInputHandler(inputTank, RecipeError.NOT_ENOUGH_INPUT);
        outputHandler = OutputHelper.getOutputHandler(outputTank, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);

    }

    @Override
    @Nullable
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputTank = BasicFluidTank.input(this.getMaxFluid(), this::containsRecipe, recipeCacheListener));
        builder.addTank(outputTank = BasicFluidTank.output(this.tier.getOutputTankCapacity(), recipeCacheListener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener, IContentsListener recipeCacheListener, CachedAmbientTemperature ambientTemperature) {
        HeatCapacitorHelper builder = HeatCapacitorHelper.forSide(this::getDirection);
        builder.addCapacitor(heatCapacitor = BasicHeatCapacitor.create(MekanismConfig.general.evaporationHeatCapacity.get() * 3, () -> biomeAmbientTemp, listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener, IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inputInputSlot = FluidInventorySlot.fill(inputTank, listener, 28, 20));
        builder.addSlot(outputInputSlot = OutputInventorySlot.at(listener, 28, 51));
        builder.addSlot(inputOutputSlot = FluidInventorySlot.drain(outputTank, listener, 132, 20));
        builder.addSlot(outputOutputSlot = OutputInventorySlot.at(listener, 132, 51));
        inputInputSlot.setSlotType(ContainerSlotType.INPUT);
        inputOutputSlot.setSlotType(ContainerSlotType.INPUT);
        return builder.build();
    }

    @Override
    protected void onUpdateClient() {
        super.onUpdateClient();
        if (updateClientLight) {
            WorldUtils.recheckLighting(level, worldPosition);
            updateClientLight = false;
        }
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();

        // Render
        float scale = MekanismUtils.getScale(prevScale, inputTank);
        if (scale != prevScale) {
            if (prevScale == 0 || scale == 0) {
                WorldUtils.recheckLighting(level, worldPosition);
            }
            prevScale = scale;
            needsPacket = true;
        }
        if (needsPacket) {
            sendUpdatePacket();
            needsPacket = false;
        }

        lastEnvironmentLoss = simulateEnvironment();
        updateHeatCapacitors(null);
        tempMultiplier = (Math.min(this.tier.getMultiplierTemp(), getTemperature()) - HeatAPI.AMBIENT_TEMP)
                * MekanismConfig.general.evaporationTempMultiplier.get();
        inputOutputSlot.drainTank(outputOutputSlot);
        inputInputSlot.fillTank(outputInputSlot);
        setActive(recipeCacheLookupMonitor.updateAndProcess());
    }

    @Override
    public double simulateEnvironment() {
        double currentTemperature = getTemperature();
        double heatCapacity = heatCapacitor.getHeatCapacity();
        if (Math.abs(currentTemperature - biomeAmbientTemp) < 0.001) {
            heatCapacitor.handleHeat(biomeAmbientTemp * heatCapacity - heatCapacitor.getHeat());
        } else {
            double incr = MekanismConfig.general.evaporationHeatDissipation.get() * Math.sqrt(Math.abs(currentTemperature - biomeAmbientTemp));
            if (currentTemperature > biomeAmbientTemp) {
                incr = -incr;
            }
            heatCapacitor.handleHeat(heatCapacity * incr);
            if (incr < 0) {
                return -incr;
            }
        }
        return 0;
    }

    @ComputerMethod
    public double getTemperature() {
        return heatCapacitor.getTemperature();
    }

    public int getMaxFluid() {
        inputTankCapacity = (288 / 4) * this.tier.getInputTankCapacity();
        return inputTankCapacity;
    }


    @Override
    public @NotNull IMekanismRecipeTypeProvider<FluidToFluidRecipe, InputRecipeCache.SingleFluid<FluidToFluidRecipe>> getRecipeType() {
        return MekanismRecipeType.EVAPORATING;
    }

    @Override
    public @Nullable FluidToFluidRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandler);
    }

    @Override
    public void clearRecipeErrors(int cacheIndex) {
        Arrays.fill(trackedErrors, false);
    }

    @Override
    public @NotNull CachedRecipe<FluidToFluidRecipe> createNewCachedRecipe(@NotNull FluidToFluidRecipe recipe, int cacheIndex) {
        return OneInputCachedRecipe.fluidToFluid(recipe, recheckAllRecipeErrors, inputHandler, outputHandler)
                .setErrorsChanged(errors -> {
                    for (int i = 0; i < trackedErrors.length; i++) {
                        trackedErrors[i] = errors.contains(TRACKED_ERROR_TYPES.get(i));
                    }
                })
                .setActive(active -> {
                    if (active) {
                        if (tempMultiplier > 0 && tempMultiplier < 1) {
                            lastGain = 1F / (int) Math.ceil(1 / tempMultiplier);
                        } else {
                            lastGain = tempMultiplier;
                        }
                    } else {
                        lastGain = 0;
                    }
                })
                .setCanHolderFunction(() -> tempMultiplier > 0 && MekanismUtils.canFunction(this))
                .setRequiredTicks(() -> tempMultiplier > 0 && tempMultiplier < 1 ? (int) Math.ceil(1 / tempMultiplier) : 1)
                .setBaselineMaxOperations(() -> tempMultiplier > 0 && tempMultiplier < 1 ? 1 : (int) tempMultiplier);
    }

    @Override
    public int getRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(inputTank.getFluidAmount(), inputTank.getCapacity());
    }

    public boolean hasWarning(RecipeError error) {
        int errorIndex = TRACKED_ERROR_TYPES.indexOf(error);
        if (errorIndex == -1) {
            return false;
        }
        return trackedErrors[errorIndex];
    }

    @Override
    protected void presetVariables() {
        super.presetVariables();
        this.tier = Attribute.getTier(getBlockType(), MoreThermalEvaporationTier.class);
    }

    @NotNull
    @Override
    public CompoundTag getReducedUpdateTag() {
        CompoundTag updateTag = super.getReducedUpdateTag();
        updateTag.put(NBTConstants.FLUID_STORED, inputTank.getFluid().writeToNBT(new CompoundTag()));
        updateTag.putFloat(NBTConstants.SCALE, prevScale);
        return updateTag;
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag) {
        super.handleUpdateTag(tag);
        NBTUtils.setFluidStackIfPresent(tag, NBTConstants.FLUID_STORED, fluid -> inputTank.setStack(fluid));
        NBTUtils.setFloatIfPresent(tag, NBTConstants.SCALE, scale -> {
            if (prevScale != scale) {
                if (prevScale == 0 || scale == 0) {
                    updateClientLight = true;
                }
                prevScale = scale;
            }
        });
    }

    @Override
    public void parseUpgradeData(@NotNull IUpgradeData upgradeData) {
        if (upgradeData instanceof MoreThermalEvaporationCompactUpgradeData data) {
            redstone = data.redstone;
            setControlType(data.controlType);
            for (ITileComponent component : getComponents()) {
                component.read(data.components);
            }
            inputTank.deserializeNBT(data.inputTank.serializeNBT());
            outputTank.deserializeNBT(data.outputTank.serializeNBT());
            inputInputSlot.deserializeNBT(data.inputInputSlot.serializeNBT());
            outputInputSlot.deserializeNBT(data.outputInputSlot.serializeNBT());
            inputOutputSlot.deserializeNBT(data.inputOutputSlot.serializeNBT());
            outputOutputSlot.deserializeNBT(data.outputOutputSlot.serializeNBT());
            heatCapacitor.deserializeNBT(data.heatCapacitor.serializeNBT());
        } else {
            Mekanism.logger.warn("Unhandled upgrade data.", new Throwable());
        }
    }

    @Override
    public @Nullable MoreThermalEvaporationCompactUpgradeData getUpgradeData() {
        return new MoreThermalEvaporationCompactUpgradeData(redstone, getControlType(), inputTank, outputTank, inputInputSlot, outputInputSlot, inputOutputSlot, outputOutputSlot, heatCapacitor, getComponents());
    }

    public MoreThermalEvaporationTier getTier() {
        return this.tier;
    }
}
