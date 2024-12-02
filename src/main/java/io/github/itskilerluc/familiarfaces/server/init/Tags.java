package io.github.itskilerluc.familiarfaces.server.init;

import io.github.itskilerluc.familiarfaces.FamiliarFaces;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

public class Tags {
    public static class Blocks {
        public static final TagKey<Block> BLOCKS_WIND_CHARGE_EXPLOSIONS =
                BlockTags.create(new ResourceLocation(FamiliarFaces.MODID, "blocks_wind_charge_explosions"));
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> REDIRECTABLE_PROJECTILE =
                TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(FamiliarFaces.MODID, "redirectable_projectile"));
    }
}
