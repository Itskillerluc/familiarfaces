package io.github.itskilerluc.familiarfaces.client.particle;

import io.github.itskilerluc.familiarfaces.server.init.ParticleTypeRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;

public class GustSeedParticle extends NoRenderParticle {
    private final double scale;
    private final int tickDelayInBetween;

    protected GustSeedParticle(ClientLevel level, double x, double y, double z, double scale, int lifetime, int tickDelayInBetween) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.scale = scale;
        this.lifetime = lifetime;
        this.tickDelayInBetween = tickDelayInBetween;
    }

    @Override
    public void tick() {
        if (this.age % (this.tickDelayInBetween + 1) == 0) {
            for (int i = 0; i < 3; i++) {
                double d0 = this.x + (this.random.nextDouble() - this.random.nextDouble()) * this.scale;
                double d1 = this.y + (this.random.nextDouble() - this.random.nextDouble()) * this.scale;
                double d2 = this.z + (this.random.nextDouble() - this.random.nextDouble()) * this.scale;
                this.level.addParticle(ParticleTypeRegistry.GUST.get(), d0, d1, d2, (double)((float)this.age / (float)this.lifetime), 0.0, 0.0);
            }
        }

        if (this.age++ == this.lifetime) {
            this.remove();
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final double scale;
        private final int lifetime;
        private final int tickDelayInBetween;

        public Provider(double scale, int lifetime, int tickDelayInBetween) {
            this.scale = scale;
            this.lifetime = lifetime;
            this.tickDelayInBetween = tickDelayInBetween;
        }

        public Particle createParticle(
                SimpleParticleType type,
                ClientLevel level,
                double x,
                double y,
                double z,
                double xSpeed,
                double ySpeed,
                double zSpeed
        ) {
            return new GustSeedParticle(level, x, y, z, this.scale, this.lifetime, this.tickDelayInBetween);
        }
    }
}
