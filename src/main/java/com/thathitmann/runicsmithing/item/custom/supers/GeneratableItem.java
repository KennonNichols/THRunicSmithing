package com.thathitmann.runicsmithing.item.custom.supers;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public record GeneratableItem(RegistryObject<Item> item, String formatableName, String sourceName, String outName) { }
