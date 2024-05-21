package baguchan.enchantwithmob.registry;

import baguchan.enchantwithmob.item.MobEnchantBookItem;
import baguchan.enchantwithmob.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static baguchan.enchantwithmob.utils.ResourceLocationHelper.modLoc;

public class EWCreativeTabs {
    public static final Supplier<CreativeModeTab> ENCHANT_WITH_MOB = create("enchantwithmob", () -> {
        return CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0).title(Component.translatable("itemGroup.enchantwithmob"))
                .icon(() -> EWItems.MOB_ENCHANT_BOOK.get().getDefaultInstance())
                .displayItems((parameters, output) -> {
                    output.acceptAll(Stream.of(
                            EWItems.MOB_UNENCHANT_BOOK
                    ).map(sup -> {
                        return sup.get().getDefaultInstance();
                    }).toList());
                    output.acceptAll(MobEnchantBookItem.generateMobEnchantmentBookTypesOnlyMaxLevel());
                }).build();
    });

    private static Supplier<CreativeModeTab> create(String key, Supplier<CreativeModeTab> builder) {
        return Services.REGISTRAR.registerObject(modLoc(key), builder, BuiltInRegistries.CREATIVE_MODE_TAB);
    }

    public static void register() {

    }
}
