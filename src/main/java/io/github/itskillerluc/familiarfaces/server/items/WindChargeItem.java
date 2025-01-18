package io.github.itskillerluc.familiarfaces.server.items;

import io.github.itskillerluc.familiarfaces.server.entities.WindCharge;
import io.github.itskillerluc.familiarfaces.server.init.SoundEventRegistry;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class WindChargeItem extends Item {
    private static final int COOLDOWN = 10;

    public WindChargeItem(Item.Properties p_326377_) {
        super(p_326377_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_326306_, Player player, InteractionHand p_326470_) {
        if (!p_326306_.isClientSide()) {
            WindCharge windcharge = new WindCharge(player, p_326306_, player.position().x(), player.getEyePosition().y(), player.position().z());
            windcharge.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            p_326306_.addFreshEntity(windcharge);
        }

        p_326306_.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEventRegistry.WIND_CHARGE_THROW.get(),
                SoundSource.NEUTRAL,
                0.5F,
                0.4F / (p_326306_.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        ItemStack itemstack = player.getItemInHand(p_326470_);
        player.getCooldowns().addCooldown(this, COOLDOWN);
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(itemstack, p_326306_.isClientSide());
    }


    public Projectile asProjectile(Level p_338589_, Position p_338670_, ItemStack p_338308_) {
        return new WindCharge(p_338589_, p_338670_.x(), p_338670_.y(), p_338670_.z(), Vec3.ZERO);
    }
}
