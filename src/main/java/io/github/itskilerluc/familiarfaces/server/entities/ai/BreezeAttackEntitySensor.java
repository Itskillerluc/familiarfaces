package io.github.itskilerluc.familiarfaces.server.entities.ai;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import io.github.itskilerluc.familiarfaces.server.entities.Breeze;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class BreezeAttackEntitySensor extends NearestLivingEntitySensor<Breeze> {
    public static final int BREEZE_SENSOR_RADIUS = 24;

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.copyOf(Iterables.concat(super.requires(), List.of(MemoryModuleType.NEAREST_ATTACKABLE)));
    }

    protected void doTick(@NotNull ServerLevel level, @NotNull Breeze breeze) {
        super.doTick(level, breeze);
        breeze.getBrain()
                .getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES)
                .stream()
                .flatMap(Collection::stream)
                .filter(EntitySelector.NO_CREATIVE_OR_SPECTATOR)
                .filter(p_312759_ -> Sensor.isEntityAttackable(breeze, p_312759_))
                .findFirst()
                .ifPresentOrElse(
                        p_312872_ -> breeze.getBrain().setMemory(MemoryModuleType.NEAREST_ATTACKABLE, p_312872_),
                        () -> breeze.getBrain().eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE)
                );
    }

    @Override
    protected int radiusXZ() {
        return BREEZE_SENSOR_RADIUS;
    }

    @Override
    protected int radiusY() {
        return BREEZE_SENSOR_RADIUS;
    }
}
