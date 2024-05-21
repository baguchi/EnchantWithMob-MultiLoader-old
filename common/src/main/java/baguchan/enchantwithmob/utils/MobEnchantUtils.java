package baguchan.enchantwithmob.utils;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.capability.MobEnchantHandler;
import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.registry.EWItems;
import baguchan.enchantwithmob.registry.EWMobEnchants;
import baguchan.enchantwithmob.registry.EWModRegistry;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MobEnchantUtils {
	public static final String TAG_MOBENCHANT = "MobEnchant";
	public static final String TAG_ENCHANT_LEVEL = "EnchantLevel";
	public static final String TAG_STORED_MOBENCHANTS = "StoredMobEnchants";

	//when projectile Shooter has mob enchant, start Runnable
	public static void executeIfPresent(Entity entity, MobEnchant mobEnchantment, Runnable runnable) {
		if (entity != null) {
			if (entity instanceof IEnchantCap cap) {
				if (MobEnchantUtils.findMobEnchantFromHandler(cap.getEnchantCap().getMobEnchants(), mobEnchantment)) {
					runnable.run();
				}
			}
		}
	}

	public static void executeIfPresent(Entity entity, Runnable runnable) {
		if (entity != null) {
			if (entity instanceof IEnchantCap cap) {
				if (cap.getEnchantCap().hasEnchant()) {
					runnable.run();
				}
			}
		}
	}

	/**
	 * get MobEnchant From NBT
	 *
	 * @param tag nbt tag
	 */
	@Nullable
	public static MobEnchant getEnchantFromNBT(@Nullable CompoundTag tag) {
		if (tag != null && EWModRegistry.MOB_ENCHANT_REGISTRY.containsKey(ResourceLocation.tryParse(tag.getString(TAG_MOBENCHANT)))) {
			return EWModRegistry.MOB_ENCHANT_REGISTRY.get(ResourceLocation.tryParse(tag.getString(TAG_MOBENCHANT)));
		} else {
			return null;
		}
	}

	/**
	 * get MobEnchant Level From NBT
	 *
	 * @param tag nbt tag
	 */
	public static int getEnchantLevelFromNBT(@Nullable CompoundTag tag) {
		if (tag != null) {
			return tag.getInt(TAG_ENCHANT_LEVEL);
		} else {
			return 0;
		}
	}

	/**
	 * get MobEnchant From String
	 *
	 * @param id MobEnchant id
	 */
	@Nullable
	public static MobEnchant getEnchantFromString(@Nullable String id) {
		if (id != null && EWModRegistry.MOB_ENCHANT_REGISTRY.containsKey(ResourceLocation.tryParse(id))) {
			return EWModRegistry.MOB_ENCHANT_REGISTRY.get(ResourceLocation.tryParse(id));
		} else {
			return null;
		}
	}

	@Nullable
	public static MobEnchant getEnchantFromResourceLocation(@Nullable ResourceLocation id) {
		if (id != null && EWModRegistry.MOB_ENCHANT_REGISTRY.containsKey(id)) {
			return EWModRegistry.MOB_ENCHANT_REGISTRY.get(id);
		} else {
			return null;
		}
	}

	/**
	 * check ItemStack has Mob Enchant
	 *
	 * @param stack MobEnchanted Item
	 */
	public static boolean hasMobEnchant(ItemStack stack) {
		CompoundTag compoundnbt = stack.getTag();
		return compoundnbt != null && compoundnbt.contains(TAG_STORED_MOBENCHANTS);
	}

	/**
	 * check NBT has Mob Enchant
	 *
	 * @param compoundnbt nbt tag
	 */
	public static ListTag getEnchantmentListForNBT(CompoundTag compoundnbt) {
		return compoundnbt != null ? compoundnbt.getList(TAG_STORED_MOBENCHANTS, 10) : new ListTag();
	}

	/**
	 * get Mob Enchantments From ItemStack
	 *
	 * @param stack MobEnchanted Item
	 */
	public static Map<MobEnchant, Integer> getEnchantments(ItemStack stack) {
		ListTag listnbt = getEnchantmentListForNBT(stack.getTag());
		return makeMobEnchantListFromListNBT(listnbt);
	}

	/**
	 * set Mob Enchantments From ItemStack
	 *
	 * @param enchMap EWMobEnchants and those level map
	 * @param stack   MobEnchanted Item
	 */
	public static void setEnchantments(Map<MobEnchant, Integer> enchMap, ItemStack stack) {
		ListTag listnbt = new ListTag();

		for (Map.Entry<MobEnchant, Integer> entry : enchMap.entrySet()) {
			MobEnchant enchantment = entry.getKey();
			if (enchantment != null) {
				int i = entry.getValue();
				CompoundTag compoundnbt = new CompoundTag();
				compoundnbt.putString(TAG_MOBENCHANT, String.valueOf((Object) EWModRegistry.MOB_ENCHANT_REGISTRY.getKey(enchantment)));
				compoundnbt.putShort(TAG_ENCHANT_LEVEL, (short) i);
				listnbt.add(compoundnbt);
				if (stack.getItem() == EWItems.MOB_ENCHANT_BOOK.get()) {
					addMobEnchantToItemStack(stack, enchantment, i);
				}
			}
		}

		if (listnbt.isEmpty()) {
			stack.removeTagKey(TAG_STORED_MOBENCHANTS);
		}
	}

	private static Map<MobEnchant, Integer> makeMobEnchantListFromListNBT(ListTag p_226652_0_) {
		Map<MobEnchant, Integer> map = Maps.newLinkedHashMap();

		for (int i = 0; i < p_226652_0_.size(); ++i) {
			CompoundTag compoundnbt = p_226652_0_.getCompound(i);
			MobEnchant mobEnchant = getEnchantFromString(compoundnbt.getString(TAG_MOBENCHANT));
			map.put(mobEnchant, compoundnbt.getInt(TAG_ENCHANT_LEVEL));

		}

		return map;
	}

	//add MobEnchantToItemstack (example,this method used to MobEnchantBook)
	public static void addMobEnchantToItemStack(ItemStack itemIn, MobEnchant mobenchant, int level) {
		ListTag listnbt = getEnchantmentListForNBT(itemIn.getTag());

		boolean flag = true;
		ResourceLocation resourcelocation = EWModRegistry.MOB_ENCHANT_REGISTRY.getKey(mobenchant);


		for (int i = 0; i < listnbt.size(); ++i) {
			CompoundTag compoundnbt = listnbt.getCompound(i);
			ResourceLocation resourcelocation1 = ResourceLocation.tryParse(compoundnbt.getString("MobEnchant"));
			if (resourcelocation1 != null && resourcelocation1.equals(resourcelocation)) {
				if (compoundnbt.getInt(TAG_ENCHANT_LEVEL) < level) {
					compoundnbt.putInt(TAG_ENCHANT_LEVEL, level);
				}

				flag = false;
				break;
			}
		}

		if (flag) {
			CompoundTag compoundnbt1 = new CompoundTag();
			compoundnbt1.putString(TAG_MOBENCHANT, String.valueOf((Object) resourcelocation));
			compoundnbt1.putInt(TAG_ENCHANT_LEVEL, level);
			listnbt.add(compoundnbt1);
		}

		itemIn.getTag().put(TAG_STORED_MOBENCHANTS, listnbt);
	}

	/**
	 * add Mob Enchantments From ItemStack
	 *
	 * @param itemIn     MobEnchanted Item
	 * @param entity     Enchanting target
	 * @param capability MobEnchant Capability
	 */
	public static boolean addItemMobEnchantToEntity(ItemStack itemIn, LivingEntity entity, LivingEntity user, IEnchantCap capability) {
		ListTag listnbt = getEnchantmentListForNBT(itemIn.getTag());
		boolean flag = false;

		for (int i = 0; i < listnbt.size(); ++i) {
			CompoundTag compoundnbt = listnbt.getCompound(i);
			if (checkAllowMobEnchantFromMob(MobEnchantUtils.getEnchantFromNBT(compoundnbt), entity, capability)) {
				capability.getEnchantCap().addMobEnchant(entity, MobEnchantUtils.getEnchantFromNBT(compoundnbt), MobEnchantUtils.getEnchantLevelFromNBT(compoundnbt));
				flag = true;

				if (!user.level().isClientSide()) {
					itemIn.hurtAndBreak(1, user, (userEntity) -> userEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));

				}
			}
		}
		return flag;
	}

	public static boolean addUnstableItemMobEnchantToEntity(ItemStack itemIn, LivingEntity entity, LivingEntity owner, IEnchantCap capability) {
		ListTag listnbt = getEnchantmentListForNBT(itemIn.getTag());
		boolean flag = false;

		for (int i = 0; i < listnbt.size(); ++i) {
			CompoundTag compoundnbt = listnbt.getCompound(i);
			if (checkAllowMobEnchantFromMob(MobEnchantUtils.getEnchantFromNBT(compoundnbt), entity, capability)) {
				capability.getEnchantCap().addMobEnchantFromOwner(entity, MobEnchantUtils.getEnchantFromNBT(compoundnbt), MobEnchantUtils.getEnchantLevelFromNBT(compoundnbt), owner);
				flag = true;
				if (!owner.level().isClientSide()) {
					itemIn.hurtAndBreak(1, owner, (userEntity) -> userEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));

				}
			}
		}
		return flag;
	}

	public static void removeMobEnchantToEntity(LivingEntity entity, IEnchantCap capability) {
		capability.getEnchantCap().removeAllMobEnchant(entity);
	}

	public static int getExperienceFromMob(IEnchantCap cap) {
		int l = 0;
		for (MobEnchantHandler list : cap.getEnchantCap().getMobEnchants()) {
			MobEnchant enchantment = list.getMobEnchant();
			int integer = list.getEnchantLevel();
			l += enchantment.getMinEnchantability(integer);
		}
		return l;
	}

	/**
	 * add Mob Enchantments To Entity
	 *
	 * @param livingEntity Enchanting target
	 * @param capability   MobEnchant Capability
	 * @param data         MobEnchant Data
	 */
	public static boolean addEnchantmentToEntity(LivingEntity livingEntity, IEnchantCap capability, MobEnchantmentData data) {
		boolean flag = false;
		if (checkAllowMobEnchantFromMob(data.enchantment, livingEntity, capability)) {
			capability.getEnchantCap().addMobEnchant(livingEntity, data.enchantment, data.enchantmentLevel, false);
			flag = true;
		}
		return flag;
	}

	public static boolean addEnchantmentToEntity(LivingEntity livingEntity, IEnchantCap capability, MobEnchantmentData data, boolean ancient) {
		boolean flag = false;
		if (checkAllowMobEnchantFromMob(data.enchantment, livingEntity, capability)) {
			capability.getEnchantCap().addMobEnchant(livingEntity, data.enchantment, data.enchantmentLevel, ancient);
			flag = true;
		}
		return flag;
	}

	/**
	 * add Mob Enchantments To Entity
	 *
	 * @param livingEntity Enchanting target
	 * @param capability   MobEnchant Capability
	 * @param random       Random
	 * @param level        max limit level MobEnchant
	 * @param allowTresure setting is allow rare enchant
	 */
	public static boolean addRandomEnchantmentToEntity(LivingEntity livingEntity, IEnchantCap capability, RandomSource random, int level, boolean allowTresure, boolean allowCurse, boolean ancient) {
		List<MobEnchantmentData> list = buildEnchantmentList(random, level, allowTresure, allowCurse);

		boolean flag = false;
		for (MobEnchantmentData enchantmentdata : list) {
			if (checkAllowMobEnchantFromMob(enchantmentdata.enchantment, livingEntity, capability)) {
				capability.getEnchantCap().addMobEnchant(livingEntity, enchantmentdata.enchantment, enchantmentdata.enchantmentLevel, ancient);
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * add Mob Enchantments To Entity(but unstable enchant)
	 *
	 * @param livingEntity Enchanting target
	 * @param capability   MobEnchant Capability
	 * @param random       Random
	 * @param level        max limit level MobEnchant
	 * @param allowTresure setting is allow rare enchant
	 */
	public static boolean addUnstableRandomEnchantmentToEntity(LivingEntity livingEntity, LivingEntity ownerEntity, IEnchantCap capability, RandomSource random, int level, boolean allowTresure, boolean allowCurse) {
		List<MobEnchantmentData> list = buildEnchantmentList(random, level, allowTresure, allowCurse);

		boolean flag = false;

		for (MobEnchantmentData enchantmentdata : list) {
			if (checkAllowMobEnchantFromMob(enchantmentdata.enchantment, livingEntity, capability)) {
				capability.getEnchantCap().addMobEnchantFromOwner(livingEntity, enchantmentdata.enchantment, enchantmentdata.enchantmentLevel, ownerEntity);
				flag = true;
			}
		}
		return flag;
	}

	public static ItemStack addRandomEnchantmentToItemStack(RandomSource random, ItemStack stack, int level, boolean allowRare, boolean allowCurse) {
		List<MobEnchantmentData> list = buildEnchantmentList(random, level, allowRare, allowCurse);

		for (MobEnchantmentData enchantmentdata : list) {
			if (!enchantmentdata.enchantment.isDisabled()) {
				addMobEnchantToItemStack(stack, enchantmentdata.enchantment, enchantmentdata.enchantmentLevel);
			}
		}

		return stack;
	}

	public static boolean findMobEnchantHandler(List<MobEnchantHandler> list, MobEnchant findMobEnchant) {
		for (MobEnchantHandler mobEnchant : list) {
			if (mobEnchant.getMobEnchant().equals(findMobEnchant)) {
				return true;
			}
		}
		return false;
	}

	public static boolean findMobEnchant(List<MobEnchant> list, MobEnchant findMobEnchant) {
		if (list.contains(findMobEnchant)) {
			return true;
		}
		return false;
	}

	public static boolean findMobEnchantFromHandler(List<MobEnchantHandler> list, MobEnchant findMobEnchant) {
		for (MobEnchantHandler mobEnchant : list) {
			if (mobEnchant != null && !findMobEnchant.isDisabled()) {
				if (mobEnchant.getMobEnchant().equals(findMobEnchant)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean checkAllowMobEnchantFromMob(@Nullable MobEnchant mobEnchant, LivingEntity livingEntity, IEnchantCap capability) {
		//if (!EnchantConfig.COMMON.universalEnchant.get()) {
			if (mobEnchant != null && !mobEnchant.isCompatibleMob(livingEntity)) {
				return false;
			}
		//}

		if (mobEnchant.isDisabled()) {
			return false;
		}

		for (MobEnchantHandler enchantHandler : capability.getEnchantCap().getMobEnchants()) {
			if (mobEnchant != null && enchantHandler.getMobEnchant() != null && !enchantHandler.getMobEnchant().isCompatibleWith(mobEnchant)) {
				return false;
			}
		}

		//check mob enchant is not null
		return mobEnchant != null;
	}

	public static int getMobEnchantLevelFromHandler(List<MobEnchantHandler> list, MobEnchant findMobEnchant) {
		for (MobEnchantHandler mobEnchant : list) {
			if (mobEnchant != null) {
				if (mobEnchant.getMobEnchant().equals(findMobEnchant)) {
					return mobEnchant.getEnchantLevel();
				}
			}
		}
		return 0;
	}

	/*
	 * build MobEnchantment list like vanilla's enchantment
	 */
	public static List<MobEnchantmentData> buildEnchantmentList(RandomSource randomIn, int level, boolean allowTresure, boolean allowCursed) {
		List<MobEnchantmentData> list = Lists.newArrayList();
		int i = 1; //Enchantability
		if (i <= 0) {
			return list;
		} else {
			level = level + 1 + randomIn.nextInt(i / 4 + 1) + randomIn.nextInt(i / 4 + 1);
			float f = (randomIn.nextFloat() + randomIn.nextFloat() - 1.0F) * 0.15F;
			level = Mth.clamp(Math.round((float) level + (float) level * f), 1, Integer.MAX_VALUE);
			List<MobEnchantmentData> list1 = makeMobEnchantmentDatas(level, allowTresure, allowCursed);
			if (!list1.isEmpty()) {
				WeightedRandom.getRandomItem(randomIn, list1).ifPresent(list::add);

				while (randomIn.nextInt(50) <= level) {
					if (!list.isEmpty()) {
						removeIncompatible(list1, Util.lastOf(list));
					}
					if (list1.isEmpty()) {
						break;
					}

					WeightedRandom.getRandomItem(randomIn, list1).ifPresent(list::add);
					level /= 2;
				}
			}

			return list;
		}
	}

	/*
	 * get MobEnchantment data.
	 * when not allow rare enchantment,Ignore rare enchantment
	 */
	public static List<MobEnchantmentData> makeMobEnchantmentDatas(int p_185291_0_, boolean allowTresure, boolean allowCursed) {
		List<MobEnchantmentData> list = Lists.newArrayList();

		for (MobEnchant enchantment : EWModRegistry.MOB_ENCHANT_REGISTRY) {
			if ((!enchantment.isCursedEnchant() || allowCursed) && (!enchantment.isTresureEnchant() || allowTresure) && enchantment.isDiscoverable()) {
				for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
					if (p_185291_0_ >= enchantment.getMinEnchantability(i) && p_185291_0_ <= enchantment.getMaxEnchantability(i)) {
						list.add(new MobEnchantmentData(enchantment, i));
						break;
					}
				}
			}
		}

		return list;
	}

	private static void removeIncompatible(List<MobEnchantmentData> dataList, MobEnchantmentData data) {
		Iterator<MobEnchantmentData> iterator = dataList.iterator();

		while (iterator.hasNext()) {
			if (!data.enchantment.isCompatibleWith((iterator.next()).enchantment) || data.enchantment.isDisabled()) {
				iterator.remove();
			}
		}

	}
}