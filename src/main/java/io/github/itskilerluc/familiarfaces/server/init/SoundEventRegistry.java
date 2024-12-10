package io.github.itskilerluc.familiarfaces.server.init;

import io.github.itskilerluc.familiarfaces.FamiliarFaces;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundEventRegistry {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, FamiliarFaces.MODID);

    public static final RegistryObject<SoundEvent> BOGGED_AMBIENT = SOUND_EVENTS.register("entity.bogged.ambient",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.bogged.ambient")));

    public static final RegistryObject<SoundEvent> BOGGED_DEATH = SOUND_EVENTS.register("entity.bogged.death",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.bogged.death")));

    public static final RegistryObject<SoundEvent> BOGGED_HURT = SOUND_EVENTS.register("entity.bogged.hurt",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.bogged.hurt")));

    public static final RegistryObject<SoundEvent> BOGGED_STEP = SOUND_EVENTS.register("entity.bogged.step",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.bogged.step")));

    public static final RegistryObject<SoundEvent> BREEZE_CHARGE = SOUND_EVENTS.register("entity.breeze.charge",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.breeze.charge")));
    public static final RegistryObject<SoundEvent> BREEZE_DEFLECT = SOUND_EVENTS.register("entity.breeze.deflect",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.breeze.deflect")));
    public static final RegistryObject<SoundEvent> BREEZE_INHALE = SOUND_EVENTS.register("entity.breeze.inhale",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.breeze.inhale")));
    public static final RegistryObject<SoundEvent> BREEZE_IDLE_GROUND = SOUND_EVENTS.register("entity.breeze.idle_ground",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.breeze.idle_ground")));
    public static final RegistryObject<SoundEvent> BREEZE_IDLE_AIR = SOUND_EVENTS.register("entity.breeze.idle_air",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.breeze.idle_air")));
    public static final RegistryObject<SoundEvent> BREEZE_SHOOT = SOUND_EVENTS.register("entity.breeze.shoot",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.breeze.shoot")));
    public static final RegistryObject<SoundEvent> BREEZE_JUMP = SOUND_EVENTS.register("entity.breeze.jump",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.breeze.jump")));
    public static final RegistryObject<SoundEvent> BREEZE_LAND = SOUND_EVENTS.register("entity.breeze.land",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.breeze.land")));
    public static final RegistryObject<SoundEvent> BREEZE_SLIDE = SOUND_EVENTS.register("entity.breeze.slide",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.breeze.slide")));
    public static final RegistryObject<SoundEvent> BREEZE_DEATH = SOUND_EVENTS.register("entity.breeze.death",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.breeze.death")));
    public static final RegistryObject<SoundEvent> BREEZE_HURT = SOUND_EVENTS.register("entity.breeze.hurt",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.breeze.hurt")));
    public static final RegistryObject<SoundEvent> BREEZE_WHIRL = SOUND_EVENTS.register("entity.breeze.whirl",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.breeze.whirl")));
    public static final RegistryObject<SoundEvent> BREEZE_WIND_CHARGE_BURST = SOUND_EVENTS.register("entity.breeze.wind_charge_burst",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.breeze.wind_charge_burst")));
    public static final RegistryObject<SoundEvent> WIND_CHARGE_THROW = SOUND_EVENTS.register("entity.wind_charge.throw",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.wind_charge.throw")));
    public static final RegistryObject<SoundEvent> WIND_CHARGE_BURST = SOUND_EVENTS.register("entity.wind_charge.burst",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.wind_charge.burst")));
}
