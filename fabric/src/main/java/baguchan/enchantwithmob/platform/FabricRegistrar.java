package baguchan.enchantwithmob.platform;

import baguchan.enchantwithmob.platform.services.IRegistrar;
import baguchan.enchantwithmob.registry.*;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class FabricRegistrar implements IRegistrar {

    @Override
    public void setupRegistrar() {
        EWModRegistry.init();
        EWItems.register();
        EWEntityTypes.register();
        EWMobEnchants.init();
        EWCreativeTabs.register();
        EwSoundEvents.register();
        EwArgumentTypeInfos.register();
        EwLootItemFunctions.init();
    }

    @Override
    public <V, T extends V> Supplier<T> registerObject(final ResourceLocation objId, final Supplier<T> objSup, Registry<V> targetRegistry) {
        T targetObject = Registry.register(targetRegistry, objId, objSup.get());
        return () -> targetObject;
    }

    @Override
    public <P> Registry<P> createRegistry(Class<P> type, ResourceLocation registryName) {
        return FabricRegistryBuilder.createSimple(type, registryName)
                .attribute(RegistryAttribute.SYNCED)
                .buildAndRegister();
    }
}
