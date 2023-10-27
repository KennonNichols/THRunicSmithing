package com.thathitmann.runicsmithing.block.entity;


import com.thathitmann.runicsmithing.generators.RSDynamicRecipeRegistry;
import com.thathitmann.runicsmithing.generators.RSRecipeCategory;
import com.thathitmann.runicsmithing.item.custom.supers.SmithingChainItem;
import com.thathitmann.runicsmithing.screen.ForgeBlockMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.thathitmann.runicsmithing.block.custom.ForgeBlock.LIT;

public class ForgeBlockEntity extends ForgeBlockEntityParent implements MenuProvider {


    public ForgeBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state, ModBlockEntities.FORGE_BLOCK.get());
    }


    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new ForgeBlockMenu(id, inventory, this, this.data);
    }


    public static void tick(Level level, BlockPos blockPos, BlockState blockState, ForgeBlockEntityParent entity) {
        if(level.isClientSide()) {
            return;
        }



        //Heat up while burning
        if (entity.burnTime > 0) {
            entity.burnTime--;
            if (entity.heatPercentage < 100) {
                if (rand.nextFloat() > 0.66f) {
                    entity.heatPercentage++;
                }

            }


            //if (entity.burnTime <= 0) {
            //    setToUnlit(level, blockPos, blockState);
            //}
        }
        //Cool down otherwise
        else {
            if (entity.heatPercentage >= 100) {
                setToUnlit(level, blockPos, blockState);
            }


            if (entity.heatPercentage > 0) {
                entity.heatPercentage--;
            }
        }


        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i=0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        if(hasRecipe(entity, inventory) && entity.heatPercentage >= 100) {
            setChanged(level, blockPos, blockState);
            entity.progress++;
            if(entity.progress >= entity.maxProgress) {
                craftItem(entity, inventory);
                entity.resetProgress();
            }
        } else {
            entity.resetProgress();
            setChanged(level, blockPos, blockState);
        }


    }


    private static void craftItem(ForgeBlockEntityParent entity, SimpleContainer inventory) {
        if (hasRecipe(entity, inventory)) {
            //New dynamic recipe
            Item forgeOutput = RSDynamicRecipeRegistry.getRecipeResult(RSRecipeCategory.FORGE_HEATING, inventory.getItem(0).getItem());




            entity.itemHandler.extractItem(0, 1, false);
            entity.itemHandler.setStackInSlot(1, new ItemStack(forgeOutput, entity.itemHandler.getStackInSlot(1).getCount() + 1));
        }
    }
    private static boolean hasRecipe(@NotNull ForgeBlockEntityParent entity, SimpleContainer inventory) {
        boolean hasValidMaterialInFirstSlot = false;
        //Accept only items with a primitive output


        //New recipe code
        Item output = null;
        Item itemInFirstSlot = inventory.getItem(0).getItem();
        if (RSDynamicRecipeRegistry.isItemAValidInput(itemInFirstSlot, RSRecipeCategory.FORGE_HEATING)) {
            output = RSDynamicRecipeRegistry.getRecipeResult(RSRecipeCategory.FORGE_HEATING, itemInFirstSlot);
            if (output instanceof SmithingChainItem) {
                if (((SmithingChainItem) output).getMaterial().getPrimitive()) {hasValidMaterialInFirstSlot = true;}
            }
        }



        return hasValidMaterialInFirstSlot && canInsertAmountIntoOutput(inventory) && canInsertItemIntoOutputSlot(inventory, output);
    }
    private static boolean canInsertItemIntoOutputSlot(@NotNull SimpleContainer inventory, Item stack) {
        return inventory.getItem(1).isEmpty() || inventory.getItem(1).getItem() == stack;
    }
    private static boolean canInsertAmountIntoOutput(@NotNull SimpleContainer inventory) {
        return inventory.getItem(1).getMaxStackSize() > inventory.getItem(1).getCount();
    }


}
