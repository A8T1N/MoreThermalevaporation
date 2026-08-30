package morethermalevaporation.client.jei;

import giselle.jei_mekanism_multiblocks.client.JEI_MekanismMultiblocks_Client;
import giselle.jei_mekanism_multiblocks.client.SavedData;
import giselle.jei_mekanism_multiblocks.client.jei.MultiblockCategory;
import giselle.jei_mekanism_multiblocks.client.jei.MultiblockWidget;
import giselle.jei_mekanism_multiblocks.common.JEI_MekanismMultiblocks;
import giselle.jei_mekanism_multiblocks.common.config.ClientConfig;
import giselle.jei_mekanism_multiblocks.common.config.JEI_MekanismMultiblocks_Config;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import morethermalevaporation.client.jei.category.MoreEvaporationPlantCategory;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class MoreThermalEvaporationJEIHelper {
    private static final List<MultiblockCategory<? extends MultiblockWidget>> categories = new ArrayList<>();

    public static void registerCategories(IRecipeCategoryRegistration registry) {

        ClientConfig config = JEI_MekanismMultiblocks_Config.CLIENT;
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        categories.clear();
        addCategory(config.evaporationPlantVisible, () -> new MoreEvaporationPlantCategory(guiHelper, MoreThermalEvaporationTier.BASIC, MoreEvaporationPlantCategory.BasicEvaporationPlantWidget.class));
        addCategory(config.evaporationPlantVisible, () -> new MoreEvaporationPlantCategory(guiHelper, MoreThermalEvaporationTier.ADVANCED, MoreEvaporationPlantCategory.AdvancedEvaporationPlantWidget.class));
        addCategory(config.evaporationPlantVisible, () -> new MoreEvaporationPlantCategory(guiHelper, MoreThermalEvaporationTier.ELITE, MoreEvaporationPlantCategory.EliteEvaporationPlantWidget.class));
        addCategory(config.evaporationPlantVisible, () -> new MoreEvaporationPlantCategory(guiHelper, MoreThermalEvaporationTier.ULTIMATE, MoreEvaporationPlantCategory.UltimateEvaporationPlantWidget.class));
        addCategory(config.evaporationPlantVisible, () -> new MoreEvaporationPlantCategory(guiHelper, MoreThermalEvaporationTier.CREATIVE, MoreEvaporationPlantCategory.CreativeEvaporationPlantWidget.class));
        for (MultiblockCategory<?> category : getCategories()) {
            registry.addRecipeCategories(category);
        }
    }

    private static <CATEGORY extends MultiblockCategory<?>> void addCategory(ModConfigSpec.BooleanValue config, Supplier<CATEGORY> constructor) {
        if (config.get()) {
            categories.add(constructor.get());
        }

    }

    public static void registerRecipes(IRecipeRegistration registry) {
        if (JEI_MekanismMultiblocks.EMILoaded) {
            return;
        }

        for (MultiblockCategory<?> category : getCategories()) {
            @SuppressWarnings("unchecked")
            RecipeType<MultiblockWidget> recipeType = (RecipeType<MultiblockWidget>) category.getRecipeType();
            registry.addRecipes(recipeType, Arrays.asList(createWidget(category)));
        }
    }

    public static <WIDGET extends MultiblockWidget> WIDGET createWidget(MultiblockCategory<WIDGET> category) {
        try {
            RecipeType<WIDGET> recipeType = category.getRecipeType();
            WIDGET widget = recipeType.getRecipeClass().getDeclaredConstructor().newInstance();

            if (SavedData.hasMultiblock(recipeType.getUid())) {
                widget.load(SavedData.getMultiblock(recipeType.getUid()));
            }

            widget.addChangedHandler(w -> onWidgetChanged(category, widget));
            return widget;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException("Category: " + category.getRecipeType(), e);
        }

    }

    private static void onWidgetChanged(MultiblockCategory<?> category, MultiblockWidget widget) {
        CompoundTag tag = new CompoundTag();
        widget.save(tag);

        SavedData.setMultiblockData(category.getRecipeType().getUid(), tag);
        JEI_MekanismMultiblocks_Client.markNeedSave();
    }

    public static List<MultiblockCategory<? extends MultiblockWidget>> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public static void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        for (MultiblockCategory<?> category : getCategories()) {
            category.registerRecipeCatalysts(registry);
        }
    }
}
