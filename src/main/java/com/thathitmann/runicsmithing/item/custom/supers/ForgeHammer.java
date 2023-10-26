package com.thathitmann.runicsmithing.item.custom.supers;

import com.thathitmann.runicsmithing.screen.HammeringMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ForgeHammer extends THRSItemBase implements MenuProvider {

    protected boolean advancedMode;

    public boolean isAdvancedMode() {return advancedMode;}

    public ForgeHammer(Properties properties) {
        super(properties);
    }




    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Forging");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new HammeringMenu(pContainerId, pPlayerInventory, this, 4);
    }
}