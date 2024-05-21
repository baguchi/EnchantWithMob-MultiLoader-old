package baguchan.enchantwithmob.mobenchant;


import baguchan.enchantwithmob.capability.MobEnchantCapability;
import baguchan.enchantwithmob.registry.EWMobEnchants;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
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


    public static float getDamageIncrease(float damage, MobEnchantCapability cap) {
        int level = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getMobEnchants(), EWMobEnchants.HUGE.get());
        if (level > 0) {
            damage *= 1.0F + level * 0.15F;
        }
        return damage;
    }

    @Override
    public boolean isTresureEnchant() {
        return true;
    }

}
