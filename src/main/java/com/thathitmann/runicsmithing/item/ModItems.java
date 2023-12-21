package com.thathitmann.runicsmithing.item;

import com.thathitmann.runicsmithing.RunicSmithing;
import com.thathitmann.runicsmithing.generators.GeneratedItemRegistry;
import com.thathitmann.runicsmithing.item.custom.*;
import com.thathitmann.runicsmithing.item.custom.supers.*;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.HotIngotBase;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.HotToolBase;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.ToolBase;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.forged_tool.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;


public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RunicSmithing.MOD_ID);



    //New generic hot ingot
    public static final RegistryObject<Item> HOT_INGOT = ITEMS.register("hot_ingot", () -> new HotIngotBase(new Item.Properties()));

    //New hot tool bases
    public static final RegistryObject<Item> HOT_PICKAXE = ITEMS.register("hot_pickaxe", () -> new HotToolBase(new Item.Properties()));
    public static final RegistryObject<Item> HOT_AXE = ITEMS.register("hot_axe", () -> new HotToolBase(new Item.Properties()));
    public static final RegistryObject<Item> HOT_SWORD = ITEMS.register("hot_sword", () -> new HotToolBase(new Item.Properties()));
    public static final RegistryObject<Item> HOT_HOE = ITEMS.register("hot_hoe", () -> new HotToolBase(new Item.Properties()));
    public static final RegistryObject<Item> HOT_SHOVEL = ITEMS.register("hot_shovel", () -> new HotToolBase(new Item.Properties()));

    //New hot tool bases
    public static final RegistryObject<Item> BASE_PICKAXE = ITEMS.register("base_pickaxe", () -> new ToolBase(new Item.Properties()));
    public static final RegistryObject<Item> BASE_AXE = ITEMS.register("base_axe", () -> new ToolBase(new Item.Properties()));
    public static final RegistryObject<Item> BASE_SWORD = ITEMS.register("base_sword", () -> new ToolBase(new Item.Properties()));
    public static final RegistryObject<Item> BASE_HOE = ITEMS.register("base_hoe", () -> new ToolBase(new Item.Properties()));
    public static final RegistryObject<Item> BASE_SHOVEL = ITEMS.register("base_shovel", () -> new ToolBase(new Item.Properties()));

    //New forged Tools
    public static final RegistryObject<Item> FORGED_PICKAXE = ITEMS.register("forged_pickaxe", () -> new ForgedPickaxe(new Item.Properties()));
    public static final RegistryObject<Item> FORGED_AXE = ITEMS.register("forged_axe", () -> new ForgedAxe(new Item.Properties()));
    public static final RegistryObject<Item> FORGED_SWORD = ITEMS.register("forged_sword", () -> new ForgedSword(new Item.Properties()));
    public static final RegistryObject<Item> FORGED_HOE = ITEMS.register("forged_hoe", () -> new ForgedHoe(new Item.Properties()));
    public static final RegistryObject<Item> FORGED_SHOVEL = ITEMS.register("forged_shovel", () -> new ForgedShovel(new Item.Properties()));





    //Tablet
    public static final RegistryObject<Item> TABLET = ITEMS.register("research_tablet",
            () -> new ResearchTabletItem(new Item.Properties()));
    //Tongs
    public static final RegistryObject<Item> TONGS = ITEMS.register("tongs",
            () -> new TongsItem(new Item.Properties()));
    //Gloves
    public static final RegistryObject<Item> GLOVES = ITEMS.register("gloves",
            () -> new GlovesItem(new Item.Properties()));
    //Hammer
    public static final RegistryObject<Item> IRON_SMITHING_HAMMER = ITEMS.register("iron_smithing_hammer",
            () -> new IronHammerItem(new Item.Properties()));
    //Stone Hammer
    public static final RegistryObject<Item> STONE_SMITHING_HAMMER = ITEMS.register("stone_smithing_hammer",
            () -> new StoneHammerItem(new Item.Properties()));
    //Charcoal Briquette
    public static final RegistryObject<Item> CHARCOAL_BRIQUETTE = ITEMS.register("charcoal_briquette",
            () -> new CharcoalBriquetteItem(new Item.Properties()) {
                @Override
                public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                    return 400;
                }
            });


















    public static void register(IEventBus eventBus) {
        GeneratedItemRegistry.generatedItems.add(new GeneratableItem(HOT_INGOT, "Hot %s Ingot","hot_ingot","hot_ingot"));
        GeneratedItemRegistry.generatedItems.add(new GeneratableItem(HOT_PICKAXE, "Hot %s Pickaxe","hot_pickaxe","hot_pickaxe"));
        GeneratedItemRegistry.generatedItems.add(new GeneratableItem(HOT_AXE, "Hot %s Axe","hot_axe","hot_axe"));
        GeneratedItemRegistry.generatedItems.add(new GeneratableItem(HOT_HOE, "Hot %s Hoe","hot_hoe","hot_hoe"));
        GeneratedItemRegistry.generatedItems.add(new GeneratableItem(HOT_SWORD, "Hot %s Sword","hot_sword","hot_sword"));
        GeneratedItemRegistry.generatedItems.add(new GeneratableItem(HOT_SHOVEL, "Hot %s Shovel","hot_shovel","hot_shovel"));
        GeneratedItemRegistry.generatedItems.add(new GeneratableItem(BASE_PICKAXE, "Incomplete %s Pickaxe","pickaxe","base_pickaxe"));
        GeneratedItemRegistry.generatedItems.add(new GeneratableItem(BASE_AXE, "Incomplete %s Axe","axe","base_axe"));
        GeneratedItemRegistry.generatedItems.add(new GeneratableItem(BASE_HOE, "Incomplete %s Hoe","hoe","base_hoe"));
        GeneratedItemRegistry.generatedItems.add(new GeneratableItem(BASE_SWORD, "Incomplete %s Sword","sword","base_sword"));
        GeneratedItemRegistry.generatedItems.add(new GeneratableItem(BASE_SHOVEL, "Incomplete %s Shovel","shovel","base_shovel"));
        GeneratedItemRegistry.generatedItems.add(new GeneratableItem(FORGED_PICKAXE, "%s Pickaxe","pickaxe","pickaxe"));
        GeneratedItemRegistry.generatedItems.add(new GeneratableItem(FORGED_AXE, "%s Axe","axe","axe"));
        GeneratedItemRegistry.generatedItems.add(new GeneratableItem(FORGED_HOE, "%s Hoe","hoe","hoe"));
        GeneratedItemRegistry.generatedItems.add(new GeneratableItem(FORGED_SWORD, "%s Sword","sword","sword"));
        GeneratedItemRegistry.generatedItems.add(new GeneratableItem(FORGED_SHOVEL, "%s Shovel","shovel","shovel"));

        ITEMS.register(eventBus);
    }
}
