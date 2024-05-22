package baguchan.enchantwithmob.network;

import baguchan.enchantwithmob.utils.ResourceLocationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ForgeNetworkHelper {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel SIMPLE_CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocationHelper.modLoc("network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        int idx = 0;
        for (Map.Entry<String, Packet.Handler<?>> entry : Packet.PACKETS.entrySet()) {
            registerMessage(idx++, entry.getValue());
        }
    }

    public static <T extends Packet> void registerMessage(int idx, Packet.Handler<T> handler) {
        SIMPLE_CHANNEL.registerMessage(idx, handler.clazz(), handler.write(), handler.read(), (t, contextSupplier) -> handle(t, contextSupplier, handler.handle()));
    }

    public static <T extends Packet> void sendToPlayer(ServerPlayer playerEntity, T packet) {
        SIMPLE_CHANNEL.sendTo(packet, playerEntity.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <T extends Packet> void sendToEntity(Entity entity, T packet) {
        SIMPLE_CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), packet);
    }

    public static Consumer<net.minecraft.network.protocol.Packet<?>> trackingEntityAndSelf(Supplier<Entity> entitySupplier) {
        return (p) -> {
            Entity entity = (Entity) entitySupplier.get();
            ((ServerChunkCache) entity.getCommandSenderWorld().getChunkSource()).broadcastAndSend(entity, p);
        };
    }

    public static <T extends Packet> void sendToAllPlayers(List<ServerPlayer> playerEntities, T packet) {
        for (ServerPlayer playerEntity : playerEntities) {
            SIMPLE_CHANNEL.sendTo(packet, playerEntity.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    public static <T extends Packet> void sendToServer(T packet) {
        SIMPLE_CHANNEL.sendToServer(packet);
    }

    public static <T extends Packet> void handle(T packet, Supplier<NetworkEvent.Context> ctx, Packet.Handle<T> handle) {
        NetworkEvent.Context context = ctx.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> Client.clientHandle(packet, handle));
        } else {
            ServerPlayer sender = context.getSender();
            handle.handle(packet, sender != null ? sender.level() : null, sender);
        }
        context.setPacketHandled(true);
    }

    private static class Client {
        private static <T extends Packet> void clientHandle(T packet, Packet.Handle<T> handle) {
            handle.handle(packet, Minecraft.getInstance().level, Minecraft.getInstance().player);
        }
    }
}