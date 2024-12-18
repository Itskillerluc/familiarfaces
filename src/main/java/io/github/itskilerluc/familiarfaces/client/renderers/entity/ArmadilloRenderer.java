package io.github.itskilerluc.familiarfaces.client.renderers.entity;

import io.github.itskilerluc.familiarfaces.FamiliarFaces;
import io.github.itskilerluc.familiarfaces.client.models.entity.ArmadilloModel;
import io.github.itskilerluc.familiarfaces.server.entities.Armadillo;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ArmadilloRenderer extends MobRenderer<Armadillo, ArmadilloModel> {
    private static final ResourceLocation ARMADILLO_LOCATION = new ResourceLocation(FamiliarFaces.MODID, "textures/entity/armadillo.png");

    public ArmadilloRenderer(EntityRendererProvider.Context p_316729_) {
        super(p_316729_, new ArmadilloModel(p_316729_.bakeLayer(ArmadilloModel.ARMADILLO)), 0.4F);
    }

    public ResourceLocation getTextureLocation(Armadillo p_316224_) {
        return ARMADILLO_LOCATION;
    }
}
