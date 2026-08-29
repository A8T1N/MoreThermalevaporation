package morethermalevaporation.common.config;

import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedBooleanValue;
import mekanism.common.config.value.CachedDoubleValue;
import mekanism.common.config.value.CachedIntValue;
import morethermalevaporation.common.content.evaporation.MoreThermalEvaporationType;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Locale;

public class MoreThermalEvaporationPlantConfig extends BaseMekanismConfig {
    private final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
    public final CachedBooleanValue renderFluid;
    public final CachedBooleanValue enabledLargeType;
    private final ModConfigSpec configSpec;

    MoreThermalEvaporationPlantConfig() {

        // Tier settings
        builder.comment("More Thermal Evaporations Settings").push("more_thermal_evaporation");
        addMoreThermalEvaporationCategory();

        // Render settings
        builder.comment("Settings for the Render fluid");
        renderFluid = CachedBooleanValue.wrap(this, builder.comment("Render fluid inside of More Thermal Evaporation Plants.").define("RenderFluid", true));

        // Type settings
        builder.comment("Settings for the Type");
        enabledLargeType = CachedBooleanValue.wrap(this, builder.comment("Enable Large More Thermal Evaporation Plants.").define("EnabledLargeType", true));
        CachedIntValue multiplierReference = CachedIntValue.wrap(this, builder.comment("Performance multiplier for Large More Thermal Evaporation Plants.").defineInRange("LargeMultiplier", MoreThermalEvaporationType.LARGE.getBaseMultiplier(), 1, 2147483646));
        MoreThermalEvaporationType.LARGE.setConfigReference(multiplierReference);

        builder.pop();

        configSpec = builder.build();
    }

    private void addMoreThermalEvaporationCategory() {
        for (MoreThermalEvaporationTier tier : MoreThermalEvaporationTier.values()) {
            String tierName = tier.getBaseTier().getSimpleName();
            CachedDoubleValue multiplierTempReference = CachedDoubleValue.wrap(this, builder.comment("Maximum " + "temperature capping the temperature multiplier for the " + tierName + " Thermal Evaporation Plant.")
                    .defineInRange(tierName.toLowerCase(Locale.ROOT) + "ThermalEvaporationMultiplierTempCap",
                            tier.getBaseMultiplierTemp(), 3000, 2147483646));

            CachedIntValue heightReference = CachedIntValue.wrap(this, builder.comment("Buildable Height (in blocks) for the " + tierName + " Thermal Evaporation Plant.")
                    .defineInRange(tierName.toLowerCase(Locale.ROOT) + "ThermalEvaporationHeight", tier.getBaseHeight(), 18, 2147483646));

            CachedIntValue inputTankCapacityReference = CachedIntValue.wrap(this, builder.comment("Amount of fluid (mB) that each block of the " + tierName + " Thermal Evaporation Plant contributes to the input tank capacity. Max = volume * fluidPerTank")
                    .defineInRange(tierName.toLowerCase(Locale.ROOT) + "ThermalEvaporationInputTankCapacity", tier.getBaseInputTankCapacity(), 1, 29826161));

            CachedIntValue outputTankCapacityReference = CachedIntValue.wrap(this, builder.comment("Amount of output fluid (mB) that the " + tierName + " Thermal Evaporation Plant can store.")
                    .defineInRange(tierName.toLowerCase(Locale.ROOT) + "ThermalEvaporationOutputTankCapacity", tier.getBaseOutputTankCapacity(), 1, 2147483646));

            tier.setConfigReference(multiplierTempReference, heightReference, inputTankCapacityReference, outputTankCapacityReference);
        }
    }

    @Override
    public String getFileName() {
        return "more-thermal-evaporations";
    }

    @Override
    public String getTranslation() {
        return "More Thermal Evaporation Config";
    }

    @Override
    public ModConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public Type getConfigType() {
        return Type.COMMON;
    }
}