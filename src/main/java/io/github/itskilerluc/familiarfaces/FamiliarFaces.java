package io.github.itskilerluc.familiarfaces;

import io.github.itskilerluc.familiarfaces.server.init.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(FamiliarFaces.MODID)
public class FamiliarFaces {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "familiar_faces";

    public FamiliarFaces(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        SoundEventRegistry.SOUND_EVENTS.register(modEventBus);
        EntityTypeRegistry.ENTITY_TYPES.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        MemoryModuleTypeRegistry.MEMORY_MODULE_TYPES.register(modEventBus);
        SensorTypeRegistry.SENSOR_TYPES.register(modEventBus);
    }
}
