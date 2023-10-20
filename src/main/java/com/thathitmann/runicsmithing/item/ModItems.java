package com.thathitmann.runicsmithing.item;

import com.thathitmann.runicsmithing.RunicSmithing;
import com.thathitmann.runicsmithing.item.custom.*;
import com.thathitmann.runicsmithing.item.custom.supers.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
            () -> new IronHammerItem(new Item.Properties().tab(ModCreativeTab.TAB)));
    //Stone Hammer
    public static final RegistryObject<Item> STONE_SMITHING_HAMMER = ITEMS.register("stone_smithing_hammer",
            () -> new StoneHammerItem(new Item.Properties().tab(ModCreativeTab.TAB)));
    //Charcoal Briquette
    public static final RegistryObject<Item> CHARCOAL_BRIQUETTE = ITEMS.register("charcoal_briquette",
            () -> new CharcoalBriquetteItem(new Item.Properties().tab(ModCreativeTab.TAB)) {
                @Override
                public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                    return 400;
                }
            });









    public static void generateLateRegistry() {





        //Copper
        MaterialRegistry.addMaterialToRegistry(Items.COPPER_INGOT,"copper");
        //Iron
        MaterialRegistry.addMaterialToRegistry(Items.IRON_INGOT,"iron");
        //Gold
        MaterialRegistry.addMaterialToRegistry(Items.GOLD_INGOT,"gold");
        //Diamond
        MaterialRegistry.addMaterialToRegistry(Items.DIAMOND,"diamond", "");
        //Netherite
        MaterialRegistry.addMaterialToRegistry(Items.NETHERITE_INGOT,"netherite");



        //ITEMS.register("forged_iron_pickaxe", () -> new Item(new Item.Properties().tab(ModCreativeTab.TAB)));




    }













    public static void register(IEventBus eventBus) {
        generateLateRegistry();
        ITEMS.register(eventBus);
    }
}
