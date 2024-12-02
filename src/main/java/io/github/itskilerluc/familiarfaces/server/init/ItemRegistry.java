package io.github.itskilerluc.familiarfaces.server.init;

import io.github.itskilerluc.familiarfaces.FamiliarFaces;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FamiliarFaces.MODID);

    public static final RegistryObject<ForgeSpawnEggItem> BOGGED_SPAWN_EGG = ITEMS.register("bogged_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypeRegistry.BOGGED, 9084018, 3231003, new Item.Properties()));
}
