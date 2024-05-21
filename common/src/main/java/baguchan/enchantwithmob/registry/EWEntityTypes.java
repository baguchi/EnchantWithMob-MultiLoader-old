package baguchan.enchantwithmob.registry;

import baguchan.enchantwithmob.entity.Enchanter;
import baguchan.enchantwithmob.platform.Services;
import baguchan.enchantwithmob.utils.ResourceLocationHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.function.Supplier;

import static baguchan.enchantwithmob.utils.ResourceLocationHelper.modLoc;

public class EWEntityTypes {
    public static final Supplier<EntityType<Enchanter>> ENCHANTER = create("enchanter", () -> EntityType.Builder.of(Enchanter::new, MobCategory.MONSTER).sized(0.6F, 1.95F).build(ResourceLocationHelper.modLoc("enchanter").toString()));

    public static void register() {
        Services.MOB_REGISTRY.attributes(ENCHANTER, Enchanter::createAttributeMap);
        Services.MOB_REGISTRY.registrySpawnPlacement(ENCHANTER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
    }

   private static <T extends Entity, E extends EntityType<? extends T>> Supplier<E> create(String key, Supplier<E> builder) {
        return Services.REGISTRAR.<E>registerObject(modLoc(key), builder, (Registry<E>) BuiltInRegistries.ENTITY_TYPE);
    }
}