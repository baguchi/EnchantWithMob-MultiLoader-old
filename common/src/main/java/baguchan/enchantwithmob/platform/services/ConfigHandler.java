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


    List<? extends String> getAlwayEnchantableAncientMobs();

    List<? extends String> getAlwayEnchantableMobs();

    boolean getSpawnEnchantedAnimal();

    boolean getNaturalSpawnEnchantedMob();

    List<? extends String> getEnchantOnSpawnExclusionMobs();

    double getDifficultyBasePercent();

    double getEffectiveBasePercent();
}
