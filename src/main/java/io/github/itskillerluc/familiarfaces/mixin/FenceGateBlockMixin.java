package io.github.itskillerluc.familiarfaces.mixin;

import io.github.itskillerluc.familiarfaces.server.util.FenceGateBlockMixinHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.FenceGateBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FenceGateBlock.class)
public abstract class FenceGateBlockMixin implements FenceGateBlockMixinHelper {
    @Accessor
    public abstract SoundEvent getOpenSound();

    @Accessor
    public abstract SoundEvent getCloseSound();

    @Override
    public SoundEvent familiar_face$openSound() {
        return getOpenSound();
    }

    @Override
    public SoundEvent familiar_face$closeSound() {
        return getCloseSound();
    }
}
