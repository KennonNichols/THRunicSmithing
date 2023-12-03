package com.thathitmann.runicsmithing.item.custom.supers;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public record GeneratableItem(RegistryObject<Item> item, @NotNull String formatableName, @NotNull String sourceName, @NotNull String outName) { }
