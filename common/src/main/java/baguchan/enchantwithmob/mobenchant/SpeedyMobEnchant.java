package baguchan.enchantwithmob.mobenchant;


public class SpeedyMobEnchant extends MobEnchant {
	public SpeedyMobEnchant(Properties properties) {
		super(properties);
	}
    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + (enchantmentLevel - 1) * 10;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 20;
    }
}
