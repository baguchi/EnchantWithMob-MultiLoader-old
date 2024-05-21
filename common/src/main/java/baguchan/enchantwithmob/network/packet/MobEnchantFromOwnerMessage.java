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

public class MobEnchantFromOwnerMessage implements Packet {

    private int entityId;
    private int ownerID;

    public MobEnchantFromOwnerMessage(Entity entity, Entity ownerEntity) {
        this.entityId = entity.getId();
        this.ownerID = ownerEntity.getId();
    }

    public MobEnchantFromOwnerMessage(int id, int ownerID) {
        this.entityId = id;
        this.ownerID = ownerID;
    }


    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeInt(this.ownerID);
    }
    public static MobEnchantFromOwnerMessage readFromPacket(FriendlyByteBuf buffer) {
        return new MobEnchantFromOwnerMessage(buffer.readInt(), buffer.readInt());
    }

    @Override
    public void handle(@Nullable Level level, @Nullable Player player) {
            Entity entity = Minecraft.getInstance().player.level().getEntity(entityId);
            Entity ownerEntity = Minecraft.getInstance().player.level().getEntity(ownerID);
            if (entity instanceof LivingEntity livingEntity && ownerEntity instanceof LivingEntity) {
                    if (livingEntity instanceof IEnchantCap cap) {
                        cap.getEnchantCap().addOwner((LivingEntity) entity, (LivingEntity) ownerEntity);
                    }
                }
    }
}