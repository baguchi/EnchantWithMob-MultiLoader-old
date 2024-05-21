package baguchan.enchantwithmob.mobenchant;

import baguchan.enchantwithmob.registry.EWMobEnchants;
import baguchan.enchantwithmob.registry.EWModRegistry;
import baguchan.enchantwithmob.utils.MobEnchantConfigUtils;
import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class MobEnchant implements FeatureElement {
    private final Map<Attribute, AttributeModifier> attributeModifierMap = Maps.newHashMap();
	protected final Rarity enchantType;
	private final int level;
	private int minlevel = 1;
    private final FeatureFlagSet requiredFeatures;
    @Nullable
    private String descriptionId;

    public MobEnchant(Properties properties) {
		this.enchantType = properties.enchantType;
		this.level = properties.level;
        this.requiredFeatures = properties.requiredFeatures;
	}

    public Rarity getRarity() {
        return enchantType;
    }

    public MobEnchant setMinLevel(int level) {
        this.minlevel = level;

        return this;
    }


    /**
     * Returns the minimum level that the enchantment can have.
     */
    public int getMinLevel() {
        return minlevel;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return level;
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + enchantmentLevel * 10;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 5;
    }


	public void tick(LivingEntity entity, int level) {

	}

	public final boolean isCompatibleWith(MobEnchant enchantmentIn) {
		return this.canApplyTogether(enchantmentIn) && enchantmentIn.canApplyTogether(this);
	}

	public boolean isTresureEnchant() {
		return false;
	}

    public boolean isDiscoverable() {
        return true;
	}


    public boolean isCompatibleMob(LivingEntity livingEntity) {
        return !(livingEntity instanceof Player) || MobEnchantConfigUtils.isPlayerEnchantable(this);
	}

	/**
	 * Determines if the enchantment passed can be applyied together with this enchantment.
	 */
	protected boolean canApplyTogether(MobEnchant ench) {
		return this != ench;
    }

    public MobEnchant addAttributesModifier(Attribute attributeIn, String uuid, double amount, AttributeModifier.Operation operation) {
        AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString(uuid), Util.makeDescriptionId("mobenchant", EWModRegistry.MOB_ENCHANT_REGISTRY.getKey(this)), amount, operation);
        this.attributeModifierMap.put(attributeIn, attributemodifier);
        return this;
    }

    public Map<Attribute, AttributeModifier> getAttributeModifierMap() {
        return this.attributeModifierMap;
    }

    public void removeAttributesModifiersFromEntity(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn) {
        for (Map.Entry<Attribute, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
            AttributeInstance modifiableattributeinstance = attributeMapIn.getInstance(entry.getKey());
            if (modifiableattributeinstance != null) {
                modifiableattributeinstance.removeModifier(entry.getValue());
            }
        }

    }

    public void applyAttributesModifiersToEntity(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        for (Map.Entry<Attribute, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
            AttributeInstance modifiableattributeinstance = attributeMapIn.getInstance(entry.getKey());
            if (modifiableattributeinstance != null) {
                AttributeModifier attributemodifier = entry.getValue();
                modifiableattributeinstance.removeModifier(attributemodifier);
                modifiableattributeinstance.addPermanentModifier(new AttributeModifier(attributemodifier.getId(), EWModRegistry.MOB_ENCHANT_REGISTRY.getKey(this).toString() + " " + amplifier, this.getAttributeModifierAmount(amplifier, attributemodifier), attributemodifier.getOperation()));
            }
        }
    }

    public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier) {
        return modifier.getAmount() * (double) (amplifier);
    }

    public boolean isDisabled() {
        return false/*EnchantConfig.COMMON.DISABLE_ENCHANTS.get().contains(MobEnchants.getRegistry().getKey(this).toString())*/;
    }

    public boolean isCursedEnchant() {
        return false;
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return this.requiredFeatures;
    }

    @Override
    public boolean isEnabled(FeatureFlagSet p_249172_) {
        return !this.isDisabled() && this.requiredFeatures().isSubsetOf(p_249172_);
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("mob_enchant", EWModRegistry.MOB_ENCHANT_REGISTRY.getKey(this));
        }

        return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public Component getFullname(int p_44701_) {
        MutableComponent mutablecomponent = Component.translatable(this.getDescriptionId());
        if (this.isCursedEnchant()) {
            mutablecomponent.withStyle(ChatFormatting.RED);
        } else {
            mutablecomponent.withStyle(ChatFormatting.AQUA);
        }

        if (p_44701_ != 1 || this.getMaxLevel() != 1) {
            mutablecomponent.append(CommonComponents.SPACE).append(Component.translatable("enchantment.level." + p_44701_));
        }

        return mutablecomponent;
    }


    public static class Properties {
        private final Rarity enchantType;
        private final int level;

        FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;

        public Properties(Rarity enchantType, int level) {
            this.enchantType = enchantType;
            this.level = level;
        }

        public FeatureFlagSet getRequiredFeatures() {
            return requiredFeatures;
        }

        public Properties requiredFeatures(FeatureFlag... p_250948_) {
            this.requiredFeatures = FeatureFlags.REGISTRY.subset(p_250948_);
            return this;
        }
    }

    public static enum Rarity {
        COMMON(10),
        UNCOMMON(5),
        RARE(2),
        VERY_RARE(1);

        private final int weight;

        private Rarity(int rarityWeight) {
            this.weight = rarityWeight;
        }

        /**
         * Retrieves the weight of Rarity.
         */
        public int getWeight() {
            return this.weight;
        }


    }
}