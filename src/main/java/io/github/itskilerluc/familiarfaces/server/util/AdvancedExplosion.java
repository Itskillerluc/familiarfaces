package io.github.itskilerluc.familiarfaces.server.util;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import io.github.itskilerluc.familiarfaces.server.networking.CustomExplodePacket;
import io.github.itskilerluc.familiarfaces.server.networking.FamiliarFacesNetwork;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkDirection;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class AdvancedExplosion extends Explosion {
    public static final SimpleExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(true, true, Optional.empty(), Optional.empty());
    private static final Random RANDOM = new Random();
    public final float radius;
    private final ParticleOptions smallExplosionParticles;
    private final ParticleOptions largeExplosionParticles;
    private final SoundEvent explosionSound;
    private final Level level;
    private final SimpleExplosionDamageCalculator damageCalculator;
    private final boolean fire;
    private final BlockInteraction blockInteraction;
    public boolean interact = false;

    public AdvancedExplosion(Level pLevel, @Nullable Entity pSource, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, List<BlockPos> pPositions) {
        super(pLevel, pSource, pToBlowX, pToBlowY, pToBlowZ, pRadius, pPositions);
        smallExplosionParticles = ParticleTypes.EXPLOSION;
        largeExplosionParticles = ParticleTypes.EXPLOSION_EMITTER;
        explosionSound = SoundEvents.GENERIC_EXPLODE;
        this.level = pLevel;
        this.radius = pRadius;
        this.damageCalculator = EXPLOSION_DAMAGE_CALCULATOR;
        this.fire = false;
        blockInteraction = BlockInteraction.DESTROY_WITH_DECAY;
    }

    public AdvancedExplosion(Level pLevel, @Nullable Entity pSource, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, boolean pFire, BlockInteraction pBlockInteraction, List<BlockPos> pPositions) {
        super(pLevel, pSource, pToBlowX, pToBlowY, pToBlowZ, pRadius, pFire, pBlockInteraction, pPositions);
        smallExplosionParticles = ParticleTypes.EXPLOSION;
        largeExplosionParticles = ParticleTypes.EXPLOSION_EMITTER;
        explosionSound = SoundEvents.GENERIC_EXPLODE;
        this.level = pLevel;
        this.radius = pRadius;
        this.damageCalculator = EXPLOSION_DAMAGE_CALCULATOR;
        this.fire = pFire;
        blockInteraction = pBlockInteraction;
    }

    public AdvancedExplosion(Level pLevel, @Nullable Entity pSource, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, boolean pFire, BlockInteraction pBlockInteraction) {
        super(pLevel, pSource, pToBlowX, pToBlowY, pToBlowZ, pRadius, pFire, pBlockInteraction);
        smallExplosionParticles = ParticleTypes.EXPLOSION;
        largeExplosionParticles = ParticleTypes.EXPLOSION_EMITTER;
        explosionSound = SoundEvents.GENERIC_EXPLODE;
        this.level = pLevel;
        this.radius = pRadius;
        this.damageCalculator = EXPLOSION_DAMAGE_CALCULATOR;
        this.fire = pFire;
        this.blockInteraction = pBlockInteraction;
    }

    public AdvancedExplosion(Level pLevel, @Nullable Entity pSource, @Nullable DamageSource pDamageSource, @Nullable SimpleExplosionDamageCalculator pDamageCalculator, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, boolean pFire, BlockInteraction pBlockInteraction) {
        super(pLevel, pSource, pDamageSource, pDamageCalculator, pToBlowX, pToBlowY, pToBlowZ, pRadius, pFire, pBlockInteraction);
        smallExplosionParticles = ParticleTypes.EXPLOSION;
        largeExplosionParticles = ParticleTypes.EXPLOSION_EMITTER;
        explosionSound = SoundEvents.GENERIC_EXPLODE;
        this.level = pLevel;
        this.radius = pRadius;
        this.damageCalculator = pDamageCalculator == null ? EXPLOSION_DAMAGE_CALCULATOR : pDamageCalculator;
        this.fire = pFire;
        this.blockInteraction = pBlockInteraction;
    }

    public AdvancedExplosion(Level level, @Nullable Entity source, @Nullable DamageSource damageSource, @Nullable SimpleExplosionDamageCalculator damageCalculator, double x, double y, double z, float radius, boolean fire, BlockInteraction blockInteraction, ParticleOptions smallExplosionParticles, ParticleOptions largeExplosionParticles, SoundEvent explosionSound) {
        super(level, source, damageSource, damageCalculator, x, y, z, radius, fire, blockInteraction);
        this.smallExplosionParticles = smallExplosionParticles;
        this.largeExplosionParticles = largeExplosionParticles;
        this.explosionSound = explosionSound;
        this.level = level;
        this.radius = radius;
        this.damageCalculator = damageCalculator == null ? EXPLOSION_DAMAGE_CALCULATOR : damageCalculator;
        this.fire = fire;
        this.blockInteraction = blockInteraction;
    }

    public AdvancedExplosion(
            Level level,
            @javax.annotation.Nullable Entity source,
            double x,
            double y,
            double z,
            float radius,
            List<BlockPos> toBlow,
            BlockInteraction blockInteraction,
            ParticleOptions smallExplosionParticles,
            ParticleOptions largeExplosionParticles,
            Holder<SoundEvent> explosionSound
    ) {
        this(
                level,
                source,
                getDefaultDamageSource(level, source),
                null,
                x,
                y,
                z,
                radius,
                false,
                blockInteraction,
                smallExplosionParticles,
                largeExplosionParticles,
                explosionSound.get()
        );
        this.toBlow.addAll(toBlow);
    }

    private static BlockInteraction getDestroyType(GameRules.Key<GameRules.BooleanValue> pGameRule, Level level) {
        return level.getGameRules().getBoolean(pGameRule) ? BlockInteraction.DESTROY_WITH_DECAY : BlockInteraction.DESTROY;
    }

    public static DamageSource getDefaultDamageSource(Level level, @javax.annotation.Nullable Entity source) {
        return level.damageSources().explosion(source, getIndirectSourceEntityInternal(source));
    }

    @javax.annotation.Nullable
    private static LivingEntity getIndirectSourceEntityInternal(@javax.annotation.Nullable Entity source) {
        if (source == null) {
            return null;
        } else if (source instanceof PrimedTnt primedtnt) {
            return primedtnt.getOwner();
        } else if (source instanceof LivingEntity) {
            return (LivingEntity) source;
        } else {
            if (source instanceof Projectile projectile) {
                Entity entity = projectile.getOwner();
                if (entity instanceof LivingEntity) {
                    return (LivingEntity) entity;
                }
            }

            return null;
        }
    }

    private static void addBlockDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> pDropPositionArray, ItemStack pStack, BlockPos pPos) {
        int i = pDropPositionArray.size();

        for (int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = pDropPositionArray.get(j);
            ItemStack itemstack = pair.getFirst();
            if (ItemEntity.areMergable(itemstack, pStack)) {
                ItemStack itemstack1 = ItemEntity.merge(itemstack, pStack, 16);
                pDropPositionArray.set(j, Pair.of(itemstack1, pair.getSecond()));
                if (pStack.isEmpty()) {
                    return;
                }
            }
        }

        pDropPositionArray.add(Pair.of(pStack, pPos));
    }

    public static AdvancedExplosion explode(
            Level level,
            @Nullable Entity source,
            @Nullable DamageSource damageSource,
            @Nullable SimpleExplosionDamageCalculator damageCalculator,
            double x,
            double y,
            double z,
            float radius,
            boolean fire,
            Level.ExplosionInteraction explosionInteraction,
            boolean spawnParticles,
            ParticleOptions smallExplosionParticles,
            ParticleOptions largeExplosionParticles,
            SoundEvent explosionSound,
            boolean interaction
    ) {
        BlockInteraction explosion$blockinteraction = switch (explosionInteraction) {
            case NONE -> BlockInteraction.KEEP;
            case BLOCK -> getDestroyType(GameRules.RULE_BLOCK_EXPLOSION_DROP_DECAY, level);
            case MOB ->
                    ForgeEventFactory.getMobGriefingEvent(level, source) ? getDestroyType(GameRules.RULE_MOB_EXPLOSION_DROP_DECAY, level) : BlockInteraction.KEEP;
            case TNT -> getDestroyType(GameRules.RULE_TNT_EXPLOSION_DROP_DECAY, level);
        };

        AdvancedExplosion explosion = new AdvancedExplosion(
                level,
                source,
                damageSource,
                damageCalculator,
                x,
                y,
                z,
                radius,
                fire,
                explosion$blockinteraction,
                smallExplosionParticles,
                largeExplosionParticles,
                explosionSound
        );

        explosion.interact = interaction;
        if (ForgeEventFactory.onExplosionStart(level, explosion)) return explosion;
        explosion.explode();
        explosion.finalizeExplosion(spawnParticles);

        if (!explosion.interactsWithBlocks()) {
            explosion.clearToBlow();
        }

        if (level instanceof ServerLevel serverLevel) {
            for (ServerPlayer serverPlayer : serverLevel.players()) {
                if (serverPlayer.distanceToSqr(x, y, z) < 4096.0d) {

                    var knockback = explosion.getHitPlayers().get(serverPlayer);
                    FamiliarFacesNetwork.CHANNEL.sendTo(new CustomExplodePacket(x, y, z, radius, explosion.getToBlow(), knockback == null ? 0 : ((float) knockback.x), knockback == null ? 0 : ((float) knockback.y), knockback == null ? 0 : ((float) knockback.z), smallExplosionParticles, largeExplosionParticles, explosion$blockinteraction, Holder.direct(explosionSound), interaction), serverPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                }
            }
        }

        return explosion;
    }


    public void explode() {
        level.gameEvent(getDirectSourceEntity(), GameEvent.EXPLODE, new Vec3(getPosition().x, getPosition().y, getPosition().z));
        Set<BlockPos> set = Sets.newHashSet();
        for (int j = 0; j < 16; j++) {
            for (int k = 0; k < 16; k++) {
                for (int l = 0; l < 16; l++) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d0 = (float) j / 15.0F * 2.0F - 1.0F;
                        double d1 = (float) k / 15.0F * 2.0F - 1.0F;
                        double d2 = (float) l / 15.0F * 2.0F - 1.0F;
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        float f = this.radius * (0.7F + this.level.random.nextFloat() * 0.6F);
                        double d4 = getPosition().x;
                        double d6 = getPosition().y;
                        double d8 = getPosition().z;

                        for (; f > 0.0F; f -= 0.22500001F) {
                            BlockPos blockpos = BlockPos.containing(d4, d6, d8);
                            BlockState blockstate = this.level.getBlockState(blockpos);
                            FluidState fluidstate = this.level.getFluidState(blockpos);
                            if (!this.level.isInWorldBounds(blockpos)) {
                                break;
                            }

                            Optional<Float> optional = this.damageCalculator.getBlockExplosionResistance(this, this.level, blockpos, blockstate, fluidstate);
                            if (optional.isPresent()) {
                                f -= (optional.get() + 0.3F) * 0.3F;
                            }

                            if (f > 0.0F && this.damageCalculator.shouldBlockExplode(this, this.level, blockpos, blockstate, f)) {
                                set.add(blockpos);
                            }

                            d4 += d0 * 0.3F;
                            d6 += d1 * 0.3F;
                            d8 += d2 * 0.3F;
                        }
                    }
                }
            }
        }

        this.getToBlow().addAll(set);
        float f2 = this.radius * 2.0F;
        int k1 = Mth.floor(getPosition().x - (double) f2 - 1.0);
        int l1 = Mth.floor(getPosition().x + (double) f2 + 1.0);
        int i2 = Mth.floor(getPosition().y - (double) f2 - 1.0);
        int i1 = Mth.floor(getPosition().y + (double) f2 + 1.0);
        int j2 = Mth.floor(getPosition().z - (double) f2 - 1.0);
        int j1 = Mth.floor(getPosition().z + (double) f2 + 1.0);
        List<Entity> list = this.level.getEntities(getDirectSourceEntity(), new AABB(k1, i2, j2, l1, i1, j1));
        ForgeEventFactory.onExplosionDetonate(this.level, this, list, f2);
        Vec3 vec3 = new Vec3(getPosition().x, getPosition().y, getPosition().z);

        for (Entity entity : list) {
            if (!entity.ignoreExplosion()) {
                double d11 = Math.sqrt(entity.distanceToSqr(vec3)) / (double) f2;
                if (d11 <= 1.0) {
                    double d5 = entity.getX() - getPosition().x;
                    double d7 = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - getPosition().y;
                    double d9 = entity.getZ() - getPosition().z;
                    double d12 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                    if (d12 != 0.0) {
                        d5 /= d12;
                        d7 /= d12;
                        d9 /= d12;
                        if (this.damageCalculator.damagesEntities) {
                            entity.hurt(this.getDamageSource(), this.damageCalculator.getEntityDamageAmount(this, entity));
                        }

                        double d13 = (1.0 - d11) * (double) getSeenPercent(vec3, entity) * this.damageCalculator.getKnockbackMultiplier(entity);
                        double d10;
                        if (entity instanceof LivingEntity livingentity) {
                            d10 = d13 * (1.0 - livingentity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                        } else {
                            d10 = d13;
                        }

                        d5 *= d10;
                        d7 *= d10;
                        d9 *= d10;
                        Vec3 vec31 = new Vec3(d5, d7, d9);
                        entity.setDeltaMovement(entity.getDeltaMovement().add(vec31));
                        if (entity instanceof Player player) {
                            if (!player.isSpectator() && (!player.isCreative() || !player.getAbilities().flying)) {
                                this.getHitPlayers().put(player, vec31);
                            }
                        }
                    }
                }
            }
        }
    }

    public void finalizeExplosion(boolean spawnParticles) {
        if (this.level.isClientSide) {
            this.level
                    .playLocalSound(
                            getPosition().x,
                            getPosition().y,
                            getPosition().z,
                            this.explosionSound,
                            SoundSource.BLOCKS,
                            4.0F,
                            (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F,
                            false
                    );
        }

        boolean flag = this.interactsWithBlocks();
        if (spawnParticles) {
            ParticleOptions particleoptions;
            if (!(this.radius < 2.0F) && flag) {
                particleoptions = this.largeExplosionParticles;
            } else {
                particleoptions = this.smallExplosionParticles;
            }

            level.addParticle(particleoptions, getPosition().x, getPosition().y, getPosition().z, 0.0, 0.0, 0.0);

        }

        if (flag) {
            this.level.getProfiler().push("explosion_blocks");
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();

            Util.shuffle(ObjectArrayList.wrap(getToBlow().toArray(BlockPos[]::new)), this.level.random);

            for (BlockPos blockpos : this.getToBlow()) {
                BlockState blockstate = this.level.getBlockState(blockpos);
                if (!interact) {
                    if (!blockstate.isAir()) {
                        BlockPos blockpos1 = blockpos.immutable();
                        this.level.getProfiler().push("explosion_blocks");
                        if (blockstate.canDropFromExplosion(this.level, blockpos, this)) {
                            if (this.level instanceof ServerLevel serverlevel) {
                                BlockEntity blockentity = blockstate.hasBlockEntity() ? this.level.getBlockEntity(blockpos) : null;
                                LootParams.Builder lootparams$builder = (new LootParams.Builder(serverlevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockpos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockentity).withOptionalParameter(LootContextParams.THIS_ENTITY, getDirectSourceEntity());
                                if (this.blockInteraction == BlockInteraction.DESTROY_WITH_DECAY) {
                                    lootparams$builder.withParameter(LootContextParams.EXPLOSION_RADIUS, this.radius);
                                }
                                boolean flag1 = this.getIndirectSourceEntity() instanceof Player;

                                blockstate.spawnAfterBreak(serverlevel, blockpos, ItemStack.EMPTY, flag1);
                                blockstate.getDrops(lootparams$builder).forEach(p_46074_ -> addBlockDrops(objectarraylist, p_46074_, blockpos1));
                            }
                        }

                        blockstate.onBlockExploded(this.level, blockpos, this);
                        this.level.getProfiler().pop();
                    }
                } else {
                    if (blockstate.getBlock() instanceof AbstractCandleBlock) {
                        if (interact && blockstate.getValue(AbstractCandleBlock.LIT)) {
                            AbstractCandleBlock.extinguish(null, blockstate, this.level, blockpos);
                        }
                    } else if (blockstate.getBlock() instanceof BellBlock bell) {
                        if (interact) {
                            bell.attemptToRing(level, blockpos, null);
                        }
                    } else if (blockstate.getBlock() instanceof ButtonBlock button) {
                        if (interact && !blockstate.getValue(ButtonBlock.POWERED)) {
                            button.press(blockstate, level, blockpos);
                        }
                    } else if (blockstate.getBlock() instanceof DoorBlock door) {
                        if (this.interact
                                && blockstate.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER
                                && door.type().canOpenByHand()
                                && !blockstate.getValue(DoorBlock.POWERED)) {
                            door.setOpen(null, level, blockstate, blockpos, !door.isOpen(blockstate));
                        }
                    } else if (blockstate.getBlock() instanceof FenceGateBlock gate) {
                        if (interact && !blockstate.getValue(FenceGateBlock.POWERED)) {
                            boolean f = blockstate.getValue(FenceGateBlock.OPEN);
                            level.setBlockAndUpdate(blockpos, blockstate.setValue(FenceGateBlock.OPEN, !flag));
                            level.playSound(
                                    null,
                                    blockpos,
                                    f ? ((FenceGateBlockMixinHelper) gate).familiar_face$closeSound() : ((FenceGateBlockMixinHelper) gate).familiar_face$openSound(),
                                    SoundSource.BLOCKS,
                                    1.0F,
                                    level.getRandom().nextFloat() * 0.1F + 0.9F
                            );
                            level.gameEvent(f ? GameEvent.BLOCK_CLOSE : GameEvent.BLOCK_OPEN, blockpos, GameEvent.Context.of(blockstate));
                        }
                    } else if (blockstate.getBlock() instanceof LeverBlock lever) {
                        if (interact) {
                            lever.pull(blockstate, level, blockpos);
                        }
                    } else if (blockstate.getBlock() instanceof TrapDoorBlock trapdoor) {
                        if (interact && trapdoor.type.canOpenByHand() && !blockstate.getValue(TrapDoorBlock.POWERED)) {
                            BlockState blockstate1 = blockstate.cycle(TrapDoorBlock.OPEN);
                            level.setBlock(blockpos, blockstate1, 2);
                            if (blockstate1.getValue(TrapDoorBlock.WATERLOGGED)) {
                                level.scheduleTick(blockpos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
                            }

                            level.playSound(null, blockpos, blockstate1.<Boolean>getValue(TrapDoorBlock.OPEN) ? trapdoor.type.trapdoorOpen() : trapdoor.type.trapdoorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
                            level.gameEvent(null, blockstate1.<Boolean>getValue(TrapDoorBlock.OPEN) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, blockpos);
                        }
                    }
                }
            }

            for (Pair<ItemStack, BlockPos> pair : objectarraylist) {
                Block.popResource(this.level, pair.getSecond(), pair.getFirst());
            }

            this.level.getProfiler().pop();
        }

        if (this.fire) {
            for (BlockPos blockpos1 : this.getToBlow()) {
                if (RANDOM.nextInt(3) == 0
                        && this.level.getBlockState(blockpos1).isAir()
                        && this.level.getBlockState(blockpos1.below()).isSolidRender(this.level, blockpos1.below())) {
                    this.level.setBlockAndUpdate(blockpos1, BaseFireBlock.getState(this.level, blockpos1));
                }
            }
        }
    }
}
