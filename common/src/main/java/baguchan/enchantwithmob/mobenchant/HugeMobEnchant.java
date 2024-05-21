package baguchan.enchantwithmob.mobenchant;


import baguchan.enchantwithmob.platform.Services;
import net.minecraft.world.entity.LivingEntity;

public class HugeMobEnchant extends MobEnchant {
    public HugeMobEnchant(Properties properties) {
        super(properties);
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 20 + (enchantmentLevel - 1) * 10;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 20;
    }


    @Override
    public boolean isCompatibleMob(LivingEntity livingEntity) {
        return super.isCompatibleMob(livingEntity) || Services.CONFIG_HANDLER.getBigYourSelf();
    }


    @Override
    public boolean isTresureEnchant() {
        return true;
    }

}
