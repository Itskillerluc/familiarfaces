package io.github.itskillerluc.familiarfaces.server.entities.ai;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import io.github.itskillerluc.familiarfaces.server.entities.Breeze;
import io.github.itskillerluc.familiarfaces.server.init.MemoryModuleTypeRegistry;
import io.github.itskillerluc.familiarfaces.server.init.SoundEventRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

public class LongJump extends Behavior<Breeze> {
    private static final int REQUIRED_AIR_BLOCKS_ABOVE = 4;
    private static final int JUMP_COOLDOWN_TICKS = 10;
    private static final int JUMP_COOLDOWN_WHEN_HURT_TICKS = 2;
    private static final int INHALING_DURATION_TICKS = Math.round(10.0F);
    private static final float MAX_JUMP_VELOCITY = 1.4F;
    private static final ObjectArrayList<Integer> ALLOWED_ANGLES = new ObjectArrayList<>(Lists.newArrayList(40, 55, 60, 75, 80));

    @VisibleForTesting
    public LongJump() {
        super(
                Map.of(
                        MemoryModuleType.ATTACK_TARGET,
                        MemoryStatus.VALUE_PRESENT,
                        MemoryModuleTypeRegistry.BREEZE_JUMP_COOLDOWN.get(),
                        MemoryStatus.VALUE_ABSENT,
                        MemoryModuleTypeRegistry.BREEZE_JUMP_INHALING.get(),
                        MemoryStatus.REGISTERED,
                        MemoryModuleTypeRegistry.BREEZE_JUMP_TARGET.get(),
                        MemoryStatus.REGISTERED,
                        MemoryModuleTypeRegistry.BREEZE_SHOOT.get(),
                        MemoryStatus.VALUE_ABSENT,
                        MemoryModuleType.WALK_TARGET,
                        MemoryStatus.VALUE_ABSENT,
                        MemoryModuleTypeRegistry.BREEZE_LEAVING_WATER.get(),
                        MemoryStatus.REGISTERED
                ),
                200
        );
    }

    public static boolean canRun(ServerLevel level, Breeze breeze) {
        if (!breeze.onGround() && !breeze.isInWater()) {
            return false;
        } else if (breeze.isInWater() && breeze.getFluidTypeHeight(Fluids.WATER.getFluidType()) > breeze.getFluidJumpThreshold() || breeze.isInLava() || breeze.isInFluidType((fluidType, height) -> breeze.canSwimInFluidType(fluidType) && height > breeze.getFluidJumpThreshold())) {
            return false;
        } else if (breeze.getBrain().checkMemory(MemoryModuleTypeRegistry.BREEZE_JUMP_TARGET.get(), MemoryStatus.VALUE_PRESENT)) {
            return true;
        } else {
            LivingEntity livingentity = breeze.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
            if (livingentity == null) {
                return false;
            } else if (outOfAggroRange(breeze, livingentity)) {
                breeze.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
                return false;
            } else if (tooCloseForJump(breeze, livingentity)) {
                return false;
            } else if (!canJumpFromCurrentPosition(level, breeze)) {
                return false;
            } else {
                BlockPos blockpos = snapToSurface(breeze, BreezeUtil.randomPointBehindTarget(livingentity, breeze.getRandom()));
                if (blockpos == null) {
                    return false;
                } else {
                    BlockState blockstate = level.getBlockState(blockpos.below());
                    if (breeze.getType().isBlockDangerous(blockstate)) {
                        return false;
                    } else if (!BreezeUtil.hasLineOfSight(breeze, blockpos.getCenter())
                            && !BreezeUtil.hasLineOfSight(breeze, blockpos.above(4).getCenter())) {
                        return false;
                    } else {
                        breeze.getBrain().setMemory(MemoryModuleTypeRegistry.BREEZE_JUMP_TARGET.get(), blockpos);
                        return true;
                    }
                }
            }
        }
    }

    protected boolean checkExtraStartConditions(ServerLevel level, Breeze owner) {
        return canRun(level, owner);
    }

    protected boolean canStillUse(ServerLevel level, Breeze entity, long gameTime) {
        return (entity.getExtraPose() != ExtraPose.NONE || entity.getPose() != Pose.STANDING) && !entity.getBrain().hasMemoryValue(MemoryModuleTypeRegistry.BREEZE_JUMP_COOLDOWN.get());
    }

    protected void start(ServerLevel level, Breeze entity, long gameTime) {
        if (entity.getBrain().checkMemory(MemoryModuleTypeRegistry.BREEZE_JUMP_INHALING.get(), MemoryStatus.VALUE_ABSENT)) {
            entity.getBrain().setMemoryWithExpiry(MemoryModuleTypeRegistry.BREEZE_JUMP_INHALING.get(), Unit.INSTANCE, (long)INHALING_DURATION_TICKS);
        }

        entity.setExtraPose(ExtraPose.INHALING);
        level.playSound(null, entity, SoundEventRegistry.BREEZE_CHARGE.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
        entity.getBrain()
                .getMemory(MemoryModuleTypeRegistry.BREEZE_JUMP_TARGET.get())
                .ifPresent(p_312818_ -> entity.lookAt(EntityAnchorArgument.Anchor.EYES, p_312818_.getCenter()));
    }

    protected void tick(ServerLevel level, Breeze owner, long gameTime) {
        boolean flag = owner.isInWater();
        if (!flag && owner.getBrain().checkMemory(MemoryModuleTypeRegistry.BREEZE_LEAVING_WATER.get(), MemoryStatus.VALUE_PRESENT)) {
            owner.getBrain().eraseMemory(MemoryModuleTypeRegistry.BREEZE_LEAVING_WATER.get());
        }

        if (isFinishedInhaling(owner)) {
            Vec3 vec3 = owner.getBrain()
                    .getMemory(MemoryModuleTypeRegistry.BREEZE_JUMP_TARGET.get())
                    .flatMap(p_352815_ -> calculateOptimalJumpVector(owner, owner.getRandom(), Vec3.atBottomCenterOf(p_352815_)))
                    .orElse(null);
            if (vec3 == null) {
                owner.setPose(Pose.STANDING);
                return;
            }

            if (flag) {
                owner.getBrain().setMemory(MemoryModuleTypeRegistry.BREEZE_LEAVING_WATER.get(), Unit.INSTANCE);
            }

            owner.playSound(SoundEventRegistry.BREEZE_JUMP.get(), 1.0F, 1.0F);
            owner.setPose(Pose.LONG_JUMPING);
            owner.setExtraPose(ExtraPose.NONE);
            owner.setYRot(owner.yBodyRot);
            owner.setDiscardFriction(true);
            owner.setDeltaMovement(vec3);
        } else if (isFinishedJumping(owner)) {
            owner.playSound(SoundEventRegistry.BREEZE_LAND.get(), 1.0F, 1.0F);
            owner.setPose(Pose.STANDING);
            owner.setDiscardFriction(false);
            boolean flag1 = owner.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
            owner.getBrain().setMemoryWithExpiry(MemoryModuleTypeRegistry.BREEZE_JUMP_COOLDOWN.get(), Unit.INSTANCE, flag1 ? 2L : 10L);
            owner.getBrain().setMemoryWithExpiry(MemoryModuleTypeRegistry.BREEZE_SHOOT.get(), Unit.INSTANCE, 100L);
        }
    }

    protected void stop(ServerLevel level, Breeze entity, long gameTime) {
        if (entity.getPose() == Pose.LONG_JUMPING || entity.getExtraPose() == ExtraPose.INHALING) {
            entity.setPose(Pose.STANDING);
            entity.setExtraPose(ExtraPose.NONE);
        }

        entity.getBrain().eraseMemory(MemoryModuleTypeRegistry.BREEZE_JUMP_TARGET.get());
        entity.getBrain().eraseMemory(MemoryModuleTypeRegistry.BREEZE_JUMP_INHALING.get());
        entity.getBrain().eraseMemory(MemoryModuleTypeRegistry.BREEZE_LEAVING_WATER.get());
    }

    private static boolean isFinishedInhaling(Breeze breeze) {
        return breeze.getBrain().getMemory(MemoryModuleTypeRegistry.BREEZE_JUMP_INHALING.get()).isEmpty() && breeze.getExtraPose() == ExtraPose.INHALING;
    }

    private static boolean isFinishedJumping(Breeze breeze) {
        boolean flag = breeze.getPose() == Pose.LONG_JUMPING;
        boolean flag1 = breeze.onGround();
        boolean flag2 = breeze.isInWater() && breeze.getBrain().checkMemory(MemoryModuleTypeRegistry.BREEZE_LEAVING_WATER.get(), MemoryStatus.VALUE_ABSENT);
        return flag && (flag1 || flag2);
    }

    @Nullable
    private static BlockPos snapToSurface(LivingEntity owner, Vec3 targetPos) {
        ClipContext clipcontext = new ClipContext(
                targetPos, targetPos.relative(Direction.DOWN, 10.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, owner
        );
        HitResult hitresult = owner.level().clip(clipcontext);
        if (hitresult.getType() == HitResult.Type.BLOCK) {
            return BlockPos.containing(hitresult.getLocation()).above();
        } else {
            ClipContext clipcontext1 = new ClipContext(
                    targetPos, targetPos.relative(Direction.UP, 10.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, owner
            );
            HitResult hitresult1 = owner.level().clip(clipcontext1);
            return hitresult1.getType() == HitResult.Type.BLOCK ? BlockPos.containing(hitresult1.getLocation()).above() : null;
        }
    }

    private static boolean outOfAggroRange(Breeze breeze, LivingEntity target) {
        return !target.closerThan(breeze, 24.0);
    }

    private static boolean tooCloseForJump(Breeze breeze, LivingEntity target) {
        return target.distanceTo(breeze) - 4.0F <= 0.0F;
    }

    private static boolean canJumpFromCurrentPosition(ServerLevel level, Breeze breeze) {
        BlockPos blockpos = breeze.blockPosition();

        for (int i = 1; i <= 4; i++) {
            BlockPos blockpos1 = blockpos.relative(Direction.UP, i);
            if (!level.getBlockState(blockpos1).isAir() && !level.getFluidState(blockpos1).is(FluidTags.WATER)) {
                return false;
            }
        }

        return true;
    }

    private static Optional<Vec3> calculateOptimalJumpVector(Breeze breeze, RandomSource random, Vec3 target) {
        for (int i : Util.shuffledCopy(ALLOWED_ANGLES, random)) {
            Optional<Vec3> optional = LongJumpUtil.calculateJumpVectorForAngle(breeze, target, 1.4F, i, false);
            if (optional.isPresent()) {
                return optional;
            }
        }

        return Optional.empty();
    }
}
