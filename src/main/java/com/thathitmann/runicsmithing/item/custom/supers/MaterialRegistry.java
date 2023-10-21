package com.thathitmann.runicsmithing.item.custom.supers;

import com.thathitmann.runicsmithing.generators.RSDynamicRecipe;
import com.thathitmann.runicsmithing.generators.RSDynamicRecipeRegistry;
import com.thathitmann.runicsmithing.generators.RSRawDynamicRecipe;
import com.thathitmann.runicsmithing.generators.RSRecipeCategory;
import com.thathitmann.runicsmithing.item.ModCreativeTab;
import com.thathitmann.runicsmithing.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.*;

//import java.io.File;
//import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
        RegistryObject<Item> newHotIngot = ModItems.ITEMS.register(fileName, () -> new HotIngotBase(new Item.Properties().tab(ModCreativeTab.TAB), material));




        //New dynamic recipes

        //add forge heating recipe
        RSRawDynamicRecipe newRawRecipe = new RSRawDynamicRecipe(associatedItem, newHotIngot, RSRecipeCategory.FORGE_HEATING, null);
        RSDynamicRecipeRegistry.addNewRawRecipe(newRawRecipe);



        //Old JSON recipe
        /*
        //Get input and output names
        String inputItemNameId = "minecraft:" + material.getAssociatedIngot().toString();
        String outputItemNameId = "runicsmithing:" + fileName;
        //Create recipe json
        String recipeFileContents = "{\"type\": \"runicsmithing:forge_heating\",\"ingredients\": [{\"item\": \"" + inputItemNameId + "\"}],\"output\": {\"item\": \"" + outputItemNameId + "\"}}";
        String recipeFileName = name + "_heating.json";
        try {
            File file = new File("../src/generated/resources/data/runicsmithing/recipes/" + recipeFileName);
            file.createNewFile();
            PrintWriter out = new PrintWriter("../src/generated/resources/data/runicsmithing/recipes/" + recipeFileName);
            out.println(recipeFileContents);
            out.close();
        } catch (Exception e) {
        }*/


    }



}
