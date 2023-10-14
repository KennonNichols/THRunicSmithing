package com.thathitmann.runicsmithing.block;

import com.thathitmann.runicsmithing.RunicSmithing;
import com.thathitmann.runicsmithing.block.custom.CoreForgeBlock;
import com.thathitmann.runicsmithing.block.custom.ForgeBlock;
import com.thathitmann.runicsmithing.item.ModCreativeTab;
import com.thathitmann.runicsmithing.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    //region Bookkeeping and registry
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, RunicSmithing.MOD_ID);

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return  toReturn;
    }

    private static <T extends  Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
    //endregion


    //private static final BlockBehaviour.Properties forgeProperties = new BlockBehaviour.Properties(Material.AIR);

    public static final RegistryObject<Block> FORGE_BLOCK = registerBlock("forge_block",
        () -> new ForgeBlock(BlockBehaviour.Properties.of(Material.STONE).strength(6f).requiresCorrectToolForDrops().lightLevel(state -> state.getValue(ForgeBlock.LIT) ? 15 : 0)),
            ModCreativeTab.TAB);

    public static final RegistryObject<Block> CORE_FORGE_BLOCK = registerBlock("core_forge_block",
            () -> new CoreForgeBlock(BlockBehaviour.Properties.of(Material.STONE).strength(6f).requiresCorrectToolForDrops().lightLevel(state -> state.getValue(CoreForgeBlock.LIT) ? 15 : 0)),
            ModCreativeTab.TAB);


    /*
    public static final RegistryObject<Block> CARVING_TABLE_BLOCK = registerBlock("carving_table_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(6f).requiresCorrectToolForDrops()),
            ModCreativeTab.TAB);
    public static final RegistryObject<Block> WRAPPING_TABLE_BLOCK = registerBlock("wrapping_table_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(6f).requiresCorrectToolForDrops()),
            ModCreativeTab.TAB);
     */



}
