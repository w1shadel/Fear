package com.maxwell.fear.curse;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Curse_of_sheildItem extends Item {

    public Curse_of_sheildItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) level;
            BlockPos origin = player.blockPosition();
            double radius = 100.0;

            Map<UUID, Vec3> entityPositionMap = new HashMap<>();

            for (LivingEntity entity : serverLevel.getEntitiesOfClass(LivingEntity.class,
                    new AABB(origin).inflate(radius))) {
                if (!entity.getUUID().equals(player.getUUID())) {
                    entityPositionMap.put(entity.getUUID(), entity.position());
                }
            }

            new PositionLockTask(serverLevel, entityPositionMap, 20).start(); // 20秒
            player.displayClientMessage(Component.literal("§0呪いによって敵は押し止められた"), true);
            player.getCooldowns().addCooldown(this, 400);
        }

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}

