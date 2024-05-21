package baguchan.enchantwithmob.platform;

import baguchan.enchantwithmob.EWConstants;
import baguchan.enchantwithmob.registry.*;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import baguchan.enchantwithmob.platform.services.IRegistrar;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

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
        EwSoundEvents.register();
    }

    @Override
    public <T> RegistryObject<T> registerObject(ResourceLocation objId, Supplier<T> objSup, Registry<T> targetRegistry) {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus(); // Should not be null at the time this method is called

        ResourceKey<? extends Registry<T>> targetRegistryKey = targetRegistry.key();

        DeferredRegister<T> existingDefReg = (DeferredRegister<T>) CACHED_REGISTRIES.computeIfAbsent(targetRegistryKey, defReg -> {
            DeferredRegister<T> cachedDefReg = DeferredRegister.create(targetRegistryKey, MOD_ID);
            cachedDefReg.register(modBus);
            return cachedDefReg;
        });
        return existingDefReg.register(objId.getPath(), objSup);
    }



    @Override
    public <T> Registry<T> registerNewRegistry(ResourceKey<Registry<T>> registryKey, ResourceLocation defaultId) {
        FMLJavaModLoadingContext.get().getModEventBus().<NewRegistryEvent>addListener(event -> event.create(new RegistryBuilder<T>().setName(registryKey.location())));
        final  var register = DeferredRegister.create(registryKey, MOD_ID);
        Supplier<Registry<T>> registryInstance = Suppliers.memoize(() -> (Registry<T>) get(BuiltInRegistries.REGISTRY, register.getRegistryKey()));

        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        register.register(bus);
        return registryInstance.get();
    }

    @SuppressWarnings("unchecked")
    private static <T> T get(Registry<T> registry, ResourceKey<?> key) {
        return registry.get((ResourceKey<T>) key);
    }


    public static ImmutableMap<ResourceKey, DeferredRegister> getCachedRegistries() {
        return ImmutableMap.copyOf(CACHED_REGISTRIES);
    }
}
