package com.thathitmann.runicsmithing.generators;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class RSDynamicRecipeRegistry {
    static final List<RSRawDynamicRecipe> rawRecipes = new ArrayList<>();
    static final List<RSDynamicRecipe> recipes = new ArrayList<>();



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
