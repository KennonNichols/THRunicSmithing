package com.thathitmann.runicsmithing.block.entity;

import com.thathitmann.runicsmithing.generators.RSDynamicRecipeRegistry;
import com.thathitmann.runicsmithing.generators.RSRecipeCategory;
import com.thathitmann.runicsmithing.item.ModItems;
import com.thathitmann.runicsmithing.item.custom.supers.RunicSmithingMaterial;
//import com.thathitmann.runicsmithing.recipe.ForgeRecipe;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.ToolBase;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.toolModifiers.AspectModifier;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.toolModifiers.ToolModifierStack;
import com.thathitmann.runicsmithing.screen.CoreForgeBlockMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.thathitmann.runicsmithing.block.custom.CoreForgeBlock.DEPTH;

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

    private static AspectModifier getDepthAspect(BlockState state) {
        int depth = state.getValue(DEPTH);
        AspectModifier depthAspect;

        if (depth > 500) {
            return new AspectModifier("Coreforged at " + -depth + " for +15 quality.", 15);
        } else if (depth > 450) {
            return new AspectModifier("Hellforged at " + -depth + " for +10 quality.", 10);
        } else if (depth > 400) {
            return new  AspectModifier("Netherforged at " + -depth + " for +9 quality.", 9);
        } else if (depth > 350) {
            return new  AspectModifier("Stygianforged at " + -depth + " for +8 quality.", 8);
        } else if (depth > 300) {
            return new AspectModifier("Ultradeepforged at " + -depth + " for +7 quality.", 7);
        } else if (depth > 250) {
            return new AspectModifier("Deepforged at " + -depth + " for +6 quality.", 6);
        } else if (depth > 200) {
            return new AspectModifier("Dwarvforged at " + -depth + " for +5 quality.", 5);
        } else if (depth > 150) {
            return new AspectModifier("Extreme pressure forged at " + -depth + " for +4 quality.", 4);
        } else if (depth > 100) {
            return new AspectModifier("Pressure forged at " + -depth + " for +3 quality.", 3);
        } else if (depth > 50) {
            return new  AspectModifier("Low-pressure forged at " + -depth + " for +2 quality.", 2);
        } else if (depth > 0) {
            return new AspectModifier("Depth forged at " + -depth + " for +1 quality.", 1);
        } else {
            return new AspectModifier("Normally forged for no quality bonus.", 0);
        }
    }

    private static void craftItem(ForgeBlockEntityParent entity, SimpleContainer inventory) {
        if (hasRecipe(entity, inventory)) {

            ItemStack forgeOutput;
            ItemStack forgeInput = inventory.getItem(0);

            RunicSmithingMaterial material = RunicSmithingMaterial.getAssociatedMaterial(forgeInput.getItem());

            //If it's heating to an ingot, do that
            if (material != null) {
                forgeOutput = new ItemStack(ModItems.HOT_INGOT.get(), entity.itemHandler.getStackInSlot(1).getCount() + 1);
                CompoundTag tag = new CompoundTag();
                tag.putInt("CustomModelData", material.ordinal());
                //Tag it with material
                CompoundTag quickgrabTag = new CompoundTag();
                quickgrabTag.putInt(ToolModifierStack.QUICKGRAB_MATERIAL_TAG_ID, material.ordinal());
                tag.put(ToolModifierStack.QUICKGRAB_TAG_ID, quickgrabTag);


                //Add depth aspect
                ToolModifierStack.addToolModifierAndTransferNBT(tag, tag, getDepthAspect(entity.getBlockState()));



                forgeOutput.setTag(tag);
                forgeOutput.setHoverName(Component.literal(String.format("Hot %s Ingot", StringUtils.capitalize(material.getMaterialName()))));
            }
            //Else, just turn it into a hot tool base
            else
            {
                Item outputItem = RSDynamicRecipeRegistry.getRecipeResult(RSRecipeCategory.FORGE_HEATING, forgeInput.getItem());
                forgeOutput = new ItemStack(outputItem, entity.itemHandler.getStackInSlot(1).getCount() + 1);
                //Copy the tag
                forgeOutput.setTag(forgeInput.getTag());
            }




            entity.itemHandler.extractItem(0, 1, false);
            entity.itemHandler.setStackInSlot(1, forgeOutput);

        }
    }
    private static boolean hasRecipe(@NotNull ForgeBlockEntityParent ignoredEntity, SimpleContainer inventory) {

        Item output = null;
        Item itemInFirstSlot = inventory.getItem(0).getItem();
        RunicSmithingMaterial outputMaterial = RunicSmithingMaterial.getAssociatedMaterial(itemInFirstSlot);
        boolean hasValidMaterialInFirstSlot = false;
        if (outputMaterial != null) {
            output = ModItems.HOT_INGOT.get();
            hasValidMaterialInFirstSlot = true;
        }
        if (itemInFirstSlot instanceof ToolBase) {
            output = RSDynamicRecipeRegistry.getRecipeResult(RSRecipeCategory.FORGE_HEATING, itemInFirstSlot);
            hasValidMaterialInFirstSlot = true;
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
