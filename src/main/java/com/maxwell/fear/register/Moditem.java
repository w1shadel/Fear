package com.maxwell.fear.register;

import com.maxwell.fear.curse.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Moditem {
    public static final DeferredRegister<Item> ITEMS;
    public static final DeferredRegister<CreativeModeTab> TABS;
    public static final RegistryObject<Item> CURSE_SWORD_OF_2B2T;
    public static final RegistryObject<Item> CURSE_STAR;
    public static final RegistryObject<Item> CURSE_OF_BOUNDING;
    public static final RegistryObject<Item> CURSE_OF_ACCELERATION;
    public static final RegistryObject<Item> CURSE_OF_CANTMOVE;
    public static final RegistryObject<Item> CURSE_NULL;
    public static final RegistryObject<Item> NOTHING;
    public static final RegistryObject<Item> TERMINUS;
    static
    {
        TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "fear");
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "fear");
        CURSE_SWORD_OF_2B2T = ITEMS.register("the_end_of_all_valine", () -> new curse(Tiers.DIAMOND,662,662,new Item.Properties()));
        CURSE_STAR = ITEMS.register("curse_star", () -> new star_of_curse(new Item.Properties()));
        CURSE_OF_BOUNDING = ITEMS.register("curse_of_bounding", () -> new RoaringItem(new Item.Properties()));
        CURSE_OF_ACCELERATION = ITEMS.register("curse_of_acceleration", () -> new MaglmiessensItem(new Item.Properties()));
        CURSE_OF_CANTMOVE = ITEMS.register("curse_of_movedisabled", () -> new Curse_of_sheildItem(new Item.Properties()));
        CURSE_NULL = ITEMS.register("curse_of_null", () -> new Item(new Item.Properties()));
        NOTHING = ITEMS.register(".", () -> new Item(new Item.Properties()));
        TERMINUS = ITEMS.register("terminus", () -> new Terminus(new Item.Properties()));
    }
}
