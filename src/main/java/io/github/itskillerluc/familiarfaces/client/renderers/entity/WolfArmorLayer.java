package io.github.itskillerluc.familiarfaces.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.itskillerluc.familiarfaces.FamiliarFaces;
import io.github.itskillerluc.familiarfaces.server.entities.ai.WolfCrackiness;
import io.github.itskillerluc.familiarfaces.server.init.ItemRegistry;
import io.github.itskillerluc.familiarfaces.server.items.WolfArmor;
import io.github.itskillerluc.familiarfaces.server.util.WolfArmorUtils;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class WolfArmorLayer extends RenderLayer<Wolf, WolfModel<Wolf>> {
    public static final ModelLayerLocation WOLF_ARMOR = new ModelLayerLocation(new ResourceLocation(FamiliarFaces.MODID, "wolf_armor"), "main");
    private static final Map<WolfCrackiness.Level, ResourceLocation> ARMOR_CRACK_LOCATIONS = Map.of(
            WolfCrackiness.Level.LOW,
            new ResourceLocation(FamiliarFaces.MODID, "textures/entity/wolf/wolf_armor_crackiness_low.png"),
            WolfCrackiness.Level.MEDIUM,
            new ResourceLocation(FamiliarFaces.MODID, "textures/entity/wolf/wolf_armor_crackiness_medium.png"),
            WolfCrackiness.Level.HIGH,
            new ResourceLocation(FamiliarFaces.MODID, "textures/entity/wolf/wolf_armor_crackiness_high.png")
    );
    private final WolfModel<Wolf> model;

    public WolfArmorLayer(RenderLayerParent<Wolf, WolfModel<Wolf>> renderer, EntityModelSet models) {
        super(renderer);
        this.model = new WolfModel<>(models.bakeLayer(WOLF_ARMOR));
    }

    public void render(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            Wolf livingEntity,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        ItemStack itemstack = WolfArmorUtils.getBodyArmorItem(livingEntity);
        if (itemstack.getItem() instanceof WolfArmor animalarmoritem) {
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
            this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(animalarmoritem.getTexture()));
            this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            this.maybeRenderColoredLayer(poseStack, bufferSource, packedLight, itemstack, animalarmoritem);
            this.maybeRenderCracks(poseStack, bufferSource, packedLight, itemstack);
            if (itemstack.hasFoil()) {
                renderGlint(poseStack, bufferSource, packedLight, this.model);
            }
        }
    }

    private void renderGlint(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, net.minecraft.client.model.Model pModel) {
        pModel.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.entityGlint()), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void maybeRenderColoredLayer(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack, WolfArmor armorItem) {
        if (armorStack.is(ItemRegistry.WOLF_ARMOR.get())) {
            if (!armorStack.hasTag() || !armorStack.getTag().contains("display", 10) || !armorStack.getTag().getCompound("display").contains("color", 3)) {
                return;
            }
            int i = ((DyeableLeatherItem) armorStack.getItem()).getColor(armorStack);

            ResourceLocation resourcelocation = armorItem.getOverlayTexture();
            if (resourcelocation == null) {
                return;
            }

            this.model
                    .renderToBuffer(
                            poseStack,
                            buffer.getBuffer(RenderType.entityCutoutNoCull(resourcelocation)),
                            packedLight,
                            OverlayTexture.NO_OVERLAY,
                            FastColor.ARGB32.red(i) / 255f,
                            FastColor.ARGB32.green(i) / 255f,
                            FastColor.ARGB32.blue(i) / 255f,
                            1);
        }
    }

    private void maybeRenderCracks(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack) {
        WolfCrackiness.Level crackiness$level = WolfCrackiness.WOLF_ARMOR.byDamage(armorStack);
        if (crackiness$level != WolfCrackiness.Level.NONE) {
            ResourceLocation resourcelocation = ARMOR_CRACK_LOCATIONS.get(crackiness$level);
            VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(resourcelocation));
            this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        }
    }
}
