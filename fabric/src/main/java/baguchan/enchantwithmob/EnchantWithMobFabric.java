package baguchan.enchantwithmob;

import baguchan.enchantwithmob.network.FabricNetworkHelper;
import baguchan.enchantwithmob.registry.EWEntityTypes;
import fuzs.extensibleenums.api.extensibleenums.v1.BuiltInEnumFactories;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.level.entity.EntityInLevelCallback;

public class EnchantWithMobFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        EnchantWithMob.init();

        FabricNetworkHelper.init();
        BuiltInEnumFactories.createRaiderType("enchanter", EWEntityTypes.ENCHANTER.get(), new int[]{0, 0, 1, 0, 1, 1, 2, 1});
    }
}
