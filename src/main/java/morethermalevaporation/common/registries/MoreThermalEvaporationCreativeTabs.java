package morethermalevaporation.common.registries;

import morethermalevaporation.MoreThermalevaporation;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MoreThermalEvaporationCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MoreThermalevaporation.MODID);

    public static final RegistryObject<CreativeModeTab> TAB_MORE_THERMAL_EVAPORATION = CREATIVE_TABS.register("tab_more_thermal_evaporation", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.morethermalevaporation.tab"))
                    .icon(() -> new ItemStack(MoreThermalEvaporationBlocks.BASIC_THERMAL_EVAPORATION_BLOCK.asItem()))
                    .displayItems((parameters, output) -> {
                        output.accept(MoreThermalEvaporationBlocks.BASIC_THERMAL_EVAPORATION_BLOCK.getBlock());
                        output.accept(MoreThermalEvaporationBlocks.BASIC_THERMAL_EVAPORATION_VALVE.getBlock());
                        output.accept(MoreThermalEvaporationBlocks.BASIC_THERMAL_EVAPORATION_CONTROLLER.getBlock());

                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}