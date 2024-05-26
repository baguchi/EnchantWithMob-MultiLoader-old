package baguchan.enchantwithmob.mixins;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.api.IEnchantedTime;
import baguchan.enchantwithmob.network.packet.MobEnchantedMessage;
import baguchan.enchantwithmob.platform.Services;
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
                if (Services.CONFIG_HANDLER.getChangeSizeWhenEnchant()) {
                    float totalWidth = this.dimensions.width * 1.025F;
                    float totalHeight = this.dimensions.height * 1.025F;
                    this.eyeHeight = (this.eyeHeight * (1.025F));
                    dimensions = EntityDimensions.fixed(totalWidth, totalHeight);
                }
            }
        }
    }

    @Inject(method = "restoreFrom", at = @At("TAIL"))
    public void restoreFrom(Entity $$0, CallbackInfo ci) {
        Entity entity = ((Entity) (Object) this);
        if ($$0 instanceof IEnchantCap enchantCap) {
            if (!this.level().isClientSide) {
                for (int i = 0; i < enchantCap.getEnchantCap().getMobEnchants().size(); i++) {
                    MobEnchantedMessage message = new MobEnchantedMessage(entity, enchantCap.getEnchantCap().getMobEnchants().get(i));
                    Services.NETWORK_HANDLER.sendToEntity(entity, message);
                }
            }
        }
    }



    public final float getEyeHeight() {
        return this.eyeHeight;
    }

}
