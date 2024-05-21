package baguchan.enchantwithmob.mobenchant;

import baguchan.enchantwithmob.registry.EWMobEnchants;


public class DeflectMobEnchant extends MobEnchant {
    public DeflectMobEnchant(Properties properties) {
        super(properties);
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 30;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 30;
    }

    @Override
    protected boolean canApplyTogether(MobEnchant ench) {
        return super.canApplyTogether(ench) && ench != EWMobEnchants.THORN;
    }

    @Override
    public boolean isTresureEnchant() {
        return true;
    }
}
