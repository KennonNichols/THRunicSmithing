package com.thathitmann.runicsmithing.generators;

import com.thathitmann.runicsmithing.item.custom.supers.GeneratableItem;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeneratedItemRegistry {




    public static final List<GeneratableItem> generatedItems = new ArrayList<>();

    public static GeneratableItem getGeneratableItem(Item item) {
        for (GeneratableItem generatableItem : generatedItems) {
            if (generatableItem.item().get() == item) {
                return generatableItem;
            }
        }
        return null;
    }


    public static final List<String> itemsToAddToLangFile = new ArrayList<>();

    public static void createTranslationFile() {
        StringBuilder jsonText = new StringBuilder("{");

        //Prebuilt language stuff
        jsonText.append("\"item.runicsmithing.tongs\": \"Tongs\",\n");
        jsonText.append("\"item.runicsmithing.gloves\": \"Forge Gloves\",\n");
        jsonText.append("\"item.runicsmithing.iron_smithing_hammer\": \"Smithing Hammer\",\n");
        jsonText.append("\"item.runicsmithing.stone_smithing_hammer\": \"Stone Smithing Hammer\",\n");
        jsonText.append("\"item.runicsmithing.charcoal_briquette\": \"Charcoal Briquette\",\n");
        jsonText.append("\"block.runicsmithing.forge_block\": \"Smithing Forge\",\n");
        jsonText.append("\"block.runicsmithing.core_forge_block\": \"Core Forge\",\n");
        jsonText.append("\"block.runicsmithing.stone_anvil_block\": \"Stone Anvil\",\n");
        jsonText.append("\"block.runicsmithing.wooden_basin_block\": \"Wooden Basin\",\n");
        jsonText.append("\"creativemodetab:runicsmithing_tab\": \"Runic Smithing\",\n");
        //Subtitles
        jsonText.append("\"sounds.runicsmithing.tong_clicking\": \"Tongs Click\",\n");



        for (int i = 0; i < itemsToAddToLangFile.size(); i++) {
            jsonText.append(itemsToAddToLangFile.get(i));
            if (i < itemsToAddToLangFile.size() - 1) {
                jsonText.append(",\n");
            }
        }
        jsonText.append("}");







        File newTranslation = new File("../src/generated/resources/assets/runicsmithing/lang/en_us.json");
        try {
            newTranslation.createNewFile();
            FileWriter writer = new FileWriter("../src/generated/resources/assets/runicsmithing/lang/en_us.json");
            writer.write(jsonText.toString());
            writer.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
