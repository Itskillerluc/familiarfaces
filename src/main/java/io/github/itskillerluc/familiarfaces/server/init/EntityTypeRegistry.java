package io.github.itskillerluc.familiarfaces.server.init;

import io.github.itskillerluc.familiarfaces.FamiliarFaces;
import io.github.itskillerluc.familiarfaces.server.entities.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypeRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FamiliarFaces.MODID);

    public static final RegistryObject<EntityType<Bogged>> BOGGED = ENTITY_TYPES.register("bogged",
            () -> EntityType.Builder.of(Bogged::new, MobCategory.MONSTER).sized(0.6F, 1.99F)
                    .build("bogged"));

    public static final RegistryObject<EntityType<WindCharge>> WIND_CHARGE = ENTITY_TYPES.register("wind_charge",
            () -> EntityType.Builder.<WindCharge>of(WindCharge::new, MobCategory.MISC).sized(0.3125F, 0.3125F)
                    .build("wind_charge"));

    public static final RegistryObject<EntityType<BreezeWindCharge>> BREEZE_WIND_CHARGE = ENTITY_TYPES.register("breeze_wind_charge",
            () -> EntityType.Builder.<BreezeWindCharge>of(BreezeWindCharge::new, MobCategory.MISC).sized(0.3125F, 0.3125F)
                    .build("breeze_wind_charge"));

    public static final RegistryObject<EntityType<Breeze>> BREEZE = ENTITY_TYPES.register("breeze",
            () -> EntityType.Builder.of(Breeze::new, MobCategory.MONSTER).sized(0.6F, 1.77F)
                    .build("breeze"));

    public static final RegistryObject<EntityType<Armadillo>> ARMADILLO = ENTITY_TYPES.register("armadillo",
            () -> EntityType.Builder.of(Armadillo::new, MobCategory.CREATURE).sized(0.7F, 0.65F)
                    .build("armadillo"));
}
