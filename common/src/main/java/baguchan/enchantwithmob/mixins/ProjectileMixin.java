package baguchan.enchantwithmob.mixins;

import baguchan.enchantwithmob.api.IEnchantedProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public abstract class ProjectileMixin extends Entity implements IEnchantedProjectile {
    public boolean enchantWithMob_MultiLoader$enchantProjectile;

    public ProjectileMixin(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    public boolean hasEnchantProjectile() {
        return enchantWithMob_MultiLoader$enchantProjectile;
    }

    @Override
    public void setEnchantProjectile(boolean enchantVisual) {
        this.enchantWithMob_MultiLoader$enchantProjectile = enchantVisual;
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    protected void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        this.setEnchantProjectile(compoundTag.getBoolean("EnchantProjectile"));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    protected void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putBoolean("EnchantProjectile", this.hasEnchantProjectile());
    }
}
