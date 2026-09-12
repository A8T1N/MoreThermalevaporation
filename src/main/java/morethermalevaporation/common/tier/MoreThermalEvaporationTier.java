package morethermalevaporation.common.tier;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.tier.BaseTier;
import mekanism.api.tier.ITier;
import mekanism.common.config.value.CachedDoubleValue;
import mekanism.common.config.value.CachedIntValue;
import org.jetbrains.annotations.UnknownNullability;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

@NothingNullByDefault
public enum MoreThermalEvaporationTier implements ITier {
    BASIC(() -> BaseTier.BASIC, 6_000, 18, 64000, 20000),
    ADVANCED(() -> BaseTier.ADVANCED, 12_000, 18, 64000, 80000),
    ELITE(() -> BaseTier.ELITE, 24_000, 18, 64000, 640000),
    ULTIMATE(() -> BaseTier.ULTIMATE, 48_000, 18, 64000, 10240000),

    // Evolved Mekanism
    OVERCLOCKED(() -> getOptionalBaseTier("OVERCLOCKED"), 96_000, 18, 128000, 20480000),
    QUANTUM(() -> getOptionalBaseTier("QUANTUM"), 192_000, 18, 256000, 40960000),
    DENSE(() -> getOptionalBaseTier("DENSE"), 384_000, 18, 512000, 81920000),
    MULTIVERSAL(() -> getOptionalBaseTier("MULTIVERSAL"), 768_000, 18, 1024000, 163840000),

    CREATIVE(() -> BaseTier.CREATIVE, Integer.MAX_VALUE, 18, Integer.MAX_VALUE, Integer.MAX_VALUE),
    ;

    private final Supplier<BaseTier> baseTier;
    private final double baseMultiplierTemp;
    private final int baseHeight;
    private final int baseInputTankCapacity;
    private final int baseOutputTankCapacity;

    @Nullable
    private CachedDoubleValue multiplierTempReference;
    @Nullable
    private CachedIntValue heightReference;
    @Nullable
    private CachedIntValue inputTankCapacityReference;
    @Nullable
    private CachedIntValue outputTankCapacityReference;

    MoreThermalEvaporationTier(@UnknownNullability Supplier<BaseTier> baseTier, double baseMultiplierTemp, int baseHeight, int baseInputTankCapacity, int baseOutputTankCapacity) {
        this.baseTier = baseTier;
        this.baseMultiplierTemp = baseMultiplierTemp;
        this.baseHeight = baseHeight;
        this.baseInputTankCapacity = baseInputTankCapacity;
        this.baseOutputTankCapacity = baseOutputTankCapacity;
    }

    @Override
    public BaseTier getBaseTier() {
        return baseTier.get();
    }

    public double getMultiplierTemp() {
        return multiplierTempReference == null ? getBaseMultiplierTemp() : multiplierTempReference.getOrDefault();
    }

    public int getHeight() {
        return heightReference == null ? getBaseHeight() : heightReference.getOrDefault();
    }

    public int getInputTankCapacity() {
        return inputTankCapacityReference == null ? getBaseInputTankCapacity() : inputTankCapacityReference.getOrDefault();
    }

    public int getOutputTankCapacity() {
        return outputTankCapacityReference == null ? getBaseOutputTankCapacity() : outputTankCapacityReference.getOrDefault();
    }

    public double getBaseMultiplierTemp() {
        return baseMultiplierTemp;
    }

    public int getBaseHeight() {
        return baseHeight;
    }

    public int getBaseInputTankCapacity() {
        return baseInputTankCapacity;
    }

    public int getBaseOutputTankCapacity() {
        return baseOutputTankCapacity;
    }

    public static Stream<MoreThermalEvaporationTier> availableTiers() {
        return Arrays.stream(values())
                .filter(MoreThermalEvaporationTier::isAvailable);
    }

    public boolean isAvailable() {
        return Arrays.stream(BaseTier.values())
                .anyMatch(tier -> tier.name().equals(name()));
    }

    private static BaseTier getOptionalBaseTier(String tierName) {
        return BaseTier.valueOf(tierName.toUpperCase());
    }

    public static Optional<MoreThermalEvaporationTier> getUpgradeTarget(MoreThermalEvaporationTier tier) {
        MoreThermalEvaporationTier evolvedTarget = EVOLVED_UPGRADES.get(tier);
        if (evolvedTarget != null && evolvedTarget.isAvailable()) {
            return Optional.of(evolvedTarget);
        }

        MoreThermalEvaporationTier baseTarget = BASE_UPGRADES.get(tier);
        if (baseTarget != null && baseTarget.isAvailable()) {
            return Optional.of(baseTarget);
        }

        return Optional.empty();
    }

    /**
     * Mekanism Tier TO FROM
     */
    private static final Map<MoreThermalEvaporationTier, MoreThermalEvaporationTier> BASE_UPGRADES = Map.of(
            BASIC, ADVANCED,
            ADVANCED, ELITE,
            ELITE, ULTIMATE,
            ULTIMATE, CREATIVE
    );

    /**
     * Evolved Mekanism Tier TO FROM
     */
    private static final Map<MoreThermalEvaporationTier, MoreThermalEvaporationTier> EVOLVED_UPGRADES = Map.of(
            ULTIMATE, OVERCLOCKED,
            OVERCLOCKED, QUANTUM,
            QUANTUM, DENSE,
            DENSE, MULTIVERSAL,
            MULTIVERSAL, CREATIVE
    );

    /**
     * ONLY CALL THIS FROM TierConfig. It is used to give the FluidTankTier a reference to the actual config value object
     */
    public void setConfigReference(CachedDoubleValue multiplierTempReference, CachedIntValue heightReference, CachedIntValue inputTankCapacityReference, CachedIntValue outputTankCapacityReference) {
        this.multiplierTempReference = multiplierTempReference;
        this.heightReference = heightReference;
        this.inputTankCapacityReference = inputTankCapacityReference;
        this.outputTankCapacityReference = outputTankCapacityReference;
    }
}
