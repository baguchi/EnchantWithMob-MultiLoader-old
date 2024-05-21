package baguchan.enchantwithmob.client.render;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class EnchantRenderType extends RenderType {
    public static final Function<ResourceLocation, RenderType> ENCHANTED_EYES = Util.memoize((p_173253_) -> {
        TextureStateShard renderstateshard$texturestateshard = new TextureStateShard(p_173253_, false, false);
        return new CompositeRenderType("enchanted_eyes", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, CompositeState.builder().setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState(renderstateshard$texturestateshard).setCullState(NO_CULL).setTransparencyState(TRANSLUCENT_TRANSPARENCY).createCompositeState(false));
    });

    public EnchantRenderType(String $$0, VertexFormat $$1, VertexFormat.Mode $$2, int $$3, boolean $$4, boolean $$5, Runnable $$6, Runnable $$7) {
        super($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7);
    }

    public static CompositeRenderType enchantSwirl(ResourceLocation resourceLocation) {
        return new CompositeRenderType("enchant_effect", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, CompositeState.builder().setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER).setWriteMaskState(COLOR_WRITE).setTextureState(new TextureStateShard(resourceLocation, false, false)).setTransparencyState(ADDITIVE_TRANSPARENCY).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));
    }

    public static CompositeRenderType enchantBeamSwirl(ResourceLocation resourceLocation) {
        return new CompositeRenderType("enchant_beam_effect", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, CompositeState.builder().setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER).setWriteMaskState(COLOR_WRITE).setTextureState(new TextureStateShard(resourceLocation, false, false)).setTransparencyState(ADDITIVE_TRANSPARENCY).setCullState(NO_CULL).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));
    }

    private static final class CompositeRenderType extends RenderType {
        static final BiFunction<ResourceLocation, CullStateShard, RenderType> OUTLINE = Util.memoize((resourceLocation, cullStateShard) -> {
            return new CompositeRenderType("outline", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, EnchantRenderType.CompositeState.builder().setShaderState(RENDERTYPE_OUTLINE_SHADER).setTextureState(new EnchantRenderType.TextureStateShard(resourceLocation, false, false)).setCullState(cullStateShard).setDepthTestState(NO_DEPTH_TEST).setOutputState(OUTLINE_TARGET).createCompositeState(OutlineProperty.IS_OUTLINE));
        });
        private final EnchantRenderType.CompositeState state;
        private final Optional<RenderType> outline;
        private final boolean isOutline;

        CompositeRenderType(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean bl, boolean bl2, EnchantRenderType.CompositeState compositeState) {
            super(string, vertexFormat, mode, i, bl, bl2, () -> {
                compositeState.states.forEach(RenderStateShard::setupRenderState);
            }, () -> {
                compositeState.states.forEach(RenderStateShard::clearRenderState);
            });
            this.state = compositeState;
            this.outline = compositeState.outlineProperty == OutlineProperty.AFFECTS_OUTLINE ? compositeState.textureState.cutoutTexture().map((resourceLocation) -> {
                return (RenderType)OUTLINE.apply(resourceLocation, compositeState.cullState);
            }) : Optional.empty();
            this.isOutline = compositeState.outlineProperty == OutlineProperty.IS_OUTLINE;
        }

        public Optional<RenderType> outline() {
            return this.outline;
        }

        public boolean isOutline() {
            return this.isOutline;
        }

        protected final EnchantRenderType.CompositeState state() {
            return this.state;
        }

        public String toString() {
            return "RenderType[" + this.name + ":" + this.state + "]";
        }
    }

    protected static final class CompositeState {
        private final EmptyTextureStateShard textureState;
        private final RenderStateShard.ShaderStateShard shaderState;
        private final RenderStateShard.TransparencyStateShard transparencyState;
        private final RenderStateShard.DepthTestStateShard depthTestState;
        final RenderStateShard.CullStateShard cullState;
        private final RenderStateShard.LightmapStateShard lightmapState;
        private final RenderStateShard.OverlayStateShard overlayState;
        private final RenderStateShard.LayeringStateShard layeringState;
        private final RenderStateShard.OutputStateShard outputState;
        private final RenderStateShard.TexturingStateShard texturingState;
        private final RenderStateShard.WriteMaskStateShard writeMaskState;
        private final RenderStateShard.LineStateShard lineState;
        private final RenderStateShard.ColorLogicStateShard colorLogicState;
        final OutlineProperty outlineProperty;
        final ImmutableList<RenderStateShard> states;

        CompositeState(EmptyTextureStateShard $$0, RenderStateShard.ShaderStateShard $$1, RenderStateShard.TransparencyStateShard $$2, RenderStateShard.DepthTestStateShard $$3, RenderStateShard.CullStateShard $$4, RenderStateShard.LightmapStateShard $$5, RenderStateShard.OverlayStateShard $$6, RenderStateShard.LayeringStateShard $$7, RenderStateShard.OutputStateShard $$8, RenderStateShard.TexturingStateShard $$9, RenderStateShard.WriteMaskStateShard $$10, RenderStateShard.LineStateShard $$11, RenderStateShard.ColorLogicStateShard $$12, OutlineProperty $$13) {
            this.textureState = $$0;
            this.shaderState = $$1;
            this.transparencyState = $$2;
            this.depthTestState = $$3;
            this.cullState = $$4;
            this.lightmapState = $$5;
            this.overlayState = $$6;
            this.layeringState = $$7;
            this.outputState = $$8;
            this.texturingState = $$9;
            this.writeMaskState = $$10;
            this.lineState = $$11;
            this.colorLogicState = $$12;
            this.outlineProperty = $$13;
            this.states = ImmutableList.of(this.textureState, this.shaderState, this.transparencyState, this.depthTestState, this.cullState, this.lightmapState, this.overlayState, this.layeringState, this.outputState, this.texturingState, this.writeMaskState, this.colorLogicState, new RenderStateShard[]{this.lineState});
        }

        public String toString() {
            return "CompositeState[" + this.states + ", outlineProperty=" + this.outlineProperty + "]";
        }

        public static CompositeState.CompositeStateBuilder builder() {
            return new CompositeState.CompositeStateBuilder();
        }

        public static class CompositeStateBuilder {
            private EmptyTextureStateShard textureState;
            private RenderStateShard.ShaderStateShard shaderState;
            private RenderStateShard.TransparencyStateShard transparencyState;
            private RenderStateShard.DepthTestStateShard depthTestState;
            private RenderStateShard.CullStateShard cullState;
            private RenderStateShard.LightmapStateShard lightmapState;
            private RenderStateShard.OverlayStateShard overlayState;
            private RenderStateShard.LayeringStateShard layeringState;
            private RenderStateShard.OutputStateShard outputState;
            private RenderStateShard.TexturingStateShard texturingState;
            private RenderStateShard.WriteMaskStateShard writeMaskState;
            private RenderStateShard.LineStateShard lineState;
            private RenderStateShard.ColorLogicStateShard colorLogicState;

            CompositeStateBuilder() {
                this.textureState = new EmptyTextureStateShard();
                this.shaderState = RenderStateShard.NO_SHADER;
                this.transparencyState = RenderStateShard.NO_TRANSPARENCY;
                this.depthTestState = RenderStateShard.LEQUAL_DEPTH_TEST;
                this.cullState = RenderStateShard.CULL;
                this.lightmapState = RenderStateShard.NO_LIGHTMAP;
                this.overlayState = RenderStateShard.NO_OVERLAY;
                this.layeringState = RenderStateShard.NO_LAYERING;
                this.outputState = RenderStateShard.MAIN_TARGET;
                this.texturingState = RenderStateShard.DEFAULT_TEXTURING;
                this.writeMaskState = RenderStateShard.COLOR_DEPTH_WRITE;
                this.lineState = RenderStateShard.DEFAULT_LINE;
                this.colorLogicState = RenderStateShard.NO_COLOR_LOGIC;
            }

            public CompositeState.CompositeStateBuilder setTextureState(EmptyTextureStateShard $$0) {
                this.textureState = $$0;
                return this;
            }

            public CompositeState.CompositeStateBuilder setShaderState(RenderStateShard.ShaderStateShard $$0) {
                this.shaderState = $$0;
                return this;
            }

            public CompositeState.CompositeStateBuilder setTransparencyState(RenderStateShard.TransparencyStateShard $$0) {
                this.transparencyState = $$0;
                return this;
            }

            public CompositeState.CompositeStateBuilder setDepthTestState(RenderStateShard.DepthTestStateShard $$0) {
                this.depthTestState = $$0;
                return this;
            }

            public CompositeState.CompositeStateBuilder setCullState(RenderStateShard.CullStateShard $$0) {
                this.cullState = $$0;
                return this;
            }

            public CompositeState.CompositeStateBuilder setLightmapState(RenderStateShard.LightmapStateShard $$0) {
                this.lightmapState = $$0;
                return this;
            }

            public CompositeState.CompositeStateBuilder setOverlayState(RenderStateShard.OverlayStateShard $$0) {
                this.overlayState = $$0;
                return this;
            }

            public CompositeState.CompositeStateBuilder setLayeringState(RenderStateShard.LayeringStateShard $$0) {
                this.layeringState = $$0;
                return this;
            }

            public CompositeState.CompositeStateBuilder setOutputState(RenderStateShard.OutputStateShard $$0) {
                this.outputState = $$0;
                return this;
            }

            public CompositeState.CompositeStateBuilder setTexturingState(RenderStateShard.TexturingStateShard $$0) {
                this.texturingState = $$0;
                return this;
            }

            public CompositeState.CompositeStateBuilder setWriteMaskState(RenderStateShard.WriteMaskStateShard $$0) {
                this.writeMaskState = $$0;
                return this;
            }

            public CompositeState.CompositeStateBuilder setLineState(RenderStateShard.LineStateShard $$0) {
                this.lineState = $$0;
                return this;
            }

            public CompositeState.CompositeStateBuilder setColorLogicState(RenderStateShard.ColorLogicStateShard $$0) {
                this.colorLogicState = $$0;
                return this;
            }

            public CompositeState createCompositeState(boolean $$0) {
                return this.createCompositeState($$0 ? OutlineProperty.AFFECTS_OUTLINE : OutlineProperty.NONE);
            }

            public CompositeState createCompositeState(OutlineProperty $$0) {
                return new CompositeState(this.textureState, this.shaderState, this.transparencyState, this.depthTestState, this.cullState, this.lightmapState, this.overlayState, this.layeringState, this.outputState, this.texturingState, this.writeMaskState, this.lineState, this.colorLogicState, $$0);
            }
        }
    }

    private static enum OutlineProperty {
        NONE("none"),
        IS_OUTLINE("is_outline"),
        AFFECTS_OUTLINE("affects_outline");

        private final String name;

        private OutlineProperty(String $$0) {
            this.name = $$0;
        }

        public String toString() {
            return this.name;
        }
    }

    protected static class EmptyTextureStateShard extends RenderStateShard {
        public EmptyTextureStateShard(Runnable $$0, Runnable $$1) {
            super("texture", $$0, $$1);
        }

        EmptyTextureStateShard() {
            super("texture", () -> {
            }, () -> {
            });
        }

        protected Optional<ResourceLocation> cutoutTexture() {
            return Optional.empty();
        }
    }

    protected static class TextureStateShard extends EmptyTextureStateShard {
        private final Optional<ResourceLocation> texture;
        private final boolean blur;
        private final boolean mipmap;

        public TextureStateShard(ResourceLocation $$0, boolean $$1, boolean $$2) {
            super(() -> {
                TextureManager $$3 = Minecraft.getInstance().getTextureManager();
                $$3.getTexture($$0).setFilter($$1, $$2);
                RenderSystem.setShaderTexture(0, $$0);
            }, () -> {
            });
            this.texture = Optional.of($$0);
            this.blur = $$1;
            this.mipmap = $$2;
        }

        public String toString() {
            return this.name + "[" + this.texture + "(blur=" + this.blur + ", mipmap=" + this.mipmap + ")]";
        }

        protected Optional<ResourceLocation> cutoutTexture() {
            return this.texture;
        }
    }
}
