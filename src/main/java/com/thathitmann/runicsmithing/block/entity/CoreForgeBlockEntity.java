package com.thathitmann.runicsmithing.block.entity;

import com.thathitmann.runicsmithing.generators.RSDynamicRecipeRegistry;
import com.thathitmann.runicsmithing.generators.RSRecipeCategory;
import com.thathitmann.runicsmithing.item.custom.supers.Aspect;
import com.thathitmann.runicsmithing.item.custom.supers.HotIngotBase;
import com.thathitmann.runicsmithing.item.custom.supers.SmithingChainItem;
//import com.thathitmann.runicsmithing.recipe.ForgeRecipe;
import com.thathitmann.runicsmithing.screen.CoreForgeBlockMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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

import static com.thathitmann.runicsmithing.block.custom.CoreForgeBlock.DEPTH;
import static com.thathitmann.runicsmithing.block.custom.ForgeBlock.LIT;

public class CoreForgeBlockEntity extends ForgeBlockEntityParent implements MenuProvider {


    public CoreForgeBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state, ModBlockEntities.CORE_FORGE_BLOCK.get());

    }




    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new CoreForgeBlockMenu(id, inventory, this, this.data);
    }



    public static void tick(Level level, BlockPos blockPos, BlockState blockState, ForgeBlockEntityParent entity) {

        if(level.isClientSide()) {
            return;
        }



        //Heat up while burning
        if (entity.burnTime > 0) {
            entity.burnTime--;
            if (entity.heatPercentage < 100) {
                entity.heatPercentage++;
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

    private static Aspect getDepthAspect(BlockState state) {
        int depth = state.getValue(DEPTH);

        if (depth > 500) {
            return new Aspect("Coreforged at " + -depth + " for +15 quality.", 15);
        } else if (depth > 450) {
            return new Aspect("Hellforged at " + -depth + " for +10 quality.", 10);
        } else if (depth > 400) {
            return new Aspect("Netherforged at " + -depth + " for +9 quality.", 9);
        } else if (depth > 350) {
            return new Aspect("Stygianforged at " + -depth + " for +8 quality.", 8);
        } else if (depth > 300) {
            return new Aspect("Ultradeepforged at " + -depth + " for +7 quality.", 7);
        } else if (depth > 250) {
            return new Aspect("Deepforged at " + -depth + " for +6 quality.", 6);
        } else if (depth > 200) {
            return new Aspect("Dwarvforged at " + -depth + " for +5 quality.", 5);
        } else if (depth > 150) {
            return new Aspect("Extreme pressure forged at " + -depth + " for +4 quality.", 4);
        } else if (depth > 100) {
            return new Aspect("Pressure forged at " + -depth + " for +3 quality.", 3);
        } else if (depth > 50) {
            return new Aspect("Low-pressure forged at " + -depth + " for +2 quality.", 2);
        } else if (depth > 0) {
            return new Aspect("Depth forged at " + -depth + " for +1 quality.", 1);
        } else {
            return new Aspect("Normally forged for no quality bonus.", 10);
        }
    }

    private static void craftItem(ForgeBlockEntityParent entity, SimpleContainer inventory) {
        if (hasRecipe(entity, inventory)) {

            //New dynamic recipe
            Item forgeOutput = RSDynamicRecipeRegistry.getRecipeResult(RSRecipeCategory.FORGE_HEATING, inventory.getItem(0).getItem());


            //Switch the item and add NBT tags
            ItemStack inputItemStack = entity.itemHandler.getStackInSlot(0);
            entity.itemHandler.extractItem(0, 1, false);
            ItemStack outputItemStack = new ItemStack(forgeOutput, entity.itemHandler.getStackInSlot(1).getCount() + 1);



            if (forgeOutput instanceof HotIngotBase && outputItemStack.getTag() != null) {
                CompoundTag tag = SmithingChainItem.addNBTAspectTag(outputItemStack.getTag(), getDepthAspect(entity.getBlockState()).getQualityLevel(), getDepthAspect(entity.getBlockState()).getName());
                outputItemStack.setTag(tag);
            }
            else if (forgeOutput instanceof SmithingChainItem) {
                outputItemStack.setTag(inputItemStack.getTag());
            }

            entity.itemHandler.setStackInSlot(1, outputItemStack);

        }
    }
    private static boolean hasRecipe(@NotNull ForgeBlockEntityParent ignoredEntity, SimpleContainer inventory) {

        //If not valid, return false immediately
        if (!RSDynamicRecipeRegistry.isItemAValidInput(inventory.getItem(0).getItem(), RSRecipeCategory.FORGE_HEATING)) {
            return false;
        }

        Item output = RSDynamicRecipeRegistry.getRecipeResult(RSRecipeCategory.FORGE_HEATING, inventory.getItem(0).getItem());


        return canInsertAmountIntoOutput(inventory) && canInsertItemIntoOutputSlot(inventory, output);
    }
    private static boolean canInsertItemIntoOutputSlot(@NotNull SimpleContainer inventory, Item stack) {
        return inventory.getItem(1).isEmpty() || inventory.getItem(1).getItem() == stack;
    }
    private static boolean canInsertAmountIntoOutput(@NotNull SimpleContainer inventory) {
        return inventory.getItem(1).getMaxStackSize() > inventory.getItem(1).getCount();
    }
}
