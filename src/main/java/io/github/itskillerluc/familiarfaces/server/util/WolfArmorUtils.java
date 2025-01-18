package io.github.itskillerluc.familiarfaces.server.util;

import io.github.itskillerluc.familiarfaces.server.capability.WolfArmorCapabilityProvider;
import io.github.itskillerluc.familiarfaces.server.init.ItemRegistry;
import io.github.itskillerluc.familiarfaces.server.init.Tags;
import io.github.itskillerluc.familiarfaces.server.networking.FamiliarFacesNetwork;
import io.github.itskillerluc.familiarfaces.server.networking.SyncWolfArmorPacket;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

public class WolfArmorUtils {
    public static boolean canArmorAbsorb(DamageSource damageSource, Wolf wolf) {
        return hasArmor(wolf) && !damageSource.is(Tags.DamageTypes.BYPASSES_WOLF_ARMOR);
    }

    public static boolean hasArmor(Wolf wolf) {
        return getBodyArmorItem(wolf).getItem() == ItemRegistry.WOLF_ARMOR.get();
    }

    public static ItemStack getBodyArmorItem(Wolf wolf) {
        if (wolf.getCapability(WolfArmorCapabilityProvider.WOLF_ARMOR_CAPABILITY).isPresent()) {
            return wolf.getCapability(WolfArmorCapabilityProvider.WOLF_ARMOR_CAPABILITY).orElseThrow(NullPointerException::new).getBodyArmorItem();
        }
        return ItemStack.EMPTY;
    }

    public static float getBodyArmorDropChance(Wolf wolf) {
        if (wolf.getCapability(WolfArmorCapabilityProvider.WOLF_ARMOR_CAPABILITY).isPresent()) {
            return wolf.getCapability(WolfArmorCapabilityProvider.WOLF_ARMOR_CAPABILITY).orElseThrow(NullPointerException::new).getBodyArmorDropChance();
        }
        return 0;
    }

    public static void setBodyArmorItem(Wolf wolf, ItemStack stack) {
        wolf.getCapability(WolfArmorCapabilityProvider.WOLF_ARMOR_CAPABILITY).ifPresent(capability -> {
            capability.setBodyArmorItem(stack);
            if (!stack.isEmpty()) {
                capability.setBodyArmorDropChance(2f);
            } else {
                capability.setBodyArmorDropChance(0f);
            }
            if (!wolf.level().isClientSide) {
                FamiliarFacesNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> wolf), new SyncWolfArmorPacket(capability.getBodyArmorItem(), capability.getBodyArmorDropChance(), wolf));
            }
        });
    }

    public static MeshDefinition createMeshDefinition(CubeDeformation cubeDeformation) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        float f = 13.5F;
        PartDefinition partdefinition1 = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-1.0F, 13.5F, -7.0F));
        partdefinition1.addOrReplaceChild(
                "real_head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2.0F, -3.0F, -2.0F, 6.0F, 6.0F, 4.0F, cubeDeformation)
                        .texOffs(16, 14)
                        .addBox(-2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, cubeDeformation)
                        .texOffs(16, 14)
                        .addBox(2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, cubeDeformation)
                        .texOffs(0, 10)
                        .addBox(-0.5F, -0.001F, -5.0F, 3.0F, 3.0F, 4.0F, cubeDeformation),
                PartPose.ZERO
        );
        partdefinition.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(18, 14).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F, cubeDeformation),
                PartPose.offsetAndRotation(0.0F, 14.0F, 2.0F, (float) (Math.PI / 2), 0.0F, 0.0F)
        );
        partdefinition.addOrReplaceChild(
                "upper_body",
                CubeListBuilder.create().texOffs(21, 0).addBox(-3.0F, -3.0F, -3.0F, 8.0F, 6.0F, 7.0F, cubeDeformation),
                PartPose.offsetAndRotation(-1.0F, 14.0F, -3.0F, (float) (Math.PI / 2), 0.0F, 0.0F)
        );
        CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, cubeDeformation);
        partdefinition.addOrReplaceChild("right_hind_leg", cubelistbuilder, PartPose.offset(-2.5F, 16.0F, 7.0F));
        partdefinition.addOrReplaceChild("left_hind_leg", cubelistbuilder, PartPose.offset(0.5F, 16.0F, 7.0F));
        partdefinition.addOrReplaceChild("right_front_leg", cubelistbuilder, PartPose.offset(-2.5F, 16.0F, -4.0F));
        partdefinition.addOrReplaceChild("left_front_leg", cubelistbuilder, PartPose.offset(0.5F, 16.0F, -4.0F));
        PartDefinition partdefinition2 = partdefinition.addOrReplaceChild(
                "tail", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 12.0F, 8.0F, (float) (Math.PI / 5), 0.0F, 0.0F)
        );
        partdefinition2.addOrReplaceChild(
                "real_tail", CubeListBuilder.create().texOffs(9, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, cubeDeformation), PartPose.ZERO
        );
        return meshdefinition;
    }
}
