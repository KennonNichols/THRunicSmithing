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

    private static void buildModelJson(String filePath, String fileName) {
        buildModelJson(filePath, fileName, fileName);
    }


    private static void buildModelJson(String filePath, String fileName, String textureName) {
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



        String filePath = "../src/generated/resources/assets/runicsmithing/models/item/";



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
        buildModelJson(filePath, fileName);
        //endregion

        //region hot pickaxe
        //creates ingot name
        fileName = "hot_forged_" + name + "_pickaxe";
        //Register the ingot to ModItems
        RegistryObject<Item> newHotPickaxe = ModItems.ITEMS.register(fileName, () -> new HotForgedToolBase(new Item.Properties(), material));
        //Add the pickaxe to the stupid list
        GeneratedItemRegistry.itemsToAddToCreativeModeTabBecauseOfThisNonsense.add(newHotPickaxe);
        //Add it to translation list
        GeneratedItemRegistry.itemsToAddToLangFile.add("\"item.runicsmithing." + fileName + "\": \"Hot " + StringUtils.capitalize(name) + " Pickaxe\"");
        //add forge heating recipe
        newRawRecipe = new RSRawDynamicRecipe(newHotIngot, newHotPickaxe, RSRecipeCategory.SHAPED_HAMMERING, Arrays.asList(RSDynamicRecipeRegistry.PICKAXE_SHAPE));
        RSDynamicRecipeRegistry.addNewRawRecipe(newRawRecipe);
        //Add item model
        buildModelJson(filePath, fileName);
        //endregion

        //region hot axe
        //creates ingot name
        fileName = "hot_forged_" + name + "_axe";
        //Register the ingot to ModItems
        RegistryObject<Item> newHotAxe = ModItems.ITEMS.register(fileName, () -> new HotForgedToolBase(new Item.Properties(), material));
        //Add the axe to the stupid list
        GeneratedItemRegistry.itemsToAddToCreativeModeTabBecauseOfThisNonsense.add(newHotAxe);
        //Add it to translation list
        GeneratedItemRegistry.itemsToAddToLangFile.add("\"item.runicsmithing." + fileName + "\": \"Hot " + StringUtils.capitalize(name) + " Axe\"");
        //add forge heating recipe
        newRawRecipe = new RSRawDynamicRecipe(newHotIngot, newHotAxe, RSRecipeCategory.SHAPED_HAMMERING, Arrays.asList(RSDynamicRecipeRegistry.AXE_SHAPE));
        RSDynamicRecipeRegistry.addNewRawRecipe(newRawRecipe);
        //Add item model
        buildModelJson(filePath, fileName);
        //endregion

        //region hot shovel
        //creates ingot name
        fileName = "hot_forged_" + name + "_shovel";
        //Register the ingot to ModItems
        RegistryObject<Item> newHotShovel = ModItems.ITEMS.register(fileName, () -> new HotForgedToolBase(new Item.Properties(), material));
        //Add the pickaxe to the stupid list
        GeneratedItemRegistry.itemsToAddToCreativeModeTabBecauseOfThisNonsense.add(newHotShovel);
        //Add it to translation list
        GeneratedItemRegistry.itemsToAddToLangFile.add("\"item.runicsmithing." + fileName + "\": \"Hot " + StringUtils.capitalize(name) + " Shovel\"");
        //add forge heating recipe
        newRawRecipe = new RSRawDynamicRecipe(newHotIngot, newHotShovel, RSRecipeCategory.SHAPED_HAMMERING, Arrays.asList(RSDynamicRecipeRegistry.SHOVEL_SHAPE));
        RSDynamicRecipeRegistry.addNewRawRecipe(newRawRecipe);
        //Add item model
        buildModelJson(filePath, fileName);
        //endregion

        //region hot hoe
        //creates ingot name
        fileName = "hot_forged_" + name + "_hoe";
        //Register the ingot to ModItems
        RegistryObject<Item> newHotHoe = ModItems.ITEMS.register(fileName, () -> new HotForgedToolBase(new Item.Properties(), material));
        //Add the pickaxe to the stupid list
        GeneratedItemRegistry.itemsToAddToCreativeModeTabBecauseOfThisNonsense.add(newHotHoe);
        //Add it to translation list
        GeneratedItemRegistry.itemsToAddToLangFile.add("\"item.runicsmithing." + fileName + "\": \"Hot " + StringUtils.capitalize(name) + " Hoe\"");
        //add forge heating recipe
        newRawRecipe = new RSRawDynamicRecipe(newHotIngot, newHotHoe, RSRecipeCategory.SHAPED_HAMMERING, Arrays.asList(RSDynamicRecipeRegistry.HOE_SHAPE));
        RSDynamicRecipeRegistry.addNewRawRecipe(newRawRecipe);
        //Add item model
        buildModelJson(filePath, fileName);
        //endregion

        //region hot sword
        //creates ingot name
        fileName = "hot_forged_" + name + "_sword";
        //Register the ingot to ModItems
        RegistryObject<Item> newHotSword = ModItems.ITEMS.register(fileName, () -> new HotForgedToolBase(new Item.Properties(), material));
        //Add the pickaxe to the stupid list
        GeneratedItemRegistry.itemsToAddToCreativeModeTabBecauseOfThisNonsense.add(newHotSword);
        //Add it to translation list
        GeneratedItemRegistry.itemsToAddToLangFile.add("\"item.runicsmithing." + fileName + "\": \"Hot " + StringUtils.capitalize(name) + " Sword\"");
        //add forge heating recipe
        newRawRecipe = new RSRawDynamicRecipe(newHotIngot, newHotSword, RSRecipeCategory.SHAPED_HAMMERING, Arrays.asList(RSDynamicRecipeRegistry.SWORD_SHAPE));
        RSDynamicRecipeRegistry.addNewRawRecipe(newRawRecipe);
        //Add item model
        buildModelJson(filePath, fileName);
        //endregion

        //region base pickaxe
        //creates ingot name
        fileName = "base_forged_" + name + "_pickaxe";
        //Register the ingot to ModItems
        RegistryObject<Item> newBasePickaxe = ModItems.ITEMS.register(fileName, () -> new ForgedToolBase(new Item.Properties(), material));
        //Add the pickaxe to the stupid list
        GeneratedItemRegistry.itemsToAddToCreativeModeTabBecauseOfThisNonsense.add(newBasePickaxe);
        //Add it to translation list
        GeneratedItemRegistry.itemsToAddToLangFile.add("\"item.runicsmithing." + fileName + "\": \"Base " + StringUtils.capitalize(name) + " Pickaxe\"");
        //add forge heating recipe
        newRawRecipe = new RSRawDynamicRecipe(newHotPickaxe, newBasePickaxe, RSRecipeCategory.QUENCHING, null);
        RSDynamicRecipeRegistry.addNewRawRecipe(newRawRecipe);
        //Add item model
        buildModelJson(filePath, fileName,"forged_" + name + "_pickaxe");
        //endregion

        //region base axe
        //creates ingot name
        fileName = "base_forged_" + name + "_axe";
        //Register the ingot to ModItems
        RegistryObject<Item> newBaseAxe = ModItems.ITEMS.register(fileName, () -> new ForgedToolBase(new Item.Properties(), material));
        //Add the axe to the stupid list
        GeneratedItemRegistry.itemsToAddToCreativeModeTabBecauseOfThisNonsense.add(newBaseAxe);
        //Add it to translation list
        GeneratedItemRegistry.itemsToAddToLangFile.add("\"item.runicsmithing." + fileName + "\": \"Base " + StringUtils.capitalize(name) + " Axe\"");
        //add forge heating recipe
        newRawRecipe = new RSRawDynamicRecipe(newHotAxe, newBaseAxe, RSRecipeCategory.QUENCHING, null);
        RSDynamicRecipeRegistry.addNewRawRecipe(newRawRecipe);
        //Add item model
        buildModelJson(filePath, fileName,"forged_" + name + "_axe");
        //endregion

        //region base shovel
        //creates ingot name
        fileName = "base_forged_" + name + "_shovel";
        //Register the ingot to ModItems
        RegistryObject<Item> newBaseShovel = ModItems.ITEMS.register(fileName, () -> new ForgedToolBase(new Item.Properties(), material));
        //Add the pickaxe to the stupid list
        GeneratedItemRegistry.itemsToAddToCreativeModeTabBecauseOfThisNonsense.add(newBaseShovel);
        //Add it to translation list
        GeneratedItemRegistry.itemsToAddToLangFile.add("\"item.runicsmithing." + fileName + "\": \"Base " + StringUtils.capitalize(name) + " Shovel\"");
        //add forge heating recipe
        newRawRecipe = new RSRawDynamicRecipe(newHotShovel, newBaseShovel, RSRecipeCategory.QUENCHING, null);
        RSDynamicRecipeRegistry.addNewRawRecipe(newRawRecipe);
        //Add item model
        buildModelJson(filePath, fileName,"forged_" + name + "_shovel");
        //endregion

        //region base hoe
        //creates ingot name
        fileName = "base_forged_" + name + "_hoe";
        //Register the ingot to ModItems
        RegistryObject<Item> newBaseHoe = ModItems.ITEMS.register(fileName, () -> new ForgedToolBase(new Item.Properties(), material));
        //Add the pickaxe to the stupid list
        GeneratedItemRegistry.itemsToAddToCreativeModeTabBecauseOfThisNonsense.add(newBaseHoe);
        //Add it to translation list
        GeneratedItemRegistry.itemsToAddToLangFile.add("\"item.runicsmithing." + fileName + "\": \"Base " + StringUtils.capitalize(name) + " Hoe\"");
        //add forge heating recipe
        newRawRecipe = new RSRawDynamicRecipe(newHotHoe, newBaseHoe, RSRecipeCategory.QUENCHING, null);
        RSDynamicRecipeRegistry.addNewRawRecipe(newRawRecipe);
        //Add item model
        buildModelJson(filePath, fileName,"forged_" + name + "_hoe");
        //endregion

        //region base sword
        //creates ingot name
        fileName = "base_forged_" + name + "_sword";
        //Register the ingot to ModItems
        RegistryObject<Item> newBaseSword = ModItems.ITEMS.register(fileName, () -> new ForgedToolBase(new Item.Properties(), material));
        //Add the pickaxe to the stupid list
        GeneratedItemRegistry.itemsToAddToCreativeModeTabBecauseOfThisNonsense.add(newBaseSword);
        //Add it to translation list
        GeneratedItemRegistry.itemsToAddToLangFile.add("\"item.runicsmithing." + fileName + "\": \"Base " + StringUtils.capitalize(name) + " Sword\"");
        //add forge heating recipe
        newRawRecipe = new RSRawDynamicRecipe(newHotSword, newBaseSword, RSRecipeCategory.QUENCHING, null);
        RSDynamicRecipeRegistry.addNewRawRecipe(newRawRecipe);
        //Add item model
        buildModelJson(filePath, fileName,"forged_" + name + "_sword");
        //endregion

        //region reheating
        newRawRecipe = new RSRawDynamicRecipe(newBasePickaxe, newHotPickaxe, RSRecipeCategory.FORGE_HEATING, null);
        RSDynamicRecipeRegistry.addNewRawRecipe(newRawRecipe);
        newRawRecipe = new RSRawDynamicRecipe(newBaseAxe, newHotAxe, RSRecipeCategory.FORGE_HEATING, null);
        RSDynamicRecipeRegistry.addNewRawRecipe(newRawRecipe);
        newRawRecipe = new RSRawDynamicRecipe(newBaseHoe, newHotHoe, RSRecipeCategory.FORGE_HEATING, null);
        RSDynamicRecipeRegistry.addNewRawRecipe(newRawRecipe);
        newRawRecipe = new RSRawDynamicRecipe(newBaseShovel, newHotShovel, RSRecipeCategory.FORGE_HEATING, null);
        RSDynamicRecipeRegistry.addNewRawRecipe(newRawRecipe);
        newRawRecipe = new RSRawDynamicRecipe(newBaseSword, newHotSword, RSRecipeCategory.FORGE_HEATING, null);
        RSDynamicRecipeRegistry.addNewRawRecipe(newRawRecipe);
        //endregion





    }



}
