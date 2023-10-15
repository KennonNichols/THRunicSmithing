package com.thathitmann.runicsmithing.item.custom.supers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class ForgeIngotLookup {

    static Item copperIngot = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:copper_ingot"));
    static Item ironIngot = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:iron_ingot"));
    static Item goldIngot = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:gold_ingot"));
    static Item diamond = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:diamond"));
    static Item netheriteIngot = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:netherite_ingot"));

    static Item hotCopperIngot = ForgeRegistries.ITEMS.getValue(new ResourceLocation("runicsmithing:hot_copper_ingot"));
    static Item hotIronIngot = ForgeRegistries.ITEMS.getValue(new ResourceLocation("runicsmithing:hot_iron_ingot"));
    static Item hotGoldIngot = ForgeRegistries.ITEMS.getValue(new ResourceLocation("runicsmithing:hot_gold_ingot"));
    static Item hotDiamond = ForgeRegistries.ITEMS.getValue(new ResourceLocation("runicsmithing:hot_diamond"));
    static Item hotNetheriteIngot = ForgeRegistries.ITEMS.getValue(new ResourceLocation("runicsmithing:hot_netherite_ingot"));


    public static final HashMap<Item, Item> forgeHeatingLookup = new HashMap<>(Map.of(
            copperIngot, hotCopperIngot,
            goldIngot, hotGoldIngot,
            ironIngot, hotIronIngot,
            diamond, hotDiamond,
            netheriteIngot, hotNetheriteIngot
    ));
}
