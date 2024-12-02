package io.github.itskilerluc.familiarfaces.server.entities.ai;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import io.github.itskilerluc.familiarfaces.server.entities.Breeze;
import io.github.itskilerluc.familiarfaces.server.init.MemoryModuleTypeRegistry;
import io.github.itskilerluc.familiarfaces.server.init.SensorTypeRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class BreezeAi {
    public static final float SPEED_MULTIPLIER_WHEN_SLIDING = 0.6F;
    public static final List<SensorType<? extends Sensor<? super Breeze>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.NEAREST_PLAYERS, SensorTypeRegistry.BREEZE_ATTACK_ENTITY_SENSOR.get()
    );
    public static final List<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_ATTACKABLE,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleTypeRegistry.BREEZE_JUMP_COOLDOWN.get(),
            MemoryModuleTypeRegistry.BREEZE_JUMP_INHALING.get(),
            MemoryModuleTypeRegistry.BREEZE_SHOOT.get(),
            MemoryModuleTypeRegistry.BREEZE_SHOOT_CHARGING.get(),
            MemoryModuleTypeRegistry.BREEZE_SHOOT_RECOVERING.get(),
            MemoryModuleTypeRegistry.BREEZE_SHOOT_COOLDOWN.get(),
            MemoryModuleTypeRegistry.BREEZE_JUMP_TARGET.get(),
            MemoryModuleTypeRegistry.BREEZE_LEAVING_WATER.get(),
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.PATH
    );

    public static Brain<?> makeBrain(Breeze breeze, Brain<Breeze> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(breeze, brain);
        brain.setCoreActivities(Set.of(Activity.CORE));
        brain.setDefaultActivity(Activity.FIGHT);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<Breeze> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), new LookAtTargetSink(45, 90)));
    }

    private static void initIdleActivity(Brain<Breeze> brain) {
        brain.addActivity(
                Activity.IDLE,
                ImmutableList.of(
                        Pair.of(0, StartAttacking.create(p_312881_ -> p_312881_.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE))),
                        Pair.of(1, StartAttacking.create(Breeze::getHurtBy)),
                        Pair.of(2, new BreezeAi.SlideToTargetSink(20, 40)),
                        Pair.of(3, new RunOne<>(ImmutableList.of(Pair.of(new DoNothing(20, 100), 1), Pair.of(RandomStroll.stroll(SPEED_MULTIPLIER_WHEN_SLIDING), 2))))
                )
        );
    }

    private static void initFightActivity(Breeze breeze, Brain<Breeze> brain) {
        brain.addActivityWithConditions(
                Activity.FIGHT,
                ImmutableList.of(
                        Pair.of(0, StopAttackingIfTargetInvalid.create(p_350106_ -> !Sensor.isEntityAttackable(breeze, p_350106_))),
                        Pair.of(1, new Shoot()),
                        Pair.of(2, new LongJump()),
                        Pair.of(3, new ShootWhenStuck()),
                        Pair.of(4, new Slide())
                ),
                ImmutableSet.of(
                        Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT)
                )
        );
    }

    public static void updateActivity(Breeze breeze) {
        breeze.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
    }

    public static class SlideToTargetSink extends MoveToTargetSink {
        @VisibleForTesting
        public SlideToTargetSink(int p_311828_, int p_312532_) {
            super(p_311828_, p_312532_);
        }

        @Override
        protected void start(@NotNull ServerLevel serverLevel, @NotNull Mob mob, long GameTime) {
            super.start(serverLevel, mob, GameTime);
            //TODO mob.playSound(SoundEvents.BREEZE_SLIDE);
            if (mob instanceof Breeze breeze) {
                breeze.setExtraPose(ExtraPose.SLIDING);
            }
        }

        @Override
        protected void stop(@NotNull ServerLevel serverLevel, @NotNull Mob mob, long gameTime) {
            super.stop(serverLevel, mob, gameTime);
            mob.setPose(Pose.STANDING);
            if (mob instanceof Breeze breeze) {
                breeze.setExtraPose(ExtraPose.NONE);
            }
            if (mob.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
                mob.getBrain().setMemoryWithExpiry(MemoryModuleTypeRegistry.BREEZE_SHOOT.get(), Unit.INSTANCE, 60L);
            }
        }
    }
}
