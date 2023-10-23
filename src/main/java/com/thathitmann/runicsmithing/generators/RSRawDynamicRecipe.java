package com.thathitmann.runicsmithing.generators;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Objects;

public final class RSRawDynamicRecipe {
    private final RegistryObject<Item> inputRegistry;
    private final Item inputItem;
    private final RegistryObject<Item> output;
    private final RSRecipeCategory category;
    private final List<Boolean> specialArgs;

    public RSRawDynamicRecipe(Item input, RegistryObject<Item> output, RSRecipeCategory category, List<Boolean> specialArgs) {
        this.inputRegistry = null;
        this.inputItem = input;


        this.output = output;
        this.category = category;
        this.specialArgs = specialArgs;
    }
    public RSRawDynamicRecipe(RegistryObject<Item> input, RegistryObject<Item> output, RSRecipeCategory category, List<Boolean> specialArgs) {
        this.inputRegistry = input;
        this.inputItem = null;

        this.output = output;
        this.category = category;
        this.specialArgs = specialArgs;
    }


    public Item getInput() {
        if (inputItem != null) {return inputItem;}
        else {return inputRegistry.get();}
    }

    public Item getOutput() {
        return output.get();
    }

    public RSRecipeCategory category() {
        return category;
    }

    public List<Boolean> specialArgs() {
        return specialArgs;
    }

}
