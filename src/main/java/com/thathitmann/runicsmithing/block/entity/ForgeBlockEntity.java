package com.thathitmann.runicsmithing.block.entity;


import com.thathitmann.runicsmithing.generators.RSDynamicRecipeRegistry;
import com.thathitmann.runicsmithing.generators.RSRecipeCategory;
import com.thathitmann.runicsmithing.item.ModItems;
import com.thathitmann.runicsmithing.item.custom.supers.RunicSmithingMaterial;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.ToolBase;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.toolModifiers.ToolModifierStack;
import com.thathitmann.runicsmithing.screen.ForgeBlockMenu;
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


        }
        //Cool down otherwise
        else {
            if (entity.heatPercentage >= 100) {
                setToUnlit(level, blockPos, blockState, true);
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

                forgeOutput.setTag(tag);
                forgeOutput.setHoverName(Component.literal(String.format("Hot %s Ingot", StringUtils.capitalize(material.getMaterialName())).strip()));
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
    private static boolean hasRecipe(@NotNull ForgeBlockEntityParent entity, SimpleContainer inventory) {
        boolean hasValidMaterialInFirstSlot = false;
        //Accept only items with a primitive output


        //New recipe code
        Item output = null;
        Item itemInFirstSlot = inventory.getItem(0).getItem();

        RunicSmithingMaterial outputMaterial = RunicSmithingMaterial.getAssociatedMaterial(itemInFirstSlot);

        if (outputMaterial != null) {
            output = ModItems.HOT_INGOT.get();
            if (outputMaterial.getPrimitive()) {
                {hasValidMaterialInFirstSlot = true;}
            }
        }
        else if (itemInFirstSlot instanceof ToolBase) {
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
