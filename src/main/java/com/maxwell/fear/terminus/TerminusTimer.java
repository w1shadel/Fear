package com.maxwell.fear.terminus;

import com.maxwell.fear.register.Moditem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = "fear")
public class TerminusTimer {

    private static final Map<UUID, Integer> timeTracker = new HashMap<>();
    private static final ResourceKey<Level> TARGET_DIMENSION = Level.OVERWORLD; // 例：オーバーワールド対象
    private static final int TICKS_REQUIRED = 20 * 60 * 60 * 24; // 24時間分のtick数

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        MinecraftServer server = event.getServer();
        if (server == null) return;

        ServerLevel world = server.getLevel(TARGET_DIMENSION);
        if (world == null) return;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (!player.level().dimension().equals(TARGET_DIMENSION)) continue;

            UUID uuid = player.getUUID();
            int current = timeTracker.getOrDefault(uuid, 0) + 1;

            if (current == TICKS_REQUIRED) {
                ItemStack diamond = new ItemStack(Moditem.TERMINUS.get());
                player.getInventory().add(diamond);
                player.sendSystemMessage(Component.literal("§4終焉の遺物は貴方に興味を示した..."));
            }

            if (current <= TICKS_REQUIRED) {
                timeTracker.put(uuid, current);
            }
        }
    }
}

