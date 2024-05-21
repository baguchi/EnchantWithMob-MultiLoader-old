package baguchan.enchantwithmob.utils;

import baguchan.enchantwithmob.mobenchant.MobEnchant;

public class MobEnchantConfigUtils {

    public static boolean isPlayerEnchantable(MobEnchant mobEnchant) {
        return true;//!EnchantConfig.COMMON.BLACKLIST_PLAYER_ENCHANT.get().contains(MobEnchants.getRegistry().getKey(mobEnchant).toString());
    }
}
