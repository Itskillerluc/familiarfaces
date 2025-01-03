package io.github.itskilerluc.familiarfaces.server.init;

import io.github.itskilerluc.familiarfaces.FamiliarFaces;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PotionRegistry {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, FamiliarFaces.MODID);

    public static final RegistryObject<Potion> WEAVING = POTIONS.register("weaving",
            () -> new Potion(new MobEffectInstance(MobEffectRegistry.WEAVING.get(), 3600)));

    public static final RegistryObject<Potion> OOZING = POTIONS.register("oozing",
            () -> new Potion(new MobEffectInstance(MobEffectRegistry.OOZING.get(), 3600)));

    public static final RegistryObject<Potion> WIND_CHARGED = POTIONS.register("wind_charged",
            () -> new Potion(new MobEffectInstance(MobEffectRegistry.WIND_CHARGED.get(), 3600)));

    public static final RegistryObject<Potion> INFESTED = POTIONS.register("infested",
            () -> new Potion(new MobEffectInstance(MobEffectRegistry.INFESTED.get(), 3600)));
}
