package io.github.itskilerluc.familiarfaces.server.networking;

import io.github.itskilerluc.familiarfaces.server.util.AdvancedExplosion;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Explosion;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public final class CustomExplodePacket {
    private final double x;
    private final double y;
    private final double z;
    private final float power;
    private final List<BlockPos> toBlow;
    private final float knockbackX;
    private final float knockbackY;
    private final float knockbackZ;
    private final ParticleOptions smallExplosionParticles;
    private final ParticleOptions largeExplosionParticles;
    private final Explosion.BlockInteraction blockInteraction;
    private final Holder<SoundEvent> explosionSound;


    public CustomExplodePacket(double x, double y, double z, float power, List<BlockPos> toBlow, float knockbackX,
                               float knockbackY, float knockbackZ, ParticleOptions smallExplosionParticles,
                               ParticleOptions largeExplosionParticles, Explosion.BlockInteraction blockInteraction,
                               Holder<SoundEvent> explosionSound) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.power = power;
        this.toBlow = toBlow;
        this.knockbackX = knockbackX;
        this.knockbackY = knockbackY;
        this.knockbackZ = knockbackZ;
        this.smallExplosionParticles = smallExplosionParticles;
        this.largeExplosionParticles = largeExplosionParticles;
        this.blockInteraction = blockInteraction;
        this.explosionSound = explosionSound;
    }

    public CustomExplodePacket(FriendlyByteBuf buffer) {
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
        this.power = buffer.readFloat();
        int i = Mth.floor(x);
        int j = Mth.floor(y);
        int k = Mth.floor(z);
        this.toBlow = buffer.readList(element -> {
            int l = element.readByte() + i;
            int m = element.readByte() + j;
            int n = element.readByte() + k;
            return new BlockPos(l, m, n);
        });
        this.knockbackX = buffer.readFloat();
        this.knockbackY = buffer.readFloat();
        this.knockbackZ = buffer.readFloat();
        this.smallExplosionParticles = ParticleTypes.CODEC.parse(NbtOps.INSTANCE, buffer.readNbt()).result().orElse(ParticleTypes.EXPLOSION_EMITTER);
        this.largeExplosionParticles = ParticleTypes.CODEC.parse(NbtOps.INSTANCE, buffer.readNbt()).result().orElse(ParticleTypes.EXPLOSION);
        this.blockInteraction = buffer.readEnum(Explosion.BlockInteraction.class);
        this.explosionSound = SoundEvent.CODEC.parse(NbtOps.INSTANCE, buffer.readNbt()).result().orElse(null);
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeFloat(power);
        buffer.writeCollection(toBlow, (element, pos) -> {
            element.writeByte(pos.getX() - Mth.floor(x));
            element.writeByte(pos.getY() - Mth.floor(y));
            element.writeByte(pos.getZ() - Mth.floor(z));
        });
        buffer.writeFloat(knockbackX);
        buffer.writeFloat(knockbackY);
        buffer.writeFloat(knockbackZ);
        buffer.writeNbt((CompoundTag) ParticleTypes.CODEC.encodeStart(NbtOps.INSTANCE, smallExplosionParticles).result().orElse(NbtOps.INSTANCE.empty()));
        buffer.writeNbt((CompoundTag) ParticleTypes.CODEC.encodeStart(NbtOps.INSTANCE, largeExplosionParticles).result().orElse(NbtOps.INSTANCE.empty()));
        buffer.writeEnum(blockInteraction);
        buffer.writeNbt((CompoundTag) SoundEvent.CODEC.encodeStart(NbtOps.INSTANCE, explosionSound).result().orElse(NbtOps.INSTANCE.empty()));
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        if (Minecraft.getInstance().level == null || Minecraft.getInstance().player == null) return;
        AdvancedExplosion explosion = new AdvancedExplosion(Minecraft.getInstance().level, null, x, y, z, power, toBlow, blockInteraction, smallExplosionParticles, largeExplosionParticles, explosionSound);
        explosion.finalizeExplosion(true);
        Minecraft.getInstance().player.setDeltaMovement(Minecraft.getInstance().player.getDeltaMovement().add(knockbackX, knockbackY, knockbackZ));
    }
}
