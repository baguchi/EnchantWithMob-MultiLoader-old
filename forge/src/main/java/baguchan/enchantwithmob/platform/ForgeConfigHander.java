package baguchan.enchantwithmob.platform;

import baguchan.enchantwithmob.EnchantConfig;
import baguchan.enchantwithmob.platform.services.ConfigHandler;

import java.util.List;

public class ForgeConfigHander implements ConfigHandler {
    @Override
    public boolean getBigYourSelf() {
        return EnchantConfig.COMMON.bigYourSelf.get();
    }

    @Override
    public boolean getChangeSizeWhenEnchant() {
        return EnchantConfig.COMMON.changeSizeWhenEnchant.get();
    }


    @Override
    public boolean getDisableMobEnchantStuffItems() {
        return EnchantConfig.COMMON.disableMobEnchantStuffItems.get();
    }

    @Override
    public boolean getDungeonsLikeHealth() {
        return EnchantConfig.COMMON.dungeonsLikeHealth.get();
    }

    @Override
    public boolean getEnchantYourSelf() {
        return EnchantConfig.COMMON.enchantYourSelf.get();
    }

    @Override
    public boolean getUniversalEnchant() {
        return EnchantConfig.COMMON.universalEnchant.get();
    }

    @Override
    public List<? extends String> getDisableEnchants() {
        return EnchantConfig.COMMON.disableEnchants.get();
    }

    @Override
    public List<? extends String> getBlacklistPlayerEnchant() {
        return EnchantConfig.COMMON.blacklistPlayerEnchant.get();
    }

    @Override
    public List<? extends String> getWhitelistShootEntity() {
        return EnchantConfig.COMMON.whitelistShootEntity.get();
    }


    @Override
    public List<? extends String> getAlwayEnchantableAncientMobs() {
        return EnchantConfig.COMMON.alwayEnchantableAncientMobs.get();
    }

    @Override
    public List<? extends String> getAlwayEnchantableMobs() {
        return EnchantConfig.COMMON.alwayEnchantableMobs.get();
    }

    @Override
    public boolean getSpawnEnchantedAnimal() {
        return EnchantConfig.COMMON.spawnEnchantedAnimal.get();
    }

    @Override
    public boolean getNaturalSpawnEnchantedMob() {
        return EnchantConfig.COMMON.naturalSpawnEnchantedMob.get();
    }

    @Override
    public List<? extends String> getEnchantOnSpawnExclusionMobs() {
        return EnchantConfig.COMMON.enchantOnSpawnExclusionMobs.get();
    }

    @Override
    public double getDifficultyBasePercent() {
        return EnchantConfig.COMMON.difficultyBasePercent.get();
    }

    @Override
    public double getEffectiveBasePercent() {
        return EnchantConfig.COMMON.effectiveBasePercent.get();
    }
}
