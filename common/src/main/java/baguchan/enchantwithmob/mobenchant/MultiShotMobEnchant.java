package baguchan.enchantwithmob.mobenchant;

import baguchan.enchantwithmob.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Witch;

public class MultiShotMobEnchant extends MobEnchant {

    public MultiShotMobEnchant(Properties properties) {
        super(properties);
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 10;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 40;
    }

    @Override
    public boolean isCompatibleMob(LivingEntity livingEntity) {
        return Services.CONFIG_HANDLER.getWhitelistShootEntity().contains(BuiltInRegistries.ENTITY_TYPE.getKey(livingEntity.getType()).toString()) && !(livingEntity instanceof Witch) || super.isCompatibleMob(livingEntity);
    }
}
