package io.github.itskilerluc.familiarfaces.server.util;

import net.minecraft.world.entity.AnimationState;

public class Util {
    public static void fastForward(AnimationState state, int duration, float speed) {
        if (state.isStarted()) {
            state.accumulatedTime += (long) ((float) (duration * 1000) * speed) / 20L;
        }
    }
}
