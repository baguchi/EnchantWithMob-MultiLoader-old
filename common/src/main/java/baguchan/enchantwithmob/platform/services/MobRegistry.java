package baguchan.enchantwithmob.platform.services;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Map;
import java.util.function.Supplier;

public interface MobRegistry {
    public static final Map<EntityType<? extends Mob>, Item> EGGS = Maps.newIdentityHashMap();

    void attributes(Supplier<? extends EntityType<? extends LivingEntity>> type, Supplier<AttributeSupplier.Builder> builder);

    Item spawnEgg(Supplier<? extends EntityType<? extends Mob>> entity, int backgroundColor, int highlightColor, Item.Properties properties);

    <T extends Mob> void registrySpawnPlacement(EntityType<T> entityType, SpawnPlacements.Type placementType, Heightmap.Types heightTypes, SpawnPlacements.SpawnPredicate<T> spawnPredicate);

    static Iterable<Item> eggs() {
        return Iterables.unmodifiableIterable(EGGS.values());
    }
}