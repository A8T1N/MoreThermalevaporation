package morethermalevaporation.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import mekanism.api.Upgrade;
import mekanism.api.text.APILang;
import mekanism.api.text.EnumColor;
import morethermalevaporation.common.upgrade.MoreThermalEvaporationAPILang;
import morethermalevaporation.common.upgrade.MoreThermalEvaporationUpgrade;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

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

    @ModifyVariable(method = "buildMap", at = @At(value = "STORE", ordinal = 0), name = "upgrades")
    private static Map<Upgrade, Integer> mte$buildMap(@Nullable Map<Upgrade, Integer> upgrades,
                                                            @Nullable CompoundTag nbtTags) {
        return MoreThermalEvaporationUpgrade.buildMap(upgrades, nbtTags);
    }

    @ModifyExpressionValue(method = "saveMap", at = @At(value = "INVOKE", target = "Ljava/util/Map;entrySet()Ljava/util/Set;"))
    private static Set<Map.Entry<Upgrade, Integer>> mte$saveMap(
            Set<Map.Entry<Upgrade, Integer>> original, @Local(argsOnly = true, name = "arg1") CompoundTag nbtTags) {
        return MoreThermalEvaporationUpgrade.saveMap(original, nbtTags);
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