package baguchan.enchantwithmob.network.packet;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.network.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class RemoveAllMobEnchantMessage implements Packet {


    private int entityId;

    public static RemoveAllMobEnchantMessage readFromPacket(FriendlyByteBuf buffer) {
        return new RemoveAllMobEnchantMessage(buffer.readInt());
    }

    public RemoveAllMobEnchantMessage(Entity entity) {
        this.entityId = entity.getId();
    }

    public RemoveAllMobEnchantMessage(int id) {
        this.entityId = id;
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
    }

    @Override
    public void handle(@Nullable Level level, @Nullable Player player) {
        Entity entity = Minecraft.getInstance().player.level().getEntity(entityId);
        if (entity != null && entity instanceof LivingEntity livingEntity) {
            if (livingEntity instanceof IEnchantCap cap) {
                cap.getEnchantCap().removeAllMobEnchant((LivingEntity) entity);
            }
        }
    }
}