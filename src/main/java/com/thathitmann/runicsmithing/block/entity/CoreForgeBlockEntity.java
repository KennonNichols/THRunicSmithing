package com.thathitmann.runicsmithing.block.entity;

import com.thathitmann.runicsmithing.item.custom.supers.Aspect;
import com.thathitmann.runicsmithing.item.custom.supers.SmithingChainItem;
import com.thathitmann.runicsmithing.recipe.ForgeRecipe;
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

import java.util.Optional;

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


            if (entity.burnTime <= 0) {
                level.setBlock(blockPos, blockState.setValue(LIT, false),3);
            }
        }
        //Cool down otherwise
        else {
            if (entity.heatPercentage > 0) {
                entity.heatPercentage--;
                level.setBlock(blockPos, blockState.setValue(LIT, false),3);
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
            Item forgeInput = inventory.getItem(0).getItem();
            Level level = entity.getLevel();
            Optional<ForgeRecipe> recipe = level.getRecipeManager().getRecipeFor(ForgeRecipe.Type.INSTANCE, inventory, level);
            Item forgeOutput = recipe.get().getResultItem().getItem();



            entity.itemHandler.extractItem(0, 1, false);
            ItemStack outputItemStack = new ItemStack(forgeOutput, entity.itemHandler.getStackInSlot(1).getCount() + 1);
            if (forgeOutput instanceof SmithingChainItem) {
                CompoundTag tag = ((SmithingChainItem) forgeOutput).buildNBTAspectTag(outputItemStack, getDepthAspect(entity.getBlockState()).getQualityLevel(), getDepthAspect(entity.getBlockState()).getName());
                outputItemStack.setTag(tag);
            }
            entity.itemHandler.setStackInSlot(1, outputItemStack);

        }
    }
    private static boolean hasRecipe(@NotNull ForgeBlockEntityParent entity, SimpleContainer inventory) {
        Level level = entity.getLevel();
        Optional<ForgeRecipe> recipe = level.getRecipeManager().getRecipeFor(ForgeRecipe.Type.INSTANCE, inventory, level);


        return recipe.isPresent() && canInsertAmountIntoOutput(inventory) && canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem());
    }
    private static boolean canInsertItemIntoOutputSlot(@NotNull SimpleContainer inventory, ItemStack stack) {
        return inventory.getItem(1).isEmpty() || inventory.getItem(1).getItem() == stack.getItem();
    }
    private static boolean canInsertAmountIntoOutput(@NotNull SimpleContainer inventory) {
        return inventory.getItem(1).getMaxStackSize() > inventory.getItem(1).getCount();
    }
}
