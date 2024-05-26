package baguchan.enchantwithmob.platform;

import baguchan.enchantwithmob.platform.services.IRegistrar;
import baguchan.enchantwithmob.registry.*;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

import static baguchan.enchantwithmob.EWConstants.MOD_ID;

public class ForgeRegistrar implements IRegistrar {
    private static final Object2ObjectLinkedOpenHashMap<ResourceKey, DeferredRegister> CACHED_REGISTRIES = new Object2ObjectLinkedOpenHashMap<>(); // Faster lookups + menial memory usage increase over a map backed by arrays :p

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
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus(); // Should not be null at the time this method is called

        ResourceKey<? extends Registry<V>> targetRegistryKey = targetRegistry.key();

        DeferredRegister<T> existingDefReg = (DeferredRegister<T>) CACHED_REGISTRIES.computeIfAbsent(targetRegistryKey, defReg -> {
            DeferredRegister<V> cachedDefReg = DeferredRegister.create(targetRegistryKey, MOD_ID);
            cachedDefReg.register(modBus);
            return cachedDefReg;
        });
        return existingDefReg.register(objId.getPath(), objSup);
    }



    @Override
    public <P> Registry<P> createRegistry(Class<P> type, ResourceLocation registryName) {
        ResourceKey<Registry<P>> registryKey = ResourceKey.createRegistryKey(registryName);
        DeferredRegister<P> deferredRegister = DeferredRegister.create(registryKey, MOD_ID);
        Supplier<IForgeRegistry<P>> iForgeRegistrySupplier = deferredRegister.makeRegistry(RegistryBuilder::new);
        return new MappedRegistry<>(registryKey, Lifecycle.stable());
    }

    @SuppressWarnings("unchecked")
    private static <T> T get(Registry<T> registry, ResourceKey<?> key) {
        return registry.get((ResourceKey<T>) key);
    }


    public static ImmutableMap<ResourceKey, DeferredRegister> getCachedRegistries() {
        return ImmutableMap.copyOf(CACHED_REGISTRIES);
    }
}
