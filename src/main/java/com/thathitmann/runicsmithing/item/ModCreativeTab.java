package com.thathitmann.runicsmithing.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTab {
    public static final CreativeModeTab TAB = new CreativeModeTab("runicsmithingtab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.TONGS.get());
        }
    };

}
