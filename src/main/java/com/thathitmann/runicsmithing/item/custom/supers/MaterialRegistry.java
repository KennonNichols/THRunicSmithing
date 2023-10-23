package com.thathitmann.runicsmithing.item.custom.supers;

import com.thathitmann.runicsmithing.generators.RSDynamicRecipeRegistry;
import com.thathitmann.runicsmithing.generators.RSRawDynamicRecipe;
import com.thathitmann.runicsmithing.generators.RSRecipeCategory;
import com.thathitmann.runicsmithing.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    private static void buildModelJson(String filePath, String fileName) {
        File newModel = new File(filePath + fileName + ".json");
        try {
            newModel.createNewFile();
            FileWriter writer = new FileWriter(filePath + fileName + ".json");
            writer.write(
                    "{\"parent\": \"item/generated\", \"textures\": {\"layer0\": \"runicsmithing:item/" + fileName + "\"}}"
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



        //Add material to list
        materials.add(material);



        String filePath = "../src/generated/resources/assets/runicsmithing/models/item/";



        //region hot ingot
        //creates ingot name
        String fileName = "hot_" + name + suffix;
        //Register the ingot to ModItems
        RegistryObject<Item> newHotIngot = ModItems.ITEMS.register(fileName, () -> new HotIngotBase(new Item.Properties(), material));
        //Add the ingot to the stupid list
        CreativeTabRegistry.itemsToAddToCreativeModeTabBecauseOfThisNonsense.add(newHotIngot);
        //add forge heating recipe
        RSRawDynamicRecipe newRawRecipe = new RSRawDynamicRecipe(associatedItem, newHotIngot, RSRecipeCategory.FORGE_HEATING, null);
        RSDynamicRecipeRegistry.addNewRawRecipe(newRawRecipe);
        //Add item model
        buildModelJson(filePath, fileName);
        //endregion




        //region hot pickaxe
        //creates ingot name
        fileName = "hot_forged_" + name + "_pickaxe";
        //Register the ingot to ModItems
        RegistryObject<Item> newHotPickaxe = ModItems.ITEMS.register(fileName, () -> new SmithingChainItem(new Item.Properties(), material));
        //Add the pickaxe to the stupid list
        CreativeTabRegistry.itemsToAddToCreativeModeTabBecauseOfThisNonsense.add(newHotPickaxe);
        //add forge heating recipe
        newRawRecipe = new RSRawDynamicRecipe(newHotIngot, newHotPickaxe, RSRecipeCategory.SHAPED_HAMMERING, Arrays.asList(RSDynamicRecipeRegistry.PICKAXE_SHAPE));
        RSDynamicRecipeRegistry.addNewRawRecipe(newRawRecipe);
        //Add item model
        buildModelJson(filePath, fileName);
        //endregion


    }



}
