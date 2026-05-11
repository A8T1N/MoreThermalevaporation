package morethermalevaporation.mixin;

import mekanism.api.Upgrade;
import mekanism.api.text.APILang;
import mekanism.api.text.EnumColor;
import morethermalevaporation.common.upgrade.MoreThermalEvaporationAPILang;
import morethermalevaporation.common.upgrade.MoreThermalEvaporationUpgrade;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = Upgrade.class, remap = false)
public abstract class MixinUpgrade {

    @Shadow
    @Final
    @Mutable
    private static Upgrade[] $VALUES;

    @Shadow
    @Final
    @Mutable
    private static Upgrade[] UPGRADES;

    public MixinUpgrade() {
    }

    @Invoker("<init>")
    public static Upgrade mte$initInvoker(String internalName, int internalId, String name, APILang langKey, APILang descLangKey, int maxStack, EnumColor color) {
        throw new AssertionError();
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void mte$clinit(CallbackInfo ci) {
        MoreThermalEvaporationUpgrade.STRUCTURE = mte$addVariant("STRUCTURE", MoreThermalEvaporationAPILang.UPGRADE_STRUCTURE, MoreThermalEvaporationAPILang.UPGRADE_STRUCTURE_DESCRIPTION, 8, EnumColor.DARK_GREEN);

        UPGRADES = $VALUES;
    }

    @Unique
    private static Upgrade mte$addVariant(String internalName, APILang langKey, APILang descLangKey, int maxStack, EnumColor color) {
        ArrayList<Upgrade> variants = new ArrayList<>(Arrays.asList($VALUES));
        Upgrade upgrade = mte$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                internalName.toLowerCase(),
                langKey,
                descLangKey,
                maxStack,
                color);
        variants.add(upgrade);
        MixinUpgrade.$VALUES = variants.toArray(new Upgrade[0]);
        return upgrade;
    }

}