package baguchan.enchantwithmob.mobenchant;

import baguchan.enchantwithmob.registry.EWMobEnchants;

public class SlowMobEnchant extends MobEnchant {
    public SlowMobEnchant(Properties properties) {
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
        return true;
    }


    @Override
    protected boolean canApplyTogether(MobEnchant ench) {
        return super.canApplyTogether(ench) && ench != EWMobEnchants.SPEEDY.get() && ench != EWMobEnchants.HASTE.get();
    }
}