package baguchan.enchantwithmob.registry;

import baguchan.enchantwithmob.EWConstants;
import baguchan.enchantwithmob.item.EnchantersBookItem;
import baguchan.enchantwithmob.item.MobEnchantBookItem;
import baguchan.enchantwithmob.item.MobUnEnchantBookItem;
import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

import static baguchan.enchantwithmob.utils.ResourceLocationHelper.modLoc;
import static net.minecraft.resources.ResourceKey.createRegistryKey;

public class EWItems {
    public static final Supplier<Item> ENCHANTERS_BOOK = registerItem("enchanters_book", () -> new EnchantersBookItem((new Item.Properties()).stacksTo(1).durability(64)));
    public static final Supplier<Item> MOB_ENCHANT_BOOK = registerItem("mob_enchant_book", () -> new MobEnchantBookItem((new Item.Properties()).stacksTo(1).durability(5)));
    public static final Supplier<Item> MOB_UNENCHANT_BOOK = registerItem("mob_unenchant_book", () -> new MobUnEnchantBookItem((new Item.Properties()).stacksTo(1).durability(5)));

    public static void register() {
    }

    private static Supplier<Item> registerItem(String id, Supplier<Item> attribSup) {
        return Services.REGISTRAR.registerObject(modLoc(id), attribSup, BuiltInRegistries.ITEM);
    }
}