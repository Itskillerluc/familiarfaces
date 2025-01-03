package io.github.itskilerluc.familiarfaces.mixin;

import com.github.alexthe668.domesticationinnovation.server.enchantment.PetEnchantment;
import io.github.itskilerluc.familiarfaces.server.init.ItemRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
    @Inject(method = "canEnchant" , at = @At("HEAD"), cancellable = true)
    private void familiar_faces$canEnchant(ItemStack pStack, CallbackInfoReturnable<Boolean> cir) {
        if ((Enchantment) (Object) this instanceof PetEnchantment) {
            if (pStack.getItem().getEquipmentSlot(pStack) == EquipmentSlot.CHEST) {
                if (pStack.getItem().equals(ItemRegistry.WOLF_ARMOR.get())) {
                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                }
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
}
