package morethermalevaporation.client;

import mekanism.client.ClientRegistrationUtil;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.client.gui.GuiMoreThermalEvaporationCompact;
import morethermalevaporation.client.gui.GuiMoreThermalEvaporationController;
import morethermalevaporation.client.render.tileentity.RenderMoreThermalEvaporationCompact;
import morethermalevaporation.client.render.tileentity.RenderMoreThermalEvaporationPlant;
import morethermalevaporation.common.registries.MoreThermalEvaporationContainerTypes;
import morethermalevaporation.common.registries.MoreThermalEvaporationTileEntityTypes;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;

@EventBusSubscriber(modid = MoreThermalEvaporation.MODID, value = Dist.CLIENT)
public class ClientRegistration {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        for (MoreThermalEvaporationTier tier : MoreThermalEvaporationTier.values()) {
            event.registerBlockEntityRenderer(MoreThermalEvaporationTileEntityTypes.CONTROLLERS.get(tier).get(), (context) -> new RenderMoreThermalEvaporationPlant(tier, context));
            event.registerBlockEntityRenderer(MoreThermalEvaporationTileEntityTypes.COMPACTS.get(tier).get(), (context) -> new RenderMoreThermalEvaporationCompact(tier, context));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerContainers(RegisterMenuScreensEvent event) {
        for (MoreThermalEvaporationTier tier : MoreThermalEvaporationTier.values()) {
            ClientRegistrationUtil.registerScreen(event, MoreThermalEvaporationContainerTypes.MORE_THERMAL_EVAPORATION_CONTROLLER.get(tier), GuiMoreThermalEvaporationController::new);
            ClientRegistrationUtil.registerScreen(event, MoreThermalEvaporationContainerTypes.MORE_THERMAL_EVAPORATION_COMPACT.get(tier), GuiMoreThermalEvaporationCompact::new);
        }
    }

    @SubscribeEvent
    public static void onStitch(TextureAtlasStitchedEvent event) {
        RenderMoreThermalEvaporationCompact.resetCachedModels();
    }
}