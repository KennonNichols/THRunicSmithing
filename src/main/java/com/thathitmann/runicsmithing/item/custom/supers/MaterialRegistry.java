package com.thathitmann.runicsmithing.item.custom.supers;

import com.thathitmann.runicsmithing.item.ModCreativeTab;
import com.thathitmann.runicsmithing.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class MaterialRegistry {
    private final static List<RunicSmithingMaterial> materials = new ArrayList<>();


    public static void addMaterialToRegistry(Item associatedItem, String name) {
        addMaterialToRegistry(associatedItem, name, "_ingot");
    }
    public static void addMaterialToRegistry(Item associatedItem, String name, String suffix) {

        RunicSmithingMaterial material = new RunicSmithingMaterial(associatedItem, name);


        //Register the ingot to ModItems
        ModItems.ITEMS.register("hot_" + name + suffix, () ->
                new HotIngotBase(new Item.Properties().tab(ModCreativeTab.TAB), material));

        //Add material to list
        materials.add(material);

    }



}
