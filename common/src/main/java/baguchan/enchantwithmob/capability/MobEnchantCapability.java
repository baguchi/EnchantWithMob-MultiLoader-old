package baguchan.enchantwithmob.capability;

import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.network.packet.*;
import baguchan.enchantwithmob.platform.Services;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;


public class MobEnchantCapability {
	private static final UUID HEALTH_MODIFIER_UUID = UUID.fromString("6699a403-e2cc-31e6-195e-4757200e0935");

	private static final AttributeModifier HEALTH_MODIFIER = new AttributeModifier(HEALTH_MODIFIER_UUID, "Health boost", 0.5D, AttributeModifier.Operation.MULTIPLY_BASE);


	private List<MobEnchantHandler> mobEnchants = Lists.newArrayList();
	@Nullable
	private LivingEntity enchantOwner;
	private boolean fromOwner;
	private EnchantType enchantType = EnchantType.NORMAL;


	/**
	 * add MobEnchant on Entity
	 *
	 * @param entity       Entity given a MobEnchant
	 * @param mobEnchant   Mob Enchant attached to mob
	 * @param enchantLevel Mob Enchant Level
	 */
	public void addMobEnchant(LivingEntity entity, MobEnchant mobEnchant, int enchantLevel) {

		this.mobEnchants.add(new MobEnchantHandler(mobEnchant, enchantLevel));
		if (!entity.level().isClientSide) {
			MobEnchantedMessage message = new MobEnchantedMessage(entity, mobEnchant, enchantLevel);
			Services.NETWORK_HANDLER.sendToAllClients(((ServerLevel) entity.level()).players(), message);

		}
		this.onNewEnchantEffect(entity, mobEnchant, enchantLevel);
		//Sync Client Enchant
		//size changed like minecraft dungeons
		entity.refreshDimensions();
	}

	public void addMobEnchant(LivingEntity entity, MobEnchant mobEnchant, int enchantLevel, boolean ancient) {
		this.addMobEnchant(entity, mobEnchant, enchantLevel);
		this.setEnchantType(entity, ancient ? EnchantType.ANCIENT : EnchantType.NORMAL);
	}

	public void setEnchantType(LivingEntity entity, EnchantType enchantType) {
		this.enchantType = enchantType;
		if (!entity.level().isClientSide) {
			AncientMessage message = new AncientMessage(entity, enchantType == EnchantType.ANCIENT);
			Services.NETWORK_HANDLER.sendToAllClients(((ServerLevel) entity.level()).players(), message);

		}
	}

	/**
	 * add MobEnchant on Entity From Owner
	 *
	 * @param entity       Entity given a MobEnchant
	 * @param mobEnchant   Mob Enchant attached to mob
	 * @param enchantLevel Mob Enchant Level
	 * @param owner        OwnerEntity with a mob Enchant attached to that mob
	 */
	public void addMobEnchantFromOwner(LivingEntity entity, MobEnchant mobEnchant, int enchantLevel, LivingEntity owner) {

		this.mobEnchants.add(new MobEnchantHandler(mobEnchant, enchantLevel));
		if (!entity.level().isClientSide) {
			MobEnchantedMessage message = new MobEnchantedMessage(entity, mobEnchant, enchantLevel);
			Services.NETWORK_HANDLER.sendToAllClients(((ServerLevel) entity.level()).players(), message);
		}
		this.addOwner(entity, owner);
		this.onNewEnchantEffect(entity, mobEnchant, enchantLevel);
		entity.refreshDimensions();
	}

	public void addOwner(LivingEntity entity, LivingEntity owner) {
		this.fromOwner = true;
		this.enchantOwner = owner;
		if (!entity.level().isClientSide) {
			MobEnchantFromOwnerMessage message = new MobEnchantFromOwnerMessage(entity, owner);
			Services.NETWORK_HANDLER.sendToAllClients(((ServerLevel) entity.level()).players(), message);
		}
	}
	public void removeOwner(LivingEntity livingEntity) {
		this.fromOwner = false;
		this.enchantOwner = null;
		//Sync Client Enchant
		if (!livingEntity.level().isClientSide) {
			RemoveMobEnchantOwnerMessage message = new RemoveMobEnchantOwnerMessage(livingEntity);
			Services.NETWORK_HANDLER.sendToAllClients(((ServerLevel) livingEntity.level()).players(), message);
		}
	}

	/*
	 * Remove MobEnchant on Entity
	 */
	public void removeAllMobEnchant(LivingEntity entity) {

		for (int i = 0; i < mobEnchants.size(); ++i) {
			this.onRemoveEnchantEffect(entity, mobEnchants.get(i).getMobEnchant());
		}
		//Sync Client Enchant
		if (!entity.level().isClientSide) {
			RemoveAllMobEnchantMessage message = new RemoveAllMobEnchantMessage(entity);
			Services.NETWORK_HANDLER.sendToAllClients(((ServerLevel) entity.level()).players(), message);
		}
		this.mobEnchants.removeAll(mobEnchants);
		//size changed like minecraft dungeons
		entity.refreshDimensions();
	}

	/*
	 * Remove MobEnchant on Entity from owner
	 */
	public void removeMobEnchantFromOwner(LivingEntity entity) {
		for (int i = 0; i < mobEnchants.size(); ++i) {
			this.onRemoveEnchantEffect(entity, mobEnchants.get(i).getMobEnchant());
		}
		//Sync Client Enchant
		if (!entity.level().isClientSide) {
			RemoveAllMobEnchantMessage message = new RemoveAllMobEnchantMessage(entity);
			Services.NETWORK_HANDLER.sendToAllClients(((ServerLevel) entity.level()).players(), message);
		}
		this.mobEnchants.removeAll(mobEnchants);
		this.removeOwner(entity);
		//size changed like minecraft dungeons
		entity.refreshDimensions();
	}


	/*
	 * Add Enchant Attribute
	 */
	public void onNewEnchantEffect(LivingEntity entity, MobEnchant enchant, int enchantLevel) {
		enchant.applyAttributesModifiersToEntity(entity, entity.getAttributes(), enchantLevel);

		//if (EnchantConfig.COMMON.dungeonsLikeHealth.get()) {
		if(false) {
			AttributeInstance modifiableattributeinstance = entity.getAttributes().getInstance(Attributes.MAX_HEALTH);
			if (modifiableattributeinstance != null && !modifiableattributeinstance.hasModifier(HEALTH_MODIFIER)) {
				modifiableattributeinstance.removeModifier(HEALTH_MODIFIER_UUID);
				modifiableattributeinstance.addPermanentModifier(HEALTH_MODIFIER);
				entity.setHealth(entity.getHealth() * 1.25F);
			}
			//}
		}
	}

	/*
	 * Changed Enchant Attribute When Enchant is Changed
	 */
	public void onChangedEnchantEffect(LivingEntity entity, MobEnchant enchant, int enchantLevel) {
		enchant.applyAttributesModifiersToEntity(entity, entity.getAttributes(), enchantLevel);
	}

	/*
	 * Remove Enchant Attribute effect
	 */
	protected void onRemoveEnchantEffect(LivingEntity entity, MobEnchant enchant) {
		enchant.removeAttributesModifiersFromEntity(entity, entity.getAttributes());

		AttributeInstance modifiableattributeinstance = entity.getAttributes().getInstance(Attributes.MAX_HEALTH);
		if (modifiableattributeinstance != null) {
			if (modifiableattributeinstance.hasModifier(HEALTH_MODIFIER)) {
				entity.setHealth(entity.getHealth() / 1.25F);
                modifiableattributeinstance.removeModifier(HEALTH_MODIFIER_UUID);
			}
		}
	}

	public List<MobEnchantHandler> getMobEnchants() {
		return mobEnchants;
	}

	public boolean hasEnchant() {
		return !this.mobEnchants.isEmpty();
	}

	@Nullable
	public LivingEntity getEnchantOwner() {
		return enchantOwner;
	}

	public boolean hasOwner() {
		return this.enchantOwner != null && this.enchantOwner.isAlive();
	}

	//check this enchant from owner
	public boolean isFromOwner() {
		return this.fromOwner;
	}

	public EnchantType getEnchantType() {
		return enchantType;
	}

	public boolean isAncient() {
		return enchantType == EnchantType.ANCIENT;
	}

	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();

		ListTag listnbt = new ListTag();

		for (int i = 0; i < mobEnchants.size(); i++) {
			listnbt.add(mobEnchants.get(i).writeNBT());
		}

		nbt.put("StoredMobEnchants", listnbt);
		nbt.putBoolean("FromOwner", fromOwner);

		nbt.putString("EnchantType", enchantType.name());

		return nbt;
	}

	public void deserializeNBT(CompoundTag nbt) {
		ListTag list = MobEnchantUtils.getEnchantmentListForNBT(nbt);

		mobEnchants.clear();

		for (int i = 0; i < list.size(); ++i) {
			CompoundTag compoundnbt = list.getCompound(i);

			MobEnchant mobEnchant = MobEnchantUtils.getEnchantFromNBT(compoundnbt);
			//check mob enchant is not null
			if (mobEnchant != null) {
				mobEnchants.add(new MobEnchantHandler(mobEnchant, MobEnchantUtils.getEnchantLevelFromNBT(compoundnbt)));
			}
		}

		fromOwner = nbt.getBoolean("FromOwner");
		enchantType = EnchantType.get(nbt.getString("EnchantType"));
	}

	public enum EnchantType {
		NORMAL,
		ANCIENT;

		public static EnchantType get(String nameIn) {
			for (EnchantType enchantType : values()) {
				if (enchantType.name().equals(nameIn))
					return enchantType;
			}
			return NORMAL;
		}
	}
}