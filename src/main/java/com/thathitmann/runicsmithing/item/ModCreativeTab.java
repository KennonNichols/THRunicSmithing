package com.thathitmann.runicsmithing.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ModCreativeTab {
    public static final CreativeModeTab TAB = new CreativeModeTab("runicsmithingtab") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ModItems.TONGS.get());
        }
    };

}
