package io.github.itskilerluc.familiarfaces.server.init;

import io.github.itskilerluc.familiarfaces.FamiliarFaces;
import io.github.itskilerluc.familiarfaces.server.effect.InfestedMobEffect;
import io.github.itskilerluc.familiarfaces.server.effect.OozingMobEffect;
import io.github.itskilerluc.familiarfaces.server.effect.WeavingMobEffect;
import io.github.itskilerluc.familiarfaces.server.effect.WindChargedMobEffect;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MobEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, FamiliarFaces.MODID);

    public static final RegistryObject<WeavingMobEffect> WEAVING = MOB_EFFECTS.register("weaving",
            () -> new WeavingMobEffect(MobEffectCategory.HARMFUL, 7891290, p_340706_ -> Mth.randomBetweenInclusive(p_340706_, 2, 3)));

    public static final RegistryObject<OozingMobEffect> OOZING = MOB_EFFECTS.register("oozing",
            () -> new OozingMobEffect(MobEffectCategory.HARMFUL, 10092451, p_338668_ -> 2));

    public static final RegistryObject<WindChargedMobEffect> WIND_CHARGED = MOB_EFFECTS.register("wind_charged",
            () -> new WindChargedMobEffect(MobEffectCategory.HARMFUL, 12438015));

    public static final RegistryObject<InfestedMobEffect> INFESTED = MOB_EFFECTS.register("infested",
            () -> new InfestedMobEffect(MobEffectCategory.HARMFUL, 9214860, 0.1F, p_340705_ -> Mth.randomBetweenInclusive(p_340705_, 1, 2)));
}
