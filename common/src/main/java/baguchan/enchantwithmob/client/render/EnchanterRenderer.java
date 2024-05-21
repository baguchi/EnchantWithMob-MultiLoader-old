package baguchan.enchantwithmob.client.render;

import baguchan.enchantwithmob.EWConstants;
import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.client.ModModelLayers;
import baguchan.enchantwithmob.client.model.EnchanterModel;
import baguchan.enchantwithmob.entity.Enchanter;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class EnchanterRenderer<T extends Enchanter> extends MobRenderer<T, EnchanterModel<T>> {
    private static final ResourceLocation ILLAGER = new ResourceLocation(EWConstants.MOD_ID, "textures/entity/enchanter_clothed.png");

    private static final ResourceLocation GLOW = new ResourceLocation(EWConstants.MOD_ID, "textures/entity/enchanter_clothed_glow.png");
    private static final RenderType GLOW_TYPE = RenderType.eyes(GLOW);


    public EnchanterRenderer(EntityRendererProvider.Context p_173952_) {
        super(p_173952_, new EnchanterModel<>(p_173952_.bakeLayer(ModModelLayers.ENCHANTER)), 0.5F);
        this.addLayer(new EyesLayer<T, EnchanterModel<T>>(this) {
            @Override
            public RenderType renderType() {
                return GLOW_TYPE;
            }
        });

    }

    @Override
    public ResourceLocation getTextureLocation(T p_110775_1_) {
        return ILLAGER;
    }
}