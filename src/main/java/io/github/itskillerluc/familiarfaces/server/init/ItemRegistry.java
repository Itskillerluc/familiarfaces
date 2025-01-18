package io.github.itskillerluc.familiarfaces.server.init;

import io.github.itskillerluc.familiarfaces.FamiliarFaces;
import io.github.itskillerluc.familiarfaces.server.items.WolfArmor;
import io.github.itskillerluc.familiarfaces.server.items.WindChargeItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FamiliarFaces.MODID);

    public static final RegistryObject<ForgeSpawnEggItem> BOGGED_SPAWN_EGG = ITEMS.register("bogged_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypeRegistry.BOGGED, 9084018, 3231003, new Item.Properties()));

    public static final RegistryObject<ForgeSpawnEggItem> BREEZE_SPAWN_EGG = ITEMS.register("breeze_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypeRegistry.BREEZE, 11506911, 9529055, new Item.Properties()));

    public static final RegistryObject<ForgeSpawnEggItem> ARMADILLO_SPAWN_EGG = ITEMS.register("armadillo_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityTypeRegistry.ARMADILLO, 11366765, 8538184, new Item.Properties()));

    public static final RegistryObject<WindChargeItem> WIND_CHARGE = ITEMS.register("wind_charge",
            () -> new WindChargeItem(new Item.Properties()));

    public static final RegistryObject<Item> BREEZE_ROD = ITEMS.register("breeze_rod",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ARMADILLO_SCUTE = ITEMS.register("armadillo_scute",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<WolfArmor> WOLF_ARMOR = ITEMS.register("wolf_armor",
            () -> new WolfArmor(
                    ArmorMaterials.ARMADILLO, true, new Item.Properties().durability(64)
            )
    );

}
