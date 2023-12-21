package com.thathitmann.runicsmithing.screen;

import com.thathitmann.runicsmithing.generators.GeneratedItemRegistry;
import com.thathitmann.runicsmithing.generators.RSDynamicRecipeRegistry;
import com.thathitmann.runicsmithing.generators.RSRecipeCategory;
import com.thathitmann.runicsmithing.item.custom.supers.*;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.SmithingChainItem;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.toolModifiers.AspectModifier;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.toolModifiers.ToolModifierStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.StringUtils;
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
            if (entity.isAdvancedMode()) {
                //Random pitch between .8 and 1.2f
                float pitch = player.getRandom().nextFloat() * .4f + .8f;
                player.playNotifySound(SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 0.2f, pitch);
            }


            //player.level().playSeededSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 1.0f, 0.5f, 1);
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
            ItemStack outputItemStack;
            Item result = RSDynamicRecipeRegistry.getRecipeResult(RSRecipeCategory.SHAPED_HAMMERING, inputStack.getItem(), getListAsForgeLevels());
            if (result != inputStack.getItem()) {
                outputItemStack = new ItemStack(result, player.getMainHandItem().getCount());
                CompoundTag tag = inputStack.getTag();
                if (entity.isAdvancedMode()) {
                    ToolModifierStack.addToolModifierAndTransferNBT(tag, tag, new AspectModifier("Well Forged", "Forged with iron tools for +3 quality.", 3));
                }
                outputItemStack.setTag(tag);
                outputItemStack.setHoverName(Component.literal(String.format(GeneratedItemRegistry.getGeneratableItem(result).formatableName(), StringUtils.capitalize(RunicSmithingMaterial.values()[tag.getCompound(ToolModifierStack.QUICKGRAB_TAG_ID).getInt(ToolModifierStack.QUICKGRAB_MATERIAL_TAG_ID)].getMaterialName()))));
                player.playNotifySound(SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 1f,1f);
            }
            else {
                //Destroy invalid recipes
                player.playNotifySound (SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1f,1f);
                outputItemStack = new ItemStack(Items.AIR);
            }
            entity.queueReplacement(outputItemStack, player.getId());
        }
    }
}
