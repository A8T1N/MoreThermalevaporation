package morethermalevaporation.common.tile.multiblock;

import mekanism.api.Action;
import mekanism.api.IContentsListener;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.tile.base.SubstanceType;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileEntityMoreThermalEvaporationValve extends TileEntityMoreThermalEvaporationBlock {

    private static final Capability<?>[] caps = {
            ForgeCapabilities.ITEM_HANDLER, ForgeCapabilities.FLUID_HANDLER
    };

    public TileEntityMoreThermalEvaporationValve(MoreThermalEvaporationTier tier, BlockPos pos, BlockState state) {
        super(MoreThermalEvaporationBlocks.VALVES.get(tier), pos, state);
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener) {
        return side -> getMultiblock().getFluidTanks(side);
    }

    @NotNull
    @Override
    protected IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener, CachedAmbientTemperature ambientTemperature) {
        return side -> getMultiblock().getHeatCapacitors(side);
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        return side -> getMultiblock().getInventorySlots(side);
    }

    @Override
    public boolean persists(SubstanceType type) {
        if (type == SubstanceType.FLUID || type == SubstanceType.HEAT) {
            return false;
        }
        return super.persists(type);
    }

    @NotNull
    @Override
    public FluidStack insertFluid(@NotNull FluidStack stack, Direction side, @NotNull Action action) {
        FluidStack ret = super.insertFluid(stack, side, action);
        if (ret.getAmount() < stack.getAmount() && action.execute()) {
            getMultiblock().triggerValveTransfer(this);
        }
        return ret;
    }

    @Override
    public int getRedstoneLevel() {
        return getMultiblock().getCurrentRedstoneLevel();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        LazyOptional<T> cap = super.getCapability(capability, side);
        if (MoreThermalEvaporation.PipezLoaded && !cap.isPresent() && ArrayUtils.contains(caps, capability)) {
            return LazyOptional.of(() -> (T) MoreThermalEvaporationPipezHelper.MAP.get(capability));
        }
        return cap;
    }
}