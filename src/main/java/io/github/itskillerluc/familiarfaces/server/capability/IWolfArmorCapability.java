package io.github.itskillerluc.familiarfaces.server.capability;

import net.minecraft.world.item.ItemStack;

public interface IWolfArmorCapability {
    ItemStack getBodyArmorItem();
    void setBodyArmorItem(ItemStack itemStack);

    float getBodyArmorDropChance();
    void setBodyArmorDropChance(float dropChance);
}
