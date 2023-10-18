package com.thathitmann.runicsmithing.api_integration;

import com.thathitmann.runicsmithing.RunicSmithing;
import com.thathitmann.runicsmithing.recipe.ForgeRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;


@JeiPlugin
public class JEIRunicSmithingPlugin implements IModPlugin {

    public static RecipeType<ForgeRecipe> FORGE_TYPE =
            new RecipeType<>(ForgeRecipeCategory.UID, ForgeRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(RunicSmithing.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new
                ForgeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<ForgeRecipe> recipesForge = rm.getAllRecipesFor(ForgeRecipe.Type.INSTANCE);
        registration.addRecipes(FORGE_TYPE, recipesForge);
    }
}
