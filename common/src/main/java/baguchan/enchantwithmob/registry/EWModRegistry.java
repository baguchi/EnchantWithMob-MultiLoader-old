package baguchan.enchantwithmob.registry;

import baguchan.enchantwithmob.EWConstants;
import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import static net.minecraft.resources.ResourceKey.createRegistryKey;

public class EWModRegistry {
    public static final ResourceKey<Registry<MobEnchant>> MOB_ENCHANT_REGISTRY_KEY = createRegistryKey(new ResourceLocation(EWConstants.MOD_ID, "mob_enchant"));

    public static final Registry<MobEnchant> MOB_ENCHANT_REGISTRY = Services.REGISTRAR.createRegistry(MobEnchant.class, new ResourceLocation(EWConstants.MOD_ID, "mob_enchant"));

    public static void init() {

    }
}
