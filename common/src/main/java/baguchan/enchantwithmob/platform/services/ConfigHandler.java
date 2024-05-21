package baguchan.enchantwithmob.platform.services;

import java.util.List;

public interface ConfigHandler {
    boolean getBigYourSelf();

    boolean getChangeSizeWhenEnchant();


    boolean getDisableMobEnchantStuffItems();

    boolean getDungeonsLikeHealth();

    boolean getEnchantYourSelf();

    boolean getUniversalEnchant();


    List<? extends String> getDisableEnchants();

    List<? extends String> getBlacklistPlayerEnchant();

    List<? extends String> getWhitelistShootEntity();
}
