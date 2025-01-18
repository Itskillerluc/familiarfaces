package io.github.itskillerluc.familiarfaces.server.init;

import io.github.itskillerluc.familiarfaces.FamiliarFaces;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleTypeRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, FamiliarFaces.MODID);

    public static final RegistryObject<SimpleParticleType> GUST = PARTICLES.register("gust",
            () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> SMALL_GUST = PARTICLES.register("small_gust",
            () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> GUST_EMITTER_LARGE = PARTICLES.register("gust_emitter_large",
            () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> GUST_EMITTER_SMALL = PARTICLES.register("gust_emitter_small",
            () -> new SimpleParticleType(true));
}
