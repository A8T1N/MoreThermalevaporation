package morethermalevaporation.client.jei;

import mekanism.client.recipe_viewer.jei.CatalystRegistryHelper;
import mekanism.client.recipe_viewer.jei.MekanismJEI;
import mekanism.client.recipe_viewer.type.RecipeViewerRecipeType;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import morethermalevaporation.MoreThermalEvaporation;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class MoreThermalEvaporationJEI implements IModPlugin {

    public MoreThermalEvaporationJEI() {
    }

    @Override
    public ResourceLocation getPluginUid() {
        return MoreThermalEvaporation.rl("jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        if (!MekanismJEI.shouldLoad()) {
            return;
        }

        CatalystRegistryHelper.register(
                registry,
                MekanismJEI.genericRecipeType(RecipeViewerRecipeType.EVAPORATING),
                getWorkstations()
        );

        // ↓↓↓ Just Enough Mekanism Multiblocks連携
        if (MoreThermalEvaporation.JustEnoughMekanismMultiblocksLoaded) {
            MoreThermalEvaporationJEIHelper.registerRecipeCatalysts(registry);
        }
    }

    // ↓↓↓ Just Enough Mekanism Multiblocks連携 ↓↓↓
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        if (MoreThermalEvaporation.JustEnoughMekanismMultiblocksLoaded) {
            MoreThermalEvaporationJEIHelper.registerCategories(registry);
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        if (MoreThermalEvaporation.JustEnoughMekanismMultiblocksLoaded) {
            MoreThermalEvaporationJEIHelper.registerRecipes(registry);
        }
    }

    private static List<ItemLike> getWorkstations() {
        List<ItemLike> workstations = new ArrayList<>();

        for (MoreThermalEvaporationTier tier : MoreThermalEvaporationTier.values()) {
            workstations.add(MoreThermalEvaporationBlocks.CONTROLLERS.get(tier));
            workstations.add(MoreThermalEvaporationBlocks.VALVES.get(tier));
            workstations.add(MoreThermalEvaporationBlocks.BLOCKS.get(tier));
            workstations.add(MoreThermalEvaporationBlocks.COMPACTS.get(tier));
        }

        return workstations;
    }

}
