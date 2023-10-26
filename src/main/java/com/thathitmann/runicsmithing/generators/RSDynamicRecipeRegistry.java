package com.thathitmann.runicsmithing.generators;

import com.thathitmann.runicsmithing.item.custom.supers.ForgeLevel;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class RSDynamicRecipeRegistry {
    public static final List<RSRawDynamicRecipe> rawRecipes = new ArrayList<>();
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
        for (RSRawDynamicRecipe rawRecipe : rawRecipes) {
            Item input = rawRecipe.getInput();
            Item output = rawRecipe.getOutput();
            RSRecipeCategory category = rawRecipe.category();
            List<ForgeLevel> specialArgs = rawRecipe.specialArgs();

            addNewRecipe(new RSDynamicRecipe(input, output, category, specialArgs));
        }

    }


    private static void addNewRecipe(RSDynamicRecipe newRecipe) {recipes.add(newRecipe);}
    public static void addNewRawRecipe(RSRawDynamicRecipe newRawRecipe) {rawRecipes.add(newRawRecipe);}


}
