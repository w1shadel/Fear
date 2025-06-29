package com.maxwell.fear.curse;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;
import java.util.UUID;

public class PositionLockTask {
    private final ServerLevel level;
    private final Map<UUID, Vec3> targets;
    private final int durationTicks;
    private int elapsed = 0;
    private final int interval = 5; // 5tickごとにワープ

    public PositionLockTask(ServerLevel level, Map<UUID, Vec3> targets, int durationSeconds) {
        this.level = level;
        this.targets = targets;
        this.durationTicks = durationSeconds * 20;
    }

    public void start() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (++elapsed >= durationTicks) {
            MinecraftForge.EVENT_BUS.unregister(this);
            return;
        }

        if (elapsed % interval == 0) {
            for (Map.Entry<UUID, Vec3> entry : targets.entrySet()) {
                Entity entity = level.getEntity(entry.getKey());
                if (entity instanceof LivingEntity living) {
                    Vec3 pos = entry.getValue();

                    // 📍 位置を固定
                    entity.teleportTo(pos.x, pos.y, pos.z);

                    // 🧊 鈍足効果（レベル2、5秒持続）
                    living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1, true, false));

                    // 💨 パーティクル
                    level.sendParticles(ParticleTypes.SOUL,
                            pos.x + 0.5, pos.y + 1, pos.z + 0.5,
                            5, 0.2, 0.2, 0.2, 0.01);

                    // 🐉 エンダードラゴンの咆哮（最初の1回）
                    if (elapsed == interval) {
                        level.playSound(null, BlockPos.containing(pos), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 2.0f, 0.9f);
                    }
                }
            }
        }
    }

}
