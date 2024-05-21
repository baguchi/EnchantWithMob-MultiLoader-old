package baguchan.enchantwithmob.mixins.client;

import baguchan.enchantwithmob.api.IEnchantPacket;
import baguchan.enchantwithmob.network.packet.SyncEntityPacketToServer;
import baguchan.enchantwithmob.platform.Services;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Inject(method = "handleAddEntity(Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;recreateFromPacket(Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            require = 0
    )
    private void syncEntity(
            ClientboundAddEntityPacket p_104958_, CallbackInfo ci, Entity entity) {
        if (entity instanceof IEnchantPacket && entity.level().isClientSide()) {
            Services.NETWORK_HANDLER.sendToServer(new SyncEntityPacketToServer(entity.getUUID()));
        }
    }
}