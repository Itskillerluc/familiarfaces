package io.github.itskilerluc.familiarfaces.server.entities;

import io.github.itskilerluc.familiarfaces.server.entities.ai.ProjectileDeflection;
import io.github.itskilerluc.familiarfaces.server.init.EntityTypeRegistry;
import io.github.itskilerluc.familiarfaces.server.init.Tags;
import io.github.itskilerluc.familiarfaces.server.util.AdvancedExplosion;
import io.github.itskilerluc.familiarfaces.server.util.SimpleExplosionDamageCalculator;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public class WindCharge extends AbstractWindCharge {
    @SuppressWarnings("deprecation")
    private static final SimpleExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(
            true, false, Optional.of(1.22F),
            BuiltInRegistries.BLOCK.getTag(Tags.Blocks.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity())
    );
    private static final float RADIUS = 1.2F;
    private int noDeflectTicks = 5;

    public WindCharge(EntityType<? extends AbstractWindCharge> entityType, Level level) {
        super(entityType, level);
    }

    public WindCharge(Player player, Level level, double x, double y, double z) {
        super(EntityTypeRegistry.WIND_CHARGE.get(), level, player, x, y, z);
    }

    public WindCharge(Level level, double x, double y, double z, Vec3 movement) {
        super(EntityTypeRegistry.WIND_CHARGE.get(), x, y, z, movement, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.noDeflectTicks > 0) {
            this.noDeflectTicks--;
        }
    }



    public boolean deflect(ProjectileDeflection deflection, @Nullable Entity entity, @Nullable Entity owner, boolean deflectedByPlayer) {
        return this.noDeflectTicks <= 0 && super.deflect(deflection, entity, owner, deflectedByPlayer);
    }

    @Override
    protected void explode(Vec3 pos) {
        AdvancedExplosion.explode(
                level(),
                this,
                null,
                EXPLOSION_DAMAGE_CALCULATOR,
                pos.x(),
                pos.y(),
                pos.z(),
                RADIUS,
                false,
                Level.ExplosionInteraction.NONE,
                true,
                ParticleTypes.EXPLOSION_EMITTER,
                ParticleTypes.EXPLOSION,
                //todo SoundEvents.WIND_CHARGE_BURST
                SoundEvents.TNT_PRIMED
        );
    }

    @Override
    protected float getEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return 0;
    }
}
