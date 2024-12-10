package io.github.itskilerluc.familiarfaces.server.entities;

import com.mojang.serialization.Dynamic;
import io.github.itskilerluc.familiarfaces.server.entities.ai.BreezeAi;
import io.github.itskilerluc.familiarfaces.server.entities.ai.ExtraPose;
import io.github.itskilerluc.familiarfaces.server.init.EntityTypeRegistry;
import io.github.itskilerluc.familiarfaces.server.init.SoundEventRegistry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public class Breeze extends Monster {
    public static final EntityDataSerializer<ExtraPose> EXTRA_POSE_SERIALIZER = EntityDataSerializer.simpleEnum(ExtraPose.class);
    public static final EntityDataAccessor<ExtraPose> EXTRA_POSE = SynchedEntityData.defineId(Breeze.class, EXTRA_POSE_SERIALIZER);
    private static final int SLIDE_PARTICLES_AMOUNT = 20;
    private static final int IDLE_PARTICLES_AMOUNT = 1;
    private static final int JUMP_DUST_PARTICLES_AMOUNT = 20;
    private static final int JUMP_TRAIL_PARTICLES_AMOUNT = 3;
    private static final int JUMP_TRAIL_DURATION_TICKS = 5;
    private static final int JUMP_CIRCLE_DISTANCE_Y = 10;
    private static final float FALL_DISTANCE_SOUND_TRIGGER_THRESHOLD = 3.0F;
    private static final int WHIRL_SOUND_FREQUENCY_MIN = 1;
    private static final int WHIRL_SOUND_FREQUENCY_MAX = 80;

    static {
        EntityDataSerializers.registerSerializer(EXTRA_POSE_SERIALIZER);
    }

    public AnimationState idle = new AnimationState();
    public AnimationState slide = new AnimationState();
    public AnimationState slideBack = new AnimationState();
    public AnimationState longJump = new AnimationState();
    public AnimationState shoot = new AnimationState();
    public AnimationState inhale = new AnimationState();
    private int jumpTrailStartedTick = 0;
    private int soundTick = 0;

    public Breeze(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.TRAPDOOR, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.xpReward = 10;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.63F)
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(EXTRA_POSE, ExtraPose.NONE);
    }

    public ExtraPose getExtraPose() {
        return this.entityData.get(EXTRA_POSE);
    }

    public void setExtraPose(ExtraPose pose) {
        this.entityData.set(EXTRA_POSE, pose);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return BreezeAi.makeBrain(this, this.brainProvider().makeBrain(dynamic));
    }

    @Override
    public Brain<Breeze> getBrain() {
        return (Brain<Breeze>) super.getBrain();
    }

    @Override
    protected Brain.Provider<Breeze> brainProvider() {
        return Brain.provider(BreezeAi.MEMORY_TYPES, BreezeAi.SENSOR_TYPES);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (this.level().isClientSide() && EXTRA_POSE.equals(key)) {
            this.resetAnimations();
            ExtraPose pose = this.getExtraPose();
            switch (pose) {
                case SHOOTING:
                    this.shoot.startIfStopped(this.tickCount);
                    break;
                case INHALING:
                    this.longJump.startIfStopped(this.tickCount);
                    break;
                case SLIDING:
                    this.slide.startIfStopped(this.tickCount);
            }
        }

        super.onSyncedDataUpdated(key);
    }

    private void resetAnimations() {
        this.shoot.stop();
        this.idle.stop();
        this.inhale.stop();
        this.longJump.stop();
    }

    @Override
    public void tick() {
        ExtraPose pose = this.getExtraPose();
        switch (pose) {
            case SHOOTING:
            case INHALING:
                this.resetJumpTrail().emitGroundParticles(1 + this.getRandom().nextInt(1));
                break;
            case SLIDING:
                this.emitGroundParticles(20);
                break;
            default:
                switch (getPose()) {
                    case STANDING:
                        this.resetJumpTrail().emitGroundParticles(1 + this.getRandom().nextInt(1));
                        break;
                    case LONG_JUMPING:
                        this.emitJumpTrailParticles();
                        break;
                }
        }

        if (pose != ExtraPose.SLIDING && this.slide.isStarted()) {
            this.slideBack.start(this.tickCount);
            this.slide.stop();
        }

        this.soundTick = this.soundTick == 0 ? this.random.nextIntBetweenInclusive(1, 80) : this.soundTick - 1;
        if (this.soundTick == 0) {
            this.playWhirlSound();
        }

        super.tick();
    }

    public Breeze resetJumpTrail() {
        this.jumpTrailStartedTick = 0;
        return this;
    }

    public void emitJumpTrailParticles() {
        if (++this.jumpTrailStartedTick <= 5) {
            BlockState blockstate = !this.getFeetBlockState().isAir() ? this.getFeetBlockState() : this.getBlockStateOn();
            Vec3 vec3 = this.getDeltaMovement();
            Vec3 vec31 = this.position().add(vec3).add(0.0, 0.1F, 0.0);

            for (int i = 0; i < 3; i++) {
                this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), vec31.x, vec31.y, vec31.z, 0.0, 0.0, 0.0);
            }
        }
    }

    public void emitGroundParticles(int count) {
        if (!this.isPassenger()) {
            Vec3 vec3 = this.getBoundingBox().getCenter();
            Vec3 vec31 = new Vec3(vec3.x, this.position().y, vec3.z);
            BlockState blockstate = !this.getFeetBlockState().isAir() ? this.getFeetBlockState() : this.getBlockStateOn();
            if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                for (int i = 0; i < count; i++) {
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), vec31.x, vec31.y, vec31.z, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    @Override
    public void playAmbientSound() {
        if (this.getTarget() == null || !this.onGround()) {
            this.level().playLocalSound(blockPosition(), Objects.requireNonNull(this.getAmbientSound()), this.getSoundSource(), 1.0F, 1.0F, false);
        }
    }

    public void playWhirlSound() {
        float f = 0.7F + 0.4F * this.random.nextFloat();
        float f1 = 0.8F + 0.2F * this.random.nextFloat();
        this.level().playLocalSound(blockPosition(), SoundEventRegistry.BREEZE_WHIRL.get(), this.getSoundSource(), f1, f, false);
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEventRegistry.BREEZE_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEventRegistry.BREEZE_HURT.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.onGround() ? SoundEventRegistry.BREEZE_IDLE_GROUND.get() : SoundEventRegistry.BREEZE_IDLE_AIR.get();
    }

    public Optional<LivingEntity> getHurtBy() {
        return this.getBrain()
                .getMemory(MemoryModuleType.HURT_BY)
                .map(DamageSource::getEntity)
                .filter(p_321467_ -> p_321467_ instanceof LivingEntity)
                .map(p_321468_ -> (LivingEntity) p_321468_);
    }

    public boolean withinInnerCircleRange(Vec3 pos) {
        Vec3 vec3 = this.blockPosition().getCenter();
        return closerThan(vec3, pos, 4.0, 10.0);
    }

    private boolean closerThan(Vec3 pos, Vec3 otherPos, double horizontalDistance, double verticalDistance) {
        double d0 = pos.x() - otherPos.x;
        double d1 = pos.y() - otherPos.y;
        double d2 = pos.z() - otherPos.z;
        return Mth.lengthSquared(d0, d2) < Mth.square(horizontalDistance) && Math.abs(d1) < verticalDistance;
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("breezeBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().popPush("breezeActivityUpdate");
        BreezeAi.updateActivity(this);
        this.level().getProfiler().pop();
        super.customServerAiStep();
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    public boolean canAttackType(EntityType<?> type) {
        return type == EntityType.PLAYER || type == EntityType.IRON_GOLEM;
    }

    @Override
    public int getMaxHeadYRot() {
        return 30;
    }

    @Override
    public int getHeadRotSpeed() {
        return 25;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.getEntity() instanceof Breeze || super.isInvulnerableTo(source);
    }

    @Override
    public double getFluidJumpThreshold() {
        return this.getEyeHeight();
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        if (fallDistance > 3.0F) {
            this.playSound(SoundEventRegistry.BREEZE_LAND.get(), 1.0F, 1.0F);
        }

        return super.causeFallDamage(fallDistance, multiplier, source);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        return this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }

    @Override
    public boolean hurt(DamageSource source, float p_21017_) {
        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            var entity = source.getDirectEntity();
            if (entity instanceof Projectile projectile) {
                if (projectile.getType() != EntityTypeRegistry.BREEZE_WIND_CHARGE.get() && projectile.getType() != EntityTypeRegistry.WIND_CHARGE.get()) {
                    level().playSound(null, this, SoundEventRegistry.BREEZE_DEFLECT.get(), this.getSoundSource(), 1.0F, 1.0F);
                    float f = 170.0F + random.nextFloat() * 20.0F;
                    projectile.setDeltaMovement(projectile.getDeltaMovement().scale(-0.5));
                    projectile.setYRot(projectile.getYRot() + f);
                    projectile.yRotO += f;
                    projectile.hasImpulse = true;
                }
                return false;
            }
        }
        return super.hurt(source, p_21017_);
    }

    @Override
    public float getEyeHeight(Pose pPose) {
        return 1.3452F;
    }
}
