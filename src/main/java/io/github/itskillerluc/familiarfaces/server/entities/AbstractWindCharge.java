package io.github.itskillerluc.familiarfaces.server.entities;

import io.github.itskillerluc.familiarfaces.server.entities.ai.ProjectileDeflection;
import io.github.itskillerluc.familiarfaces.server.init.DamageTypeInit;
import io.github.itskillerluc.familiarfaces.server.init.Tags;
import io.github.itskillerluc.familiarfaces.server.util.SimpleExplosionDamageCalculator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractWindCharge extends AbstractHurtingProjectile implements ItemSupplier {
    public static final SimpleExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(
            true, false, Optional.empty(), Optional.of(HolderSet.direct(Objects.requireNonNull(ForgeRegistries.BLOCKS.tags()).getTag(Tags.Blocks.BLOCKS_WIND_CHARGE_EXPLOSIONS).stream().map(Holder::direct).toList()))
    );
    public double accelerationPower = 0.1;

    protected AbstractWindCharge(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
        accelerationPower = 0.0;
    }

    protected AbstractWindCharge(
            EntityType<? extends AbstractHurtingProjectile> entityType, Level level, Entity owner, double x, double y, double z
    ) {
        this(entityType, level);
        this.setPos(x, y, z);
        this.accelerationPower = 0;
    }

    public AbstractWindCharge(
            EntityType<? extends AbstractHurtingProjectile> entityType, double x, double y, double z, Vec3 movement, Level level
    ) {
        this(entityType, level);
        this.moveTo(x, y, z, this.getYRot(), this.getXRot());
        this.reapplyPosition();
        this.assignDirectionalMovement(movement, this.accelerationPower);
        this.accelerationPower = 0.0;
    }

    public AbstractWindCharge(EntityType<? extends AbstractHurtingProjectile> entityType, LivingEntity owner, Vec3 movement, Level level) {
        this(entityType, owner.getX(), owner.getY(), owner.getZ(), movement, level);
        this.setOwner(owner);
        this.setRot(owner.getYRot(), owner.getXRot());
        this.accelerationPower = 0.0;
    }

    @Override
    protected @NotNull AABB makeBoundingBox() {
        float f = this.getType().getDimensions().width / 2.0F;
        float f1 = this.getType().getDimensions().height;
        return new AABB(
                this.position().x - (double) f,
                this.position().y - 0.15F,
                this.position().z - (double) f,
                this.position().x + (double) f,
                this.position().y - 0.15F + (double) f1,
                this.position().z + (double) f
        );
    }

    @Override
    public boolean canCollideWith(@NotNull Entity entity) {
        return !(entity instanceof AbstractWindCharge) && super.canCollideWith(entity);
    }

    @Override
    protected boolean canHitEntity(@NotNull Entity target) {
        if (target instanceof AbstractWindCharge) {
            return false;
        } else {
            return target.getType() != EntityType.END_CRYSTAL && super.canHitEntity(target);
        }
    }

    /**
     * Called when the arrow hits an entity
     */
    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            LivingEntity livingentity = this.getOwner() instanceof LivingEntity livingentity1 ? livingentity1 : null;
            Entity entity = result.getEntity();
            if (livingentity != null) {
                livingentity.setLastHurtMob(entity);
            }

            DamageSource damagesource = new DamageSource(level().registryAccess().lookup(Registries.DAMAGE_TYPE).orElseThrow().getOrThrow(DamageTypeInit.WIND_CHARGE), this);
            if (entity.hurt(damagesource, 1.0F) && entity instanceof LivingEntity livingentity2) {
                if (livingentity != null) {
                    EnchantmentHelper.doPostDamageEffects(livingentity, livingentity2);
                }
            }

            this.explode(this.position());
        }
    }


    @Override
    public void push(double x, double y, double z) {
    }

    protected abstract void explode(Vec3 pos);

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            Vec3i vec3i = result.getDirection().getNormal();
            Vec3 vec3 = Vec3.atLowerCornerOf(vec3i).multiply(0.25, 0.25, 0.25);
            Vec3 vec31 = result.getLocation().add(vec3);
            this.explode(vec31);
            this.discard();
        }
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected float getInertia() {
        return 1.0F;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    protected @NotNull ParticleOptions getTrailParticle() {
        return null;
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide && this.getBlockY() > this.level().getMaxBuildHeight() + 30) {
            this.explode(this.position());
            this.discard();
        } else {
            Entity entity = this.getOwner();
            if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
                HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
                if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                    this.onHit(hitresult);
                }

                this.checkInsideBlocks();
                Vec3 vec3 = this.getDeltaMovement();
                double d0 = this.getX() + vec3.x;
                double d1 = this.getY() + vec3.y;
                double d2 = this.getZ() + vec3.z;
                ProjectileUtil.rotateTowardsMovement(this, 0.2F);
                float f;
                if (this.isInWater()) {
                    for (int i = 0; i < 4; i++) {
                        float f1 = 0.25F;
                        this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25, d1 - vec3.y * 0.25, d2 - vec3.z * 0.25, vec3.x, vec3.y, vec3.z);
                    }

                    f = 0.8F;
                } else {
                    f = this.getInertia();
                }

                this.setDeltaMovement(vec3.add(vec3.normalize().scale(this.accelerationPower)).scale(f));
                ParticleOptions particleoptions = this.getTrailParticle();

                //noinspection ConstantValue
                if (particleoptions != null) {
                    this.level().addParticle(particleoptions, d0, d1 + 0.5, d2, 0.0, 0.0, 0.0);
                }

                this.setPos(d0, d1, d2);
            } else {
                this.discard();
            }
        }
    }

    protected void onHit(HitResult result) {
        HitResult.Type hitresult$type = result.getType();
        if (hitresult$type == HitResult.Type.ENTITY) {
            EntityHitResult entityhitresult = (EntityHitResult)result;
            Entity entity = entityhitresult.getEntity();
            if (entity.getType().is(Tags.EntityTypes.REDIRECTABLE_PROJECTILE) && entity instanceof Projectile projectile) {
                deflect(ProjectileDeflection.AIM_DEFLECT, this.getOwner(), this.getOwner(), true);
            }

            this.onHitEntity(entityhitresult);
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, result.getLocation(), GameEvent.Context.of(this, null));
        } else if (hitresult$type == HitResult.Type.BLOCK) {
            BlockHitResult blockhitresult = (BlockHitResult)result;
            this.onHitBlock(blockhitresult);
            BlockPos blockpos = blockhitresult.getBlockPos();
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, blockpos, GameEvent.Context.of(this, this.level().getBlockState(blockpos)));
        }

        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    public boolean deflect(ProjectileDeflection deflection, @Nullable Entity entity, @Nullable Entity owner, boolean deflectedByPlayer) {
        if (!this.level().isClientSide) {
            deflection.deflect(this, entity, this.random);
            this.setOwner(owner);
            this.onDeflection(entity, deflectedByPlayer);
        }

        return true;
    }

    protected void onDeflection(@Nullable Entity entity, boolean deflectedByPlayer) {
        if (deflectedByPlayer) {
            accelerationPower = 0.1;
        } else {
            accelerationPower *= 0.5;
        }
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        return false;
    }

    private void assignDirectionalMovement(Vec3 movement, double accelerationPower) {
        this.setDeltaMovement(movement.normalize().scale(accelerationPower));
        this.hasImpulse = true;
    }
}
