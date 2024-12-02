package io.github.itskilerluc.familiarfaces.server.entities;

import io.github.itskilerluc.familiarfaces.FamiliarFaces;
import io.github.itskilerluc.familiarfaces.server.init.SoundEventRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.IForgeShearable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Bogged extends AbstractSkeleton implements IForgeShearable {
    private static final ResourceLocation SHEAR_LOOT = new ResourceLocation(FamiliarFaces.MODID, "shear_bogged");

    private static final EntityDataAccessor<Boolean> DATA_SHEARED = SynchedEntityData.defineId(Bogged.class, EntityDataSerializers.BOOLEAN);


    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return AbstractSkeleton.createAttributes().add(Attributes.MAX_HEALTH, 16.0);
    }

    public Bogged(EntityType<? extends Bogged> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_SHEARED, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("sheared", this.isSheared());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSheared(compound.getBoolean("sheared"));
    }

    public boolean isSheared() {
        return this.entityData.get(DATA_SHEARED);
    }

    public void setSheared(boolean sheared) {
        this.entityData.set(DATA_SHEARED, sheared);
    }

    @Override
    public @NotNull List<ItemStack> onSheared(@Nullable Player player, @NotNull ItemStack item, Level world, BlockPos pos, int fortune) {
        world.playSound(null, this, SoundEvents.SHEEP_SHEAR, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0F, 1.0F);
        this.gameEvent(GameEvent.SHEAR, player);
        if (!world.isClientSide) {
            this.setSheared(true);
            return getShearedMushrooms();
        }
        return java.util.Collections.emptyList();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEventRegistry.BOGGED_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEventRegistry.BOGGED_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEventRegistry.BOGGED_DEATH.get();
    }

    @Override
    protected @NotNull SoundEvent getStepSound() {
        return SoundEventRegistry.BOGGED_STEP.get();
    }

    @Override
    protected @NotNull AbstractArrow getArrow(@NotNull ItemStack stack, float velocity) {
        AbstractArrow abstractarrow = super.getArrow(stack, velocity);
        if (abstractarrow instanceof Arrow arrow) {
            arrow.addEffect(new MobEffectInstance(MobEffects.POISON, 100));
        }

        return abstractarrow;
    }


    private List<ItemStack> getShearedMushrooms() {
        if (this.level() instanceof ServerLevel serverlevel) {

            LootTable loottable = serverlevel.getServer().getLootData().getLootTable(SHEAR_LOOT);
            LootParams lootparams = new LootParams.Builder(serverlevel)
                    .withParameter(LootContextParams.ORIGIN, this.position())
                    .withParameter(LootContextParams.THIS_ENTITY, this)
                    .create(LootContextParamSets.SELECTOR);

            return new ArrayList<>(loottable.getRandomItems(lootparams));
        }
        return new ArrayList<>();
    }

    @Override
    public float getEyeHeight(Pose p_20237_) {
        return 1.74F;
    }

    @Override
    public double getMyRidingOffset() {
        return -0.7F;
    }
}
