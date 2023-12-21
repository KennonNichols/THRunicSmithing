package com.thathitmann.runicsmithing.block.entity;

import com.thathitmann.runicsmithing.generators.RSDynamicRecipeRegistry;
import com.thathitmann.runicsmithing.generators.RSRecipeCategory;
import com.thathitmann.runicsmithing.item.ModItems;
import com.thathitmann.runicsmithing.item.custom.supers.RunicSmithingMaterial;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.ToolBase;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.forged_tool.ForgedTool;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.toolModifiers.ToolModifierStack;
import com.thathitmann.runicsmithing.screen.ToolStationBlockMenu;
import com.thathitmann.runicsmithing.screen.ToolStationBlockScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ToolStationBlockEntity extends BlockEntity implements MenuProvider {

    private final ToolModifierStack toolModifierStack = ToolModifierStack.getBlankInstance();
    private ItemStack inputItemStack = null;
    private boolean displayInfo = false;


    public ToolStationBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TOOL_STATION_BLOCK.get(), pos, state);
        attemptUpdateInputItemStack();
    }


    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);

    }

    private ToolStationBlockScreen screen;
    public void setScreen(ToolStationBlockScreen screen) {
        this.screen = screen;
    }

    protected final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        protected void onContentsChanged(int slot) {
            attemptUpdateInputItemStack();
            setChanged();
            if (screen != null) {
                screen.containerChanged();
            }
        }
    };

    private void attemptUpdateInputItemStack() {
        if (itemHandler.getStackInSlot(0) != inputItemStack) {
            inputItemStack = itemHandler.getStackInSlot(0);


            //If out input item is a tool, then enable display and update the modifier stack
            if (inputItemStack.getItem() instanceof ForgedTool tool) {
                toolModifierStack.duplicateDataFrom(ToolModifierStack.buildFromTag(inputItemStack.getOrCreateTag().getCompound(ToolModifierStack.TOOL_MODIFIER_STACK_TAG_ID)));
                displayInfo = true;
            }
            else {
                displayInfo = false;
            }
            //return true;
        }
        //return false;
    }




    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return  lazyItemHandler.cast();
        }


        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
    }



    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new ToolStationBlockMenu(id, inventory, this, toolModifierStack);
    }


    public @NotNull ItemStack getItemInMainSlot() {
        return itemHandler.getStackInSlot(0);
    }


    public static void tick(Level level, BlockPos blockPos, BlockState blockState, ToolStationBlockEntity entity) {
        if(level.isClientSide()) {
            return;
        }


    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Tool Station");
    }

    public @NotNull ToolModifierStack getToolModifierStack() {
        return toolModifierStack;
    }

    public boolean getDisplayingInfo() {
        return displayInfo;
    }
}
