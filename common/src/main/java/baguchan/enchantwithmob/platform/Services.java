package baguchan.enchantwithmob.platform;

import baguchan.enchantwithmob.EWConstants;
import baguchan.enchantwithmob.platform.services.*;

import java.util.ServiceLoader;

public class Services {
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IRegistrar REGISTRAR = load(IRegistrar.class);
    public static final MobRegistry MOB_REGISTRY = load(MobRegistry.class);
    public static final RenderRegistry RENDER_REGISTRY = load(RenderRegistry.class);
    public static final INetworkHandler NETWORK_HANDLER = load(INetworkHandler.class);
    public static final ConfigHandler CONFIG_HANDLER = load(ConfigHandler.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        EWConstants.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);

        return loadedService;
    }
}