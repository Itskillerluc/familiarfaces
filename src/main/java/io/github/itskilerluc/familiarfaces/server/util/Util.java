package io.github.itskilerluc.familiarfaces.server.util;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class Util {
    public static void fastForward(AnimationState state, int duration, float speed) {
        if (state.isStarted()) {
            state.accumulatedTime += (long) ((float) (duration * 1000) * speed) / 20L;
        }
    }

    public static void consume(int amount, @Nullable Player entity, ItemStack stack) {
        if (entity == null || !entity.isCreative()) {
            stack.shrink(amount);
        }
    }
}
