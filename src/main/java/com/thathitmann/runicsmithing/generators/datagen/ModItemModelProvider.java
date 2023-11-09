package com.thathitmann.runicsmithing.generators.datagen;

import com.thathitmann.runicsmithing.RunicSmithing;
import com.thathitmann.runicsmithing.generators.GeneratedItemRegistry;
import com.thathitmann.runicsmithing.item.custom.supers.GeneratableItem;
import com.thathitmann.runicsmithing.item.custom.supers.RunicSmithingMaterial;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, RunicSmithing.MOD_ID, existingFileHelper);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(RunicSmithing.MOD_ID, "item/" + item.getId().getPath()));
    }
    private ItemModelBuilder simpleItem(RegistryObject<Item> item, String textureName) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0", textureName);
    }

    private ItemModelBuilder materialTrees(RunicSmithingMaterial material, String inFolder, String outFolder) {
        return withExistingParent("item/" + outFolder + "/" + material.getMaterialName(),
                new ResourceLocation("item/generated")).texture("layer0", "runicsmithing:item/" + inFolder + "/" + material.getMaterialName());
    }

    private ItemModelBuilder materialItem(RegistryObject<Item> item, String inFolder, String outFolder) {
        ItemModelBuilder builder = withExistingParent(item.getId().getPath(),new ResourceLocation("item/generated"))
                .texture("layer0", "runicsmithing:item/base/" + inFolder);


        for (RunicSmithingMaterial material : RunicSmithingMaterial.getNonNoneMaterials()) {
            builder = builder.override().model(
                    new ModelFile.UncheckedModelFile(new ResourceLocation("runicsmithing:item/" + outFolder + "/" + material.getMaterialName()))
            ).predicate(new ResourceLocation("custom_model_data"), material.ordinal()).end();
        }
        return builder;
    }

    @Override
    protected void registerModels() {
        for (GeneratableItem item : GeneratedItemRegistry.generatedItems) {
            materialItem(item.item(), item.sourceName(), item.outName());
            for (RunicSmithingMaterial material : RunicSmithingMaterial.getNonNoneMaterials()) {
                materialTrees(material, item.sourceName(), item.outName());
            }
        }
    }
}
