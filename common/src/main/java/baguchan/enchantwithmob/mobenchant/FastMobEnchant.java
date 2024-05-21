package baguchan.enchantwithmob.mobenchant;

import baguchan.enchantwithmob.registry.EWMobEnchants;
import net.minecraft.world.entity.LivingEntity;

public class FastMobEnchant extends MobEnchant {
    public FastMobEnchant(Properties properties) {
        super(properties);
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + (enchantmentLevel - 1) * 10;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 20;
    }

    @Override
    public boolean isTresureEnchant() {
        return true;
    }

    public boolean isCursedEnchant() {
        return false;
    }


    @Override
    protected boolean canApplyTogether(MobEnchant ench) {
        return super.canApplyTogether(ench) && ench != EWMobEnchants.SPEEDY.get() && ench != EWMobEnchants.SLOW.get();
    }
}