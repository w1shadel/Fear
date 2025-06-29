package com.maxwell.fear.curse;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class Terminus extends Item {
    public Terminus(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.fear.terminus.desc1").withStyle(ChatFormatting.RED));
        tooltip.add(Component.translatable("item.fear.terminus.desc2").withStyle(ChatFormatting.RED));
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }
}
