package baguchan.enchantwithmob.platform.services;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public interface RenderRegistry {
    <T extends Entity> void entityModel(Supplier<? extends EntityType<? extends T>> type, EntityRendererProvider<T> provider);

    void layerDefinition(ModelLayerLocation location, Supplier<LayerDefinition> definition);
}
