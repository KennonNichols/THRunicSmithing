package com.thathitmann.runicsmithing;

//import com.mojang.logging.LogUtils;

import com.thathitmann.runicsmithing.block.ModBlocks;
import com.thathitmann.runicsmithing.block.entity.ModBlockEntities;
import com.thathitmann.runicsmithing.generators.RSDynamicRecipeRegistry;
import com.thathitmann.runicsmithing.item.ModCreativeTab;
import com.thathitmann.runicsmithing.item.ModItems;
import com.thathitmann.runicsmithing.generators.GeneratedItemRegistry;
import com.thathitmann.runicsmithing.item.custom.supers.GeneratableItem;
import com.thathitmann.runicsmithing.item.custom.supers.RunicSmithingMaterial;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.toolModifiers.AspectModifier;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.toolModifiers.ToolModifierStack;
import com.thathitmann.runicsmithing.particle.ModParticles;
import com.thathitmann.runicsmithing.screen.*;
import com.thathitmann.runicsmithing.sound.ModSounds;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.StringUtils;
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
        //Register particles
        ModParticles.register(modEventBus);
        //Register the recipes
        //ModRecipes.register(modEventBus);
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        GeneratedItemRegistry.createTranslationFile();
    }





    private void commonSetup(final FMLCommonSetupEvent ignoredEvent)
    {
        //Cook the recipes on minecraft starting
        RSDynamicRecipeRegistry.cook();
    }


    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == ModCreativeTab.TAB.getKey()) {
            event.accept(ModItems.CHARCOAL_BRIQUETTE);
            event.accept(ModItems.GLOVES);
            event.accept(ModItems.IRON_SMITHING_HAMMER);
            event.accept(ModItems.STONE_SMITHING_HAMMER);
            event.accept(ModItems.TONGS);
            event.accept(ModItems.TABLET);


            //Generated stuff
            for (RunicSmithingMaterial material : RunicSmithingMaterial.getNonNoneMaterials()) {
                for (GeneratableItem item : GeneratedItemRegistry.generatedItems) {
                    CompoundTag tag = new CompoundTag();
                    ItemStack newItemStack = new ItemStack(item.item().get(), 1);
                    tag.putInt("CustomModelData", material.ordinal());

                    //Marking material
                    CompoundTag materialTag = new CompoundTag();
                    materialTag.putInt(ToolModifierStack.QUICKGRAB_MATERIAL_TAG_ID, material.ordinal());
                    tag.put(ToolModifierStack.QUICKGRAB_TAG_ID, materialTag);

                    newItemStack.setTag(tag);
                    newItemStack.setHoverName(Component.literal(String.format(item.formatableName(), StringUtils.capitalize(material.getMaterialName()))));
                    event.accept(newItemStack);
                }
            }




            event.accept(ModBlocks.FORGE_BLOCK);
            event.accept(ModBlocks.CORE_FORGE_BLOCK);
            event.accept(ModBlocks.TOOL_STATION_BLOCK);
            event.accept(ModBlocks.STONE_ANVIL_BLOCK);
            event.accept(ModBlocks.WOODEN_BASIN_BLOCK);




            //Debug sword
            ItemStack newItemStack = new ItemStack(ModItems.FORGED_SWORD.get(), 1);
            ToolModifierStack modifierStack = ToolModifierStack.getBlankInstance();
            for (int i = 0; i < 20; i++) {
                modifierStack.addToolModifier(
                    new AspectModifier("Debug mod " + i, "This is a debug modifier.", i)
                );
            }

            //Adds the three longest modifiers
            modifierStack.addToolModifier(AspectModifier.getDepthAspect(1, true));
            modifierStack.addToolModifier(AspectModifier.getDepthAspect(151, true));
            modifierStack.addToolModifier(AspectModifier.getDepthAspect(301, true));

            newItemStack.getOrCreateTag().put(ToolModifierStack.TOOL_MODIFIER_STACK_TAG_ID, modifierStack.getAsTag());
            ToolModifierStack.loadQuickGrab(newItemStack);
            event.accept(newItemStack);
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
            MenuScreens.register(ModMenuTypes.TOOL_STATION_BLOCK_MENU.get(), ToolStationBlockScreen::new);
            MenuScreens.register(ModMenuTypes.CORE_FORGE_BLOCK_MENU.get(), CoreForgeBlockScreen::new);
            MenuScreens.register(ModMenuTypes.HAMMERING_MENU.get(), HammeringScreen::new);
            MenuScreens.register(ModMenuTypes.RESEARCH_TABLET_MENU.get(), ResearchScreen::new);
        }
    }

}
