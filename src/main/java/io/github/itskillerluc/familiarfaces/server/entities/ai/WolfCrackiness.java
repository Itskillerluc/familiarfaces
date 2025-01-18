package io.github.itskillerluc.familiarfaces.server.entities.ai;

import net.minecraft.world.item.ItemStack;

public class WolfCrackiness {
    public static final WolfCrackiness WOLF_ARMOR = new WolfCrackiness(0.95F, 0.69F, 0.32F);
    private final float fractionLow;
    private final float fractionMedium;
    private final float fractionHigh;

    private WolfCrackiness(float fractionLow, float fractionMedium, float fractionHigh) {
        this.fractionLow = fractionLow;
        this.fractionMedium = fractionMedium;
        this.fractionHigh = fractionHigh;
    }

    public WolfCrackiness.Level byFraction(float fraction) {
        if (fraction < this.fractionHigh) {
            return WolfCrackiness.Level.HIGH;
        } else if (fraction < this.fractionMedium) {
            return WolfCrackiness.Level.MEDIUM;
        } else {
            return fraction < this.fractionLow ? WolfCrackiness.Level.LOW : WolfCrackiness.Level.NONE;
        }
    }

    public WolfCrackiness.Level byDamage(ItemStack stack) {
        return !stack.isDamageableItem() ? WolfCrackiness.Level.NONE : this.byDamage(stack.getDamageValue(), stack.getMaxDamage());
    }

    public WolfCrackiness.Level byDamage(int damage, int durability) {
        return this.byFraction((float)(durability - damage) / (float)durability);
    }

    public static enum Level {
        NONE,
        LOW,
        MEDIUM,
        HIGH;
    }
}
