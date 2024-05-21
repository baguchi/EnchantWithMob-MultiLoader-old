package baguchan.enchantwithmob.network.packet;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.capability.MobEnchantCapability;
import baguchan.enchantwithmob.network.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class AncientMessage implements Packet {

    private int entityId;
    private boolean isAncient;

    public AncientMessage() {

    }

    public AncientMessage(Entity entity, boolean ancient) {
        this.entityId = entity.getId();
        this.isAncient = ancient;
    }

    public AncientMessage(int id, boolean ancient) {
        this.entityId = id;
        this.isAncient = ancient;
    }


    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeBoolean(this.isAncient);
    }

    public static AncientMessage readFromPacket(FriendlyByteBuf buffer) {
        return new AncientMessage(buffer.readInt(), buffer.readBoolean());
    }

    @Override
    public void handle(@Nullable Level level, @Nullable Player player) {

        Entity entity = Minecraft.getInstance().player.level().getEntity(entityId);
        if (entity != null && entity instanceof LivingEntity livingEntity) {
            if (livingEntity instanceof IEnchantCap cap) {
                cap.getEnchantCap().setEnchantType((LivingEntity) entity, isAncient ? MobEnchantCapability.EnchantType.ANCIENT : MobEnchantCapability.EnchantType.NORMAL);
            }

        }

    }
}