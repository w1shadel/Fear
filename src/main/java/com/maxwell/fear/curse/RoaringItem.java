package com.maxwell.fear.curse;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoaringItem extends Item {
    public RoaringItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) level;
            BlockPos center = player.blockPosition();
            int radius = 100;

            List<BlockPos> vaporTargets = new ArrayList<>();

            for (BlockPos pos : BlockPos.betweenClosed(center.offset(-radius, -radius, -radius),
                    center.offset(radius, radius, radius))) {
                BlockEntity be = serverLevel.getBlockEntity(pos);
                if (be instanceof Container) {
                    // 💥 インベントリ付きブロックは即削除
                    serverLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    serverLevel.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            5, 0.1, 0.1, 0.1, 0.01);
                    level.playSound(null, pos, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 2.0f, 19f);
                } else if (!serverLevel.getBlockState(pos).isAir()) {
                    vaporTargets.add(pos); // 後で徐々に削除

                }
            }

            Collections.shuffle(vaporTargets); // 演出効果
            new VaporizeTask(serverLevel, vaporTargets, 30).start(); // 1tickあたり30ブロックずつ消す

            player.displayClientMessage(Component.literal("§0咆哮"), true);
            player.getCooldowns().addCooldown(this, 40);

        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
