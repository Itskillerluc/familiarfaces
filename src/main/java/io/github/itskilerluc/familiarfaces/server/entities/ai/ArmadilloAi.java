package io.github.itskilerluc.familiarfaces.server.entities.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import io.github.itskilerluc.familiarfaces.server.entities.Armadillo;
import io.github.itskilerluc.familiarfaces.server.init.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class ArmadilloAi {
    private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);
    private static final ImmutableList<SensorType<? extends Sensor<? super Armadillo>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorTypeRegistry.ARMADILLO_TEMPTATIONS.get(), SensorType.NEAREST_ADULT, SensorTypeRegistry.ARMADILLO_SCARE_DETECTED.get()
    );
    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            MemoryModuleType.IS_PANICKING,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.TEMPTING_PLAYER,
            MemoryModuleType.TEMPTATION_COOLDOWN_TICKS,
            MemoryModuleType.GAZE_COOLDOWN_TICKS,
            MemoryModuleType.IS_TEMPTED,
            MemoryModuleType.BREED_TARGET,
            MemoryModuleType.NEAREST_VISIBLE_ADULT,
            MemoryModuleTypeRegistry.DANGER_DETECTED_RECENTLY.get()
    );
    private static final OneShot<Armadillo> ARMADILLO_ROLLING_OUT = BehaviorBuilder.create(
            p_316587_ -> p_316587_.group(p_316587_.absent(MemoryModuleTypeRegistry.DANGER_DETECTED_RECENTLY.get()))
                    .apply(p_316587_, p_316348_ -> (p_319679_, p_319680_, p_319681_) -> {
                        if (p_319680_.isScared()) {
                            p_319680_.rollOut();
                            return true;
                        } else {
                            return false;
                        }
                    })
    );

    public static Brain.Provider<Armadillo> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    public static Brain<?> makeBrain(Brain<Armadillo> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initScaredActivity(brain);
        brain.setCoreActivities(Set.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<Armadillo> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new Swim(0.8F),
                        new ArmadilloAi.ArmadilloPanic(2.0F),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink() {
                            @Override
                            protected boolean checkExtraStartConditions(ServerLevel p_316506_, Mob p_316710_) {
                                if (p_316710_ instanceof Armadillo armadillo && armadillo.isScared()) {
                                    return false;
                                }

                                return super.checkExtraStartConditions(p_316506_, p_316710_);
                            }
                        },
                        new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS),
                        new CountDownCooldownTicks(MemoryModuleType.GAZE_COOLDOWN_TICKS),
                        ARMADILLO_ROLLING_OUT
                )
        );
    }

    private static void initIdleActivity(Brain<Armadillo> brain) {
        brain.addActivity(
                Activity.IDLE,
                ImmutableList.of(
                        Pair.of(0, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))),
                        Pair.of(1, new AnimalMakeLove(EntityTypeRegistry.ARMADILLO.get(), 1.0F)),
                        Pair.of(
                                2,
                                new RunOne<>(
                                        ImmutableList.of(
                                                Pair.of(new FollowTemptation(p_316818_ -> 1.25F, p_319682_ -> p_319682_.isBaby() ? 1.0 : 2.0), 1),
                                                Pair.of(BabyFollowAdult.create(ADULT_FOLLOW_RANGE, 1.25F), 1)
                                        )
                                )
                        ),
                        Pair.of(3, new RandomLookAround(UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F)),
                        Pair.of(
                                4,
                                new RunOne<>(
                                        ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                                        ImmutableList.of(
                                                Pair.of(RandomStroll.stroll(1.0F), 1), Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), 1), Pair.of(new DoNothing(30, 60), 1)
                                        )
                                )
                        )
                )
        );
    }

    private static void initScaredActivity(Brain<Armadillo> brain) {
        brain.addActivityWithConditions(
                Activity.PANIC,
                ImmutableList.of(Pair.of(0, new ArmadilloAi.ArmadilloBallUp())),
                Set.of(
                        Pair.of(MemoryModuleTypeRegistry.DANGER_DETECTED_RECENTLY.get(), MemoryStatus.VALUE_PRESENT),
                        Pair.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT)
                )
        );
    }

    public static void updateActivity(Armadillo armadillo) {
        armadillo.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.PANIC, Activity.IDLE));
    }

    public static Ingredient getTemptations() {
        return Ingredient.of(Tags.Items.ARMADILLO_FOOD);
    }

    public static class ArmadilloBallUp extends Behavior<Armadillo> {
        static final int BALL_UP_STAY_IN_STATE = 5 * 60 * 20;
        int nextPeekTimer = 0;
        boolean dangerWasAround;

        public ArmadilloBallUp() {
            super(Map.of(), BALL_UP_STAY_IN_STATE);
        }

        protected void tick(ServerLevel level, Armadillo owner, long gameTime) {
            super.tick(level, owner, gameTime);
            if (this.nextPeekTimer > 0) {
                this.nextPeekTimer--;
            }

            if (owner.shouldSwitchToScaredState()) {
                owner.switchToState(Armadillo.ArmadilloState.SCARED);
                if (owner.onGround()) {
                    owner.playSound(SoundEventRegistry.ARMADILLO_LAND.get());
                }
            } else {
                Armadillo.ArmadilloState armadillo$armadillostate = owner.getState();
                long i = owner.getBrain().getTimeUntilExpiry(MemoryModuleTypeRegistry.DANGER_DETECTED_RECENTLY.get());
                boolean flag = i > 75L;
                if (flag != this.dangerWasAround) {
                    this.nextPeekTimer = this.pickNextPeekTimer(owner);
                }

                this.dangerWasAround = flag;
                if (armadillo$armadillostate == Armadillo.ArmadilloState.SCARED) {
                    if (this.nextPeekTimer == 0 && owner.onGround() && flag) {
                        level.broadcastEntityEvent(owner, (byte)64);
                        this.nextPeekTimer = this.pickNextPeekTimer(owner);
                    }

                    if (i < (long)Armadillo.ArmadilloState.UNROLLING.animationDuration()) {
                        owner.playSound(SoundEventRegistry.ARMADILLO_UNROLL_START.get());
                        owner.switchToState(Armadillo.ArmadilloState.UNROLLING);
                    }
                } else if (armadillo$armadillostate == Armadillo.ArmadilloState.UNROLLING && i > (long)Armadillo.ArmadilloState.UNROLLING.animationDuration()) {
                    owner.switchToState(Armadillo.ArmadilloState.SCARED);
                }
            }
        }

        private int pickNextPeekTimer(Armadillo armadillo) {
            return Armadillo.ArmadilloState.SCARED.animationDuration() + armadillo.getRandom().nextIntBetweenInclusive(100, 400);
        }

        protected boolean checkExtraStartConditions(ServerLevel level, Armadillo owner) {
            return owner.onGround();
        }

        protected boolean canStillUse(ServerLevel level, Armadillo entity, long gameTime) {
            return entity.getState().isThreatened();
        }

        protected void start(ServerLevel level, Armadillo entity, long gameTime) {
            entity.rollUp();
        }

        protected void stop(@NotNull ServerLevel level, Armadillo entity, long gameTime) {
            if (!entity.canStayRolledUp()) {
                entity.rollOut();
            }
        }
    }

    public static class ArmadilloPanic extends AnimalPanic {
        public ArmadilloPanic(float p_316413_) {
            super(p_316413_, owner -> owner.getBrain()
                    .getMemory(MemoryModuleType.HURT_BY)
                    .map(src -> src.is(Tags.DamageTypes.PANIC_ENVIRONMENTAL_CAUSES))
                    .orElse(false) || owner.getBrain().hasMemoryValue(MemoryModuleType.IS_PANICKING));
        }

        @Override
        protected void start(@NotNull ServerLevel pLevel, @NotNull PathfinderMob pEntity, long pGameTime) {
            if (pEntity instanceof Armadillo armadillo) {
                armadillo.rollOut();
            }
            super.start(pLevel, pEntity, pGameTime);
        }
    }
}
