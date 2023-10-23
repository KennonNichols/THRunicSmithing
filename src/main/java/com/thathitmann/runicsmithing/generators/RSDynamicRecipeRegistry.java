package com.thathitmann.runicsmithing.generators;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class RSDynamicRecipeRegistry {
    static final List<RSRawDynamicRecipe> rawRecipes = new ArrayList<>();
    static final List<RSDynamicRecipe> recipes = new ArrayList<>();


    public static final Boolean[] PICKAXE_SHAPE = {
            true, true, true,
            false, true, false,
            false, true, false};
    public static final Boolean[] SWORD_SHAPE = {
            false, true, false,
            false, true, false,
            false, false, false};
    public static final Boolean[] SHOVEL_SHAPE = {
            false, true, false,
            false, true, false,
            false, true, false};
    public static final Boolean[] AXE_SHAPE = {
            false, true, true,
            false, true, true,
            false, true, false};
    public static final Boolean[] HOE_SHAPE = {
            false, true, true,
            false, true, false,
            false, true, false};


    public static Boolean isShapedHammeringPatternValid(List<Boolean> shapedHammeringPatternList) {

        Boolean[] shapedHammeringPattern = shapedHammeringPatternList.toArray(new Boolean[9]);

        if (Arrays.equals(shapedHammeringPattern, PICKAXE_SHAPE)) {
            return true;
        }
        if (Arrays.equals(shapedHammeringPattern, SHOVEL_SHAPE)) {
            return true;
        }
        if (Arrays.equals(shapedHammeringPattern, SWORD_SHAPE)) {
            return true;
        }
        if (Arrays.equals(shapedHammeringPattern, AXE_SHAPE)) {
            return true;
        }
        if (Arrays.equals(shapedHammeringPattern, HOE_SHAPE)) {
            return true;
        }
        return false;
    }
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

    public static @Nullable Item getRecipeResult (RSRecipeCategory recipeCategory, Item input) {
        return getRecipeResult(recipeCategory, input, new ArrayList<>());
    }

    public static @Nullable Item getRecipeResult (RSRecipeCategory recipeCategory, Item input, List<Boolean> specialArgs) {
        for (RSDynamicRecipe recipe : getAllRecipesOfType(recipeCategory)) {
            //Check that specialArgs matches if using shaped hammering
            if (recipeCategory == RSRecipeCategory.SHAPED_HAMMERING) {
                if (recipe.input() == input && recipe.specialArgs() == specialArgs) {return recipe.output();}
            }
            else {
                if (recipe.input() == input) {return recipe.output();}
            }
        }
        return null;
    }

    public static void cook() {
        for (RSRawDynamicRecipe rawRecipe : rawRecipes) {
            Item input = rawRecipe.getInput();
            Item output = rawRecipe.getOutput();
            RSRecipeCategory category = rawRecipe.category();
            List<Boolean> specialArgs = rawRecipe.specialArgs();

            addNewRecipe(new RSDynamicRecipe(input, output, category, specialArgs));
        }

    }


    private static void addNewRecipe(RSDynamicRecipe newRecipe) {recipes.add(newRecipe);}
    public static void addNewRawRecipe(RSRawDynamicRecipe newRawRecipe) {rawRecipes.add(newRawRecipe);}


}
