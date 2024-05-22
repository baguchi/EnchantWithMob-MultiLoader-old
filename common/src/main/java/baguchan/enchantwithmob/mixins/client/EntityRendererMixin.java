package baguchan.enchantwithmob.mixins.client;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.client.ClientEventHandler;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    @Inject(method = "shouldRender", at = @At("RETURN"), cancellable = true)
    public void shouldRender(T p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {

            if (p_114491_ instanceof IEnchantCap enchantCap) {
                LivingEntity livingentity = enchantCap.getEnchantCap().getEnchantOwner();
                if (livingentity != null) {
                    Vec3 vec3 = ClientEventHandler.getPosition(livingentity, (double) livingentity.getBbHeight() * 0.5F, 1.0F);
                    Vec3 vec31 = ClientEventHandler.getPosition(p_114491_, (double) p_114491_.getBbHeight() * 0.5F, 1.0F);
                    cir.setReturnValue(p_114492_.isVisible(new AABB(vec31.x, vec31.y, vec31.z, vec3.x, vec3.y, vec3.z)));

                }
            }
        }
    }
}
