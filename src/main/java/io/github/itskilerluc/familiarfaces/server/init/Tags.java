package io.github.itskilerluc.familiarfaces.server.init;

import io.github.itskilerluc.familiarfaces.FamiliarFaces;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class Tags {
    public static class Blocks {
        public static final TagKey<Block> BLOCKS_WIND_CHARGE_EXPLOSIONS =
                BlockTags.create(new ResourceLocation(FamiliarFaces.MODID, "blocks_wind_charge_explosions"));
        public static final TagKey<Block> ARMADILLO_SPAWNABLE_ON =
                BlockTags.create(new ResourceLocation(FamiliarFaces.MODID, "armadillo_spawnable_on"));
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> REDIRECTABLE_PROJECTILE =
                TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(FamiliarFaces.MODID, "redirectable_projectile"));
    }

    public static class Items {
        public static final TagKey<Item> ARMADILLO_FOOD =
                ItemTags.create(new ResourceLocation(FamiliarFaces.MODID, "armadillo_food"));
    }

    public static class DamageTypes {
        public static final TagKey<DamageType> PANIC_ENVIRONMENTAL_CAUSES = TagKey.create(Registries.DAMAGE_TYPE,
                new ResourceLocation(FamiliarFaces.MODID, "panic_environmental_causes"));

        public static final TagKey<DamageType> BYPASSES_WOLF_ARMOR = TagKey.create(Registries.DAMAGE_TYPE,
                new ResourceLocation(FamiliarFaces.MODID, "bypasses_wolf_armor"));
    }
}
