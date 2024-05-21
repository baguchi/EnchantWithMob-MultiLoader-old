package baguchan.enchantwithmob;

import baguchan.enchantwithmob.network.ForgeNetworkHelper;
import baguchan.enchantwithmob.registry.EWEntityTypes;
import net.minecraft.world.entity.raid.Raid;
import net.minecraftforge.fml.common.Mod;

@Mod(EWConstants.MOD_ID)
public class EnchantWithMobForge {
    
    public EnchantWithMobForge() {
        EnchantWithMob.init();
        ForgeNetworkHelper.init();
        Raid.RaiderType.create("enchanter", EWEntityTypes.ENCHANTER.get(), new int[]{0, 0, 1, 0, 1, 1, 2, 1});
    }
}