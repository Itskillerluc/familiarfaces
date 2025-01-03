package io.github.itskilerluc.familiarfaces.server.capability;

import net.minecraft.world.item.ItemStack;

public class WolfArmorCapability implements IWolfArmorCapability {
    private ItemStack bodyArmorItem = ItemStack.EMPTY;
    private float bodyArmorDropChance = 0.085f;

    @Override
    public ItemStack getBodyArmorItem() {
        return bodyArmorItem;
    }

    @Override
    public void setBodyArmorItem(ItemStack itemStack) {
        this.bodyArmorItem = itemStack;
    }

    @Override
    public float getBodyArmorDropChance() {
        return bodyArmorDropChance;
    }

    @Override
    public void setBodyArmorDropChance(float dropChance) {
        this.bodyArmorDropChance = dropChance;
    }
}
