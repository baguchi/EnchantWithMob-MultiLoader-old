package baguchan.enchantwithmob.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class EnchantedWindModel<T extends LivingEntity> extends HierarchicalModel<T> {
    private static final float WIND_TOP_SPEED = 0.6F;
    private static final float WIND_MIDDLE_SPEED = 0.8F;
    private static final float WIND_BOTTOM_SPEED = 1.0F;
    private final ModelPart root;
    private final ModelPart windBody;
    private final ModelPart windTop;
    private final ModelPart windMid;
    private final ModelPart windBottom;

    public EnchantedWindModel(ModelPart p_312152_) {
        super(RenderType::entityTranslucent);
        this.root = p_312152_;
        this.windBody = p_312152_.getChild("wind_body");
        this.windBottom = this.windBody.getChild("wind_bottom");
        this.windMid = this.windBottom.getChild("wind_mid");
        this.windTop = this.windMid.getChild("wind_top");
    }

    public static LayerDefinition createWindBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition partdefinition1 = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition partdefinition2 = partdefinition1.addOrReplaceChild("rods", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 0.0F));
        partdefinition1.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, 0.0F));
        PartDefinition partdefinition3 = partdefinition.addOrReplaceChild("wind_body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition partdefinition4 = partdefinition3.addOrReplaceChild(
                "wind_bottom",
                CubeListBuilder.create().texOffs(1, 83).addBox(-2.5F, -7.0F, -2.5F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );
        PartDefinition partdefinition5 = partdefinition4.addOrReplaceChild(
                "wind_mid",
                CubeListBuilder.create()
                        .texOffs(74, 28)
                        .addBox(-6.0F, -6.0F, -6.0F, 12.0F, 6.0F, 12.0F, new CubeDeformation(0.0F))
                        .texOffs(78, 32)
                        .addBox(-4.0F, -6.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
                        .texOffs(49, 71)
                        .addBox(-2.5F, -6.0F, -2.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -7.0F, 0.0F)
        );
        partdefinition5.addOrReplaceChild(
                "wind_top",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-9.0F, -8.0F, -9.0F, 18.0F, 8.0F, 18.0F, new CubeDeformation(0.0F))
                        .texOffs(6, 6)
                        .addBox(-6.0F, -8.0F, -6.0F, 12.0F, 8.0F, 12.0F, new CubeDeformation(0.0F))
                        .texOffs(105, 57)
                        .addBox(-2.5F, -8.0F, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -6.0F, 0.0F)
        );
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    public void setupAnim(T p_312040_, float p_311926_, float p_312463_, float p_311788_, float p_311860_, float p_312017_) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        float f = p_311788_ * (float) Math.PI * -0.1F;
        this.windTop.x = Mth.cos(f) * 1.0F * 0.6F;
        this.windTop.z = Mth.sin(f) * 1.0F * 0.6F;
        this.windMid.x = Mth.sin(f) * 0.5F * 0.8F;
        this.windMid.z = Mth.cos(f) * 0.8F;
        this.windBottom.x = Mth.cos(f) * -0.25F * 1.0F;
        this.windBottom.z = Mth.sin(f) * -0.25F * 1.0F;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public ModelPart windTop() {
        return this.windTop;
    }

    public ModelPart windMiddle() {
        return this.windMid;
    }

    public ModelPart windBottom() {
        return this.windBottom;
    }
}
