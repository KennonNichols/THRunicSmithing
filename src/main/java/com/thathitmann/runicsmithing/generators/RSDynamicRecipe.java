package com.thathitmann.runicsmithing.generators;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public record RSDynamicRecipe(Item input, Item output, RSRecipeCategory category, List<Boolean> specialArgs) {
}
