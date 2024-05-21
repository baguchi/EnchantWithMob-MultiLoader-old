package baguchan.enchantwithmob.item;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.registry.EWModRegistry;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class EnchantersBookItem extends Item {
	private final TargetingConditions enchantTargeting = TargetingConditions.forNonCombat().range(16.0D).ignoreLineOfSight().ignoreInvisibilityTesting();
	private final TargetingConditions alreadyEnchantTargeting = TargetingConditions.forNonCombat().range(16.0D).ignoreLineOfSight().ignoreInvisibilityTesting().selector((entity) -> {
		return entity instanceof IEnchantCap enchantCap && enchantCap.getEnchantCap().hasEnchant();
	});

	public EnchantersBookItem(Properties properties) {
		super(properties);
	}

	/*@Override
	public boolean isEnabled(FeatureFlagSet p_249172_) {
		return super.isEnabled(p_249172_) && !EnchantConfig.COMMON.disableMobEnchantStuffItems.get();
	}*/
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
			if (MobEnchantUtils.hasMobEnchant(stack)) {
				List<LivingEntity> list = level.getNearbyEntities(LivingEntity.class, this.enchantTargeting, playerIn, playerIn.getBoundingBox().inflate(16.0D));
				List<LivingEntity> hasEnchantedMoblist = level.getNearbyEntities(LivingEntity.class, this.alreadyEnchantTargeting, playerIn, playerIn.getBoundingBox().inflate(16.0D));

				if (hasEnchantedMoblist.isEmpty() || hasEnchantedMoblist.size() < 5) {
					if (!list.isEmpty()) {
						int size = list.size();
						final boolean[] flag = {false};
						for (int i = 0; i < size; ++i) {
							LivingEntity enchantedMob = list.get(i);

							if (i >= 5) {
								break;
							}

							if (!enchantedMob.canAttack(playerIn) && playerIn != enchantedMob) {
								if (enchantedMob instanceof IEnchantCap cap) {
									if (!cap.getEnchantCap().hasEnchant()) {
										if (flag[0]) {
											MobEnchantUtils.addUnstableItemMobEnchantToEntity(stack, enchantedMob, playerIn, cap);
										} else {
											flag[0] = MobEnchantUtils.addUnstableItemMobEnchantToEntity(stack, enchantedMob, playerIn, cap);
										}
									}
								}
								;
							}
						}

						//When flag is true, enchanting is success.
						if (flag[0]) {
							level.playSound(playerIn, playerIn.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS);

							playerIn.getCooldowns().addCooldown(stack.getItem(), 40);

							return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
						}
					} else {
						playerIn.displayClientMessage(Component.translatable("enchantwithmob.cannot.no_enchantable_ally"), true);

						playerIn.getCooldowns().addCooldown(stack.getItem(), 20);

						return InteractionResultHolder.fail(stack);
					}
				} else {
					playerIn.displayClientMessage(Component.translatable("enchantwithmob.cannot.no_enchantable_ally"), true);

					playerIn.getCooldowns().addCooldown(stack.getItem(), 20);

					return InteractionResultHolder.fail(stack);
				}
			}
		return super.use(level, playerIn, handIn);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag p_41424_) {
		super.appendHoverText(stack, level, tooltip, p_41424_);
		ChatFormatting[] textformatting2 = new ChatFormatting[]{ChatFormatting.DARK_PURPLE};

		tooltip.add(Component.translatable("mobenchant.enchantwithmob.enchanter_book.tooltip").withStyle(textformatting2));
		tooltip.add(Component.translatable("mobenchant.enchantwithmob.enchanter_book.tooltip2").withStyle(textformatting2));
		if (MobEnchantUtils.hasMobEnchant(stack)) {
			ListTag listnbt = MobEnchantUtils.getEnchantmentListForNBT(stack.getTag());

			for (int i = 0; i < listnbt.size(); ++i) {
				CompoundTag compoundnbt = listnbt.getCompound(i);

				MobEnchant mobEnchant = MobEnchantUtils.getEnchantFromNBT(compoundnbt);
				int enchantmentLevel = MobEnchantUtils.getEnchantLevelFromNBT(compoundnbt);

				if (mobEnchant != null) {
					ChatFormatting[] textformatting = new ChatFormatting[]{ChatFormatting.AQUA};

					tooltip.add(Component.translatable("mobenchant." + EWModRegistry.MOB_ENCHANT_REGISTRY.getKey(mobEnchant).getNamespace() + "." + EWModRegistry.MOB_ENCHANT_REGISTRY.getKey(mobEnchant).getPath()).withStyle(textformatting).append(" ").append(Component.translatable("enchantment.level." + enchantmentLevel).withStyle(textformatting)));
				}
			}

			List<Pair<Attribute, AttributeModifier>> list1 = Lists.newArrayList();

			for (int i = 0; i < listnbt.size(); ++i) {
				CompoundTag compoundnbt = listnbt.getCompound(i);

				MobEnchant mobEnchant = MobEnchantUtils.getEnchantFromNBT(compoundnbt);
				int mobEnchantLevel = MobEnchantUtils.getEnchantLevelFromNBT(compoundnbt);

				if (mobEnchant != null) {
					Map<Attribute, AttributeModifier> map = mobEnchant.getAttributeModifierMap();
					if (!map.isEmpty()) {
						for (Map.Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
							AttributeModifier attributemodifier = entry.getValue();
							AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), mobEnchant.getAttributeModifierAmount(mobEnchantLevel, attributemodifier), attributemodifier.getOperation());
							list1.add(new Pair<>(entry.getKey(), attributemodifier1));
						}
					}
				}
			}


			if (!list1.isEmpty()) {
				//tooltip.add(StringTextComponent.EMPTY);
				tooltip.add((Component.translatable("mobenchant.enchantwithmob.when_ehcnanted")).withStyle(ChatFormatting.DARK_PURPLE));

				for (Pair<Attribute, AttributeModifier> pair : list1) {
					AttributeModifier attributemodifier2 = pair.getSecond();
					double d0 = attributemodifier2.getAmount();
					double d1;
					if (attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
						d1 = attributemodifier2.getAmount();
					} else {
						d1 = attributemodifier2.getAmount() * 100.0D;
					}

					if (d0 > 0.0D) {
						tooltip.add((Component.translatable("attribute.modifier.plus." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(pair.getFirst().getDescriptionId()))).withStyle(ChatFormatting.BLUE));
					} else if (d0 < 0.0D) {
						d1 = d1 * -1.0D;
						tooltip.add((Component.translatable("attribute.modifier.take." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(pair.getFirst().getDescriptionId()))).withStyle(ChatFormatting.RED));
					}
				}
			}
		}
	}

	@Override
	public boolean isFoil(ItemStack p_77636_1_) {
		return true;
	}
}
