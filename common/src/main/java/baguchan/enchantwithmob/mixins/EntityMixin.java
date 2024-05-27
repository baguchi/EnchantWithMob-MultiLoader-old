package baguchan.enchantwithmob.mixins;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.api.IEnchantedTime;
import baguchan.enchantwithmob.platform.Services;
import baguchan.enchantwithmob.registry.EWMobEnchants;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Entity.class)
public abstract class EntityMixin implements IEnchantedTime {

    @Shadow
    private EntityDimensions dimensions;
    @Shadow
    private float eyeHeight;

    @Shadow
    public abstract Level level();

    private float differentTime = 1.0F;

    @Override
    public float getDifferentTime() {
        return differentTime;
    }

    @Override
    public void setDifferentTime(float time) {
        differentTime = time;
    }


    @Inject(method = "refreshDimensions", at = @At("RETURN"))
    public void refreshDimensions(CallbackInfo callbackInfo) {
        if (this instanceof IEnchantCap cap) {
            if (cap.getEnchantCap().hasEnchant()) {
                int small = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), EWMobEnchants.SMALL);
                int big = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), EWMobEnchants.HUGE);
                if (small > 0) {
                    int level = small;
                    float cappedScale = Mth.clamp(0.15F * level, 0.0F, 0.9F);
                    float totalWidth = this.dimensions.width * (1.0F - cappedScale);
                    float totalHeight = this.dimensions.height * (1.0F - cappedScale);
                    eyeHeight = eyeHeight * (1.0F - cappedScale);

                    dimensions = EntityDimensions.fixed(totalWidth, totalHeight);
                } else if (big > 0) {
                    int level = big;

                    float totalWidth = this.dimensions.width * (1.0F + level * 0.15F);
                    float totalHeight = this.dimensions.height * (1.0F + level * 0.15F);
                    eyeHeight = eyeHeight * (1.0F + level * 0.15F);
                    dimensions = EntityDimensions.fixed(totalWidth, totalHeight);
                }
                if (Services.CONFIG_HANDLER.getChangeSizeWhenEnchant()) {
                    float totalWidth = this.dimensions.width * 1.025F;
                    float totalHeight = this.dimensions.height * 1.025F;
                    this.eyeHeight = (this.eyeHeight * (1.025F));
                    dimensions = EntityDimensions.fixed(totalWidth, totalHeight);
                }
            }
        }
    }


    public final float getEyeHeight() {
        return this.eyeHeight;
    }

}
