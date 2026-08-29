package morethermalevaporation.common.registries;

import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MoreThermalEvaporationCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MoreThermalEvaporation.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_MORE_THERMAL_EVAPORATION =
            CREATIVE_TABS.register("tab_more_thermal_evaporation", () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("creativetab.morethermalevaporation.tab"))
                            .icon(() -> new ItemStack(
                                    MoreThermalEvaporationBlocks.BLOCKS
                                            .get(MoreThermalEvaporationTier.BASIC)
                                            .asItem()
                            ))
                            .displayItems((parameters, output) -> {
                                for (MoreThermalEvaporationTier tier : MoreThermalEvaporationTier.values()) {
                                    output.accept(MoreThermalEvaporationBlocks.BLOCKS.get(tier));
                                    output.accept(MoreThermalEvaporationBlocks.VALVES.get(tier));
                                    output.accept(MoreThermalEvaporationBlocks.CONTROLLERS.get(tier));
                                    output.accept(MoreThermalEvaporationBlocks.COMPACTS.get(tier));
                                }
                            })
                            .build()
            );

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}