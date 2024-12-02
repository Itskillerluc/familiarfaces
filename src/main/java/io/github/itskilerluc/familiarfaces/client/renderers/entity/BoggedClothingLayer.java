package io.github.itskilerluc.familiarfaces.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.itskilerluc.familiarfaces.FamiliarFaces;
import io.github.itskilerluc.familiarfaces.client.models.entity.BoggedModel;
import io.github.itskilerluc.familiarfaces.server.entities.Bogged;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BoggedClothingLayer extends RenderLayer<Bogged, BoggedModel> {
    public static final ModelLayerLocation BOGGED_OUTER_LAYER = new ModelLayerLocation(new ResourceLocation(FamiliarFaces.MODID, "bogged"), "outer_armor");
    private static final ResourceLocation BOGGED_OUTER_LAYER_LOCATION = new ResourceLocation(FamiliarFaces.MODID, "textures/entity/bogged_overlay.png");


    private final SkeletonModel<Bogged> layerModel;

    public BoggedClothingLayer(RenderLayerParent<Bogged, BoggedModel> renderer, EntityModelSet models) {
        super(renderer);
        this.layerModel = new SkeletonModel<>(models.bakeLayer(BOGGED_OUTER_LAYER));
    }

    public void render(
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource bufferSource,
            int packedLight,
            @NotNull Bogged livingEntity,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        coloredCutoutModelCopyLayerRender(
                this.getParentModel(),
                this.layerModel,
                BOGGED_OUTER_LAYER_LOCATION,
                poseStack,
                bufferSource,
                packedLight,
                livingEntity,
                limbSwing,
                limbSwingAmount,
                ageInTicks,
                netHeadYaw,
                headPitch,
                partialTick,
                1,
                1,
                1
        );
    }
}
