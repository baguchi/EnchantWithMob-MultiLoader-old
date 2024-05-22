package baguchan.enchantwithmob.registry;

import baguchan.enchantwithmob.command.MobEnchantArgument;
import baguchan.enchantwithmob.platform.Services;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static baguchan.enchantwithmob.utils.ResourceLocationHelper.modLoc;

public class EwArgumentTypeInfos {
    public static final Map<Class, ArgumentTypeInfo<?, ?>> ARGUMENT_TYPE_CLASSES = new HashMap<>();
    private static final Map<ResourceLocation, ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES = new HashMap<>();


    public static final Supplier<ArgumentTypeInfo<?, ?>> MOB_ENCHANT = create("mob_enchant",
            MobEnchantArgument.class, SingletonArgumentInfo.contextFree(MobEnchantArgument::mobEnchantment));

    private static Supplier<ArgumentTypeInfo<?, ?>> create(String key, Class<? extends ArgumentType<?>> clazz, ArgumentTypeInfo<?, ?> builder) {
        ARGUMENT_TYPE_CLASSES.put(clazz, builder);
        ARGUMENT_TYPES.put(modLoc(key), builder);
        return Services.REGISTRAR.registerObject(modLoc(key), () -> builder, BuiltInRegistries.COMMAND_ARGUMENT_TYPE);
    }


    public static void register() {

    }
}
