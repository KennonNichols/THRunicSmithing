package com.thathitmann.runicsmithing.networking;

import com.thathitmann.runicsmithing.RunicSmithing;
import com.thathitmann.runicsmithing.networking.packet.RuneKnowledgeDataSyncC2SPacket;
import com.thathitmann.runicsmithing.networking.packet.RuneKnowledgeDataSyncS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(RunicSmithing.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(RuneKnowledgeDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(RuneKnowledgeDataSyncS2CPacket::new)
                .encoder(RuneKnowledgeDataSyncS2CPacket::toBytes)
                .consumerMainThread(RuneKnowledgeDataSyncS2CPacket::handle)
                .add();



        net.messageBuilder(RuneKnowledgeDataSyncC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RuneKnowledgeDataSyncC2SPacket::new)
                .encoder(RuneKnowledgeDataSyncC2SPacket::toBytes)
                .consumerMainThread(RuneKnowledgeDataSyncC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }


}
