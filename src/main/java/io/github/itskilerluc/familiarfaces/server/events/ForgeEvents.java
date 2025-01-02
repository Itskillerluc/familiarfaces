package io.github.itskilerluc.familiarfaces.server.events;

import io.github.itskilerluc.familiarfaces.FamiliarFaces;
import io.github.itskilerluc.familiarfaces.server.capability.WolfArmorCapabilityProvider;
import io.github.itskilerluc.familiarfaces.server.entities.ai.WolfCrackiness;
import io.github.itskilerluc.familiarfaces.server.init.ArmorMaterials;
import io.github.itskilerluc.familiarfaces.server.init.ItemRegistry;
import io.github.itskilerluc.familiarfaces.server.init.SoundEventRegistry;
import io.github.itskilerluc.familiarfaces.server.networking.FamiliarFacesNetwork;
import io.github.itskilerluc.familiarfaces.server.networking.SyncWolfArmorPacket;
import io.github.itskilerluc.familiarfaces.server.util.Util;
import io.github.itskilerluc.familiarfaces.server.util.WolfArmorUtils;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = FamiliarFaces.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    @SubscribeEvent
    public static void attachCapabilities(final AttachCapabilitiesEvent<Entity> event) {
        event.addCapability(WolfArmorCapabilityProvider.IDENTIFIER, new WolfArmorCapabilityProvider());
    }

    @SubscribeEvent
    public static void damageEvent(final LivingDamageEvent event) {
        if (event.getEntity() instanceof Wolf wolf) {
            if (WolfArmorUtils.canArmorAbsorb(event.getSource(), wolf)) {
                event.setCanceled(true);
                ItemStack itemstack = WolfArmorUtils.getBodyArmorItem(wolf);
                int i = itemstack.getDamageValue();
                int j = itemstack.getMaxDamage();
                itemstack.hurtAndBreak(Mth.ceil(event.getAmount()), wolf, (p_148282_) -> {
                    p_148282_.broadcastBreakEvent(EquipmentSlot.CHEST);
                });
                WolfArmorUtils.setBodyArmorItem(wolf, itemstack);
                if (WolfCrackiness.WOLF_ARMOR.byDamage(i, j) != WolfCrackiness.WOLF_ARMOR.byDamage(WolfArmorUtils.getBodyArmorItem(wolf))) {
                    wolf.playSound(SoundEventRegistry.WOLF_ARMOR_CRACK.get());
                    if (event.getEntity().level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ItemRegistry.ARMADILLO_SCUTE.get().getDefaultInstance()),
                                wolf.getX(),
                                wolf.getY() + 1.0,
                                wolf.getZ(),
                                20,
                                0.2,
                                0.1,
                                0.2,
                                0.1
                        );
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void interactionEvent(final PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof Wolf wolf) {
            ItemStack itemstack = event.getItemStack();
            Player player = event.getEntity();
            if (itemstack.is(ItemRegistry.WOLF_ARMOR.get()) && wolf.isOwnedBy(player) && WolfArmorUtils.getBodyArmorItem(wolf).isEmpty() && !wolf.isBaby()) {
                WolfArmorUtils.setBodyArmorItem(wolf, itemstack.copyWithCount(1));
                Util.consume(1, player, itemstack);
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
                wolf.playSound(SoundEventRegistry.WOLF_ARMOR_EQUIP.get());
            } else if (itemstack.is(Items.SHEARS)
                    && wolf.isOwnedBy(player)
                    && WolfArmorUtils.hasArmor(wolf)
                    && (!EnchantmentHelper.hasBindingCurse(WolfArmorUtils.getBodyArmorItem(wolf)) || player.isCreative())) {
                itemstack.hurtAndBreak(1, player, (p_148282_) -> {
                    p_148282_.broadcastBreakEvent(event.getHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                });
                wolf.playSound(SoundEventRegistry.WOLF_ARMOR_UNEQUIP.get());
                ItemStack itemstack1 = WolfArmorUtils.getBodyArmorItem(wolf);
                WolfArmorUtils.setBodyArmorItem(wolf, ItemStack.EMPTY);
                wolf.spawnAtLocation(itemstack1);
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            } else if (ArmorMaterials.ARMADILLO.getRepairIngredient().test(itemstack)
                    && wolf.isInSittingPose()
                    && WolfArmorUtils.hasArmor(wolf)
                    && wolf.isOwnedBy(player)
                    && WolfArmorUtils.getBodyArmorItem(wolf).isDamaged()) {
                itemstack.shrink(1);
                wolf.playSound(SoundEventRegistry.WOLF_ARMOR_REPAIR.get());
                ItemStack itemstack2 = WolfArmorUtils.getBodyArmorItem(wolf);

                int i = (int) ((float) itemstack2.getMaxDamage() * 0.125F);
                itemstack2.setDamageValue(Math.max(0, itemstack2.getDamageValue() - i));
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public static void onPlayerJoin(final PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof Wolf wolf) {
            wolf.getCapability(WolfArmorCapabilityProvider.WOLF_ARMOR_CAPABILITY).ifPresent((capability) -> {
                FamiliarFacesNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> ((ServerPlayer) event.getEntity())), new SyncWolfArmorPacket(capability.getBodyArmorItem(), capability.getBodyArmorDropChance(), wolf));
            });
        }
    }
}



