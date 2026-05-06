package morethermalevaporation.mixin;

import mekanism.api.text.APILang;
import mekanism.api.text.ILangEntry;
import morethermalevaporation.common.upgrade.MoreThermalEvaporationAPILang;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(value = APILang.class, remap = false)
public abstract class MixinAPILang implements ILangEntry {

    @Shadow(remap = false)
    @Final
    @Mutable
    private static APILang[] $VALUES;

    public MixinAPILang() {
    }

    @Invoker("<init>")
    public static APILang mte$initInvoker(String internalName, int internalId, String type, String path) {
        throw new AssertionError();
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void mte$client(CallbackInfo ci) {
        MoreThermalEvaporationAPILang.UPGRADE_STRUCTURE = mte$addVariant("UPGRADE_STRUCTURE", "upgrade", "structure");
        MoreThermalEvaporationAPILang.UPGRADE_STRUCTURE_DESCRIPTION = mte$addVariant("UPGRADE_STRUCTURE_DESCRIPTION", "upgrade", "structure.description");
    }

    @Unique
    private static APILang mte$addVariant(String internalName, String type, String path) {
        ArrayList<APILang> variants = new ArrayList<>(Arrays.asList($VALUES));
        APILang upgrade = mte$initInvoker(internalName,
                variants.get(variants.size() - 1).ordinal() + 1,
                type,
                path);
        variants.add(upgrade);
        $VALUES = variants.toArray(new APILang[0]);
        return upgrade;
    }
}