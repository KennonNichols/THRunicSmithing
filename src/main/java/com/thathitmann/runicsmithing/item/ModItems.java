package com.thathitmann.runicsmithing.item;

import com.thathitmann.runicsmithing.RunicSmithing;
import com.thathitmann.runicsmithing.item.custom.*;
import com.thathitmann.runicsmithing.item.custom.supers.HotIngotBase;
import com.thathitmann.runicsmithing.item.custom.supers.Material;
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









    //Tongs
    public static final RegistryObject<Item> TONGS = ITEMS.register("tongs",
            () -> new TongsItem(new Item.Properties().tab(ModCreativeTab.TAB)));
    //Gloves
    public static final RegistryObject<Item> GLOVES = ITEMS.register("gloves",
            () -> new GlovesItem(new Item.Properties().tab(ModCreativeTab.TAB)));
    //Hammer
    public static final RegistryObject<Item> IRON_SMITHING_HAMMER = ITEMS.register("iron_smithing_hammer",
            () -> new Item(new Item.Properties().tab(ModCreativeTab.TAB)));
    //Stone Hammer
    public static final RegistryObject<Item> STONE_SMITHING_HAMMER = ITEMS.register("stone_smithing_hammer",
            () -> new Item(new Item.Properties().tab(ModCreativeTab.TAB)));
    //Charcoal Briquette
    public static final RegistryObject<Item> CHARCOAL_BRIQUETTE = ITEMS.register("charcoal_briquette",
            () -> new CharcoalBriquetteItem(new Item.Properties().tab(ModCreativeTab.TAB)) {
                @Override
                public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                    return 400;
                }
            });
    //Hot copper
    public static final RegistryObject<Item> HOT_COPPER_INGOT = ITEMS.register("hot_copper_ingot",
            () -> new HotIngotBase(new Item.Properties().tab(ModCreativeTab.TAB), Material.materialCopper));
    //Hot iron
    public static final RegistryObject<Item> HOT_IRON_INGOT = ITEMS.register("hot_iron_ingot",
            () -> new HotIngotBase(new Item.Properties().tab(ModCreativeTab.TAB), Material.materialIron));
    //Hot gold
    public static final RegistryObject<Item> HOT_GOLD_INGOT = ITEMS.register("hot_gold_ingot",
            () -> new HotIngotBase(new Item.Properties().tab(ModCreativeTab.TAB), Material.materialGold));
    //Hot netherite
    public static final RegistryObject<Item> HOT_NETHERITE_INGOT = ITEMS.register("hot_netherite_ingot",
            () -> new HotIngotBase(new Item.Properties().tab(ModCreativeTab.TAB), Material.materialNetherite));
    //Hot diamond
    public static final RegistryObject<Item> HOT_DIAMOND = ITEMS.register("hot_diamond",
            () -> new HotIngotBase(new Item.Properties().tab(ModCreativeTab.TAB), Material.materialDiamond));





















    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
