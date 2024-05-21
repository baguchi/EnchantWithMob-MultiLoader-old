package baguchan.enchantwithmob;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.network.FabricNetworkHelper;
import baguchan.enchantwithmob.registry.EWEntityTypes;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import fuzs.extensibleenums.api.extensibleenums.v1.BuiltInEnumFactories;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class EnchantWithMobFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        EnchantWithMob.init();

        FabricNetworkHelper.init();
        BuiltInEnumFactories.createRaiderType("enchanter", EWEntityTypes.ENCHANTER.get(), new int[]{0, 0, 1, 0, 1, 1, 2, 1});

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            ItemStack stack = player.getItemInHand(hand);
            if (MobEnchantUtils.hasMobEnchant(stack)) {
                if (entity instanceof IEnchantCap cap && entity instanceof LivingEntity livingEntity) {
                    MobEnchantUtils.addItemMobEnchantToEntity(stack, livingEntity, livingEntity, cap);

                    player.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);

                    stack.hurtAndBreak(1, player, (entity2) -> entity2.broadcastBreakEvent(hand));

                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        });
    }
}
