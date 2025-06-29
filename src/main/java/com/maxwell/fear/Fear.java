package com.maxwell.fear;

import com.maxwell.fear.register.Moditem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Fear.MODID)
public class Fear
{
    public static final String MODID = "fear";
    public Fear(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        Moditem.ITEMS.register(modEventBus);
    }
}
