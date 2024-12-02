package io.github.itskilerluc.familiarfaces.server.init;

import io.github.itskilerluc.familiarfaces.FamiliarFaces;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class DamageTypeInit {
    public static final ResourceKey<DamageType> WIND_CHARGE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(FamiliarFaces.MODID, "wind_charge"));
}
