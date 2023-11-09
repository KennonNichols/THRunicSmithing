package com.thathitmann.runicsmithing.generators;

import com.thathitmann.runicsmithing.item.ModItems;
import com.thathitmann.runicsmithing.item.custom.supers.ForgeLevel;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class RSDynamicRecipeRegistry {
    public static final List<RSDynamicRecipe> recipes = new ArrayList<>();

    private static final ForgeLevel p = ForgeLevel.PRESENT;
    private static final ForgeLevel g = ForgeLevel.GONE;

    public static final ForgeLevel[] PICKAXE_SHAPE = {
            p, p, p,
            g, p, g,
            g, p, g};
    public static final ForgeLevel[] SWORD_SHAPE = {
            g, p, g,
            g, p, g,
            g, g, g};
    public static final ForgeLevel[] SHOVEL_SHAPE = {
            g, p, g,
            g, p, g,
            g, p, g};
    public static final ForgeLevel[] AXE_SHAPE = {
            g, p, p,
            g, p, p,
            g, p, g};
    public static final ForgeLevel[] HOE_SHAPE = {
            g, p, p,
            g, p, g,
            g, p, g};




    public static @NotNull Boolean isItemAValidInput(Item item, RSRecipeCategory recipeCategory) {
        for (RSDynamicRecipe recipe : recipes) {
            if (recipe.input() == item && recipe.category() == recipeCategory) {return true;}
        }
        return false;
    }

    private static @NotNull List<RSDynamicRecipe> getAllRecipesOfType(RSRecipeCategory recipeCategory) {
        List<RSDynamicRecipe> recipeList = new ArrayList<>();
        for (RSDynamicRecipe recipe : recipes) {
            if (recipe.category() == recipeCategory) {recipeList.add(recipe);}
        }
        return recipeList;
    }

    public static Boolean doListsMatch(List<ForgeLevel> listA, List<ForgeLevel> listB) {
        for (int i = 0; i < listA.size(); i++) {
            if (listA.get(i) != listB.get(i)) {return false;}
        }
        return true;
    }

    public static @NotNull Item getRecipeResult (RSRecipeCategory recipeCategory, Item input) {
        return getRecipeResult(recipeCategory, input, new ArrayList<>());
    }

    public static @NotNull Item getRecipeResult (RSRecipeCategory recipeCategory, Item input, List<ForgeLevel> specialArgs) {
        for (RSDynamicRecipe recipe : getAllRecipesOfType(recipeCategory)) {
            //Check that specialArgs matches if using shaped hammering
            if (recipeCategory == RSRecipeCategory.SHAPED_HAMMERING) {
                if (recipe.input() == input && doListsMatch(recipe.specialArgs(),specialArgs)) {
                    return recipe.output();
                }
            }
            else {
                if (recipe.input() == input) {return recipe.output();}
            }
        }
        return input;
    }



    public static void cook() {
        //Heating
        addNewRecipe(new RSDynamicRecipe(ModItems.BASE_PICKAXE.get(), ModItems.HOT_PICKAXE.get(), RSRecipeCategory.FORGE_HEATING, null));
        addNewRecipe(new RSDynamicRecipe(ModItems.BASE_AXE.get(), ModItems.HOT_AXE.get(), RSRecipeCategory.FORGE_HEATING, null));
        addNewRecipe(new RSDynamicRecipe(ModItems.BASE_SWORD.get(), ModItems.HOT_SWORD.get(), RSRecipeCategory.FORGE_HEATING, null));
        addNewRecipe(new RSDynamicRecipe(ModItems.BASE_SHOVEL.get(), ModItems.HOT_SHOVEL.get(), RSRecipeCategory.FORGE_HEATING, null));
        addNewRecipe(new RSDynamicRecipe(ModItems.BASE_HOE.get(), ModItems.HOT_HOE.get(), RSRecipeCategory.FORGE_HEATING, null));
        //Hammering
        addNewRecipe(new RSDynamicRecipe(ModItems.HOT_INGOT.get(), ModItems.HOT_PICKAXE.get(), RSRecipeCategory.SHAPED_HAMMERING, List.of(PICKAXE_SHAPE)));
        addNewRecipe(new RSDynamicRecipe(ModItems.HOT_INGOT.get(), ModItems.HOT_AXE.get(), RSRecipeCategory.SHAPED_HAMMERING, List.of(AXE_SHAPE)));
        addNewRecipe(new RSDynamicRecipe(ModItems.HOT_INGOT.get(), ModItems.HOT_SWORD.get(), RSRecipeCategory.SHAPED_HAMMERING, List.of(SWORD_SHAPE)));
        addNewRecipe(new RSDynamicRecipe(ModItems.HOT_INGOT.get(), ModItems.HOT_SHOVEL.get(), RSRecipeCategory.SHAPED_HAMMERING, List.of(SHOVEL_SHAPE)));
        addNewRecipe(new RSDynamicRecipe(ModItems.HOT_INGOT.get(), ModItems.HOT_HOE.get(), RSRecipeCategory.SHAPED_HAMMERING, List.of(HOE_SHAPE)));
        //Quenching
        addNewRecipe(new RSDynamicRecipe(ModItems.HOT_PICKAXE.get(), ModItems.BASE_PICKAXE.get(), RSRecipeCategory.QUENCHING, null));
        addNewRecipe(new RSDynamicRecipe(ModItems.HOT_AXE.get(), ModItems.BASE_AXE.get(), RSRecipeCategory.QUENCHING, null));
        addNewRecipe(new RSDynamicRecipe(ModItems.HOT_SWORD.get(), ModItems.BASE_SWORD.get(), RSRecipeCategory.QUENCHING, null));
        addNewRecipe(new RSDynamicRecipe(ModItems.HOT_SHOVEL.get(), ModItems.BASE_SHOVEL.get(), RSRecipeCategory.QUENCHING, null));
        addNewRecipe(new RSDynamicRecipe(ModItems.HOT_HOE.get(), ModItems.BASE_HOE.get(), RSRecipeCategory.QUENCHING, null));
        //Grinding
        addNewRecipe(new RSDynamicRecipe(ModItems.BASE_PICKAXE.get(), ModItems.FORGED_PICKAXE.get(), RSRecipeCategory.SHARPENING, null));
        addNewRecipe(new RSDynamicRecipe(ModItems.BASE_AXE.get(), ModItems.FORGED_AXE.get(), RSRecipeCategory.SHARPENING, null));
        addNewRecipe(new RSDynamicRecipe(ModItems.BASE_SWORD.get(), ModItems.FORGED_SWORD.get(), RSRecipeCategory.SHARPENING, null));
        addNewRecipe(new RSDynamicRecipe(ModItems.BASE_SHOVEL.get(), ModItems.FORGED_SHOVEL.get(), RSRecipeCategory.SHARPENING, null));
        addNewRecipe(new RSDynamicRecipe(ModItems.BASE_HOE.get(), ModItems.FORGED_HOE.get(), RSRecipeCategory.SHARPENING, null));
    }



    private static void addNewRecipe(RSDynamicRecipe newRecipe) {recipes.add(newRecipe);}


}
