package com.thathitmann.runicsmithing.generators;

import com.thathitmann.runicsmithing.item.custom.supers.ForgeLevel;
import net.minecraft.world.item.Item;

import java.util.List;

public record RSDynamicRecipe(Item input, Item output, RSRecipeCategory category, List<ForgeLevel> specialArgs) {
}
