package morethermalevaporation.common.tile.multiblock;

import de.maxhenkel.pipez.utils.DummyFluidHandler;
import de.maxhenkel.pipez.utils.DummyItemHandler;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.HashMap;
import java.util.Map;

public class MoreThermalEvaporationPipezHelper {

    public MoreThermalEvaporationPipezHelper() {
    }

    public static Map<Capability<?>, Object> MAP = new HashMap<>();

    static {
        MAP.put(ForgeCapabilities.ITEM_HANDLER, DummyItemHandler.INSTANCE);
        MAP.put(ForgeCapabilities.FLUID_HANDLER, DummyFluidHandler.INSTANCE);
    }
}
