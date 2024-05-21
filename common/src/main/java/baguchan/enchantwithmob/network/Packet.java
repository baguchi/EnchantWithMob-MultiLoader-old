package baguchan.enchantwithmob.network;

import baguchan.enchantwithmob.network.packet.*;
import baguchan.enchantwithmob.utils.ResourceLocationHelper;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface Packet {
    Map<String, Handler<?>> PACKETS = Util.make(new HashMap<>(), map -> {
        map.put(ResourceLocationHelper.modLoc("ancient").toString(), new Handler<>(AncientMessage.class, PacketDirection.SERVER_TO_CLIENT, AncientMessage::write, AncientMessage::readFromPacket, AncientMessage::handle));
        map.put(ResourceLocationHelper.modLoc("mob_enchanted").toString(), new Handler<>(MobEnchantedMessage.class, PacketDirection.SERVER_TO_CLIENT, MobEnchantedMessage::write, MobEnchantedMessage::readFromPacket, MobEnchantedMessage::handle));
        map.put(ResourceLocationHelper.modLoc("mob_enchanted_from_owner").toString(), new Handler<>(MobEnchantFromOwnerMessage.class, PacketDirection.SERVER_TO_CLIENT, MobEnchantFromOwnerMessage::write, MobEnchantFromOwnerMessage::readFromPacket, MobEnchantFromOwnerMessage::handle));
        map.put(ResourceLocationHelper.modLoc("remove_all_mob_enchant").toString(), new Handler<>(RemoveAllMobEnchantMessage.class, PacketDirection.SERVER_TO_CLIENT, RemoveAllMobEnchantMessage::write, RemoveAllMobEnchantMessage::readFromPacket, RemoveAllMobEnchantMessage::handle));
        map.put(ResourceLocationHelper.modLoc("remove_all_mob_enchant_from_owner").toString(), new Handler<>(RemoveMobEnchantOwnerMessage.class, PacketDirection.SERVER_TO_CLIENT, RemoveMobEnchantOwnerMessage::write, RemoveMobEnchantOwnerMessage::readFromPacket, RemoveMobEnchantOwnerMessage::handle));
        map.put(ResourceLocationHelper.modLoc("sync_mob_enchant").toString(), new Handler<>(SyncEntityPacketToServer.class, PacketDirection.SERVER_TO_CLIENT, SyncEntityPacketToServer::write, SyncEntityPacketToServer::read, SyncEntityPacketToServer::handle));
    });


    void write(FriendlyByteBuf buf);

    void handle(@Nullable Level level, @Nullable Player player);

    record Handler<T extends Packet>(Class<T> clazz, PacketDirection direction, BiConsumer<T, FriendlyByteBuf> write,
                                     Function<FriendlyByteBuf, T> read,
                                     Handle<T> handle) {
    }

    enum PacketDirection {
        SERVER_TO_CLIENT,
        CLIENT_TO_SERVER
    }

    @FunctionalInterface
    interface Handle<T extends Packet> {
        void handle(T packet, Level level, Player player);
    }
}