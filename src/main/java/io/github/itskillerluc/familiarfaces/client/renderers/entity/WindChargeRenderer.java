package io.github.itskillerluc.familiarfaces.client.renderers.entity;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.itskillerluc.familiarfaces.FamiliarFaces;
import io.github.itskillerluc.familiarfaces.client.models.entity.WindChargeModel;
import io.github.itskillerluc.familiarfaces.server.entities.AbstractWindCharge;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class WindChargeRenderer extends EntityRenderer<AbstractWindCharge> {
    private static final float MIN_CAMERA_DISTANCE_SQUARED = Mth.square(3.5F);
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(FamiliarFaces.MODID, "textures/entity/projectiles/wind_charge.png");
    private final WindChargeModel model;

    public WindChargeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new WindChargeModel(context.bakeLayer(WindChargeModel.WIND_CHARGE));
    }

    public void render(AbstractWindCharge entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) < (double)MIN_CAMERA_DISTANCE_SQUARED)) {
            float f = (float)entity.tickCount + partialTick;
            VertexConsumer vertexconsumer = bufferSource.getBuffer(breezeWind(TEXTURE_LOCATION, this.xOffset(f) % 1.0F, 0.0F));
            this.model.setupAnim(entity, 0.0F, 0.0F, f, 0.0F, 0.0F);
            this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        }
    }

    protected float xOffset(float tickCount) {
        return tickCount * 0.03F;
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(AbstractWindCharge entity) {
        return TEXTURE_LOCATION;
    }

    public static RenderType breezeWind(ResourceLocation location, float u, float v) {
        return new RenderType.CompositeRenderType("breeze_wind", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, false, true,
                RenderType.CompositeState.builder()
                        .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(location, false, false))
                        .setTexturingState(new RenderStateShard.OffsetTexturingStateShard(u, v))
                        .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                        .setCullState(RenderStateShard.NO_CULL)
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .setOverlayState(RenderStateShard.NO_OVERLAY)
                        .createCompositeState(false));
    }

    public static ShaderInstance breezeShaderInstance;

    public static final RenderStateShard.ShaderStateShard RENDERTYPE_BREEZE_WIND_SHADER = new RenderStateShard.ShaderStateShard(
            () -> breezeShaderInstance
    );
}
