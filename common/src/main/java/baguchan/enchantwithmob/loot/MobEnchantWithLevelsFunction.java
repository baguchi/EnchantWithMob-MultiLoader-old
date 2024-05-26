package baguchan.enchantwithmob.loot;

import baguchan.enchantwithmob.registry.EwLootItemFunctions;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.Set;

public class MobEnchantWithLevelsFunction extends LootItemConditionalFunction {
    final NumberProvider levels;
    final boolean treasure;
    final boolean curse;

    MobEnchantWithLevelsFunction(LootItemCondition[] p_165193_, NumberProvider p_165194_, boolean p_165195_, boolean curse) {
        super(p_165193_);
        this.levels = p_165194_;
        this.treasure = p_165195_;
        this.curse = curse;
    }

    public LootItemFunctionType getType() {
        return EwLootItemFunctions.MOB_ENCHANT_WITH_LEVELS;
    }

    public Set<LootContextParam<?>> getReferencedContextParams() {
        return this.levels.getReferencedContextParams();
    }

    public ItemStack run(ItemStack p_80483_, LootContext p_80484_) {
        RandomSource random = p_80484_.getRandom();
        return MobEnchantUtils.addRandomEnchantmentToItemStack(random, p_80483_, this.levels.getInt(p_80484_), this.treasure, this.curse);
    }

    public static Builder enchantWithLevels(NumberProvider p_165197_) {
        return new Builder(p_165197_);
    }

    public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
        private final NumberProvider levels;
        private boolean treasure;
        private boolean curse;

        public Builder(NumberProvider p_165200_) {
            this.levels = p_165200_;
        }

        protected Builder getThis() {
            return this;
        }

        public Builder allowTreasure() {
            this.treasure = true;
            return this;
        }

        public Builder allowCurse() {
            this.curse = true;
            return this;
        }

        public LootItemFunction build() {
            return new MobEnchantWithLevelsFunction(this.getConditions(), this.levels, this.treasure, this.curse);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<MobEnchantWithLevelsFunction> {
        public void serialize(JsonObject p_80506_, MobEnchantWithLevelsFunction p_80507_, JsonSerializationContext p_80508_) {
            super.serialize(p_80506_, p_80507_, p_80508_);
            p_80506_.add("levels", p_80508_.serialize(p_80507_.levels));
            p_80506_.addProperty("treasure", p_80507_.treasure);
            p_80506_.addProperty("curse", p_80507_.curse);
        }

        public MobEnchantWithLevelsFunction deserialize(JsonObject p_80502_, JsonDeserializationContext p_80503_, LootItemCondition[] p_80504_) {
            NumberProvider numberprovider = GsonHelper.getAsObject(p_80502_, "levels", p_80503_, NumberProvider.class);
            boolean flag = GsonHelper.getAsBoolean(p_80502_, "treasure", false);
            boolean flag2 = GsonHelper.getAsBoolean(p_80502_, "curse", false);
            return new MobEnchantWithLevelsFunction(p_80504_, numberprovider, flag, flag2);
        }
    }
}