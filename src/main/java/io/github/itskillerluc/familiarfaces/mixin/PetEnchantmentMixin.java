package io.github.itskillerluc.familiarfaces.mixin;

import com.github.alexthe668.domesticationinnovation.server.enchantment.PetEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Arrays;

@Mixin(PetEnchantment.class)
public abstract class PetEnchantmentMixin extends Enchantment {
    protected PetEnchantmentMixin(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;<init>(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;Lnet/minecraft/world/item/enchantment/EnchantmentCategory;[Lnet/minecraft/world/entity/EquipmentSlot;)V"), index = 2)
    private static EquipmentSlot[] familiar_faces$modifyEnchantmentRarity(EquipmentSlot[] pApplicableSlots) {
        if (Arrays.stream(pApplicableSlots).anyMatch(slot -> slot == EquipmentSlot.CHEST)) return pApplicableSlots;
        EquipmentSlot[] newSlots = new EquipmentSlot[pApplicableSlots.length + 1];
        System.arraycopy(pApplicableSlots, 0, newSlots, 0, pApplicableSlots.length);
        newSlots[pApplicableSlots.length] = EquipmentSlot.CHEST;
        return newSlots;
    }
}
