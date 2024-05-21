package baguchan.enchantwithmob.utils;

import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.platform.Services;
import baguchan.enchantwithmob.registry.EWModRegistry;

public class MobEnchantConfigUtils {

    public static boolean isPlayerEnchantable(MobEnchant mobEnchant) {
        return !Services.CONFIG_HANDLER.getBlacklistPlayerEnchant().contains(EWModRegistry.MOB_ENCHANT_REGISTRY.getKey(mobEnchant).toString());
    }
}
