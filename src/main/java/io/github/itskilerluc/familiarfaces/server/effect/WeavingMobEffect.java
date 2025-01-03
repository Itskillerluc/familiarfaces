package io.github.itskilerluc.familiarfaces.server.effect;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.Set;
import java.util.function.ToIntFunction;

public class WeavingMobEffect extends MobEffect {
    private final ToIntFunction<RandomSource> maxCobwebs;

    public WeavingMobEffect(MobEffectCategory category, int color, ToIntFunction<RandomSource> maxCobwebs) {
        super(category, color);
        this.maxCobwebs = maxCobwebs;
    }

    public void onMobRemoved(LivingEntity livingEntity, int amplifier, Entity.RemovalReason reason) {
        if (reason == Entity.RemovalReason.KILLED
                && (livingEntity instanceof Player || livingEntity.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING))) {
            spawnCobwebsRandomlyAround(livingEntity.level(), livingEntity.getRandom(), livingEntity.getOnPos());
        }
    }

    private void spawnCobwebsRandomlyAround(Level level, RandomSource random, BlockPos pos) {
        Set<BlockPos> set = Sets.newHashSet();
        int i = this.maxCobwebs.applyAsInt(random);

        for (BlockPos blockpos : BlockPos.randomInCube(random, 15, pos, 1)) {
            BlockPos blockpos1 = blockpos.below();
            if (!set.contains(blockpos)
                    && level.getBlockState(blockpos).canBeReplaced()
                    && level.getBlockState(blockpos1).isFaceSturdy(level, blockpos1, Direction.UP)) {
                set.add(blockpos.immutable());
                if (set.size() >= i) {
                    break;
                }
            }
        }

        for (BlockPos blockpos2 : set) {
            level.setBlock(blockpos2, Blocks.COBWEB.defaultBlockState(), 3);
            level.levelEvent(3018, blockpos2, 0);
        }
    }
}
