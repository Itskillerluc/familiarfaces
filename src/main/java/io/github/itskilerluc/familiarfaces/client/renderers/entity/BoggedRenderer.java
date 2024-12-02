package io.github.itskilerluc.familiarfaces.client.renderers.entity;

import io.github.itskilerluc.familiarfaces.FamiliarFaces;
import io.github.itskilerluc.familiarfaces.client.models.entity.BoggedModel;
import io.github.itskilerluc.familiarfaces.server.entities.Bogged;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BoggedRenderer extends HumanoidMobRenderer<Bogged, BoggedModel> {
    private static final ResourceLocation BOGGED_SKELETON_LOCATION = new ResourceLocation(FamiliarFaces.MODID, "textures/entity/bogged.png");


    public BoggedRenderer(EntityRendererProvider.Context context) {
        super(context, new BoggedModel(context.bakeLayer(BoggedModel.BOGGED)), 0.5f);
        this.addLayer(
                new HumanoidArmorLayer<>(
                        this, new SkeletonModel<>(context.bakeLayer(BoggedModel.BOGGED_INNER_ARMOR)),
                        new SkeletonModel<>(context.bakeLayer(BoggedModel.BOGGED_OUTER_ARMOR)),
                        context.getModelManager()
                )
        );
        this.addLayer(
                new BoggedClothingLayer(this, context.getModelSet())
        );
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Bogged entity) {
        return BOGGED_SKELETON_LOCATION;
    }

    @Override
    protected boolean isShaking(Bogged entity) {
        return entity.isShaking();
    }
}
