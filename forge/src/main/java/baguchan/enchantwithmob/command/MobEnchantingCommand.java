package baguchan.enchantwithmob.command;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.capability.MobEnchantCapability;
import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.registry.EWModRegistry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class MobEnchantingCommand {

    private static final Dynamic2CommandExceptionType ERROR_LEVEL_TOO_HIGH = new Dynamic2CommandExceptionType((p_137022_, p_137023_) -> {
        return Component.translatable("commands.enchant.failed.level", p_137022_, p_137023_);
    });


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        LiteralArgumentBuilder<CommandSourceStack> enchantCommand = Commands.literal("mob_enchanting")
                .requires(player -> player.hasPermission(2));
        enchantCommand.then(Commands.literal("clear").then(Commands.argument("target", EntityArgument.entity()).executes((ctx) -> {
            return setClear(ctx.getSource(), EntityArgument.getEntity(ctx, "target"));
        }))).then(Commands.literal("give").then(Commands.argument("target", EntityArgument.entity())
                .then(Commands.argument("mob_enchantment", MobEnchantArgument.mobEnchantment()).executes((p_198357_0_) -> setMobEnchants(p_198357_0_.getSource(), EntityArgument.getEntity(p_198357_0_, "target"), MobEnchantArgument.getMobEnchant(p_198357_0_, "mob_enchantment"), 1))
                        .then(Commands.argument("level", IntegerArgumentType.integer(1)).executes((p_198357_0_) -> setMobEnchants(p_198357_0_.getSource(), EntityArgument.getEntity(p_198357_0_, "target"), MobEnchantArgument.getMobEnchant(p_198357_0_, "mob_enchantment"), IntegerArgumentType.getInteger(p_198357_0_, "level")))))));

        dispatcher.register(enchantCommand);

        LiteralArgumentBuilder<CommandSourceStack> ancientEnchantCommand = Commands.literal("ancient_mob")
                .requires(player -> player.hasPermission(2));


        ancientEnchantCommand.then(Commands.argument("target", EntityArgument.entity()).then(Commands.argument("ancient", BoolArgumentType.bool()).executes((ctx) -> {
            return setAncientMob(ctx.getSource(), EntityArgument.getEntity(ctx, "target"), BoolArgumentType.getBool(ctx, "ancient"));
        })));

        dispatcher.register(ancientEnchantCommand);
    }

    private static int setClear(CommandSourceStack commandStack, Entity entity) {

        if (entity != null) {
            if (entity instanceof LivingEntity) {
                if (entity instanceof IEnchantCap enchantCap) {
                    enchantCap.getEnchantCap().removeAllMobEnchant((LivingEntity) entity);
                    enchantCap.getEnchantCap().setEnchantType((LivingEntity) entity, MobEnchantCapability.EnchantType.NORMAL);
                }

                commandStack.sendSuccess(() -> Component.translatable("commands.enchantwithmob.mob_enchanting.clear", entity.getDisplayName()), true);
                return 1;
            } else {
                commandStack.sendFailure(Component.translatable("commands.enchantwithmob.mob_enchanting.clear.fail.no_living_entity", entity.getDisplayName()));

                return 0;
            }
        } else {
            commandStack.sendFailure(Component.translatable("commands.enchantwithmob.mob_enchanting.clear.fail.no_entity"));

            return 0;
        }
    }

    private static int setAncientMob(CommandSourceStack commandStack, Entity entity, boolean ancientMob) {
        if (entity != null) {
            if (entity instanceof LivingEntity) {

                if (entity instanceof IEnchantCap enchantCap) {
                    enchantCap.getEnchantCap().setEnchantType((LivingEntity) entity, ancientMob ? MobEnchantCapability.EnchantType.ANCIENT : MobEnchantCapability.EnchantType.NORMAL);
                }

                commandStack.sendSuccess(() -> Component.translatable("commands.enchantwithmob.ancient_mob.set_ancient", entity.getDisplayName()), true);
                return 1;
            } else {
                commandStack.sendFailure(Component.translatable("commands.enchantwithmob.ancient_mobb.fail.no_living_entity", entity.getDisplayName()));

                return 0;
            }
        } else {
            commandStack.sendFailure(Component.translatable("commands.enchantwithmob.ancient_mob.fail.no_entity"));

            return 0;
        }
    }

    private static int setMobEnchants(CommandSourceStack commandStack, Entity entity, MobEnchant mobEnchant, int level) {

        if (entity != null) {
            if (entity instanceof LivingEntity) {
                if (mobEnchant != null) {
                    if (level > mobEnchant.getMaxLevel()) {
                        commandStack.sendFailure(Component.translatable("commands.enchantwithmob.mob_enchanting.set_enchant.fail.too_high"));
                        return 0;
                    } else {
                        if (entity instanceof IEnchantCap enchantCap) {
                            enchantCap.getEnchantCap().addMobEnchant((LivingEntity) entity, mobEnchant, level);
                        }

                        commandStack.sendSuccess(() -> Component.translatable("commands.enchantwithmob.mob_enchanting.set_enchant", entity.getDisplayName(), EWModRegistry.MOB_ENCHANT_REGISTRY.getKey(mobEnchant)), true);
                        return 1;
                    }
                } else {
                    commandStack.sendFailure(Component.translatable("commands.enchantwithmob.mob_enchanting.set_enchant.fail.no_mobenchant"));

                    return 0;
                }
            } else {
                commandStack.sendFailure(Component.translatable("commands.enchantwithmob.mob_enchanting.set_enchant.fail.no_living_entity", entity.getDisplayName()));

                return 0;
            }
        } else {
            commandStack.sendFailure(Component.translatable("commands.enchantwithmob.mob_enchanting.set_enchant.fail.no_entity"));

            return 0;
        }
    }
}