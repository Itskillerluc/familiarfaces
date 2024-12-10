package io.github.itskilerluc.familiarfaces.client.events;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import io.github.itskilerluc.familiarfaces.FamiliarFaces;
import io.github.itskilerluc.familiarfaces.client.models.entity.BoggedModel;
import io.github.itskilerluc.familiarfaces.client.models.entity.BreezeModel;
import io.github.itskilerluc.familiarfaces.client.models.entity.WindChargeModel;
import io.github.itskilerluc.familiarfaces.client.particle.GustParticle;
import io.github.itskilerluc.familiarfaces.client.particle.GustSeedParticle;
import io.github.itskilerluc.familiarfaces.client.renderers.entity.*;
import io.github.itskilerluc.familiarfaces.server.init.EntityTypeRegistry;
import io.github.itskilerluc.familiarfaces.server.init.ParticleTypeRegistry;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = FamiliarFaces.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEvents {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityTypeRegistry.BOGGED.get(), BoggedRenderer::new);
        event.registerEntityRenderer(EntityTypeRegistry.WIND_CHARGE.get(), WindChargeRenderer::new);
        event.registerEntityRenderer(EntityTypeRegistry.BREEZE_WIND_CHARGE.get(), WindChargeRenderer::new);
        event.registerEntityRenderer(EntityTypeRegistry.BREEZE.get(), BreezeRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BoggedModel.BOGGED, BoggedModel::createBodyLayer);
        event.registerLayerDefinition(BreezeModel.BREEZE, () -> BreezeModel.createBodyLayer(32, 32));
        event.registerLayerDefinition(BreezeWindLayer.BREEZE_WIND, () -> BreezeModel.createBodyLayer(128, 128));
        event.registerLayerDefinition(BoggedModel.BOGGED_INNER_ARMOR,
                () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
        event.registerLayerDefinition(BoggedModel.BOGGED_OUTER_ARMOR,
                () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
        event.registerLayerDefinition(BoggedClothingLayer.BOGGED_OUTER_LAYER,
                () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.2f), 0), 64, 32));
        event.registerLayerDefinition(WindChargeModel.WIND_CHARGE,
                WindChargeModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerShaders(final RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation(FamiliarFaces.MODID, "rendertype_breeze_wind"), DefaultVertexFormat.NEW_ENTITY),
                shaderInstance -> WindChargeRenderer.breezeShaderInstance = shaderInstance);
    }

    @SubscribeEvent
    public static void registerParticleProviders(final RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleTypeRegistry.GUST.get(), GustParticle.Provider::new);
        event.registerSpriteSet(ParticleTypeRegistry.SMALL_GUST.get(), GustParticle.SmallProvider::new);
        event.registerSpecial(ParticleTypeRegistry.GUST_EMITTER_LARGE.get(), new GustSeedParticle.Provider(3.0, 7, 0));
        event.registerSpecial(ParticleTypeRegistry.GUST_EMITTER_SMALL.get(), new GustSeedParticle.Provider(1.0, 3, 2));
    }
}
