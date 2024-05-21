package baguchan.enchantwithmob.platform;

import baguchan.enchantwithmob.platform.services.MobRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Supplier;

public class ForgeMobRegistryImpl implements MobRegistry {
    public void attributes(Supplier<? extends EntityType<? extends LivingEntity>> type, Supplier<AttributeSupplier.Builder> builder) {
        FMLJavaModLoadingContext.get().getModEventBus().<EntityAttributeCreationEvent>addListener(event -> event.put(type.get(), builder.get().build()));
    }

    public Item spawnEgg(Supplier<? extends EntityType<? extends Mob>> entity, int backgroundColor, int highlightColor, Item.Properties properties) {
        return new ForgeSpawnEggItem(entity, backgroundColor, highlightColor, properties);
    }

    public <T extends Mob> void registrySpawnPlacement(Supplier<EntityType<T>> entityType, SpawnPlacements.Type placementType, Heightmap.Types heightTypes, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
        FMLJavaModLoadingContext.get().getModEventBus().<SpawnPlacementRegisterEvent>addListener(event -> event.register(entityType.get(), placementType, heightTypes, spawnPredicate, SpawnPlacementRegisterEvent.Operation.REPLACE));
    }
}