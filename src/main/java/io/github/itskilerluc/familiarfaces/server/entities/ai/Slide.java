package io.github.itskilerluc.familiarfaces.server.entities.ai;

import io.github.itskilerluc.familiarfaces.server.entities.Breeze;
import io.github.itskilerluc.familiarfaces.server.init.MemoryModuleTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class Slide extends Behavior<Breeze> {
    public Slide() {
        super(
                Map.of(
                        MemoryModuleType.ATTACK_TARGET,
                        MemoryStatus.VALUE_PRESENT,
                        MemoryModuleType.WALK_TARGET,
                        MemoryStatus.VALUE_ABSENT,
                        MemoryModuleTypeRegistry.BREEZE_JUMP_COOLDOWN.get(),
                        MemoryStatus.VALUE_ABSENT,
                        MemoryModuleTypeRegistry.BREEZE_SHOOT.get(),
                        MemoryStatus.VALUE_ABSENT
                )
        );
    }

    protected boolean checkExtraStartConditions(ServerLevel level, Breeze owner) {
        return owner.onGround() && !owner.isInWater() && owner.getPose() == Pose.STANDING && owner.getExtraPose() == ExtraPose.NONE;
    }

    protected void start(ServerLevel level, Breeze entity, long gameTime) {
        LivingEntity livingentity = entity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
        if (livingentity != null) {
            boolean flag = entity.withinInnerCircleRange(livingentity.position());
            Vec3 vec3 = null;
            if (flag) {
                Vec3 vec31 = DefaultRandomPos.getPosAway(entity, 5, 5, livingentity.position());
                if (vec31 != null
                        && BreezeUtil.hasLineOfSight(entity, vec31)
                        && livingentity.distanceToSqr(vec31.x, vec31.y, vec31.z) > livingentity.distanceToSqr(entity)) {
                    vec3 = vec31;
                }
            }

            if (vec3 == null) {
                vec3 = entity.getRandom().nextBoolean()
                        ? BreezeUtil.randomPointBehindTarget(livingentity, entity.getRandom())
                        : randomPointInMiddleCircle(entity, livingentity);
            }

            entity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(BlockPos.containing(vec3), 0.6F, 1));
        }
    }

    private static Vec3 randomPointInMiddleCircle(Breeze breeze, LivingEntity target) {
        Vec3 vec3 = target.position().subtract(breeze.position());
        double d0 = vec3.length() - Mth.lerp(breeze.getRandom().nextDouble(), 8.0, 4.0);
        Vec3 vec31 = vec3.normalize().multiply(d0, d0, d0);
        return breeze.position().add(vec31);
    }
}
