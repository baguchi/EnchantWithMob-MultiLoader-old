package baguchan.enchantwithmob.mixins;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.api.IEnchantedTime;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Entity.class, remap = false)
public class EntityMixin implements IEnchantedTime {

    @Shadow
    private EntityDimensions dimensions;
    @Shadow
    private float eyeHeight;
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
                //if (EnchantConfig.COMMON.changeSizeWhenEnchant.get()) {
                    float totalWidth = this.dimensions.width * 1.025F;
                    float totalHeight = this.dimensions.height * 1.025F;
                    this.eyeHeight = (this.eyeHeight * (1.025F));
                    dimensions = EntityDimensions.fixed(totalWidth, totalHeight);
                //}
            }
        }
    }

    public final float getEyeHeight() {
        return this.eyeHeight;
    }

}
