package io.github.itskilerluc.familiarfaces.server.events;

import io.github.itskilerluc.familiarfaces.FamiliarFaces;
import io.github.itskilerluc.familiarfaces.server.entities.Bogged;
import io.github.itskilerluc.familiarfaces.server.init.EntityTypeRegistry;
import io.github.itskilerluc.familiarfaces.server.init.ItemRegistry;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FamiliarFaces.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void entityAttributeCreation(final EntityAttributeCreationEvent event) {
        event.put(EntityTypeRegistry.BOGGED.get(), Bogged.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerItemsToTab(final BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ItemRegistry.BOGGED_SPAWN_EGG);
        }
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(final SpawnPlacementRegisterEvent event) {
        event.register(EntityTypeRegistry.BOGGED.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Bogged::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
}
