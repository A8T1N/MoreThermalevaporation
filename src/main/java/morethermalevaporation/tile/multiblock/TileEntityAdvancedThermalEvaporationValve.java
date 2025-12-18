package morethermalevaporation.tile.multiblock;

import mekanism.api.Action;
import mekanism.api.IContentsListener;
import mekanism.api.text.TextComponentUtil;
import mekanism.api.tier.BaseTier;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.tile.base.SubstanceType;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class TileEntityAdvancedThermalEvaporationValve extends TileEntityAdvancedThermalEvaporationBlock {

    public TileEntityAdvancedThermalEvaporationValve(BlockPos pos, BlockState state) {
        super(MoreThermalEvaporationBlocks.ADVANCED_THERMAL_EVAPORATION_VALVE, pos, state);
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
        //But that we do not handle fluid when it comes to syncing it/saving this tile to disk
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

    @Override
    public @NotNull Component getDisplayName() {
        return TextComponentUtil.color(super.getDisplayName().copy(), BaseTier.ADVANCED.getColor().getValue());
    }
}