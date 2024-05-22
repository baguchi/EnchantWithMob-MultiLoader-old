package baguchan.enchantwithmob.platform;

import baguchan.enchantwithmob.network.ForgeNetworkHelper;
import baguchan.enchantwithmob.network.Packet;
import baguchan.enchantwithmob.platform.services.INetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class ForgeNetworkHandler implements INetworkHandler {
    @Override
    public <P extends Packet> void sendToClient(ServerPlayer player, P packet) {
        ForgeNetworkHelper.sendToPlayer(player, packet);
    }

    @Override
    public <P extends Packet> void sendToEntity(Entity player, P packet) {
        ForgeNetworkHelper.sendToEntity(player, packet);
    }

    @Override
    public <P extends Packet> void sendToServer(P packet) {
        ForgeNetworkHelper.sendToServer(packet);
    }
}
