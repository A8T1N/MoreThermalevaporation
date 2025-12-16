package morethermalevaporation.client;

import mekanism.client.ClientRegistrationUtil;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.client.gui.GuiAdvancedThermalEvaporationController;
import morethermalevaporation.client.gui.GuiBasicThermalEvaporationController;
import morethermalevaporation.client.gui.GuiEliteThermalEvaporationController;
import morethermalevaporation.client.gui.GuiUltimateThermalEvaporationController;
import morethermalevaporation.client.render.tileentity.RenderAdvancedThermalEvaporationPlant;
import morethermalevaporation.client.render.tileentity.RenderBasicThermalEvaporationPlant;
import morethermalevaporation.client.render.tileentity.RenderEliteThermalEvaporationPlant;
import morethermalevaporation.client.render.tileentity.RenderUltimateThermalEvaporationPlant;
import morethermalevaporation.common.registries.MoreThermalEvaporationContainerTypes;
import morethermalevaporation.common.registries.MoreThermalEvaporationTileEntityTypes;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = MoreThermalEvaporation.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistration {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(MoreThermalEvaporationTileEntityTypes.BASIC_THERMAL_EVAPORATION_CONTROLLER.get(), RenderBasicThermalEvaporationPlant::new);
        event.registerBlockEntityRenderer(MoreThermalEvaporationTileEntityTypes.ADVANCED_THERMAL_EVAPORATION_CONTROLLER.get(), RenderAdvancedThermalEvaporationPlant::new);
        event.registerBlockEntityRenderer(MoreThermalEvaporationTileEntityTypes.ELITE_THERMAL_EVAPORATION_CONTROLLER.get(), RenderEliteThermalEvaporationPlant::new);
        event.registerBlockEntityRenderer(MoreThermalEvaporationTileEntityTypes.ULTIMATE_THERMAL_EVAPORATION_CONTROLLER.get(), RenderUltimateThermalEvaporationPlant::new);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerContainers(RegisterEvent event) {
        event.register(Registries.MENU, helper -> {
            ClientRegistrationUtil.registerScreen(MoreThermalEvaporationContainerTypes.BASIC_THERMAL_EVAPORATION_CONTROLLER, GuiBasicThermalEvaporationController::new);
            ClientRegistrationUtil.registerScreen(MoreThermalEvaporationContainerTypes.ADVANCED_THERMAL_EVAPORATION_CONTROLLER, GuiAdvancedThermalEvaporationController::new);
            ClientRegistrationUtil.registerScreen(MoreThermalEvaporationContainerTypes.ELITE_THERMAL_EVAPORATION_CONTROLLER, GuiEliteThermalEvaporationController::new);
            ClientRegistrationUtil.registerScreen(MoreThermalEvaporationContainerTypes.ULTIMATE_THERMAL_EVAPORATION_CONTROLLER, GuiUltimateThermalEvaporationController::new);
        });
    }
}