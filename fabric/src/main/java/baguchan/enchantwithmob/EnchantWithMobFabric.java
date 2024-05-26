package baguchan.enchantwithmob;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.api.IEnchantedProjectile;
import baguchan.enchantwithmob.command.MobEnchantArgument;
import baguchan.enchantwithmob.command.MobEnchantingCommand;
import baguchan.enchantwithmob.loot.MobEnchantWithLevelsFunction;
import baguchan.enchantwithmob.network.FabricNetworkHelper;
import baguchan.enchantwithmob.network.packet.MobEnchantedMessage;
import baguchan.enchantwithmob.platform.Services;
import baguchan.enchantwithmob.registry.EWEntityTypes;
import baguchan.enchantwithmob.registry.EWItems;
import baguchan.enchantwithmob.registry.EWMobEnchants;
import baguchan.enchantwithmob.utils.MobEnchantCombatRules;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import baguchan.enchantwithmob.utils.ModSpawnUtil;
import fuzs.extensibleenums.api.extensibleenums.v1.BuiltInEnumFactories;
import io.github.fabricators_of_create.porting_lib.config.ConfigRegistry;
import io.github.fabricators_of_create.porting_lib.config.ConfigType;
import io.github.fabricators_of_create.porting_lib.entity.events.EntityEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.LivingEntityEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.PlayerEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.ProjectileImpactEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.loot.v2.FabricLootTableBuilder;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.mixin.command.ArgumentTypesAccessor;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

import static baguchan.enchantwithmob.mobenchant.PoisonCloudMobEnchant.shooterIsLiving;

public class EnchantWithMobFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        EnchantWithMob.init();

        FabricNetworkHelper.init();
        BuiltInEnumFactories.createRaiderType("enchanter", EWEntityTypes.ENCHANTER.get(), new int[]{0, 0, 1, 0, 1, 1, 2, 1});

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() == EWItems.MOB_ENCHANT_BOOK.get() && MobEnchantUtils.hasMobEnchant(stack)) {
                if (entity instanceof IEnchantCap cap && entity instanceof LivingEntity livingEntity) {
                    MobEnchantUtils.addItemMobEnchantToEntity(stack, livingEntity, livingEntity, cap);

                    player.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);

                    stack.hurtAndBreak(1, player, (entity2) -> entity2.broadcastBreakEvent(hand));

                    return InteractionResult.SUCCESS;
                }
            }

            if (stack.getItem() == EWItems.MOB_UNENCHANT_BOOK.get() && !player.getCooldowns().isOnCooldown(stack.getItem())) {
                if (entity instanceof LivingEntity) {
                    LivingEntity target = (LivingEntity) entity;

                    if (target instanceof IEnchantCap cap) {
                        MobEnchantUtils.removeMobEnchantToEntity(target, cap);
                        player.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);

                        stack.hurtAndBreak(1, player, (entity2) -> entity2.broadcastBreakEvent(hand));

                        player.getCooldowns().addCooldown(stack.getItem(), 80);

                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.PASS;
        });
        LivingEntityEvents.CHECK_SPAWN.register(((entity, world, x, y, z, spawner, spawnReason) -> {
            ModSpawnUtil.finalizeSpawn(entity, world, spawnReason);
            return true;
        }));

        LivingEntityEvents.HURT.register((source, damaged, amount) -> {
            LivingEntity livingEntity = damaged;

            if (source.getEntity() instanceof LivingEntity) {
                LivingEntity attacker = (LivingEntity) source.getEntity();

                if (attacker instanceof IEnchantCap cap) {
                    if (cap.getEnchantCap().hasEnchant()) {
                        int mobEnchantLevel = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), EWMobEnchants.STRONG);
                        int mobEnchantSize = cap.getEnchantCap().getMobEnchants().size();

                        //make snowman stronger
                        if (!livingEntity.isDamageSourceBlocked(source) && amount == 0) {
                            amount = MobEnchantCombatRules.getDamageAddition(1, mobEnchantLevel, mobEnchantSize);
                        } else if (amount > 0) {
                            amount = MobEnchantCombatRules.getDamageAddition(amount, mobEnchantLevel, mobEnchantSize);
                        }
                    }

                    if (cap.getEnchantCap().hasEnchant() && MobEnchantUtils.findMobEnchantFromHandler(cap.getEnchantCap().getMobEnchants(), EWMobEnchants.POISON)) {
                        int i = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), EWMobEnchants.POISON);

                        if (amount > 0) {
                            if (attacker.getRandom().nextFloat() < i * 0.125F) {
                                livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 60 * i, 0), attacker);
                            }
                        }
                    }


                }
            }

            if (livingEntity instanceof IEnchantCap cap) {
                if (!source.is(DamageTypeTags.BYPASSES_EFFECTS) && cap.getEnchantCap().hasEnchant()) {
                    int mobEnchantLevel = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), EWMobEnchants.PROTECTION);
                    int mobEnchantSize = cap.getEnchantCap().getMobEnchants().size();

                    amount = MobEnchantCombatRules.getDamageReduction(amount, mobEnchantLevel, mobEnchantSize);
                }
                if (source.getDirectEntity() != null) {
                    if (cap.getEnchantCap().hasEnchant()) {
                        int i = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), EWMobEnchants.THORN);

                        if (source.getDirectEntity() instanceof LivingEntity && !source.is(DamageTypeTags.IS_PROJECTILE) && !source.is(DamageTypes.THORNS) && livingEntity.getRandom().nextFloat() < i * 0.1F) {
                            LivingEntity attacker = (LivingEntity) source.getDirectEntity();

                            attacker.hurt(livingEntity.damageSources().thorns(livingEntity), MobEnchantCombatRules.getThornDamage(amount, cap.getEnchantCap()));
                        }
                    }
                }
            }
            return amount;
        });
        LivingEntityEvents.EXPERIENCE_DROP.register((i, attackingPlayer, entity) -> {
            if (entity instanceof IEnchantCap cap) {
                if (cap.getEnchantCap().hasEnchant()) {
                    if (cap.getEnchantCap().isAncient()) {
                        i = i + MobEnchantUtils.getExperienceFromMob(cap) * 5;
                    } else {
                        i = i + MobEnchantUtils.getExperienceFromMob(cap);
                    }

                }
            }
            return i;
        });
        ProjectileImpactEvent.PROJECTILE_IMPACT.register((event) -> {

            Projectile projectile = event.getProjectile();
            if (event.getRayTraceResult() instanceof EntityHitResult) {
                EntityHitResult entityHitResult = (EntityHitResult) event.getRayTraceResult();
                MobEnchantUtils.executeIfPresent(entityHitResult.getEntity(), EWMobEnchants.DEFLECT, () -> {
                    event.setCanceled(true);
                    Vec3 vec3 = projectile.getDeltaMovement();

                    projectile.setDeltaMovement(-vec3.x * 0.75F, -vec3.y * 0.75F, -vec3.z * 0.75F);
                    if (projectile instanceof AbstractArrow arrow) {
                        arrow.setPierceLevel((byte) 0);
                    }
                });
            }

            if (!shooterIsLiving(projectile) || !EnchantConfig.COMMON.allowPoisonCloudProjectile.get().contains(BuiltInRegistries.ENTITY_TYPE.getKey(projectile.getType()).toString()))
                return;
            LivingEntity owner = (LivingEntity) projectile.getOwner();
            if (owner instanceof IEnchantCap cap) {
                int i = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), EWMobEnchants.POISON_CLOUD);

                if (cap.getEnchantCap().hasEnchant() && MobEnchantUtils.findMobEnchantFromHandler(cap.getEnchantCap().getMobEnchants(), EWMobEnchants.POISON_CLOUD)) {
                    //arrow is different
                    if (!(projectile instanceof AbstractArrow) || !projectile.onGround()) {
                        AreaEffectCloud areaeffectcloud = new AreaEffectCloud(owner.level(), event.getRayTraceResult().getLocation().x, event.getRayTraceResult().getLocation().y, event.getRayTraceResult().getLocation().z);
                        areaeffectcloud.setRadius(0.6F);
                        areaeffectcloud.setRadiusOnUse(-0.01F);
                        areaeffectcloud.setWaitTime(10);
                        areaeffectcloud.setDuration(80);
                        areaeffectcloud.setOwner(owner);
                        areaeffectcloud.setRadiusPerTick(-0.001F);

                        areaeffectcloud.addEffect(new MobEffectInstance(MobEffects.POISON, 80, i - 1));
                        owner.level().addFreshEntity(areaeffectcloud);
                    }
                }
            }
        });
        EntityEvents.ON_JOIN_WORLD.register((entity, world, loadedFromDisk) -> {
            if (!loadedFromDisk) {
                Level level = world;
                if (entity instanceof Projectile && entity instanceof IEnchantedProjectile enchantedProjectile) {
                    Projectile projectile = (Projectile) entity;
                    if (!(!shooterIsLiving(projectile) || !EnchantConfig.COMMON.allowMultishotProjectile.get().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()))) {

                        LivingEntity owner = (LivingEntity) projectile.getOwner();
                        MobEnchantUtils.executeIfPresent(owner, EWMobEnchants.MULTISHOT, () -> {
                            if (!level.isClientSide && projectile.tickCount == 0 && !enchantedProjectile.hasEnchantProjectile()) {
                                enchantedProjectile.setEnchantProjectile(true);
                                CompoundTag compoundNBT = new CompoundTag();
                                compoundNBT = projectile.saveWithoutId(compoundNBT);
                                addProjectile(projectile, compoundNBT, level, 15.0F);
                                addProjectile(projectile, compoundNBT, level, -15.0F);
                            }
                        });
                    }
                }
            }
            return true;
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            if (environment.includeIntegrated)
                MobEnchantingCommand.register(dispatcher);
        });
        ArgumentTypesAccessor.fabric_getClassMap().put(
                MobEnchantArgument.class, SingletonArgumentInfo.contextFree(MobEnchantArgument::mobEnchantment));

        PlayerEvents.LOGGED_IN.register(player -> {
            if (player instanceof ServerPlayer serverPlayer) {
                if (!player.level().isClientSide()) {
                    if (serverPlayer instanceof IEnchantCap cap) {
                        for (int i = 0; i < cap.getEnchantCap().getMobEnchants().size(); i++) {
                            Services.NETWORK_HANDLER.sendToEntity(serverPlayer, new MobEnchantedMessage(serverPlayer, cap.getEnchantCap().getMobEnchants().get(i)));

                        }
                    }
                }
            }
        });
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            {
                if (id.toString().equals("minecraft:archaeology/desert_pyramid")) {
                    tableBuilder = FabricLootTableBuilder.copyOf(tableBuilder.build()).modifyPools(poolBuilder -> poolBuilder.with(LootItem.lootTableItem(EWItems.MOB_ENCHANT_BOOK.get()).apply(MobEnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(10, 20))).build()));
                }
                if (id.toString().equals("minecraft:chests/ancient_city")) {
                    tableBuilder = FabricLootTableBuilder.copyOf(tableBuilder.build()).pool(LootPool.lootPool().add(LootItem.lootTableItem(EWItems.MOB_ENCHANT_BOOK.get()).apply(MobEnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(30, 40)))).build());
                }
                if (id.toString().equals("minecraft:chests/wood_land_mansion")) {
                    tableBuilder = FabricLootTableBuilder.copyOf(tableBuilder.build()).pool(LootPool.lootPool().add(LootItem.lootTableItem(EWItems.MOB_ENCHANT_BOOK.get()).apply(MobEnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(20, 30)))).add(LootItem.lootTableItem(EWItems.ENCHANTERS_BOOK.get()).apply(MobEnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(20, 30)))).build());
                }
                if (id.toString().equals("minecraft:chests/stronghold_library")) {
                    tableBuilder = FabricLootTableBuilder.copyOf(tableBuilder.build()).pool(LootPool.lootPool().add(LootItem.lootTableItem(EWItems.MOB_ENCHANT_BOOK.get()).apply(MobEnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(20, 30)))).build());
                }
            }
        });

        ConfigRegistry.registerConfig(EWConstants.MOD_ID, ConfigType.COMMON, EnchantConfig.COMMON_SPEC);
    }

    private static void addProjectile(Projectile projectile, CompoundTag compoundNBT, Level level, float rotation) {
        Projectile newProjectile = (Projectile) projectile.getType().create(level);
        UUID uuid = newProjectile.getUUID();
        newProjectile.load(compoundNBT);
        newProjectile.setUUID(uuid);
        Vec3 vector3d = newProjectile.getDeltaMovement().yRot((float) (Math.PI / rotation));

        newProjectile.setDeltaMovement(vector3d);
        float f = Mth.sqrt((float) vector3d.horizontalDistanceSqr());
        newProjectile.setYRot((float) (Mth.atan2(vector3d.x, vector3d.z) * (double) (180F / (float) Math.PI)));
        newProjectile.setXRot((float) (Mth.atan2(vector3d.y, (double) f) * (double) (180F / (float) Math.PI)));
        newProjectile.yRotO = newProjectile.getYRot();
        newProjectile.xRotO = newProjectile.getXRot();
        if (newProjectile instanceof Projectile) {
            Projectile newDamagingProjectile = (Projectile) newProjectile;
            Vec3 newPower = new Vec3(newDamagingProjectile.getDeltaMovement().x, newDamagingProjectile.getDeltaMovement().y, newDamagingProjectile.getDeltaMovement().z).yRot((float) (Math.PI / rotation));

            newDamagingProjectile.setDeltaMovement(newPower);
        }

        if (newProjectile instanceof AbstractArrow) {
            ((AbstractArrow) newProjectile).pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }

        if (newProjectile instanceof IEnchantedProjectile enchantedProjectile) {
            enchantedProjectile.setEnchantProjectile(true);
        }

        level.addFreshEntity(newProjectile);
    }
}
