package baguchan.enchantwithmob;

import baguchan.enchantwithmob.command.MobEnchantingCommand;
import baguchan.enchantwithmob.network.ForgeNetworkHelper;
import baguchan.enchantwithmob.registry.EWEntityTypes;
import baguchan.enchantwithmob.registry.EwArgumentTypeInfos;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.world.entity.raid.Raid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(EWConstants.MOD_ID)
public class EnchantWithMobForge {
    
    public EnchantWithMobForge() {
        EnchantWithMob.init();
        ForgeNetworkHelper.init();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.addListener(this::commonEvent);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> EnchantWithMobClient.client());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EnchantConfig.COMMON_SPEC);
        EwArgumentTypeInfos.ARGUMENT_TYPE_CLASSES.forEach(ArgumentTypeInfos::registerByClass);
    }

    private void registerCommands(RegisterCommandsEvent evt) {
        MobEnchantingCommand.register(evt.getDispatcher());
    }


    public void commonEvent(FMLCommonSetupEvent event) {
        Raid.RaiderType.create("enchanter", EWEntityTypes.ENCHANTER.get(), new int[]{0, 0, 1, 0, 1, 1, 2, 1});
    }
}