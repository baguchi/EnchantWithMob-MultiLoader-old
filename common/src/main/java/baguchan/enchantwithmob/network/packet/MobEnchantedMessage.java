package baguchan.enchantwithmob.network.packet;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.capability.MobEnchantHandler;
import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.network.Packet;
import baguchan.enchantwithmob.registry.EWModRegistry;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class MobEnchantedMessage implements Packet {


    private int entityId;
    private MobEnchant enchantType;
    private int level;

    public MobEnchantedMessage(Entity entity, MobEnchantHandler enchantType) {
        this.entityId = entity.getId();
        this.enchantType = enchantType.getMobEnchant();
        this.level = enchantType.getEnchantLevel();
    }

    public MobEnchantedMessage(int id, MobEnchantHandler enchantType) {
        this.entityId = id;
        this.enchantType = enchantType.getMobEnchant();
        this.level = enchantType.getEnchantLevel();
    }

    public MobEnchantedMessage(Entity entity, MobEnchant enchantType, int level) {
        this.entityId = entity.getId();
        this.enchantType = enchantType;
        this.level = level;
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeResourceKey(EWModRegistry.MOB_ENCHANT_REGISTRY.getResourceKey(this.enchantType).get());
        buffer.writeInt(this.level);
    }

    public static MobEnchantedMessage readFromPacket(FriendlyByteBuf buffer) {
        return new MobEnchantedMessage(buffer.readInt(), new MobEnchantHandler(EWModRegistry.MOB_ENCHANT_REGISTRY.get(buffer.readResourceKey(EWModRegistry.MOB_ENCHANT_REGISTRY_KEY)), buffer.readInt()));
    }

    @Override
    public void handle(@Nullable Level level, @Nullable Player player) {
        Entity entity = Minecraft.getInstance().player.level().getEntity(this.entityId);
                if (entity != null && entity instanceof LivingEntity livingEntity) {
                    if (livingEntity instanceof IEnchantCap cap) {
                        if (!MobEnchantUtils.findMobEnchantHandler(cap.getEnchantCap().getMobEnchants(), this.enchantType)) {
                            cap.getEnchantCap().addMobEnchant((LivingEntity) entity, this.enchantType, this.level);
                        }
                    }
                }
    }
}