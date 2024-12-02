package io.github.itskilerluc.familiarfaces.server.init;

import io.github.itskilerluc.familiarfaces.FamiliarFaces;
import io.github.itskilerluc.familiarfaces.server.entities.ai.BreezeAttackEntitySensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SensorTypeRegistry {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, FamiliarFaces.MODID);

    public static final RegistryObject<SensorType<BreezeAttackEntitySensor>> BREEZE_ATTACK_ENTITY_SENSOR = SENSOR_TYPES.register("breeze_attack_entity_sensor",
            () -> new SensorType<>(BreezeAttackEntitySensor::new));
}
