package baguchan.enchantwithmob;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class EnchantConfig {
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();

    }


    public static class Common {
        public final ForgeConfigSpec.BooleanValue naturalSpawnEnchantedMob;
        public final ForgeConfigSpec.BooleanValue spawnEnchantedAnimal;
        public final ForgeConfigSpec.BooleanValue enchantYourSelf;
        public final ForgeConfigSpec.BooleanValue changeSizeWhenEnchant;
        public final ForgeConfigSpec.BooleanValue dungeonsLikeHealth;
        public final ForgeConfigSpec.BooleanValue bigYourSelf;
        public final ForgeConfigSpec.BooleanValue universalEnchant;
        public final ForgeConfigSpec.DoubleValue difficultyBasePercent;
        public final ForgeConfigSpec.DoubleValue effectiveBasePercent;
        public final ForgeConfigSpec.BooleanValue disableMobEnchantStuffItems;


        public final ForgeConfigSpec.ConfigValue<List<? extends String>> enchantOnSpawnExclusionMobs;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> alwayEnchantableMobs;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> alwayEnchantableAncientMobs;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> disableEnchants;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> allowPoisonCloudProjectile;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> allowMultishotProjectile;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> whitelistShootEntity;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklistPlayerEnchant;

        public Common(ForgeConfigSpec.Builder builder) {
            naturalSpawnEnchantedMob = builder
                    .comment("Enable the the spawning of enchanted mobs. [true / false]")
                    .define("Enchanted Mob can Spawn Natural", true);
            enchantOnSpawnExclusionMobs = builder
                    .comment("Disables specific mob from receiveing enchantments on spawn. Use the full name, eg: minecraft:ender_dragon.")
                    .define("enchantOnSpawnExclusionMobs", Lists.newArrayList("minecraft:wither", "minecraft:ender_dragon"));
            alwayEnchantableMobs = builder
                    .comment("Allow the specific mob from alway receiveing enchantments on spawn. Use the full name, eg: minecraft:zombie.")
                    .define("alwayEnchantableMobs", Lists.newArrayList());
            alwayEnchantableAncientMobs = builder
                    .comment("Allow the specific mob from alway receiveing enchantments as Ancient Mob on spawn(This feature may break for balance so be careful). Use the full name, eg: minecraft:zombie.")
                    .define("alwayEnchantableAncientMobs", Lists.newArrayList());
            disableEnchants = builder
                    .comment("Disables the specific mob enchant. Use the full name(This config only disabled mob enchant when mob spawn. not mean delete complete, eg: enchantwithmob:thorn.")
                    .define("disableMobEnchants", Lists.newArrayList());
            allowPoisonCloudProjectile = builder
                    .comment("Allow the poison cloud for projectile. Use the full name(eg: minecraft:arrow.")
                    .define("allowPoisonCloudProjectiles", Lists.newArrayList("minecraft:arrow", "minecraft:snowball", "earthmobsmod:melon_seeds", "earthmobsmod:zombie_flesh", "conjurer_illager:throwing_card", "conjurer_illager:bouncy_ball", "tofucraft:fukumame", "tofucraft:nether_fukumame", "tofucraft:soul_fukumame"));
            allowMultishotProjectile = builder
                    .comment("Allow the multi shot for projectile. Use the full name(eg: minecraft:arrow.")
                    .define("allowMultiShotProjectiles", Lists.newArrayList("minecraft:arrow", "minecraft:snowball", "earthmobsmod:melon_seeds", "earthmobsmod:zombie_flesh", "conjurer_illager:throwing_card", "conjurer_illager:bouncy_ball", "tofucraft:fukumame", "tofucraft:nether_fukumame", "tofucraft:soul_fukumame"));

            whitelistShootEntity = builder
                    .comment("Whitelist the projectile mob enchant for mob. Use the full name(eg: minecraft:zombie.")
                    .define("whitelistShootEntity", Lists.newArrayList("minecraft:skeleton", "minecraft:pillager", "minecraft:shulker", "minecraft:llama", "conjurer_illager:conjurer", "earthmobsmod:bone_spider", "earthmobsmod:lobber_zombie", "earthmobsmod:lobber_drowned"
                            , "earthmobsmod:melon_golem", "minecraft:piglin", "minecraft:snow_golem", "minecraft:player"));

            blacklistPlayerEnchant = builder
                    .comment("Blacklist the mob enchant for player. Use the full name(eg: enchantwithmob:thorn.")
                    .define("blacklistPlayerEnchant", Lists.newArrayList());

            spawnEnchantedAnimal = builder
                    .comment("Enable the the spawning of enchanted animal mobs. [true / false]")
                    .define("Enchanted Animal can Spawn Natural", false);
            enchantYourSelf = builder
                    .comment("Enable enchanting yourself. [true / false]")
                    .define("Enchant yourself", true);
            changeSizeWhenEnchant = builder
                    .comment("Enable Change Size When Enchanted. [true / false]")
                    .define("Change Size", false);
            dungeonsLikeHealth = builder
                    .comment("Enable Increase Health like Dungeons When Enchanted. [true / false]")
                    .define("Increase Health like Dungeons", false);
            bigYourSelf = builder
                    .comment("Enable Player More Bigger When You have Huge Enchant. [true / false]")
                    .define("Big Your Self", false);
            universalEnchant = builder
                    .comment("Enable All MobEnchant for all mob. [true / false]")
                    .define("UniversalEnchant", false);
            difficultyBasePercent = builder
                    .comment("Set The Difficulty Base Enchanted Mob Spawn Percent. [(Difficulty Base Percent * Difficulty id) + (Effective Difficulty Percent * Effective Difficulty)]")
                    .defineInRange("Difficulty Enchanted Spawn Percent", 0.005D, 0.0D, 1D);
            effectiveBasePercent = builder
                    .comment("Set The Effective Difficulty Base Enchanted Mob Spawn Percent [(Difficulty Base Percent * Difficulty id) + (Effective Difficulty Percent * Effective Difficulty)]")
                    .defineInRange("Effective Difficulty Enchanted Spawn Percent", 0.025D, 0.0D, 1D);
            disableMobEnchantStuffItems = builder
                    .comment("Disable MobEnchant Stuff Items. [true / false]")
                    .define("Disable MobEnchant Stuff Items", false);

        }

    }

}
