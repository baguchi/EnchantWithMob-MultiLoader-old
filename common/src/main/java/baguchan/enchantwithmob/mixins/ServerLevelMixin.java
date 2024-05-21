package baguchan.enchantwithmob.mixins;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.api.IEnchantedTime;
import baguchan.enchantwithmob.registry.EWMobEnchants;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(value = ServerLevel.class)
public abstract class ServerLevelMixin extends Level {
    protected ServerLevelMixin(WritableLevelData p_270739_, ResourceKey<Level> p_270683_, RegistryAccess p_270200_, Holder<DimensionType> p_270240_, Supplier<ProfilerFiller> p_270692_, boolean p_270904_, boolean p_270470_, long p_270248_, int p_270466_) {
        super(p_270739_, p_270683_, p_270200_, p_270240_, p_270692_, p_270904_, p_270470_, p_270248_, p_270466_);
    }

    @Inject(method = "tickNonPassenger", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/Entity;tickCount:I", opcode = 181), cancellable = true)
    public void tickNonPassenger(Entity instance, CallbackInfo ci) {
        if (instance instanceof IEnchantCap enchantCap && instance instanceof IEnchantedTime enchantedTime) {
            //ajust time
            int fastTime = Mth.clamp(MobEnchantUtils.getMobEnchantLevelFromHandler(enchantCap.getEnchantCap().getMobEnchants(), EWMobEnchants.HASTE), 0, 2);
            int slowTime = Mth.clamp(MobEnchantUtils.getMobEnchantLevelFromHandler(enchantCap.getEnchantCap().getMobEnchants(), EWMobEnchants.SLOW), 0, 2);

            float different = 1 + fastTime * 0.125F - slowTime * 0.125F;


            if (different > 1F || different < 1F) {
                enchantedTime.setDifferentTime(enchantedTime.getDifferentTime() + different);
                instance.tickCount -= 1;

                ProfilerFiller profilerfiller = this.getProfiler();
                while (enchantedTime.getDifferentTime() >= 1F) {
                    instance.tickCount += 1;
                    this.getProfiler().push(() -> {
                        return BuiltInRegistries.ENTITY_TYPE.getKey(instance.getType()).toString();
                    });
                    profilerfiller.incrementCounter("tickNonPassenger");
                    instance.tick();
                    this.getProfiler().pop();
                    enchantedTime.setDifferentTime(enchantedTime.getDifferentTime() - 1F);
                }

                for (Entity entity : instance.getPassengers()) {
                    this.tickPassenger(instance, entity);
                }
                ci.cancel();
            } else {
                enchantedTime.setDifferentTime(0);
            }
        }
    }

    @Shadow
    private void tickPassenger(Entity p_8663_, Entity p_8664_) {

    }
}
