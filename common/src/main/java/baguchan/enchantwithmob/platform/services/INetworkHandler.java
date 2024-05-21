package baguchan.enchantwithmob.platform.services;

import baguchan.enchantwithmob.network.Packet;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public interface INetworkHandler {

    <P extends Packet> void sendToClient(ServerPlayer player, P packet);

    default <P extends Packet> void sendToAllClients(List<ServerPlayer> players, P packet) {
        for (ServerPlayer player : players) {
            sendToClient(player, packet);
        }
    }

    <P extends Packet> void sendToServer(P packet);
}
