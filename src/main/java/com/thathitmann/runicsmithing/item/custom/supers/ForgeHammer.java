package com.thathitmann.runicsmithing.item.custom.supers;

import com.thathitmann.runicsmithing.screen.HammeringMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


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









    @Override
    public void inventoryTick(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (!pLevel.isClientSide()) {
            if (queuedReplacements.containsKey(pEntity.getId()) && pEntity instanceof Player player) {
                ItemStack itemStack = queuedReplacements.get(pEntity.getId());
                player.getInventory().setItem(player.getInventory().selected, itemStack);
                queuedReplacements.remove(pEntity.getId());
            }
        }
    }
    private final Map<Integer, ItemStack> queuedReplacements = new HashMap<>();
    //TODO consider a better alternative for server-side movement
    public void queueReplacement(ItemStack outputItemStack, int playerId) {
        queuedReplacements.put(playerId, outputItemStack);
    }

}