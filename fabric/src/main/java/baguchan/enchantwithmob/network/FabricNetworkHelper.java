package baguchan.enchantwithmob.network;


import baguchan.enchantwithmob.EWConstants;
import baguchan.enchantwithmob.utils.ResourceLocationHelper;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class FabricNetworkHelper {

    private static final String PACKET_LOCATION = EWConstants.MOD_ID;
    private static final Function<String, ResourceLocation> PACKET_ID = id -> {
        ResourceLocation value = new ResourceLocation(id);

        return value.getNamespace().equals("minecraft") ? ResourceLocationHelper.modLoc(id) : value;
    };

    private static final Map<Class<? extends Packet>, BiConsumer<?, FriendlyByteBuf>> ENCODERS = new ConcurrentHashMap<>();
    private static final Map<Class<? extends Packet>, ResourceLocation> PACKET_IDS = new ConcurrentHashMap<>();

    public static void init() {
        Packet.PACKETS.forEach(FabricNetworkHelper::register);
    }

    private static <T extends Packet> void register(String path, Packet.Handler<T> handler) {
        registerMessage(path, handler.clazz(), handler.write(), handler.read(), handler.handle());
    }

    private static <T extends Packet> void registerMessage(String id, Class<T> clazz,
                                                           BiConsumer<T, FriendlyByteBuf> encode,
                                                           Function<FriendlyByteBuf, T> decode,
                                                           Packet.Handle<T> handler) {
        ENCODERS.put(clazz, encode);
        PACKET_IDS.put(clazz, PACKET_ID.apply(id));


        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientProxy.registerClientReceiver(id, decode, handler);
        }
        ServerProxy.registerServerReceiver(id, decode, handler);

    }


    public static <MSG extends Packet> void sendToPlayer(ServerPlayer player, MSG packet) {
        ResourceLocation packetId = PACKET_IDS.get(packet.getClass());
        @SuppressWarnings("unchecked")
        BiConsumer<MSG, FriendlyByteBuf> encoder = (BiConsumer<MSG, FriendlyByteBuf>) ENCODERS.get(packet.getClass());
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        encoder.accept(packet, buf);
        ServerPlayNetworking.send(player, packetId, buf);
    }

    public static <MSG extends Packet> void sendToAllPlayers(List<ServerPlayer> players, MSG packet) {
        players.forEach(player -> sendToPlayer(player, packet));
    }

    public static <MSG extends Packet> void sendToServer(MSG packet) {
        ResourceLocation packetId = PACKET_IDS.get(packet.getClass());
        @SuppressWarnings("unchecked")
        BiConsumer<MSG, FriendlyByteBuf> encoder = (BiConsumer<MSG, FriendlyByteBuf>) ENCODERS.get(packet.getClass());
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        encoder.accept(packet, buf);
        ClientPlayNetworking.send(packetId, buf);
    }

    public static <MSG extends Packet> void sendToEntity(Entity entity, MSG packet) {
        ResourceLocation packetId = PACKET_IDS.get(packet.getClass());
        @SuppressWarnings("unchecked")
        BiConsumer<MSG, FriendlyByteBuf> encoder = (BiConsumer<MSG, FriendlyByteBuf>) ENCODERS.get(packet.getClass());
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        encoder.accept(packet, buf);
        collectPacketsToServer(ofTrackingEntity(() -> entity), packetId, buf);
    }

    public static void collectPacketsToServer(Consumer<net.minecraft.network.protocol.Packet> sink, ResourceLocation id, FriendlyByteBuf buf) {
        sink.accept(toPacketToServer(id, buf));

    }

    public static net.minecraft.network.protocol.Packet<ClientGamePacketListener> toPacketToServer(ResourceLocation id, FriendlyByteBuf buf) {
        return ServerPlayNetworking.createS2CPacket(id, buf);
    }

    public static Consumer<net.minecraft.network.protocol.Packet> ofTrackingEntity(final Supplier<Entity> entitySupplier) {
        return packet -> {
            final Entity entity = entitySupplier.get();
            ((ServerChunkCache) entity.getCommandSenderWorld().getChunkSource()).broadcastAndSend(entity, packet);
        };
    }

    public record ClientProxy() {

        public static <T extends Packet> void registerClientReceiver(String id, Function<FriendlyByteBuf, T> decode,
                                                                     Packet.Handle<T> handler) {
            ClientPlayNetworking.registerGlobalReceiver(PACKET_ID.apply(id), (client, listener, buf, responseSender) -> {
                buf.retain();
                client.execute(() -> {
                    T packet = decode.apply(buf);
                    ClientLevel level = client.level;
                    if (level != null) {
                        try {
                            handler.handle(packet, level, Minecraft.getInstance().player);
                        } catch (Throwable throwable) {
                            throw throwable;
                        }
                    }
                    buf.release();
                });
            });
        }
    }

    public static class ServerProxy {
        private static <T extends Packet> void registerServerReceiver(String id, Function<FriendlyByteBuf, T> decode, Packet.Handle<T> handler) {
            ServerPlayNetworking.registerGlobalReceiver(PACKET_ID.apply(id), (server, player, handler1, buf, responseSender) -> {
                buf.retain();
                server.execute(() -> {
                    T packet = decode.apply(buf);
                    Level level = player.level();
                    try {
                        handler.handle(packet, level, player);
                    } catch (Throwable throwable) {
                        throw throwable;
                    }
                    buf.release();
                });
            });
        }
    }
}