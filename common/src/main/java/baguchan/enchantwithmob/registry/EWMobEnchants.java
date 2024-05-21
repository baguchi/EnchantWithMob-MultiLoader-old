package baguchan.enchantwithmob.registry;

import baguchan.enchantwithmob.mobenchant.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

import static baguchan.enchantwithmob.utils.ResourceLocationHelper.modLoc;

public class EWMobEnchants {
    private static final ObjectArrayList<Supplier<Item>> MOB_ENCHANTS = new ObjectArrayList<>();
    public static final MobEnchant PROTECTION = registerMobEnchant("protection", new ProtectionMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.COMMON, 4)));
    public static final MobEnchant TOUGH = registerMobEnchant("tough", new ToughMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 2)).addAttributesModifier(Attributes.ARMOR, "313644c5-ead2-4670-b9eb-0b41d59ce5a2", (double) 2.0F, AttributeModifier.Operation.ADDITION).addAttributesModifier(Attributes.ARMOR_TOUGHNESS, "8135df8f-38d9-490a-8d6f-c908fa973b34", (double) 0.5F, AttributeModifier.Operation.ADDITION));
    public static final MobEnchant SPEEDY = registerMobEnchant("speedy", new SpeedyMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.UNCOMMON, 2)).addAttributesModifier(Attributes.MOVEMENT_SPEED, "501f27a9-4a75-4c2e-a2ab-91eeed71d748", (double) 0.05F, AttributeModifier.Operation.ADDITION));
    public static final MobEnchant STRONG = registerMobEnchant("strong", new StrongMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.COMMON, 3)));
    public static final MobEnchant THORN = registerMobEnchant("thorn", new ThornEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 3)));
    public static final MobEnchant HEALTH_BOOST = registerMobEnchant("health_boost", new HealthBoostMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 5)).addAttributesModifier(Attributes.MAX_HEALTH, "f5d32c9f-2a3d-4157-bbf7-469d348ce097", 2.0D, AttributeModifier.Operation.ADDITION));
    public static final MobEnchant POISON = registerMobEnchant("poison", new PoisonMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.RARE, 3)));
    public static final MobEnchant POISON_CLOUD = registerMobEnchant("poison_cloud", new PoisonCloudMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.RARE, 2)));

    public static final MobEnchant MULTISHOT = registerMobEnchant("multishot", new MultiShotMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.RARE, 1)));
    /*  public static final MobEnchant WIND = registerMobEnchant("wind", new WindMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 1)));
*/

    //public static final MobEnchant SOUL_STEAL = registerMobEnchant("soul_steal", new SoulStealMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 2)));
    public static final MobEnchant DEFLECT = registerMobEnchant("deflect", new DeflectMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 1)));
    public static final MobEnchant SMALL = registerMobEnchant("small", new SmallMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 2)).addAttributesModifier(Attributes.MAX_HEALTH, "b4170c63-d50b-a0ee-15b7-9156c6e41940", -0.1D, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final MobEnchant HUGE = registerMobEnchant("huge", new HugeMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 2)).addAttributesModifier(Attributes.MAX_HEALTH, "c988bca7-7fa9-4fea-bb44-c3625ac74241", 0.1D, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final MobEnchant HASTE = registerMobEnchant("fast", new FastMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 2)));
    public static final MobEnchant SLOW = registerMobEnchant("slow", new SlowMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 2)));


    private static MobEnchant registerMobEnchant(String id, MobEnchant attribSup) {
        return Registry.register(EWModRegistry.MOB_ENCHANT_REGISTRY, modLoc(id), attribSup);
    }

    public static void init() {

    }
}
