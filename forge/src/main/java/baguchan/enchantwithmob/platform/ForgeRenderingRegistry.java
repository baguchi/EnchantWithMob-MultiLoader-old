package baguchan.enchantwithmob.platform;

import baguchan.enchantwithmob.EWConstants;
import baguchan.enchantwithmob.platform.services.RenderRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = EWConstants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeRenderingRegistry implements RenderRegistry {
    private static final Set<Consumer<EntityRenderersEvent.RegisterRenderers>> RENDERERS = ConcurrentHashMap.newKeySet();
    private static final Set<Consumer<EntityRenderersEvent.RegisterLayerDefinitions>> DEFINITIONS = ConcurrentHashMap.newKeySet();


    @SubscribeEvent
    public static void onRendererRegistry(EntityRenderersEvent.RegisterRenderers event) {
        RENDERERS.forEach(registry -> registry.accept(event));
    }

    @SubscribeEvent
    public static void onLayerDefinitionRegistry(EntityRenderersEvent.RegisterLayerDefinitions event) {
        DEFINITIONS.forEach(registry -> registry.accept(event));
    }

    @Override
    public <T extends Entity> void entityModel(Supplier<? extends EntityType<? extends T>> type, EntityRendererProvider<T> provider) {
        RENDERERS.add(event -> event.registerEntityRenderer(type.get(), provider));
    }

    @Override
    public void layerDefinition(ModelLayerLocation location, Supplier<LayerDefinition> definition) {
        DEFINITIONS.add(event -> event.registerLayerDefinition(location, definition));
    }
}
