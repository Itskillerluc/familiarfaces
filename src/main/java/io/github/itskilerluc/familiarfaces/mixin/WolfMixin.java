package io.github.itskilerluc.familiarfaces.mixin;

import io.github.itskilerluc.familiarfaces.server.init.SoundEventRegistry;
import io.github.itskilerluc.familiarfaces.server.util.WolfArmorUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Wolf.class)
public class WolfMixin {
    @Inject(method="getHurtSound", at=@At("HEAD"), cancellable = true)
    protected void familiar_faces$getHurtSound(DamageSource pDamageSource, CallbackInfoReturnable<SoundEvent> cir) {
        if (WolfArmorUtils.canArmorAbsorb(pDamageSource, (Wolf)(Object)this)) {
            cir.setReturnValue(SoundEventRegistry.WOLF_ARMOR_DAMAGE.get());
            cir.cancel();
        }
    }
}
