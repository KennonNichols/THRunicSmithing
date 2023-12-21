package com.thathitmann.runicsmithing.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;


public class ResearchMenu extends AbstractContainerMenu {
    public final Player player;
    public final ItemStack itemStack;
    public ResearchMenu(int id, Inventory inventory, FriendlyByteBuf ignoredExtraData) {
        this(id, inventory);
    }

    public ResearchMenu(int id, @NotNull Inventory inventory){
        super(ModMenuTypes.RESEARCH_TABLET_MENU.get(), id);
        this.player = inventory.player;
        this.itemStack = player.getMainHandItem();
    }


    @SuppressWarnings("DataFlowIssue")
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public final boolean stillValid(@NotNull Player pPlayer) {
        return true;
    }

}
