package io.github.itskilerluc.familiarfaces.server.networking;

import io.github.itskilerluc.familiarfaces.server.capability.WolfArmorCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncWolfArmorPacket {
    private final ItemStack stack;
    private final int entityId;
    private final float dropChance;

    public SyncWolfArmorPacket(ItemStack stack, float dropChance, Wolf wolf) {
        this.stack = stack;
        this.dropChance = dropChance;
        entityId = wolf.getId();
    }

    public SyncWolfArmorPacket(FriendlyByteBuf buffer) {
        stack = buffer.readItem();
        entityId = buffer.readInt();
        dropChance = buffer.readFloat();
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeItemStack(stack, false);
        buffer.writeInt(entityId);
        buffer.writeFloat(dropChance);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        if (Minecraft.getInstance().level == null || Minecraft.getInstance().player == null) return;
        contextSupplier.get().enqueueWork(() -> {
            var entity = Minecraft.getInstance().level.getEntity(entityId);
            if (entity != null) {
                entity.getCapability(WolfArmorCapabilityProvider.WOLF_ARMOR_CAPABILITY).ifPresent(cap -> {
                    cap.setBodyArmorItem(stack);
                    cap.setBodyArmorDropChance(dropChance);
                });
            }
        });
    }
}
