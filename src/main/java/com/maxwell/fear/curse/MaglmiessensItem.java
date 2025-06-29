package com.maxwell.fear.curse;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MaglmiessensItem extends Item {
    public MaglmiessensItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            new AccelerateTask((ServerLevel) level, player).start();
            player.getCooldowns().addCooldown(this, 10); // クールダウン10秒
        }

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    // 内部クラス：加速タスク
    private static class AccelerateTask {
        private final ServerLevel level;
        private final ServerPlayer player;
        private int ticks = 0;
        private final int maxTicks = 400; // 10秒（1秒=20tick）

        public AccelerateTask(ServerLevel level, Player player) {
            this.level = level;
            this.player = (ServerPlayer) player;
        }

        public void start() {
            MinecraftForge.EVENT_BUS.register(this);
        }

        @SubscribeEvent
        public void onServerTick(TickEvent.ServerTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;
            if (ticks++ >= maxTicks) {
                MinecraftForge.EVENT_BUS.unregister(this);
                return;
            }

            // 加速ベクトルの演出（段階的）
            Vec3 look = player.getLookAngle();
            double speed = 4 + 3 * ticks;

            player.setDeltaMovement(look.scale(speed));
            player.hurtMarked = true;
            destroyFrontBlockBox();
            // パーティクル演出
            level.sendParticles(ParticleTypes.END_ROD,
                    player.getX(), player.getY() + 1.2, player.getZ(),
                    2, 0.1, 0.1, 0.1, 0.01);

            // 任意演出：プレイヤーが浮かないようY速度を固定するなら以下を加える
            // Vec3 motion = new Vec3(look.x * speed, 0, look.z * speed);
            // player.setDeltaMovement(motion);
        }
        private void destroyFrontBlockBox() {
            Vec3 look = player.getLookAngle().normalize();
            BlockPos center = player.blockPosition().offset(
                    (int)(look.x * 2.5), (int)(look.y * 2.5), (int)(look.z * 2.5)
            );

            int halfSize = 2; // ⇒ 直径4ブロック

            for (int dx = -halfSize; dx < halfSize; dx++) {
                for (int dy = -halfSize; dy < halfSize; dy++) {
                    for (int dz = -halfSize; dz < halfSize; dz++) {
                        BlockPos target = center.offset(dx, dy, dz);
                        if (!level.getBlockState(target).isAir() &&
                                level.getBlockState(target).getDestroySpeed(level, target) >= 0 &&
                                !level.getBlockState(target).is(Blocks.BEDROCK)) {

                            level.setBlock(target, Blocks.AIR.defaultBlockState(), 3);
                            level.playSound(null, target, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 0.2f, 1.0f);
                            level.sendParticles(ParticleTypes.CRIT,
                                    target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5,
                                    3, 0.1, 0.1, 0.1, 0.01);
                        }
                    }
                }
            }
        }
    }


}
