package io.github.itskilerluc.familiarfaces.server.entities.ai;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import io.github.itskilerluc.familiarfaces.server.entities.Breeze;
import io.github.itskilerluc.familiarfaces.server.entities.BreezeWindCharge;
import io.github.itskilerluc.familiarfaces.server.init.MemoryModuleTypeRegistry;
import io.github.itskilerluc.familiarfaces.server.init.SoundEventRegistry;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;

public class Shoot extends Behavior<Breeze> {
    private static final int ATTACK_RANGE_MIN_SQRT = 4;
    private static final int ATTACK_RANGE_MAX_SQRT = 256;
    private static final int UNCERTAINTY_BASE = 5;
    private static final int UNCERTAINTY_MULTIPLIER = 4;
    private static final float PROJECTILE_MOVEMENT_SCALE = 0.7F;
    private static final int SHOOT_INITIAL_DELAY_TICKS = Math.round(15.0F);
    private static final int SHOOT_RECOVER_DELAY_TICKS = Math.round(4.0F);
    private static final int SHOOT_COOLDOWN_TICKS = Math.round(10.0F);

    @VisibleForTesting
    public Shoot() {
        super(
                ImmutableMap.of(
                        MemoryModuleType.ATTACK_TARGET,
                        MemoryStatus.VALUE_PRESENT,
                        MemoryModuleTypeRegistry.BREEZE_SHOOT_COOLDOWN.get(),
                        MemoryStatus.VALUE_ABSENT,
                        MemoryModuleTypeRegistry.BREEZE_SHOOT_CHARGING.get(),
                        MemoryStatus.VALUE_ABSENT,
                        MemoryModuleTypeRegistry.BREEZE_SHOOT_RECOVERING.get(),
                        MemoryStatus.VALUE_ABSENT,
                        MemoryModuleTypeRegistry.BREEZE_SHOOT.get(),
                        MemoryStatus.VALUE_PRESENT,
                        MemoryModuleType.WALK_TARGET,
                        MemoryStatus.VALUE_ABSENT,
                        MemoryModuleTypeRegistry.BREEZE_JUMP_TARGET.get(),
                        MemoryStatus.VALUE_ABSENT
                ),
                SHOOT_INITIAL_DELAY_TICKS + 1 + SHOOT_RECOVER_DELAY_TICKS
        );
    }

    protected boolean checkExtraStartConditions(ServerLevel level, Breeze owner) {
        return owner.getPose() == Pose.STANDING && owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).map(p_312632_ -> isTargetWithinRange(owner, p_312632_)).map(p_312737_ -> {
            if (!p_312737_) {
                owner.getBrain().eraseMemory(MemoryModuleTypeRegistry.BREEZE_SHOOT.get());
            }

            return (Boolean) p_312737_;
        }).orElse(false);
    }

    protected boolean canStillUse(ServerLevel level, Breeze entity, long gameTime) {
        return entity.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && entity.getBrain().hasMemoryValue(MemoryModuleTypeRegistry.BREEZE_SHOOT.get());
    }

    protected void start(ServerLevel level, Breeze entity, long gameTime) {
        entity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent(p_312833_ -> entity.setExtraPose(ExtraPose.SHOOTING));
        entity.getBrain().setMemoryWithExpiry(MemoryModuleTypeRegistry.BREEZE_SHOOT_CHARGING.get(), Unit.INSTANCE, (long)SHOOT_INITIAL_DELAY_TICKS);
        entity.playSound(SoundEventRegistry.BREEZE_INHALE.get(), 1.0F, 1.0F);
    }

    protected void stop(ServerLevel level, Breeze entity, long gameTime) {
        if (entity.getExtraPose() == ExtraPose.SHOOTING) {
            entity.setPose(Pose.STANDING);
            entity.setExtraPose(ExtraPose.NONE);
        }

        entity.getBrain().setMemoryWithExpiry(MemoryModuleTypeRegistry.BREEZE_SHOOT_COOLDOWN.get(), Unit.INSTANCE, (long)SHOOT_COOLDOWN_TICKS);
        entity.getBrain().eraseMemory(MemoryModuleTypeRegistry.BREEZE_SHOOT.get());
    }

    protected void tick(ServerLevel level, Breeze owner, long gameTime) {
        Brain<Breeze> brain = owner.getBrain();
        LivingEntity livingentity = brain.getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
        if (livingentity != null) {
            owner.lookAt(EntityAnchorArgument.Anchor.EYES, livingentity.position());
            if (!brain.getMemory(MemoryModuleTypeRegistry.BREEZE_SHOOT_CHARGING.get()).isPresent() && !brain.getMemory(MemoryModuleTypeRegistry.BREEZE_SHOOT_RECOVERING.get()).isPresent()) {
                brain.setMemoryWithExpiry(MemoryModuleTypeRegistry.BREEZE_SHOOT_RECOVERING.get(), Unit.INSTANCE, (long)SHOOT_RECOVER_DELAY_TICKS);
                if (isFacingTarget(owner, livingentity)) {
                    double d0 = livingentity.getX() - owner.getX();
                    double d1 = livingentity.getY(livingentity.isPassenger() ? 0.8 : 0.3) - owner.getY(0.5);
                    double d2 = livingentity.getZ() - owner.getZ();
                    BreezeWindCharge breezewindcharge = new BreezeWindCharge(owner, level);
                    owner.playSound(SoundEventRegistry.BREEZE_SHOOT.get(), 1.5F, 1.0F);
                    breezewindcharge.shoot(d0, d1, d2, 0.7F, (float)(5 - level.getDifficulty().getId() * 4));
                    level.addFreshEntity(breezewindcharge);
                }
            }
        }
    }

    @VisibleForTesting
    public static boolean isFacingTarget(Breeze breeze, LivingEntity target) {
        Vec3 vec3 = breeze.getViewVector(1.0F);
        Vec3 vec31 = target.position().subtract(breeze.position()).normalize();
        return vec3.dot(vec31) > 0.5;
    }

    private static boolean isTargetWithinRange(Breeze breeze, LivingEntity target) {
        double d0 = breeze.position().distanceToSqr(target.position());
        return d0 > 4.0 && d0 < 256.0;
    }
}
