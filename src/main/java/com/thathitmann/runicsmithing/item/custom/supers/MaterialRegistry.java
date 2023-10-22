package com.thathitmann.runicsmithing.item.custom.supers;

import com.thathitmann.runicsmithing.generators.RSDynamicRecipeRegistry;
import com.thathitmann.runicsmithing.generators.RSRawDynamicRecipe;
import com.thathitmann.runicsmithing.generators.RSRecipeCategory;
import com.thathitmann.runicsmithing.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class MaterialRegistry {
    private final static List<RunicSmithingMaterial> materials = new ArrayList<>();


    public static void addMaterialToRegistry(Item associatedItem, String name) {
        addMaterialToRegistry(associatedItem, name, "_ingot", false);
    }
    public static void addMaterialToRegistry(Item associatedItem, String name, Boolean isPrimitive) {
        addMaterialToRegistry(associatedItem, name, "_ingot", isPrimitive);
    }
    public static void addMaterialToRegistry(Item associatedItem, String name, String suffix) {
        addMaterialToRegistry(associatedItem, name, suffix, false);
    }
    public static void addMaterialToRegistry(Item associatedItem, String name, String suffix, Boolean isPrimitive) {
        //Creates material
        RunicSmithingMaterial material = new RunicSmithingMaterial(associatedItem, name, isPrimitive);



        //Add material to list
        materials.add(material);



        //creates ingot name
        String fileName = "hot_" + name + suffix;
        //Register the ingot to ModItems
        RegistryObject<Item> newHotIngot = ModItems.ITEMS.register(fileName, () -> new HotIngotBase(new Item.Properties(), material));
        //Add the ingot to the stupid list
        CreativeTabRegistry.itemsToAddToCreativeModeTabBecauseOfThisNonsense.add(newHotIngot);


        //add forge heating recipe
        RSRawDynamicRecipe newRawRecipe = new RSRawDynamicRecipe(associatedItem, newHotIngot, RSRecipeCategory.FORGE_HEATING, null);
        RSDynamicRecipeRegistry.addNewRawRecipe(newRawRecipe);


    }



}
