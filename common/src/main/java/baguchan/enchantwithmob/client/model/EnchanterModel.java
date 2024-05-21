package baguchan.enchantwithmob.client.model;

import baguchan.enchantwithmob.client.animation.EnchanterAnimation;
import baguchan.enchantwithmob.entity.Enchanter;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class EnchanterModel<T extends Enchanter> extends HierarchicalModel<T> {
	private final ModelPart realRoot;
	private final ModelPart everything;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart body;
	private final ModelPart Cape;
	private final ModelPart head;
	private final ModelPart hat;
	private final ModelPart leftEye;
	private final ModelPart rightEye;
	private final ModelPart righteyebrows;
	private final ModelPart lefteyebrows;
	private final ModelPart left_arm;
	private final ModelPart leftHand;
	private final ModelPart right_arm;
	private final ModelPart rightHand;
	private final ModelPart arms;
	private final ModelPart book;
	private final ModelPart leftBookCover;
	private final ModelPart rightBookCover;
	private final ModelPart pages;
	private final ModelPart leftPage;
	private final ModelPart leftPage2;

	public EnchanterModel(ModelPart root) {
		this.realRoot = root;
		this.everything = root.getChild("everything");
		this.left_leg = this.everything.getChild("left_leg");
		this.right_leg = this.everything.getChild("right_leg");
		this.body = this.everything.getChild("body");
		this.Cape = this.body.getChild("Cape");
		this.head = this.body.getChild("head");
		this.hat = this.head.getChild("hat");
		this.leftEye = this.head.getChild("leftEye");
		this.rightEye = this.head.getChild("rightEye");
		this.righteyebrows = this.head.getChild("righteyebrows");
		this.lefteyebrows = this.head.getChild("lefteyebrows");
		this.left_arm = this.body.getChild("left_arm");
		this.leftHand = this.left_arm.getChild("leftHand");
		this.right_arm = this.body.getChild("right_arm");
		this.rightHand = this.right_arm.getChild("rightHand");
		this.arms = this.body.getChild("arms");
		this.book = this.everything.getChild("book");
		this.leftBookCover = this.book.getChild("leftBookCover");
		this.rightBookCover = this.book.getChild("rightBookCover");
		this.pages = this.book.getChild("pages");
		this.leftPage = this.pages.getChild("leftPage");
		this.leftPage2 = this.pages.getChild("leftPage2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition everything = partdefinition.addOrReplaceChild("everything", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition left_leg = everything.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, -12.0F, 0.0F));

		PartDefinition right_leg = everything.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -12.0F, 0.0F));

		PartDefinition body = everything.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, -12.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 38).addBox(-4.0F, -12.0F, -3.0F, 8.0F, 18.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition Cape = body.addOrReplaceChild("Cape", CubeListBuilder.create().texOffs(0, 64).addBox(-6.0F, 0.0F, 0.0F, 12.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -12.0F, 3.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(24, 0).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(1, 91).addBox(-5.0F, -8.0F, -5.0F, 10.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

		PartDefinition leftEye = head.addOrReplaceChild("leftEye", CubeListBuilder.create().texOffs(6, 5).addBox(0.0F, -1.4604F, 0.74F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -2.5F, -4.75F));

		PartDefinition rightEye = head.addOrReplaceChild("rightEye", CubeListBuilder.create().texOffs(6, 5).addBox(-1.0F, -1.4604F, 0.74F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -2.5F, -4.75F));

		PartDefinition righteyebrows = head.addOrReplaceChild("righteyebrows", CubeListBuilder.create(), PartPose.offset(-2.5F, -4.9604F, -3.5196F));

		PartDefinition righteyebrows_r1 = righteyebrows.addOrReplaceChild("righteyebrows_r1", CubeListBuilder.create().texOffs(0, 5).addBox(-1.5F, -1.0F, -0.5902F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.0902F, 0.0F, 3.1416F, 0.0F));

		PartDefinition lefteyebrows = head.addOrReplaceChild("lefteyebrows", CubeListBuilder.create(), PartPose.offset(2.5F, -4.9604F, -3.5196F));

		PartDefinition lefteyebrows_r1 = lefteyebrows.addOrReplaceChild("lefteyebrows_r1", CubeListBuilder.create().texOffs(0, 5).mirror().addBox(-1.5F, -1.0F, -0.5902F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -0.0902F, 0.0F, 3.1416F, 0.0F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 46).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -10.0F, 0.0F));

		PartDefinition leftHand = left_arm.addOrReplaceChild("leftHand", CubeListBuilder.create(), PartPose.offset(1.0F, 11.0F, 0.0F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 46).mirror().addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, -10.0F, 0.0F));

		PartDefinition rightHand = right_arm.addOrReplaceChild("rightHand", CubeListBuilder.create(), PartPose.offset(-1.0F, 11.0F, 0.0F));

		PartDefinition arms = body.addOrReplaceChild("arms", CubeListBuilder.create().texOffs(44, 22).mirror().addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(44, 22).addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, -0.95F, -0.7854F, 0.0F, 0.0F));

		PartDefinition book = everything.addOrReplaceChild("book", CubeListBuilder.create().texOffs(43, 72).addBox(-2.0F, -3.0F, -1.0F, 3.0F, 9.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offset(0.5F, -18.5F, -9.0F));

		PartDefinition leftBookCover = book.addOrReplaceChild("leftBookCover", CubeListBuilder.create().texOffs(26, 72).mirror().addBox(-8.0F, -3.0F, -1.0F, 8.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(44, 62).addBox(-6.9F, -2.5F, -0.5F, 7.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition rightBookCover = book.addOrReplaceChild("rightBookCover", CubeListBuilder.create().texOffs(26, 62).addBox(0.0F, -3.0F, -1.0F, 8.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(44, 62).addBox(-0.1F, -2.5F, -0.5F, 7.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition pages = book.addOrReplaceChild("pages", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, 0.0F, -0.1F, 0.0F, 1.5708F, 0.0F));

		PartDefinition leftPage = pages.addOrReplaceChild("leftPage", CubeListBuilder.create().texOffs(26, 82).addBox(-7.0F, -3.0F, 0.01F, 7.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 0.0F));

		PartDefinition leftPage2 = pages.addOrReplaceChild("leftPage2", CubeListBuilder.create().texOffs(26, 82).addBox(-7.0F, -3.0F, 0.01F, 7.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.head.xRot = headPitch * ((float) Math.PI / 180F);

		this.Cape.xRot = 0.1F + limbSwingAmount * 0.6F;

		if (this.riding) {
			this.applyStatic(EnchanterAnimation.SIT);
		}

		if (entity.castingAnimationState.isStarted()) {
			this.animate(entity.castingAnimationState, EnchanterAnimation.ENCHANCE, ageInTicks);
		} else if (entity.attackAnimationState.isStarted()) {
			this.animate(entity.attackAnimationState, EnchanterAnimation.ATTACK, ageInTicks);
		} else if (!entity.castingAnimationState.isStarted() && !entity.attackAnimationState.isStarted()) {
			if (!entity.idleAnimationState.isStarted()) {
				this.animateWalk(EnchanterAnimation.WALK, limbSwing, limbSwingAmount, 3.0F, 4.5F);
				this.applyStatic(EnchanterAnimation.WALK_STOP);
			} else {
				this.animate(entity.idleAnimationState, EnchanterAnimation.IDLE, ageInTicks);
			}
		}
	}

	@Override
	public ModelPart root() {
		return this.realRoot;
	}
}
