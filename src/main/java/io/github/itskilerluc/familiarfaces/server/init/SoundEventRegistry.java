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
    public static final RegistryObject<SoundEvent> BREEZE_WIND_CHARGE_BURST = SOUND_EVENTS.register("entity.breeze.wind_burst",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.breeze.wind_burst")));
    public static final RegistryObject<SoundEvent> WIND_CHARGE_THROW = SOUND_EVENTS.register("entity.wind_charge.throw",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.wind_charge.throw")));
    public static final RegistryObject<SoundEvent> WIND_CHARGE_BURST = SOUND_EVENTS.register("entity.wind_charge.wind_burst",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.wind_charge.wind_burst")));

    public static final RegistryObject<SoundEvent> ARMADILLO_AMBIENT = SOUND_EVENTS.register("entity.armadillo.ambient",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.armadillo.ambient")));
    public static final RegistryObject<SoundEvent> ARMADILLO_BRUSH = SOUND_EVENTS.register("entity.armadillo.brush",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.armadillo.brush")));
    public static final RegistryObject<SoundEvent> ARMADILLO_DEATH = SOUND_EVENTS.register("entity.armadillo.death",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.armadillo.death")));
    public static final RegistryObject<SoundEvent> ARMADILLO_EAT = SOUND_EVENTS.register("entity.armadillo.eat",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.armadillo.eat")));
    public static final RegistryObject<SoundEvent> ARMADILLO_HURT = SOUND_EVENTS.register("entity.armadillo.hurt",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.armadillo.hurt")));
    public static final RegistryObject<SoundEvent> ARMADILLO_HURT_REDUCED = SOUND_EVENTS.register("entity.armadillo.hurt_reduced",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.armadillo.hurt_reduced")));
    public static final RegistryObject<SoundEvent> ARMADILLO_LAND = SOUND_EVENTS.register("entity.armadillo.land",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.armadillo.land")));
    public static final RegistryObject<SoundEvent> ARMADILLO_PEEK = SOUND_EVENTS.register("entity.armadillo.peek",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.armadillo.peek")));
    public static final RegistryObject<SoundEvent> ARMADILLO_ROLL = SOUND_EVENTS.register("entity.armadillo.roll",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.armadillo.roll")));
    public static final RegistryObject<SoundEvent> ARMADILLO_SCUTE_DROP = SOUND_EVENTS.register("entity.armadillo.scute_drop",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.armadillo.scute_drop")));
    public static final RegistryObject<SoundEvent> ARMADILLO_STEP = SOUND_EVENTS.register("entity.armadillo.step",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.armadillo.step")));
    public static final RegistryObject<SoundEvent> ARMADILLO_UNROLL_FINISH = SOUND_EVENTS.register("entity.armadillo.unroll_finish",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.armadillo.unroll_finish")));
    public static final RegistryObject<SoundEvent> ARMADILLO_UNROLL_START = SOUND_EVENTS.register("entity.armadillo.unroll_start",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "entity.armadillo.unroll_start")));
    public static final RegistryObject<SoundEvent> WOLF_ARMOR_BREAK = SOUND_EVENTS.register("item.wolf_armor.break",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "item.wolf_armor.break")));
    public static final RegistryObject<SoundEvent> WOLF_ARMOR_CRACK = SOUND_EVENTS.register("item.wolf_armor.crack",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "item.wolf_armor.crack")));
    public static final RegistryObject<SoundEvent> WOLF_ARMOR_DAMAGE = SOUND_EVENTS.register("item.wolf_armor.damage",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "item.wolf_armor.damage")));
    public static final RegistryObject<SoundEvent> WOLF_ARMOR_REPAIR = SOUND_EVENTS.register("item.wolf_armor.repair",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "item.wolf_armor.repair")));
    public static final RegistryObject<SoundEvent> WOLF_ARMOR_EQUIP = SOUND_EVENTS.register("item.armor.equip_wolf",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "item.armor.equip_wolf")));
    public static final RegistryObject<SoundEvent> WOLF_ARMOR_UNEQUIP = SOUND_EVENTS.register("item.armor.unequip_wolf",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FamiliarFaces.MODID, "item.armor.unequip_wolf")));
}
