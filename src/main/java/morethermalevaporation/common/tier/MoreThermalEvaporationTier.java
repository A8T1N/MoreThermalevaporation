package morethermalevaporation.common.tier;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.tier.BaseTier;
import mekanism.api.tier.ITier;
import mekanism.common.config.value.CachedDoubleValue;
import mekanism.common.config.value.CachedIntValue;

import javax.annotation.Nullable;

@NothingNullByDefault
public enum MoreThermalEvaporationTier implements ITier {
    BASIC(BaseTier.BASIC, 6_000, 18, 64000, 20000),
    ADVANCED(BaseTier.ADVANCED, 12_000, 18, 64000, 80000),
    ELITE(BaseTier.ELITE, 24_000, 18, 64000, 640000),
    ULTIMATE(BaseTier.ULTIMATE, 48_000, 18, 64000, 10240000),
    CREATIVE(BaseTier.CREATIVE, Integer.MAX_VALUE, 18, Integer.MAX_VALUE, Integer.MAX_VALUE);

    private final BaseTier baseTier;
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

    MoreThermalEvaporationTier(BaseTier baseTier, double baseMultiplierTemp, int baseHeight, int baseInputTankCapacity, int baseOutputTankCapacity) {
        this.baseTier = baseTier;
        this.baseMultiplierTemp = baseMultiplierTemp;
        this.baseHeight = baseHeight;
        this.baseInputTankCapacity = baseInputTankCapacity;
        this.baseOutputTankCapacity = baseOutputTankCapacity;
    }

    @Override
    public BaseTier getBaseTier() {
        return baseTier;
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
