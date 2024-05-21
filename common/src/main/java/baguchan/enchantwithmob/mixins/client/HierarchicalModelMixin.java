package baguchan.enchantwithmob.mixins.client;

import baguchan.enchantwithmob.api.IEnchantedTime;
import net.minecraft.client.model.HierarchicalModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = HierarchicalModel.class)
public class HierarchicalModelMixin implements IEnchantedTime {
    private float differentTime = 1.0F;

    @Override
    public float getDifferentTime() {
        return differentTime;
    }

    @Override
    public void setDifferentTime(float time) {
        differentTime = time;
    }

    @ModifyArg(method = "animate(Lnet/minecraft/world/entity/AnimationState;Lnet/minecraft/client/animation/AnimationDefinition;FF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/AnimationState;updateTime(FF)V"), index = 1)
    public float animate(float constant) {
        return constant * getDifferentTime();
    }
}
