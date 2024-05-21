package baguchan.enchantwithmob;

import baguchan.enchantwithmob.client.ModModelLayers;
import baguchan.enchantwithmob.client.model.EnchanterModel;
import baguchan.enchantwithmob.client.render.EnchanterRenderer;
import baguchan.enchantwithmob.platform.Services;
import baguchan.enchantwithmob.registry.EWEntityTypes;

public class EnchantWithMobClient {

    public static void client() {
        Services.RENDER_REGISTRY.entityModel(EWEntityTypes.ENCHANTER, EnchanterRenderer::new);
        Services.RENDER_REGISTRY.layerDefinition(ModModelLayers.ENCHANTER, EnchanterModel::createBodyLayer);
    }
}