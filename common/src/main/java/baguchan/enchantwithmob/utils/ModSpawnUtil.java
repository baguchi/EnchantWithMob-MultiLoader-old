package baguchan.enchantwithmob.utils;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.ServerLevelAccessor;

public class ModSpawnUtil {
    public static void finalizeSpawn(Mob entity, ServerLevelAccessor level, MobSpawnType mobSpawnType) {
        if (entity instanceof IEnchantCap cap) {
            if (!level.isClientSide()) {
                LivingEntity livingEntity = entity;

                if (isSpawnAlwayEnchantableAncientEntity(livingEntity)) {
                    int i = 0;
                    float difficultScale = level.getCurrentDifficultyAt(livingEntity.blockPosition()).getEffectiveDifficulty() - 0.2F;
                    switch (level.getDifficulty()) {
                        case EASY:
                            i = (int) Mth.clamp((5 + level.getRandom().nextInt(10)) * difficultScale, 1, 30);

                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, level.getRandom(), i, true, false, true);
                            break;
                        case NORMAL:
                            i = (int) Mth.clamp((5 + level.getRandom().nextInt(15)) * difficultScale, 1, 60);

                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, level.getRandom(), i, true, false, true);
                            break;
                        case HARD:
                            i = (int) Mth.clamp((5 + level.getRandom().nextInt(20)) * difficultScale, 1, 100);

                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, level.getRandom(), i, true, false, true);
                            break;
                    }

                    livingEntity.setHealth(livingEntity.getMaxHealth());
                }

                // On add MobEnchant Alway Enchantable Mob
                if (isSpawnAlwayEnchantableEntity(livingEntity)) {
                    int i = 0;
                    float difficultScale = level.getCurrentDifficultyAt(livingEntity.blockPosition()).getEffectiveDifficulty() - 0.2F;
                    switch (level.getDifficulty()) {
                        case EASY:
                            i = (int) Mth.clamp((5 + level.getRandom().nextInt(5)) * difficultScale, 1, 20);

                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, level.getRandom(), i, true, false, false);
                            break;
                        case NORMAL:
                            i = (int) Mth.clamp((5 + level.getRandom().nextInt(5)) * difficultScale, 1, 40);

                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, level.getRandom(), i, true, false, false);
                            break;
                        case HARD:
                            i = (int) Mth.clamp((5 + level.getRandom().nextInt(10)) * difficultScale, 1, 50);

                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, level.getRandom(), i, true, false, false);
                            break;
                    }

                    livingEntity.setHealth(livingEntity.getMaxHealth());
                }


                //if (Services.CONFIG_HANDLER.naturalSpawnEnchantedMob.get() && isSpawnEnchantableEntity(entity)) {
                if (Services.CONFIG_HANDLER.getNaturalSpawnEnchantedMob() && isSpawnEnchantableEntity(entity)) {

                    if (!(livingEntity instanceof Animal) && !(livingEntity instanceof WaterAnimal) || Services.CONFIG_HANDLER.getSpawnEnchantedAnimal()) {
                        if (mobSpawnType != MobSpawnType.BREEDING && mobSpawnType != MobSpawnType.CONVERSION && mobSpawnType != MobSpawnType.STRUCTURE && mobSpawnType != MobSpawnType.MOB_SUMMONED) {
                            if (level.getRandom().nextFloat() < (Services.CONFIG_HANDLER.getDifficultyBasePercent() * level.getDifficulty().getId()) + level.getCurrentDifficultyAt(livingEntity.blockPosition()).getEffectiveDifficulty() * Services.CONFIG_HANDLER.getEffectiveBasePercent()) {
                                if (!level.isClientSide()) {
                                    int i = 0;
                                    float difficultScale = level.getCurrentDifficultyAt(livingEntity.blockPosition()).getEffectiveDifficulty() - 0.2F;
                                    switch (level.getDifficulty()) {
                                        case EASY:
                                            i = (int) Mth.clamp((5 + level.getRandom().nextInt(5)) * difficultScale, 1, 20);

                                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, level.getRandom(), i, true, true, false);
                                            break;
                                        case NORMAL:
                                            i = (int) Mth.clamp((5 + level.getRandom().nextInt(5)) * difficultScale, 1, 40);

                                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, level.getRandom(), i, true, true, false);
                                            break;
                                        case HARD:
                                            i = (int) Mth.clamp((5 + level.getRandom().nextInt(10)) * difficultScale, 1, 50);

                                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, level.getRandom(), i, true, true, false);
                                            break;
                                    }

                                    livingEntity.setHealth(livingEntity.getMaxHealth());
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private static boolean isSpawnAlwayEnchantableEntity(Entity entity) {
        return !(entity instanceof Player) && !(entity instanceof ArmorStand) && !(entity instanceof Boat) && !(entity instanceof Minecart) && Services.CONFIG_HANDLER.getAlwayEnchantableMobs().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
    }

    private static boolean isSpawnAlwayEnchantableAncientEntity(Entity entity) {
        return !(entity instanceof Player) && !(entity instanceof ArmorStand) && !(entity instanceof Boat) && !(entity instanceof Minecart) && Services.CONFIG_HANDLER.getAlwayEnchantableAncientMobs().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
    }

    private static boolean isSpawnEnchantableEntity(Entity entity) {
        return !(entity instanceof Player) && !(entity instanceof ArmorStand) && !(entity instanceof Boat) && !(entity instanceof Minecart) && !Services.CONFIG_HANDLER.getEnchantOnSpawnExclusionMobs().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
    }
}
