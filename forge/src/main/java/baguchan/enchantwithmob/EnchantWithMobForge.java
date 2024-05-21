package baguchan.enchantwithmob;

import baguchan.enchantwithmob.network.ForgeNetworkHelper;
import baguchan.enchantwithmob.registry.EWEntityTypes;
import net.minecraft.world.entity.raid.Raid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(EWConstants.MOD_ID)
public class EnchantWithMobForge {
    
    public EnchantWithMobForge() {
        EnchantWithMob.init();
        ForgeNetworkHelper.init();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.<FMLCommonSetupEvent>addListener(this::commonEvent);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> EnchantWithMobClient.client());
    }

    public void commonEvent(FMLCommonSetupEvent event) {
        Raid.RaiderType.create("enchanter", EWEntityTypes.ENCHANTER.get(), new int[]{0, 0, 1, 0, 1, 1, 2, 1});
    }
}