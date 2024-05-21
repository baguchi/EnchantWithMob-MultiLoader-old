package baguchan.enchantwithmob.platform;

import baguchan.enchantwithmob.platform.services.MobRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.function.Supplier;

public class FabricMobRegistryImpl implements MobRegistry {
    public void attributes(Supplier<? extends EntityType<? extends LivingEntity>> type, Supplier<AttributeSupplier.Builder> builder) {
        FabricDefaultAttributeRegistry.register(type.get(), builder.get());
    }

    public Item spawnEgg(Supplier<? extends EntityType<? extends Mob>> entity, int backgroundColor, int highlightColor, Item.Properties properties) {
        Item entry = new SpawnEggItem(entity.get(), backgroundColor, highlightColor, properties);
        MobRegistry.EGGS.put(entity.get(), entry);
        return entry;
    }

    public <T extends Mob> void registrySpawnPlacement(Supplier<EntityType<T>> entityType, SpawnPlacements.Type placementType, Heightmap.Types heightTypes, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
        SpawnPlacements.register(entityType.get(), placementType, heightTypes, spawnPredicate);
    }
}