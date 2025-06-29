package com.maxwell.fear.curse;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class VaporizeTask {
    private final ServerLevel level;
    private final List<BlockPos> targets;
    private final int perTick;
    private int index = 0;

    public VaporizeTask(ServerLevel level, List<BlockPos> targets, int perTick) {
        this.level = level;
        this.targets = targets;
        this.perTick = perTick;
    }

    public void start() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        for (int i = 0; i < perTick && index < targets.size(); i++) {
            BlockPos pos = targets.get(index++);
            if (level.isLoaded(pos) && !level.getBlockState(pos).isAir()) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                level.sendParticles(ParticleTypes.CLOUD, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        2, 0.05, 0.05, 0.05, 0.01);
            }
        }

        if (index >= targets.size()) {
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }
}
