package io.github.itskilerluc.familiarfaces.client.events;

import io.github.itskilerluc.familiarfaces.FamiliarFaces;
import io.github.itskilerluc.familiarfaces.client.models.entity.BoggedModel;
import io.github.itskilerluc.familiarfaces.client.renderers.entity.BoggedClothingLayer;
import io.github.itskilerluc.familiarfaces.client.renderers.entity.BoggedRenderer;
import io.github.itskilerluc.familiarfaces.server.init.EntityTypeRegistry;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FamiliarFaces.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEvents {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityTypeRegistry.BOGGED.get(), BoggedRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BoggedModel.BOGGED, BoggedModel::createBodyLayer);
        event.registerLayerDefinition(BoggedModel.BOGGED_INNER_ARMOR,
                () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
        event.registerLayerDefinition(BoggedModel.BOGGED_OUTER_ARMOR,
                () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
        event.registerLayerDefinition(BoggedClothingLayer.BOGGED_OUTER_LAYER,
                () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.2f), 0), 64, 32));
    }
}
