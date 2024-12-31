package io.github.itskilerluc.familiarfaces.server.networking;

import io.github.itskilerluc.familiarfaces.FamiliarFaces;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class FamiliarFacesNetwork {
    public static final String VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(FamiliarFaces.MODID, "network"))
        .clientAcceptedVersions(VERSION::equals)
        .serverAcceptedVersions(VERSION::equals)
        .networkProtocolVersion(() -> VERSION)
        .simpleChannel();

    public static void register() {
        CHANNEL.messageBuilder(CustomExplodePacket.class, 0)
                .encoder(CustomExplodePacket::write)
                .decoder(CustomExplodePacket::new)
                .consumerMainThread(CustomExplodePacket::handle)
                .add();

        CHANNEL.messageBuilder(SyncWolfArmorPacket.class, 1)
                .encoder(SyncWolfArmorPacket::write)
                .decoder(SyncWolfArmorPacket::new)
                .consumerMainThread(SyncWolfArmorPacket::handle)
                .add();
    }
}

