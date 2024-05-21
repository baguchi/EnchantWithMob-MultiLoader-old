package baguchan.enchantwithmob.datagen;

import baguchan.enchantwithmob.EWConstants;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.JsonKeySortOrderCallback;
import net.minecraft.core.RegistrySetBuilder;
import org.jetbrains.annotations.Nullable;

public class EnchantWithMobDataGenFabric implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {

    }

    @Nullable
    @Override
    public String getEffectiveModId() {
        return EWConstants.MOD_ID;
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        DataGeneratorEntrypoint.super.buildRegistry(registryBuilder);
    }

    @Override
    public void addJsonKeySortOrders(JsonKeySortOrderCallback callback) {
        DataGeneratorEntrypoint.super.addJsonKeySortOrders(callback);
    }
}
