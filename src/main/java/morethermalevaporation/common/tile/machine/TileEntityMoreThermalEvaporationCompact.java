package morethermalevaporation.common.tile.machine;

import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.SerializationConstants;
import mekanism.api.heat.HeatAPI;
import mekanism.api.recipes.FluidToFluidRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.cache.OneInputCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.api.recipes.vanilla_input.SingleFluidRecipeInput;
import mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType;
import mekanism.client.recipe_viewer.type.RecipeViewerRecipeType;
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
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleFluid;
import mekanism.common.tile.component.ITileComponent;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.FluidSlotInfo;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.interfaces.IHasDumpButton;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.upgrade.IUpgradeData;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import morethermalevaporation.common.upgrade.MoreThermalEvaporationCompactUpgradeData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TileEntityMoreThermalEvaporationCompact extends TileEntityRecipeMachine<FluidToFluidRecipe>
        implements ISingleRecipeLookupHandler.FluidRecipeLookupHandler<FluidToFluidRecipe>, IHasDumpButton {

    public static final int MAX_HEIGHT = 18;

    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );

    private final IOutputHandler<@NotNull FluidStack> outputHandler;
    private final IInputHandler<@NotNull FluidStack> inputHandler;

    @ContainerSync
    @WrappingComputerMethod(
            wrapper = ComputerFluidTankWrapper.class,
            methodNames = {"getInput", "getInputCapacity", "getInputNeeded", "getInputFilledPercentage"},
            docPlaceholder = "input tank"
    )
    public BasicFluidTank inputTank;

    @ContainerSync
    @WrappingComputerMethod(
            wrapper = ComputerFluidTankWrapper.class,
            methodNames = {"getOutput", "getOutputCapacity", "getOutputNeeded", "getOutputFilledPercentage"},
            docPlaceholder = "output tank"
    )
    public BasicFluidTank outputTank;

    @WrappingComputerMethod(
            wrapper = ComputerHeatCapacitorWrapper.class,
            methodNames = "getTemperature",
            docPlaceholder = "heater"
    )
    public BasicHeatCapacitor heatCapacitor;

    public float prevScale;

    @ContainerSync
    @SyntheticComputerMethod(getter = "getProductionAmount")
    public double lastGain;

    @ContainerSync
    @SyntheticComputerMethod(getter = "getEnvironmentalLoss")
    public double lastEnvironmentLoss;

    public MoreThermalEvaporationTier tier;

    @WrappingComputerMethod(
            wrapper = ComputerIInventorySlotWrapper.class,
            methodNames = "getInputItemInput",
            docPlaceholder = "input side's input slot"
    )
    FluidInventorySlot inputInputSlot;

    @WrappingComputerMethod(
            wrapper = ComputerIInventorySlotWrapper.class,
            methodNames = "getInputItemOutput",
            docPlaceholder = "input side's output slot"
    )
    OutputInventorySlot outputInputSlot;

    @WrappingComputerMethod(
            wrapper = ComputerIInventorySlotWrapper.class,
            methodNames = "getOutputItemInput",
            docPlaceholder = "output side's input slot"
    )
    FluidInventorySlot inputOutputSlot;

    @WrappingComputerMethod(
            wrapper = ComputerIInventorySlotWrapper.class,
            methodNames = "getOutputItemOutput",
            docPlaceholder = "output side's output slot"
    )
    OutputInventorySlot outputOutputSlot;

    private double tempMultiplier;
    private boolean updateClientLight;

    public TileEntityMoreThermalEvaporationCompact(
            MoreThermalEvaporationTier tier,
            BlockPos pos,
            BlockState state
    ) {
        super(
                MoreThermalEvaporationBlocks.COMPACTS.get(tier),
                pos,
                state,
                TRACKED_ERROR_TYPES
        );

        heatCapacitor.setHeatCapacity(
                MekanismConfig.general.evaporationHeatCapacity.get() * MAX_HEIGHT,
                true
        );

        ConfigInfo itemConfig = configComponent.getConfig(TransmissionType.ITEM);
        if (itemConfig != null) {
            itemConfig.addSlotInfo(
                    DataType.INPUT_1,
                    new InventorySlotInfo(true, false, inputInputSlot)
            );
            itemConfig.addSlotInfo(
                    DataType.INPUT_2,
                    new InventorySlotInfo(true, false, inputOutputSlot)
            );
            itemConfig.addSlotInfo(
                    DataType.OUTPUT_1,
                    new InventorySlotInfo(false, true, outputInputSlot)
            );
            itemConfig.addSlotInfo(
                    DataType.OUTPUT_2,
                    new InventorySlotInfo(false, true, outputOutputSlot)
            );
            itemConfig.addSlotInfo(
                    DataType.INPUT_OUTPUT,
                    new InventorySlotInfo(
                            true,
                            true,
                            inputInputSlot,
                            inputOutputSlot,
                            outputInputSlot,
                            outputOutputSlot
                    )
            );

            itemConfig.setDataType(DataType.INPUT_1, RelativeSide.TOP);
            itemConfig.setDataType(DataType.INPUT_2, RelativeSide.BOTTOM);
        }

        ConfigInfo fluidConfig = configComponent.getConfig(TransmissionType.FLUID);
        if (fluidConfig != null) {
            fluidConfig.addSlotInfo(
                    DataType.INPUT,
                    new FluidSlotInfo(true, false, inputTank)
            );
            fluidConfig.addSlotInfo(
                    DataType.OUTPUT,
                    new FluidSlotInfo(false, true, outputTank)
            );
            fluidConfig.addSlotInfo(
                    DataType.INPUT_OUTPUT,
                    new FluidSlotInfo(true, true, inputTank, outputTank)
            );

            fluidConfig.setDataType(DataType.INPUT, RelativeSide.LEFT);
            fluidConfig.setDataType(DataType.OUTPUT, RelativeSide.RIGHT);

            configComponent
                    .setupIOConfig(
                            TransmissionType.FLUID,
                            inputTank,
                            outputTank,
                            RelativeSide.RIGHT
                    )
                    .setEjecting(true);
        }

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent
                .setOutputData(
                        configComponent,
                        TransmissionType.ITEM,
                        TransmissionType.FLUID
                )
                .setCanTankEject(tank -> tank != inputTank);

        inputHandler = InputHelper.getInputHandler(
                inputTank,
                RecipeError.NOT_ENOUGH_INPUT
        );

        outputHandler = OutputHelper.getOutputHandler(
                outputTank,
                RecipeError.NOT_ENOUGH_OUTPUT_SPACE
        );
    }

    @Override
    @Nullable
    protected IFluidTankHolder getInitialFluidTanks(
            IContentsListener listener,
            IContentsListener recipeCacheListener,
            IContentsListener recipeCacheUnpauseListener
    ) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this);

        builder.addTank(
                inputTank = BasicFluidTank.input(
                        getMaxFluid(),
                        this::containsRecipe,
                        recipeCacheListener
                )
        );

        builder.addTank(
                outputTank = BasicFluidTank.output(
                        tier.getOutputTankCapacity(),
                        recipeCacheUnpauseListener
                )
        );

        return builder.build();
    }

    @NotNull
    @Override
    protected IHeatCapacitorHolder getInitialHeatCapacitors(
            IContentsListener listener,
            IContentsListener recipeCacheListener,
            IContentsListener recipeCacheUnpauseListener,
            CachedAmbientTemperature ambientTemperature
    ) {
        HeatCapacitorHelper builder =
                HeatCapacitorHelper.forSide(this::getDirection);

        builder.addCapacitor(
                heatCapacitor = BasicHeatCapacitor.create(
                        MekanismConfig.general.evaporationHeatCapacity.get() * 3,
                        ambientTemperature,
                        listener
                )
        );

        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(
            IContentsListener listener,
            IContentsListener recipeCacheListener,
            IContentsListener recipeCacheUnpauseListener
    ) {
        InventorySlotHelper builder =
                InventorySlotHelper.forSideWithConfig(this);

        builder.addSlot(
                inputInputSlot =
                        FluidInventorySlot.fill(inputTank, listener, 28, 20)
        );

        builder.addSlot(
                outputInputSlot =
                        OutputInventorySlot.at(listener, 28, 51)
        );

        builder.addSlot(
                inputOutputSlot =
                        FluidInventorySlot.drain(outputTank, listener, 132, 20)
        );

        builder.addSlot(
                outputOutputSlot =
                        OutputInventorySlot.at(listener, 132, 51)
        );

        inputInputSlot.setSlotType(ContainerSlotType.INPUT);
        inputOutputSlot.setSlotType(ContainerSlotType.INPUT);

        return builder.build();
    }

    @Override
    protected void onUpdateClient() {
        super.onUpdateClient();

        if (updateClientLight) {
            level.getLightEngine().checkBlock(worldPosition);
            updateClientLight = false;
        }
    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket = super.onUpdateServer();

        float scale = MekanismUtils.getScale(prevScale, inputTank);

        if (MekanismUtils.scaleChanged(scale, prevScale)) {
            if (prevScale == 0 || scale == 0) {
                level.getLightEngine().checkBlock(worldPosition);
            }

            prevScale = scale;
            sendUpdatePacket = true;
        }

        lastEnvironmentLoss = simulateEnvironment();
        updateHeatCapacitors(null);

        tempMultiplier =
                (Math.min(tier.getMultiplierTemp(), getTemperature())
                        - HeatAPI.AMBIENT_TEMP)
                        * MekanismConfig.general.evaporationTempMultiplier.get();

        inputOutputSlot.drainTank(outputOutputSlot);
        inputInputSlot.fillTank(outputInputSlot);

        recipeCacheLookupMonitor.updateAndProcess();

        return sendUpdatePacket;
    }

    @Override
    public double simulateEnvironment() {
        double biomeAmbientTemp = ambientTemperature.getAsDouble();
        double currentTemperature = getTemperature();
        double heatCapacity = heatCapacitor.getHeatCapacity();

        if (Math.abs(currentTemperature - biomeAmbientTemp) < 0.001) {
            heatCapacitor.handleHeat(
                    biomeAmbientTemp * heatCapacity
                            - heatCapacitor.getHeat()
            );
        } else {
            double incr =
                    MekanismConfig.general.evaporationHeatDissipation.get()
                            * Math.sqrt(
                            Math.abs(currentTemperature - biomeAmbientTemp)
                    );

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
        long capacity =
                72L * tier.getInputTankCapacity();

        return (int) Math.min(Integer.MAX_VALUE, capacity);
    }

    @NotNull
    @Override
    public IMekanismRecipeTypeProvider<SingleFluidRecipeInput, FluidToFluidRecipe, SingleFluid<FluidToFluidRecipe>> getRecipeType() {
        return MekanismRecipeType.EVAPORATING;
    }

    @Override
    public IRecipeViewerRecipeType<FluidToFluidRecipe> recipeViewerType() {
        return RecipeViewerRecipeType.EVAPORATING;
    }

    @Nullable
    @Override
    public FluidToFluidRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandler);
    }

    @NotNull
    @Override
    public CachedRecipe<FluidToFluidRecipe> createNewCachedRecipe(
            @NotNull FluidToFluidRecipe recipe,
            int cacheIndex
    ) {
        return OneInputCachedRecipe
                .fluidToFluid(
                        recipe,
                        recheckAllRecipeErrors,
                        inputHandler,
                        outputHandler
                )
                .setErrorsChanged(this::onErrorsChanged)
                .setActive(active -> {
                    setActive(active);

                    if (active) {
                        if (tempMultiplier > 0 && tempMultiplier < 1) {
                            lastGain =
                                    1F / Mth.ceil(1 / tempMultiplier);
                        } else {
                            lastGain = tempMultiplier;
                        }
                    } else {
                        lastGain = 0;
                    }
                })
                .setCanHolderFunction(
                        () -> tempMultiplier > 0 && canFunction()
                )
                .setRequiredTicks(
                        () -> tempMultiplier > 0 && tempMultiplier < 1
                                ? Mth.ceil(1 / tempMultiplier)
                                : 1
                )
                .setBaselineMaxOperations(
                        () -> tempMultiplier > 0 && tempMultiplier < 1
                                ? 1
                                : (int) tempMultiplier
                );
    }

    @Override
    public int getRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(
                inputTank.getFluidAmount(),
                inputTank.getCapacity()
        );
    }

    public boolean hasWarning(RecipeError error) {
        return getWarningCheck(error).getAsBoolean();
    }

    @Override
    protected void presetVariables() {
        super.presetVariables();
        tier = Attribute.getTier(
                getBlockHolder(),
                MoreThermalEvaporationTier.class
        );
    }

    @NotNull
    @Override
    public CompoundTag getReducedUpdateTag(@NotNull HolderLookup.Provider provider) {
        CompoundTag updateTag = super.getReducedUpdateTag(provider);

        updateTag.put(SerializationConstants.FLUID, inputTank.getFluid().saveOptional(provider));

        updateTag.putFloat(SerializationConstants.SCALE, prevScale);

        return updateTag;
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.handleUpdateTag(tag, provider);

        NBTUtils.setFluidStackIfPresent(
                provider,
                tag,
                SerializationConstants.FLUID,
                fluid -> inputTank.setStack(fluid)
        );

        NBTUtils.setFloatIfPresent(
                tag,
                SerializationConstants.SCALE,
                scale -> {
                    if (MekanismUtils.scaleChanged(scale, prevScale)) {
                        if (prevScale == 0 || scale == 0) {
                            updateClientLight = true;
                        }

                        prevScale = scale;
                    }
                }
        );
    }

    @Override
    public void parseUpgradeData(HolderLookup.Provider provider, @NotNull IUpgradeData upgradeData) {
        if (upgradeData instanceof MoreThermalEvaporationCompactUpgradeData data) {
            redstone = data.redstone;
            setControlType(data.controlType);

            for (ITileComponent component : getComponents()) {
                component.read(data.components, provider);
            }

            inputTank.deserializeNBT(provider, data.inputTank.serializeNBT(provider));
            outputTank.deserializeNBT(provider, data.outputTank.serializeNBT(provider));
            inputInputSlot.deserializeNBT(provider, data.inputInputSlot.serializeNBT(provider));
            outputInputSlot.deserializeNBT(provider, data.outputInputSlot.serializeNBT(provider));
            inputOutputSlot.deserializeNBT(provider, data.inputOutputSlot.serializeNBT(provider));
            outputOutputSlot.deserializeNBT(provider, data.outputOutputSlot.serializeNBT(provider));
            heatCapacitor.deserializeNBT(provider, data.heatCapacitor.serializeNBT(provider));
        } else {
            super.parseUpgradeData(provider, upgradeData);
        }
    }

    @NotNull
    @Override
    public MoreThermalEvaporationCompactUpgradeData getUpgradeData(HolderLookup.Provider provider) {
        return new MoreThermalEvaporationCompactUpgradeData(
                provider,
                redstone,
                getControlType(),
                inputTank,
                outputTank,
                inputInputSlot,
                outputInputSlot,
                inputOutputSlot,
                outputOutputSlot,
                heatCapacitor,
                getComponents()
        );
    }

    public MoreThermalEvaporationTier getTier() {
        return tier;
    }

    @Override
    public void dump() {
        inputTank.setEmpty();
        outputTank.setEmpty();
    }
}