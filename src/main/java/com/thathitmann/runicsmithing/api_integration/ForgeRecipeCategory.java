package com.thathitmann.runicsmithing.api_integration;

import com.thathitmann.runicsmithing.RunicSmithing;
import com.thathitmann.runicsmithing.block.ModBlocks;
import com.thathitmann.runicsmithing.recipe.ForgeRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ForgeRecipeCategory implements IRecipeCategory<ForgeRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(RunicSmithing.MOD_ID, "forge_heating");
    public static final ResourceLocation TEXTURE = new ResourceLocation(RunicSmithing.MOD_ID, "textures/gui/forge_block_gui.png");

    private final IDrawable background;
    private final IDrawable icon;


    public ForgeRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.FORGE_BLOCK.get()));
    }



    @Override
    public RecipeType<ForgeRecipe> getRecipeType() {
        return JEIRunicSmithingPlugin.FORGE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Forge Heating");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ForgeRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,42, 15).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT,116, 15).addItemStack(recipe.getResultItem());
    }
}
