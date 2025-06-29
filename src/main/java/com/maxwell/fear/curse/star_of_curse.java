package com.maxwell.fear.curse;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

public class star_of_curse extends Item {
    public star_of_curse(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) level;

            ServerPlayer nearest = serverLevel.getServer().getPlayerList().getPlayers().stream()
                    .filter(p -> !p.getUUID().equals(player.getUUID())) // 自分自身を除外
                    .min(Comparator.comparingDouble(p -> p.distanceToSqr(player)))
                    .orElse(null);

            if (nearest != null) {
                // ⚡ 雷を落とす
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);
                lightning.moveTo(nearest.getX(), nearest.getY(), nearest.getZ());
                lightning.setVisualOnly(false);
                serverLevel.addFreshEntity(lightning);

                // ✨ 発光エフェクト
                nearest.addEffect(new MobEffectInstance(MobEffects.GLOWING, (int) Long.MAX_VALUE, (int) Long.MAX_VALUE));

                // 🌑 暗闇エフェクト（1.19以降）
                nearest.addEffect(new MobEffectInstance(MobEffects.DARKNESS, (int) Long.MAX_VALUE, (int) Long.MAX_VALUE));

                player.displayClientMessage(Component.literal("⚡ 対象に雷撃を実行しました！"), true);
            } else {
                player.displayClientMessage(Component.literal("📡 他にプレイヤーが見つかりませんでした。"), true);
            }
        }

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.fear.curse_star.desc1").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.translatable("item.fear.curse_star.desc2").withStyle(ChatFormatting.YELLOW));
    }
}
