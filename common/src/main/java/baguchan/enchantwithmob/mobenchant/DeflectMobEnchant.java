package baguchan.enchantwithmob.mobenchant;

import baguchan.enchantwithmob.registry.EWMobEnchants;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;


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
        return super.canApplyTogether(ench) && ench != EWMobEnchants.THORN.get();
    }

    @Override
    public boolean isTresureEnchant() {
        return true;
    }
}
