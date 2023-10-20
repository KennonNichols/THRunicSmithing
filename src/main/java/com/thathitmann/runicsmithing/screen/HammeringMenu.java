package com.thathitmann.runicsmithing.screen;

import com.thathitmann.runicsmithing.item.custom.supers.ForgeHammer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class HammeringMenu extends AbstractContainerMenu {
    private final Level level;


    public HammeringMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (ForgeHammer) inventory.player.getOffhandItem().getItem(),new SimpleContainerData(1));
    }

    public HammeringMenu(int id, Inventory inventory, ForgeHammer entity, ContainerData data){
        super(ModMenuTypes.HAMMERING_MENU.get(), id);
        checkContainerSize(inventory, 2);
        this.level = inventory.player.level;

        //Create player slots
        //addPlayerInventory(inventory);
        //addPlayerHotbar(inventory);
        //Create slots


        /*
        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, 0, 42, 15));
            this.addSlot(new SlotItemHandler(handler, 1, 116, 15));
        });*/


    }


    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public final boolean stillValid(Player pPlayer) {
        return true;
    }








}
