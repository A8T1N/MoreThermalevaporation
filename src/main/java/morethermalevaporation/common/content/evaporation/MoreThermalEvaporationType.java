package morethermalevaporation.common.content.evaporation;

import mekanism.common.config.value.CachedIntValue;
import morethermalevaporation.common.MoreThermalEvaporationLang;

import javax.annotation.Nullable;

public enum MoreThermalEvaporationType {
    NORMAL(MoreThermalEvaporationLang.TYPE_NORMAL, 1, 2),
    LARGE(MoreThermalEvaporationLang.TYPE_LARGE, 6, 7);

    private final MoreThermalEvaporationLang lang;
    private final int baseMultiplier;
    private final int renderSize;

    @Nullable
    private CachedIntValue multiplierReference;

    MoreThermalEvaporationType(MoreThermalEvaporationLang lang, int baseMultiplier, int renderSize) {
        this.lang = lang;
        this.baseMultiplier = baseMultiplier;
        this.renderSize = renderSize;
    }

    public MoreThermalEvaporationLang getLang() {
        return lang;
    }

    public int getMultiplier() {
        return multiplierReference == null ? getBaseMultiplier() : multiplierReference.getOrDefault();
    }

    public int getBaseMultiplier() {
        return baseMultiplier;
    }

    public int getRenderSize() {
        return renderSize;
    }

    public void setConfigReference(CachedIntValue multiplierReference) {
        this.multiplierReference = multiplierReference;
    }
}