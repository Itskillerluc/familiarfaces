package io.github.itskillerluc.familiarfaces.server.capability;

import io.github.itskillerluc.familiarfaces.FamiliarFaces;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WolfArmorCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final ResourceLocation IDENTIFIER = new ResourceLocation(FamiliarFaces.MODID, "wolf_armor");
    public static Capability<IWolfArmorCapability> WOLF_ARMOR_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    private IWolfArmorCapability capability = null;
    private final LazyOptional<IWolfArmorCapability> optional = LazyOptional.of(this::createWolfArmor);

    private IWolfArmorCapability createWolfArmor() {
        if (capability == null) {
            capability = new WolfArmorCapability();
        }
        return capability;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == WOLF_ARMOR_CAPABILITY) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        var tag = new CompoundTag();
        var item = new CompoundTag();
        createWolfArmor().getBodyArmorItem().save(item);
        tag.put("item", item);
        tag.putFloat("drop_chance", createWolfArmor().getBodyArmorDropChance());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createWolfArmor().setBodyArmorItem(ItemStack.of(nbt.getCompound("item")));
        createWolfArmor().setBodyArmorDropChance(nbt.getFloat("drop_chance"));
    }
}
