package baguchan.enchantwithmob.network.packet;

import baguchan.enchantwithmob.api.IEnchantPacket;
import baguchan.enchantwithmob.network.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SyncEntityPacketToServer implements Packet {


    private final UUID uuid;

    public SyncEntityPacketToServer(UUID uuid) {
        this.uuid = uuid;
    }

    public static SyncEntityPacketToServer read(FriendlyByteBuf buf) {
        return  new SyncEntityPacketToServer(buf.readUUID());
    }

    @Override
    public void handle(@Nullable Level level, @Nullable Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            Entity entity = serverPlayer.serverLevel().getEntity(uuid);
            if (entity instanceof IEnchantPacket baguPacket) {
                baguPacket.resync(entity);
            }
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.uuid);
    }
}