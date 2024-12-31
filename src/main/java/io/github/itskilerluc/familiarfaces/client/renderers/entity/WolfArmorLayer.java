package io.github.itskilerluc.familiarfaces.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.itskilerluc.familiarfaces.FamiliarFaces;
import io.github.itskilerluc.familiarfaces.server.entities.ai.WolfCrackiness;
import io.github.itskilerluc.familiarfaces.server.init.ItemRegistry;
import io.github.itskilerluc.familiarfaces.server.items.WolfArmor;
import io.github.itskilerluc.familiarfaces.server.util.WolfArmorUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
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
        if (WolfArmorUtils.hasArmor(livingEntity)) {
            ItemStack itemstack = WolfArmorUtils.getBodyArmorItem(livingEntity);
            if (itemstack.getItem() instanceof WolfArmor animalarmoritem) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
                this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(animalarmoritem.getTexture()));
                this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
                this.maybeRenderColoredLayer(poseStack, bufferSource, packedLight, itemstack, animalarmoritem);
                this.maybeRenderCracks(poseStack, bufferSource, packedLight, itemstack);
            }
        }
    }

    private void maybeRenderColoredLayer(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack, WolfArmor armorItem) {
        if (armorStack.is(ItemRegistry.WOLF_ARMOR.get())) {
            int i = ((DyeableLeatherItem) armorStack.getItem()).getColor(armorStack);
            if (FastColor.ARGB32.alpha(i) == 0) {
                return;
            }

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
                            FastColor.ARGB32.red(i),
                            FastColor.ARGB32.blue(i),
                            FastColor.ARGB32.green(i),
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
