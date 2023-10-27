package com.thathitmann.runicsmithing;

//import com.mojang.logging.LogUtils;

import com.thathitmann.runicsmithing.block.ModBlocks;
import com.thathitmann.runicsmithing.block.entity.ModBlockEntities;
import com.thathitmann.runicsmithing.generators.RSDynamicRecipeRegistry;
import com.thathitmann.runicsmithing.item.ModCreativeTab;
import com.thathitmann.runicsmithing.item.ModItems;
import com.thathitmann.runicsmithing.generators.GeneratedItemRegistry;
import com.thathitmann.runicsmithing.screen.CoreForgeBlockScreen;
import com.thathitmann.runicsmithing.screen.ForgeBlockScreen;
import com.thathitmann.runicsmithing.screen.HammeringScreen;
import com.thathitmann.runicsmithing.screen.ModMenuTypes;
import com.thathitmann.runicsmithing.sound.ModSounds;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
//import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(RunicSmithing.MOD_ID)
public class RunicSmithing
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "runicsmithing";
    // Directly reference a slf4j logger
    //private static final Logger LOGGER = LogUtils.getLogger();
    public RunicSmithing()
    {
        //Create Event Bus
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();


        //Register tabs
        ModCreativeTab.register(modEventBus);
        //Register items
        ModItems.register(modEventBus);
        //Register blocks
        ModBlocks.register(modEventBus);
        //Register block entities
        ModBlockEntities.register(modEventBus);
        //Register the menus
        ModMenuTypes.register(modEventBus);
        //Register sounds
        ModSounds.register(modEventBus);
        //Register the recipes
        //ModRecipes.register(modEventBus);
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        GeneratedItemRegistry.createTranslationFile();
    }





    private void commonSetup(final FMLCommonSetupEvent event)
    {
        //Cook the recipes on world open
        RSDynamicRecipeRegistry.cook();
    }


    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == ModCreativeTab.TAB.getKey()) {
            for (RegistryObject<Item> item : GeneratedItemRegistry.itemsToAddToCreativeModeTabBecauseOfThisNonsense)
            {
                event.accept(item);
            }
            event.accept(ModItems.CHARCOAL_BRIQUETTE);
            event.accept(ModItems.GLOVES);
            event.accept(ModItems.IRON_SMITHING_HAMMER);
            event.accept(ModItems.STONE_SMITHING_HAMMER);
            event.accept(ModItems.TONGS);

            event.accept(ModBlocks.FORGE_BLOCK);
            event.accept(ModBlocks.CORE_FORGE_BLOCK);
            event.accept(ModBlocks.STONE_ANVIL_BLOCK);
            event.accept(ModBlocks.WOODEN_BASIN_BLOCK);

        }

    }
    public static final TagKey<Item> burningHotTag = ItemTags.create(new ResourceLocation("runicsmithing", "burninghot"));
    public static final TagKey<Item> heatInsulatingTag = ItemTags.create(new ResourceLocation("runicsmithing", "heatinsulating"));

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(ModMenuTypes.FORGE_BLOCK_MENU.get(), ForgeBlockScreen::new);
            MenuScreens.register(ModMenuTypes.CORE_FORGE_BLOCK_MENU.get(), CoreForgeBlockScreen::new);
            MenuScreens.register(ModMenuTypes.HAMMERING_MENU.get(), HammeringScreen::new);
        }
    }

}
