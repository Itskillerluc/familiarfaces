package io.github.itskillerluc.familiarfaces.server.init;

import io.github.itskillerluc.familiarfaces.FamiliarFaces;
import io.github.itskillerluc.familiarfaces.server.entities.Armadillo;
import io.github.itskillerluc.familiarfaces.server.entities.ai.ArmadilloAi;
import io.github.itskillerluc.familiarfaces.server.entities.ai.BreezeAttackEntitySensor;
import io.github.itskillerluc.familiarfaces.server.entities.ai.MobSensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SensorTypeRegistry {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, FamiliarFaces.MODID);

    public static final RegistryObject<SensorType<BreezeAttackEntitySensor>> BREEZE_ATTACK_ENTITY_SENSOR = SENSOR_TYPES.register("breeze_attack_entity_sensor",
            () -> new SensorType<>(BreezeAttackEntitySensor::new));

    public static final RegistryObject<SensorType<TemptingSensor>> ARMADILLO_TEMPTATIONS = SENSOR_TYPES.register("armadillo_temptations",
            () -> new SensorType<>(() -> new TemptingSensor(ArmadilloAi.getTemptations())));

    public static final RegistryObject<SensorType<MobSensor<Armadillo>>> ARMADILLO_SCARE_DETECTED = SENSOR_TYPES.register("armadillo_scare_detected",
            () -> new SensorType<>(() -> new MobSensor<>(20, Armadillo::isScaredBy, Armadillo::canStayRolledUp, MemoryModuleTypeRegistry.DANGER_DETECTED_RECENTLY.get(), 80)));
}
