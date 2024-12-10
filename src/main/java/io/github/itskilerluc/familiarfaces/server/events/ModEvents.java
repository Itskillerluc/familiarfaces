package io.github.itskilerluc.familiarfaces.server.events;

import io.github.itskilerluc.familiarfaces.FamiliarFaces;
import io.github.itskilerluc.familiarfaces.server.entities.Bogged;
import io.github.itskilerluc.familiarfaces.server.entities.Breeze;
import io.github.itskilerluc.familiarfaces.server.entities.WindCharge;
import io.github.itskilerluc.familiarfaces.server.init.EntityTypeRegistry;
import io.github.itskilerluc.familiarfaces.server.init.ItemRegistry;
import io.github.itskilerluc.familiarfaces.server.items.WindChargeItem;
import net.minecraft.Util;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = FamiliarFaces.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void entityAttributeCreation(final EntityAttributeCreationEvent event) {
        event.put(EntityTypeRegistry.BOGGED.get(), Bogged.createAttributes().build());
        event.put(EntityTypeRegistry.BREEZE.get(), Breeze.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerItemsToTab(final BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ItemRegistry.BOGGED_SPAWN_EGG);
            event.accept(ItemRegistry.BREEZE_SPAWN_EGG);
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ItemRegistry.WIND_CHARGE);
        }
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(final SpawnPlacementRegisterEvent event) {
        event.register(EntityTypeRegistry.BOGGED.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Bogged::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    @SubscribeEvent
    public static void registerDispenserBehaviour(final FMLCommonSetupEvent event) {
        DispenserBlock.registerBehavior(ItemRegistry.WIND_CHARGE.get(), new AbstractProjectileDispenseBehavior() {
            @Override
            protected Projectile getProjectile(Level pLevel, Position pPosition, ItemStack pStack) {
                return ItemRegistry.WIND_CHARGE.get().asProjectile(pLevel, pPosition, pStack);
            }
        });
    }
}
