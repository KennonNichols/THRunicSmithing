package com.thathitmann.runicsmithing.item.custom.supers;

import com.thathitmann.runicsmithing.generators.GeneratedItemRegistry;
import com.thathitmann.runicsmithing.generators.RSDynamicRecipeRegistry;
import com.thathitmann.runicsmithing.generators.RSRawDynamicRecipe;
import com.thathitmann.runicsmithing.generators.RSRecipeCategory;
import com.thathitmann.runicsmithing.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class MaterialRegistry {
    private final static HashMap<String, RunicSmithingMaterial> materials = new HashMap<>();
    private static final String filePath = "../src/generated/resources/assets/runicsmithing/models/item/";

    public static RunicSmithingMaterial getMaterialFromRegistry(String materialKey) {
        return materials.get(materialKey);
    }


    public static void addMaterialToRegistry(Item associatedItem, String name) {
        addMaterialToRegistry(associatedItem, name, "_ingot", false);
    }
    public static void addMaterialToRegistry(Item associatedItem, String name, Boolean isPrimitive) {
        addMaterialToRegistry(associatedItem, name, "_ingot", isPrimitive);
    }
    public static void addMaterialToRegistry(Item associatedItem, String name, String suffix) {
        addMaterialToRegistry(associatedItem, name, suffix, false);
    }

    private static void buildModelJson(String fileName) {
        buildModelJson(fileName, fileName);
    }


    private static void buildModelJson(String fileName, String textureName) {
        File newModel = new File(filePath + fileName + ".json");
        try {
            newModel.createNewFile();
            FileWriter writer = new FileWriter(filePath + fileName + ".json");
            writer.write(
                    "{\"parent\": \"item/generated\", \"textures\": {\"layer0\": \"runicsmithing:item/" + textureName + "\"}}"
            );
            writer.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }



    public static void addMaterialToRegistry(Item associatedItem, String name, String suffix, Boolean isPrimitive) {
        //Creates material
        RunicSmithingMaterial material = new RunicSmithingMaterial(associatedItem, name, isPrimitive);



        //Add material to registry
        materials.put(name, material);





        //Create the hot ingot
        //region hot ingot
        //creates ingot name
        String fileName = "hot_" + name + suffix;
        //Register the ingot to ModItems
        RegistryObject<Item> newHotIngot = ModItems.ITEMS.register(fileName, () -> new HotIngotBase(new Item.Properties(), material));
        //Add the ingot to the stupid list
        GeneratedItemRegistry.itemsToAddToCreativeModeTabBecauseOfThisNonsense.add(newHotIngot);
        //Add it to translation list
        GeneratedItemRegistry.itemsToAddToLangFile.add("\"item.runicsmithing." + fileName + "\": \"Hot " + StringUtils.capitalize(name) + "\"");
        //add forge heating recipe
        RSRawDynamicRecipe newRawRecipe = new RSRawDynamicRecipe(associatedItem, newHotIngot, RSRecipeCategory.FORGE_HEATING, null);
        RSDynamicRecipeRegistry.addNewRawRecipe(newRawRecipe);
        //Add item model
        buildModelJson(fileName);
        //endregion


        for (ToolType type : ToolType.values()) {
            createToolComponents(type, newHotIngot, material);
        }




    }


    private static void createToolComponents(ToolType type, RegistryObject<Item> hotIngot, RunicSmithingMaterial material) {
        //region hot tool
        //creates tool name
        String fileName = "hot_forged_" + material.getMaterialName() + "_" + type.getName();
        //Register the tool to ModItems
        RegistryObject<Item> newHotTool = ModItems.ITEMS.register(fileName, () -> new HotForgedToolBase(new Item.Properties(), material));
        //Add the tool to the stupid list
        GeneratedItemRegistry.itemsToAddToCreativeModeTabBecauseOfThisNonsense.add(newHotTool);
        //Add it to translation list
        GeneratedItemRegistry.itemsToAddToLangFile.add(String.format("\"item.runicsmithing.%s\": \"Hot %s %s\"", fileName, StringUtils.capitalize(material.getMaterialName()), type.getCapitalizedName()));
        //add hammering recipe
        RSDynamicRecipeRegistry.addNewRawRecipe(new RSRawDynamicRecipe(hotIngot, newHotTool, RSRecipeCategory.SHAPED_HAMMERING, Arrays.asList(type.getShape())));
        //Add item model
        buildModelJson(fileName);
        //endregion


        //region base tool
        //creates tool name
        fileName = "base_forged_" + material.getMaterialName() + "_" + type.getName();
        //Register the tool to ModItems
        RegistryObject<Item> newBaseTool = ModItems.ITEMS.register(fileName, () -> new ForgedToolBase(new Item.Properties(), material, type));
        //Add the tool to the stupid list
        GeneratedItemRegistry.itemsToAddToCreativeModeTabBecauseOfThisNonsense.add(newBaseTool);
        //Add it to translation list
        GeneratedItemRegistry.itemsToAddToLangFile.add(String.format("\"item.runicsmithing.%s\": \"Base %s %s\"", fileName, StringUtils.capitalize(material.getMaterialName()), type.getCapitalizedName()));
        //add forge heating recipe
        RSDynamicRecipeRegistry.addNewRawRecipe(new RSRawDynamicRecipe(newHotTool, newBaseTool, RSRecipeCategory.QUENCHING, null));
        //Add item model
        buildModelJson(fileName,"forged_" + material.getMaterialName() + "_" + type.getName());
        //endregion


        //Adds heating recipe
        RSDynamicRecipeRegistry.addNewRawRecipe(new RSRawDynamicRecipe(newBaseTool, newHotTool, RSRecipeCategory.FORGE_HEATING, null));
    }




}
