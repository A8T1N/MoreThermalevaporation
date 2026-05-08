package morethermalevaporation.common.tile.multiblock;

import mekanism.common.tile.interfaces.IHasDumpButton;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.common.content.evaporation.MoreThermalEvaporationMultiblockData;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileEntityMoreThermalEvaporationController extends TileEntityMoreThermalEvaporationBlock implements IHasDumpButton {

    private static final Capability<?>[] caps = {ForgeCapabilities.ITEM_HANDLER};

    public TileEntityMoreThermalEvaporationController(MoreThermalEvaporationTier tier, BlockPos pos, BlockState state) {
        super(MoreThermalEvaporationBlocks.CONTROLLERS.get(tier), pos, state);
        delaySupplier = NO_DELAY;
    }

    @Override
    protected boolean onUpdateServer(MoreThermalEvaporationMultiblockData multiblock) {
        boolean needsPacket = super.onUpdateServer(multiblock);
        setActive(multiblock.isFormed());
        return needsPacket;
    }

    @Override
    public boolean canBeMaster() {
        return true;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        var cap = super.getCapability(capability, side);
        if (MoreThermalEvaporation.PipezLoaded && !cap.isPresent() && ArrayUtils.contains(caps, capability)) {
            return LazyOptional.of(() -> (T) MoreThermalEvaporationPipezHelper.MAP.get(capability));
        }
        return cap;
    }

    @Override
    public void dump() {
        MoreThermalEvaporationMultiblockData multiblock = getMultiblock();
        multiblock.inputTank.setEmpty();    // InputTank
        multiblock.outputTank.setEmpty();   // OutputTank
    }
}