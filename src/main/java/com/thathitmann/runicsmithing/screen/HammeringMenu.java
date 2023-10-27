package com.thathitmann.runicsmithing.screen;

import com.thathitmann.runicsmithing.generators.RSDynamicRecipeRegistry;
import com.thathitmann.runicsmithing.generators.RSRecipeCategory;
import com.thathitmann.runicsmithing.item.custom.supers.Aspect;
import com.thathitmann.runicsmithing.item.custom.supers.ForgeHammer;
import com.thathitmann.runicsmithing.item.custom.supers.ForgeLevel;
import com.thathitmann.runicsmithing.item.custom.supers.SmithingChainItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class HammeringMenu extends AbstractContainerMenu {
    private final Player player;


    private final List<Integer> states = new ArrayList<>(9);
    private final int startingState;
    private final ForgeHammer entity;

    public HammeringMenu(int id, Inventory inventory, FriendlyByteBuf ignoredExtraData) {
        this(id, inventory, (ForgeHammer)inventory.player.getOffhandItem().getItem(), 4);
    }

    public HammeringMenu(int id, @NotNull Inventory inventory, ForgeHammer entity, int startingState){
        super(ModMenuTypes.HAMMERING_MENU.get(), id);
        this.player = inventory.player;

        this.entity = entity;

        this.startingState = startingState;
        for (int i = 0; i <9; i++) {
            states.add(i, startingState);
        }
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


    public int getButtonState(int index) {
        return this.states.get(index);
    }

    public Boolean tryReduceButtonState(int index) {
        if (this.states.get(index) > 0) {
            this.states.set(index, this.states.get(index) - 1);
        }
        return this.states.get(index) <= 0;
    }

    private List<ForgeLevel> getListAsForgeLevels() {
        List<ForgeLevel> levelList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (this.states.get(i) == startingState) {
                levelList.add(i, ForgeLevel.PRESENT);
            }
            else if (this.states.get(i) == 0) {
                levelList.add(i, ForgeLevel.GONE);
            }
            else
            {
                levelList.add(i, ForgeLevel.INCOMPLETE);
            }
        }
        return levelList;
    }

    private Boolean hasListChanged() {
        for (int i = 0; i < 9; i++) {
            if (this.states.get(i) != startingState) {return true;}
        }
        return false;
    }

    public void attemptToCraft() {
        ItemStack inputStack = player.getMainHandItem();
        if (hasListChanged() && inputStack.getItem() instanceof SmithingChainItem) {
            Item result = RSDynamicRecipeRegistry.getRecipeResult(RSRecipeCategory.SHAPED_HAMMERING, inputStack.getItem(), getListAsForgeLevels());
            if (result != inputStack.getItem()) {
                ItemStack outputItemStack = new ItemStack(result, player.getMainHandItem().getCount());
                CompoundTag tag = inputStack.getTag();
                if (entity.isAdvancedMode() && tag != null) {
                    tag = SmithingChainItem.addNBTAspectTag(tag, new Aspect("Forged with iron tools for +3 quality.", 3));
                }
                outputItemStack.setTag(tag);
                //player.level().playSeededSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 1f,1f,0);
                player.getInventory().setItem(player.getInventory().selected, outputItemStack);
            }
            else {
                //Destroy invalid recipes
                //player.level().playSeededSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1f,1f,0);
                player.getInventory().setItem(player.getInventory().selected, new ItemStack(Items.AIR));
            }
        }
    }
}
