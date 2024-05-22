package baguchan.enchantwithmob.platform;

import baguchan.enchantwithmob.network.FabricNetworkHelper;
import baguchan.enchantwithmob.network.Packet;
import baguchan.enchantwithmob.platform.services.INetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class FabricNetworkHandler implements INetworkHandler {

    @Override
    public <P extends Packet> void sendToClient(ServerPlayer player, P packet) {
        FabricNetworkHelper.sendToPlayer(player, packet);
    }

    @Override
    public <P extends Packet> void sendToEntity(Entity player, P packet) {
        FabricNetworkHelper.sendToEntity(player, packet);
    }

    @Override
    public <P extends Packet> void sendToServer(P packet) {
        FabricNetworkHelper.sendToServer(packet);
    }
}
