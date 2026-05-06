package morethermalevaporation.mixin;

import mekanism.api.Upgrade;
import mekanism.common.util.UpgradeUtils;
import morethermalevaporation.common.registries.MoreThermalEvaporationItems;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = UpgradeUtils.class, remap = false)
public class MixinUpgradeUtils {

    @Inject(method = "getStack(Lmekanism/api/Upgrade;I)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "HEAD"), cancellable = true)
    private static void getItem(Upgrade upgrade, int count, CallbackInfoReturnable<ItemStack> cir) {
        switch (upgrade.toString()) {
            case "STRUCTURE" -> cir.setReturnValue(MoreThermalEvaporationItems.STRUCTURE_UPGRADE.getItemStack(count));
        }
    }
}