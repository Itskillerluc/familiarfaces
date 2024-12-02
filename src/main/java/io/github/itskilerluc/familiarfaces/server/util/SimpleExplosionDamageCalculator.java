package io.github.itskilerluc.familiarfaces.server.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class SimpleExplosionDamageCalculator extends ExplosionDamageCalculator {
    private final boolean explodesBlocks;
    public final boolean damagesEntities;
    private final Optional<Float> knockbackMultiplier;
    private final Optional<HolderSet<Block>> immuneBlocks;

    public SimpleExplosionDamageCalculator(boolean explodesBlocks, boolean damagesEntities, Optional<Float> knockbackMultiplier, Optional<HolderSet<Block>> immuneBlocks) {
        this.explodesBlocks = explodesBlocks;
        this.damagesEntities = damagesEntities;
        this.knockbackMultiplier = knockbackMultiplier;
        this.immuneBlocks = immuneBlocks;
    }

    @Override
    public Optional<Float> getBlockExplosionResistance(
            Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, FluidState fluid
    ) {
        if (this.immuneBlocks.isPresent()) {
            return state.is(this.immuneBlocks.get()) ? Optional.of(3600000.0F) : Optional.empty();
        } else {
            return super.getBlockExplosionResistance(explosion, reader, pos, state, fluid);
        }
    }

    @Override
    public boolean shouldBlockExplode(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, float power) {
        return this.explodesBlocks;
    }

    public float getEntityDamageAmount(AdvancedExplosion explosion, Entity entity) {
        float f = explosion.radius * 2.0F;
        Vec3 vec3 = explosion.getPosition();
        double d0 = Math.sqrt(entity.distanceToSqr(vec3)) / (double)f;
        double d1 = (1.0 - d0) * (double)Explosion.getSeenPercent(vec3, entity);
        return (float)((d1 * d1 + d1) / 2.0 * 7.0 * (double)f + 1.0);
    }

    public float getKnockbackMultiplier(Entity entity) {
        boolean flag1;
        label17: {
            if (entity instanceof Player player && player.getAbilities().flying) {
                flag1 = true;
                break label17;
            }

            flag1 = false;
        }

        boolean flag = flag1;
        return flag ? 0.0F : this.knockbackMultiplier.orElse(1f);
    }
}
