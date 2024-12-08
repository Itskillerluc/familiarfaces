package io.github.itskilerluc.familiarfaces.server.init;

import com.mojang.serialization.Codec;
import io.github.itskilerluc.familiarfaces.FamiliarFaces;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

public class MemoryModuleTypeRegistry {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, FamiliarFaces.MODID);

    public static final RegistryObject<MemoryModuleType<Unit>> BREEZE_JUMP_COOLDOWN = MEMORY_MODULE_TYPES.register("breeze_jump_cooldown",
            () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<Unit>> BREEZE_SHOOT = MEMORY_MODULE_TYPES.register("breeze_shoot",
            () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<Unit>> BREEZE_SHOOT_CHARGING = MEMORY_MODULE_TYPES.register("breeze_shoot_charging",
            () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<Unit>> BREEZE_SHOOT_RECOVERING = MEMORY_MODULE_TYPES.register("breeze_shoot_recovering",
            () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<Unit>> BREEZE_SHOOT_COOLDOWN = MEMORY_MODULE_TYPES.register("breeze_shoot_cooldown",
            () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<Unit>> BREEZE_JUMP_INHALING = MEMORY_MODULE_TYPES.register("breeze_jump_inhaling",
            () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final RegistryObject<MemoryModuleType<BlockPos>> BREEZE_JUMP_TARGET = MEMORY_MODULE_TYPES.register("breeze_jump_target",
            () -> new MemoryModuleType<>(Optional.of(BlockPos.CODEC)));
    public static final RegistryObject<MemoryModuleType<Unit>> BREEZE_LEAVING_WATER = MEMORY_MODULE_TYPES.register("breeze_leaving_water",
            () -> new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
}
