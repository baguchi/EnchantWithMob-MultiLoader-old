package baguchan.enchantwithmob.platform;

import baguchan.enchantwithmob.platform.services.IRegistrar;
import baguchan.enchantwithmob.registry.*;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class FabricRegistrar implements IRegistrar {

    @Override
    public void setupRegistrar() {
        EWModRegistry.init();
        EWItems.register();
        EWEntityTypes.register();
        EWMobEnchants.init();
        EwSoundEvents.register();
    }

    @Override
    public <T> Supplier<T> registerObject(ResourceLocation objId, Supplier<T> objSup, Registry<T> targetRegistry) {
        T targetObject = Registry.register(targetRegistry, objId, objSup.get());
        return () -> targetObject;
    }

    @Override
    public <T> Registry<T> registerNewRegistry(ResourceKey<Registry<T>> registryKey, ResourceLocation defaultId) {
        return FabricRegistryBuilder.createDefaulted(registryKey, defaultId).buildAndRegister();
    }
}
