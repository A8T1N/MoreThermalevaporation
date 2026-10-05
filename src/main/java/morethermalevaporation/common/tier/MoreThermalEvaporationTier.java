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
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NothingNullByDefault
public enum MoreThermalEvaporationTier implements ITier {
    BASIC(() -> BaseTier.BASIC, 6_000, 18, 64_000, 20_000),
    ADVANCED(() -> BaseTier.ADVANCED, 12_000, 18, 64_000, 80_000),
    ELITE(() -> BaseTier.ELITE, 24_000, 18, 64_000, 640_000),
    ULTIMATE(() -> BaseTier.ULTIMATE, 48_000, 18, 64_000, 10_240_000),

    // Evolved Mekanism
    OVERCLOCKED(() -> getAddonTier("OVERCLOCKED"), 96_000, 18, 128_000, 20_480_000),
    QUANTUM(() -> getAddonTier("QUANTUM"), 192_000, 18, 256_000, 40_960_000),
    DENSE(() -> getAddonTier("DENSE"), 384_000, 18, 512_000, 81_920_000),
    MULTIVERSAL(() -> getAddonTier("MULTIVERSAL"), 768_000, 18, 1_024_000, 163_840_000),

    CREATIVE(() -> BaseTier.CREATIVE, Integer.MAX_VALUE, 18, Integer.MAX_VALUE, Integer.MAX_VALUE),
    ;

    private static final Set<String> BASE_TIER_NAMES = Arrays.stream(BaseTier.values())
            .map(BaseTier::name)
            .collect(Collectors.toUnmodifiableSet());

    /**
     * Mekanism Tier FROM -> TO
     */
    private static final Map<MoreThermalEvaporationTier, MoreThermalEvaporationTier> BASE_UPGRADES = Map.of(
            BASIC, ADVANCED,
            ADVANCED, ELITE,
            ELITE, ULTIMATE,
            ULTIMATE, CREATIVE
    );

    /**
     * Evolved Mekanism Tier FROM -> TO
     */
    private static final Map<MoreThermalEvaporationTier, MoreThermalEvaporationTier> EVOLVED_UPGRADES = Map.of(
            ULTIMATE, OVERCLOCKED,
            OVERCLOCKED, QUANTUM,
            QUANTUM, DENSE,
            DENSE, MULTIVERSAL,
            MULTIVERSAL, CREATIVE
    );

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
        return BASE_TIER_NAMES.contains(name());
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
     * ONLY CALL THIS FROM TierConfig. It is used to give the MoreThermalEvaporationTier a reference to the actual config value object.
     */
    public void setConfigReference(CachedDoubleValue multiplierTempReference, CachedIntValue heightReference, CachedIntValue inputTankCapacityReference, CachedIntValue outputTankCapacityReference) {
        this.multiplierTempReference = multiplierTempReference;
        this.heightReference = heightReference;
        this.inputTankCapacityReference = inputTankCapacityReference;
        this.outputTankCapacityReference = outputTankCapacityReference;
    }

    private static BaseTier getAddonTier(String tierName) {
        return BaseTier.valueOf(tierName);
    }
}
