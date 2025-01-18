package io.github.itskillerluc.familiarfaces.server.effect;

import io.github.itskillerluc.familiarfaces.server.entities.AbstractWindCharge;
import io.github.itskillerluc.familiarfaces.server.init.ParticleTypeRegistry;
import io.github.itskillerluc.familiarfaces.server.init.SoundEventRegistry;
import io.github.itskillerluc.familiarfaces.server.util.AdvancedExplosion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class WindChargedMobEffect extends MobEffect {
    public WindChargedMobEffect(MobEffectCategory p_338347_, int p_338254_) {
        super(p_338347_, p_338254_);
    }

    public void onMobRemoved(LivingEntity p_338439_, int amplifier, Entity.RemovalReason p_338258_) {
        if (p_338258_ == Entity.RemovalReason.KILLED && p_338439_.level() instanceof ServerLevel serverlevel) {
            double d2 = p_338439_.getX();
            double d0 = p_338439_.getY() + (double)(p_338439_.getBbHeight() / 2.0F);
            double d1 = p_338439_.getZ();
            float f = 3.0F + p_338439_.getRandom().nextFloat() * 2.0F;
            AdvancedExplosion.explode(
                    serverlevel,
                    p_338439_,
                    null,
                    AbstractWindCharge.EXPLOSION_DAMAGE_CALCULATOR,
                    d2,
                    d0,
                    d1,
                    f,
                    false,
                    Level.ExplosionInteraction.NONE,
                    true,
                    ParticleTypeRegistry.GUST_EMITTER_SMALL.get(),
                    ParticleTypeRegistry.GUST_EMITTER_LARGE.get(),
                    SoundEventRegistry.BREEZE_WIND_CHARGE_BURST.get(),
                    true
            );
        }
    }
}
