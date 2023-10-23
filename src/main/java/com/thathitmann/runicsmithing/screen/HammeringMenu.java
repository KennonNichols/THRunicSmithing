package com.thathitmann.runicsmithing.screen;

import com.thathitmann.runicsmithing.generators.RSDynamicRecipeRegistry;
import com.thathitmann.runicsmithing.generators.RSRecipeCategory;
import com.thathitmann.runicsmithing.item.custom.supers.ForgeHammer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;


public class HammeringMenu extends AbstractContainerMenu {
    private final Player player;


    private final List<Integer> states = new ArrayList<>(9);
    private final int startingState;

    public HammeringMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (ForgeHammer)inventory.player.getOffhandItem().getItem(), 4);
    }

    public HammeringMenu(int id, Inventory inventory, ForgeHammer entity, int startingState){
        super(ModMenuTypes.HAMMERING_MENU.get(), id);
        this.player = inventory.player;

        this.startingState = startingState;
        for (int i = 0; i <9; i++) {
            states.add(i, startingState);
        }
    }


    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public final boolean stillValid(Player pPlayer) {
        return true;
    }


    public int getButtonState(int index) {
        return this.states.get(index);
    }

    public void tryReduceButtonState(int index) {
        if (this.states.get(index) == this.startingState) {
            this.states.set(index, this.states.get(index) - 1);
        }
    }

    private List<Boolean> getListAsBoolean() {
        List<Boolean> booleanizedList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            booleanizedList.add(i, this.states.get(i) == startingState);
        }
        return booleanizedList;
    }

    private Boolean hasListChanged() {
        for (int i = 0; i < 9; i++) {
            if (this.states.get(i) != startingState) {return true;}
        }
        return false;
    }

    public void attemptToCraft() {
        if (hasListChanged()) {
            Item result = RSDynamicRecipeRegistry.getRecipeResult(RSRecipeCategory.SHAPED_HAMMERING, player.getMainHandItem().getItem(), getListAsBoolean());
            if (result != null) {
                player.getInventory().setItem(player.getInventory().selected, new ItemStack(result, player.getMainHandItem().getCount()));
            }
            else {
                player.getInventory().setItem(player.getInventory().selected, new ItemStack(Items.AIR));
            }
        }
    }
}
