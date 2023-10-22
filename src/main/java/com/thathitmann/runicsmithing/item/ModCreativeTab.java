package com.thathitmann.runicsmithing.item;

import com.thathitmann.runicsmithing.RunicSmithing;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class ModCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RunicSmithing.MOD_ID);


    public static RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("runicsmithing_tab", () ->
            CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.TONGS.get()))
                    .title(Component.translatable("creativemodetab:runicsmithing_tab")).build()
    );




    public static void register(IEventBus eventBus) {CREATIVE_MODE_TABS.register(eventBus);}

}
