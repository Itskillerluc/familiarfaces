package io.github.itskilerluc.familiarfaces.mixin;

import com.github.alexthe668.domesticationinnovation.server.entity.TameableUtils;
import io.github.itskilerluc.familiarfaces.server.capability.WolfArmorCapabilityProvider;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TameableUtils.class)
public class TameableUtilsMixin {
    @Inject(method = "getEnchantmentList", at = @At("RETURN"), cancellable = true, remap = false)
    private static void familiar_faces$getEnchantmentList(LivingEntity entity, CallbackInfoReturnable<ListTag> cir) {
        ListTag tag = cir.getReturnValue();
        if (tag == null) {
            tag = new ListTag();
        }
        var cap = entity.getCapability(WolfArmorCapabilityProvider.WOLF_ARMOR_CAPABILITY);
        if (cap.isPresent()) {
            tag.addAll(cap.orElseThrow(NullPointerException::new).getBodyArmorItem().getEnchantmentTags());
        }
        cir.setReturnValue(tag);
        cir.cancel();
    }
}
